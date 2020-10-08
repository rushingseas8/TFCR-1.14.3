package tfcr.worldgen.genlayer;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.ImprovedNoiseGenerator;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import tfcr.data.TFCRTemperature;

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

    @Override
    public int apply(INoiseRandom rand, int x, int z) {
        if (!TFCRTemperature.isInitialized()) {
            TFCRTemperature.initialize(rand);
        }

        return TFCRTemperature.getWorldGen(x, z);
    }
}
