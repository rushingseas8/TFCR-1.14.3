package tfcr.worldgen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import tfcr.blocks.BranchBlock;
import tfcr.blocks.LeavesBlock;
import tfcr.data.WoodType;

import java.util.Random;
import java.util.Set;

/**
 * Temporary placeholder tree for TFCR. Enough to get some basic gameplay done while
 * working on fixing TreeFeatureTFCR.
 */
public class PlaceholderTreeFeatureTFCR extends TreeFeature {
//    public static final Feature<NoFeatureConfig> OAK_PLACEHOLDER = new PlaceholderTreeFeatureTFCR(false, 4, BranchBlock.get(WoodType.OAK, 10, false).getDefaultState(), LeavesBlock.get(WoodType.OAK).getDefaultState(), false);
    // TODO for some reason oak branches with diameter 10 and leafy=false freezes the game. I have no clue why, and no other blocks seem to have the same issue.
    public static final Feature<NoFeatureConfig> OAK = new TreeFeature(NoFeatureConfig::deserialize, false, 4, BranchBlock.get(WoodType.OAK, 8, false).getDefaultState(), LeavesBlock.get(WoodType.OAK).getDefaultState(), false);

    public PlaceholderTreeFeatureTFCR(boolean notify, int minTreeHeightIn, BlockState woodMeta, BlockState leafMeta, boolean growVines) {
        super(NoFeatureConfig::deserialize, notify, minTreeHeightIn, woodMeta, leafMeta, growVines);
    }

    @Override
    public boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox p_208519_5_) {
        return super.place(changedBlocks, worldIn, rand, position, p_208519_5_);
    }
}
