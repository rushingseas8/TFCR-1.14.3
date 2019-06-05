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
import tfcr.worldgen.biome.BaseTFCRBiome;
import tfcr.worldgen.biome.ChaparralBiomeBigHill;
import tfcr.worldgen.biome.ChaparralBiomeFlat;
import tfcr.worldgen.biome.ChaparralBiomeMountain;
import tfcr.worldgen.biome.ChaparralBiomeSmallHill;
import tfcr.worldgen.biome.DeciduousForestBiomeBigHill;
import tfcr.worldgen.biome.DeciduousForestBiomeFlat;
import tfcr.worldgen.biome.DeciduousForestBiomeMountain;
import tfcr.worldgen.biome.DeciduousForestBiomeSmallHill;
import tfcr.worldgen.biome.DeepOceanBiome;
import tfcr.worldgen.biome.DesertBiomeBigHill;
import tfcr.worldgen.biome.DesertBiomeFlat;
import tfcr.worldgen.biome.DesertBiomeSmallHill;
import tfcr.worldgen.biome.DesertBiomeMountain;
import tfcr.worldgen.biome.DryTundraBiomeBigHill;
import tfcr.worldgen.biome.DryTundraBiomeFlat;
import tfcr.worldgen.biome.DryTundraBiomeMountain;
import tfcr.worldgen.biome.DryTundraBiomeSmallHill;
import tfcr.worldgen.biome.HighGrasslandBiomeBigHill;
import tfcr.worldgen.biome.HighGrasslandBiomeFlat;
import tfcr.worldgen.biome.HighGrasslandBiomeMountain;
import tfcr.worldgen.biome.HighGrasslandBiomeSmallHill;
import tfcr.worldgen.biome.LowGrasslandBiomeBigHill;
import tfcr.worldgen.biome.LowGrasslandBiomeFlat;
import tfcr.worldgen.biome.LowGrasslandBiomeMountain;
import tfcr.worldgen.biome.LowGrasslandBiomeSmallHill;
import tfcr.worldgen.biome.OceanBiome;
import tfcr.worldgen.biome.PolarBiomeBigHill;
import tfcr.worldgen.biome.PolarBiomeFlat;
import tfcr.worldgen.biome.PolarBiomeMountain;
import tfcr.worldgen.biome.PolarBiomeSmallHills;
import tfcr.worldgen.biome.SavannaBiomeBigHill;
import tfcr.worldgen.biome.SavannaBiomeFlat;
import tfcr.worldgen.biome.SavannaBiomeMountain;
import tfcr.worldgen.biome.SavannaBiomeSmallHill;
import tfcr.worldgen.biome.ShrublandBiomeBigHill;
import tfcr.worldgen.biome.ShrublandBiomeFlat;
import tfcr.worldgen.biome.ShrublandBiomeMountain;
import tfcr.worldgen.biome.ShrublandBiomeSmallHill;
import tfcr.worldgen.biome.TaigaBiomeBigHill;
import tfcr.worldgen.biome.TaigaBiomeFlat;
import tfcr.worldgen.biome.TaigaBiomeMountain;
import tfcr.worldgen.biome.TaigaBiomeSmallHill;
import tfcr.worldgen.biome.TemperateConiferousBiomeBigHill;
import tfcr.worldgen.biome.TemperateConiferousBiomeFlat;
import tfcr.worldgen.biome.TemperateConiferousBiomeMountain;
import tfcr.worldgen.biome.TemperateConiferousBiomeSmallHill;
import tfcr.worldgen.biome.TemperateDesertBigHill;
import tfcr.worldgen.biome.TemperateDesertFlat;
import tfcr.worldgen.biome.TemperateDesertMountain;
import tfcr.worldgen.biome.TemperateDesertSmallHill;
import tfcr.worldgen.biome.TemperateRainforestBigHill;
import tfcr.worldgen.biome.TemperateRainforestFlat;
import tfcr.worldgen.biome.TemperateRainforestMountain;
import tfcr.worldgen.biome.TemperateRainforestSmallHill;
import tfcr.worldgen.biome.TreeShrublandBiomeBigHill;
import tfcr.worldgen.biome.TreeShrublandBiomeFlat;
import tfcr.worldgen.biome.TreeShrublandBiomeMountain;
import tfcr.worldgen.biome.TreeShrublandBiomeSmallHill;
import tfcr.worldgen.biome.TropicalRainforestBiomeBigHill;
import tfcr.worldgen.biome.TropicalRainforestBiomeFlat;
import tfcr.worldgen.biome.BeachBiome;
import tfcr.worldgen.biome.CliffBiome;
import tfcr.worldgen.biome.TropicalRainforestBiomeMountain;
import tfcr.worldgen.biome.TropicalRainforestBiomeSmallHill;
import tfcr.worldgen.biome.TundraBiomeBigHill;
import tfcr.worldgen.biome.TundraBiomeFlat;
import tfcr.worldgen.biome.TundraBiomeMountain;
import tfcr.worldgen.biome.TundraBiomeSmallHill;
import tfcr.worldgen.biome.WetlandBiomeBigHill;
import tfcr.worldgen.biome.WetlandBiomeFlat;
import tfcr.worldgen.biome.WetlandBiomeMountain;
import tfcr.worldgen.biome.WetlandBiomeSmallHill;
import tfcr.worldgen.biome.placeholder.PlaceholderBiome;
import tfcr.worldgen.biome.RiverBiome;
import tfcr.worldgen.genlayer.GenLayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
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

    private static float noiseScaleX;
    private static float noiseScaleZ;


    // TODO add placeholder height placeholderBiomes?
    public static final Biome[] placeholderBiomes = new Biome[] {
            new DeepOceanBiome(),
            new OceanBiome(),
            new BeachBiome(),
            new CliffBiome(),
            new RiverBiome(),
            new PlaceholderBiome(TerrainType.FLAT),
            new PlaceholderBiome(TerrainType.SMALL_HILLS),
            new PlaceholderBiome(TerrainType.BIG_HILLS),
            new PlaceholderBiome(TerrainType.MOUNTAINS),
    };

    public static final BaseTFCRBiome[] biomes = new BaseTFCRBiome[] {
            new DesertBiomeFlat(),
            new DesertBiomeSmallHill(),
            new DesertBiomeBigHill(),
            new DesertBiomeMountain(),

            new SavannaBiomeFlat(),
            new SavannaBiomeSmallHill(),
            new SavannaBiomeBigHill(),
            new SavannaBiomeMountain(),

            new ShrublandBiomeFlat(),
            new ShrublandBiomeSmallHill(),
            new ShrublandBiomeBigHill(),
            new ShrublandBiomeMountain(),

            new TreeShrublandBiomeFlat(),
            new TreeShrublandBiomeSmallHill(),
            new TreeShrublandBiomeBigHill(),
            new TreeShrublandBiomeMountain(),

            new TropicalRainforestBiomeFlat(),
            new TropicalRainforestBiomeSmallHill(),
            new TropicalRainforestBiomeBigHill(),
            new TropicalRainforestBiomeMountain(),

            new ChaparralBiomeFlat(),
            new ChaparralBiomeSmallHill(),
            new ChaparralBiomeBigHill(),
            new ChaparralBiomeMountain(),

            new LowGrasslandBiomeFlat(),
            new LowGrasslandBiomeSmallHill(),
            new LowGrasslandBiomeBigHill(),
            new LowGrasslandBiomeMountain(),

            new HighGrasslandBiomeFlat(),
            new HighGrasslandBiomeSmallHill(),
            new HighGrasslandBiomeBigHill(),
            new HighGrasslandBiomeMountain(),

            new TemperateConiferousBiomeFlat(),
            new TemperateConiferousBiomeSmallHill(),
            new TemperateConiferousBiomeBigHill(),
            new TemperateConiferousBiomeMountain(),

            new DeciduousForestBiomeFlat(),
            new DeciduousForestBiomeSmallHill(),
            new DeciduousForestBiomeBigHill(),
            new DeciduousForestBiomeMountain(),

            new TemperateRainforestFlat(),
            new TemperateRainforestSmallHill(),
            new TemperateRainforestBigHill(),
            new TemperateRainforestMountain(),

            new TemperateDesertFlat(),
            new TemperateDesertSmallHill(),
            new TemperateDesertBigHill(),
            new TemperateDesertMountain(),

            new WetlandBiomeFlat(),
            new WetlandBiomeSmallHill(),
            new WetlandBiomeBigHill(),
            new WetlandBiomeMountain(),

            new DryTundraBiomeFlat(),
            new DryTundraBiomeSmallHill(),
            new DryTundraBiomeBigHill(),
            new DryTundraBiomeMountain()
            ,
            new TundraBiomeFlat(),
            new TundraBiomeSmallHill(),
            new TundraBiomeBigHill(),
            new TundraBiomeMountain(),

            new TaigaBiomeFlat(),
            new TaigaBiomeSmallHill(),
            new TaigaBiomeBigHill(),
            new TaigaBiomeMountain(),

            new PolarBiomeFlat(),
            new PolarBiomeSmallHills(),
            new PolarBiomeBigHill(),
            new PolarBiomeMountain()
    };

    private static HashMap<Biome, Integer> placeholderBiomeToIndexLookup;
    public static HashMap<Class, Integer> biomeClassToIndexLookup;

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

        noiseScaleX = 1 / 100f;
        noiseScaleZ = 1 / 100f;

        // Placeholder biome reverse lookup map
        placeholderBiomeToIndexLookup = new HashMap<>();
        for (int i = 0; i < placeholderBiomes.length; i++) {
            placeholderBiomeToIndexLookup.put(placeholderBiomes[i], i);
        }

        // Biome reverse lookup map (by class)
        biomeClassToIndexLookup = new HashMap<>();
        for (int i = 0; i < biomes.length; i++) {
            biomeClassToIndexLookup.put(biomes[i].getClass(), i);
        }
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
        // Temperature seems to range [0, 2]. Precip ranges from [-2, 2]??
        // TODO double check these ranges and ensure they're properly normalized.
        // TODO later, add a bias to the values so that temperate is more common.
        double tempRaw = temperature.func_205563_a(pos.getX() / noiseScaleX, 0, pos.getZ() / noiseScaleZ);
        double precipRaw = precipitation.func_205563_a(pos.getX() / noiseScaleX, 0, pos.getZ() / noiseScaleZ);

        System.out.println("Temperature: " + tempRaw);
        System.out.println("Precipitation: " + precipRaw);

        // Roughly normalize
        tempRaw /= 2;
        precipRaw /= 2;

        int temp = (int)((tempRaw * 200.0) - 100.0); // Temperature ranges from -100 to 100
        int precip = (int)(precipRaw * 100.0); // Precipitation ranges from 0 to 100

        // Ensure it's within range
        temp = MathHelper.clamp(temp, -100, 100);
        precip = MathHelper.clamp(precip, 0, 100);

        // Deep ocean, ocean, river, beach, cliff all map to their placeholders
        if (LayerUtilsTFCR.isWater(placeholderBiome) ||
                placeholderBiome == LayerUtilsTFCR.CLIFF) {
            return placeholderBiomes[placeholderBiome];
        }

        // Else we have flat, small hills, big hills, or mountains.
        // Go through all the biomes and see if any match the given parameters.
        // There should only be one that matches, so we'll return that.
        for (BaseTFCRBiome biome : biomes) {
            if (biome.matchesRange(temp, precip, TerrainType.values()[placeholderBiome])) {
                Biome toReturn = biome.provideClosest(temp, precip, TerrainType.values()[placeholderBiome]);
                if (toReturn == null) {
                    System.out.println("Biome \"" + biome.getRegistryName() + "\" failed to provide closest.");
                    return DEFAULT;
                } else {
                    return toReturn;
                }
            }
        }
        System.out.println("Failed to resolve biome with temperature=" + temp +
                ", precipitation=" + precip +
                " and TerrainType=" + TerrainType.values()[placeholderBiome]);
        //return null;
        return DEFAULT;
    }

    /**
     * Transforms a placeholder Biome into a concrete implementation.
     * @param pos
     * @param placeholderBiome
     * @return
     */
    private Biome applyTempPrecip(@Nonnull BlockPos pos, @Nonnull Biome placeholderBiome) {
        if (placeholderBiomeToIndexLookup == null) {
            System.out.println("Lookup index was null?");
            return DEFAULT;
        }
        Integer index = placeholderBiomeToIndexLookup.get(placeholderBiome);
        if (index == null) {
            System.out.println("Failed to lookup biome: " + placeholderBiome.getRegistryName());
            return DEFAULT;
        }
        return applyTempPrecip(pos, index);
    }

    /**
     * Transforms an array of placeholder Biomes into concrete ones.
     * @param startX
     * @param startZ
     * @param xSize
     * @param zSize
     * @param placeholderBiomes
     * @return
     */
    private Biome[] applyTempPrecip(int startX, int startZ, int xSize, int zSize, Biome[] placeholderBiomes) {
        Biome[] concrete = new Biome[placeholderBiomes.length];

        // Iterate over all the placeholder Biomes and replace with concrete ones
        // based on the world coordinates
        for (int z = 0; z < zSize; z++) {
            for (int x = 0; x < xSize; x++) {
                int index = x + z * xSize;
                concrete[index] = applyTempPrecip(new BlockPos(startX + x, 0, startZ + z), placeholderBiomes[index]);
            }
        }
        return concrete;
    }

    @Nullable
    @Override
    public Biome getBiome(@Nonnull BlockPos pos, @Nullable Biome defaultBiome) {
//        System.out.println("getBiome called with pos: " + pos);
//        return this.cache.getBiome(pos.getX(), pos.getZ(), DEFAULT);

        // Cache already has temp/precip applied
        Biome biome = this.cache.getBiome(pos.getX(), pos.getZ(), DEFAULT);
        return biome;
//        Biome toReturn = applyTempPrecip(pos, biome);
//        return toReturn == null ? defaultBiome : toReturn;
    }

    @Nonnull
    @Override
    public Biome[] getBiomes(int startX, int startZ, int xSize, int zSize) {
//        System.out.println("getBiomes called with bounds: " + startX + ", " + startZ + ", " + xSize + ", " + zSize);
//        return this.genBiomes.generateBiomes(startX, startZ, xSize, zSize, DEFAULT);
        Biome[] placeholders = this.genBiomes.generateBiomes(startX, startZ, xSize, zSize, DEFAULT);
        return applyTempPrecip(startX, startZ, xSize, zSize, placeholders);
    }

    @Nonnull
    @Override

    // Gets the biomes from the cache. If the temp/precip is already applied, we don't apply it again.
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
//        System.out.println("getBiomes called with bounds: " + x + ", " + z + ", " + width + ", " + length);
//        return cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0 ? this.cache.getCachedBiomes(x, z) : this.biomeFactoryLayer.generateBiomes(x, z, width, length, DEFAULT);

        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0) {
            return this.cache.getCachedBiomes(x, z);
        } else {
            return applyTempPrecip(x, z, width, length, this.biomeFactoryLayer.generateBiomes(x, z, width, length, DEFAULT));
        }
    }

    @Nonnull
    @Override
    // TODO Are these chunk coordinates or world coordinates?
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
//        System.out.println("getBiomesInSquare called with bounds: " + centerX + ", " + centerZ + ", " + sideLength);
        int i = centerX - sideLength >> 2;
        int j = centerZ - sideLength >> 2;
        int k = centerX + sideLength >> 2;
        int l = centerZ + sideLength >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;

        Biome[] placeholders = this.genBiomes.generateBiomes(i, j, i1, j1, (Biome)null);
        Biome[] concrete = applyTempPrecip(i, j, i1, j1, placeholders);

        Set<Biome> set = Sets.newHashSet();
        Collections.addAll(set, concrete);
//        Collections.addAll(set, placeholders);

        return set;
    }

    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, @Nonnull List<Biome> biomes, @Nonnull Random random) {
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Biome[] abiome = this.genBiomes.generateBiomes(i, j, i1, j1, (Biome)null);
        Biome[] concrete = applyTempPrecip(i, j, i1, j1, abiome);
        BlockPos blockpos = null;
        int k1 = 0;

        for(int l1 = 0; l1 < i1 * j1; ++l1) {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            if (biomes.contains(concrete[l1])) {
//            if (biomes.contains(abiome[l1])) {
                if (blockpos == null || random.nextInt(k1 + 1) == 0) {
                    blockpos = new BlockPos(i2, 0, j2);
                }

                ++k1;
            }
        }

        return blockpos;
    }

    @Override
    public boolean hasStructure(@Nonnull Structure<?> structureIn) {
        return this.hasStructureCache.computeIfAbsent(structureIn, (p_205006_1_) -> {
            for(Biome biome : this.placeholderBiomes) {
                if (biome.hasStructure(p_205006_1_)) {
                    return true;
                }
            }

            return false;
        });
    }

    @Nonnull
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
