package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import tfcr.data.TerrainType;

/**
 * Initializes the river mask layer. Copied from Vanilla.
 *
 * If the biome is ocean, then its value is kept. Otherwise, return an absurdly
 * large random value (2 - 300001).
 */
public enum GenLayerRiverInit implements IC0Transformer {
    INSTANCE;


    public int apply(IContext context, int value) {
        // If this is an ocean, then keep the ocean value.
        // Else, return an absurdly large range random value (?)
        return TerrainType.values()[value] == TerrainType.OCEAN ? value : context.random(299999) + 2;
    }
}
