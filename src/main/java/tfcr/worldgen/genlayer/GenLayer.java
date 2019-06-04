package tfcr.worldgen.genlayer;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import tfcr.worldgen.BiomeProviderTFCR;

import javax.annotation.Nullable;

/**
 * Custom implementation of GenLayer. This class is used to lookup the int
 * return values from all the GenLayer* classes, and resolve them into Biomes.
 *
 * The Vanilla version does a lookup of the Biome using Biome.getBiome. Since
 * this is hardcoded to use the registry, we provide a custom implementation
 * that turns the ID into a special PlaceholderBiome instead. Each placeholder
 * maps to a TerrainType, which represents a general height of terrain (or
 * sometimes technical biomes, like River or Beach).
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

        int maxSize = BiomeProviderTFCR.placeholderBiomes.length;

        for(int i = 0; i < zSize; ++i) {
            for(int j = 0; j < xSize; ++j) {
//                abiome[j + i * xSize] = Biome.getBiome(lazyarea.getValue(j, i), defaultBiome);

                int value = lazyarea.getValue(j, i);
                abiome[j + i * xSize] = value >= maxSize ? defaultBiome : BiomeProviderTFCR.placeholderBiomes[value];
            }
        }

        return abiome;
    }
}
