package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.BeachSabbathModel;
import net.hydra.jojomod.client.models.stand.BlackSabbathModel;
import net.hydra.jojomod.entity.stand.BeachSabbathEntity;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;

public class BeachSabbathRenderer extends BlackSabbathBaseRenderer {

    public BeachSabbathRenderer(EntityRendererProvider.Context context) {
        super(context, new BeachSabbathModel(context.bakeLayer(ModEntityRendererClient.BEACH_SABBATH_LAYER)), 0f);
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
