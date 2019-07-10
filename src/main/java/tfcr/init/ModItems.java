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
import tfcr.data.OreType;
import tfcr.items.ItemLog;
import tfcr.items.ItemOre;
import tfcr.items.ItemTFCR;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModItems {

    public static final ItemOre bismuthinite = null;
    public static final ItemTFCR leaves = new ItemTFCR(new Item.Properties().group(ItemGroup.MATERIALS), "leaves");
    public static final ItemTFCR mud_ball = new ItemTFCR(new Item.Properties().group(ItemGroup.MATERIALS), "mud_ball");
    public static final ItemTFCR stick_mesh = new ItemTFCR(new Item.Properties().group(ItemGroup.MATERIALS), "stick_mesh");

    public static ArrayList<Item> allItems = new ArrayList<>();

    private static void initItems() {
        allItems.addAll(ItemLog.getAllItems());

        allItems.add(bismuthinite);
        allItems.add(leaves);
        allItems.add(mud_ball);
        allItems.add(stick_mesh);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        initItems();

        System.out.println("Register items called");
        IForgeRegistry<Item> registry = event.getRegistry();

        for (Item i : allItems) {
            if (i instanceof ISelfRegisterItem) {
                ((ISelfRegisterItem) i).registerItem(registry);
            }
        }

        // Register ItemBlocks for modded Blocks
        for (Block b : ModBlocks.allBlocks) {
            if (b instanceof ISelfRegisterItem) {
                ((ISelfRegisterItem) b).registerItem(registry);
            }
        }


//        event.getRegistry().registerAll(
//                new ItemOre(OreType.BISMUTHINITE, new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(TFCR.MODID, "ore/bismuthinite")
//                //new ItemBlock(ModBlocks.block_branch, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(TFCR.MODID, "block_branch"),
//                //new ItemBlock(ModBlocks.marsh_marigold, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(TFCR.MODID, "marsh_marigold")
////                new ItemBlock(ModBlocks.tall_sapling, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(TFCR.MODID, "tall_sapling")
//        );
    }
}
