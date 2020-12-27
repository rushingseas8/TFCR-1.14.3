package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import tfcr.data.WoodType;

import java.util.Random;
import java.util.function.Function;

/**
 * Placeholder for ChooseTreeFeature.
 *
 * This one only generates oak trees, but of all different ages. As more tree
 * types have structures associated with them, they can get added in.
 *
 * @see ChooseTreeFeatureTFCR for the one that uses temperature and precipitation
 *  to place trees down based on the proper biome maps.
 */
public class ChooseTreeFeatureTFCR2 extends Feature<NoFeatureConfig> {

    public static ChooseTreeFeatureTFCR2 INSTANCE = new ChooseTreeFeatureTFCR2(NoFeatureConfig::deserialize);

    private static final float[] AGE_DISTRIBUTION = new float[] {
            1, // sapling
            1, // tall sapling
            2, // young tree
            2,
            3,
            3, // adult tree
            3,
            5,
            5  // oldest tree
    };

    private static float[] AGE_SUMS = new float[AGE_DISTRIBUTION.length + 1];
    static {
        AGE_SUMS[0] = 0;
        // Compute sums
        for (int i = 0; i < AGE_DISTRIBUTION.length; i++) {
            AGE_SUMS[i + 1] = AGE_SUMS[i] + AGE_DISTRIBUTION[i];
        }
        // Normalize
        for (int i = 0; i < AGE_SUMS.length; i++) {
            AGE_SUMS[i] /= AGE_SUMS[AGE_SUMS.length - 1];
        }
    }

    public ChooseTreeFeatureTFCR2(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }


    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        float randomAge = rand.nextFloat();

        int age = 0;
        for (int i = 0; i < AGE_SUMS.length - 1; i++) {
            if (randomAge >= AGE_SUMS[i] && randomAge < AGE_SUMS[i + 1]) {
                age = i;
                break;
            }
        }

        return TreeFeatureTFCR.INSTANCE.place(worldIn, generator, rand, pos, new TreeFeatureConfig(WoodType.OAK, age));
    }
}
