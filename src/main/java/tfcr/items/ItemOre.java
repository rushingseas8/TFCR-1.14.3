package tfcr.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import tfcr.TFCR;
import tfcr.data.OreType;
import tfcr.init.ISelfRegisterItem;

/**
 * A class representing a TFCR ore type.
 */
public class ItemOre extends Item implements ISelfRegisterItem {

    private OreType oreType;

    public ItemOre(OreType oreType) {
        super(new Item.Properties().group(ItemGroup.MATERIALS));
        this.oreType = oreType;
        setRegistryName(TFCR.MODID, "ore/bismuthinite");
    }
}
