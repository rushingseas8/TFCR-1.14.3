package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-30-60], Precipitation: [65-100]
 */
public class TemperateRainforestMountain extends BaseTFCRBiome {
    public TemperateRainforestMountain() {
        super(-30, 60, 65, 100, TerrainType.MOUNTAINS,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.JUNGLE)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "temperate_rainforest_mountain");
    }
}
