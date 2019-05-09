package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import tfcr.TFCR;

/**
 * Generic TFCR flower block.
 */
public class BlockFlowerTFCR extends BlockFlower {

    public BlockFlowerTFCR(String name) {
        super(Block.Properties.from(Blocks.POPPY));
        this.setRegistryName(TFCR.MODID, name);
    }
}
