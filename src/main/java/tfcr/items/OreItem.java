package tfcr.items;

import net.minecraft.item.Item;
import tfcr.TFCR;
import tfcr.data.OreType;
import tfcr.init.ISelfRegisterItem;

/**
 * A class representing a TFCR ore type.
 */
public class OreItem extends Item implements ISelfRegisterItem {

    private OreType oreType;

    public OreItem(OreType oreType, Item.Properties properties) {
        super(properties);
        this.oreType = oreType;
        this.setRegistryName(TFCR.MODID, "ore/" + oreType.getName());
    }
}
