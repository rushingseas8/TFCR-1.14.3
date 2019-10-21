package tfcr.data;

import net.minecraft.util.IStringSerializable;

/**
 * An enum representing soil fertility levels. Used for dirt and farmland blocks.
 *
 * Salted, if implemented, can only occur when the player waters farmland with saltwater.
 *  Salted farmland cannot sustain crops for a long period of time; worse than barren.
 * Barren indicates no plants will grow (except fixing plants).
 * Low fertility means plants will grow, albeit slowly and with lower yield.
 * Normal is the same as vanilla farmland.
 * Fertile will boost crop growth speed and yield.
 */
public enum Fertility implements IStringSerializable {
    // SALTED, // possibly as a joke
    BARREN,
    INFERTILE,
    NORMAL,
    FERTILE
    ;

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}