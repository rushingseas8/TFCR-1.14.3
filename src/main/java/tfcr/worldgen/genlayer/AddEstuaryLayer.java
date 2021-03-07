package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.*;

/**
 * Adds Estuary TerrainTypes if the center is ocean, surrounded by at least one
 * river biome; or if the center is river, surrounded by at least one ocean.
 */
public enum AddEstuaryLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        // If we're ocean, and contain any river, then return estuary.
        // Same for rivers containing surrounding oceans.
        boolean hasRiver = LayerUtilsTFCR.hasTerrain(RIVER, south, east, north, west);
        boolean hasOcean = LayerUtilsTFCR.hasTerrain(OCEAN, south, east, north, west);
        if ((center == OCEAN && hasRiver) || (center == RIVER && hasOcean)) {
            return ESTUARY;
        }

        if (!LayerUtilsTFCR.isWater(center)) {
            if (hasRiver && hasOcean) {
                return RIVER_DELTA;
            }
        }

        // Otherwise, pass-through.
        return center;
    }
}
