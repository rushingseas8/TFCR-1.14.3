package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
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
        if (newState.getBlock() instanceof BlockBranch) {
            System.out.println("Sapling replaced by branch");
            if (newState.get(BlockBranch.ROOT)) {
                System.out.println("Branch is considered root. Not removing TileEntity.");
                return; // no replace
            }
        } else {
            System.out.println("New state is block type: " + newState.getBlock());
        }


        System.out.println("Conditions not met. Removing TileEntity.");
//        System.out.println("onReplaced for Sapling. Remote: " + worldIn.isRemote);
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

//    private Block getTargetBlockForAge(int age) {
//        switch(age) {
//            case 0:
//            case 1: return sapling;
//            case 2: return BlockBranch.get(2);
//            case 3: return BlockBranch.get(4);
//            case 4: return BlockBranch.get(6);
//            case 5: return BlockBranch.get(8);
//            case 6: return BlockBranch.get(10);
//            case 7: return BlockBranch.get(12);
//            case 8: return BlockBranch.get(14);
//            default: throw new IllegalArgumentException("Invalid age for BlockSapling: " + age);
//        }
//    }

    // TODO add more drops
    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
        int age = state.get(AGE);
//        System.out.println("Age: " + age);
//        if (age <= 1) {
//            drops.clear();
//            drops.add(new ItemStack(this, 1));
//            return;
//        }
//        getTargetBlockForAge(age).getDrops(state, drops, world, pos, fortune);
    }

    // TODO copy directly from block branch
    @Override
    public float getBlockHardness(IBlockState state, IBlockReader world, BlockPos pos) {
        int age = state.get(AGE);
//        if (age <= 1) {
            return this.blockHardness;
//        }
//        return getTargetBlockForAge(age).getBlockHardness(state, world, pos);
    }

    @Override
    public SoundType getSoundType(IBlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) {
        int age = state.get(AGE);
//        if (age <= 1) {
            return this.soundType;
//        }
//        return getTargetBlockForAge(age).getSoundType(state, world, pos, entity);
    }

    //    @Override
//    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
//        int age = state.get(AGE);
//        if (age <= 1) {
//            return super.getShape(state, worldIn, pos);
//        }
//        return getTargetBlockForAge(age).getShape(state, worldIn, pos);
        //return super.getShape(state, worldIn, pos);
//    }

//    @Override
//    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
//        return getShape(state, worldIn, pos);
//    }
}
