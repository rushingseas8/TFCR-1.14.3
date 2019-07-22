package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FourWayBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

/**
 * A fence-like block made from a stick mesh. Different in that a lone block
 * does not act like a fence post, but instead is directional (x or z axis aligned).
 * Additionally, the model is thinner (2 pixels wide instead of 4).
 */
public class WattleBlock extends FourWayBlock implements ISelfRegisterItem, ISelfRegisterBlock {

    private static WattleBlock instance;

    private final VoxelShape[] voxelShapes;

    // X/Z alignment for single wattle post
    public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    protected WattleBlock(Block.Properties builder) {
        /*
         * 1: 8 +/- x
         * 2: 8 +/- z
         * 3: max x width
         * 4: max z width
         * 5: height
         */
        super(1.0F, 1.0F, 16.0F, 16.0F, 24.0F, builder);
        setDefaultState(
                this.stateContainer.getBaseState()
                        .with(NORTH, false)
                        .with(SOUTH, false)
                        .with(EAST, false)
                        .with(WEST, false)
                        .with(WATERLOGGED, false)
                        .with(HORIZONTAL_AXIS, Direction.Axis.X)
                        .with(HALF, Half.TOP)
        );
        setRegistryName(TFCR.MODID, "wattle");
        this.voxelShapes = this.makeShapes(1.0F, 1.0F, 16.0F, 2.0F, 15.0F);
    }

    private static void init() {
        instance = new WattleBlock(Block.Properties.from(Blocks.OAK_FENCE));
    }

    public static WattleBlock get() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED, HORIZONTAL_AXIS, HALF);
    }


    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return this.voxelShapes[this.getIndex(state)];
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());

        BlockState superState = super.getStateForPlacement(context);
        if (superState == null) {
            return this.getDefaultState();
        }

        // Get horizontal facing direction. Rotate 90 degrees to get proper axis state
        // (since facing North will make a West-East fence)
        Direction playerFacing = context.getPlacementHorizontalFacing();
        Direction.Axis axis = playerFacing.rotateAround(Direction.Axis.Y).getAxis();

        // We're the bottom half of a block if we're below another wattle block
        boolean isBottom = iblockreader.getBlockState(blockpos.up()).getBlock() instanceof WattleBlock;

        return superState
                .with(NORTH, canFenceConnectTo(iblockreader, blockpos, Direction.NORTH))
                .with(EAST, canFenceConnectTo(iblockreader, blockpos, Direction.EAST))
                .with(SOUTH, canFenceConnectTo(iblockreader, blockpos, Direction.SOUTH))
                .with(WEST, canFenceConnectTo(iblockreader, blockpos, Direction.WEST))
                .with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER)
                .with(HORIZONTAL_AXIS, axis)
                .with(HALF, isBottom ? Half.BOTTOM : Half.TOP);
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     *
     * @param facingState The state that is currently at the position offset of the provided face to the stateIn at
     * currentPos
     */
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        BlockState state = facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), this.canFenceConnectTo(worldIn, currentPos, facing)) : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);

        if (worldIn.getBlockState(currentPos.up()).getBlock() instanceof WattleBlock) {
            return state.with(HALF, Half.BOTTOM);
        }
        return state.with(HALF, Half.TOP);
    }

    // Copied from BlockFence
    // TODO validate this logic
    private boolean canFenceConnectTo(IBlockReader world, BlockPos pos, Direction facing) {
        BlockPos offset = pos.offset(facing);
        BlockState other = world.getBlockState(offset);
        Block block = other.getBlock();

        // This logic is copied from FenceBlock#func_220111_a(BlockState, boolean, Direction)
        boolean isMatchingFence = block.isIn(BlockTags.FENCES) && other.getMaterial() == this.material;
        boolean isFenceGate = block instanceof FenceGateBlock && FenceGateBlock.isParallel(other, facing);

        // This fence can connect iff:
        //  - The block is a fence (tested via tags) with the same material as us
        //  - OR the block is a fence gate in line with this fence
        //  - OR the block is not an exception block (@see Block#cannotAttach), like leaves or barriers
        //  - AND the block has a solid side facing this fence
        return isMatchingFence || isFenceGate ||
                (!cannotAttach(block) && Block.hasSolidSide(other, world, offset, facing.getOpposite()));
    }
}
