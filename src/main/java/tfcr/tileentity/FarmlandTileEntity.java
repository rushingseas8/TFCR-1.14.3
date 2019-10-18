package tfcr.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.blocks.FarmlandBlock;
import tfcr.data.TFCRTime;

/**
 * A TileEntity registered to TFCR dirt objects. Used for handling long-term
 * drying mechanics (since the intention is for this to occur over a period of
 * 1-3 days somewhat consistently, vs. Vanilla implementation of a geometric
 * spread averaging 1/3 of a Minecraft day).
 */
public class FarmlandTileEntity extends TileEntity implements ITickableTileEntity {

    private long count;

    private static TileEntityType<FarmlandTileEntity> FARMLAND;

    public FarmlandTileEntity() {
        super(FARMLAND);
    }

    public static void registerTileEntity(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        FARMLAND = TileEntityType.Builder
                .create(
                    FarmlandTileEntity::new,
                    FarmlandBlock.getAllBlocks().toArray(new FarmlandBlock[0])
                )
                .build(null);
        FARMLAND.setRegistryName(TFCR.MODID, "tile_entity_farmland");
        tileEntityRegistry.register(FARMLAND);
    }

    @Override
    public void tick() {
        count++;

        // Only trigger an update 8 times per minecraft day
        if (count >= TFCRTime.TICKS_PER_DAY / 8) {
            count = 0;

            // If we're server-side, then flip a coin to see if we update dryness this cycle.
            // If we succeed, then decrement moisture level by 1. Note this makes a negative
            // binomial curve, with a mean of 16 cycles to fully dry a dirt block (minimum of 8),
            // which corresponds to 1-3 days on average (and anything more being increasingly rare).
            if (world != null && !world.isRemote && world.rand.nextInt(2) == 0) {
                BlockState farmlandBlockState = world.getBlockState(pos);
                int moisture = farmlandBlockState.get(net.minecraft.block.FarmlandBlock.MOISTURE);
                if (moisture == 0) {
                    return;
                }

                world.setBlockState(pos, farmlandBlockState.with(net.minecraft.block.FarmlandBlock.MOISTURE, moisture - 1));
            }
        }
    }
}
