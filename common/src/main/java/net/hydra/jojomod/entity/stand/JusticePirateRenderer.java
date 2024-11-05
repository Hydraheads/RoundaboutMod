package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;

public class JusticePirateRenderer<T extends StandEntity> extends JusticeBaseRenderer<JusticeEntity> {
    public JusticePirateRenderer(EntityRendererProvider.Context context) {
        super(context, new JusticeModel<>(context.bakeLayer(ModEntityRendererClient.JUSTICE_PIRATE_LAYER)),0f);
    }

    @Override
    public void render(JusticeEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(JusticeEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}


