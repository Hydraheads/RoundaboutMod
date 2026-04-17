package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.hydra.jojomod.stand.powers.PowersTusk;
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
import org.joml.Quaternionf;

import java.util.Map;

public class TuskLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    public TuskLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
    }
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer()) && !entity.isUsingItem()) {
            LivingEntity livent = entity;
            if (!entity.isInvisible()) {
                StandUser user = ((StandUser) livent);
                if (PowerTypes.isUsingStand(entity)) {
                    if (user.roundabout$getStandPowers() instanceof PowersTusk PT) {
                        boolean isHurt = livent.hurtTime > 0;
                        float r = 1;
                        float g = isHurt ? 0.0F : 1.0F;
                        float b = isHurt ? 0.0F : 1.0F;

                        poseStack.pushPose(); // RIGHT NAIL
                        getParentModel().rightArm.translateAndRotate(poseStack);
                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 0, 90), 0, 0, 0);
                        poseStack.translate(-0.125, -0.145, -0.6); // +X LEFT, -Y UP, -Z FORWARD
                        poseStack.scale(1, 1, 1);
                        for (int i = 0; i < 4; i++) {
                            if (PT.getMaxActiveNails() > 5+i) {
                                ModStrayModels.TUSK_NAIL.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1.0F, 4 - i);
                            }
                            poseStack.translate(0.055,0,0);
                        }
                        if (PT.getMaxActiveNails() > 9) {
                            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1, 90), 0, 0, 0);
                            poseStack.translate(0.065, 0, 0);
                            ModStrayModels.TUSK_NAIL.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1.0F, 0);
                        }
                        poseStack.popPose();

                        poseStack.pushPose(); // LEFT NAIL
                        getParentModel().leftArm.translateAndRotate(poseStack);
                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 0, 90), 0, 0, 0);
                        poseStack.translate(0.13, -0.145, -0.6); // +X LEFT, -Y UP, -Z FORWARD
                        poseStack.scale(1, 1, 1);
                        for (int i = 0; i < 4; i++) {
                            if (PT.getMaxActiveNails() > i) {
                                ModStrayModels.TUSK_NAIL.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1.0F, 4 - i);
                            }
                            poseStack.translate(-0.055, 0, 0);
                        }
                        if (PT.getMaxActiveNails() > 5) {
                            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1, 90), 0, 0, 0);
                            poseStack.translate(0.065, 0, 0);
                            ModStrayModels.TUSK_NAIL.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1.0F, 0);
                        }
                        poseStack.popPose();



                        if (PT.getAct() > 1) {
                            poseStack.pushPose(); // DRILL
                            if (livent.getMainArm() == HumanoidArm.RIGHT) {
                                getParentModel().rightArm.translateAndRotate(poseStack);
                                poseStack.translate(-0.15, -0.5, 0);
                            } else {
                                getParentModel().leftArm.translateAndRotate(poseStack);
                                poseStack.translate(-0.05, -0.5, 0);
                            }
                            poseStack.scale(0.80F, 0.8F, 0.8F);
                            ModStrayModels.TUSK_DRILL.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1.0F);
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
                StandUser user = ((StandUser) entity);
                if (PowerTypes.hasStandActivelyEquipped(entity)) {
                    if (user.roundabout$getStandPowers() instanceof PowersTusk PT) {
                        ClientUtil.pushPoseAndCooperate(poseStack,48);


                        if (entity.getMainArm() == HumanoidArm.RIGHT) {
                            handarm.translateAndRotate(poseStack);
                            poseStack.translate(-0.1F, 0.83, 0.02F);

                        } else {
                            handarm.translateAndRotate(poseStack);
                            poseStack.translate(0.1F, 0.83, -0.02F);
                        }
                        // Render your model here
                        poseStack.scale(1.0F, 1.0F, 1.0F);
                        boolean isHurt = entity.hurtTime > 0;
                        float r = 1.0F;
                        float g = isHurt ? 0.0F : 1.0F;
                        float b = isHurt ? 0.0F : 1.0F;
                        ModStrayModels.TUSK_DRILL.render(entity, partialTicks, poseStack, bufferSource, packedLight, rr, gg, bb, 1.0F);
                        ClientUtil.popPoseAndCooperate(poseStack,48);
                    }
                }
            }
        }
    }
}
