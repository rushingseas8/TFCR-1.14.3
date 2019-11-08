package tfcr.containers;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tfcr.TFCR;

// annotation for the GUI override
@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TFCRInventoryContainerScreen extends ContainerScreen {

    private float oldMouseX;
    private float oldMouseY;

    public static final ResourceLocation INVENTORY_BACKGROUND = new ResourceLocation(TFCR.MODID, "textures/gui/container/inventory.png");

    public TFCRInventoryContainerScreen(Container p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    public TFCRInventoryContainerScreen(PlayerEntity player) {
        super(player.openContainer, player.inventory, new TranslationTextComponent("containers.tfcr.inventory"));
        // TODO link this to the player's extra capabilities (player.getcapabilities)
        // TODO link to the custom container
        // TODO make this inventory NOT open in creative mode (change the event)
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        // The +18 handles the offset due to shifting the armor slot right by one slot.
        InventoryScreen.drawEntityOnScreen(i + 51 + 18, j + 75, 30, (float)(i + 51 + 18) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.minecraft.player);
    }

    @Override
    public void render(int x, int y, float p_render_3_) {
        super.render(x, y, p_render_3_);
        this.oldMouseX = x;
        this.oldMouseY = y;
    }

    @SubscribeEvent
    public static void onGuiOpenEvent(GuiOpenEvent event) {
        if (event.getGui() instanceof InventoryScreen) {
            System.out.println("Player inventory opened");
            event.setGui(new TFCRInventoryContainerScreen(Minecraft.getInstance().player));
        }
    }
}
