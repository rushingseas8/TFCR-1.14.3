package tfcr.player;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TFCRArmorCapability extends ItemStackHandler {
    /**
     * Storage class for writing to/reading from NBT
     */
    public static class TFCRArmorStorage implements Capability.IStorage<IItemHandler> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IItemHandler> capability, IItemHandler instance, Direction side) {
            //return instance.serializeNBT();
            return null;
        }

        @Override
        public void readNBT(Capability<IItemHandler> capability, IItemHandler instance, Direction side, INBT nbt) {
            //instance.deserializeNBT((CompoundNBT) nbt);
        }
    }

    public enum TFCRArmorSlot implements IStringSerializable {
        CLOTHING_HEAD,
        CLOTHING_CHEST,
        CLOTHING_LEGS,
        CLOTHING_FEET,
        INSULATION_HEAD,
        INSULATION_CHEST,
        INSULATION_LEGS,
        INSULATION_FEET
        ;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public TFCRArmorCapability() {
        super(8);
    }
}
