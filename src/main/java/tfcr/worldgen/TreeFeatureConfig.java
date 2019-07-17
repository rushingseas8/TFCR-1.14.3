package tfcr.worldgen;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.IFeatureConfig;
import tfcr.data.WoodType;

import java.util.stream.IntStream;

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

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        T t;
        t = ops.createMap(
                ImmutableMap.of(
                        ops.createString("WoodType"), ops.createString(woodType.toString()),
                        ops.createString("Age"), ops.createInt(age)
                )
            );
        return new Dynamic<>(ops, t);
    }

    public static <T> TreeFeatureConfig deserialize(Dynamic<T> dynamic) {
        DynamicOps<T> ops = dynamic.getOps();
        WoodType woodType = WoodType.valueOf(dynamic.getElement("WoodType").flatMap(ops::getStringValue).orElse("Oak"));
        // dynamic.get("WoodType").asString("Oak"); // does this work?
        int age = dynamic.get("Age").asInt(0);

        return new TreeFeatureConfig(woodType, age);
    }
}
