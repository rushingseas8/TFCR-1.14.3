package tfcr.data;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import tfcr.blocks.BlockBranch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreeStructure {
    // Each entry in the arraylist contains the shape of a tree for a given age.
    private ArrayList<HashMap<Vec3i, IBlockState>> structure;

    public boolean canPlace(World world, BlockPos base, int age) {
        if (age < 0 || age > structure.size() - 1) {
            System.out.println("canPlace called with invalid age.");
            return false;
        }

        HashMap<Vec3i, IBlockState> currentStructure = structure.get(age);

        for (Map.Entry<Vec3i, IBlockState> entry : currentStructure.entrySet()) {
            BlockPos testPos = base.add(entry.getKey());
            IBlockState state = world.getBlockState(testPos);

            if (!(state instanceof BlockBranch || state instanceof BlockLeaves)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Should we place this specific block at this position?
     *
     * We should place branches over leaves of any kind; we should not place leaves
     * over existing leaves. Neither branches nor leaves can override any other blocks.
     * TODO: consider more options. Logs? Fluids?
     *
     * Return true if we should place this block at the position, even if
     * a block already exists. False if we shouldn't.
     *
     * @param world
     * @param pos
     * @param toPlace
     * @return
     */
    public boolean shouldPlace(World world, BlockPos pos, IBlockState toPlace) {
        Block existing = world.getBlockState(pos).getBlock();
        Block replacing = toPlace.getBlock();

        // Can grow into air always
        if (existing instanceof BlockAir) {
            return true;
        }

        // Branches can grow through leaves of any kind
        if (existing instanceof BlockLeaves && (replacing instanceof BlockBranch || replacing instanceof BlockLog)) {
            return true;
        }

        // Leaves can override existing leaves? Would cause ownership of nearby trees'
        // leaves to constantly change as they grow. Probably a bad idea.
        // TODO: consider the case of a single tree growing: it should be able to grow into
        //  its own leaves. We should consider the structure when growing up.
        return false;
    }
}
