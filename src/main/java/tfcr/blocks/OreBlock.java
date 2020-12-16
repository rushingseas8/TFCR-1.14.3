package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.data.OreType;
import tfcr.init.ModTabs;

public class OreBlock extends TFCRBlock {

    public OreBlock(OreType oreType) {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), oreType.getName());
    }

    @Override
    public Item registerItem(IForgeRegistry<Item> itemRegistry) {
        Item toReturn = new BlockItem(this, new Item.Properties().group(ModTabs.TFCR_ORES)).setRegistryName(TFCR.MODID, getRegistryName().getPath());
        itemRegistry.register(toReturn);
        return toReturn;
    }
}
