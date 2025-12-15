package net.hydra.jojomod.client.models.layers.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class FirearmFirstPersonAnimations {
    public static final AnimationDefinition snubnose_aim = AnimationDefinition.Builder.withLength(0.125F).looping()
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(311.7032F, 67.1051F, -383.1185F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 10.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-67.6702F, -47.112F, 45.6632F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -10.0F, 7.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition snubnose_fire_recoil = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(311.7032F, 67.1051F, -383.1185F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(299.2032F, 67.1051F, -383.1185F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(311.7032F, 67.1051F, -383.1185F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -10.0F, 10.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-67.6702F, -47.112F, 45.6632F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-77.6702F, -47.112F, 45.6632F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-67.6702F, -47.112F, 45.6632F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -10.0F, 7.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-1.0F, -10.0F, 7.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
    public static final AnimationDefinition snubnose_model_aim = AnimationDefinition.Builder.withLength(0.5F).looping()
            .addAnimation("SexyGun", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.2476F, -2.5F, -180.0278F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("SexyGun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, 18.0F, -14.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition snubnose_model_recoil = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("SexyGun", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.2476F, -2.5F, -180.0278F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-12.7476F, -2.5F, -180.0278F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0.2476F, -2.5F, -180.0278F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("SexyGun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, 18.0F, -14.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(4.0F, 16.4F, -14.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(4.0F, 18.0F, -14.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition snubnose_aim_left = AnimationDefinition.Builder.withLength(0.125F).looping()
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-67.6702F, 47.112F, -45.6632F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -10.0F, 7.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(311.7032F, -67.1051F, 383.1185F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 10.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition snubnose_fire_recoil_left = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("right_arm", new AnimationChannel(
                    AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-67.6702F, 47.112F, -45.6632F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-77.6702F, 47.112F, -45.6632F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-67.6702F, 47.112F, -45.6632F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(
                    AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -10.0F, 7.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(1.0F, -10.0F, 7.0F), AnimationChannel.Interpolations.LINEAR)
            ))

            .addAnimation("left_arm", new AnimationChannel(
                    AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(311.7032F, -67.1051F, 383.1185F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(299.2032F, -67.1051F, 383.1185F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(311.7032F, -67.1051F, 383.1185F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(
                    AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -10.0F, 10.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition snubnose_model_aim_left = AnimationDefinition.Builder.withLength(0.5F).looping()
            .addAnimation("SexyGun", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.2476F, 2.5F, 180.0278F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("SexyGun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-4.0F, 18.0F, -14.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition snubnose_model_recoil_left = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("SexyGun", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.2476F, 2.5F, 180.0278F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-12.7476F, 2.5F, 180.0278F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0.2476F, 2.5F, 180.0278F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("SexyGun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-4.0F, 18.0F, -14.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(-4.0F, 16.4F, -14.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-4.0F, 18.0F, -14.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition tommy_aim = AnimationDefinition.Builder.withLength(0.125F).looping()
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(311.7032F, 67.1051F, -383.1185F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, -2.0F, 13.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-68.6322F, -44.7914F, 47.0013F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-2.0F, -11.0F, 7.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition tommy_model_aim = AnimationDefinition.Builder.withLength(0.0F).looping()
            .addAnimation("TommyGun", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -180.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("TommyGun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, 27.0F, -12.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition tommy_aim_left =
            AnimationDefinition.Builder.withLength(0.125F).looping()

                    .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.0F,
                                    KeyframeAnimations.degreeVec(-68.6322F, 44.7914F, -47.0013F),
                                    AnimationChannel.Interpolations.LINEAR)
                    ))
                    .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.0F,
                                    KeyframeAnimations.posVec(2.0F, -11.0F, 7.0F),
                                    AnimationChannel.Interpolations.LINEAR)
                    ))

                    .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.0F,
                                    KeyframeAnimations.degreeVec(311.7032F, -67.1051F, 383.1185F),
                                    AnimationChannel.Interpolations.LINEAR)
                    ))
                    .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.0F,
                                    KeyframeAnimations.posVec(-4.0F, -2.0F, 13.0F),
                                    AnimationChannel.Interpolations.LINEAR)
                    ))
                    .build();

    public static final AnimationDefinition tommy_model_aim_left =
            AnimationDefinition.Builder.withLength(0.0F).looping()

                    .addAnimation("TommyGun", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.0F,
                                    KeyframeAnimations.degreeVec(0.0F, 0.0F, 180.0F),
                                    AnimationChannel.Interpolations.LINEAR)
                    ))
                    .addAnimation("TommyGun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.0F,
                                    KeyframeAnimations.posVec(-4.0F, 27.0F, -12.0F),
                                    AnimationChannel.Interpolations.LINEAR)
                    ))
                    .build();

    public static final AnimationDefinition tommy_recoil = AnimationDefinition.Builder.withLength(0.125F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(311.7032F, 67.1051F, -383.1185F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(309.2032F, 67.1051F, -383.1185F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(311.7032F, 67.1051F, -383.1185F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, -2.0F, 13.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(4.0F, -2.0F, 13.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(4.0F, -2.0F, 13.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-68.6322F, -44.7914F, 47.0013F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-71.1322F, -44.7914F, 47.0013F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-68.6322F, -44.7914F, 47.0013F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-2.0F, -11.0F, 7.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(-2.0F, -11.0F, 7.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(-2.0F, -11.0F, 7.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition tommy_model_recoil = AnimationDefinition.Builder.withLength(0.125F)
            .addAnimation("TommyGun", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -180.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, -180.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -180.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("TommyGun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, 27.0F, -12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(4.0F, 26.9F, -12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(4.0F, 27.0F, -12.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition tommy_recoil_left =
            AnimationDefinition.Builder.withLength(0.125F)

                    .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.0F, KeyframeAnimations.degreeVec(-68.6322F, 44.7914F, -47.0013F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-71.1322F, 44.7914F, -47.0013F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125F, KeyframeAnimations.degreeVec(-68.6322F, 44.7914F, -47.0013F), AnimationChannel.Interpolations.LINEAR)
                    ))
                    .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.0F, KeyframeAnimations.posVec(2.0F, -11.0F, 7.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.0417F, KeyframeAnimations.posVec(2.0F, -11.0F, 7.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125F, KeyframeAnimations.posVec(2.0F, -11.0F, 7.0F), AnimationChannel.Interpolations.LINEAR)
                    ))

                    .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.0F, KeyframeAnimations.degreeVec(311.7032F, -67.1051F, 383.1185F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.0417F, KeyframeAnimations.degreeVec(309.2032F, -67.1051F, 383.1185F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125F, KeyframeAnimations.degreeVec(311.7032F, -67.1051F, 383.1185F), AnimationChannel.Interpolations.LINEAR)
                    ))
                    .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.0F, KeyframeAnimations.posVec(-4.0F, -2.0F, 13.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.0417F, KeyframeAnimations.posVec(-4.0F, -2.0F, 13.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125F, KeyframeAnimations.posVec(-4.0F, -2.0F, 13.0F), AnimationChannel.Interpolations.LINEAR)
                    ))
                    .build();

    public static final AnimationDefinition tommy_model_recoil_left =
            AnimationDefinition.Builder.withLength(0.125F)

                    .addAnimation("TommyGun", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 180.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 180.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 180.0F), AnimationChannel.Interpolations.LINEAR)
                    ))
                    .addAnimation("TommyGun", new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.0F, KeyframeAnimations.posVec(-4.0F, 27.0F, -12.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.0417F, KeyframeAnimations.posVec(-4.0F, 26.9F, -12.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.125F, KeyframeAnimations.posVec(-4.0F, 27.0F, -12.0F), AnimationChannel.Interpolations.LINEAR)
                    ))
                    .build();

}
