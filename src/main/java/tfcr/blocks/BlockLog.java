package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.init.Blocks;
import tfcr.TFCR;
import tfcr.data.WoodType;

public class BlockLog extends net.minecraft.block.BlockLog {

    private static BlockLog[] allBlocks;

    public BlockLog(String name) {
        super(MaterialColor.WOOD, Block.Properties.from(Blocks.OAK_WOOD));
        setRegistryName(TFCR.MODID, name);
    }

    private static void init() {
        allBlocks = new BlockLog[WoodType.values().length];
        WoodType[] values = WoodType.values();
        for (int i = 0; i < values.length; i++) {
            WoodType type = values[i];
            allBlocks[i] = new BlockLog("block_log_" + type.name);
        }
    }

    public static BlockLog[] getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return allBlocks;
    }

    public static BlockLog get(WoodType type) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[type.ordinal()];
    }
}
