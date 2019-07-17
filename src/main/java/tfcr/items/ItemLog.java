package tfcr.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterItem;

import java.util.Arrays;
import java.util.List;

public class ItemLog extends Item implements ISelfRegisterItem {

    private WoodType woodType;

    private static ItemLog[] allItems;

    public ItemLog(WoodType woodType) {
        super(new Properties().group(ItemGroup.MATERIALS));
        this.woodType = woodType;
        setRegistryName(TFCR.MODID, "itemlog/" + woodType.getName());
    }

    private static void init() {
        allItems = new ItemLog[WoodType.values().length];
        WoodType[] values = WoodType.values();
        for (int i = 0; i < values.length; i++) {
            WoodType woodType = values[i];
            allItems[i] = new ItemLog(woodType);
        }
    }

    public static List<ItemLog> getAllItems() {
        if (allItems == null) {
            init();
        }
        return Arrays.asList(allItems);
    }

    public static ItemLog get(WoodType woodType) {
        if (allItems == null) {
            init();
        }
        return allItems[woodType.ordinal()];
    }

    public WoodType getWoodType() {
        return woodType;
    }

    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;
    }
}
