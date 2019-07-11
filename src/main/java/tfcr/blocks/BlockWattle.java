package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFourWay;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import tfcr.TFCR;

/**
 * A fence-like block made from a stick mesh. Different in that a lone block
 * does not act like a fence post, but instead is directional (x or z axis aligned).
 * Additionally, the model is thinner (2 pixels wide instead of 4).
 */
public class BlockWattle extends BlockFourWay {

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
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        return super.getStateForPlacement(context)
                .with(NORTH, canFenceConnectTo(iblockreader, blockpos, EnumFacing.NORTH))
                .with(EAST, canFenceConnectTo(iblockreader, blockpos, EnumFacing.EAST))
                .with(SOUTH, canFenceConnectTo(iblockreader, blockpos, EnumFacing.SOUTH))
                .with(WEST, canFenceConnectTo(iblockreader, blockpos, EnumFacing.WEST))
                .with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
    }
}
