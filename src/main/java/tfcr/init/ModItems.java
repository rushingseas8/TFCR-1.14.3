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
import tfcr.items.ItemOre;
import tfcr.items.ItemTFCR;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModItems {

    public static final ItemOre bismuthinite = null;
    public static final ItemTFCR mud_ball = new ItemTFCR(new Item.Properties().group(ItemGroup.MATERIALS), "mud_ball");

    public static final Item[] allItems = {
            bismuthinite,
            mud_ball
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {

        System.out.println("Register items called");
        IForgeRegistry<Item> registry = event.getRegistry();

        for (Block b : ModBlocks.allBlocks) {
            if (b instanceof ISelfRegisterItem) {
                ((ISelfRegisterItem) b).registerItem(registry);
            }
        }

        for (Item item : allItems) {
            if (item instanceof ISelfRegisterItem) {
                ((ISelfRegisterItem) item).registerItem(registry);
            } else {
                System.out.println("Warning: non self-registering item found in item list: " + item.toString());
                event.getRegistry().register(item);
            }
        }
    }
}
