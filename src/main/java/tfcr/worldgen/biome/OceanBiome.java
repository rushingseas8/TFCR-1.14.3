package tfcr.worldgen.biome;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;
import tfcr.init.ModFluids;

public class OceanBiome extends BaseTFCRBiome {
    public OceanBiome() {
        super(-100, 100, 0, 100, TerrainType.OCEAN,
                Blocks.STONE.getDefaultState(),
                ModFluids.SALT_WATER.fluidBlock.getDefaultState(),
                new Biome.Builder()
                        .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                        .category(Category.OCEAN)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "ocean");
    }
}
