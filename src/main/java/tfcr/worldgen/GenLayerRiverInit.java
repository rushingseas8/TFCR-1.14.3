package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import tfcr.data.TerrainType;

public enum GenLayerRiverInit implements IC0Transformer {
    INSTANCE;


    public int apply(IContext context, int value) {
        // If this is an ocean, then keep the ocean value.
        // Else, return an absurdly large range random value (?)
        return TerrainType.values()[value] == TerrainType.OCEAN ? value : context.random(299999) + 2;
    }
}
