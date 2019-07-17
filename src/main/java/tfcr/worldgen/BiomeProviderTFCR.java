package tfcr.worldgen;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
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
import tfcr.worldgen.biome.ChaparralBiome;
import tfcr.worldgen.biome.DeciduousForestBiome;
import tfcr.worldgen.biome.DeepOceanBiome;
import tfcr.worldgen.biome.DesertBiome;
import tfcr.worldgen.biome.DryTundraBiome;
import tfcr.worldgen.biome.HighGrasslandBiome;
import tfcr.worldgen.biome.LowGrasslandBiome;
import tfcr.worldgen.biome.OceanBiome;
import tfcr.worldgen.biome.BeachBiome;
import tfcr.worldgen.biome.CliffBiome;
import tfcr.worldgen.biome.PolarBiome;
import tfcr.worldgen.biome.SavannaBiome;
import tfcr.worldgen.biome.ShrublandBiome;
import tfcr.worldgen.biome.TaigaBiome;
import tfcr.worldgen.biome.TemperateConiferousBiome;
import tfcr.worldgen.biome.TemperateDesertBiome;
import tfcr.worldgen.biome.TemperateRainforestBiome;
import tfcr.worldgen.biome.TreeShrublandBiome;
import tfcr.worldgen.biome.TropicalRainforestBiome;
import tfcr.worldgen.biome.TundraBiome;
import tfcr.worldgen.biome.WetlandBiome;
import tfcr.worldgen.biome.placeholder.PlaceholderBiome;
import tfcr.worldgen.biome.RiverBiome;
import tfcr.worldgen.genlayer.GenLayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static BaseTFCRBiome[] biomes;

    // TODO this map only has one entry per terrain type for each biome. Should have them all
    public static HashMap<Class, Integer> biomeClassToIndexLookup;
    static {
        // Initialize biomes
        ArrayList<BaseTFCRBiome> biomesList = new ArrayList<>();

        // Water and technical biomes.
        biomesList.addAll(
            Arrays.asList(
                new DeepOceanBiome(),
                new OceanBiome(),
                new BeachBiome(),
                new CliffBiome(),
                new RiverBiome()
            )
        );

        // Temp/precip based biomes.
        biomesList.addAll(Arrays.asList(ChaparralBiome.generate()));
        biomesList.addAll(Arrays.asList(DeciduousForestBiome.generate()));
        biomesList.addAll(Arrays.asList(DesertBiome.generate()));
        biomesList.addAll(Arrays.asList(DryTundraBiome.generate()));
        biomesList.addAll(Arrays.asList(HighGrasslandBiome.generate()));
        biomesList.addAll(Arrays.asList(LowGrasslandBiome.generate()));
        biomesList.addAll(Arrays.asList(PolarBiome.generate()));
        biomesList.addAll(Arrays.asList(SavannaBiome.generate()));
        biomesList.addAll(Arrays.asList(ShrublandBiome.generate()));
        biomesList.addAll(Arrays.asList(TaigaBiome.generate()));
        biomesList.addAll(Arrays.asList(TemperateConiferousBiome.generate()));
        biomesList.addAll(Arrays.asList(TemperateDesertBiome.generate()));
        biomesList.addAll(Arrays.asList(TemperateRainforestBiome.generate()));
        biomesList.addAll(Arrays.asList(TreeShrublandBiome.generate()));
        biomesList.addAll(Arrays.asList(TropicalRainforestBiome.generate()));
        biomesList.addAll(Arrays.asList(TundraBiome.generate()));
        biomesList.addAll(Arrays.asList(WetlandBiome.generate()));

        biomes = biomesList.toArray(new BaseTFCRBiome[0]);

        // Biome reverse lookup map (by class)
        // Keep earlist; this will tend to be the flat versions.
        biomeClassToIndexLookup = new HashMap<>();
        for (int i = 0; i < biomes.length; i++) {
            biomeClassToIndexLookup.putIfAbsent(biomes[i].getClass(), i);
        }
    }

    public static final Biome DEFAULT = Biomes.PLAINS; // Default is plains biome

    /**
     * Creates a new BiomeProvider for TFCR.
     *
     * TODO change the settings to a custom type
     * @param settings
     */
    public BiomeProviderTFCR(WorldInfo worldInfo, OverworldGenSettings settings) {
        long seed = worldInfo.getSeed();
        WorldType worldType = worldInfo.getTerrainType();

        // Regular temp/precip biome map
//        GenLayer[] genLayers = LayerUtilsTFCR.buildOverworldProcedure(seed, worldType, settings);
        // Simplified biomes (only TemperateConiferousBiome + river, ocean, etc.)
        GenLayer[] genLayers = LayerUtilsTFCR.buildSimpleProcedure(seed, worldType, settings);
        this.genBiomes = genLayers[0];
        this.biomeFactoryLayer = genLayers[1]; // Voronoi zoomed out version of genBiomes

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
        return this.genBiomes.generateBiomes(startX, startZ, xSize, zSize, DEFAULT);
    }

    @Nonnull
    @Override

    // Gets the biomes from the cache. If the temp/precip is already applied, we don't apply it again.
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
//        System.out.println("getBiomes called with bounds: " + x + ", " + z + ", " + width + ", " + length);
        return cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0 ? this.cache.getCachedBiomes(x, z) : this.biomeFactoryLayer.generateBiomes(x, z, width, length, DEFAULT);

//        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0) {
//            return this.cache.getCachedBiomes(x, z);
//        } else {
//            return applyTempPrecip(x, z, width, length, this.biomeFactoryLayer.generateBiomes(x, z, width, length, DEFAULT));
//        }
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

        Set<Biome> set = Sets.newHashSet();
        Collections.addAll(set, placeholders);

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
    public boolean hasStructure(@Nonnull Structure<?> structureIn) {
        return this.hasStructureCache.computeIfAbsent(structureIn, (p_205006_1_) -> {
            for(Biome biome : this.biomes) {
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
            for(Biome biome : this.biomes) {
                this.topBlocksCache.add(biome.getSurfaceBuilderConfig().getTop());
            }
        }

        return this.topBlocksCache;
    }
}
