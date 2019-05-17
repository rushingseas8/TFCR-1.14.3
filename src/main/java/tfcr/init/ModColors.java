package tfcr.init;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tfcr.TFCR;
import tfcr.blocks.BlockLeaves;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModColors {

    /**
     * Sets up all blocks that have tinting effects. Currently only leaves.
     * @param event The color handler event (on the Forge event bus, not Mod)
     */
    @SubscribeEvent
    public static void registerBlockColourHandlers(final ColorHandlerEvent.Block event) {
        System.out.println("Registering block color handlers");
        final BlockColors blockColors = event.getBlockColors();

        // A handler that gives leaf colors
        final IBlockColor leafColorHander = (state, blockAccess, pos, tintIndex) -> {
            if (blockAccess != null && pos != null) {
                return BiomeColors.getFoliageColor(blockAccess, pos);
            }

            return FoliageColors.getDefault();
        };

        // Register all leaves to have the leaf coloring handler
        blockColors.register(leafColorHander, BlockLeaves.getAllBlocks().toArray(new BlockLeaves[0]));

    }
}
