package tfcr.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.WorldServer;
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
import tfcr.init.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TileEntityTree extends TileEntity implements ITickable {
    public static TileEntityType<TileEntityTree> TREE;

    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        TREE = TileEntityType.register(TFCR.MODID + ":tile_entity.tree", TileEntityType.Builder.create(TileEntityTree::new));
        //tileEntityRegistry.register();
    }

    public TileEntityTree() {
        super(TREE);
        System.out.println("TileEntityTree init");
    }

    // TODO add wood type

    // Local count of ticks. We only take action once every N ticks.
    private int count = 0;

    // How old this TileEntity is. Later will be used to determine tree shape.
    private int age = 0;

    // Are we done growing? True when we hit age cap, or this variant of tree
    // cannot grow any further in the current constraints.
    // Currently makes this stop ticking forever; later should be rarely ticked.
    private boolean doneGrowing = false;

    /**
     * The variant determines which specific tree model we use at each growth stage.
     * It is randomly chosen on the first tick of the sapling being placed down.
     */
    private int variant = -1;

    @Override
    public void remove() {
        super.remove();

        // Only delete on server side
        if (world.isRemote) {
            return;
        }

        // Get the Template for this current stage
        String name = "oak_age_" + age;
        Template template = getTemplate(name);
        if (template == null) {
            System.out.println("Failed to remove additional blocks- template " + name + " could not be found.");
            return;
        }

        // Get a map of BlockPos : IBlockState, so we can query the template.
        Map<BlockPos, IBlockState> posToStateMap = getBlockMap(template);

        // Remove branches that are specifically part of this Template
        removeBranches(template, posToStateMap);

        // Remove leaves that are part of this Template, or notify them of tree removal
        removeLeaves(template, posToStateMap);
    }

    private void removeBranches(Template template, Map<BlockPos, IBlockState> posToStateMap) {
        BlockPos size = template.getSize();
        Vec3i centerOffset = new Vec3i(-size.getX() / 2, 0, -size.getZ() / 2);
        for (int x = 0; x < size.getX(); x++) {
            for (int z = 0; z < size.getZ(); z++) {
                for (int y = 0; y < size.getY(); y++) {
                    Vec3i offset = new Vec3i(x, y, z);

                    // Get the blockstate for this position in the template. If it is not a branch, then
                    // we don't care about it for removal purposes. This prevents deleting too much.
                    // If we couldn't access the Template's internals, then this block never gets hit.
                    IBlockState templateState = posToStateMap.get(new BlockPos(offset));
                    if (templateState == null || !((templateState.getBlock() instanceof BlockBranch) || (templateState.getBlock() instanceof BlockLog))) {
                        continue;
                    }

                    BlockPos worldPos = pos.add(centerOffset).add(offset);

                    IBlockState localState = world.getBlockState(worldPos);
                    Block localBlock = localState.getBlock();

                    if (localBlock instanceof BlockBranch || localBlock instanceof BlockLog) {
                        // Just remove branches and logs
                        world.removeBlock(worldPos);
                    }
                }
            }
        }
    }

    private void removeLeaves(Template template, Map<BlockPos, IBlockState> posToStateMap) {
        BlockPos size = template.getSize();
        Vec3i centerOffset = new Vec3i(-size.getX() / 2, 0, -size.getZ() / 2);
        for (int x = 0; x < size.getX(); x++) {
            for (int z = 0; z < size.getZ(); z++) {
                for (int y = 0; y < size.getY(); y++) {
                    Vec3i offset = new Vec3i(x, y, z);

                    // Get the blockstate for this position in the template. If it is not a leaf, then
                    // we don't care about it for removal purposes. This prevents deleting too much.
                    // If we couldn't access the Template's internals, then this statement never gets hit.
                    if (posToStateMap != null) {
                        IBlockState templateState = posToStateMap.get(new BlockPos(offset));
                        if (templateState == null || !(templateState.getBlock() instanceof BlockLeaves)) {
                            continue;
                        }
                    }

                    BlockPos localPos = pos.add(centerOffset).add(offset);

                    IBlockState localState = world.getBlockState(localPos);
                    Block localBlock = localState.getBlock();

                    if (localBlock instanceof BlockLeaves) {
                        WoodType woodType = ((BlockLeaves) localBlock).woodType;
                        // TODO if (woodType != this.woodType) { continue; }
                        // We don't want to modify other trees' leaves!

                        int numTrees = localState.get(BlockLeaves.NUM_TREES);
                        if (numTrees == 1) {
                            // Leaves are only part of this tree, so remove
                            world.removeBlock(localPos);
                        } else if (numTrees > 1) {
                            // Leaves are part of >1 tree, so decrement count
                            world.setBlockState(localPos, localState.with(BlockLeaves.NUM_TREES, numTrees - 1));
                        }
                    }
                }
            }
        }
    }

    /**
     * Exposes the internal list of BlockInfo objects in a Template via reflection.
     * @param template
     * @return
     */
    // hold your nose, this is stinky
    @SuppressWarnings("unchecked")
    private static List<Template.BlockInfo> getBlocks(Template template) {
        if (template == null) {
            return null;
        }

        try {
            Field templateBlocks = Template.class.getDeclaredField("blocks");
            templateBlocks.setAccessible(true);

            List<List<Template.BlockInfo>> blocks = (List<List<Template.BlockInfo>>) templateBlocks.get(template);

            ArrayList<Template.BlockInfo> blockInfos = new ArrayList<>();
            for (List<Template.BlockInfo> blockList : blocks) {
                blockInfos.addAll(blockList);
            }

            return blockInfos;
        } catch (NoSuchFieldException n) {
            n.printStackTrace();
            System.out.println("Failed to get field \"blocks\" from Template. Did the API change?");
        } catch (IllegalAccessException c) {
            c.printStackTrace();
            System.out.println("Failed to access field \"blocks\" from Template. Security issue?");
        }

        return null;
    }

    /**
     * Gets a map of BlockPos to IBlockState from a Template using reflection.
     * @param template A Template
     * @return A map of all the non-empty IBlockStates and their positions
     */
    private static Map<BlockPos, IBlockState> getBlockMap(Template template) {
        List<Template.BlockInfo> templateBlockList = getBlocks(template);
        return templateBlockList == null ?
                null :
                templateBlockList.stream().collect(Collectors.toMap(a -> a.pos, a -> a.blockState));
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

        count++;
        if (count % 100 == 0) {
            if (!world.isRemote) {

                System.out.println("Tile entity tree tick. Age = " + age);

                // TODO Check if we can grow up first.

                if (age > 0 && age <= 5) {
                    Template template = getTemplate("oak_age_" + age);
                    if (template != null) {
                        removeLeaves(template);
                    }
                }

                age++;
                if (age >= 5) {
                    System.out.println("Done growing now");
                    age = 5;
                    doneGrowing = true;
                }

                if (age > 0 && age <= 5) {
                    // TODO this is a relatively slow method call- maybe find a way to schedule it?
                    spawnTemplate("oak_age_" + age);
                }

                // If we hit max age, cap the age and mark this tree as done growing.
//                if (age >= BlockSapling.getMaxAge()) {
//                    System.out.println("Done growing now");
//                    age = BlockSapling.getMaxAge();
//                    doneGrowing = true;
//                }


                markDirty();
            }
        }
    }

    private Template getTemplate(String name) {
        if (world.isRemote) {
            System.out.println("getTemplate failed, since world is remote.");
            return null;
        }
        ResourceLocation location = new ResourceLocation(TFCR.MODID, name);
        Template template = ((WorldServer) world).getStructureTemplateManager().getTemplate(location);
        if (template == null) {
            System.out.println("Failed to get template at location: \"" + location + "\".");
        }
        return template;
    }

    private void spawnTemplate(String name) {
        System.out.println("Trying to spawn structure at pos: " + pos);
        // TODO try to cleanup old structure before spawning new one
        // TODO ensure that we can place the new structure down before adding it

        // Access the Template for the tree's structure
        Template template = getTemplate(name);
        if (template == null) {
            System.out.println("Failed to find structure: " + name);
            return;
        }

        // Rotation center should be the trunk of the tree.
        BlockPos size = template.getSize();
        BlockPos center = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);

        // Add the structure to the world.
        // TODO maybe make variant determine a random rotation/mirroring.
        // TODO instead of just adding structure, modify existing leaf blocks!!
        PlacementSettings settings = new PlacementSettings().setCenterOffset(center);
        template.addBlocksToWorld(world, pos.add(-center.getX(), 0, -center.getZ()), new TemplateProcessorTrees(WoodType.OAK), settings, 2);
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

    @Nonnull
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        compound = super.write(compound);
        compound.setInt("age", age);
        compound.setBoolean("doneGrowing", doneGrowing);
        compound.setInt("variant", variant);
        System.out.println("Wrote age: " + age);
        return compound;
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        this.age = compound.getInt("age");
        this.doneGrowing = compound.getBoolean("doneGrowing");
        this.variant = compound.getInt("variant");
        System.out.println("Read type: " + age);
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
//                WoodType theirWoodType = ((BlockLeaves) currentBlockState.getBlock()).woodType;
//                if (this.woodType != theirWoodType) {
//                    return null;
//                }

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
