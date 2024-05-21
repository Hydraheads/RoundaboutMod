package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class StandModel<T extends StandEntity> extends SinglePartEntityModel<T> {
    private float alpha;
    ModelPart stand;
    ModelPart head;
    ModelPart body;


    public void setHeadRotations(float pitch,float yaw){
        this.head.pitch = pitch;
        this.head.yaw = yaw;
    }public void setBodyRotations(float pitch,float yaw){
        this.body.pitch = pitch;
        this.body.yaw = yaw;
    } public void setStandRotations(float pitch,float yaw){
        this.stand.pitch = pitch;
        this.stand.yaw = yaw;
    }

    @Override
    public ModelPart getPart() {
        return stand;
    }

    public void defaultAnimations(T entity, float animationProgress, float windupLength){
        this.updateAnimation(entity.idleAnimationState, StandAnimations.STAND_IDLE_FLOAT, animationProgress, 1f);
        this.updateAnimation(entity.punchState1, StandAnimations.COMBO1, animationProgress, 1f);
        this.updateAnimation(entity.punchState2, StandAnimations.COMBO2, animationProgress, 1f);
        this.updateAnimation(entity.punchState3, StandAnimations.COMBO3, animationProgress, 1f);
        this.updateAnimation(entity.blockAnimationState, StandAnimations.BLOCK, animationProgress, 1f);
        this.updateAnimation(entity.barrageChargeAnimationState, StandAnimations.BARRAGECHARGE, animationProgress, windupLength);
        this.updateAnimation(entity.barrageAnimationState, StandAnimations.BARRAGE, animationProgress, 2.7f);
        this.updateAnimation(entity.barrageEndAnimationState, StandAnimations.COMBO3, animationProgress, 2.2f);
        this.updateAnimation(entity.barrageHurtAnimationState, StandAnimations.BARRAGEDAMAGE, animationProgress, 2.5f);



    }

    public void defaultModifiers(T entity){
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!mc.isPaused()) {
            float tickDelta = mc.getLastFrameDuration();
            if (entity.getUser() != null) {
                rotateStand(entity, this.getPart(), tickDelta);
                if (this.getPart().hasChild("stand2")) {

                    if (this.getPart().getChild("stand2").hasChild("head")) {
                        rotateHead(entity, this.getPart().getChild("stand2").getChild("head"), tickDelta);
                    }

                    if (this.getPart().getChild("stand2").hasChild("body")) {
                        rotateBody(entity, this.getPart().getChild("stand2").getChild("body"), tickDelta);
                    }

                }
            }
        }
    }
    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
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
            if (mobEntity.isSwimming() || mobEntity.isCrawling() || mobEntity.isFallFlying()) {
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
            rotX = ((mobEntity.getUser().getPitch(tickDelta2)%360) - swimRotCorrect) * MathHelper.RADIANS_PER_DEGREE;
            rotY = (MathHelper.lerpAngleDegrees(tickDelta2, (rotY * MathHelper.DEGREES_PER_RADIAN),
                    ((MathHelper.lerpAngleDegrees(tickDelta2, mobEntity.getUser().prevHeadYaw, mobEntity.getUser().headYaw)%360)
                    - (MathHelper.lerpAngleDegrees(tickDelta2, mobEntity.getUser().prevBodyYaw, mobEntity.getUser().bodyYaw)%360)))
                    ) * MathHelper.RADIANS_PER_DEGREE;

        } else if (animationStyle == OffsetIndex.FIXED_STYLE){
            rotX = 0;
            rotY = (float) -(mobEntity.getPunchYaw(mobEntity.getAnchorPlace(),
                    0.36) * MathHelper.RADIANS_PER_DEGREE);
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
        float cRX = 0;
        float cRY = 0;
        if (animationStyle == OffsetIndex.FIXED_STYLE){
            cRX = (mobEntity.getUser().getPitch(tickDelta)%360) * MathHelper.RADIANS_PER_DEGREE;
        } else if (animationStyle == OffsetIndex.LOOSE_STYLE){
            cRX = (mobEntity.getPitch(tickDelta)%360) * MathHelper.RADIANS_PER_DEGREE;
        }
        rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, cRX, 0.8f);
        rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, cRY, 0.8f);
        mobEntity.setStandRotationX(rotX);
        mobEntity.setStandRotationY(rotY);
        this.setStandRotations(rotX,rotY);

    }
    public void rotateBody(T mobEntity,  ModelPart body, float tickDelta){
        var animationNumber = mobEntity.getOffsetType();
        var animationStyle = OffsetIndex.OffsetStyle(animationNumber);

        float rotX = mobEntity.getBodyRotationX();
        float rotY = mobEntity.getBodyRotationY();
        if (animationStyle == OffsetIndex.FOLLOW_STYLE){
            float cRot = maxRotX;

            if (mobEntity.isSwimming() || mobEntity.isFallFlying()) {
                cRot = ((mobEntity.getUser().getPitch(tickDelta)%360) + 90) * MathHelper.RADIANS_PER_DEGREE;
            } else if (mobEntity.isCrawling()) {
                cRot = 90 * MathHelper.RADIANS_PER_DEGREE;
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
            rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, 0, 0.8f);
            rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, (float) -(mobEntity.getPunchYaw(mobEntity.getAnchorPlace(),
                    0.36) * MathHelper.RADIANS_PER_DEGREE), 0.8f);
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
