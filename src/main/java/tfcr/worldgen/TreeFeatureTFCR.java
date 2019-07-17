package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import tfcr.utils.TemplateHelper;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class TreeFeatureTFCR extends Feature<TreeFeatureConfig> {
    public static final Feature<TreeFeatureConfig> INSTANCE = new TreeFeatureTFCR(TreeFeatureConfig::deserialize);

    public TreeFeatureTFCR(Function<Dynamic<?>, ? extends TreeFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, TreeFeatureConfig config) {
        // TODO can't use templates directly, need to subclass ScatteredStructure
        //  (like Igloo or SwampHut) to prevent spawning blocks outside of chunk

        System.out.println("Generation? called");

        // TODO readd me
//        String templateName = TemplateHelper.getTreeTemplateLocation(config.getWoodType(), config.getAge());
        String templateName = "oak/age_1";
        Template template = TemplateHelper.getTemplate(worldIn.getWorld(), templateName);

        if (template == null) {
            System.out.println("Failed to get template: " + templateName);
            return false;
        }

        List<Template.BlockInfo> blocks = TemplateHelper.getBlocks(template);

        BlockPos size = template.getSize();
        BlockPos center = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);

        PlacementSettings settings = new PlacementSettings().setCenterOffset(center);

        System.out.println("Template adding blocks to world");
        return true;
    }
}
