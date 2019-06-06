package tfcr.worldgen.biome;

public class WetlandBiome {
    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new WetlandBiomeFlat(),
                new WetlandBiomeSmallHill(),
                new WetlandBiomeBigHill(),
                new WetlandBiomeMountain()
        };
    }
}
