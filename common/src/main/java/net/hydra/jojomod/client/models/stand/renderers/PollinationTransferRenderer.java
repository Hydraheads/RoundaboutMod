package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.PollinationTransferModel;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PollinationTransferRenderer extends  ManhattanTransferBaseRenderer{

    public  PollinationTransferRenderer(EntityRendererProvider.Context context) {
        super(context, new PollinationTransferModel(context.bakeLayer(ModEntityRendererClient.POLLINATION_TRANSFER_LAYER)), 0f);
    }

    public void render(ManhattanTransferEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(ManhattanTransferEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
