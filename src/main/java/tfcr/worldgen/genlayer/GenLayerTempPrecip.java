package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.layer.traits.IC0Transformer;

import java.util.Random;

/**
 * TFCR custom gen. Applies temperature and precipitation to the map.
 *
 * Somewhat different from Vanilla worldgen in that it encapsulates its own
 * noise logic, instead of pulling from the context. Thus, it might behave
 * strangely with regards to zooming.
 */
public class GenLayerTempPrecip implements IC0Transformer {

    private static NoiseGeneratorOctaves temperature;
    private static NoiseGeneratorOctaves precipitation;

    public GenLayerTempPrecip(Random sharedSeed) {
        temperature = new NoiseGeneratorOctaves(sharedSeed, 2);
        precipitation = new NoiseGeneratorOctaves(sharedSeed, 2);
    }

    @Override
    public int apply(IContext context, int value) {
        return 0;
    }
}
