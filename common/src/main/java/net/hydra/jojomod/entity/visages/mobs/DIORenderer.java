package net.hydra.jojomod.entity.visages.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.PlayerLikeRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DIORenderer<T extends JojoNPC> extends PlayerLikeRenderer<DIONPC> {

    private static final ResourceLocation DIO_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/dio.png");
    public DIORenderer(EntityRendererProvider.Context context) {
        super(context, new DIOModel<>(context.bakeLayer(ModEntityRendererClient.DIO_LAYER)),0f);
    }
    @Override
    public void render(DIONPC mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.975F, 0.975F, 0.975F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        return DIO_SKIN;
    }
}
