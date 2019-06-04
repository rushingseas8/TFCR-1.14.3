package tfcr.worldgen.biome.placeholder;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

public class RiverBiome extends PlaceholderBiome {
    public RiverBiome() {
        super(TerrainType.RIVER, new BiomeBuilder()
                .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                .category(Category.RIVER)
                .depth(TerrainType.RIVER.depth)
                .scale(TerrainType.RIVER.scale)
                .downfall(0f)
                .precipitation(RainType.RAIN)
                .temperature(0.5f)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent("none")
        );
    }
}
