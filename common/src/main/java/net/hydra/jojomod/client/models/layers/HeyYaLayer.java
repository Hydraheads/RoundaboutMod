package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public class HeyYaLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public HeyYaLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            LivingEntity livent = entity;
            if (!entity.isInvisible()) {
                if (entity != null) {
                    if (entity instanceof JojoNPC jnp && jnp.host != null) {
                        livent = jnp.host;
                    }
                    StandUser user = ((StandUser) livent);
                    if (user.roundabout$getStandPowers() instanceof PowersHeyYa PH) {
                        poseStack.pushPose();

                        // Translate to the right/left hand
                            getParentModel().body.translateAndRotate(poseStack); // Use leftArm for off-hand
                            // Apply additional transformations
                            poseStack.translate(-0.27F, -0.25, 0.19F); //1 1
                            // The first value goes to the right (negative) and left (positive)
                            // The second value is correlated with up (negative) and down (positive)
                        // the third is further and closer, positive to head towards bakc negative for away
                        // Render your model here
                        poseStack.scale(0.6F, 0.6F, 0.6F);
                        boolean isHurt = livent.hurtTime > 0;
                        float r = isHurt ? 1.0F : 1.0F;
                        float g = isHurt ? 0.0F : 1.0F;
                        float b = isHurt ? 0.0F : 1.0F;
                        ModStrayModels.HEY_YA.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1F);
                        poseStack.popPose();
                    }
                }
            }
        }
    }
}

