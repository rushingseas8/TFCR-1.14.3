package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import sun.security.provider.SHA;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

public class LeafRoofBlock extends TFCRBlock implements ISelfRegisterBlock, ISelfRegisterItem {

    // TODO: not all permutations are valid.
    // Innerleft/innerright/outerleft/outerright/straight all need facing, but center does not.
    // left/right are the same model, but mirrored. This is the same as a 90 degree turn, since
    // the models are actually non-chiral.
    // Maybe this just means we should not include center?
    public enum LeafRoofShape implements IStringSerializable {
        CENTER("center"),
        STRAIGHT("straight"),
        INNER_LEFT("inner_left"),
        INNER_RIGHT("inner_right"),
        OUTER_LEFT("outer_left"),
        OUTER_RIGHT("outer_right");

        private final String name;

        private LeafRoofShape(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<LeafRoofShape> SHAPE = EnumProperty.create("shape", LeafRoofShape.class);

    public LeafRoofBlock() {
        super(Block.Properties.from(Blocks.OAK_LEAVES), "leaf_roof");
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    public boolean isSolid(BlockState state) {
        return false;
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(SHAPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader reader = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState state = this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
        return state.with(SHAPE, getShapeProperty(state, reader, pos));
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getAxis().isHorizontal() ? stateIn.with(SHAPE, getShapeProperty(stateIn, worldIn, currentPos)) : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    /**
     * Returns a stair shape property based on the surrounding stairs from the given blockstate and position
     */
    private static LeafRoofShape getShapeProperty(BlockState state, IBlockReader worldIn, BlockPos pos) {
        // Additional logic for center block (needs to be surrounded on 4 cardinal sides)
        if (isBlockSameAsUs(worldIn.getBlockState(pos.north())) &&
            isBlockSameAsUs(worldIn.getBlockState(pos.east())) &&
            isBlockSameAsUs(worldIn.getBlockState(pos.south())) &&
            isBlockSameAsUs(worldIn.getBlockState(pos.west()))) {
            return LeafRoofShape.CENTER;
        }

        // Basic logic for stairs
        Direction direction = state.get(FACING);
        BlockState blockstate = worldIn.getBlockState(pos.offset(direction));
        if (isBlockSameAsUs(blockstate)) {
            Direction direction1 = blockstate.get(FACING);
            if (direction1.getAxis() != state.get(FACING).getAxis() && isDifferentStairs(state, worldIn, pos, direction1.getOpposite())) {
                if (direction1 == direction.rotateYCCW()) {
                    return LeafRoofShape.OUTER_LEFT;
                }

                return LeafRoofShape.OUTER_RIGHT;
            }
        }

        BlockState blockstate1 = worldIn.getBlockState(pos.offset(direction.getOpposite()));
        if (isBlockSameAsUs(blockstate1)) {
            Direction direction2 = blockstate1.get(FACING);
            if (direction2.getAxis() != state.get(FACING).getAxis() && isDifferentStairs(state, worldIn, pos, direction2)) {
                if (direction2 == direction.rotateYCCW()) {
                    return LeafRoofShape.INNER_LEFT;
                }

                return LeafRoofShape.INNER_RIGHT;
            }
        }

        return LeafRoofShape.STRAIGHT;
    }

    private static boolean isDifferentStairs(BlockState state, IBlockReader worldIn, BlockPos pos, Direction face) {
        BlockState blockstate = worldIn.getBlockState(pos.offset(face));
        return !isBlockSameAsUs(blockstate) || blockstate.get(FACING) != state.get(FACING);
    }

    public static boolean isBlockSameAsUs(BlockState state) {
        return state.getBlock() instanceof LeafRoofBlock;
    }
}
