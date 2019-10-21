package tfcr.data;

import net.minecraft.util.IStringSerializable;

public enum CropType implements IStringSerializable {
    BARLEY;

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
