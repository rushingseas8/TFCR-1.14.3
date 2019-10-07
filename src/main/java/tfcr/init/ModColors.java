package tfcr.init;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.BranchBlock;
import tfcr.blocks.GrassBlock;
import tfcr.blocks.LeavesBlock;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
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
        final IBlockColor leafColorHandler = (state, blockAccess, pos, tintIndex) -> {
            if (blockAccess != null && pos != null) {
                return BiomeColors.getFoliageColor(blockAccess, pos);
            }

            return FoliageColors.getDefault();
        };

        // Register all leaves to have the leaf coloring handler
        blockColors.register(leafColorHandler, LeavesBlock.getAllBlocks().toArray(new LeavesBlock[0]));

        // Ditto for branches (TODO make this only apply to leafy variants)
        blockColors.register(leafColorHandler, BranchBlock.getAllBlocks().toArray(new BranchBlock[0]));

        // A handler for grass variations. Copied from BlockColors for grass tints.
        final IBlockColor grassColorHandler = (state, blockAccess, pos, tintIndex) ->
                blockAccess != null && pos != null ?
                        BiomeColors.getGrassColor(blockAccess, pos) :
                        GrassColors.get(0.5D, 1.0D);

        // Register grass blocks
        blockColors.register(grassColorHandler, GrassBlock.getAllBlocks().toArray(new GrassBlock[0]));
    }
}
