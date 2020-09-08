package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.items.JugItem;
import tfcr.items.LogItem;
import tfcr.items.TFCRItem;

import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModItems {

//    public static final OreItem bismuthinite = null;
    public static final TFCRItem leaves = new TFCRItem(new Item.Properties().group(ItemGroup.MATERIALS), "leaves");
    public static final TFCRItem mud_ball = new TFCRItem(new Item.Properties().group(ItemGroup.MATERIALS), "mud_ball");
    public static final TFCRItem wicker = new TFCRItem(new Item.Properties().group(ItemGroup.MATERIALS), "wicker");
    public static final TFCRItem raw_fiber = new TFCRItem(new Item.Properties().group(ItemGroup.MATERIALS), "raw_fiber");
    public static final TFCRItem plant_fiber = new TFCRItem(new Item.Properties().group(ItemGroup.MATERIALS), "plant_fiber");
    public static final TFCRItem small_rock = new TFCRItem(new Item.Properties().group(ItemGroup.MATERIALS), "small_rock");
    public static final TFCRItem sharp_rock = new TFCRItem(new Item.Properties().group(ItemGroup.MATERIALS), "sharp_rock");
    public static final TFCRItem cordage = new TFCRItem(new Item.Properties().group(ItemGroup.MATERIALS), "cordage");

    public static ArrayList<Item> allItems = new ArrayList<>();
    public static HashMap<Block, Item> itemForBlocks = new HashMap<>();

    private static void initItems() {
        // Add all the individual log items
        allItems.addAll(LogItem.getAllItems());

        // Add all the ceramic jug variants
        allItems.addAll(JugItem.getAllItems());

//        allItems.add(bismuthinite);
        allItems.add(leaves);
        allItems.add(mud_ball);
        allItems.add(wicker);
        allItems.add(raw_fiber);
        allItems.add(plant_fiber);
        allItems.add(small_rock);
        allItems.add(sharp_rock);
        allItems.add(cordage);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        initItems();

        System.out.println("Register items called");
        IForgeRegistry<Item> registry = event.getRegistry();

        for (Block b : ModBlocks.allBlocks) {
            if (b instanceof ISelfRegisterItem) {
                Item itemBlock = ((ISelfRegisterItem) b).registerItem(registry);
                itemForBlocks.put(b, itemBlock);
            }
        }

        for (Item item : allItems) {
            if (item == null) {
                System.out.println("Warning: found null item.");
                continue;
            }

            if (item instanceof ISelfRegisterItem) {
                ((ISelfRegisterItem) item).registerItem(registry);
            } else {
                System.out.println("Warning: non self-registering item found in item list: " + item.toString());
                event.getRegistry().register(item);
            }
        }
    }
}
