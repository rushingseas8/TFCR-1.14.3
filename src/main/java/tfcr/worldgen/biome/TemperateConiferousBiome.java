package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;
import tfcr.worldgen.PlaceholderTreeFeatureTFCR;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [30-65]
 */
public class TemperateConiferousBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.FOREST)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TemperateConiferousBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TemperateConiferousBiome(TerrainType type, Biome.Builder builder) {
        super(-30, 60, 30, 40, type, builder);

        // Tree spawning feature.
        // TODO create specific Feature that encapsulates all tree types, with frequency based on temp/precip,
        //  age (so most trees are middle-age, with rare old trees and medium rarity young trees), and height (mountains = no trees).
        // TODO This method freezes the game on load.
        // TODO Needs to spawn a TreeTileEntity as well- what about level 0 structures containing only saplings?
//        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, new CompositeFeature<>(TreeFeatureTFCR.INSTANCE, new TreeFeatureConfig(WoodType.OAK, 4), SURFACE_PLUS_32, new FrequencyConfig(2)));
//        addFeature(
//                GenerationStage.Decoration.VEGETAL_DECORATION,
//                new CompositeFeature<>(
//                        TreeFeatureTFCR.INSTANCE,
//                        new TreeFeatureConfig(WoodType.OAK, 5),
//                        TWICE_SURFACE,
//                        new FrequencyConfig(2)
//                )
//        );
//        this.addFeature(
//                GenerationStage.Decoration.VEGETAL_DECORATION,
//                createCompositeFeature(
//                        Feature.RANDOM_FEATURE_LIST,
//                        new RandomDefaultFeatureListConfig(
//                                new Feature[]{
//                                        Feature.BIRCH_TREE,
//                                        Feature.BIG_TREE
//                                },
//                                new IFeatureConfig[]{
//                                        IFeatureConfig.NO_FEATURE_CONFIG,
//                                        IFeatureConfig.NO_FEATURE_CONFIG
//                                },
//                                new float[]{
//                                        0.2F,
//                                        0.1F
//                                },
//                                Feature.TREE,
//                                IFeatureConfig.NO_FEATURE_CONFIG
//                        ),
//                        AT_SURFACE_WITH_EXTRA,
//                        new AtSurfaceWithExtraConfig(
//                                10,
//                                0.1F,
//                                1
//                        )
//                )
//        );
        this.addFeature(
                GenerationStage.Decoration.VEGETAL_DECORATION,createDecoratedFeature(
                        PlaceholderTreeFeatureTFCR.OAK_PLACEHOLDER,
                        new NoFeatureConfig(),
                        Placement.COUNT_HEIGHTMAP,
                        new FrequencyConfig(16)
                )
        );
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new TemperateConiferousBiome(TerrainType.FLAT),
                new TemperateConiferousBiome(TerrainType.SMALL_HILLS),
                new TemperateConiferousBiome(TerrainType.BIG_HILLS),
                new TemperateConiferousBiome(TerrainType.MOUNTAINS),
        };
    }
}
