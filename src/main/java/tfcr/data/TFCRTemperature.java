package tfcr.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.ImprovedNoiseGenerator;
import tfcr.worldgen.biome.BaseTFCRBiome;

// TODO: should there be a difference for air vs. ground vs. water temp?
//  The baseline assumption is air temperature for these computations.
public class TFCRTemperature {

    private static boolean initialized = false;

    private static ImprovedNoiseGenerator noiseGenerator = null;

    private static double tempOffsetX;
    private static double tempOffsetZ;

    private static double precipOffsetX;
    private static double precipOffsetZ;

    // Scale factor on noise.
    // The value of this is roughly half the diameter of a thermal biome, since
    // we apply a fuzzy zoom on top of this genlayer.
    private static final double noiseScaleX = 1000f;
    private static final double noiseScaleZ = 1000f;

    public static boolean isInitialized() {
        return initialized;
    }

    // Initialized by worldgen, see TempPrecipLayer
    public static void initialize(INoiseRandom rand) {
        // One-off setup
        if (!initialized) {
            tempOffsetX = 1 + rand.random(1000);
            tempOffsetZ = 1 + rand.random(1000);

            precipOffsetX = 1 + rand.random(1000);
            precipOffsetZ = 1 + rand.random(1000);

            noiseGenerator = rand.getNoiseGenerator();

            initialized = true;
        }
    }

    public static float getTemperature_01(BlockPos pos) {
        double noiseX = pos.getX() / noiseScaleX;
        double noiseZ = pos.getZ() / noiseScaleZ;

        // Get temperature and precipitation as a randomly seeded Perlin noise value.
        // TODO this is just one iteration, check if this is enough (or fuzz)
        // TODO check if this is the right method call on the noise function
        // TODO unreproduceable, but one time noiseGenerator was null upon reload.
        double tempRaw = noiseGenerator.func_215456_a(noiseX + tempOffsetX, noiseZ + tempOffsetZ, 0, 0, 0);

        return (float) ((tempRaw + 1.0) / 2.0); // [-1, 1] -> [0, 2] -> [0, 1]
    }

    public static float getTemperatureDegrees(BlockPos pos) {
        double noiseX = pos.getX() / noiseScaleX;
        double noiseZ = pos.getZ() / noiseScaleZ;

        // Get temperature and precipitation as a randomly seeded Perlin noise value.
        // TODO this is just one iteration, check if this is enough (or fuzz)
        // TODO check if this is the right method call on the noise function
        double tempRaw = noiseGenerator.func_215456_a(noiseX + tempOffsetX, noiseZ + tempOffsetZ, 0, 0, 0);

        return (int) (tempRaw * 100.0); // [-1, 1] -> [-100, 100]
    }

    public static float getPrecipitation_01(BlockPos pos) {
        double noiseX = pos.getX() / noiseScaleX;
        double noiseZ = pos.getZ() / noiseScaleZ;

        // Get temperature and precipitation as a randomly seeded Perlin noise value.
        // TODO this is just one iteration, check if this is enough (or fuzz)
        // TODO check if this is the right method call on the noise function
        double precipRaw = noiseGenerator.func_215456_a(noiseX + precipOffsetX, noiseZ + precipOffsetZ, 0, 0, 0);

        return (float) ((precipRaw + 1.0) / 2.0); // [-1, 1] -> [0, 2] -> [0, 1]
    }

    public static float getPrecipitation(BlockPos pos) {
        double noiseX = pos.getX() / noiseScaleX;
        double noiseZ = pos.getZ() / noiseScaleZ;

        // Get temperature and precipitation as a randomly seeded Perlin noise value.
        // TODO this is just one iteration, check if this is enough (or fuzz)
        // TODO check if this is the right method call on the noise function
        double precipRaw = noiseGenerator.func_215456_a(noiseX + precipOffsetX, noiseZ + precipOffsetZ, 0, 0, 0);

        return (int) ((precipRaw + 1.0) * 50.0); // [-1, 1] -> -> [0, 2] -> [0, 100]
    }

    // Optimized worldgen-specific code that mirrors getTemperature and getPrecipitation above.
    // This is intended to minimize the amount of function calls made during worldgen.
    public static int getWorldGen(int x, int z) {
        double noiseX = x / noiseScaleX;
        double noiseZ = z / noiseScaleZ;

        // Get temperature and precipitation as a randomly seeded Perlin noise value.
        // TODO this is just one iteration, check if this is enough (or fuzz)
        // TODO check if this is the right method call on the noise function
        double tempRaw = noiseGenerator.func_215456_a(noiseX + tempOffsetX, noiseZ + tempOffsetZ, 0, 0, 0);
        double precipRaw = noiseGenerator.func_215456_a(noiseX + precipOffsetX, noiseZ + precipOffsetZ, 0, 0, 0);

        // Perlin noise goes from [-1, 1], so we adjust it to the correct ranges here.
        int temp = (int) ((tempRaw + 1.0) * 100.0); // [-1, 1] -> [0, 2] -> [0, 200]
        int precip = (int) ((precipRaw + 1.0) * 50.0); // [-1, 1] -> -> [0, 2] -> [0, 100]

        // Ensure it's within range (some perlin implementations allow slight deviations from the range)
        temp = MathHelper.clamp(temp, 0, 199);
        precip = MathHelper.clamp(precip, 0, 99);

        // Return a 16 bit value. This will be used later to resolve the actual biome value.
        return (temp << 8) | precip;
    }

//    public static float getTemperature(World world, BlockPos pos) {
//        return getBiomeTemperature(world, pos);
//    }

    /**
     * Get the contribution to the local temperature due to the biome.
     */
    public static float getBiomeTemperature(World world, BlockPos pos) {
        Biome biome = world.getBiome(pos);

        // If this biome is not a TFCR biome, make a best guess.
        if (!(biome instanceof BaseTFCRBiome)) {
            return 100 * biome.getTemperature(pos); // Normalize [0, 1] to [0, 100].]
        }

        // If we have a TFCR biome, return the temperature based on the time of year.
        BaseTFCRBiome tfcrBiome = (BaseTFCRBiome) biome;
        return tfcrBiome.getTemperature(TFCRTime.getTimeOfYear());
    }

    /**
     * Get the contribution to the local temperature due to the time of day.
     */
    public static float getDayTemperature(World world, BlockPos pos) {
        float timeOfDay = TFCRTime.getTimeOfDay();
        // There is a minimum just before sunrise, and a maximum just after sunset.
        // If sunrise/sunset times change, this needs to be accounted for.
        // With vanilla, the min is at 0/1, and the max is at 0.5.
        float timeOfDayScale = (float)(-Math.cos(2 * Math.PI * timeOfDay));

        // The actual min/max scaling depends on the latitude and time of year;
        // e.g., +/- 23.5 degrees at the equator, with the 0 tilt points being
        // during the summer and winter solstices. With 0 tilt, there would be
        // the maximum possible contribution to day temp increase when timeOfDay
        // is at its local maximum contribution.

        // For now we go with a simple solution where the variance is 20 degrees
        // from avg. low to avg. high. This hits -10 at sunrise, and +10 at sunset.
        return timeOfDayScale * 10f;
    }

    /**
     * Each day, there's a random target temperature which can modify the day's temp
     * by +/- 10 degrees. Essentially, think how adjacent days in a season still have
     * a small amount of variance to them.
     *
     * This function computes the temperature randomness for a given day (which is
     * computed using a fixed seed value), as well as the surrounding days. It then
     * interpolates between yesterday and tomorrow to obtain a smooth transition between
     * the different day temperatures.
     *
     * It returns a smooth random offset, with peaks occurring each day, ranging from
     * -10 to +10.
     *
     * TODO: implement cubic splines for this or something similar
     */
    public static float getInterDayRandomness(World world, BlockPos pos) {
        return 0;
    }
}
