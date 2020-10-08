package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import tfcr.TFCR;
import tfcr.data.TerrainType;
import tfcr.worldgen.LayerUtilsTFCR;

public class BaseTFCRBiome extends Biome {

    protected int minTemp;
    protected int maxTemp;
    protected int minPrecip;
    protected int maxPrecip;
    protected TerrainType terrainType;

    protected BaseTFCRBiome instance;

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
    protected BaseTFCRBiome(int minTemp, int maxTemp, int minPrecip, int maxPrecip, TerrainType terrainType, Biome.Builder builder) {
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

        // Try to generate a default name that looks like, e.g., "wetland_flat".
        // Skip over the water biomes, since they handle their own logic.
        if (LayerUtilsTFCR.isWater(terrainType.ordinal())) {
            return;
        }

        String defaultName = this.getClass().getSimpleName().toLowerCase();
        defaultName = defaultName.replace("biome", "");
        defaultName += "_" + terrainType.name().toLowerCase();

        setRegistryName(TFCR.MODID, defaultName);
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
     * @param type
     * @return
     */
    public BaseTFCRBiome getActualBiome(TerrainType type) {
        return this;
    }

    /**
     * @param time Time of year float in the interval [0, 1]. 0 is January 1.
     * @return The temperature for this given biome at this time.
     */
    public float getTemperature(float time) {
        double scale = ((maxTemp - minTemp) / 2.0);
        double baseTemp = Math.sin((2 * Math.PI * time) - (Math.PI / 2)) + 1;
        return (float)(scale * baseTemp) + minTemp;
    }
}
