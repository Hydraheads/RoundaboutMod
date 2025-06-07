package net.hydra.jojomod.client.models.visages.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.ValentineModel;
import net.hydra.jojomod.entity.visages.mobs.ValentineNPC;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ValentineRenderer<T extends JojoNPC> extends PlayerLikeRenderer<ValentineNPC> {

    private static final ResourceLocation VALENTINE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/valentine.png");
    public ValentineRenderer(EntityRendererProvider.Context context) {
        super(context, new ValentineModel<>(context.bakeLayer(ModEntityRendererClient.VALENTINE_LAYER)),0.5F);
    }
    @Override
    public void render(ValentineNPC mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.939F, 0.939F, 0.939F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        return VALENTINE_SKIN;
    }
}
