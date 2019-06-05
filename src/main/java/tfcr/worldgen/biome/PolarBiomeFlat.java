package tfcr.worldgen.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-100--60], Precipitation: [0-100]
 */
public class PolarBiomeFlat extends BaseTFCRBiome {
    public PolarBiomeFlat() {
        super(-100, -60, 0, 100, TerrainType.FLAT,
                (new BiomeBuilder())
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER,
                                new SurfaceBuilderConfig(Blocks.SNOW_BLOCK.getDefaultState(),
                                        Blocks.PACKED_ICE.getDefaultState(),
                                        Blocks.ICE.getDefaultState()
                                )))
                        .category(Category.ICY)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                );

        this.setRegistryName(TFCR.MODID, "polar_flat");
    }
}
