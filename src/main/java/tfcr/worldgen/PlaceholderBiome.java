package tfcr.worldgen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

public class PlaceholderBiome extends Biome {

    public TerrainType terrainType;

    protected PlaceholderBiome(TerrainType type) {
        super(new BiomeBuilder()
                .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                .category(Category.DESERT)
                .depth(type.depth)
                .scale(type.scale)
                .downfall(0f)
                .precipitation(RainType.RAIN)
                .temperature(0f)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent("none")
        );

        this.terrainType = type;
        this.setRegistryName(TFCR.MODID, "placeholder_" + type.name().toLowerCase());
    }
}
