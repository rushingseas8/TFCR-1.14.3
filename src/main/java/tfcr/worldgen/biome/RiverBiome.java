package tfcr.worldgen.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.CompositeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import tfcr.TFCR;
import tfcr.blocks.BlockMud;
import tfcr.data.TerrainType;
import tfcr.init.ModBlocks;
import tfcr.worldgen.MudFeature;
import tfcr.worldgen.biome.BaseTFCRBiome;

import java.util.Random;

import static net.minecraft.world.gen.feature.IFeatureConfig.NO_FEATURE_CONFIG;
import static net.minecraft.world.gen.placement.IPlacementConfig.NO_PLACEMENT_CONFIG;

public class RiverBiome extends BaseTFCRBiome {
    public RiverBiome() {
        super(-100, 100, 0, 100, TerrainType.RIVER,
                new BiomeBuilder()
                        .surfaceBuilder(
                                new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER,
                                        new SurfaceBuilderConfig(
                                                GRASS_BLOCK,
                                                DIRT,
                                                GRAVEL)))
                        .category(Category.RIVER)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "river");

        // TODO make it so that mud only spawns when directly adjacent to water, rather than
        //  everywhere in rivers below the surface.
        this.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, new CompositeFeature<>(MudFeature.INSTANCE, NO_FEATURE_CONFIG, PASSTHROUGH, NO_PLACEMENT_CONFIG));
    }
}
