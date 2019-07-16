package tfcr.worldgen;

import net.minecraft.block.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import tfcr.blocks.BranchBlock;
import tfcr.blocks.LeavesBlock;
import tfcr.data.WoodType;

/**
 * Temporary placeholder tree for TFCR. Enough to get some basic gameplay done while
 * working on fixing TreeFeatureTFCR.
 */
public class PlaceholderTreeFeatureTFCR extends TreeFeature {
    public static final Feature<NoFeatureConfig> OAK_PLACEHOLDER = new PlaceholderTreeFeatureTFCR(false, 4, BranchBlock.get(WoodType.OAK, 10, false).getDefaultState(), LeavesBlock.get(WoodType.OAK).getDefaultState(), false);

    public PlaceholderTreeFeatureTFCR(boolean notify, int minTreeHeightIn, BlockState woodMeta, BlockState leafMeta, boolean growVines) {
        super(notify, minTreeHeightIn, woodMeta, leafMeta, growVines);
    }
}
