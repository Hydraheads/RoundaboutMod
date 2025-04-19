package net.hydra.jojomod.entity.visages.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.PlayerLikeRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AyaRenderer<T extends AyaNPC> extends PlayerLikeRenderer<AyaNPC> {

    private static final ResourceLocation AYA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/aya.png");

    public AyaRenderer(EntityRendererProvider.Context context) {
        super(context, new AyaModel<>(context.bakeLayer(ModEntityRendererClient.AYA_LAYER)),0.45F);
    }
    @Override
    public void render(AyaNPC mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.8F, 0.839F, 0.8F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        return AYA_SKIN;
    }
}
