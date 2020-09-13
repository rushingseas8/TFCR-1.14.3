package tfcr.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import tfcr.worldgen.biome.BaseTFCRBiome;

public class TFCRTemperature {

    public static float getTemperature(World world, BlockPos pos) {
    }

    public static float getBiomeTemperature(World world, BlockPos pos) {
        Biome biome = world.getBiome(pos);
        if (!(biome instanceof BaseTFCRBiome)) {
            return biome.getTemperature(pos);
        }
        BaseTFCRBiome tfcrBiome = (BaseTFCRBiome) biome;
        // TODO expose temperature curve in base biome, and return the contribution based on the time of year.
        // (should look like a sine wave with a high during july and low during jan)
    }
}
