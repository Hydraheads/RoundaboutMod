package net.hydra.jojomod.client.models.visages.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.DiegoModel;
import net.hydra.jojomod.entity.visages.mobs.DiegoNPC;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DiegoRenderer<T extends JojoNPC> extends PlayerLikeRenderer<DiegoNPC> {

    private static final ResourceLocation DIEGO_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/diego.png");
    public DiegoRenderer(EntityRendererProvider.Context context) {
        super(context, new DiegoModel<>(context.bakeLayer(ModEntityRendererClient.DIEGO_LAYER)),0.5F);
    }
    @Override
    public void render(DiegoNPC mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.915F, 0.915F, 0.915F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        return DIEGO_SKIN;
    }
}
