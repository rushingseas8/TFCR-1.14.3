package tfcr.items;

import net.minecraft.item.Item;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterItem;

public class TFCRItem extends Item implements ISelfRegisterItem {
    public TFCRItem(Properties properties, String name) {
        super(properties);
        setRegistryName(TFCR.MODID, name);
    }
}
