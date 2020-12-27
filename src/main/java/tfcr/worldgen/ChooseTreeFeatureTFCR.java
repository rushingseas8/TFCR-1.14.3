package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import tfcr.data.WoodType;
import tfcr.worldgen.biome.BaseTFCRBiome;

import java.util.Random;
import java.util.function.Function;

public class ChooseTreeFeatureTFCR extends Feature<NoFeatureConfig> {
    public static ChooseTreeFeatureTFCR INSTANCE = new ChooseTreeFeatureTFCR(NoFeatureConfig::deserialize);

    public ChooseTreeFeatureTFCR(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        // TODO at some point find a way to get the exact raw temp/precip values from the world
        // For now we're using the biome values.
        Biome rawBiome = worldIn.getBiome(pos);
        if (!(rawBiome instanceof BaseTFCRBiome)) {
            throw new IllegalStateException("Biome was not a TFCR biome, failed to get tree");
        }
        BaseTFCRBiome biome = (BaseTFCRBiome) rawBiome;
        float temperature = biome.getTemperatureDegrees(pos);

        double[] probabilities = new double[WoodType.values().length];
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = WoodType.values()[i].temperatureCurve.getValue(temperature);
        }
        double[] sums = new double[probabilities.length];
        sums[0] = probabilities[0];
        for (int i = 1; i < sums.length; i++) {
            sums[i] = sums[i - 1] + probabilities[i - 1];
        }

        double chance = rand.nextDouble() * sums[sums.length - 1];
        int foundIndex = sums.length - 1;
        for (int i = 0; i < sums.length - 1; i++) {
            if (chance >= sums[i] && chance < sums[i + 1]) {
                foundIndex = i;
                break;
            }
        }

        boolean shouldSpawn = true;
        if (sums[sums.length - 1] < 0.001) {
            shouldSpawn = false;
        } else {
            shouldSpawn = rand.nextFloat() < probabilities[foundIndex];
        }


//        PlaceholderTreeFeatureTFCR.OAK.place(worldIn, generator, rand, pos, config);
//        System.out.println("Chose tree type: " + WoodType.values()[foundIndex] + " for temp= " + temperature + ". Should spawn?: " + shouldSpawn);
        if (shouldSpawn) {
            PlaceholderTreeFeatureTFCR.INSTANCE.configure(WoodType.values()[foundIndex]);
            PlaceholderTreeFeatureTFCR.INSTANCE.place(worldIn, generator, rand, pos, config);
            return true;
        }

        return false;
    }
}
