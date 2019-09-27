package tfcr.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class CropBlock extends TFCRBlock implements IPlantable {

    public static final PlantType TFCRCrop = PlantType.create("TFCRCrop");

    public CropBlock(Properties properties, String name) {
        super(properties, name);
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return TFCRCrop;
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return this.getDefaultState();
    }
}
