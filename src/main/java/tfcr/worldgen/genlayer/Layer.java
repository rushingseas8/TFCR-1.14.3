package tfcr.worldgen.genlayer;

import net.minecraft.util.SharedConstants;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import tfcr.worldgen.BiomeProviderTFCR;

import javax.annotation.Nullable;

import static net.minecraft.world.biome.Biome.LOGGER;

/**
 * Custom implementation of Layer. This class is used to lookup the int
 * return values from all the Layer* classes, and resolve them into Biomes.
 *
 * The Vanilla version does a lookup of the Biome using Biome.getBiome. Since
 * this is hardcoded to use the registry, we provide a custom implementation
 * that turns the ID into a special PlaceholderBiome instead. Each placeholder
 * maps to a TerrainType, which represents a general height of terrain (or
 * sometimes technical biomes, like River or Beach).
 */
public class Layer {
//    private final IAreaFactory<LazyArea> lazyAreaFactory;
    private final LazyArea lazyArea;

    public Layer(IAreaFactory<LazyArea> lazyAreaFactoryIn) {
        this.lazyArea = lazyAreaFactoryIn.make();
    }

    public Biome[] generateBiomes(int startX, int startZ, int xSize, int zSize, @Nullable Biome defaultBiome) {
        Biome[] abiome = new Biome[xSize * zSize];

        int maxSize = BiomeProviderTFCR.biomes.length;

        for(int i = 0; i < zSize; ++i) {
            for(int j = 0; j < xSize; ++j) {

//                int value = lazyarea.getValue(j, i);
                int value = this.lazyArea.getValue(startX + j, startZ + i);
                System.out.println("Value: " + value + " corresponds to: " + BiomeProviderTFCR.biomes[value]);
                abiome[j + i * xSize] = value >= maxSize ? defaultBiome : BiomeProviderTFCR.biomes[value];
            }
        }

        return abiome;
    }

    public Biome getByID(int id) {
        if (id > BiomeProviderTFCR.biomes.length) {
            if (SharedConstants.developmentMode) {
                throw new IllegalStateException("Unknown biome id: " + id);
            } else {
                LOGGER.warn("Unknown biome id: ", id);
                return Biomes.DEFAULT;
            }
        } else {
            return BiomeProviderTFCR.biomes[id];
        }
    }

    public Biome getByPos(int x, int z) {
        return this.getByID(this.lazyArea.getValue(x, z));
    }
}
