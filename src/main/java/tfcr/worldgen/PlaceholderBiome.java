package tfcr.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

public class PlaceholderBiome extends Biome {

    public TerrainType terrainType;

    protected PlaceholderBiome(TerrainType type) {
        this(type, new BiomeBuilder()
                .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                .category(Category.PLAINS)
                .depth(type.depth)
                .scale(type.scale)
                .downfall(0f)
                .precipitation(RainType.RAIN)
                .temperature(0.5f)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent("none"));
    }

    protected PlaceholderBiome(TerrainType type, BiomeBuilder builder) {
        super(builder);

        this.terrainType = type;
        this.setRegistryName(TFCR.MODID, "placeholder_" + type.name().toLowerCase());
    }

    // TODO override getTemperature() to change whether we get rain/snow based on season
    //  (this will probably involve getting a world reference somehow?)
    @Override
    public float getTemperature(BlockPos pos) {
        return super.getTemperature(pos);
    }
}
