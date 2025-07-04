package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
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

public class ShootingArmLayer <T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public ShootingArmLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/large_bubble.png");
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            LivingEntity livent = entity;
            if (!entity.isInvisible()) {
                if (entity!= null) {
                    StandUser user = ((StandUser)livent);
                    if (user.roundabout$getCombatMode()) {
                        if (user.roundabout$getStandPowers() instanceof PowersSoftAndWet PW) {
                            poseStack.pushPose();

                            // Translate to the right/left hand

                            if (livent.getMainArm() == HumanoidArm.RIGHT) {
                                getParentModel().rightArm.translateAndRotate(poseStack); // Use leftArm for off-hand
                                // Apply additional transformations
                                poseStack.translate(-0.05F, 0.83, 0F); //1 1
                                // The third value pushes it up (negative)

                            } else {
                                getParentModel().leftArm.translateAndRotate(poseStack); // Use leftArm for off-hand
                                // Apply additional transformations
                                poseStack.translate(0.05F, 0.83, 0F);
                            }
                            // Render your model here
                            poseStack.scale(0.8F, 0.8F, 0.8F);
                            boolean isHurt = livent.hurtTime > 0;
                            float r = isHurt ? 1.0F : 1.0F;
                            float g = isHurt ? 0.0F : 1.0F;
                            float b = isHurt ? 0.0F : 1.0F;
                            ModStrayModels.SHOOTING_ARM.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 0.8F);
                            poseStack.popPose();
                        }
                    }
                }
            }
        }
    }

    public static void renderOutOfContext(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float rr, float gg, float bb, float partialTicks, float var9, float var10,
                                          ModelPart handarm) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            if (!entity.isInvisible()) {
                if (entity!= null) {
                    StandUser user = ((StandUser)entity);
                    if (user.roundabout$getCombatMode()) {
                        if (user.roundabout$getStandPowers() instanceof PowersSoftAndWet PW) {
                            poseStack.pushPose();

                            // Translate to the right/left hand

                            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                                // Apply additional transformations
                                handarm.translateAndRotate(poseStack); // Use leftArm for off-hand
                                poseStack.translate(-0.05F, 0.83, 0F); //1 1
                                // The third value pushes it up (negative)

                            } else {
                                // Apply additional transformations
                                handarm.translateAndRotate(poseStack); // Use leftArm for off-hand
                                poseStack.translate(0.05F, 0.83, 0F);
                            }
                            // Render your model here
                            poseStack.scale(1.0F, 1.0F, 1.0F);
                            boolean isHurt = entity.hurtTime > 0;
                            float r = isHurt ? 1.0F : 1.0F;
                            float g = isHurt ? 0.0F : 1.0F;
                            float b = isHurt ? 0.0F : 1.0F;
                            ModStrayModels.SHOOTING_ARM.render(entity, partialTicks, poseStack, bufferSource, packedLight, rr, gg, bb, 0.8F);
                            poseStack.popPose();
                        }
                    }
                }
            }
        }
    }
}
