package tfcr.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import tfcr.data.CropType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CropBlock extends TFCRBlock implements IPlantable {

    // Needed for compatibility with IPlantable.
    public static final PlantType TFCRCrop = PlantType.create("TFCRCrop");

    public CropType cropType;

    private static CropBlock[] allBlocks;

    public CropBlock(Properties properties, CropType cropType) {
        super(properties, "plants/crops/" + cropType.getName());
        this.cropType = cropType;
    }

    private static void init() {
        allBlocks = new CropBlock[CropType.values().length];
        for (int i = 0; i < CropType.values().length; i++) {
            allBlocks[i] = new CropBlock(Properties.from(Blocks.WHEAT), CropType.values()[i]);
        }
    }

    public static List<CropBlock> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    public static CropBlock get(CropType type) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[type.ordinal()];
    }

    /**
     * Implements IPlantable interface.
     * @return A custom TFCR crop type
     */
    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return TFCRCrop;
    }

    /**
     * Implements IPlantable interface.
     * @return This block's default state.
     */
    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return this.getDefaultState();
    }
}
