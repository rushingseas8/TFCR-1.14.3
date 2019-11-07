package tfcr.player;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TFCRArmorCapabilityProvider implements ICapabilityProvider {

    // This gets injected because of how it's registered in the base mod class "TFCR".
    @CapabilityInject(TFCRArmorCapability.class)
    public static final Capability<TFCRArmorCapability> TFCR_ARMOR_CAPABILITY = null;

    // Private instance that is assigned to every instance of a TFCRArmorCapabilityProvider
    private LazyOptional<TFCRArmorCapability> instance = LazyOptional.of(TFCR_ARMOR_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == TFCR_ARMOR_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }
}
