package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
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
        return new Identifier(RoundaboutMod.MOD_ID,"animations/star_platinum.json");
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
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            CoreGeoBone stand = getAnimationProcessor().getBone("stand");
            CoreGeoBone body = getAnimationProcessor().getBone("body");
            CoreGeoBone head = getAnimationProcessor().getBone("head");

            if (stand != null){
                float rotX = stand.getRotX();
                float cRY = 0;
                if (animationNumber == 1) {
                    cRY = entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE;
                    RoundaboutMod.LOGGER.info(String.valueOf(cRY));
                }
                rotX = MainUtil.controlledLerp(tickDelta, rotX, cRY, 0.8f);
                animatable.setStandRotationX(rotX);
                stand.setRotX(rotX);
            }

            if (body != null) {
                float rotX = animatable.getBodyRotationX();
                float rotY = animatable.getBodyRotationY();
                if (animationNumber == 0) {
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
                } else if (animationNumber == 1) {
                    rotX = MainUtil.controlledLerp(tickDelta, rotX, 0, 0.8f);
                    rotY = MainUtil.controlledLerp(tickDelta, rotY, 0, 0.8f);
                } else {
                    rotX = MainUtil.controlledLerp(tickDelta, rotX, 0, 0.2f);
                    rotY = MainUtil.controlledLerp(tickDelta, rotY, 0, 0.2f);
                }
                animatable.setBodyRotationX(rotX);
                animatable.setBodyRotationY(rotY);
                body.setRotX(rotX);
                body.setRotY(rotY);
            }
            if (head != null) {
                if (animationNumber == 0) {
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
                    head.setRotX((entityData.headPitch() + swimRotCorrect) * MathHelper.RADIANS_PER_DEGREE);
                    head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                } else if (animationNumber == 1) {
                    float yaw = 0;
                    head.setRotX(0);
                    head.setRotY((entityData.netHeadYaw()+yaw) * MathHelper.RADIANS_PER_DEGREE);
                } else {
                    head.setRotX((entityData.headPitch()) * MathHelper.RADIANS_PER_DEGREE);
                    head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                }
            }
        }
    }
}
