package tfcr.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import tfcr.blocks.CropBlock;
import tfcr.data.CropType;

public class PerennialCropBlock extends CropBlock {

    public enum PerennialLifeCycle implements IStringSerializable {
        YEAR_1,
        DORMANT;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public static EnumProperty<PerennialLifeCycle> LIFECYCLE_STAGE = EnumProperty.create("lifecycle_stage", PerennialLifeCycle.class);

    public PerennialCropBlock(Properties properties, CropType cropType) {
        super(properties, cropType);
    }

    /**
     * Register the different states this block can take
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(LIFECYCLE_STAGE);
    }
}
