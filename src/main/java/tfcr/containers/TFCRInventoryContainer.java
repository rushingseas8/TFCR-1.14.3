package tfcr.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import tfcr.init.ModContainers;

import javax.annotation.Nullable;

public class TFCRInventoryContainer extends Container {

    public TFCRInventoryContainer(int id, PlayerInventory inventory) {
        super(ModContainers.TFCR_INVENTORY_CONTAINER_TYPE, id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
