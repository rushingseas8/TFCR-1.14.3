package tfcr.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.containers.TFCRInventoryContainer;
import tfcr.containers.TFCRInventoryContainerScreen;

@ObjectHolder(TFCR.MODID)
@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {
    @ObjectHolder("tfcr:tfcr_inventory")
    public static final ContainerType<TFCRInventoryContainer> TFCR_INVENTORY_CONTAINER_TYPE = null;

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(
                IForgeContainerType.create(
                        (int id, PlayerInventory inventory, PacketBuffer inventory2) -> new TFCRInventoryContainer(id, inventory)
                ).setRegistryName("tfcr_inventory"));
    }
}
