package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;

public class DarkMirageRenderer<T extends StandEntity> extends JusticeBaseRenderer<JusticeEntity> {
    public DarkMirageRenderer(EntityRendererProvider.Context context) {
        super(context, new DarkMirageModel<>(context.bakeLayer(ModEntityRendererClient.DARK_MIRAGE_LAYER)),0f);
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