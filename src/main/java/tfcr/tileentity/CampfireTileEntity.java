package tfcr.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.blocks.CampfireBlock;
import tfcr.container.CampfireContainer;

import java.util.ArrayList;
import java.util.List;

public class CampfireTileEntity extends LockableTileEntity implements ITickableTileEntity {

    private static TileEntityType<CampfireTileEntity> CAMPFIRE_TILE_ENTITY;

    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        List<Block> validBlocks = new ArrayList<>();
        validBlocks.add(new CampfireBlock());

        // TODO build with null? is that right?
        CAMPFIRE_TILE_ENTITY = TileEntityType.Builder.create(CampfireTileEntity::new, validBlocks.toArray(new Block[0])).build(null);
        CAMPFIRE_TILE_ENTITY.setRegistryName(TFCR.MODID, "tile_entity_tree");
        tileEntityRegistry.register(CAMPFIRE_TILE_ENTITY);
    }

    // internal constructor for TileEntityType initialization
    public CampfireTileEntity() {
        super(CAMPFIRE_TILE_ENTITY);
    }

//    public CampfireTileEntity() {
//        super(CAMPFIRE_TILE_ENTITY);
//    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected Container createMenu(int windowID, PlayerInventory playerInventory) {
        return new CampfireContainer(windowID, playerInventory);
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void tick() {

    }
}
