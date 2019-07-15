package tfcr.worldgen;

import net.minecraft.block.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import tfcr.blocks.BlockBranch;
import tfcr.blocks.BlockLeaves;
import tfcr.blocks.BlockLog;
import tfcr.blocks.IBlockWood;
import tfcr.data.WoodType;
import tfcr.utils.TemplateHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * Temporary placeholder tree for TFCR. Enough to get some basic gameplay done while
 * working on fixing TreeFeatureTFCR.
 */
public class PlaceholderTreeFeatureTFCR extends TreeFeature {
    public static final Feature<NoFeatureConfig> OAK_PLACEHOLDER = new PlaceholderTreeFeatureTFCR(false, 4, BlockBranch.get(WoodType.OAK, 10, false).getDefaultState(), BlockLeaves.get(WoodType.OAK).getDefaultState(), false);

    public PlaceholderTreeFeatureTFCR(boolean notify, int minTreeHeightIn, BlockState woodMeta, BlockState leafMeta, boolean growVines) {
        super(notify, minTreeHeightIn, woodMeta, leafMeta, growVines);
    }
}
