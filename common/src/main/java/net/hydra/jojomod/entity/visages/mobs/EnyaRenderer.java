package net.hydra.jojomod.entity.visages.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.PlayerLikeRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EnyaRenderer<T extends EnyaNPC> extends PlayerLikeRenderer<EnyaNPC> {

    private static final ResourceLocation ENYA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/enya.png");

    public EnyaRenderer(EntityRendererProvider.Context context) {
        super(context, new EnyaModel<>(context.bakeLayer(ModEntityRendererClient.ENYA_LAYER)),0.4F);
    }
    @Override
    public void render(EnyaNPC mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.6975F, 0.6975F, 0.6975F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        return ENYA_SKIN;
    }
}
