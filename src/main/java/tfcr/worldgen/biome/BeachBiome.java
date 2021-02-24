package tfcr.worldgen.biome;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;
import tfcr.init.ModFluids;

public class BeachBiome extends BaseTFCRBiome {
    public BeachBiome() {
        super(-100, 100, 0, 100, TerrainType.BEACH,
                Blocks.STONE.getDefaultState(),
                ModFluids.SALT_WATER_FLUID_BLOCK.getDefaultState(),
                new Biome.Builder()
                        .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.SAND_SAND_GRAVEL_CONFIG)
                        .category(Category.BEACH)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "beach");
    }
}
