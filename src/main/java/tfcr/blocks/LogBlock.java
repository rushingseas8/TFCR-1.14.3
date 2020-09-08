package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModTabs;

import java.util.Arrays;
import java.util.List;

public class LogBlock extends net.minecraft.block.LogBlock implements ISelfRegisterBlock, ISelfRegisterItem, IBlockWood {

    private static LogBlock[] allBlocks;

    public WoodType woodType;

    public LogBlock(WoodType woodType) {
        super(MaterialColor.WOOD, Block.Properties.from(Blocks.OAK_WOOD));
        this.woodType = woodType;
        setRegistryName(TFCR.MODID, "log/" + woodType.getName());
    }

    private static void init() {
        allBlocks = new LogBlock[WoodType.values().length];
        WoodType[] values = WoodType.values();
        for (int i = 0; i < values.length; i++) {
            WoodType type = values[i];
            allBlocks[i] = new LogBlock(type);
        }
    }

    public static List<LogBlock> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    public static LogBlock get(WoodType type) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[type.ordinal()];
    }

    @Override
    public Item registerItem(IForgeRegistry<Item> itemRegistry) {
        Item toReturn = new BlockItem(this, new Item.Properties().group(ModTabs.TFCR_WOOD)).setRegistryName(TFCR.MODID, getRegistryName().getPath());
        itemRegistry.register(toReturn);
        return toReturn;
    }

    @Override
    public WoodType getWoodType() {
        return this.woodType;
    }
}
