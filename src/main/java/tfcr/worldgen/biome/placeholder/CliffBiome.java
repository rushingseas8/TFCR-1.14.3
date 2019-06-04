package tfcr.worldgen.biome.placeholder;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

public class CliffBiome extends PlaceholderBiome {

    public CliffBiome() {
        super(TerrainType.CLIFF, new BiomeBuilder()
                .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, STONE_STONE_GRAVEL_SURFACE))
                .category(Category.BEACH)
                .depth(TerrainType.CLIFF.depth)
                .scale(TerrainType.CLIFF.scale)
                .downfall(0f)
                .precipitation(RainType.RAIN)
                .temperature(0.2f)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent("none"));
    }
}
