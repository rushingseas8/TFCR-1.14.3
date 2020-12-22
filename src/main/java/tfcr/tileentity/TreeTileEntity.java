package tfcr.tileentity;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.blocks.BranchBlock;
import tfcr.blocks.LeavesBlock;
import tfcr.blocks.LogBlock;
import tfcr.blocks.SaplingBlock;
import tfcr.blocks.TallSaplingBlock;
import tfcr.data.WoodType;
import tfcr.utils.TemplateHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeTileEntity extends TileEntity implements ITickableTileEntity {

    /**
     * The type of wood this TreeTileEntity corresponds to.
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


    private static TileEntityType<TreeTileEntity> TREE;

    /**
     * Called during registry event for this mod (specifically, in ModBlocks). Registers this TileEntity.
     * @param tileEntityRegistry A reference to the TileEntity registry.
     */
    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        List<Block> validBlocks = new ArrayList<>();
        validBlocks.addAll(SaplingBlock.getAllBlocks());
        validBlocks.addAll(TallSaplingBlock.getAllBlocks());
        validBlocks.addAll(BranchBlock.getAllBlocks());
        validBlocks.addAll(LogBlock.getAllBlocks());

        // TODO build with null? is that right?
        TREE = TileEntityType.Builder.create(TreeTileEntity::new, validBlocks.toArray(new Block[0])).build(null);
        TREE.setRegistryName(TFCR.MODID, "tile_entity_tree");
        tileEntityRegistry.register(TREE);

    }

    // Used internally to register the TileEntity. Should not be called externally.
    private TreeTileEntity() {
        super(TREE);
        this.woodType = null;
    }

    public TreeTileEntity(@Nonnull WoodType woodType, int age) {
        super(TREE);
        this.woodType = woodType;
        this.age = age;
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
        if (age > 0) {
            Template template = TemplateHelper.getTemplate(world, TemplateHelper.getTreeTemplateLocation(woodType, age));
            if (template == null) {
                System.out.println("Failed to remove additional blocks- template " + TemplateHelper.getTreeTemplateLocation(woodType, age) + " could not be found.");
                return;
            }

            cleanupTree(template);
        }

    }

    @Override
    public void tick() {
//        return;

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
                if (age > 0 && age < SaplingBlock.getMaxAge()) {
                    Template template = TemplateHelper.getTemplate(world, TemplateHelper.getTreeTemplateLocation(woodType, age));
                    if (template != null) {
                        System.out.println("Removing template with age: " + age);
                        cleanupTree(template);
                    }
                }

                // Increase age
                age++;
                // Ensure age is within range. If at max, stop growing and ticking.
                if (age >= SaplingBlock.getMaxAge()) {
                    System.out.println("Done growing now");
                    age = SaplingBlock.getMaxAge();
                    doneGrowing = true;
                }

                // Otherwise, we grew. So spawn new template
                // TODO this is a relatively slow method call- maybe find a way to schedule it?
                System.out.println("Growing template with age: " + age);
                spawnTree();

                // Mark this TileEntity as dirty, so it saves its metadata to disk
                markDirty();
            }
        }
    }

    private void spawnTree() {
        System.out.println("Trying to spawn structure at pos: " + pos);
        // TODO try to cleanup old structure before spawning new one
        // TODO ensure that we can place the new structure down before adding it

        // Access the Template for the tree's structure
        Template template = TemplateHelper.getTemplate(world, TemplateHelper.getTreeTemplateLocation(woodType, age));
        if (template == null) {
            System.out.println("Failed to find structure: " + TemplateHelper.getTreeTemplateLocation(woodType, age));
            return;
        }
//        Map<BlockPos, BlockState> map = TemplateHelper.getBlockMap(template);


        // Rotation center should be the trunk of the tree.
        BlockPos size = template.getSize();
        BlockPos center = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);

//        for (Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
//            world.setBlockState(entry.getKey().add(pos).subtract(center).add(0, center.getY(), 0), entry.getValue(), 2);
//        }

        // Add the structure to the world.
        // TODO maybe make variant determine a random rotation/mirroring.
        // TODO instead of just adding structure, modify existing leaf blocks!!
        PlacementSettings settings = new PlacementSettings().setCenterOffset(center);
        settings.addProcessor(new TemplateProcessorTrees(this.woodType));
        template.addBlocksToWorld(world, pos.add(-center.getX(), 0, -center.getZ()), settings);
//        template.addBlocksToWorld(world, pos.add(-center.getX(), 0, -center.getZ()), settings);
        System.out.println("Successfully added blocks.");

        // Validate the tree after it is added into the world, by making sure
        // there is a TreeTileEntity after the structure blocks are placed down.
        BlockState newRoot = world.getBlockState(pos);
        Block blockType = newRoot.getBlock();
        TreeTileEntity treeTileEntity = ((TreeTileEntity) world.getTileEntity(pos));

        if (blockType instanceof SaplingBlock) {
            // Valid, for now do nothing else
        } else if (blockType instanceof TallSaplingBlock) {
            // Valid, for now do nothing else
        } else if (blockType instanceof BranchBlock) {
            // Ensure this is a root
            System.out.println("Setting block type to BranchBlock. Ensuring it is root.");
            world.setBlockState(pos, newRoot.with(BranchBlock.ROOT, true));
        } else if (blockType instanceof LogBlock) {
            // Valid, for now do nothing else
        } else {
            // Some other block was generated which can't support a TreeTileEntity.
            // This means there's a bug somewhere in the code, so we report it.
            // Either way, we return, since there won't be a TreeTileEntity to modify.
            // TODO crash? Log error?
            System.out.println("TreeTileEntity created invalid trunk block: " + newRoot.getBlock().toString());
            return;
        }

        // Ensure root block has proper TileEntity
        if (treeTileEntity == null) {
            System.out.println("No TE found for root block: " + newRoot.getBlock().toString());
            return;
        }

        // Update the TileEntity there, since it is recreated if the Block type changes.
        // TODO update all other values.
        treeTileEntity.age = this.age;
        treeTileEntity.variant = this.variant;
        treeTileEntity.count = 0;
        System.out.println("Updated base of tree to have TileEntity.");
    }

    private void cleanupTree(Template template) {
        if (template == null) {
            System.out.println("Failed to cleanup tree. Template was null!");
            return;
        }

        // Get a map of BlockPos : IBlockState, so we can query the template.
        // TODO I think Templates have exposed a similar method. Look at func_215387_a and others
        Map<BlockPos, BlockState> posToStateMap = TemplateHelper.getBlockMap(template);

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
                        BlockState templateState = posToStateMap.get(new BlockPos(offset));
                        if (templateState == null || !(
                                (templateState.getBlock() instanceof BranchBlock) ||
                                        (templateState.getBlock() instanceof LogBlock) ||
                                        (templateState.getBlock() instanceof LeavesBlock))) {
                            continue;
                        }
                    }

                    BlockPos localPos = new BlockPos(offset);
                    BlockPos worldPos = pos.add(centerOffset).add(offset);

                    BlockState localState = world.getBlockState(worldPos);
                    Block localBlock = localState.getBlock();

                    if (posToStateMap != null) {
                        BlockState expectedState = posToStateMap.get(localPos);
                        Block expectedBlock = expectedState.getBlock();

//                        if (localBlock != expectedBlock) {
////                            System.out.println("Mismatch in block types. Left: " + posToStateMap.get(new BlockPos(offset)) + " Right: " + localState + ". Ignoring.");
//                            continue;
//                        }

                        // Okay, both branch blocks.. If any metadata differences, we've been overwritten, so we can't remove.
                        if (localBlock instanceof BranchBlock && expectedBlock instanceof BranchBlock) {
                            BranchBlock localBranch = (BranchBlock) localBlock;
                            BranchBlock expectedBranch = (BranchBlock) expectedBlock;

                            if (localBranch.woodType != expectedBranch.woodType) {
                                continue;
                            }

                            if (localBranch.diameter != expectedBranch.diameter) {
                                continue;
                            }

                            if (localBranch.leaflogged != expectedBranch.leaflogged) {
                                continue;
                            }

                            world.removeBlock(worldPos, false);
                        }

                        if (localBlock instanceof LeavesBlock && expectedBlock instanceof LeavesBlock) {
                            LeavesBlock localLeaf = (LeavesBlock) localBlock;
                            LeavesBlock expectedLeaf = (LeavesBlock) expectedBlock;

                            if (localLeaf.woodType != expectedLeaf.woodType) {
                                continue;
                            }

                            int numTrees = localState.get(LeavesBlock.NUM_TREES);
                            if (numTrees == 1) {
                                System.out.println("Cleanup found leaves. Removing.");
                                // Leaves are only part of this tree, so remove
                                world.removeBlock(worldPos, false);
                            } else if (numTrees > 1) {
                                System.out.println("Cleanup found leaves. Decrementing to: " + (numTrees - 1));
                                // Leaves are part of >1 tree, so decrement count
                                // TODO setBlockState seems to occasionally make grow think there's nothing there- does it remove then add?
                                world.setBlockState(worldPos, localState.with(LeavesBlock.NUM_TREES, numTrees - 1));
                            }

                        }
                    }

//                    if (posToStateMap != null && posToStateMap.get(new BlockPos(offset)) != localState) {
//                        System.out.println("Left: " + posToStateMap.get(new BlockPos(offset)) + " Right: " + localState + ". Ignoring.");
//                        continue;
//                    }

//                    if (localBlock instanceof BranchBlock || localBlock instanceof LogBlock) {
//                        // TODO check wood type matches structure
//                        WoodType woodType = localBlock instanceof BranchBlock ?
//                                ((BranchBlock) localBlock).woodType :
//                                ((LogBlock) localBlock).woodType;
//                        // We don't want to modify other trees' branches!
//                        if (woodType != this.woodType) {
//                            continue;
//                        }
//
//                        // Don't remove TileEntityTrees- that could break growing up mechanics.
////                        if (world.getTileEntity(worldPos) != null && world.getTileEntity(worldPos) instanceof TreeTileEntity) {
////                            continue;
////                        }
//
//                        // Otherwise, just remove branches and logs
//                        // TODO should "isMoving" be false?
//                        world.removeBlock(worldPos, false);
//                    } else if (localBlock instanceof LeavesBlock) {
//                        WoodType woodType = ((LeavesBlock) localBlock).woodType;
//                        // We don't want to modify other trees' leaves!
//                        if (woodType != this.woodType) {
//                            continue;
//                        }
//
//                        int numTrees = localState.get(LeavesBlock.NUM_TREES);
//                        if (numTrees == 1) {
//                            System.out.println("Cleanup found leaves. Removing.");
//                            // Leaves are only part of this tree, so remove
//                            world.removeBlock(worldPos, false);
//                        } else if (numTrees > 1) {
//                            System.out.println("Cleanup found leaves. Decrementing to: " + (numTrees - 1));
//                            // Leaves are part of >1 tree, so decrement count
//                            world.setBlockState(worldPos, localState.with(LeavesBlock.NUM_TREES, numTrees - 1));
//                        }
//                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        try {
            compound = super.write(compound);
            compound.putInt("woodTypeInt", woodType.ordinal());
            compound.putInt("age", age);
            compound.putInt("count", count);
            compound.putBoolean("doneGrowing", doneGrowing);
            compound.putInt("variant", variant);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Wrote age: " + age);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.woodType = WoodType.values()[compound.getInt("woodTypeInt")];
        this.age = compound.getInt("age");
        this.count = compound.getInt("count");
        this.doneGrowing = compound.getBoolean("doneGrowing");
        this.variant = compound.getInt("variant");
//        System.out.println("Read type: " + age);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        System.out.println("getUpdatePacket");

        CompoundNBT nbtTag = new CompoundNBT();
        this.write(nbtTag);
        return new SUpdateTileEntityPacket(this.pos, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        System.out.println("onDataPacket");
        read(packet.getNbtCompound());
    }

    public static class TemplateProcessorTrees extends StructureProcessor {

        private WoodType woodType;

        public TemplateProcessorTrees(WoodType woodType) {
            this.woodType = woodType;
        }

        @Nullable
        @Override
        public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, Template.BlockInfo unsure, Template.BlockInfo blockInfoIn, PlacementSettings placementSettings) {
            // We never override with air in the structure
            if (blockInfoIn.state.getBlock() instanceof AirBlock) {
                return null;
            }

            // If the world pos is a leaf block AND so is the block we are going to place,
            // then we need to do additional processing.
            BlockState currentBlockState = worldIn.getBlockState(pos.add(unsure.pos));
            BlockState placingBlockState = blockInfoIn.state;

            if ((currentBlockState.getBlock() instanceof LeavesBlock) && (placingBlockState.getBlock() instanceof LeavesBlock)) {
                // If the woodtype is different, then we don't replace leaves
                WoodType theirWoodType = ((LeavesBlock) currentBlockState.getBlock()).woodType;
                if (this.woodType != theirWoodType) {
                    return null;
                }

                // Else if the woodtype is the same, we increment the numTree count.
                int numTrees = currentBlockState.get(LeavesBlock.NUM_TREES);
                System.out.println("Both are leaves! Existing numTrees: " + numTrees);
                return new Template.BlockInfo(blockInfoIn.pos, placingBlockState.with(LeavesBlock.NUM_TREES, numTrees + 1), blockInfoIn.nbt);
            }

            // Ensure that we always place leaves with numTrees at least 1.
            if (placingBlockState.getBlock() instanceof LeavesBlock) {
                // Don't grow into any sort of logs/branches
                if (currentBlockState.getBlock() instanceof BranchBlock ||
                        currentBlockState.getBlock() instanceof LogBlock ||
                        currentBlockState.getBlock() instanceof SaplingBlock ||
                        currentBlockState.getBlock() instanceof TallSaplingBlock) {
                    return null;
                }
                System.out.println("Growing leaves into space containing: " + currentBlockState);

                if (placingBlockState.get(LeavesBlock.NUM_TREES) == 0) {
                    return new Template.BlockInfo(blockInfoIn.pos, placingBlockState.with(LeavesBlock.NUM_TREES, 1), blockInfoIn.nbt);
                }
            }

            // Default- place whatever we were going to place.
            return blockInfoIn;
        }

        @Override
        protected IStructureProcessorType getType() {
            return IStructureProcessorType.RULE;
        }

        // TODO I'm not sure what this method does. I used the NopProcessor's code as a placeholder
        @Override
        protected <T> Dynamic<T> serialize0(DynamicOps<T> p_215193_1_) {
            return new Dynamic<>(p_215193_1_, p_215193_1_.emptyMap());
        }
    }

}
