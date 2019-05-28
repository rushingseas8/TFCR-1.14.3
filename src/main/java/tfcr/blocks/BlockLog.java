package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.init.Blocks;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

import java.util.Arrays;
import java.util.List;

public class BlockLog extends net.minecraft.block.BlockLog implements ISelfRegisterBlock, ISelfRegisterItem {

    private static BlockLog[] allBlocks;

    public WoodType woodType;

    public BlockLog(WoodType woodType) {
        super(MaterialColor.WOOD, Block.Properties.from(Blocks.OAK_WOOD));
        this.woodType = woodType;
        setRegistryName(TFCR.MODID, "log/" + woodType.getName());
    }

    private static void init() {
        allBlocks = new BlockLog[WoodType.values().length];
        WoodType[] values = WoodType.values();
        for (int i = 0; i < values.length; i++) {
            WoodType type = values[i];
            allBlocks[i] = new BlockLog(type);
        }
    }

    public static List<BlockLog> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    public static BlockLog get(WoodType type) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[type.ordinal()];
    }
}
