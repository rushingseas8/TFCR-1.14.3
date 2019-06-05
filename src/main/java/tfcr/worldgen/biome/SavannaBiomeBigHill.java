package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [10-20]
 *
 * Savanna biomes only spawn in Flat variants. Modifications:
 * Hills --> Shrubland
 * Mountains --> Chaparral
 */
public class SavannaBiomeBigHill extends BaseTFCRBiome {

    public SavannaBiomeBigHill() {
        super(60, 100, 10, 20, TerrainType.BIG_HILLS,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.SAVANNA)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "savanna_bighill");
    }

    /**
     * Overridden to return ShrublandBiomeBigHill.
     * @param temp
     * @param precip
     * @param type
     * @return
     */
    @Override
    public BaseTFCRBiome provideClosest(int temp, int precip, TerrainType type) {
        if (super.provideClosest(temp, precip, type) != null) {
            return resolve(ShrublandBiomeBigHill.class);
        }
        return null;
    }
}
