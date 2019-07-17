package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import tfcr.data.TerrainType;

import javax.annotation.Nonnull;

/**
 * The first step in the worldgen algorithm.
 *
 * After some boundary conditions (where we return flatlands), there's a simple
 * 1 in 10 chance of turning an ocean tile into flat land.
 */
public enum GenLayerIsland implements IAreaTransformer0 {
    INSTANCE;

    @Override
    public int apply(@Nonnull IContext context, AreaDimension areaDimensionIn, int x, int z) {
        // Not sure what the first condition entails. Some boundary condition? Returns flat island
        if (x == -areaDimensionIn.getStartX() && z == -areaDimensionIn.getStartZ() && areaDimensionIn.getStartX() > -areaDimensionIn.getXSize() && areaDimensionIn.getStartX() <= 0 && areaDimensionIn.getStartZ() > -areaDimensionIn.getZSize() && areaDimensionIn.getStartZ() <= 0) {
            return TerrainType.FLAT.ordinal();
        } else {
            // 1 in 10 chance of choosing a flat island; otherwise, choose ocean.
            return context.random(10) == 0 ? TerrainType.FLAT.ordinal() : TerrainType.OCEAN.ordinal();
        }
    }
}
