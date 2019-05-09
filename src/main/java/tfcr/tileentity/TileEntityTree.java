package tfcr.tileentity;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.blocks.BlockBranch;
import tfcr.blocks.BlockSapling;
import tfcr.init.ModBlocks;

import javax.annotation.Nonnull;

public class TileEntityTree extends TileEntity implements ITickable {
    public static TileEntityType<TileEntityTree> TREE;

    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        TREE = TileEntityType.register(TFCR.MODID + ":tile_entity.tree", TileEntityType.Builder.create(TileEntityTree::new));
        //tileEntityRegistry.register();
    }

    public TileEntityTree() {
        super(TREE);
    }

    int count = 0;
    int age = 0;

    @Override
    public void tick() {
        // Should be server-side only
        if (!world.isRemote) {
            return;
        }

        count++;
        if (count % 100 == 0) {
            System.out.println("Tile entity tree tick. Type = " + age);

            age++;
            if (age > 5) {
                age = 5;
            }

            markDirty();

//            IBlockState newState = ModBlocks.block_branch_8.getDefaultState()
//                .with(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y);
            IBlockState newState = getBlockState().with(BlockSapling.AGE, age);

            //world.getBlockState(pos).onReplaced(world, pos, newState, false);
            world.setBlockState(pos, newState, 7);

//            updateContainingBlockInfo();
//            getBlockState();

            System.out.println(world.getBlockState(pos));

            markDirty();

            //world.addTileEntity()
        }
        //System.out.println("Tile entity tree tick!");
    }

    @Nonnull
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        //return super.write(compound);
        compound = super.write(compound);
        compound.setInt("age", age);
        System.out.println("Wrote age: " + age);
        return compound;
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        this.age = compound.getInt("age");
        System.out.println("Read type: " + age);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        System.out.println("getUpdatePacket");

        NBTTagCompound nbtTag = new NBTTagCompound();
        this.write(nbtTag);
        return new SPacketUpdateTileEntity(this.pos, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        System.out.println("onDataPacket");
        read(packet.getNbtCompound());
    }


}
