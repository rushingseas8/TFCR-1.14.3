package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import tfcr.TFCR;
import tfcr.data.Fertility;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

import java.util.Arrays;
import java.util.List;

/**
 * TFCR dirt block. Supports fertility.
 */
public class DirtBlock extends SnowyDirtBlock implements ISelfRegisterItem, ISelfRegisterBlock {

    public Fertility fertility;

    private static DirtBlock[] allBlocks;

    protected DirtBlock(Properties builder, Fertility fertility) {
        super(builder);
        this.fertility = fertility;
        setRegistryName(TFCR.MODID, "dirt/" + fertility.getName());
    }

    private static void init() {
        allBlocks = new DirtBlock[Fertility.values().length];
        for (int i = 0; i < Fertility.values().length; i++) {
            allBlocks[i] = new DirtBlock(Properties.from(Blocks.FARMLAND), Fertility.values()[i]);
        }
    }

    public static DirtBlock get(Fertility fertility) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[fertility.ordinal()];
    }

    public static List<DirtBlock> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

}
