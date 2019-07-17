package tfcr.items;

import net.minecraft.item.Item;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterItem;

public class ItemTFCR extends Item implements ISelfRegisterItem {
    public ItemTFCR(Properties properties, String name) {
        super(properties);
        setRegistryName(TFCR.MODID, name);
    }
}
