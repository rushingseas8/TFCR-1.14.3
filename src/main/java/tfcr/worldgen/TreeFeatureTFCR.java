package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.IPlantable;
import tfcr.tileentity.TreeTileEntity;
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

//        System.out.println("Generation? called");

        // TODO readd me
        String templateName = TemplateHelper.getTreeTemplateLocation(config.getWoodType(), config.getAge());
        Template template = TemplateHelper.getTemplate(worldIn.getWorld(), templateName);

        if (template == null) {
            System.out.println("Failed to get template: " + templateName);
            return false;
        }

        BlockPos size = template.getSize();
        BlockPos center = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);

        // Check that the placing block is valid. It's the pos + the center offset.
        Block placingBlock = worldIn.getBlockState(pos.add(center).down()).getBlock();
        Block baseBlock = worldIn.getBlockState(pos.add(center)).getBlock();
        if (!(placingBlock instanceof SnowyDirtBlock)) {
            return false;
        }
        if (!(worldIn.isAirBlock(pos.add(center))) && !(baseBlock instanceof IPlantable)) {
            return false;
        }

        // TODO: This works, but because worldgen is done in parallel, there's no way to tell if we're growing into
        //  a neighboring tree. Either use external storage, or just don't let trees get too close.
        // (could create all trees in one place() call, and keep track of all leaves generated per chunk?)
        PlacementSettings settings = new PlacementSettings().setCenterOffset(center);
        settings.addProcessor(new TreeTileEntity.TemplateProcessorTrees(config.getWoodType()));
//        System.out.println("Template adding blocks to world");
        template.addBlocksToWorld(worldIn, pos, settings, 2);


        return true;
    }
}
