package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class StandModel<T extends StandEntity> extends HierarchicalModel<T> {
    /**Override this for every stand model.*/
    private float alpha;
    ModelPart stand;
    ModelPart head;
    ModelPart body;

    public void setHeadRotations(float pitch,float yaw){
        this.head.xRot = pitch;
        this.head.yRot = yaw;
    }public void setBodyRotations(float pitch,float yaw){
        this.body.xRot = pitch;
        this.body.yRot = yaw;
    } public void setStandRotations(float pitch,float yaw, float z){
        this.stand.xRot = pitch;
        this.stand.yRot = yaw;
        this.stand.zRot = z;
    }

    @Override
    public ModelPart root() {
        return stand;
    }

    /**Most humanoid stands share these animations.*/
    public void defaultAnimations(T entity, float animationProgress, float windupLength){
        this.animate(entity.idleAnimationState, StandAnimations.STAND_IDLE_FLOAT, animationProgress, 1f);
        this.animate(entity.punchState1, StandAnimations.COMBO1, animationProgress, 1.4f);
        this.animate(entity.punchState2, StandAnimations.COMBO2, animationProgress, 1.16666f); /*1.1666 for 6 ticks, 1.4 for 5*/
        this.animate(entity.punchState3, StandAnimations.COMBO3, animationProgress, 1.16666f);
        this.animate(entity.blockAnimationState, StandAnimations.BLOCK, animationProgress, 1f);
        this.animate(entity.barrageChargeAnimationState, StandAnimations.BARRAGECHARGE, animationProgress, windupLength);
        this.animate(entity.barrageAnimationState, StandAnimations.BARRAGE, animationProgress, 2.7f);
        this.animate(entity.miningBarrageAnimationState, StandAnimations.BARRAGE, animationProgress, 1.65f);
        this.animate(entity.barrageEndAnimationState, StandAnimations.COMBO3, animationProgress, 2.2f);
        this.animate(entity.barrageHurtAnimationState, StandAnimations.BARRAGEDAMAGE, animationProgress, 2.5f);
        this.animate(entity.brokenBlockAnimationState, StandAnimations.BLOCKBREAK, animationProgress, 1.8f);
    }

    /**If a stand has a head or a body, it pretty much could benefit from having
     * defaultmodifiers, because that makes its head and body rotate along
     * with the stand user.*/
    public void defaultModifiers(T entity){
        Minecraft mc = Minecraft.getInstance();
        if (entity.getUser() != null) {
            LivingEntity User = entity.getUser();
            if (!mc.isPaused() && !(((TimeStop) entity.level()).CanTimeStopEntity(User))) {
                float tickDelta = mc.getDeltaFrameTime();
                rotateStand(entity, this.root(), tickDelta);
                if (this.root().hasChild("stand2")) {

                    if (this.root().getChild("stand2").hasChild("head")) {
                        rotateHead(entity, this.root().getChild("stand2").getChild("head"), tickDelta);
                    }

                    if (this.root().getChild("stand2").hasChild("body")) {
                        rotateBody(entity, this.root().getChild("stand2").getChild("body"), tickDelta);
                    }

                }
            } else {
                    float rotX = entity.getStandRotationX();
                    float rotY = entity.getStandRotationY();
                    float rotZ = entity.getStandRotationZ();
                    this.setStandRotations(rotX, rotY, rotZ);
                    if (this.root().hasChild("stand2")) {

                        if (this.root().getChild("stand2").hasChild("head")) {
                            rotX = entity.getHeadRotationX();
                            rotY = entity.getHeadRotationY();
                            this.setHeadRotations(rotX, rotY);
                        }

                        if (this.root().getChild("stand2").hasChild("body")) {
                            rotX = entity.getBodyRotationX();
                            rotY = entity.getBodyRotationY();
                            this.setBodyRotations(rotX, rotY);
                        }

                    }
            }
        }
    }
    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }
    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        stand.render(matrices, vertexConsumer, light, overlay, red, green, blue, this.alpha);
    }

    public void setAlpha(float alpha){
        this.alpha = alpha;
    }

    public void rotateHead(T mobEntity,  ModelPart head, float tickDelta){

        var animationNumber = mobEntity.getOffsetType();
        var animationStyle = OffsetIndex.OffsetStyle(animationNumber);
        float rotX = mobEntity.getHeadRotationX();
        float rotY = mobEntity.getHeadRotationY();
        if (animationStyle == OffsetIndex.FOLLOW_STYLE){
            /*This code makes the head of the model turn towards swim rotation while swimming*/
            if (mobEntity.isSwimming() || mobEntity.isVisuallyCrawling() || mobEntity.isFallFlying()) {
                if (swimRotCorrect > -45) {
                    swimRotCorrect -= 2;
                    swimRotCorrect = Math.min(swimRotCorrect, -45);
                }
            } else {
                if (swimRotCorrect < 0) {
                    swimRotCorrect += 2;
                    swimRotCorrect = Math.max(swimRotCorrect, 0);
                }
            }
            float tickDelta2 = Math.min(tickDelta,2);
            rotX = ((mobEntity.getUser().getViewXRot(tickDelta2)%360) - swimRotCorrect) * Mth.DEG_TO_RAD;
            rotY = 0;

        } else if (animationStyle == OffsetIndex.FIXED_STYLE){
            rotX = 0;
            rotY = 0;
        } else if (animationStyle == OffsetIndex.LOOSE_STYLE){
            rotX = 0;
            rotY = 0;
        }
        mobEntity.setHeadRotationX(rotX);
        mobEntity.setHeadRotationY(rotY);
        this.setHeadRotations(rotX,rotY);
    } public void rotateStand(T mobEntity,  ModelPart stand, float tickDelta){
        var animationNumber = mobEntity.getOffsetType();
        var animationStyle = OffsetIndex.OffsetStyle(animationNumber);

        float rotX = mobEntity.getStandRotationX();
        float rotY = mobEntity.getStandRotationY();
        float rotZ = mobEntity.getStandRotationZ();
        float cRX = 0;
        float cRY = 0;
        float cRZ = 0;
        if (animationStyle == OffsetIndex.FIXED_STYLE){
            cRX = (mobEntity.getUser().getViewXRot(tickDelta)%360) * Mth.DEG_TO_RAD;

            if (animationNumber == OffsetIndex.BENEATH){
                cRX = 90 * Mth.DEG_TO_RAD;
                cRZ = 180 * Mth.DEG_TO_RAD;
            }
        } else if (animationStyle == OffsetIndex.LOOSE_STYLE){
            cRX = (mobEntity.getViewXRot(tickDelta)%360) * Mth.DEG_TO_RAD;
        }
        rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, cRX, 0.8f);
        rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, cRY, 0.8f);
        rotZ = MainUtil.controlledLerpRadianDegrees(tickDelta, rotZ, cRZ, 0.8f);
        mobEntity.setStandRotationX(rotX);
        mobEntity.setStandRotationY(rotY);
        mobEntity.setStandRotationZ(rotZ);
        this.setStandRotations(rotX,rotY,rotZ);

    }
    public void rotateBody(T mobEntity,  ModelPart body, float tickDelta){
        var animationNumber = mobEntity.getOffsetType();
        var animationStyle = OffsetIndex.OffsetStyle(animationNumber);

        float rotX = mobEntity.getBodyRotationX();
        float rotY = mobEntity.getBodyRotationY();
        if (animationStyle == OffsetIndex.FOLLOW_STYLE){
            float cRot = maxRotX;

            if (mobEntity.isSwimming() || mobEntity.isFallFlying()) {
                cRot = ((mobEntity.getUser().getViewXRot(tickDelta)%360) + 90) * Mth.DEG_TO_RAD;
            } else if (mobEntity.isVisuallyCrawling()) {
                cRot = 90 * Mth.DEG_TO_RAD;
            } else {
                int moveForward = mobEntity.getMoveForward();
                if (moveForward < 0) {
                    cRot *= -moveForward;
                } else if (moveForward > 0) {
                    cRot *= -moveForward;
                } else {
                    cRot = 0;
                }
                cRot *= -0.6F;
            }
            rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, cRot, 0.15f);
            rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, 0, 0.8f);
        } else if (animationStyle == OffsetIndex.FIXED_STYLE) {
            float xRot = 0;
            float yRot = 0;
            rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, xRot, 0.8f);
            rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, yRot, 0.8f);
        } else if (animationStyle == OffsetIndex.LOOSE_STYLE) {
            rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, 0, 0.8f);
            rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, 0, 0.8f);

        }
        mobEntity.setBodyRotationX(rotX);
        mobEntity.setBodyRotationY(rotY);
        this.setBodyRotations(rotX,rotY);
    }


    private final float maxRotX = 0.25F;
    private final float minRotX = 0.04F;
    private float swimRotCorrect = 0.0F;

}
