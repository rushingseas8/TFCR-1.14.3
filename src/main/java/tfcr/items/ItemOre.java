package tfcr.items;

import net.minecraft.item.Item;
import tfcr.data.OreType;

/**
 * A class representing a TFCR ore type.
 */
public class ItemOre extends Item {

    private OreType oreType;

    public ItemOre(OreType oreType, Item.Properties properties) {
        super(properties);
        this.oreType = oreType;
    }
}
