package net.hydra.jojomod.client.models.visages.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.AvdolModel;
import net.hydra.jojomod.entity.visages.mobs.AvdolNPC;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AvdolRenderer<T extends JojoNPC> extends PlayerLikeRenderer<AvdolNPC> {

    private static final ResourceLocation AVDOL_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/avdol.png");
    public AvdolRenderer(EntityRendererProvider.Context context) {
        super(context, new AvdolModel<>(context.bakeLayer(ModEntityRendererClient.AVDOL_LAYER)),0.5F);
    }
    @Override
    public void render(AvdolNPC mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.969F, 0.96F, 0.96F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        return AVDOL_SKIN;
    }
}