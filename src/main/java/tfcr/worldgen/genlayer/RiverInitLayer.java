package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import tfcr.data.TerrainType;
import tfcr.worldgen.LayerUtilsTFCR;

/**
 * Initializes the river mask layer. Copied from Vanilla.
 *
 * If the biome is ocean, then its value is kept. Otherwise, return an absurdly
 * large random value (2 - 300000).
 */
public enum RiverInitLayer implements IC0Transformer {
    INSTANCE;


    public int apply(INoiseRandom rand, int value) {
        // If this is an ocean, then keep the ocean value.
        // Else, return an absurdly large range random value (?)
//        return TerrainType.values()[value] == TerrainType.OCEAN ? value : rand.random(299999) + 2;
//        return value == LayerUtilsTFCR.OCEAN ? value : 2 + rand.random(299998);
        return LayerUtilsTFCR.isWater(value) ? 0 : 2 + rand.random(299990);
    }
}
