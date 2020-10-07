package tfcr.worldgen.genlayer;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.ImprovedNoiseGenerator;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

import java.util.Random;

/**
 * TFCR custom gen. Applies temperature and precipitation to the map.
 *
 * TODO: The nature of perlin noise means that the values approximate a gaussian curve.
 * This means that values closer to the middle are more likely, and essentially that
 * biomes on the "corners" of the temp/precip map are significantly less likely than values
 * in the center. We can either move the biomes around to account for this, with the more
 * common ones in the center and rarer ones out to the edges (less wetland); or just
 * fuzz the perlin noise such that the curve is more spread out to the edges (apply a transform
 * such that extreme values are more common, but smoothly so there's no spikes).
 */
public enum TempPrecipLayer implements IAreaTransformer0 {
    INSTANCE;

    private static boolean initialized = false;

    private static double tempOffsetX;
    private static double tempOffsetZ;

    private static double precipOffsetX;
    private static double precipOffsetZ;

    // Scale factor on noise.
    // The value of this is roughly half the diameter of a thermal biome, since
    // we apply a fuzzy zoom on top of this genlayer.
    private static final double noiseScaleX = 500f;
    private static final double noiseScaleZ = 500f;

    @Override
    public int apply(INoiseRandom rand, int x, int z) {
        // One-off setup
        if (!initialized) {
            tempOffsetX = 1 + rand.random(1000);
            tempOffsetZ = 1 + rand.random(1000);

            precipOffsetX = 1 + rand.random(1000);
            precipOffsetZ = 1 + rand.random(1000);

            initialized = true;
        }

        ImprovedNoiseGenerator noiseGen = rand.getNoiseGenerator();

        double noiseX = x / noiseScaleX;
        double noiseZ = z / noiseScaleZ;

        // Get temperature and precipitation as a randomly seeded Perlin noise value.
        // TODO this is just one iteration, check if this is enough (or fuzz)
        // TODO check if this is the right method call on the noise function
        double tempRaw = noiseGen.func_215456_a(noiseX + tempOffsetX, noiseZ + tempOffsetZ, 0, 0, 0);
        double precipRaw = noiseGen.func_215456_a(noiseX + precipOffsetX, noiseZ + precipOffsetZ, 0, 0, 0);

        // Perlin noise goes from [-1, 1], so we adjust it to the correct ranges here.
        int temp = (int) ((tempRaw + 1.0) * 100.0); // [-1, 1] -> [0, 2] -> [0, 200]
        int precip = (int) ((precipRaw + 1.0) * 50.0); // [-1, 1] -> -> [0, 2] -> [0, 100]

        // Ensure it's within range (some perlin implementations allow slight deviations from the range)
        temp = MathHelper.clamp(temp, 0, 199);
        precip = MathHelper.clamp(precip, 0, 99);

        // Return a 16 bit value. This will be used later to resolve the actual biome value.
        return (temp << 8) | precip;
    }
}
