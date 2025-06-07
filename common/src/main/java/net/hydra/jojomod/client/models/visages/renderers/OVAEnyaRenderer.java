package net.hydra.jojomod.client.models.visages.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.OVAEnyaModel;
import net.hydra.jojomod.entity.visages.mobs.OVAEnyaNPC;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OVAEnyaRenderer<T extends JojoNPC> extends PlayerLikeRenderer<OVAEnyaNPC> {

    private static final ResourceLocation OVA_ENYA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/enya_ova.png");
    public OVAEnyaRenderer(EntityRendererProvider.Context context) {
        super(context, new OVAEnyaModel<>(context.bakeLayer(ModEntityRendererClient.OVA_ENYA_LAYER)),0.47f);
    }
    @Override
    public void render(OVAEnyaNPC mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.84f, 0.87f, 0.84f);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        return OVA_ENYA_SKIN;
    }
}
