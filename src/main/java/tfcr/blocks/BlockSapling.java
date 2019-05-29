package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
import tfcr.tileentity.TileEntityTree;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class BlockSapling extends BlockBush implements ISelfRegisterBlock, ISelfRegisterItem, IBlockWood {

    public WoodType woodType;

    private static BlockSapling[] saplings;

    private static final int MAX_AGE = 8;

    public BlockSapling(WoodType woodType) {
        super(Block.Properties.from(Blocks.OAK_SAPLING));
        this.woodType = woodType;
        this.setRegistryName(TFCR.MODID, "sapling/" + woodType.getName());
    }

    public static void init() {
        saplings = new BlockSapling[WoodType.values().length];
        for (int i = 0; i < WoodType.values().length; i++) {
            saplings[i] = new BlockSapling(WoodType.values()[i]);
        }
    }

    public static List<BlockSapling> getAllBlocks() {
        if (saplings == null) {
            init();
        }
        return Arrays.asList(saplings);
    }

    public static BlockSapling get(WoodType woodType) {
        return saplings[woodType.ordinal()];
    }

    @Override
    public void registerItem(IForgeRegistry<Item> itemRegistry) {
        itemRegistry.register(new ItemBlock(this, new Item.Properties().group(ModTabs.TFCR_WOOD)).setRegistryName(TFCR.MODID, getRegistryName().getPath()));
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
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityTree(this.woodType);
    }

    @Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
        System.out.println("Sapling onReplaced called.");
        if (newState.getBlock() instanceof BlockTallSapling) {
            System.out.println("Sapling replaced by tall sapling.");
            if (newState.get(BlockDoublePlant.HALF) == DoubleBlockHalf.LOWER) {
                System.out.println("Keeping TileEntity.");
                return;
            }
            System.out.println("Tall sapling was invalid. Removing.");
        } else if (newState.getBlock() instanceof BlockBranch) {
            System.out.println("Sapling replaced by branch.");
            if (newState.get(BlockBranch.ROOT)) {
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
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        entityIn.motionX *= SLOWDOWN;
        entityIn.motionY *= SLOWDOWN;
        entityIn.motionZ *= SLOWDOWN;
    }
}
