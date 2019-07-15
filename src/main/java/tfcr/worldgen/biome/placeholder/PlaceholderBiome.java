package tfcr.worldgen.biome.placeholder;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.DoublePlantConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.GrassFeatureConfig;
import net.minecraft.world.gen.feature.LakesConfig;
import net.minecraft.world.gen.feature.LiquidsConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.LakeChanceConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

public class PlaceholderBiome extends Biome {

    public TerrainType terrainType;

    public PlaceholderBiome(TerrainType type) {
        this(type, new Biome.Builder()
                .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                .category(Category.PLAINS)
                .depth(type.depth)
                .scale(type.scale)
                .downfall(0f)
                .precipitation(RainType.RAIN)
                .temperature(0.5f)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent("none"));

        // Caves
        this.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(WorldCarver.CAVE, new ProbabilityConfig(0.14285715F)));

        // Water lakes
        this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(Feature.LAKE, new LakesConfig(Blocks.WATER.getDefaultState()), Placement.WATER_LAKE, new LakeChanceConfig(4)));

        // Single water source blocks ("springs")
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.SPRING_FEATURE, new LiquidsConfig(Fluids.WATER.getDefaultState()), Placement.COUNT_BIASED_RANGE, new CountRangeConfig(50, 8, 8, 256)));

        // Villages (default; TODO make specific based on exact biome)
        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Feature.VILLAGE, new VillageConfig("village/plains/town_centers", 6), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));

        // Double tall plants (taken from plains)
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.DOUBLE_PLANT, new DoublePlantConfig(Blocks.TALL_GRASS.getDefaultState()), Placement.NOISE_HEIGHTMAP_32, new NoiseDependant(-0.8D, 0, 7)));

        // Single tall grass. (taken from plains)
//        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createCompositeFeature(Feature.TALL_GRASS, new TallGrassConfig(Blocks.GRASS.getDefaultState()), TWICE_SURFACE, new FrequencyConfig(4)));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.GRASS, new GrassFeatureConfig(Blocks.GRASS.getDefaultState()), Placement.NOISE_HEIGHTMAP_DOUBLE, new NoiseDependant(-0.8D, 5, 10)));

        // Sand/clay/gravel below surface
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(Blocks.SAND.getDefaultState(), 7, 2, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState())), Placement.COUNT_TOP_SOLID, new FrequencyConfig(3)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(Blocks.CLAY.getDefaultState(), 4, 1, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.CLAY.getDefaultState())), Placement.COUNT_TOP_SOLID, new FrequencyConfig(1)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(Blocks.GRAVEL.getDefaultState(), 6, 2, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState())), Placement.COUNT_TOP_SOLID, new FrequencyConfig(1)));

        // TODO add tree structures in. Use the existing Feature? Structure API?
    }

    protected PlaceholderBiome(TerrainType type, Biome.Builder builder) {
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
