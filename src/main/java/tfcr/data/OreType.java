package tfcr.data;

import net.minecraft.util.IStringSerializable;

public enum OreType implements IStringSerializable {
    BISMUTHINITE,
    BITUMINOUS_COAL,
    BORAX,
    CASSITERITE,
    CINNABAR,
    CRYOLITE,
    GALENA,
    GARNIERITE,
    GRAPHITE,
    GYPSUM,
    HEMATITE,
    JET,
    KAOLINITE,
    KIMBERLITE,
    LAPIS_LAZULI,
    LIGNITE,
    LIMONITE,
    MAGNETITE,
    MALACHITE,
    MICROCLINE,
    NATIVE_COPPER,
    NATIVE_GOLD,
    NATIVE_PLATINUM,
    NATIVE_SILVER,
    OLIVINE,
    PETRIFIED_WOOD,
    PITCHBLENDE,
    SALTPETER,
    SATINSPAR,
    SELENITE,
    SERPENTINE,
    SPHALERITE,
    SULFUR,
    SYLVITE,
    TETRAHEDRITE;

    public int meltingTemp; // Unused for now

    OreType() {
        this.meltingTemp = 0;
    }

    OreType(int meltingTemp) {
        this.meltingTemp = meltingTemp;
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }

}
