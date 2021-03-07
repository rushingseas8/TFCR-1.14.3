package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModFluids;

import javax.annotation.Nullable;

public class CattailBlock extends DoublePlantBlock implements ISelfRegisterBlock, ISelfRegisterItem, IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public CattailBlock() {
        super(Block.Properties.from(Blocks.TALL_GRASS));
        setDefaultState(this.getStateContainer().getBaseState().with(WATERLOGGED, false).with(HALF, DoubleBlockHalf.LOWER));
        setRegistryName(TFCR.MODID, "plants/cattail");
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !state.get(BlockStateProperties.WATERLOGGED) && ModFluids.isFreshWaterLike(fluidIn);
    }

    /**
     * Placement state logic. Handles this cattail being placed in water, and
     * waterlogging the bottom half.
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        IFluidState ifluidstate = context.getWorld().getFluidState(blockpos);

        BlockState defaultState = super.getStateForPlacement(context);
        if (defaultState == null) {
            return null;
        }
        return defaultState.with(WATERLOGGED, ModFluids.isFreshWaterLike(ifluidstate.getFluid()));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        // If we're waterlogged, make sure the water under us spreads properly.
        if (stateIn.get(WATERLOGGED)) {
            Fluid fluid = worldIn.getFluidState(currentPos).getFluid();
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, fluid, fluid.getTickRate(worldIn));
        }

        return stateIn;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? ModFluids.FRESH_WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf doubleblockhalf = state.get(HALF);
        BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf) {
            IFluidState fluidState = worldIn.getFluidState(blockpos);
            if (fluidState.isEmpty()) {
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
            } else {
                worldIn.setBlockState(blockpos, fluidState.getBlockState(), 35);
            }

            worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
            if (!worldIn.isRemote && !player.isCreative()) {
                spawnDrops(state, worldIn, pos, (TileEntity)null, player, player.getHeldItemMainhand());
                spawnDrops(blockstate, worldIn, blockpos, (TileEntity)null, player, player.getHeldItemMainhand());
            }
        }

        // DoublePlantBlock calls super- this is copied here
        worldIn.playEvent(player, 2001, pos, getStateId(state));
    }
}
