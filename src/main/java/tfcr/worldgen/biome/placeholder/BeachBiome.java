package tfcr.worldgen.biome.placeholder;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

public class BeachBiome extends PlaceholderBiome {

    public BeachBiome() {
        super(TerrainType.BEACH, new BiomeBuilder()
                .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, SAND_SAND_GRAVEL_SURFACE))
                .category(Category.BEACH)
                .depth(TerrainType.BEACH.depth)
                .scale(TerrainType.BEACH.scale)
                .downfall(0f)
                .precipitation(RainType.RAIN)
                .temperature(0.5f)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent("none")
        );
    }
}
