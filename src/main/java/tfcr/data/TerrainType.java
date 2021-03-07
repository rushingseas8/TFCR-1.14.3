package tfcr.data;

/**
 * An Enum that represents different types of terrain.
 *
 * These are essentially placeholder biomes used in worldgen, and need a corresponding
 * temperature/precipitation value to determine the final biome type.
 */
public enum TerrainType {
    // Technical biomes. These are used as-is.
    DEEP_OCEAN(-1.8f, 0.1f), // Based on deep cold ocean biome
    OCEAN(-1f, 0.1f), // Based on ocean biome
    // Shallow ocean?
    BEACH(0f, 0.025f), // Based on beach biome
    CLIFF(0.1f, 0.8f), // Based on stone shore biome
    RIVER(-0.5f, 0.0f), // Based on river biome
    ESTUARY(-0.75f, 0.05f), // Intermediate between river + ocean. Brackish river.
    RIVER_EDGE(0.0f, 0.05f), // Border between river + land
    RIVER_DELTA(0.2f, 0.05f), // Borders between river + ocean
    RIVER_DELTA_EDGE(0.2f, 0.05f), // Border between delta + land

    // Placeholder biomes. These need a temp/precip to be real biomes.
    // Lake?
    FLAT(0.2f, 0.05f), // Based on plains
    SMALL_HILLS(0.5f, 0.2f), // Based on forest
    BIG_HILLS(0.8f, 0.4f), // Based on dark forest hills biome
    MOUNTAINS(1.5f, 0.75f); // Based on mountains biome
    // Highland plains? High depth, but low scale

    public float depth;
    public float scale;

    TerrainType(float depth, float scale) {
        this.depth = depth;
        this.scale = scale;
    }
}
