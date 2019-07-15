package tfcr.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import tfcr.tileentity.TileEntityTree;
import tfcr.utils.TemplateHelper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TreeFeatureTFCR extends Feature<TreeFeatureConfig> {
    public static final Feature<TreeFeatureConfig> INSTANCE = new TreeFeatureTFCR();

    /**
     *
     * @param world
     * @param chunkGenerator
     * @param random
     * @param blockPos
     * @param config
     * @return true if we were placed, false if we failed
     */
    @Override
    public boolean func_212245_a(@Nonnull IWorld world, @Nonnull IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, @Nonnull Random random, @Nonnull BlockPos blockPos, @Nonnull TreeFeatureConfig config) {
        System.out.println("Generation? called");

        String templateName = TemplateHelper.getTreeTemplateLocation(config.getWoodType(), config.getAge());
        Template template = TemplateHelper.getTemplate(world.getWorld(), templateName);

        if (template == null) {
            System.out.println("Failed to get template: " + templateName);
            return false;
        }

        List<Template.BlockInfo> blocks = TemplateHelper.getBlocks(template);

        BlockPos size = template.getSize();
        BlockPos center = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);

        PlacementSettings settings = new PlacementSettings().setCenterOffset(center);

        System.out.println("Template adding blocks to world");
        // TODO can't use templates directly, need to subclass ScatteredStructure
        //  (like Igloo or SwampHut) to prevent spawning blocks outside of chunk
//        boolean success = template.addBlocksToWorld(
//                world,
//                blockPos.add(-center.getX(), 0, -center.getZ()),
//                new TileEntityTree.TemplateProcessorTrees(config.getWoodType()),
//                settings,
//                3
//        );
//        System.out.println("Success?: " + success);

        return true;
    }
}
