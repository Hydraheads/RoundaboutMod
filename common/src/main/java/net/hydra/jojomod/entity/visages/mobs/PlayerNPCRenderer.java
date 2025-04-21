package net.hydra.jojomod.entity.visages.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.HumanoidLikeArmorLayer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.PlayerLikeRenderer;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PlayerNPCRenderer<T extends JojoNPC> extends PlayerLikeRenderer<JojoNPCPlayer> {
    private static final ResourceLocation STEVE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/steve.png");
    public PlayerNPCRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerNPCModel<>(context.bakeLayer(ModEntityRendererClient.STEVE_LAYER)),0f);
        if (ConfigManager.getClientConfig().renderArmorOnPlayerCloneAbilities) {
            this.addLayer(new HumanoidLikeArmorLayer<>(this, new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        }
    }
    @Override
    public void render(JojoNPCPlayer mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(0.9375f, 0.9375f, 0.9375f);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        if (entity instanceof JojoNPCPlayer jnp && jnp.faker instanceof AbstractClientPlayer AP){
            return ((AbstractClientPlayer) jnp.faker).getSkinTextureLocation();
        }
        else {
            return STEVE_SKIN;
        }
    }
}
