package tfcr.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import tfcr.data.TerrainType;
import tfcr.worldgen.genlayer.AddMountainLayer;
import tfcr.worldgen.genlayer.Layer;
import tfcr.worldgen.genlayer.AddIslandLayer;
import tfcr.worldgen.genlayer.DeepOceanLayer;
import tfcr.worldgen.genlayer.EqualizeLayer;
import tfcr.worldgen.genlayer.HillLayer;
import tfcr.worldgen.genlayer.IslandLayer;
import tfcr.worldgen.genlayer.RemoveTooMuchOceanLayer;
import tfcr.worldgen.genlayer.RiverLayer;
import tfcr.worldgen.genlayer.RiverInitLayer;
import tfcr.worldgen.genlayer.RiverMaskLayer;
import tfcr.worldgen.genlayer.ShoreLayer;
import tfcr.worldgen.genlayer.TempPrecipLayer;
import tfcr.worldgen.genlayer.TempPrecipMaskLayer;

import java.util.Random;
import java.util.function.LongFunction;

/**
 * A class closely based on LayerUtils in vanilla.
 *
 * Handles the custom biome worldgen for TFCR.
 */
public class LayerUtilsTFCR {


    // Constants related to the ordinal of each TerrainType.
    // These act as indices for the placeholder placeholderBiomes.
    public static final int DEEP_OCEAN = TerrainType.DEEP_OCEAN.ordinal();
    public static final int OCEAN = TerrainType.OCEAN.ordinal();
    public static final int BEACH = TerrainType.BEACH.ordinal();
    public static final int CLIFF = TerrainType.CLIFF.ordinal();
    public static final int RIVER = TerrainType.RIVER.ordinal();
    public static final int FLAT = TerrainType.FLAT.ordinal();
    public static final int SMALL_HILLS = TerrainType.SMALL_HILLS.ordinal();
    public static final int BIG_HILLS = TerrainType.BIG_HILLS.ordinal();
    public static final int MOUNTAINS = TerrainType.MOUNTAINS.ordinal();

    public static boolean isOcean(int terrainType) {
        return terrainType == OCEAN || terrainType == DEEP_OCEAN;
    }

    public static boolean isShallowOcean(int terrainType) {
        return terrainType == OCEAN;
    }

    public static boolean isWater(int terrainType) {
        return terrainType == OCEAN || terrainType == DEEP_OCEAN || terrainType == RIVER || terrainType == BEACH || terrainType == CLIFF;
    }

    /**
     * True if any of the surrounding tiles have any ocean terrain
     * @param south
     * @param east
     * @param north
     * @param west
     * @return
     */
    public static boolean hasOcean(int south, int east, int north, int west) {
        return LayerUtilsTFCR.isOcean(south) ||
                LayerUtilsTFCR.isOcean(east) ||
                LayerUtilsTFCR.isOcean(west) ||
                LayerUtilsTFCR.isOcean(north);
    }
    /**
     * True if any of the surrounding tiles have a MOUNTAINS terrain
     * @param south
     * @param east
     * @param north
     * @param west
     * @return
     */
    public static boolean hasMountain(int south, int east, int north, int west) {
        return south == LayerUtilsTFCR.MOUNTAINS ||
                east == LayerUtilsTFCR.MOUNTAINS ||
                north == LayerUtilsTFCR.MOUNTAINS ||
                west == LayerUtilsTFCR.MOUNTAINS;
    }

    /**
     * True if any of the surrounding tiles have a BIG_HILLS terrain
     * @param south
     * @param east
     * @param north
     * @param west
     * @return
     */
    public static boolean hasBigHill(int south, int east, int north, int west) {
        return south == LayerUtilsTFCR.BIG_HILLS ||
                east == LayerUtilsTFCR.BIG_HILLS ||
                north == LayerUtilsTFCR.BIG_HILLS ||
                west == LayerUtilsTFCR.BIG_HILLS;
    }

    /**
     * Repeats a given Layer a number of times.
     * @param seed The base seed for the RNG. We start at this value, and increment it by 1 each loop.
     * @param transform The Layer we will apply
     * @param base The Layer we apply transform to repeatedly
     * @param count The number of times to repeat the action.
     * @param contextFactory
     * @param <T>
     * @param <C>
     * @return
     */
    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> repeat(long seed, IAreaTransformer1 transform, IAreaFactory<T> base, int count, LongFunction<C> contextFactory) {
        IAreaFactory<T> iareafactory = base;

        for (int i = 0; i < count; ++i) {
            iareafactory = transform.apply(contextFactory.apply(seed + (long)i), iareafactory);
        }

        return iareafactory;
    }

    // Directly inspired by LayerUtils
    /**
     * A method that handles assigning placeholderBiomes in the world.
     *
     * We have to recreate a few of the Layer* classes from Vanilla, since those
     * return hard-coded indices for an overworld Biome array. We instead return
     * integers corresponding to different TerrainTypes, which will later be
     * resolved into actual placeholderBiomes based off of a temperature/precipitation map.
     *
     * @param worldTypeIn
     * @param settings
     * @param contextFactory
     * @param <T>
     * @param <C>
     * @return
     */
    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> buildOverworldProcedure(WorldType worldTypeIn, OverworldGenSettings settings, LongFunction<C> contextFactory) {
        // Basic worldgen. In Vanilla this section handles plains/forest islands and oceans
        // iareafactory --> baseAreaFactory
        IAreaFactory<T> baseAreaFactory = IslandLayer.INSTANCE.apply(contextFactory.apply(1L));
        baseAreaFactory = ZoomLayer.FUZZY.apply(contextFactory.apply(2000L), baseAreaFactory); // Zoom out 2x, fuzz
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(1L), baseAreaFactory); // Add small hill islands
        baseAreaFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(2001L), baseAreaFactory); // Zoom out 2x, normal
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(2L), baseAreaFactory);  // Add 3 layers of small hill islands
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(50L), baseAreaFactory);
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(70L), baseAreaFactory);
        baseAreaFactory = RemoveTooMuchOceanLayer.INSTANCE.apply(contextFactory.apply(2L), baseAreaFactory); // If a region is all ocean, there's a 50% chance to flip it to flat.
        // Skip the ocean-related code, since we don't care about ocean temp yet
        // GenLayerAddSnow actually adds hilly + mountainous regions. So renamed to AddMountainLayer.
        baseAreaFactory = AddMountainLayer.INSTANCE.apply(contextFactory.apply(2L), baseAreaFactory);
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(3L), baseAreaFactory); // More small hill islands
        // GenLayerEdge plays a role here. It looks like it messes with temperature (adding deserts/mountains),
        // and then adds a rare chance of having special placeholderBiomes. We don't care about that.
        baseAreaFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(2002L), baseAreaFactory); // Zoom out 2x
        baseAreaFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(2003L), baseAreaFactory); // And again, for a total of 4x
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(4L), baseAreaFactory); // More small hill islands
        // Don't care about mushroom islands
        baseAreaFactory = DeepOceanLayer.INSTANCE.apply(contextFactory.apply(4L), baseAreaFactory); // Turn center ocean tiles into deep ocean

        // Custom. Tries to normalize land. 2 passes to potentially fully normalize mountain boundaries.
        baseAreaFactory = repeat(16L, EqualizeLayer.INSTANCE, baseAreaFactory, 2, contextFactory);
        baseAreaFactory = repeat(1000L, ZoomLayer.NORMAL, baseAreaFactory, 0, contextFactory); // no-op?

        // River and biome size setup
        int biomeSize = 4;
        int riverSize = 4;
//        if (settings != null) {
//            biomeSize = settings.getBiomeSize();
//            riverSize = settings.getRiverSize();
//        }

        // Temp/precip layer. TODO find a way to put in world seed here
        IAreaFactory<T> tempPrecipLayer = new TempPrecipLayer(new Random(-1)).apply(contextFactory.apply(17L));
        // Zoom out the temp/precip layer to be roughly the size we need. Without scaling the noise,
        // the isotherms would change roughly every block. So if we zoom 10 times total, we get a scale
        // of 2^10 = 1024 blocks between isotherm changes. This gives us roughly the desired biome size.
        // We divide by ~500 in the noisegen, and apply fuzzy zooming on top to get to this value.
        tempPrecipLayer = ZoomLayer.FUZZY.apply(contextFactory.apply(2004L), tempPrecipLayer);

        // lvt_7_1_ --> riverAreaFactory
        IAreaFactory<T> riverAreaFactory = repeat(1000L, ZoomLayer.NORMAL, baseAreaFactory, 0, contextFactory); // This is a no-op?
        riverAreaFactory = RiverInitLayer.INSTANCE.apply(contextFactory.apply(100L), riverAreaFactory);

        // Add Biomes using getBiomeLayer. The TFCR variation uses TerrainType-based logic.
        // lvt_8_1_ --> biomesAreaFactory
        IAreaFactory<T> biomesAreaFactory = worldTypeIn.getBiomeLayer(baseAreaFactory, settings, contextFactory);

        // lvt_9_1_ --> zoomedRiverInitAreaFactory
//        IAreaFactory<T> zoomedRiverInitAreaFactory = repeat(1000L, GenLayerZoom.NORMAL, riverAreaFactory, 2, contextFactory);
        biomesAreaFactory = HillLayer.INSTANCE.apply(contextFactory.apply(1000L), biomesAreaFactory); // Random chance to raise land (modified from orig)
        riverAreaFactory = repeat(1000L, ZoomLayer.NORMAL, riverAreaFactory, 2, contextFactory);
        riverAreaFactory = repeat(1000L, ZoomLayer.NORMAL, riverAreaFactory, riverSize, contextFactory);
        riverAreaFactory = RiverLayer.INSTANCE.apply(contextFactory.apply(1L), riverAreaFactory); // Add rivers
        riverAreaFactory = SmoothLayer.INSTANCE.apply(contextFactory.apply(1000L), riverAreaFactory); // Smooth the region

        // Expand the placeholderBiomes as needed; based on biomeSize parameter
        for (int zoomIteration = 0; zoomIteration < biomeSize; zoomIteration++) {
            biomesAreaFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(1000L + zoomIteration), biomesAreaFactory);
            if (zoomIteration == 0) {
                biomesAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(3L), biomesAreaFactory); // Add 1 pass of small hill islands
            }

            if (zoomIteration == 1 || biomeSize == 1) {
                biomesAreaFactory = ShoreLayer.INSTANCE.apply(contextFactory.apply(1000L), biomesAreaFactory); // Add beaches
            }
        }

        biomesAreaFactory = SmoothLayer.INSTANCE.apply(contextFactory.apply(1000L), biomesAreaFactory); // Smooth after zoom
        biomesAreaFactory = RiverMaskLayer.INSTANCE.apply(contextFactory.apply(100L), biomesAreaFactory, riverAreaFactory); // Mix in the rivers

        // Apply the temp/precip map on top.
        biomesAreaFactory = TempPrecipMaskLayer.INSTANCE.apply(contextFactory.apply(1000L), biomesAreaFactory, tempPrecipLayer);

        // No ocean temperature mixing at this time

        // Voronoi zoom out
        IAreaFactory<T> voronoiZoomed = VoroniZoomLayer.INSTANCE.apply(contextFactory.apply(10L), biomesAreaFactory);

        // TODO Add a mask layer for applying temp/precip transforms. Similar to RiverMaskLayer, but
        //  using a Perlin noise based map that's been fuzzy zoomed out to the right scale.

        return ImmutableList.of(biomesAreaFactory, voronoiZoomed, biomesAreaFactory);
    }

    /**
     * TODO placeholder
     * A very basic placeholder worldgen. Primary biome is forest (specifically, TemperateConiferousBiome)
     * but with the addition of oceans, rivers, beaches, and cliff biomes (as appropriate).
     *
     * This is to allow for developing the core gameplay of TFCR without worrying too much
     * about worldgen. Other biomes will slowly be mixed in as content is added.
     */
    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> buildSimpleProcedure(WorldType worldTypeIn, OverworldGenSettings settings, LongFunction<C> contextFactory) {
        // Basic worldgen. In Vanilla this section handles plains/forest islands and oceans
        // iareafactory --> baseAreaFactory
        IAreaFactory<T> baseAreaFactory = IslandLayer.INSTANCE.apply(contextFactory.apply(1L));
        baseAreaFactory = ZoomLayer.FUZZY.apply(contextFactory.apply(2000L), baseAreaFactory); // Zoom out 2x, fuzz
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(1L), baseAreaFactory); // Add small hill islands
        baseAreaFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(2001L), baseAreaFactory); // Zoom out 2x, normal
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(2L), baseAreaFactory);  // Add 3 layers of small hill islands
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(50L), baseAreaFactory);
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(70L), baseAreaFactory);
        baseAreaFactory = RemoveTooMuchOceanLayer.INSTANCE.apply(contextFactory.apply(2L), baseAreaFactory); // If a region is all ocean, there's a 50% chance to flip it to flat.
        // Skip the ocean-related code, since we don't care about ocean temp yet
        // GenLayerAddSnow actually adds hilly + mountainous regions. So renamed to AddMountainLayer.
        baseAreaFactory = AddMountainLayer.INSTANCE.apply(contextFactory.apply(2L), baseAreaFactory);
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(3L), baseAreaFactory); // More small hill islands
        // GenLayerEdge plays a role here. It looks like it messes with temperature (adding deserts/mountains),
        // and then adds a rare chance of having special placeholderBiomes. We don't care about that.
        baseAreaFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(2002L), baseAreaFactory); // Zoom out 2x
        baseAreaFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(2003L), baseAreaFactory); // And again, for a total of 4x
        baseAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(4L), baseAreaFactory); // More small hill islands
        // Don't care about mushroom islands
        baseAreaFactory = DeepOceanLayer.INSTANCE.apply(contextFactory.apply(4L), baseAreaFactory); // Turn center ocean tiles into deep ocean

        // Custom. Tries to normalize land. 2 passes to potentially fully normalize mountain boundaries.
        baseAreaFactory = repeat(16L, EqualizeLayer.INSTANCE, baseAreaFactory, 2, contextFactory);
        baseAreaFactory = repeat(1000L, ZoomLayer.NORMAL, baseAreaFactory, 0, contextFactory); // no-op?

        // River and biome size setup
        int biomeSize = 4;
        int riverSize = 4;
//        if (settings != null) {
//            biomeSize = settings.getBiomeSize();
//            riverSize = settings.getRiverSize();
//        }

        // lvt_7_1_ --> riverAreaFactory
        IAreaFactory<T> riverAreaFactory = repeat(1000L, ZoomLayer.NORMAL, baseAreaFactory, 0, contextFactory); // This is a no-op?
        riverAreaFactory = RiverInitLayer.INSTANCE.apply(contextFactory.apply(100L), riverAreaFactory);

        // Add Biomes using getBiomeLayer. The TFCR variation uses TerrainType-based logic.
        // lvt_8_1_ --> biomesAreaFactory
        IAreaFactory<T> biomesAreaFactory = worldTypeIn.getBiomeLayer(baseAreaFactory, settings, contextFactory);

        // lvt_9_1_ --> zoomedRiverInitAreaFactory
//        IAreaFactory<T> zoomedRiverInitAreaFactory = repeat(1000L, GenLayerZoom.NORMAL, riverAreaFactory, 2, contextFactory);
        biomesAreaFactory = HillLayer.INSTANCE.apply(contextFactory.apply(1000L), biomesAreaFactory); // Random chance to raise land (modified from orig)
        riverAreaFactory = repeat(1000L, ZoomLayer.NORMAL, riverAreaFactory, 2, contextFactory);
        riverAreaFactory = repeat(1000L, ZoomLayer.NORMAL, riverAreaFactory, riverSize, contextFactory);
        riverAreaFactory = RiverLayer.INSTANCE.apply(contextFactory.apply(1L), riverAreaFactory); // Add rivers
        riverAreaFactory = SmoothLayer.INSTANCE.apply(contextFactory.apply(1000L), riverAreaFactory); // Smooth the region

        // Expand the placeholderBiomes as needed; based on biomeSize parameter
        for (int zoomIteration = 0; zoomIteration < biomeSize; zoomIteration++) {
            biomesAreaFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(1000L + zoomIteration), biomesAreaFactory);
            if (zoomIteration == 0) {
                biomesAreaFactory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(3L), biomesAreaFactory); // Add 1 pass of small hill islands
            }

            if (zoomIteration == 1 || biomeSize == 1) {
                biomesAreaFactory = ShoreLayer.INSTANCE.apply(contextFactory.apply(1000L), biomesAreaFactory); // Add beaches
            }
        }

//        biomesAreaFactory = SmoothLayer.INSTANCE.apply(contextFactory.apply(1000L), biomesAreaFactory); // Smooth after zoom
        //biomesAreaFactory = RiverMaskLayer.INSTANCE.apply(contextFactory.apply(100L), biomesAreaFactory, riverAreaFactory); // Mix in the rivers

        // This lambda replaces TempPrecipLayer, which would normally fill the world with random temp/precip values.
        // "(100 << 8) | 35" maps to a temperature of 0, precip of 35. This means that we get a world that is filled
        // with TemperateConiferousBiome biomes (and all its height variations).
        IAreaFactory<T> tempPrecipLayer = ((IAreaTransformer0) (context, x, z) -> (100 << 8) | 35).apply(contextFactory.apply(17L));

        // Apply the temp/precip map on top.
//        biomesAreaFactory = TempPrecipMaskLayer.INSTANCE.apply(contextFactory.apply(1000L), biomesAreaFactory, tempPrecipLayer);

        // No ocean temperature mixing at this time
        IAreaFactory<T> voronoiZoomed = VoroniZoomLayer.INSTANCE.apply(contextFactory.apply(10L), biomesAreaFactory);

        return ImmutableList.of(biomesAreaFactory, voronoiZoomed, biomesAreaFactory);
    }

    // Directly copied from LayerUtils
    public static Layer[] buildOverworldProcedure(long seed, WorldType typeIn, OverworldGenSettings settings) {
        int i = 25;
        ImmutableList<IAreaFactory<LazyArea>> immutablelist = buildOverworldProcedure(typeIn, settings, (p_215737_2_) -> {
            return new LazyAreaLayerContext(25, seed, p_215737_2_);
        });
        Layer genlayer = new Layer(immutablelist.get(0));
        Layer genlayer1 = new Layer(immutablelist.get(1));
        Layer genlayer2 = new Layer(immutablelist.get(2));
        return new Layer[]{genlayer, genlayer1, genlayer2};
    }

    // Similar to buildOverworldProcedure, but calls buildSimpleProcedure instead.
    public static Layer[] buildSimpleProcedure(long seed, WorldType typeIn, OverworldGenSettings settings) {
        int i = 1;
        int[] aint = new int[1];
        ImmutableList<IAreaFactory<LazyArea>> immutablelist = buildSimpleProcedure(typeIn, settings, (p_215737_2_) -> {
            ++aint[0];
            return new LazyAreaLayerContext(25, seed, p_215737_2_);
        });
        Layer genlayer = new Layer(immutablelist.get(0));
        Layer genlayer1 = new Layer(immutablelist.get(1));
        Layer genlayer2 = new Layer(immutablelist.get(2));
        return new Layer[]{genlayer, genlayer1, genlayer2};
    }
}
