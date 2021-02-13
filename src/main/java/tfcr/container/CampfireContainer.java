package tfcr.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class CampfireContainer extends Container {

    public static final ContainerType<CampfireContainer> CAMPFIRE_CONTAINER_TYPE = new ContainerType<>(CampfireContainer::new);

    // Bootstrap constructor needed for clientside + the type above
    public CampfireContainer(int windowID, PlayerInventory inv) {
        super(CAMPFIRE_CONTAINER_TYPE, windowID);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
