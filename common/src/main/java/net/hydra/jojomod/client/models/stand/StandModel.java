package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class StandModel<T extends StandEntity> extends HierarchicalModel<T> {
    /**Override this for every stand model.*/
    private float alpha;
    public ModelPart stand;
    public ModelPart head;
    public ModelPart body;
    public ModelPart leftHand;
    public ModelPart rightHand;

    public ModelPart getHead(){
        return this.head;
    }
    public ModelPart getBody(){
        return this.head;
    }
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

    protected ModelPart getArm(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.rightHand : this.leftHand;
    }

    public void translateToHand(HumanoidArm p_103778_, PoseStack p_103779_, float zshift, float yshift, float xshift) {
        float f = p_103778_ == HumanoidArm.RIGHT ? xshift : -xshift;

        ModelPart modelpart = this.getArm(p_103778_);
        modelpart.x += f;
        modelpart.y -= yshift;
        modelpart.z += zshift;
        modelpart.translateAndRotate(p_103779_);
        modelpart.x -= f;
        modelpart.y += yshift;
        modelpart.z -= zshift;
    }

    @Override
    public ModelPart root() {
        return stand;
    }

    /**Most humanoid stands share these animations.*/
    public void defaultAnimations(T entity, float animationProgress, float windupLength){
        this.animate(entity.idleAnimationState, StandAnimations.STAND_IDLE_FLOAT, animationProgress, 1f);
        this.animate(entity.idleAnimationState2, StandAnimations.IDLE_2, animationProgress, 1f);
        this.animate(entity.idleAnimationState3, StandAnimations.FLOATY_IDLE, animationProgress, 1f);
        this.animate(entity.idleAnimationState4, StandAnimations.STAR_PLATINUM_IDLE, animationProgress, 1f);
        this.animate(entity.armlessAnimation, StandAnimations.ArmIdle, animationProgress, 1f);
        this.animate(entity.armlessAnimationIdle, StandAnimations.ArmIdle2, animationProgress, 1f);
        this.animate(entity.punchState1, StandAnimations.COMBO1, animationProgress, 1.4f);
        this.animate(entity.punchState2, StandAnimations.COMBO2, animationProgress, 1.16666f); /*1.1666 for 6 ticks, 1.4 for 5*/
        this.animate(entity.punchState3, StandAnimations.COMBO3, animationProgress, 1.16666f);
        this.animate(entity.blockAnimationState, StandAnimations.BLOCK, animationProgress, 1f);
        this.animate(entity.barrageChargeAnimationState, StandAnimations.BARRAGECHARGE, animationProgress, windupLength);
        this.animate(entity.barrageAnimationState, StandAnimations.BARRAGE, animationProgress, 1f);
        this.animate(entity.miningBarrageAnimationState, StandAnimations.MINING_BARRAGE, animationProgress, 1.65f);
        this.animate(entity.barrageEndAnimationState, StandAnimations.COMBO3, animationProgress, 2.2f);
        this.animate(entity.barrageHurtAnimationState, StandAnimations.BARRAGEDAMAGE, animationProgress, 2.5f);
        this.animate(entity.brokenBlockAnimationState, StandAnimations.BLOCKBREAK, animationProgress, 1.8f);
        this.animate(entity.standLeapAnimationState, StandAnimations.STAND_LEAP, animationProgress, 1f);
        this.animate(entity.standLeapEndAnimationState, StandAnimations.STAND_LEAP_END, animationProgress, 3.0f);
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
                        if (User != null){
                            if (User.isBaby()){
                                ModelPart mp = this.root().getChild("stand2").getChild("head");
                                mp.xScale*= 1.5F;
                                mp.yScale*= 1.5F;
                                mp.zScale*= 1.5F;
                            }
                        }
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
        if (mobEntity instanceof FollowingStandEntity FSE) {
            if (mobEntity.getDisplay()) {
                this.setHeadRotations(0, 0);
                return;
            }

            var animationNumber = FSE.getOffsetType();
            var animationStyle = OffsetIndex.OffsetStyle(animationNumber);
            float rotX = mobEntity.getHeadRotationX();
            float rotY = mobEntity.getHeadRotationY();
            if (animationStyle == OffsetIndex.FOLLOW_STYLE) {
                /*This code makes the head of the model turn towards swim rotation while swimming*/
                if ((mobEntity.isSwimming() || mobEntity.isVisuallyCrawling() || mobEntity.isFallFlying()) && animationNumber != OffsetIndex.FOLLOW_NOLEAN) {
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
                float tickDelta2 = Math.min(tickDelta, 2);
                float r = mobEntity.getUser().getViewXRot(tickDelta2);
                // SAVING THIS FOR LATER
         /*       if (mobEntity.getUser() != null) {
                    StandUser SU = (StandUser) mobEntity.getUser();
                    if (SU.roundabout$getPossessor() != null && SU.roundabout$getPossessor().getTarget() != null) {
                        r = MainUtil.getLookAtEntityPitch(mobEntity.getUser(),SU.roundabout$getPossessor().getTarget());
                    }
                } */
                rotX = ((r % 360) - swimRotCorrect) * Mth.DEG_TO_RAD;
                rotY = 0;

            } else if (animationStyle == OffsetIndex.FIXED_STYLE) {
                rotX = 0;
                rotY = 0;
            } else if (animationStyle == OffsetIndex.LOOSE_STYLE) {
                rotX = 0;
                rotY = 0;
            }
            mobEntity.setHeadRotationX(rotX);
            mobEntity.setHeadRotationY(rotY);
            this.setHeadRotations(rotX, rotY);
        }
    } public void rotateStand(T mobEntity,  ModelPart stand, float tickDelta){
        if (mobEntity instanceof FollowingStandEntity FSE) {
            if (mobEntity.getDisplay()) {
                this.setStandRotations(0, 0, 0);
                return;
            }
            var animationNumber = FSE.getOffsetType();
            var animationStyle = OffsetIndex.OffsetStyle(animationNumber);

            float rotX = mobEntity.getStandRotationX();
            float rotY = mobEntity.getStandRotationY();
            float rotZ = mobEntity.getStandRotationZ();
            float cRX = 0;
            float cRY = 0;
            float cRZ = 0;
            if (animationNumber == OffsetIndex.FOLLOW) {
                cRY = FSE.getIdleRotation() * Mth.DEG_TO_RAD;
            }
            if (animationStyle == OffsetIndex.FIXED_STYLE) {
                cRX = (mobEntity.getUser().getViewXRot(tickDelta) % 360) * Mth.DEG_TO_RAD;

                if (animationNumber == OffsetIndex.BENEATH) {
                    cRX = 90 * Mth.DEG_TO_RAD;
                    cRZ = 180 * Mth.DEG_TO_RAD;
                }
            } else if (animationStyle == OffsetIndex.LOOSE_STYLE || animationNumber == OffsetIndex.GUARD_AND_TRACE) {
                cRX = (mobEntity.getViewXRot(tickDelta) % 360) * Mth.DEG_TO_RAD;
            }
            rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, cRX, 0.8f);
            rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, cRY, 0.8f);
            rotZ = MainUtil.controlledLerpRadianDegrees(tickDelta, rotZ, cRZ, 0.8f);
            mobEntity.setStandRotationX(rotX);
            mobEntity.setStandRotationY(rotY);
            mobEntity.setStandRotationZ(rotZ);
            this.setStandRotations(rotX, rotY, rotZ);
        } else if (mobEntity.forceVisualRotation()) {

            if (mobEntity.getDisplay()) {
                this.setStandRotations(0, 0, 0);
                return;
            }

            float rotX = mobEntity.getStandRotationX();
            float rotY = mobEntity.getStandRotationY();
            float rotZ = mobEntity.getStandRotationZ();
            float cRX = 0;
            float cRY = 0;
            float cRZ = 0;
            cRX = (mobEntity.getViewXRot(tickDelta) % 360) * Mth.DEG_TO_RAD;
            cRY = (mobEntity.getYRot() % 360) * Mth.DEG_TO_RAD;
            rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, cRX, 0.8f);
            rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, cRY, 0.8f);
            rotZ = MainUtil.controlledLerpRadianDegrees(tickDelta, rotZ, cRZ, 0.8f);
            mobEntity.setStandRotationX(rotX);
            mobEntity.setStandRotationY(rotY);
            mobEntity.setStandRotationZ(rotZ);
            this.setStandRotations(rotX, rotY, rotZ);
        }
    }
    public void rotateBody(T mobEntity,  ModelPart body, float tickDelta){
        if (mobEntity instanceof FollowingStandEntity FSE) {
            if (mobEntity.getDisplay()) {
                this.setBodyRotations(0, 0);
                return;
            }
            var animationNumber = FSE.getOffsetType();
            var animationStyle = OffsetIndex.OffsetStyle(animationNumber);

            float rotX = mobEntity.getBodyRotationX();
            float rotY = mobEntity.getBodyRotationY();
            if (animationStyle == OffsetIndex.FOLLOW_STYLE) {
                float cRot = maxRotX;

                if ((mobEntity.isSwimming() || mobEntity.isFallFlying()) && animationNumber != OffsetIndex.FOLLOW_NOLEAN) {
                    cRot = ((mobEntity.getUser().getViewXRot(tickDelta) % 360) + 90) * Mth.DEG_TO_RAD;
                } else if (mobEntity.isVisuallyCrawling() && animationNumber != OffsetIndex.FOLLOW_NOLEAN) {
                    cRot = 90 * Mth.DEG_TO_RAD;
                } else {
                    int moveForward = FSE.getMoveForward();
                    if (animationNumber == OffsetIndex.FOLLOW_NOLEAN) {
                        moveForward = 0;
                    }
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
            this.setBodyRotations(rotX, rotY);
        } else if (mobEntity.forceVisualRotation()) {
            if (mobEntity.getDisplay()) {
                this.setBodyRotations(0, 0);
                return;
            }

            float rotX = mobEntity.getBodyRotationX();
            float rotY = mobEntity.getBodyRotationY();
                rotX = MainUtil.controlledLerpRadianDegrees(tickDelta, rotX, 0, 0.8f);
                rotY = MainUtil.controlledLerpRadianDegrees(tickDelta, rotY, 0, 0.8f);

            mobEntity.setBodyRotationX(rotX);
            mobEntity.setBodyRotationY(rotY);
            this.setBodyRotations(rotX, rotY);
        }
    }


    private final float maxRotX = 0.25F;
    private final float minRotX = 0.04F;
    private float swimRotCorrect = 0.0F;

}
