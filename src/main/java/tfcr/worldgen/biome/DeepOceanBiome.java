package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

public class DeepOceanBiome extends BaseTFCRBiome {
    public DeepOceanBiome() {
        super(-100, 100, 0, 100, TerrainType.DEEP_OCEAN,
                new Biome.Builder()
                        .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                        .category(Category.OCEAN)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "deep_ocean");
    }
}
