package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public class CampfireBlock extends TFCRBlock {

    public CampfireBlock() {
        super(
                Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(0.2f),
                "campfire"
        );
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }
}
