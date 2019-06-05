package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;
import tfcr.worldgen.BiomeProviderTFCR;
import tfcr.worldgen.LayerUtilsTFCR;

public class BaseTFCRBiome extends Biome {

    protected int minTemp;
    protected int maxTemp;
    protected int minPrecip;
    protected int maxPrecip;
    protected TerrainType terrainType;

    protected BaseTFCRBiome instance;

    private BaseTFCRBiome() {
        super((new Biome.BiomeBuilder()).surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE)).precipitation(Biome.RainType.RAIN).category(Biome.Category.PLAINS).depth(0.125F).scale(0.05F).temperature(0.8F).downfall(0.4F).waterColor(4159204).waterFogColor(329011).parent((String)null));
        this.setRegistryName(TFCR.MODID, "tfcr_base");
    }

    /**
     * Default constructor for TFCR Biomes to use internally.
     *
     * Multiple values are set automatically in the builder for compatibility
     * with the Vanilla biome system. Specifically: depth, scale, temperature,
     * downfall, precipitation, and parent are overridden. Setting them in the
     * builder will not have any impact.
     *
     * Temperature is roughly measured in degrees Fahrenheit, and ranges from
     * [-100, 100]. Precipitation is roughly measured in inches per year, and
     * ranges from [0, 100].
     *
     * @param minTemp The lowest temperature this biome exists in
     * @param maxTemp The highest temperature this biome exists in
     * @param minPrecip The lowest amount of precipitation this biome exists in
     * @param maxPrecip The highest amount of precipitation this biome exists in
     * @param terrainType The terrain of this biome
     * @param builder Information about surface blocks, category, and water colors.
     */
    protected BaseTFCRBiome(int minTemp, int maxTemp, int minPrecip, int maxPrecip, TerrainType terrainType, BiomeBuilder builder) {
        super(builder
                .depth(terrainType.depth)
                .scale(terrainType.scale)
                .temperature((((minTemp + maxTemp) / 2.0f) + 100.0f) / 200.0f) // Average temp normalized into [0.0, 1.0]
                .downfall(((minPrecip + maxPrecip) / 2.0f) / 100.0f) // Average precip normalized into [0.0, 1.0]
                .precipitation(((minPrecip + maxPrecip) / 2f) < 0.15f ? RainType.NONE : RainType.RAIN) // Basic rain boolean (none if <15%)
                .parent("tfcr_base")
        );

        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minPrecip = minPrecip;
        this.maxPrecip = maxPrecip;
        this.terrainType = terrainType;
    }

    /**
     * Returns true if the temperature and precipitation are within this Biome's
     * bounds.
     *
     * @param temp
     * @param precip
     * @return
     */
    public final boolean matchesRange(int temp, int precip, TerrainType type) {
        return temp >= minTemp && temp < maxTemp &&
                precip >= minPrecip && precip < maxPrecip &&
                this.terrainType == type;
    }

    /**
     * If matchesRange(temp, precip) == true, then this method returns the
     * closest Biome to the requested one. By default, it will return this class.
     * In some cases, some Biome combinations are impossible (e.g., a mountainous
     * Savanna biome). In those cases, this method retuns a valid substitute
     * (in the example provided, the Chaparral biome would be returned).
     *
     * If matchesRange(temp, precip) == false, then this method returns null.
     *
     * @param temp
     * @param precip
     * @param type
     * @return
     */
    public BaseTFCRBiome provideClosest(int temp, int precip, TerrainType type) {
        if (matchesRange(temp, precip, type)) {
            return resolve(this.getClass());
        }

        return null;
    }

    protected BaseTFCRBiome resolve(Class biomeClass) {
        int index = BiomeProviderTFCR.biomeClassToIndexLookup.get(biomeClass);
        return BiomeProviderTFCR.biomes[index];
    }
}
