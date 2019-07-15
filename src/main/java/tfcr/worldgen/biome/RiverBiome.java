package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;
import tfcr.worldgen.MudFeature;

import static net.minecraft.world.gen.feature.IFeatureConfig.NO_FEATURE_CONFIG;
import static net.minecraft.world.gen.placement.IPlacementConfig.NO_PLACEMENT_CONFIG;

public class RiverBiome extends BaseTFCRBiome {
    public RiverBiome() {
        super(-100, 100, 0, 100, TerrainType.RIVER,
                new Biome.Builder()
                        .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                        .category(Category.RIVER)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "river");

        // TODO make it so that mud only spawns when directly adjacent to water, rather than
        //  everywhere in rivers below the surface.
        this.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, Biome.createDecoratedFeature(MudFeature.INSTANCE, NO_FEATURE_CONFIG, Placement.NOPE, NO_PLACEMENT_CONFIG));
    }
}
