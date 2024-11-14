package net.hydra.jojomod.entity.visages.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.PlayerLikeRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PlayerAlexRenderer<T extends JojoNPC> extends PlayerLikeRenderer<JojoNPCPlayer> {
    private static final ResourceLocation ALEX_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/alex.png");
    public PlayerAlexRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerAlexModel<>(context.bakeLayer(ModEntityRendererClient.ALEX_LAYER)),0f);
    }
    @Override
    public void render(JojoNPCPlayer mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(0.9375f, 0.9375f, 0.9375f);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPCPlayer entity) {
        if (entity.faker instanceof AbstractClientPlayer AP){
            return ((AbstractClientPlayer) entity.faker).getSkinTextureLocation();
        }
        else {
            return ALEX_SKIN;
        }
    }
}
