package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.data.OreType;
import tfcr.items.ItemFishingRodTFCR;
import tfcr.items.ItemOre;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModItems {

    public static final ItemOre bismuthinite = null;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {

        System.out.println("Register items called");
        IForgeRegistry<Item> registry = event.getRegistry();

        for (Block b : ModBlocks.allBlocks) {
            if (b instanceof ISelfRegisterItem) {
                ((ISelfRegisterItem) b).registerItem(registry);
            }
        }
        event.getRegistry().register(new ItemFishingRodTFCR());

        event.getRegistry().registerAll(
                new ItemOre(OreType.BISMUTHINITE, new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(TFCR.MODID, "ore/bismuthinite")
                //new ItemBlock(ModBlocks.block_branch, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(TFCR.MODID, "block_branch"),
                //new ItemBlock(ModBlocks.marsh_marigold, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(TFCR.MODID, "marsh_marigold")
        );
    }
}
