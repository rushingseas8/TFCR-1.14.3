package tfcr.init;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public interface ISelfRegisterTileEntity<T extends TileEntity> {

    default void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        // Can only self-register if this class is of type TileEntity
        if (!(this instanceof TileEntity)) {
            System.out.println("Failed to self-register TileEntity of type: " + this.getClass());
            return;
        }

        // Should now be a safe cast
        TileEntity tileEntity = (TileEntity) this;

        // If the TileEntity's type has already been created..
        if (tileEntity.getType() != null) {
            // Check if it's already been registered to another TileEntity.
            // Probably two TileEntities share a type, which doesn't happen in Vanilla.
            if (tileEntityRegistry.containsValue(tileEntity.getType())) {
                System.out.println("Duplicate TileEntityType registration of type: " + tileEntity.getType());
            } else { // If not, then we register it (maybe the user created the Type themselves..?)
                System.out.println("TileEntityType already created, but not registered: "
                        + tileEntity.getType().getClass() + ". Adding to registry.");
            }
            return;
        }

        // Okay, the Type wasn't created. We autogenerate a Type for the TileEntity. (Normal usecase)
        TileEntityType<T> tileEntityType = TileEntityType.Builder.<T>create(T::create(null)).build(null);
//        tileEntityRegistry.register(tileEntity.getType());
//        tileEntity.getType().



    }
}
