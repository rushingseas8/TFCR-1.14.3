package tfcr.tileentity;

import net.minecraft.block.Block;
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
import tfcr.data.WoodType;
import tfcr.init.ModBlocks;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class TileEntityTree extends TileEntity implements ITickable {
    public static TileEntityType<TileEntityTree> TREE;

    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        TREE = TileEntityType.register(TFCR.MODID + ":tile_entity.tree", TileEntityType.Builder.create(TileEntityTree::new));
        //tileEntityRegistry.register();
    }

    public TileEntityTree() {
        super(TREE);
        System.out.println("TileEntityTree init");
    }

    // Local count of ticks. We only take action once every N ticks.
    private int count = 0;

    // How old this TileEntity is. Later will be used to determine tree shape.
    private int age = 0;

    // Are we done growing? True when we hit age cap, or this variant of tree
    // cannot grow any further in the current constraints.
    // Currently makes this stop ticking forever; later should be rarely ticked.
    private boolean doneGrowing = false;

    private int variant = -1;

    private ArrayList<BlockPos> treeBlocks;

    @Override
    public void remove() {
        super.remove();
        System.out.println("Cleaning up responsible blocks");
        if (hasWorld() && !world.isRemote) {
            for (BlockPos pos : treeBlocks) {
                world.removeBlock(pos);
            }
        }
    }

    @Override
    public void tick() {
        if (variant == -1 && hasWorld() && !world.isRemote) {
            variant = world.getRandom().nextInt(2);
            treeBlocks = new ArrayList<>();
            System.out.println("Initializing variant to: " + variant);
        }

        if (doneGrowing) {
            return;
        }

        count++;
        if (count % 100 == 0) {
            if (!world.isRemote) {
                age++;

                System.out.println("Tile entity tree tick. Age = " + age);

                // Logs not added in, TODO remove the -1 when logs added
                if (age > BlockSapling.getMaxAge() - 1) {
                    System.out.println("Done growing now");
                    age = BlockSapling.getMaxAge() - 1;
                    doneGrowing = true;
                }

                if (age == 1) {
                    // now responsible for up blocks
                    if (treeBlocks != null) {
                        for (int i = 1; i < 5; i++) {
                            treeBlocks.add(pos.up(i));
                        }
                    }
                }

//                IBlockState newState = this.world.getBlockState(pos).with(BlockSapling.AGE, age);
                IBlockState newState;
                if (age < 1) {
                    newState = BlockSapling.get().getDefaultState().with(BlockSapling.AGE, age);
                } else if (age < 7) {
                    newState = BlockBranch.get(WoodType.ASH, 2 * age, false).getDefaultState().with(BlockBranch.ROOT, true);
                } else {
                    // TODO replace with log block
                    newState = BlockBranch.get(WoodType.ASH, 14, false).getDefaultState().with(BlockBranch.ROOT, true);
                }

                if (variant == 0) {
                    for (int i = 1; i < 5; i++) {
                        world.setBlockState(pos.up(i), BlockBranch.get(WoodType.ASH, 2 * age, false).getDefaultState(), 3);
                    }
                } else {
                    for (int i = 1; i < 5; i++) {
                        world.setBlockState(pos.up(i), BlockBranch.get(WoodType.ASH, Math.max(2, 2 * (age - 1)), false).getDefaultState(), 3);
                    }
                }

                world.setBlockState(pos, newState, 3);

                markDirty();
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        compound = super.write(compound);
        compound.setInt("age", age);
        compound.setBoolean("doneGrowing", doneGrowing);
        compound.setInt("variant", variant);
        System.out.println("Wrote age: " + age);
        return compound;
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        this.age = compound.getInt("age");
        this.doneGrowing = compound.getBoolean("doneGrowing");
        this.variant = compound.getInt("variant");
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
