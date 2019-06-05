package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-60--30], Precipitation: [30-100]
 */
public class TaigaBiomeBigHill extends BaseTFCRBiome {
    public TaigaBiomeBigHill() {
        super(-60, -30, 30, 100, TerrainType.BIG_HILLS,
                (new BiomeBuilder())
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.TAIGA)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                );

        this.setRegistryName(TFCR.MODID, "taiga_bighill");
    }
}
