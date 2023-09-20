package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class StandModel extends GeoModel<StandEntity> {
    private final boolean turnsHead = true;
    private final float maxRotX = 0.25F;
    private final float minRotX = 0.01F;
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

    @Override
    public void setCustomAnimations(StandEntity animatable, long instanceId, AnimationState<StandEntity> animationState) {
        if (!this.turnsHead)
            return;

        CoreGeoBone body = getAnimationProcessor().getBone("body");
         //RoundaboutMod.LOGGER.info("MF:"+ animatable.getMoveForward());
        if (body != null) {
            int moveForward = animatable.getMoveForward();
            float rotX = body.getRotX();
            float cRot = maxRotX;
            float cRot2 = minRotX;

            if (moveForward < 0) {
                cRot*=-moveForward;
            } else if (moveForward > 0){
                cRot*=-moveForward;
                cRot2*=moveForward;
            } else {
                cRot = 0;
            }
            if (Math.abs(rotX - cRot)<= minRotX){cRot=maxRotX;}
            else if (rotX > cRot){rotX-=cRot2;} else if (rotX < cRot){rotX+=cRot2;}
            body.setRotX(rotX);
        }

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
