package net.hydra.jojomod.entity.visages.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.client.HumanoidLikeArmorLayer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.PlayerLikeModel;
import net.hydra.jojomod.entity.visages.PlayerLikeRenderer;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class PlayerModifiedRenderer<T extends JojoNPC> extends PlayerLikeRenderer<JojoNPCPlayer> {
    private static final ResourceLocation STEVE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/visage/steve.png");

    public PlayerModifiedRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModifiedModel<>(context.bakeLayer(ModEntityRendererClient.MODIFIED_LAYER)),0f);
        if (ConfigManager.getClientConfig().renderArmorOnPlayerCloneAbilities) {
            this.addLayer(new HumanoidLikeArmorLayer<>(this, new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        }
    }

    @Override
    public void render(JojoNPCPlayer mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (mobEntity.host instanceof AbstractClientPlayer AP && mobEntity instanceof PlayerModifiedNPC pmn) {
            ItemStack stack = ((IPlayerEntity)AP).roundabout$getMaskSlot();
            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ModificationMaskItem && !pmn.isDisplay){
                pmn.height = stack.getOrCreateTagElement("modifications").getInt("height");
                pmn.width = stack.getOrCreateTagElement("modifications").getInt("width");
                pmn.faceSize = stack.getOrCreateTagElement("modifications").getInt("head");
                pmn.chestType = stack.getOrCreateTagElement("modifications").getInt("chest");
            }

            matrixStack.scale(0.798F + (((float) pmn.width)*0.001F), 0.7F+(((float) pmn.height)*0.001F), 0.798F+(((float) pmn.width)*0.001F));
            super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(JojoNPC entity) {
        if (entity instanceof JojoNPCPlayer jnp && jnp.host instanceof AbstractClientPlayer AP){
            return ((AbstractClientPlayer) jnp.host).getSkinTextureLocation();
        }
        else {
            return STEVE_SKIN;
        }
    }

    @Override
    public void setModelProperties(JojoNPCPlayer $$0) {
        super.setModelProperties($$0);
        if ($$0.host instanceof AbstractClientPlayer AP && $$0 instanceof PlayerModifiedNPC pmn) {
            PlayerLikeModel<JojoNPCPlayer> $$1 = this.getModel();
            float yeah = (float) (0.73F + (pmn.faceSize*0.002));
                    $$1.head.offsetScale(new Vector3f(yeah,yeah,yeah));
        }
    }
}
