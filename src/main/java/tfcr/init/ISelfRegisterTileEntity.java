package tfcr.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public interface ISelfRegisterTileEntity {
    default void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        // How to implement this?
    }
}
