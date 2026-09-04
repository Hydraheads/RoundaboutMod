package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.BeachSabbathModel;
import net.hydra.jojomod.client.models.stand.BlackSantaSabbathModel;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.BlackSantaSabbathEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;

public class BlackSantaSabbathRenderer extends BlackSabbathBaseRenderer {

    public BlackSantaSabbathRenderer(EntityRendererProvider.Context context) {
        super(context, new BlackSantaSabbathModel(context.bakeLayer(ModEntityRendererClient.SANTA_SABBATH_LAYER)), 0f);
    }

    @Override
    public void render(BlackSabbathEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(BlackSabbathEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
