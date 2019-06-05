package tfcr.worldgen.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [0-10]
 *
 * Spawns using the mesa surface generation, except with sand as its top block.
 */
public class DesertBiomeBigHill extends BaseTFCRBiome {
    public DesertBiomeBigHill() {
        super(60, 100, 0, 10, TerrainType.BIG_HILLS,
                (new Biome.BiomeBuilder())
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(MESA_SURFACE_BUILDER,
                                new SurfaceBuilderConfig(
                                        Blocks.SAND.getDefaultState(),
                                        Blocks.WHITE_TERRACOTTA.getDefaultState(),
                                        Blocks.GRAVEL.getDefaultState())))
                        .category(Category.DESERT)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "desert_bighill");
    }
}
