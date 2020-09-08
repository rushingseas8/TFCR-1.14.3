package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModTabs;
import tfcr.tileentity.TreeTileEntity;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class SaplingBlock extends BushBlock implements ISelfRegisterBlock, ISelfRegisterItem, IBlockWood {

    public WoodType woodType;

    private static SaplingBlock[] saplings;

    private static final int MAX_AGE = 8;

    public SaplingBlock(WoodType woodType) {
        super(Block.Properties.from(Blocks.OAK_SAPLING));
        this.woodType = woodType;
        this.setRegistryName(TFCR.MODID, "sapling/" + woodType.getName());
    }

    public static void init() {
        saplings = new SaplingBlock[WoodType.values().length];
        for (int i = 0; i < WoodType.values().length; i++) {
            saplings[i] = new SaplingBlock(WoodType.values()[i]);
        }
    }

    public static List<SaplingBlock> getAllBlocks() {
        if (saplings == null) {
            init();
        }
        return Arrays.asList(saplings);
    }

    public static SaplingBlock get(WoodType woodType) {
        return saplings[woodType.ordinal()];
    }

    @Override
    public Item registerItem(IForgeRegistry<Item> itemRegistry) {
        Item toReturn = new BlockItem(this, new Item.Properties().group(ModTabs.TFCR_WOOD)).setRegistryName(TFCR.MODID, getRegistryName().getPath());
        itemRegistry.register(toReturn);
        return toReturn;
    }

    @Override
    public WoodType getWoodType() {
        return this.woodType;
    }

    public static int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * All TFCR 1-tall saplings have a TileEntity.
     * @param state
     * @return
     */
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TreeTileEntity(this.woodType);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        System.out.println("Sapling onReplaced called.");
        if (newState.getBlock() instanceof TallSaplingBlock) {
            System.out.println("Sapling replaced by tall sapling.");
            if (newState.get(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                System.out.println("Keeping TileEntity.");
                return;
            }
            System.out.println("Tall sapling was invalid. Removing.");
        } else if (newState.getBlock() instanceof BranchBlock) {
            System.out.println("Sapling replaced by branch.");
            if (newState.get(BranchBlock.ROOT)) {
                System.out.println("Keeping TileEntity.");
                return; // no replace
            }
        } else {
            System.out.println("New state is block type: " + newState.getBlock());
        }


        System.out.println("Conditions not met. Removing TileEntity.");
//        System.out.println("onReplaced for Sapling. Remote: " + worldIn.isRemote);
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private static final float SLOWDOWN = 0.1f;

    /**
     * Slow down entities passing through this block.
     */
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        entityIn.setMotion(entityIn.getMotion().mul(SLOWDOWN, 1.0D, SLOWDOWN));
    }
}
