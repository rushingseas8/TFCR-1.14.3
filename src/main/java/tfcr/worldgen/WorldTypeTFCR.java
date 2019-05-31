package tfcr.worldgen;

import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.IContextExtended;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.layer.GenLayerZoom;
import tfcr.init.ModBiomes;

import java.util.function.LongFunction;

public class WorldTypeTFCR extends WorldType {

    public WorldTypeTFCR() {
        super("TerraFirmaCraft");
        System.out.println("TFCR WorldType created");
    }

    /**
     * Returns the Chunk generator for this world.
     *
     * The Chunk generator determines the actual structural terrain of the world,
     * using the Biome data from LayerUtilsTFCR and getBiomeLayer (in this class)
     * to modify the final heightmap.
     *
     * @param world
     * @return
     */
    @Override
    public IChunkGenerator<?> createChunkGenerator(World world) {
        //SingleBiomeProviderSettings settings = BiomeProviderType.FIXED.createSettings().setBiome(ModBiomes.BIOME_TFCR_BASE);
        return new ChunkGeneratorTFCR(world, new BiomeProviderTFCR(world.getWorldInfo(), new OverworldGenSettings()), new OverworldGenSettings());
    }

    /**
     * Sets the specific Biomes used in a given Area.
     *
     * Called during LayerUtilsTFCR to randomly place Biomes in a way that makes
     * logical sense within the world. Based on IForgeWorldType.getBiomeLayer.
     *
     * @param parentLayer
     * @param chunkSettings
     * @param contextFactory
     * @param <T>
     * @param <C>
     * @return
     */
    @Override
    public <T extends IArea, C extends IContextExtended<T>> IAreaFactory<T> getBiomeLayer(IAreaFactory<T> parentLayer, OverworldGenSettings chunkSettings, LongFunction<C> contextFactory) {
        parentLayer = (new GenLayerBiome(chunkSettings)).apply((IContextExtended) contextFactory.apply(200L), parentLayer);
        parentLayer = LayerUtilsTFCR.repeat(1000L, GenLayerZoom.NORMAL, parentLayer, 2, contextFactory);
        parentLayer = GenLayerBiomeEdge.INSTANCE.apply((IContextExtended) contextFactory.apply(1000L), parentLayer);
        return parentLayer;
    }

    // TODO look at ChunkGeneratorOverworld and OverworldDimension classes for reference
}
