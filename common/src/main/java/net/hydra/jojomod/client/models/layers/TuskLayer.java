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
            if (!entity.isInvisible()) {
                StandUser user = ((StandUser) entity);
                if (PowerTypes.isUsingStand(entity)) {
                    if (user.roundabout$getStandPowers() instanceof PowersTusk PT) {
                        boolean rh = entity.getMainArm() == HumanoidArm.RIGHT;
                        boolean isHurt = entity.hurtTime > 0;
                        float r = 1;
                        float g = isHurt ? 0.0F : 1.0F;
                        float b = isHurt ? 0.0F : 1.0F;

                        poseStack.pushPose(); // RIGHT NAIL
                        getParentModel().rightArm.translateAndRotate(poseStack);
                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 0, 90), 0, 0, 0);
                        poseStack.translate(-0.125, -0.145, -0.6); // +X LEFT, -Y UP, -Z FORWARD
                        poseStack.scale(1, 1, 1);
                        for (int i = 0; i < 4; i++) {
                            if (PT.getRightHandNails() > i) {
                                ModStrayModels.TUSK_NAIL.tuskNail(entity, partialTicks, poseStack, bufferSource, packedLight, 4 - i, rh ? i+1 : 5+i);
                            }
                            poseStack.translate(0.055,0,0);
                        }
                        if (PT.getRightHandNails() >= 5) {
                            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1, 90), 0, 0, 0);
                            poseStack.translate(0.065, 0, 0);
                            ModStrayModels.TUSK_NAIL.tuskNail(entity, partialTicks, poseStack, bufferSource, packedLight, 0,rh ? 5 : 10);
                        }
                        poseStack.popPose();

                        poseStack.pushPose(); // LEFT NAIL
                        getParentModel().leftArm.translateAndRotate(poseStack);
                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 0, 90), 0, 0, 0);
                        poseStack.translate(0.13, -0.145, -0.6); // +X LEFT, -Y UP, -Z FORWARD
                        poseStack.scale(1, 1, 1);
                        for (int i = 0; i < 4; i++) {
                            if (PT.getLeftHandNails() > i) {
                                ModStrayModels.TUSK_NAIL.tuskNail(entity, partialTicks, poseStack, bufferSource, packedLight, 4-i,rh ? 5+i : i+1);
                            }
                            poseStack.translate(-0.055, 0, 0);
                        }
                        if  (PT.getLeftHandNails() >= 5) {
                            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1, 90), 0, 0, 0);
                            poseStack.translate(0.065, 0, 0);
                            ModStrayModels.TUSK_NAIL.tuskNail(entity, partialTicks, poseStack, bufferSource, packedLight, 0,rh ? 10 : 5);
                        }
                        poseStack.popPose();



                       /* if (PT.renderDrill()) {
                            if (PT.renderBothArms() || entity.getMainArm() == HumanoidArm.RIGHT) {
                                poseStack.pushPose(); // DRILL
                                getParentModel().rightArm.translateAndRotate(poseStack);
                                poseStack.translate(-0.15, -0.5, 0);
                                poseStack.scale(0.80F, 0.8F, 0.8F);
                                ModStrayModels.TUSK_DRILL.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1.0F);
                                poseStack.popPose();
                            }
                            if (PT.renderBothArms() || entity.getMainArm() == HumanoidArm.LEFT) {
                                poseStack.pushPose();
                                getParentModel().leftArm.translateAndRotate(poseStack);
                                poseStack.translate(-0.05, -0.5, 0);
                                poseStack.scale(0.80F, 0.8F, 0.8F);
                                ModStrayModels.TUSK_DRILL.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1.0F);
                                poseStack.popPose();
                            }
                        } */
                    }
                }
            }
        }
    }

}
