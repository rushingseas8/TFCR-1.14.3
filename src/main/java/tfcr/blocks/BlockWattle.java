package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFourWay;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
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
public class BlockWattle extends BlockFourWay implements ISelfRegisterItem, ISelfRegisterBlock {

    private static BlockWattle instance;

    private final VoxelShape[] voxelShapes;

    // X/Z alignment for single wattle post
    public static final EnumProperty<EnumFacing.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    protected BlockWattle(Block.Properties builder) {
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
                        .with(HORIZONTAL_AXIS, EnumFacing.Axis.X)
        );
        setRegistryName(TFCR.MODID, "wattle");
        this.voxelShapes = this.func_196408_a(1.0F, 1.0F, 16.0F, 2.0F, 15.0F);
    }

    private static void init() {
        instance = new BlockWattle(Block.Properties.from(Blocks.OAK_FENCE));
    }

    public static BlockWattle get() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED, HORIZONTAL_AXIS);
    }


    public VoxelShape getRenderShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return this.voxelShapes[this.getIndex(state)];
    }

    /**
     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean allowsMovement(IBlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    /**
     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
     * <p>
     * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that does
     * not fit the other descriptions and will generally cause other things not to connect to the face.
     *
     * @return an approximation of the form of the given face
     * @deprecated call via {@link IBlockState#getBlockFaceShape(IBlockReader, BlockPos, EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER;
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());

        IBlockState superState = super.getStateForPlacement(context);
        if (superState == null) {
            return this.getDefaultState();
        } else {
            // Get horizontal facing direction. Rotate 90 degrees to get proper axis state
            // (since facing North will make a West-East fence)
            EnumFacing playerFacing = context.getPlacementHorizontalFacing();
            EnumFacing.Axis axis = playerFacing.rotateAround(EnumFacing.Axis.Y).getAxis();

            return superState
                    .with(NORTH, canFenceConnectTo(iblockreader, blockpos, EnumFacing.NORTH))
                    .with(EAST, canFenceConnectTo(iblockreader, blockpos, EnumFacing.EAST))
                    .with(SOUTH, canFenceConnectTo(iblockreader, blockpos, EnumFacing.SOUTH))
                    .with(WEST, canFenceConnectTo(iblockreader, blockpos, EnumFacing.WEST))
                    .with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER)
                    .with(HORIZONTAL_AXIS, axis);
        }
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
    public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return facing.getAxis().getPlane() == EnumFacing.Plane.HORIZONTAL ? stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), this.canFenceConnectTo(worldIn, currentPos, facing)) : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    // Copied from BlockFence
    private boolean canFenceConnectTo(IBlockReader world, BlockPos pos, EnumFacing facing) {
        BlockPos offset = pos.offset(facing);
        IBlockState other = world.getBlockState(offset);
        return other.canBeConnectedTo(world, offset, facing.getOpposite()) || getDefaultState().canBeConnectedTo(world, pos, facing);
    }

    // Copied from BlockFence
    @Override
    public boolean canBeConnectedTo(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing facing) {
        IBlockState other = world.getBlockState(pos.offset(facing));
        return attachesTo(other, other.getBlockFaceShape(world, pos.offset(facing), facing.getOpposite()));
    }

    // Copied from BlockFence
    public boolean attachesTo(IBlockState p_196416_1_, BlockFaceShape p_196416_2_) {
        Block block = p_196416_1_.getBlock();
        boolean flag = p_196416_2_ == BlockFaceShape.MIDDLE_POLE_THIN && (p_196416_1_.getMaterial() == this.material || block instanceof BlockFenceGate);
        return !BlockFence.isExcepBlockForAttachWithPiston(block) && p_196416_2_ == BlockFaceShape.SOLID || flag;
    }


}
