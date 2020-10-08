package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import tfcr.worldgen.biome.BaseTFCRBiome;

import java.util.Random;
import java.util.function.Function;

public class ChooseTreeFeatureTFCR extends Feature<NoFeatureConfig> {
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


        return false;
    }
}
