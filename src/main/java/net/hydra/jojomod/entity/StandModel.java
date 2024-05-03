package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.util.RenderUtils;

public class StandModel extends GeoModel<StandEntity> {
    private final boolean turnsHead = true;
    private final float maxRotX = 0.25F;
    private final float minRotX = 0.04F;
    private float swimRotCorrect = 0.0F;
    @Override
    public Identifier getModelResource(StandEntity animatable) {
        return new Identifier(RoundaboutMod.MOD_ID,"geo/the_world.json");
    }

    @Override
    public Identifier getTextureResource(StandEntity animatable) {
        return new Identifier(RoundaboutMod.MOD_ID,"textures/stand/the_world.png");
    }

    @Override
    public Identifier getAnimationResource(StandEntity animatable) {
        return new Identifier(RoundaboutMod.MOD_ID,"animations/the_world.json");
    }


    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        return start + (delta * (end - start))*multiplier;
    }

    /** Sets the pose of a stand when it's in idle animation. This sets pose of ALL stand models ever in existence.
     * So basically, it needs to be reset when iterating through models to render. Necessary to show movement.*/
    @Override
    public void setCustomAnimations(StandEntity animatable, long instanceId, AnimationState<StandEntity> animationState) {
        if (!this.turnsHead)
            return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!mc.isPaused() || animatable.shouldPlayAnimsWhileGamePaused()){
            //When Game is Paused, don't procede
            float tickDelta = mc.getLastFrameDuration();
            var animationNumber = animatable.getOffsetType();
            var animationStyle = OffsetIndex.OffsetStyle(animationNumber);
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            CoreGeoBone stand = getAnimationProcessor().getBone("stand");
            CoreGeoBone body = getAnimationProcessor().getBone("body");
            CoreGeoBone head = getAnimationProcessor().getBone("head");
            float rotation1 = 0;
            if (animationStyle == OffsetIndex.FIXED_STYLE){
                rotation1 = (float) (animatable.getPunchYaw(animatable.getAnchorPlace(),
                        0.36) * MathHelper.RADIANS_PER_DEGREE);
            }

            if (stand != null){
                float rotX = animatable.getStandRotationX();
                float rotY = animatable.getStandRotationY();
                float cRX = 0;
                float cRY = 0;
                if (animationStyle == OffsetIndex.FIXED_STYLE){
                    cRX = entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE;
                }
                rotX = MainUtil.controlledLerp(tickDelta, rotX, cRX, 0.8f);
                rotY = MainUtil.controlledLerp(tickDelta, rotY, cRY, 0.8f);
                animatable.setStandRotationX(rotX);
                animatable.setStandRotationY(rotY);
                stand.setRotX(rotX);
                stand.setRotY(rotY);
            }

            if (body != null) {
                float rotX = animatable.getBodyRotationX();
                float rotY = animatable.getBodyRotationY();
                if (animationStyle == OffsetIndex.FOLLOW_STYLE){
                    float cRot = maxRotX;

                    if (animatable.isSwimming() || animatable.isFallFlying()) {
                        cRot = (entityData.headPitch() - 90) * MathHelper.RADIANS_PER_DEGREE;
                    } else if (animatable.isCrawling()) {
                        cRot = -90 * MathHelper.RADIANS_PER_DEGREE;
                    } else {
                        int moveForward = animatable.getMoveForward();
                        if (moveForward < 0) {
                            cRot *= -moveForward;
                        } else if (moveForward > 0) {
                            cRot *= -moveForward;
                        } else {
                            cRot = 0;
                        }
                        cRot *= 0.6F;
                    }
                    rotX = MainUtil.controlledLerp(tickDelta, rotX, cRot, 0.2f);
                    rotY = MainUtil.controlledLerp(tickDelta, rotY, 0, 0.8f);
                } else if (animationStyle == OffsetIndex.FIXED_STYLE) {
                    rotX = MainUtil.controlledLerp(tickDelta, rotX, 0, 0.8f);
                    rotY = MainUtil.controlledLerp(tickDelta, rotY, rotation1, 0.8f);
                } else {
                    head.setRotX((entityData.headPitch()) * MathHelper.RADIANS_PER_DEGREE);
                    head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                }
                animatable.setBodyRotationX(rotX);
                animatable.setBodyRotationY(rotY);
                body.setRotX(rotX);
                body.setRotY(rotY);
            }
            if (head != null) {
                float rotX = animatable.getHeadRotationX();
                float rotY = animatable.getHeadRotationY();
                if (animationStyle == OffsetIndex.FOLLOW_STYLE){
                    /*This code makes the head of the model turn towards swim rotation while swimming*/
                    if (animatable.isSwimming() || animatable.isCrawling() || animatable.isFallFlying()) {
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
                    rotX = (entityData.headPitch() + swimRotCorrect) * MathHelper.RADIANS_PER_DEGREE;
                    rotY = entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE;
                    head.setRotX(rotX);
                    head.setRotY(rotY);
                } else if (animationStyle == OffsetIndex.FIXED_STYLE){
                    rotX = 0;
                    rotY = rotation1;
                    head.setRotX(rotX);
                    head.setRotY(rotY);
                } else {
                    rotX = (entityData.headPitch()) * MathHelper.RADIANS_PER_DEGREE;
                    rotY = entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE;
                    head.setRotX(rotX);
                    head.setRotY(rotY);

                }
                animatable.setHeadRotationX(rotX);
                animatable.setHeadRotationY(rotY);
            }
        } else {
            CoreGeoBone stand = getAnimationProcessor().getBone("stand");
            CoreGeoBone body = getAnimationProcessor().getBone("body");
            CoreGeoBone head = getAnimationProcessor().getBone("head");

            if (body != null) {
                float rotX = animatable.getBodyRotationX();
                float rotY = animatable.getBodyRotationY();
                body.setRotX(rotX);
                body.setRotY(rotY);
            }
            if (stand != null) {
                float rotX = animatable.getStandRotationX();
                float rotY = animatable.getStandRotationY();
                stand.setRotX(rotX);
                stand.setRotY(rotY);
            }
            if (head != null) {
                float rotX = animatable.getHeadRotationX();
                float rotY = animatable.getHeadRotationY();
                head.setRotX(rotX);
                head.setRotY(rotY);
            }
        }
    }
}
