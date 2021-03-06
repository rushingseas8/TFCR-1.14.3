package tfcr.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModTabs;
import tfcr.items.LogItem;
import tfcr.tileentity.TreeTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A block for wooden branches; that is, wooden logs that are thinner than a full block wide.
 *
 * TODO add all leafy variations
 * TODO: add model variation (or texture change) to make adjacent same-axis branches look better.
 *  (e.g., if a 14 wide branch is below a 12 wide branch along the y axis, then the 14 wide should
 *  have the outer rim be a bark texture instead of a log texture.)
 * TODO: UVs are slightly inconsistent between [unextended, pos, neg] and [both]. Bug?
 */
public class BranchBlock extends RotatedPillarBlock implements ISelfRegisterBlock, ISelfRegisterItem, IBlockWood {

    public int diameter;
    public WoodType woodType;
    public boolean leaflogged;

    private static final int NUM_DIAMETERS = 7;

    private static BranchBlock[] allBlocks;

//    private static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty ROOT = BooleanProperty.create("root");
    private static final BooleanProperty EXTEND_NEGATIVE = BooleanProperty.create("extend_negative");
    private static final BooleanProperty EXTEND_POSITIVE = BooleanProperty.create("extend_positive");

    public BranchBlock(WoodType woodType, int diameter, boolean leaflogged) {
        // Hardness is based on diameter. Sound is based on leaflogged property.
        super(Block.Properties.from(Blocks.OAK_WOOD)
                .hardnessAndResistance(2.0F * (diameter / 16f))
                .variableOpacity()
                .sound(leaflogged ? SoundType.PLANT : SoundType.WOOD));

        this.diameter = diameter;
        this.woodType = woodType;
        this.leaflogged = leaflogged;

        this.setRegistryName(TFCR.MODID,"branch/" +
                woodType.getName() + "/block_branch_" + diameter + (leaflogged ? "_leafy" : ""));

        this.setDefaultState(this.stateContainer.getBaseState()
            .with(AXIS, Direction.Axis.Y)
            .with(ROOT, false)
            .with(EXTEND_NEGATIVE, false)
            .with(EXTEND_POSITIVE, false)
        );
    }

    public static void init() {
        allBlocks = new BranchBlock[WoodType.values().length * 7 * 2];
        WoodType[] woodTypes = WoodType.values();

        for (int leafy = 0; leafy < 2; leafy++) {
            for (int woodIndex = 0; woodIndex < woodTypes.length; woodIndex++) {
                WoodType woodType = woodTypes[woodIndex];
                for (int width = 0; width < NUM_DIAMETERS; width++) {
                    int index = (leafy * woodTypes.length * NUM_DIAMETERS) + (woodIndex * NUM_DIAMETERS) + width;
                    allBlocks[index] = new BranchBlock(woodType, (width + 1) * 2, leafy == 0);
                }
            }
        }
    }

    /**
     * Returns every variant of BranchBlock.
     * @return A new ArrayList containing every BranchBlock.
     */
    public static List<BranchBlock> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    /**
     * Given constructor parameters, returns the appropriate BranchBlock.
     * @param diameter The diameter of the branch.
     * @return A fixed instance of a BranchBlock.
     */
    public static BranchBlock get(WoodType woodType, int diameter, boolean leaflogged) {
        if (diameter % 2 != 0 || diameter < 1 || diameter > 14) {
            throw new IllegalArgumentException("Failed to get BranchBlock with diameter: " + diameter);
        }
        if (allBlocks == null) {
            init();
        }

        int index = ((leaflogged ? 0 : 1) * WoodType.values().length * NUM_DIAMETERS) + (woodType.ordinal() * NUM_DIAMETERS) + (diameter / 2) - 1;
        return allBlocks[index];
    }

    @Override
    public Item registerItem(IForgeRegistry<Item> itemRegistry) {
        Item toReturn = new BlockItem(this, new Item.Properties().group(ModTabs.TFCR_WOOD)).setRegistryName(TFCR.MODID, getRegistryName().getPath());
        itemRegistry.register(toReturn);
        return toReturn;
    }

    @Override
    public WoodType getWoodType() {
        return this.woodType;
    }

    /**
     * The shape this block takes up. Displayed as the outline around it.
     * TODO update for leafy variations
     *
     * @param state The IBlockState for this block. Used to get Axis info.
     * @param worldIn Unused.
     * @param pos Unused.
     * @param context Sneak state. Unused.
     * @return The Voxel bounding box of this shape.
     */
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        // Return full block for leafy variants
        if (((BranchBlock) state.getBlock()).leaflogged) {
            return Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
        }

        int radius = diameter / 2;

        int min = 8 - radius;
        int max = 8 + radius;

        switch (state.get(AXIS)) {
            case X:
                return Block.makeCuboidShape(0, min, min, 16, max, max);
            case Y:
                return Block.makeCuboidShape(min, 0, min, max, 16, max);
            case Z:
                return Block.makeCuboidShape(min, min, 0, max, max, 16);
            default:
                System.out.println("Failed to get shape for block: " + state.getBlock() + ". This is a bug!");
                return Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
        }
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    public BlockRenderLayer getRenderLayer() {
        return leaflogged ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    /**
     * Fills out the possible states for this Block.
     *
     * Needed to setup blockstates for this block. Replaces metadata in <=1.12.
     * @param builder
     */
    @Override
    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AXIS).add(ROOT).add(EXTEND_NEGATIVE).add(EXTEND_POSITIVE);
    }

    /**
     * Returns an IBlockState with a different Axis value depending on the placement direction.
     *
     * @param context
     * @return
     */
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(AXIS, context.getFace().getAxis());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(ROOT);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TreeTileEntity(this.woodType, 2);
    }

    /**
     * Called when this block is removed from the world, or replaced by another one.
     *
     * This implementation ensures the TreeTileEntity is properly preserved, if the ROOT
     * property is true (or does nothing otherwise).
     * @param state The blockstate of the block that was removed.
     * @param worldIn World reference.
     * @param pos The position of the removed block.
     * @param newState The blockstate that the removed block was replaced with.
     * @param isMoving TODO Not sure what this param does.
     */
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.get(ROOT)) {
            System.out.println("Branch on replaced with TE");
            if (newState.getBlock() instanceof BranchBlock) {
                System.out.println("Still a branch");
                if (newState.get(ROOT)) {
                    System.out.println("Still a root block. Keeping TE.");
                } else {
                    worldIn.setBlockState(pos, newState.with(ROOT, true));
                    System.out.println("Base was not a root block. Setting to root and keeping TE.");
                }
                return;
            } else {
                System.out.println("New state is block type: " + newState.getBlock());
            }
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean needsPostProcessing(BlockState p_201783_1_, IBlockReader worldIn, BlockPos pos) {
        return true;
    }

    /**
     * Called when this block is placed down, either by the world or by the player.
     */
    @Nonnull
    @Override
    public BlockState updatePostPlacement(@Nonnull BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return getPostPlacementState(stateIn, facing, facingState);
    }

    /**
     * Handles placement logic during worldgen. During worldgen, we're not allowed to query
     * world positions, since that can cause infinite loops (if the north branch block
     * asks for the south branch block's state, then we get such a loop). Instead, we
     * only query the blockstate given to us via "facingState" in the appropriate direction,
     * and return the partially completed blockstate. My guess is Minecraft will eventually
     * provide all adjacent blocks as they are ready, so that the net result of multiple calls
     * of this method will be equivalent to @getExtendedState.
     *
     * @param stateIn
     * @param facing
     * @param facingState
     * @return
     */
    private BlockState getPostPlacementState(BlockState stateIn, Direction facing, BlockState facingState) {
        switch (stateIn.get(AXIS)) {
            case X:
                if (facing == Direction.EAST) {
                    return stateIn.with(EXTEND_POSITIVE, hasPerpendicularBranch(stateIn, facingState));
                }
                if (facing == Direction.WEST) {
                    return stateIn.with(EXTEND_NEGATIVE, hasPerpendicularBranch(stateIn, facingState));
                }
                break;
            case Y:
                if (facing == Direction.UP) {
                    return stateIn.with(EXTEND_POSITIVE, hasPerpendicularBranch(stateIn, facingState));
                }
                if (facing == Direction.DOWN) {
                    return stateIn.with(EXTEND_NEGATIVE, hasPerpendicularBranch(stateIn, facingState));
                }
                break;
            case Z:
                if (facing == Direction.NORTH) {
                    return stateIn.with(EXTEND_POSITIVE, hasPerpendicularBranch(stateIn, facingState));
                }
                if (facing == Direction.SOUTH) {
                    return stateIn.with(EXTEND_NEGATIVE, hasPerpendicularBranch(stateIn, facingState));
                }
                break;
        }

//        System.out.println("Error: Got invalid direction for branch post placement");
        return stateIn;
    }

    // Returns true if the provided neighbor is perpendicular to this blockstate, and
    // if our diameter is strictly less than the neighbor's. Used to determine if we should
    // extend this branch in the direction of the neighbor.
    private boolean hasPerpendicularBranch(BlockState stateIn, BlockState neighbor) {
        if (neighbor.getBlock() instanceof BranchBlock) {
            if (neighbor.get(AXIS) != stateIn.get(AXIS)) {
                if (((BranchBlock) neighbor.getBlock()).diameter >= ((BranchBlock) stateIn.getBlock()).diameter) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        // Update the block state based on extended model rules
        worldIn.setBlockState(pos, getExtendedState(worldIn, state, pos));
    }

    /**
     * Handles the logic for choosing which model to use, based on neighboring branches.
     *
     * Specifically, checks the positive and negative direction along the axis of the block.
     * If there is another BranchBlock there with a greater than or equal diameter to us, and
     * their axis is not the same as ours, then this branch block will extend its model
     * into the other BranchBlock's bounding box.
     *
     * This in short makes branches look better.
     *
     * This logic is specifically used when the player (or any entity) places this block,
     * since we need to query multiple world positions to get the full state. For logic that
     * is safe to use during worldgen, see @getPostPlacementState.
     *
     * @param worldIn
     * @param stateIn
     * @param currentPos
     * @return
     */
    private BlockState getExtendedState(World worldIn, BlockState stateIn, BlockPos currentPos) {
        BlockPos positive, negative;
        switch(stateIn.get(AXIS)) {
            case X: positive = currentPos.east(); negative = currentPos.west(); break;
            case Y: positive = currentPos.up(); negative = currentPos.down(); break;
            case Z: positive = currentPos.north(); negative = currentPos.south(); break;
            default: throw new IllegalArgumentException("Block " + stateIn.getBlock() + " did not have property Axis.");
        }

        BlockState positiveState = worldIn.getBlockState(positive);
        BlockState negativeState = worldIn.getBlockState(negative);

        boolean shouldExtendPositive, shouldExtendNegative;

        // Check in the positive axis
        shouldExtendPositive = hasPerpendicularBranch(stateIn, positiveState);

        // Check in the negative axis
        shouldExtendNegative = hasPerpendicularBranch(stateIn, negativeState);

        // Return this block state, possibly with axes extended.
        return stateIn.with(EXTEND_POSITIVE, shouldExtendPositive)
                .with(EXTEND_NEGATIVE, shouldExtendNegative);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        BranchBlock branch = (BranchBlock) state.getBlock();
        int diameter = branch.diameter;
        drops.add(new ItemStack(LogItem.get(branch.woodType), diameter));
        return drops;
    }
}
