package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.tileentity.TileEntityTree;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BlockSapling extends BlockBush implements ISelfRegisterBlock, ISelfRegisterItem {

    // TODO carry wood type as field

    private static BlockSapling sapling;

    private static final int MAX_AGE = 8;

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public BlockSapling() {
        super(Block.Properties.from(Blocks.OAK_SAPLING));

        this.setRegistryName(TFCR.MODID, "sapling");
    }

    public static void init() {
        sapling = new BlockSapling();
    }

    public static List<BlockSapling> getAllBlocks() {
        if (sapling == null) {
            init();
        }
        return Collections.singletonList(sapling);
    }

    public static BlockSapling get() {
        // TODO return different wood variants when added
        return sapling;
    }

    public static int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Fills out the possible states for this Block.
     *
     * Needed to setup blockstates for this block. Replaces metadata in <=1.12.
     * @param builder
     */
    @Override
    public void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(AGE);
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
        return new TileEntityTree();
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
