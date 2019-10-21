package tfcr.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.blocks.CropBlock;

/**
 * A TileEntity registered to TFCR crop blocks. Used to keep accurate track of
 * growth state through changes in nutrition, soil moisture, and temperature.
 */
public class CropTileEntity extends TileEntity implements ITickableTileEntity {

    private static TileEntityType<CropTileEntity> CROP;

    public CropTileEntity() {
        super(CROP);
    }

    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        CROP = TileEntityType.Builder
                .create(
                        CropTileEntity::new,
                        CropBlock.getAllBlocks().toArray(new CropBlock[0])
                )
                .build(null);
        CROP.setRegistryName(TFCR.MODID, "tile_entity_crop");
        tileEntityRegistry.register(CROP);
    }

    public CropTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {

    }
}
