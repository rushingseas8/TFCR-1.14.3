package tfcr.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterTileEntity;

/**
 * A TileEntity registered to TFCR dirt objects. Used for handling long-term
 * drying mechanics (since the intention is for this to occur over a period of
 * 1-3 days somewhat consistently, vs. Vanilla implementation of a geometric
 * spread averaging 1/3 of a Minecraft day).
 */
public class DirtTileEntity extends TileEntity implements ITickableTileEntity, ISelfRegisterTileEntity<DirtTileEntity> {


    private static TileEntityType<DirtTileEntity> DIRT;

    public DirtTileEntity() {
        super(DIRT);
    }

//    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
//        DIRT = TileEntityType.Builder.create(DirtTileEntity::new).build(null);
//        DIRT.setRegistryName(TFCR.MODID, "tile_entity_dirt");
//        tileEntityRegistry.register(DIRT);
//    }

    @Override
    public void tick() {

    }
}
