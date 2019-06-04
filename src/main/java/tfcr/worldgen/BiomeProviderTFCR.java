package tfcr.worldgen;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.storage.WorldInfo;
import tfcr.data.TerrainType;
import tfcr.worldgen.biome.DesertBiomeFlat;
import tfcr.worldgen.biome.placeholder.BeachBiome;
import tfcr.worldgen.biome.placeholder.CliffBiome;
import tfcr.worldgen.biome.placeholder.PlaceholderBiome;
import tfcr.worldgen.biome.placeholder.RiverBiome;
import tfcr.worldgen.genlayer.GenLayer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The BiomeProvider for TFCR. Closely based on OverworldBiomeProvider.
 *
 * This is a parent class that runs all of the custom worldgen this mod does,
 * mirroring how OverworldBiomeProvider calls LayerUtils.buildOverworldProcedure()
 * to generate the biome map.
 *
 * This class also stores static references to Placeholder placeholderBiomes, which will
 * later be combined with the world's temperature and precipitation maps to
 * obtain final resolved placeholderBiomes.
 */
public class BiomeProviderTFCR extends BiomeProvider {
    private final BiomeCache cache = new BiomeCache(this);
    private final GenLayer genBiomes;
    /** A GenLayer containing a factory to generate biome arrays for {@llink #getBiomes(int, int, int, int, boolean)} */
    private final GenLayer biomeFactoryLayer;

    private static NoiseGeneratorOctaves temperature;
    private static NoiseGeneratorOctaves precipitation;

    // TODO add placeholder height placeholderBiomes?
    public static final Biome[] placeholderBiomes = new Biome[] {
            new PlaceholderBiome(TerrainType.DEEP_OCEAN),
            new PlaceholderBiome(TerrainType.OCEAN),
            new BeachBiome(),
            new CliffBiome(),
            new RiverBiome(),
            new PlaceholderBiome(TerrainType.FLAT),
            new PlaceholderBiome(TerrainType.SMALL_HILLS),
            new PlaceholderBiome(TerrainType.BIG_HILLS),
            new PlaceholderBiome(TerrainType.MOUNTAINS),
    };

    public static final Biome[] biomes = new Biome[] {

    };

    private static final Biome DEFAULT = placeholderBiomes[5]; // Default is Flat biome

    /**
     * Creates a new BiomeProvider for TFCR.
     *
     * TODO change the settings to a custom type
     * @param settings
     */
    public BiomeProviderTFCR(WorldInfo worldInfo, OverworldGenSettings settings) {
        long seed = worldInfo.getSeed();
        WorldType worldType = worldInfo.getTerrainType();

        GenLayer[] genLayers = LayerUtilsTFCR.buildOverworldProcedure(seed, worldType, settings);
        this.genBiomes = genLayers[0];
        this.biomeFactoryLayer = genLayers[1]; // Voronoi zoomed out version of genBiomes

        // Shared seed random for the noise generators
        SharedSeedRandom sharedSeed = new SharedSeedRandom(seed);
        temperature = new NoiseGeneratorOctaves(sharedSeed, 2);
        precipitation = new NoiseGeneratorOctaves(sharedSeed, 2);
    }

    /**
     * Transforms a placeholder Biome (i.e., an int that maps to a TerrainType)
     * into a concrete Biome based on the temperature and precipitation at that
     * location.
     *
     * @param placeholderBiome An int ID corresponding to a placeholder biome
     * @return A concrete biome with terrain matching the placeholder
     */
    private Biome applyTempPrecip(BlockPos pos, int placeholderBiome) {
        double tempRaw = temperature.func_205563_a(pos.getX(), 0, pos.getZ());
        double precipRaw = precipitation.func_205563_a(pos.getX(), 0, pos.getZ());

        int temp = (int)((tempRaw * 200.0) - 100.0); // Temperature ranges from -100 to 100
        int precip = (int)(precipRaw * 100.0); // Precipitation ranges from 0 to 100

        // Ensure it's within range
        temp = MathHelper.clamp(temp, -100, 100);
        precip = MathHelper.clamp(precip, 0, 100);

        // Deep ocean, ocean, river, beach, cliff all map to their placeholders
        if (LayerUtilsTFCR.isWater(placeholderBiome) ||
                placeholderBiome == LayerUtilsTFCR.BEACH ||
                placeholderBiome == LayerUtilsTFCR.CLIFF) {
            return placeholderBiomes[placeholderBiome];
        }

        // Else we have flat, small hills, big hills, or mountains.

        if (precip < 10) {
            if (temp > 75) {
                if (placeholderBiome == LayerUtilsTFCR.FLAT) {
                    return biomes[new DesertBiomeFlat().getID()];
                }
            }
        }


        return null;
    }

    @Nullable
    @Override
    public Biome getBiome(BlockPos pos, @Nullable Biome defaultBiome) {
        return this.cache.getBiome(pos.getX(), pos.getZ(), DEFAULT);
    }

    @Override
    public Biome[] getBiomes(int startX, int startZ, int xSize, int zSize) {
        return this.genBiomes.generateBiomes(startX, startZ, xSize, zSize, DEFAULT);
    }

    @Override
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
        return cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0 ? this.cache.getCachedBiomes(x, z) : this.biomeFactoryLayer.generateBiomes(x, z, width, length, DEFAULT);
    }

    @Override
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
        int i = centerX - sideLength >> 2;
        int j = centerZ - sideLength >> 2;
        int k = centerX + sideLength >> 2;
        int l = centerZ + sideLength >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Set<Biome> set = Sets.newHashSet();
        Collections.addAll(set, this.genBiomes.generateBiomes(i, j, i1, j1, (Biome)null));
        return set;
    }

    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Biome[] abiome = this.genBiomes.generateBiomes(i, j, i1, j1, (Biome)null);
        BlockPos blockpos = null;
        int k1 = 0;

        for(int l1 = 0; l1 < i1 * j1; ++l1) {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            if (biomes.contains(abiome[l1])) {
                if (blockpos == null || random.nextInt(k1 + 1) == 0) {
                    blockpos = new BlockPos(i2, 0, j2);
                }

                ++k1;
            }
        }

        return blockpos;
    }

    @Override
    public boolean hasStructure(Structure<?> structureIn) {
        return this.hasStructureCache.computeIfAbsent(structureIn, (p_205006_1_) -> {
            for(Biome biome : this.placeholderBiomes) {
                if (biome.hasStructure(p_205006_1_)) {
                    return true;
                }
            }

            return false;
        });
    }

    @Override
    public Set<IBlockState> getSurfaceBlocks() {
        if (this.topBlocksCache.isEmpty()) {
            for(Biome biome : this.placeholderBiomes) {
                this.topBlocksCache.add(biome.getSurfaceBuilderConfig().getTop());
            }
        }

        return this.topBlocksCache;
    }
}
