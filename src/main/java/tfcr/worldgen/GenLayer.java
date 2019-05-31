package tfcr.worldgen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;

import javax.annotation.Nullable;

/**
 * Custom implementation of GenLayer.
 *
 * The Vanilla version does a lookup of the Biome in registry- we actually want
 * to turn this into a special PlaceholderBiome representing a TerrainType instead.
 */
public class GenLayer {
    private final IAreaFactory<LazyArea> lazyAreaFactory;

    public GenLayer(IAreaFactory<LazyArea> lazyAreaFactoryIn) {
        this.lazyAreaFactory = lazyAreaFactoryIn;
    }

    public Biome[] generateBiomes(int startX, int startZ, int xSize, int zSize, @Nullable Biome defaultBiome) {
        AreaDimension areadimension = new AreaDimension(startX, startZ, xSize, zSize);
        LazyArea lazyarea = this.lazyAreaFactory.make(areadimension);
        Biome[] abiome = new Biome[xSize * zSize];

        int maxSize = BiomeProviderTFCR.biomes.length;

        for(int i = 0; i < zSize; ++i) {
            for(int j = 0; j < xSize; ++j) {
//                abiome[j + i * xSize] = Biome.getBiome(lazyarea.getValue(j, i), defaultBiome);

                int value = lazyarea.getValue(j, i);
                abiome[j + i * xSize] = value >= maxSize ? defaultBiome : BiomeProviderTFCR.biomes[value];
            }
        }

        return abiome;
    }
}
