package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import tfcr.data.TerrainType;

/**
 * Based on GenLayerBiome in Vanilla.
 * See net.minecraft.world.gen.layer.GenLayerBiome.
 *
 * Determines the specific Biomes that we place in the world based on adjacency
 * rules. Vanilla uses a temperature system to determine compatibility; we use
 * a TerrainType based system instead.
 */
public class GenLayerBiome implements IC0Transformer {

    // TODO replace me with custom Settings
    private OverworldGenSettings settings;

    public GenLayerBiome(OverworldGenSettings settings) {
        this.settings = settings;
    }

    @Override
    public int apply(IContext context, int value) {
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
