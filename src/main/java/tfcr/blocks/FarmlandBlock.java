package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

public class FarmlandBlock extends net.minecraft.block.FarmlandBlock implements ISelfRegisterItem, ISelfRegisterBlock {

    /**
     * Fertility status. Custom to TFCR farmland.
     *
     * Increased (up to normal) by growing fixing plants, or simply waiting.
     * Increased (up to fertile) by applying fertilizer/compost.
     * Decreased by harvesting fully grown (non-fixing) plants.
     *
     * Barren indicates no plants will grow (except fixing plants).
     * Low fertility means plants will grow, albeit slowly and with lower yield.
     * Normal is the same as vanilla farmland.
     * Fertile will boost crop growth speed and yield.
     */
    public static EnumProperty<Fertility> FERTILITY = EnumProperty.create("fertility", Fertility.class);

    private static FarmlandBlock INSTANCE;

    public FarmlandBlock(Properties properties) {
        super(properties);
        this.setDefaultState(getDefaultState().with(MOISTURE, 0).with(FERTILITY, Fertility.NORMAL));
        setRegistryName(TFCR.MODID, "farmland");
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FERTILITY);
    }

    private static void init() {
        INSTANCE = new FarmlandBlock(Properties.from(Blocks.FARMLAND));
    }

    public static FarmlandBlock get() {
        if (INSTANCE == null) {
            init();
        }
        return INSTANCE;
    }

    public enum Fertility implements IStringSerializable {
        // SALTED, // possibly as a joke
        BARREN,
        LOW,
        NORMAL,
        FERTILE
        ;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }
}
