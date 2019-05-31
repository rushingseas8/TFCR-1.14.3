package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

/**
 * Based on Vanilla GenLayerBiomeEdge.
 * See net.minecraft.world.gen.layer.GenLayerBiomeEdge.
 *
 * This class handles the fuzzing of Biome boundaries. Specifically, it looks
 * at the surrounding areas in this order (R is the root, where we're modifying):
 *
 * - 2 -
 * 3 4 1
 * R 0 -
 *
 * The Vanilla code returns a modified Biome based on adjacency rules. One example
 * is looking at surrounding areas 0, 1, 2, and 3, and if they're all compatible with
 * or equal to Mountains, then it'll try to replace 4 with a MountainEdge. Similarly,
 * if the center (4) is Swamp, and the remaining borders are Desert, SnowyTaiga, or
 * SnowyTundra, then it'll return Plains.
 *
 * Since we only care about TerrainTypes, we will try our best to provide intermediate
 * TerrainTypes between extreme values.
 *
 * TODO for now we just pass-through center. Later we'll do better logic.
 * TODO explain details of algorithm
 */
public enum GenLayerBiomeEdge implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(IContext context, int south, int east, int north, int west, int center) {
        return center;
    }
}
