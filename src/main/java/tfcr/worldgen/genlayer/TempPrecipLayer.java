package tfcr.worldgen.genlayer;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.ImprovedNoiseGenerator;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

import java.util.Random;

/**
 * TFCR custom gen. Applies temperature and precipitation to the map.
 */
public class TempPrecipLayer implements IAreaTransformer0 {

    private static double tempOffsetX;
    private static double tempOffsetZ;

    private static double precipOffsetX;
    private static double precipOffsetZ;

    // Scale factor on noise.
    // The value of this is roughly half the diameter of a thermal biome, since
    // we apply a fuzzy zoom on top of this genlayer.
    private static final double noiseScaleX = 500f;
    private static final double noiseScaleZ = 500f;

    public TempPrecipLayer(Random random) {
        // We provide an offset to the Perlin noise based on the world seed.
        // This ensures thermal biomes are more or less unique per world.
        // (Technically, based on the noise scaling parameters here, you can
        // expect repetition on the order of ~500,000 blocks. I think that's
        // an acceptable level.)
        tempOffsetX = random.nextInt(1000);
        tempOffsetZ = random.nextInt(1000);

        precipOffsetX = random.nextInt(1000);
        precipOffsetZ = random.nextInt(1000);
    }

    @Override
    public int apply(INoiseRandom rand, int x, int z) {
        ImprovedNoiseGenerator noiseGen = rand.getNoiseGenerator();

        double noiseX = x / noiseScaleX;
        double noiseZ = z / noiseScaleZ;

        // Get temperature and precipitation as a randomly seeded Perlin noise value.
        // TODO this is just one iteration, check if this is enough (or fuzz)
        // TODO check if this is the right method call on the noise function
        double tempRaw = noiseGen.func_215456_a(noiseX + tempOffsetX, noiseZ + tempOffsetZ, 0, 0, 0);
        double precipRaw = noiseGen.func_215456_a(noiseX + precipOffsetX, noiseZ + precipOffsetZ, 0, 0, 0);

        int temp = (int)((tempRaw / 2.0) * 100.0); // Temperature ranges from -100 to 100
        int precip = (int)(((precipRaw / 4.0) + 0.5) * 100.0); // Precipitation ranges from 0 to 100

        // Ensure it's within range
        temp = MathHelper.clamp(temp, -100, 99);
        precip = MathHelper.clamp(precip, 0, 99);

        temp += 100; // Normalize [-100, 99] -> [0, 199] to fit in positive byte space

        // Return a 16 bit value. This will be used later to resolve the actual biome value.
        return (temp << 8) | precip;
    }
}
