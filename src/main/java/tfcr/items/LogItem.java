package tfcr.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterItem;

import java.util.Arrays;
import java.util.List;

public class LogItem extends Item implements ISelfRegisterItem {

    private WoodType woodType;

    private static LogItem[] allItems;

    public LogItem(WoodType woodType) {
        super(new Properties().group(ItemGroup.MATERIALS));
        this.woodType = woodType;
        setRegistryName(TFCR.MODID, "itemlog/" + woodType.getName());
    }

    private static void init() {
        allItems = new LogItem[WoodType.values().length];
        WoodType[] values = WoodType.values();
        for (int i = 0; i < values.length; i++) {
            WoodType woodType = values[i];
            allItems[i] = new LogItem(woodType);
        }
    }

    public static List<LogItem> getAllItems() {
        if (allItems == null) {
            init();
        }
        return Arrays.asList(allItems);
    }

    public static LogItem get(WoodType woodType) {
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
