package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
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
public class SavannaBiomeFlat extends BaseTFCRBiome {
    public SavannaBiomeFlat() {
        super(60, 100, 10, 20, TerrainType.FLAT,
                new Biome.BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.SAVANNA)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "savanna_flat");
    }
}
