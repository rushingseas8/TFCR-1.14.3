package tfcr.worldgen;

import net.minecraft.world.gen.feature.IFeatureConfig;
import tfcr.data.WoodType;

public class TreeFeatureConfig implements IFeatureConfig {

    private WoodType woodType;
    private int age;

    public TreeFeatureConfig(WoodType woodType, int age) {
        this.woodType = woodType;
        this.age = age;
    }

    public WoodType getWoodType() {
        return woodType;
    }

    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
