package net.hydra.jojomod.client.models.visages.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.JosukePartEightModel;
import net.hydra.jojomod.entity.visages.mobs.JosukePartEightNPC;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class JosukePartEightRenderer<T extends JojoNPC> extends PlayerLikeRenderer<JosukePartEightNPC> {

    private static final ResourceLocation JOSUKE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/josuke_part_eight.png");
    public JosukePartEightRenderer(EntityRendererProvider.Context context) {
        super(context, new JosukePartEightModel<>(context.bakeLayer(ModEntityRendererClient.JOSUKE_PART_EIGHT_LAYER)),0.5F);
    }
    @Override
    public void render(JosukePartEightNPC mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.937F, 0.937F, 0.937F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        return JOSUKE_SKIN;
    }
}