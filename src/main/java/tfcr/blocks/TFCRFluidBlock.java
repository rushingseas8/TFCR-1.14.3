package tfcr.blocks;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import tfcr.init.ISelfRegisterBlock;

/**
 * Wrapper around FlowingFluidBlock to allow us to use the constructor.
 */
public class TFCRFluidBlock extends FlowingFluidBlock implements ISelfRegisterBlock {
    public TFCRFluidBlock(FlowingFluid fluidIn, Properties builder) {
        super(fluidIn, builder);
    }
}
