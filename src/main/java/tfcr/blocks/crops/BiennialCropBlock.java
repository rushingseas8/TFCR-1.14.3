package tfcr.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfcr.blocks.CropBlock;
import tfcr.data.CropType;

import java.util.Random;

/**
 * Base class for TFCR crops that are biennial, meaning they require a total
 * of two growing seasons to go to seed. Usually the first season results in fruit.
 */
public class BiennialCropBlock extends CropBlock {

    public enum BiennialLifeCycle implements IStringSerializable {
        YEAR_1,
        DORMANT,
        YEAR_2;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public static EnumProperty<BiennialLifeCycle> LIFECYCLE_STAGE = EnumProperty.create("lifecycle_stage", BiennialLifeCycle.class);

    public BiennialCropBlock(Properties properties, CropType cropType) {
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

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        switch (state.get(LIFECYCLE_STAGE)) {
            case YEAR_1:
                year1Tick(state, worldIn, pos, random); return;
            case DORMANT:
                dormantTick(state, worldIn, pos, random); return;
            case YEAR_2:
                year2Tick(state, worldIn, pos, random); return;
        }
    }

    // TODO update this to account for transitioning to dormant stage
    private void year1Tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        super.tick(state, worldIn, pos, random);
    }

    // TODO implement
    private void dormantTick(BlockState state, World worldIn, BlockPos pos, Random random) {
        super.tick(state, worldIn, pos, random);
    }

    // TODO implement
    private void year2Tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        super.tick(state, worldIn, pos, random);
    }
}
