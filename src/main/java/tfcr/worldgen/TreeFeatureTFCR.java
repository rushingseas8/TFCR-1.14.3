package tfcr.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import tfcr.utils.TemplateHelper;

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
    public boolean func_212245_a(IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, Random random, BlockPos blockPos, TreeFeatureConfig config) {
        String templateName = TemplateHelper.getTreeTemplateLocation(config.getWoodType(), config.getAge());
        Template template = TemplateHelper.getTemplate(world.getWorld(), templateName);

        if (template == null) {
            System.out.println("Failed to get template: " + templateName);
            return false;
        }

        template.addBlocksToWorld(world, blockPos, new PlacementSettings());

        //BlockPos size = template.getSize();

        return true;
    }
}
