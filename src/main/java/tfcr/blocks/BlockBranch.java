package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.tileentity.TileEntityTree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A block for wooden branches; that is, wooden logs that are thinner than a full block wide.
 *
 * TODO: add different wood types
 * TODO: add model variation (or texture change) to make adjacent same-axis branches look better.
 *  (e.g., if a 14 wide branch is below a 12 wide branch along the y axis, then the 14 wide should
 *  have the outer rim be a bark texture instead of a log texture.)
 * TODO: UVs are slightly inconsistent between [unextended, pos, neg] and [both]. Bug?
 */
public class BlockBranch extends Block implements ISelfRegisterBlock, ISelfRegisterItem {

    private static BlockBranch[] allBlocks;

    private static final EnumProperty<EnumFacing.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty ROOT = BooleanProperty.create("root");

    private static final BooleanProperty EXTEND_NEGATIVE = BooleanProperty.create("extend_negative");
    private static final BooleanProperty EXTEND_POSITIVE = BooleanProperty.create("extend_positive");

    private int diameter;
    private WoodType woodType;

    // TODO add all different wood types, instead of just placeholder ash variant
    public BlockBranch(WoodType woodType, int diameter) {
        super(Block.Properties.from(Blocks.OAK_WOOD));

        this.diameter = diameter;
        this.woodType = woodType;

        this.setRegistryName(TFCR.MODID, "branch/" + woodType.getName() + "/block_branch_" + diameter);
        this.setDefaultState(this.stateContainer.getBaseState()
            .with(AXIS, EnumFacing.Axis.Y)
            .with(ROOT, false)
            .with(EXTEND_NEGATIVE, false)
            .with(EXTEND_POSITIVE, false)
        );
    }

    public static void init() {
        allBlocks = new BlockBranch[WoodType.values().length * 7];
        WoodType[] values = WoodType.values();
        for (int woodIndex = 0; woodIndex < values.length; woodIndex++) {
            WoodType woodType = values[woodIndex];
            for (int width = 0; width < 7; width++) {
                allBlocks[(woodIndex * 7) + width] = new BlockBranch(woodType, (width + 1) * 2);
            }
        }
    }

    /**
     * Returns every variant of BlockBranch.
     * @return A new ArrayList containing every BlockBranch.
     */
    public static List<BlockBranch> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    /**
     * Given constructor parameters, returns the appropriate BlockBranch.
     * @param diameter The diameter of the branch.
     * @return A fixed instance of a BlockBranch.
     */
    public static BlockBranch get(WoodType woodType, int diameter) {
        if (diameter % 2 != 0 || diameter < 1 || diameter > 14) {
            throw new IllegalArgumentException("Failed to get BlockBranch with diameter: " + diameter);
        }
        if (allBlocks == null) {
            init();
        }
        return allBlocks[(woodType.ordinal() * 7) + (diameter / 2) - 1];
    }

    /**
     * The shape this block takes up. Displayed as the outline around it.
     * @param state The IBlockState for this block. Used to get Axis info.
     * @param worldIn Unused.
     * @param pos Unused.
     * @return The Voxel bounding box of this shape.
     */
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        int radius = diameter / 2;

        int min = 8 - radius;
        int max = 8 + radius;

        // TODO fix bad code; caused by BlockSapling calling super() and not having an axis blockstate
        try {
            switch (state.get(AXIS)) {
                case X:
                    return Block.makeCuboidShape(0, min, min, 16, max, max);
                case Y:
                    return Block.makeCuboidShape(min, 0, min, max, 16, max);
                case Z:
                    return Block.makeCuboidShape(min, min, 0, max, max, 16);
            }
        } catch (IllegalArgumentException i) {
            System.out.println("Failed to get shape for block: " + state.getBlock() + ". This is a bug!");
            return Block.makeCuboidShape(min, 0, min, max, 16, max);
        }

        return Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
    }


    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }

    /**
     * Fills out the possible states for this Block.
     *
     * Needed to setup blockstates for this block. Replaces metadata in <=1.12.
     * @param builder
     */
    @Override
    public void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(AXIS).add(ROOT).add(EXTEND_NEGATIVE).add(EXTEND_POSITIVE);
    }

    /**
     * Returns an IBlockState with a different Axis value depending on the placement direction.
     *
     * @param context
     * @return
     */
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(AXIS, context.getFace().getAxis());
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.get(ROOT);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityTree();
    }

    /**
     * Called when this block is removed from the world, or replaced by another one.
     *
     * This implementation ensures the TileEntityTree is properly preserved, if the ROOT
     * property is true (or does nothing otherwise).
     * @param state The blockstate of the block that was removed.
     * @param worldIn World reference.
     * @param pos The position of the removed block.
     * @param newState The blockstate that the removed block was replaced with.
     * @param isMoving TODO Not sure what this param does.
     */
    @Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (state.get(ROOT)) {
            System.out.println("Branch on replaced with TE");
            if (newState.getBlock() instanceof BlockBranch) {
                System.out.println("Still a branch");
                if (newState.get(ROOT)) {
                    System.out.println("Still a root block. Keeping TE.");
                    return;
                }
            } else {
                System.out.println("New state is block type: " + newState.getBlock());
            }
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean needsPostProcessing(IBlockState p_201783_1_, IBlockReader worldIn, BlockPos pos) {
        return true;
    }

    @Nonnull
    @Override
    public IBlockState updatePostPlacement(@Nonnull IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return getExtendedState(worldIn.getWorld(), stateIn, currentPos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack stack) {
        // Update the block state based on extended model rules
        worldIn.setBlockState(pos, getExtendedState(worldIn, state, pos));
    }

    /**
     * Handles the logic for choosing which model to use, based on neighboring branches.
     *
     * Specifically, checks the positive and negative direction along the axis of the block.
     * If there is another BlockBranch there with a strictly larger diameter than us, and
     * their axis is not the same as ours, then this branch block will extend its model
     * into the other BlockBranch's bounding box.
     *
     * This in short makes branches look better.
     *
     * @param worldIn
     * @param stateIn
     * @param currentPos
     * @return
     */
    private IBlockState getExtendedState(World worldIn, IBlockState stateIn, BlockPos currentPos) {
        BlockPos positive, negative;
        switch(stateIn.get(AXIS)) {
            case X: positive = currentPos.east(); negative = currentPos.west(); break;
            case Y: positive = currentPos.up(); negative = currentPos.down(); break;
            case Z: positive = currentPos.north(); negative = currentPos.south(); break;
            default: throw new IllegalArgumentException("Block " + stateIn.getBlock() + " did not have property Axis.");
        }

        IBlockState positiveState = worldIn.getBlockState(positive);
        IBlockState negativeState = worldIn.getBlockState(negative);

        boolean shouldExtendPositive = false, shouldExtendNegative = false;

        // Check in the positive axis
        if (positiveState.getBlock() instanceof BlockBranch) {
            if (positiveState.get(AXIS) != stateIn.get(AXIS)) {
                if (((BlockBranch) positiveState.getBlock()).diameter > diameter) {
                    shouldExtendPositive = true;
                }
            }
        }

        // Check in the negative axis
        if (negativeState.getBlock() instanceof BlockBranch) {
            if (negativeState.get(AXIS) != stateIn.get(AXIS)) {
                if (((BlockBranch) negativeState.getBlock()).diameter > diameter) {
                    shouldExtendNegative = true;
                }
            }
        }

        // Return this block state, possibly with axes extended.
        return stateIn.with(EXTEND_POSITIVE, shouldExtendPositive)
                .with(EXTEND_NEGATIVE, shouldExtendNegative);
    }
}
