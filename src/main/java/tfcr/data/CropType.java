package tfcr.data;

import net.minecraft.util.IStringSerializable;

public enum CropType implements IStringSerializable {
    BARLEY(12000, PlantLifeCycle.ANNUAL);


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
