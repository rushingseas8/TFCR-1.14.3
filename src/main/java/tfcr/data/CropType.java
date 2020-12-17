package tfcr.data;

import net.minecraft.util.IStringSerializable;

public enum CropType implements IStringSerializable {
    // Note that one real life month translates to 8400 in-game ticks.

    // Barley is usually planted in spring, and harvested early fall.
    // ~6 months = 180 days
    BARLEY(6 * 8400, PlantLifeCycle.PERENNIAL),
    // Oats grow quickly, in just 2 months. Can be commonly grown after wheat.
    // However, lower yield/less tolerant of disease.
    OAT(2 * 8400, PlantLifeCycle.PERENNIAL),
    // Depending on variety, 3-6 months. Chose 4.5 as a decent middle ground.
    RICE((int)(4.5 * 8400), PlantLifeCycle.PERENNIAL),
    // Rye is more hardy (can tolerate saltwater, infertile soil, and colder winter planting)
    // Less heat tolerant than oat and barley. I'm just gonna say same growth as wheat
    RYE(5 * 8400, PlantLifeCycle.PERENNIAL),
    // Plant wheat earliest spring, harvest late summer. ~5 months = 150 days
    WHEAT(5 * 8400, PlantLifeCycle.PERENNIAL),

    // 60-90 day growth time
    BELL_PEPPER((int)(2.5 * 8400), PlantLifeCycle.ANNUAL),
    CABBAGE((int)(4.5 * 8400), PlantLifeCycle.ANNUAL),
    // One of the fastest growing crops. Can be turned into sugar.
    BEET((int)(1.75 * 8400), PlantLifeCycle.ANNUAL),
    // Fastest growing crop.
    RADISH((int)(1.25 * 8400), PlantLifeCycle.ANNUAL),
    CORN(3 * 8400, PlantLifeCycle.ANNUAL),
    // Grows moderately quickly, but can be harvested multiple times
    // Cannot tolerate cold temperatures.
    TOMATO(2 * 8400, PlantLifeCycle.ANNUAL),
    PEA(2 * 8400, PlantLifeCycle.ANNUAL),

    CARROT((int)(2.5 * 8400), PlantLifeCycle.BIENNIAL),
    // Very slow. Grows over winter to produce bulbs, which are re-planted.
    GARLIC(7 * 8400, PlantLifeCycle.BIENNIAL),
    ONION(3 * 8400, PlantLifeCycle.BIENNIAL),
    ;


    /**
     * Does this crop live for one, two, or many years?
     */
    public enum PlantLifeCycle {
        ANNUAL,
        PERENNIAL,
        BIENNIAL
    }

    /**
     * Boundary temperature conditions for this crop.
     *
     * If temperature is in [idealMinTemp, idealMaxTemp], then this plant will
     * grow without any penalties applied.
     *
     * If temperature is in [growMinTemp, growMaxTemp], then this plant will
     * grow (however slowly), with penalties linearly increasing the further
     * away from the ideal range the temperature goes. In other words, the
     * penalties increase from [idealMinTemp, growMinTemp], and from
     * [idealMaxTemp, growMaxTemp].
     */
    public int growMinTemp, growMaxTemp, idealMinTemp, idealMaxTemp;

    /**
     * If true, this plant will return nutrients to the soil upon harvest.
     * By false (by default), this plant will consume nutrients upon harvest.
     *
     * A successful harvest is at a plant's maximum growth stage (or either
     * fruit/seed/flower stage, as applicable). This means you cannot reap
     * the benefits of a crop without some soil nutrient cost.
     */
    public boolean returnsNutrients;

    /**
     * What is the minimum sun level this crop needs to grow successfully?
     *
     * Only sunlight levels will be considered for the sake of growth.
     *
     * Values between [minSunLevel + 1, 15] will provide a linear increase
     * in growth rate, with a maximum always being at 15 (direct sun exposure).
     * At any level strictly below minSunLevel, this crop will NEVER grow.
     *
     * There is no maximum sun level, since enforcing such a value (e.g., for
     * implementing plants that cannot tolerate full sun) would be logistically
     * difficult within Minecraft (since it would likely involve building shade
     * grids above crops, which isn't realistic).
     */
    public int minSunLevel;

    /**
     * Average time to fully grow, in seconds. Note a Minecraft day is 20 minutes.
     *
     * This can usually be found by multiplying the mean grow window by (84/365.25), and
     * then multiplying by 20 and by 60. This converts real life days to in-game seconds.
     *
     * Note that there is a soft minimum value for this, equal to growthStages * 68.27.
     * For 16 growth stages, this is equal to 1092 seconds, which is about 1 MC day.
     * Below this point, the probability for growth is >= 1, and so growth becomes
     * limited by the random tick rate.
     */
    public int meanGrowthTime;

    /**
     * Computed value. What is the probability of growing when randomly ticked,
     * such that this plant will be fully grown on average after "meanGrowthTime"
     * seconds, assuming ideal growing conditions?
     */
    public float idealGrowthChance;

    public PlantLifeCycle plantLifeCycle;

    CropType(int meanGrowthTime, PlantLifeCycle plantLifeCycle) {
        this.meanGrowthTime = meanGrowthTime;
        this.idealGrowthChance = (float)(16 / (meanGrowthTime / 68.27));
        this.plantLifeCycle = plantLifeCycle;
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
