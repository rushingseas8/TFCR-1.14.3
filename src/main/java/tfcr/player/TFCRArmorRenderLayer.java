package tfcr.player;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;

public class TFCRArmorRenderLayer extends LayerRenderer {
    public TFCRArmorRenderLayer(IEntityRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(Entity entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
