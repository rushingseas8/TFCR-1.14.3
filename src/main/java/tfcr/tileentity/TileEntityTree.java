package tfcr.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.gen.feature.template.ITemplateProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.blocks.BlockBranch;
import tfcr.blocks.BlockLeaves;
import tfcr.blocks.BlockLog;
import tfcr.blocks.BlockSapling;
import tfcr.blocks.BlockTallSapling;
import tfcr.data.WoodType;
import tfcr.utils.TemplateHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class TileEntityTree extends TileEntity implements ITickable {

    /**
     * The type of wood this TileEntityTree corresponds to.
     */
    private WoodType woodType;

    /**
     * How old this tree is, in ticks. We increment the structure-changing age
     * only once every N ticks. TODO: explain what N is, right now it's 100 for testing
     */
    private int count = 0;

    /**
     * How old this tree is. Used to determine what structure it will use.
     */
    private int age = 0;

    /**
     * A flag to determine if this tree is done growing or not. True if we have hit
     * the age limit for this tree type/variation. If true, this TileEntity will
     * stop ticking.
     */
    private boolean doneGrowing = false;

    /**
     * The variant determines which specific tree model we use at each growth stage.
     * It is randomly chosen on the first tick of the sapling being placed down.
     */
    private int variant = -1;


    private static TileEntityType<TileEntityTree> TREE;

    /**
     * Called during registry event for this mod (specifically, in ModBlocks). Registers this TileEntity.
     * @param tileEntityRegistry A reference to the TileEntity registry.
     */
    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        TREE = TileEntityType.register(TFCR.MODID + ":tile_entity.tree", TileEntityType.Builder.create(TileEntityTree::new));
    }

    // Used internally to register the TileEntity. Should not be called externally.
    private TileEntityTree() {
        super(TREE);
        this.woodType = null;
    }

    public TileEntityTree(@Nonnull WoodType woodType) {
        super(TREE);
        this.woodType = woodType;
    }

    /**
     * Called when this TileEntity is removed from the world.
     *
     * The Block that contains this TileEntity is responsible for logic relating to keeping this TE or
     * generating a new one. This method specifically handles removing additional blocks that are part
     * of this tree's structure.
     */
    @Override
    public void remove() {
        super.remove();

        // Only delete on server side
        if (world.isRemote) {
            return;
        }

        // Get the Template for this current stage
        Template template = TemplateHelper.getTemplate(world, getTemplateLocation());
        if (template == null) {
            System.out.println("Failed to remove additional blocks- template " + getTemplateLocation() + " could not be found.");
            return;
        }

        cleanupTree(template);
    }

    @Override
    public void tick() {
        if (variant == -1 && hasWorld() && !world.isRemote) {
            variant = world.getRandom().nextInt(2);
            System.out.println("Initializing variant to: " + variant);
        }

        if (doneGrowing) {
            return;
        }

        // TODO call markDirty() somewhat often so tree progress gets properly saved
        count++;
        if (count % 100 == 0) {
            if (!world.isRemote) {

                System.out.println("Tile entity tree tick. Age = " + age);

                // TODO Check if we can grow up first.

                // Remove old template
                if (age > 0 && age <= BlockSapling.getMaxAge()) {
                    Template template = TemplateHelper.getTemplate(world, getTemplateLocation());
                    if (template != null) {
                        cleanupTree(template);
                    }
                }

                age++;
                if (age >= BlockSapling.getMaxAge()) {
                    System.out.println("Done growing now");
                    age = BlockSapling.getMaxAge();
                    doneGrowing = true;
                }

                // Grow new template
                // TODO this is a relatively slow method call- maybe find a way to schedule it?
                spawnTree();

                // Mark this TileEntity as dirty, so it saves its metadata to disk
                markDirty();
            }
        }
    }

    private String getTemplateLocation() {
        return this.woodType.getName() + "/age_" + age;
    }

    private void spawnTree() {
        System.out.println("Trying to spawn structure at pos: " + pos);
        // TODO try to cleanup old structure before spawning new one
        // TODO ensure that we can place the new structure down before adding it

        // Access the Template for the tree's structure
        Template template = TemplateHelper.getTemplate(world, getTemplateLocation());
        if (template == null) {
            System.out.println("Failed to find structure: " + getTemplateLocation());
            return;
        }

        // Rotation center should be the trunk of the tree.
        BlockPos size = template.getSize();
        BlockPos center = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);

        // Add the structure to the world.
        // TODO maybe make variant determine a random rotation/mirroring.
        // TODO instead of just adding structure, modify existing leaf blocks!!
        PlacementSettings settings = new PlacementSettings().setCenterOffset(center);
        template.addBlocksToWorld(world, pos.add(-center.getX(), 0, -center.getZ()), new TemplateProcessorTrees(this.woodType), settings, 2);
//        template.addBlocksToWorld(world, pos.add(-center.getX(), 0, -center.getZ()), settings);
        System.out.println("Successfully added blocks.");

        // Validate the tree after it is added into the world, by making sure
        // there is a TileEntityTree after the structure blocks are placed down.
        IBlockState newRoot = world.getBlockState(pos);
        Block blockType = newRoot.getBlock();
        TileEntityTree tileEntityTree = ((TileEntityTree) world.getTileEntity(pos));

        if (blockType instanceof BlockSapling) {
            // Valid, for now do nothing else
        } else if (blockType instanceof BlockTallSapling) {
            // Valid, for now do nothing else
        } else if (blockType instanceof BlockBranch) {
            // Ensure this is a root
            System.out.println("Setting block type to BlockBranch. Ensuring it is root.");
            world.setBlockState(pos, newRoot.with(BlockBranch.ROOT, true));
        } else if (blockType instanceof BlockLog) {
            // Valid, for now do nothing else
        } else {
            // Some other block was generated which can't support a TileEntityTree.
            // This means there's a bug somewhere in the code, so we report it.
            // Either way, we return, since there won't be a TileEntityTree to modify.
            // TODO crash? Log error?
            System.out.println("TileEntityTree created invalid trunk block: " + newRoot.getBlock().toString());
            return;
        }

        // Ensure root block has proper TileEntity
        if (tileEntityTree == null) {
            System.out.println("No TE found for root block: " + newRoot.getBlock().toString());
            return;
        }

        // Update the TileEntity there, since it is recreated if the Block type changes.
        // TODO update all other values.
        tileEntityTree.age = this.age;
        tileEntityTree.variant = this.variant;
        tileEntityTree.count = 0;
        System.out.println("Updated base of tree to have TileEntity.");
    }

    private void cleanupTree(Template template) {
        if (template == null) {
            System.out.println("Failed to cleanup tree. Template was null!");
            return;
        }

        // Get a map of BlockPos : IBlockState, so we can query the template.
        Map<BlockPos, IBlockState> posToStateMap = TemplateHelper.getBlockMap(template);

        BlockPos size = template.getSize();
        Vec3i centerOffset = new Vec3i(-size.getX() / 2, 0, -size.getZ() / 2);
        for (int x = 0; x < size.getX(); x++) {
            for (int z = 0; z < size.getZ(); z++) {
                for (int y = 0; y < size.getY(); y++) {
                    Vec3i offset = new Vec3i(x, y, z);

                    // Get the blockstate for this position in the template. If it is not a branch or leaf,
                    // then we don't care about it for removal purposes. This prevents deleting neighbors.
                    // If we couldn't access the Template's internals, then this block never gets hit.
                    if (posToStateMap != null) {
                        IBlockState templateState = posToStateMap.get(new BlockPos(offset));
                        if (templateState == null || !(
                                (templateState.getBlock() instanceof BlockBranch) ||
                                        (templateState.getBlock() instanceof BlockLog) ||
                                        (templateState.getBlock() instanceof BlockLeaves))) {
                            continue;
                        }
                    }

                    BlockPos worldPos = pos.add(centerOffset).add(offset);

                    IBlockState localState = world.getBlockState(worldPos);
                    Block localBlock = localState.getBlock();

                    if (localBlock instanceof BlockBranch || localBlock instanceof BlockLog) {
                        // TODO check wood type matches structure
                        WoodType woodType = localBlock instanceof BlockBranch ?
                                ((BlockBranch) localBlock).woodType :
                                ((BlockLog) localBlock).woodType;
                        // We don't want to modify other trees' branches!
                        if (woodType != this.woodType) {
                            continue;
                        }

                        // Don't remove TileEntityTrees- that could break growing up mechanics.
                        if (world.getTileEntity(worldPos) != null && world.getTileEntity(worldPos) instanceof TileEntityTree) {
                            continue;
                        }

                        // Otherwise, just remove branches and logs
                        world.removeBlock(worldPos);
                    } else if (localBlock instanceof BlockLeaves) {
                        WoodType woodType = ((BlockLeaves) localBlock).woodType;
                        // We don't want to modify other trees' leaves!
                        if (woodType != this.woodType) {
                            continue;
                        }

                        int numTrees = localState.get(BlockLeaves.NUM_TREES);
                        if (numTrees == 1) {
                            // Leaves are only part of this tree, so remove
                            world.removeBlock(worldPos);
                        } else if (numTrees > 1) {
                            // Leaves are part of >1 tree, so decrement count
                            world.setBlockState(worldPos, localState.with(BlockLeaves.NUM_TREES, numTrees - 1));
                        }
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        compound = super.write(compound);
        compound.setInt("woodTypeInt", woodType.ordinal());
        compound.setInt("age", age);
        compound.setInt("count", count);
        compound.setBoolean("doneGrowing", doneGrowing);
        compound.setInt("variant", variant);
//        System.out.println("Wrote age: " + age);
        return compound;
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        this.woodType = WoodType.values()[compound.getInt("woodTypeInt")];
        this.age = compound.getInt("age");
        this.count = compound.getInt("count");
        this.doneGrowing = compound.getBoolean("doneGrowing");
        this.variant = compound.getInt("variant");
//        System.out.println("Read type: " + age);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        System.out.println("getUpdatePacket");

        NBTTagCompound nbtTag = new NBTTagCompound();
        this.write(nbtTag);
        return new SPacketUpdateTileEntity(this.pos, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        System.out.println("onDataPacket");
        read(packet.getNbtCompound());
    }

    private class TemplateProcessorTrees implements ITemplateProcessor {

        private WoodType woodType;

        public TemplateProcessorTrees(WoodType woodType) {
            this.woodType = woodType;
        }

        @Nullable
        @Override
        public Template.BlockInfo processBlock(IBlockReader worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
            // If the world pos is a leaf block AND so is the block we are going to place,
            // then we need to do additional processing.
            IBlockState currentBlockState = worldIn.getBlockState(pos);
            IBlockState placingBlockState = blockInfoIn.blockState;
            if ((currentBlockState.getBlock() instanceof BlockLeaves) && (placingBlockState.getBlock() instanceof BlockLeaves)) {
                // If the woodtype is different, then we don't replace leaves
                WoodType theirWoodType = ((BlockLeaves) currentBlockState.getBlock()).woodType;
                if (this.woodType != theirWoodType) {
                    return null;
                }

                // Else if the woodtype is the same, we increment the numTree count.
                int numTrees = currentBlockState.get(BlockLeaves.NUM_TREES);
                return new Template.BlockInfo(pos, currentBlockState.with(BlockLeaves.NUM_TREES, numTrees + 1), blockInfoIn.tileentityData);
            }

            // Ensure that we always place leaves with numTrees at least 1.
            if (placingBlockState.getBlock() instanceof BlockLeaves) {
                if (placingBlockState.get(BlockLeaves.NUM_TREES) == 0) {
                    return new Template.BlockInfo(pos, placingBlockState.with(BlockLeaves.NUM_TREES, 1), blockInfoIn.tileentityData);
                }
            }

            // Default- place whatever we were going to place.
            return blockInfoIn;
        }
    }

}
