package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tfcr.init.*;

public class WickerBlock extends TFCRBlock {

    public enum WickerDurability implements IStringSerializable {
        BARE("bare"),
        PARTIAL("partial"),
        FULL("full");

        private final String name;

        private WickerDurability(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static final int REPAIR_COST_MUD_BLOB = 4;
    private static final int REPAIR_COST_MUD_BLOB_PARTIAL = 2;
    private static final int REPAIR_COST_MUD_BLOCK = 1;

    public static EnumProperty<WickerDurability> DURABILITY = EnumProperty.create("durability", WickerDurability.class);

    public WickerBlock() {
        super(Block.Properties.from(Blocks.OAK_FENCE), "wicker_block");
    }

    @Override
    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DURABILITY);
        // TODO if we remove LeavesBlock subclass then remove distance/persistent
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.get(DURABILITY) == WickerDurability.FULL ? 0 : 1;
    }

    public boolean isSolid(BlockState state) {
        return state.get(DURABILITY) == WickerDurability.FULL;
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return true;
        }

        // We can't add mud to this block if it already has maximum mud on it.
        if (state.get(DURABILITY) == WickerDurability.FULL) {
            return false;
        }

        // Now we know this block is either partial or bare.
        // Check if the player is holding mud blobs.
        ItemStack heldItem = player.getHeldItem(handIn);
        if (heldItem.getItem() == ModItems.mud_ball) {
            if (state.get(DURABILITY) == WickerDurability.BARE) {
                // Bare -> full costs 4 mud blobs
                if (heldItem.getCount() >= REPAIR_COST_MUD_BLOB || player.isCreative()) {
                    worldIn.setBlockState(pos, state.with(DURABILITY, WickerDurability.FULL), 1 | 2);
                    if (!player.isCreative()) {
                        heldItem.shrink(REPAIR_COST_MUD_BLOB);
                    }
                    return false;
                } else if (heldItem.getCount() >= REPAIR_COST_MUD_BLOB_PARTIAL) { // Bare -> partial costs 2 mud blobs
                    worldIn.setBlockState(pos, state.with(DURABILITY, WickerDurability.PARTIAL), 1 | 2);
                    if (!player.isCreative()) {
                        heldItem.shrink(REPAIR_COST_MUD_BLOB_PARTIAL);
                    }
                    return true;
                } else { // Insufficient mud
                    return false;
                }
            } else if (state.get(DURABILITY) == WickerDurability.PARTIAL) {
                // Partial -> full costs 2 mud blobs
                if (heldItem.getCount() >= REPAIR_COST_MUD_BLOB_PARTIAL || player.isCreative()) {
                    worldIn.setBlockState(pos, state.with(DURABILITY, WickerDurability.FULL), 1 | 2);
                    if (!player.isCreative()) {
                        heldItem.shrink(REPAIR_COST_MUD_BLOB_PARTIAL);
                    }
                    return true;
                } else { // Insufficient mud
                    return false;
                }
            }
        }

        // Check if the player is holding a mud block. Can turn any wattle block into a full wattle+daub with just 1.
        if (heldItem.getItem() == ModItems.itemForBlocks.get(MudBlock.get())) {
            if (heldItem.getCount() >= REPAIR_COST_MUD_BLOCK || player.isCreative()) {
                worldIn.setBlockState(pos, state.with(DURABILITY, WickerDurability.FULL), 1 | 2);
                if (!player.isCreative()) {
                    heldItem.shrink(REPAIR_COST_MUD_BLOCK);
                }
                return true;
            }
        }

        return false;
    }
}
