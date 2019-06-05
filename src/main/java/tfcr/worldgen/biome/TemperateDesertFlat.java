package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-30-30], Precipitation: [0-10]
 */
public class TemperateDesertFlat extends BaseTFCRBiome {
    public TemperateDesertFlat() {
        super(-30, 30, 0, 10, TerrainType.FLAT,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, SAND_SAND_GRAVEL_SURFACE))
                        .category(Category.DESERT)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "temperate_desert_flat");
    }
}
