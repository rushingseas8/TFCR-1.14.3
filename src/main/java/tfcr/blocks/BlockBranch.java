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
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.tileentity.TileEntityTree;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockBranch extends Block implements ISelfRegisterBlock, ISelfRegisterItem {

    private static BlockBranch block_branch_2;
    private static BlockBranch block_branch_4;
    private static BlockBranch block_branch_6;
    private static BlockBranch block_branch_8;
    private static BlockBranch block_branch_10;
    private static BlockBranch block_branch_12;
    private static BlockBranch block_branch_14;

    private static BlockBranch[] allBlocks;

    private static final EnumProperty<EnumFacing.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty ROOT = BooleanProperty.create("root");

    private static final BooleanProperty EXTEND_NEGATIVE = BooleanProperty.create("extend_negative");
    private static final BooleanProperty EXTEND_POSITIVE = BooleanProperty.create("extend_positive");

    private int diameter;
    // TODO carry wood type as field

    // TODO add all different wood types, instead of just placeholder ash variant
    public BlockBranch(int diameter) {
        super(Block.Properties.from(Blocks.OAK_WOOD));

        this.diameter = diameter;

        this.setRegistryName(TFCR.MODID, "branch/block_branch_" + diameter);
        this.setDefaultState(this.stateContainer.getBaseState()
            .with(AXIS, EnumFacing.Axis.Y)
            .with(ROOT, false)
            .with(EXTEND_NEGATIVE, false)
            .with(EXTEND_POSITIVE, false)
        );
    }

    public static void init() {
        block_branch_2 = new BlockBranch(2);
        block_branch_4 = new BlockBranch(4);
        block_branch_6 = new BlockBranch(6);
        block_branch_8 = new BlockBranch(8);
        block_branch_10 = new BlockBranch(10);
        block_branch_12 = new BlockBranch(12);
        block_branch_14 = new BlockBranch(14);

        allBlocks = new BlockBranch[]{
            block_branch_2,
            block_branch_4,
            block_branch_6,
            block_branch_8,
            block_branch_10,
            block_branch_12,
            block_branch_14
        };
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
    public static BlockBranch get(int diameter) {
        if (diameter % 2 != 0 || diameter < 1 || diameter > 14) {
            throw new IllegalArgumentException("Failed to get BlockBranch with diameter: " + diameter);
        }
        // TODO array index out of bounds exception here
        return allBlocks[(diameter / 2) - 1];
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
        //int diameter = state.get(DIAMETER).diameter;
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
                //default: return Block.makeCuboidShape(min, 0, min, max, 16, max);
            }
        } catch (IllegalArgumentException i) {
            return Block.makeCuboidShape(min, 0, min, max, 16, max);
        }



        // TODO get the proper AABB based on the direction we're facing + diameter.
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

    @Override
    public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        System.out.println("Post placement called.");
        System.out.println("Current pos: " + currentPos);
        System.out.println("Facing state block: " + facingState.getBlock());
        System.out.println("Facing pos: " + facingPos);


        //return stateIn;
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack stack) {
        BlockPos positive, negative;
        switch(state.get(AXIS)) {
            case X: positive = pos.east(); negative = pos.west(); break;
            case Y: positive = pos.up(); negative = pos.down(); break;
            case Z: positive = pos.north(); negative = pos.south(); break;
            default: throw new IllegalArgumentException("Block " + state.getBlock() + " did not have property Axis.");
        }

        IBlockState positiveState = worldIn.getBlockState(positive);
        IBlockState negativeState = worldIn.getBlockState(negative);

        boolean shouldExtendPositive = false, shouldExtendNegative = false;

        // Check in the positive axis
        if (positiveState.getBlock() instanceof BlockBranch) {
            if (positiveState.get(AXIS) != state.get(AXIS)) {
                if (((BlockBranch) positiveState.getBlock()).diameter > diameter) {
                    shouldExtendPositive = true;
                }
            }
        }

        // Check in the negative axis
        if (negativeState.getBlock() instanceof BlockBranch) {
            if (negativeState.get(AXIS) != state.get(AXIS)) {
                if (((BlockBranch) negativeState.getBlock()).diameter > diameter) {
                    shouldExtendNegative = true;
                }
            }
        }

        // Update the block state so it makes sense
        worldIn.setBlockState(pos, state
                .with(EXTEND_POSITIVE, shouldExtendPositive)
                .with(EXTEND_NEGATIVE, shouldExtendNegative));
    }
}
