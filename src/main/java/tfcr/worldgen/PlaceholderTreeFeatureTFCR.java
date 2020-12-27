package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import tfcr.blocks.BranchBlock;
import tfcr.blocks.LeavesBlock;
import tfcr.data.WoodType;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

/**
 * Temporary placeholder tree for TFCR. Enough to get some basic gameplay done while
 * working on fixing TreeFeatureTFCR.
 */
public class PlaceholderTreeFeatureTFCR extends AbstractTreeFeature<NoFeatureConfig> {
//    public static final Feature<NoFeatureConfig> OAK_PLACEHOLDER = new PlaceholderTreeFeatureTFCR(false, 4, BranchBlock.get(WoodType.OAK, 10, false).getDefaultState(), LeavesBlock.get(WoodType.OAK).getDefaultState(), false);
    // TODO for some reason oak branches with diameter 10 and leafy=false freezes the game. I have no clue why, and no other blocks seem to have the same issue.
    public static final Feature<NoFeatureConfig> OAK = new PlaceholderTreeFeatureTFCR(NoFeatureConfig::deserialize, false, 4, BranchBlock.get(WoodType.OAK, 8, false).getDefaultState(), LeavesBlock.get(WoodType.OAK).getDefaultState());
    public static final PlaceholderTreeFeatureTFCR INSTANCE = new PlaceholderTreeFeatureTFCR(NoFeatureConfig::deserialize, false, 4, BranchBlock.get(WoodType.OAK, 8, false).getDefaultState(), LeavesBlock.get(WoodType.OAK).getDefaultState());

    private int minTreeHeight;
    private BlockState woodMeta;
    private BlockState leafMeta;

    private PlaceholderTreeFeatureTFCR(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i51422_1_, boolean p_i51422_2_, int minTreeHeight, BlockState woodMeta, BlockState leafMeta) {
        super(p_i51422_1_, p_i51422_2_);
        this.minTreeHeight = minTreeHeight;
        this.woodMeta = woodMeta;
        this.leafMeta = leafMeta;
//        super(p_i51422_1_, p_i51422_2_, minTreeHeight, p_i51422_4_, leafMeta, p_i51422_6_);
    }

    public void configure(WoodType woodType) {
        this.woodMeta = BranchBlock.get(woodType, 10, false).getDefaultState();
        this.leafMeta = LeavesBlock.get(woodType).getDefaultState();
    }

    @Override
    public boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox p_208519_5_) {
        int i = this.minTreeHeight + rand.nextInt(3);
        boolean flag = true;
        if (position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getMaxHeight()) {
            for(int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                int k = 1;
                if (j == position.getY()) {
                    k = 0;
                }

                if (j >= position.getY() + 1 + i - 2) {
                    k = 2;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for(int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for(int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < worldIn.getMaxHeight()) {
                            if (!func_214587_a(worldIn, blockpos$mutableblockpos.setPos(l, j, i1))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else if (isSoil(worldIn, position.down(), getSapling()) && position.getY() < worldIn.getMaxHeight() - i - 1) {
                this.setDirtAt(worldIn, position.down(), position);
                int j2 = 3;
                int k2 = 0;

                for(int l2 = position.getY() - 3 + i; l2 <= position.getY() + i; ++l2) {
                    int l3 = l2 - (position.getY() + i);
                    int j4 = 1 - l3 / 2;

                    for(int j1 = position.getX() - j4; j1 <= position.getX() + j4; ++j1) {
                        int k1 = j1 - position.getX();

                        for(int l1 = position.getZ() - j4; l1 <= position.getZ() + j4; ++l1) {
                            int i2 = l1 - position.getZ();
                            if (Math.abs(k1) != j4 || Math.abs(i2) != j4 || rand.nextInt(2) != 0 && l3 != 0) {
                                BlockPos blockpos = new BlockPos(j1, l2, l1);
                                if (isAirOrLeaves(worldIn, blockpos) || func_214576_j(worldIn, blockpos)) {
                                    this.setLogState(changedBlocks, worldIn, blockpos, this.leafMeta, p_208519_5_);
                                }
                            }
                        }
                    }
                }

                for(int i3 = 0; i3 < i; ++i3) {
                    if (isAirOrLeaves(worldIn, position.up(i3)) || func_214576_j(worldIn, position.up(i3))) {
                        this.setLogState(changedBlocks, worldIn, position.up(i3), this.woodMeta, p_208519_5_);
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
