package tfcr.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import tfcr.init.ModFluids;
import tfcr.init.ModItems;

public abstract class FreshWaterFluid extends WaterFluid {

    public Fluid getFlowingFluid() {
        return ModFluids.FRESH_WATER_FLUID_FLOWING;
    }

    public Fluid getStillFluid() {
        return ModFluids.FRESH_WATER_FLUID_SOURCE;
    }

    @Override
    public Item getFilledBucket() {
//        return ModItems.fresh_water_bucket;
        return null;
    }

    @Override
    public BlockState getBlockState(IFluidState state) {
        return ModFluids.FRESH_WATER_FLUID_BLOCK.getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
//        return null;
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn) {
        return fluidIn == ModFluids.FRESH_WATER_FLUID_SOURCE || fluidIn == ModFluids.FRESH_WATER_FLUID_FLOWING;
//        return false;
    }

    /**
     * Inner class for flowing variant, following WaterFluid
     */
    public static class Flowing extends FreshWaterFluid {
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(IFluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(IFluidState state) {
            return false;
        }
    }

    /**
     * Inner class for source variant, following WaterFluid
     */
    public static class Source extends FreshWaterFluid {
        public int getLevel(IFluidState state) {
            return 8;
        }

        public boolean isSource(IFluidState state) {
            return true;
        }
    }
}
