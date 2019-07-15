package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import tfcr.data.TerrainType;

/**
 * Based on BiomeLayer in Vanilla.
 * See net.minecraft.world.gen.layer.BiomeLayer.
 *
 * In Vanilla, this method randomly chooses a Biome based on built-in temperature
 * rules (ocean, cold, warm, or hot). It also handles some random variation that
 * makes sense (e.g. badlands plateau vs. wooded badlands).
 *
 * In TFCR, since our worldgen only uses height-based logic for determining
 * placeholder biomes, this method is essentially a no-op. Temperature based
 * variations will be applied later when temperature/precipitation is known.
 */
public class BiomeLayer implements IC0Transformer {

    // TODO replace me with custom Settings
    private OverworldGenSettings settings;

    public BiomeLayer(OverworldGenSettings settings) {
        this.settings = settings;
    }

    @Override
    public int apply(INoiseRandom rand, int value) {
        TerrainType inputType = TerrainType.values()[value];

        // Oceans pass-through
        if (inputType == TerrainType.DEEP_OCEAN || inputType == TerrainType.OCEAN) {
            return value;
        }

        // Else, we have a temperature? We'll try to treat it like a height I guess?
//        int temp = (value & 3840) >> 8;
        if (inputType == TerrainType.RIVER) {
            System.out.println("Found river biome.");
            return value;
        }
        return value;
    }
}
