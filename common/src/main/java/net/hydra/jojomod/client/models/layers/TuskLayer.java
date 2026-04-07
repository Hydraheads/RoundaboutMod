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
                    if (user.roundabout$getStandPowers() instanceof PowersTusk PT && PT.getAct() > 1) {
                        ClientUtil.pushPoseAndCooperate(poseStack,47);

                        if (livent.getMainArm() == HumanoidArm.RIGHT) {
                            getParentModel().rightArm.translateAndRotate(poseStack);
                            poseStack.translate(-0.15,-0.5,0);
                        } else {
                            getParentModel().leftArm.translateAndRotate(poseStack);
                            poseStack.translate(-0.05,-0.5,0);
                        }
                        poseStack.scale(0.8F, 0.8F, 0.8F);
                        boolean isHurt = livent.hurtTime > 0;
                        float r = 1;
                        float g = isHurt ? 0.0F : 1.0F;
                        float b = isHurt ? 0.0F : 1.0F;
                        ModStrayModels.TUSK_DRILL.render(livent, partialTicks, poseStack, bufferSource, packedLight, r, g, b, 1.0F);
                        ClientUtil.popPoseAndCooperate(poseStack,47);
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
