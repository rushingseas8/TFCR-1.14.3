package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import tfcr.data.TerrainType;

import javax.annotation.Nonnull;

public enum GenLayerLandOnly implements IAreaTransformer0 {
    INSTANCE;

    @Override
    public int apply(@Nonnull IContext context, AreaDimension areaDimensionIn, int x, int z) {
        return TerrainType.FLAT.ordinal();
    }
}
