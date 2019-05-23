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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.blocks.BlockBranch;
import tfcr.blocks.BlockLog;
import tfcr.blocks.BlockSapling;
import tfcr.blocks.BlockTallSapling;
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
//        System.out.println("Cleaning up responsible blocks");
//        if (hasWorld() && !world.isRemote) {
//            for (BlockPos pos : treeBlocks) {
//                world.removeBlock(pos);
//            }
//        }
        // TODO check against structure and delete as needed
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

//                if (age == 1) {
//                    // now responsible for up blocks
//                    if (treeBlocks != null) {
//                        for (int i = 1; i < 5; i++) {
//                            treeBlocks.add(pos.up(i));
//                        }
//                    }
//                }

//                IBlockState newState = this.world.getBlockState(pos).with(BlockSapling.AGE, age);
//                IBlockState newState;
//                if (age < 1) {
//                    newState = BlockSapling.get().getDefaultState().with(BlockSapling.AGE, age);
//                } else if (age < 7) {
//                    newState = BlockBranch.get(WoodType.ASH, 2 * age, false).getDefaultState().with(BlockBranch.ROOT, true);
//                } else {
//                    // TODO replace with log block
//                    newState = BlockBranch.get(WoodType.ASH, 14, false).getDefaultState().with(BlockBranch.ROOT, true);
//                }
//
//                if (variant == 0) {
//                    for (int i = 1; i < 5; i++) {
//                        world.setBlockState(pos.up(i), BlockBranch.get(WoodType.ASH, 2 * age, false).getDefaultState(), 3);
//                    }
//                } else {
//                    for (int i = 1; i < 5; i++) {
//                        world.setBlockState(pos.up(i), BlockBranch.get(WoodType.ASH, Math.max(2, 2 * (age - 1)), false).getDefaultState(), 3);
//                    }
//                }
//
//                world.setBlockState(pos, newState, 3);

                if (age > 0 && age <= 3) {
                    spawnStructure("oak_age_" + age);
                }
                markDirty();
            }
        }
    }

    private void spawnStructure(String name) {
        System.out.println("Trying to spawn structure at pos: " + pos);
        // Only spawn structures on the server world
        if (world.isRemote) {
            System.out.println("Remote- failing");
            return;
        }
        // TODO try to cleanup old structure before spawning new one

        // Access the Template for the tree's structure
        ResourceLocation location = new ResourceLocation(TFCR.MODID, name);
        Template structure = ((WorldServer) world).getStructureTemplateManager().getTemplate(location);
        if (structure == null) {
            System.out.println("Failed to find structure: " + location);
            return;
        }

        // Rotation center should be the trunk of the tree.
        BlockPos size = structure.getSize();
        BlockPos center = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);

        // Add the structure to the world.
        // TODO maybe make variant determine a random rotation/mirroring.
        PlacementSettings settings = new PlacementSettings().setCenterOffset(center);
        structure.addBlocksToWorld(world, pos.add(-center.getX(), 0, -center.getZ()), settings);
        System.out.println("Successfully added blocks.");

        // Validate the tree after it is added into the world, by making sure
        // there is a TileEntityTree after the structure blocks are placed down.
        IBlockState newRoot = world.getBlockState(pos);
        Block blockType = newRoot.getBlock();
        TileEntityTree tileEntityTree = ((TileEntityTree) world.getTileEntity(pos));

        if (blockType instanceof BlockSapling) {
            // Valid, for now do nothing else
        } else if (blockType instanceof BlockTallSapling) {
            // Valid, for now do nothing else
        } else if (blockType instanceof BlockBranch) {
            // Ensure this is a root
            world.setBlockState(pos, newRoot.with(BlockBranch.ROOT, true));
        } else if (blockType instanceof BlockLog) {
            // Valid, for now do nothing else
        } else {
            // Some other block was generated which can't support a TileEntityTree.
            // This means there's a bug somewhere in the code, so we report it.
            // Either way, we return, since there won't be a TileEntityTree to modify.
            // TODO crash? Log error?
            System.out.println("TileEntityTree created invalid trunk block: " + newRoot.getBlock().toString());
            return;
        }

        // Ensure root block has proper TileEntity
        if (tileEntityTree == null) {
            System.out.println("No TE found for root block: " + newRoot.getBlock().toString());
            return;
        }

        // Update the TileEntity there, since it is recreated if the Block type changes.
        // TODO update all other values.
        tileEntityTree.age = this.age;
        System.out.println("Updated base of tree to have TileEntity.");
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
