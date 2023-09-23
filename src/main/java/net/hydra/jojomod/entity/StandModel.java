package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
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

    @Override
    public void setCustomAnimations(StandEntity animatable, long instanceId, AnimationState<StandEntity> animationState) {
        if (!this.turnsHead)
            return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!mc.isPaused() || animatable.shouldPlayAnimsWhileGamePaused()){
            //When Game is Paused, don't procede
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        CoreGeoBone body = getAnimationProcessor().getBone("body");
        float tickDelta = mc.getLastFrameDuration();
         RoundaboutMod.LOGGER.info("MF:"+ tickDelta);
        if (body != null) {
            float rotX = body.getRotX();
            float cRot = maxRotX;


            if (animatable.isSwimming() || animatable.isFallFlying()) {
                cRot = (entityData.headPitch() - 90) * MathHelper.RADIANS_PER_DEGREE;
            } else if (animatable.isCrawling()) {
                cRot= -90 * MathHelper.RADIANS_PER_DEGREE;
            } else {
                int moveForward = animatable.getMoveForward();
                if (moveForward < 0) {
                    cRot *= -moveForward;
                } else if (moveForward > 0) {
                    cRot *= -moveForward;
                } else {
                    cRot = 0;
                }
                cRot*= 0.6F;
            }

            body.setRotX(controlledLerp(tickDelta,rotX,cRot,0.2f));
        }

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            float headRot = entityData.headPitch();
                headRot-=45;

                /*This code makes the head of the model turn towards swim rotation while swimming*/
            if (animatable.isSwimming() || animatable.isCrawling() || animatable.isFallFlying()) {
                if (swimRotCorrect > -45){swimRotCorrect-=2;swimRotCorrect=Math.min(swimRotCorrect,-45);}
            } else {
                if (swimRotCorrect < 0){swimRotCorrect+=2;swimRotCorrect=Math.max(swimRotCorrect,0);}
            }
            head.setRotX((entityData.headPitch()+swimRotCorrect) * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }}
    }
}
