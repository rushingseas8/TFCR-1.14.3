package tfcr.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import tfcr.worldgen.biome.BaseTFCRBiome;

// TODO: should there be a difference for air vs. ground vs. water temp?
//  The baseline assumption is air temperature for these computations.
public class TFCRTemperature {

    public static float getTemperature(World world, BlockPos pos) {
        return getBiomeTemperature(world, pos);
    }

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
        float timeOfDayScale = (float)(0.5 * (1.0 - Math.cos(2 * Math.PI * timeOfDay)));

        // The actual min/max scaling depends on the latitude and time of year;
        // e.g., +/- 23.5 degrees at the equator, with the 0 tilt points being
        // during the summer and winter solstices. With 0 tilt, there would be
        // the maximum possible contribution to day temp increase when timeOfDay
        // is at its local maximum contribution.

    }
}
