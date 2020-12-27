package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import tfcr.data.TerrainType;

import javax.annotation.Nonnull;

/**
 * The first step in the worldgen algorithm.
 *
 * After some boundary conditions (where we return flatlands), there's a simple
 * 1 in 10 chance of turning an ocean tile into flat land.
 */
public enum TestLayer0 implements IAreaTransformer0 {
    INSTANCE;

    @Override
    public int apply(@Nonnull INoiseRandom rand, int x, int z) {
        if ((x / 16) % 2 == 0) {
            return TerrainType.OCEAN.ordinal();
        } else {
            return TerrainType.FLAT.ordinal();
        }
    }
}
