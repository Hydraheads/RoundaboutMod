package net.hydra.jojomod.event.index;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
public class PoseAnimations {

        public static final AnimationDefinition Giorno = AnimationDefinition.Builder.withLength(0.5417F)
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(25.0F, 100.8945F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(-31.2549F, 0.0F, 108.862F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(1.4F, -1.9F, 4.9F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(-9.4957F, -14.1195F, 17.0008F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(-4.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 68.7549F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 68.7549F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(1.3F, 0.0F, 1.8F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 68.7549F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(-1.25F, 0.0F, -1.85F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition OH_NO = AnimationDefinition.Builder.withLength(0.2917F)
        .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-11.4592F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-128.6496F, -32.8019F, 17.6718F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
        new Keyframe(0.2917F, KeyframeAnimations.posVec(0.9F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-128.6496F, 32.8019F, -17.6718F), AnimationChannel.Interpolations.LINEAR)
        ))
        .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
        new Keyframe(0.2917F, KeyframeAnimations.posVec(-0.9F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .build();

        public static final AnimationDefinition JOTARO = AnimationDefinition.Builder.withLength(0.2917F)
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-2.8648F, 11.4592F, -4.5837F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.posVec(0.3274F, -0.1637F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, -51.5662F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.posVec(0.8185F, 0.0F, -1.637F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, -51.5662F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.posVec(-0.8185F, 0.0F, 1.637F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-97.4028F, 11.4592F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.posVec(1.637F, 0.0F, -4.9111F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.posVec(-1.637F, 0.0F, 4.9111F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, -51.5662F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();


        public static final AnimationDefinition WRRRYYY = AnimationDefinition.Builder.withLength(0.2272F).looping()
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.8366F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1136F, KeyframeAnimations.degreeVec(-11.4592F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2272F, KeyframeAnimations.degreeVec(-45.8366F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -5.5F, 9.5F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 57.2958F, 28.6479F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1136F, KeyframeAnimations.degreeVec(0.0F, 57.2958F, 45.8366F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2272F, KeyframeAnimations.degreeVec(0.0F, 57.2958F, 28.6479F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -6.0F, 6.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -57.2958F, -28.6479F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1136F, KeyframeAnimations.degreeVec(0.0F, -57.2958F, -45.8366F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2272F, KeyframeAnimations.degreeVec(0.0F, -57.2958F, -28.6479F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -6.0F, 6.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-51.5662F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1136F, KeyframeAnimations.degreeVec(-54.431F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2272F, KeyframeAnimations.degreeVec(-51.5662F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -5.15F, 9.05F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 11.4592F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1136F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 11.4592F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2272F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 11.4592F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -11.4592F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1136F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -11.4592F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2272F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -11.4592F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition SITTING = AnimationDefinition.Builder.withLength(0.0F).looping()
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-82.1528F, 12.0675F, 3.284F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-82.1528F, -12.0675F, -3.284F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();


        public static final AnimationDefinition Jonathan = AnimationDefinition.Builder.withLength(0.3472F)
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3472F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3472F, KeyframeAnimations.degreeVec(0.0F, 76.7763F, 34.3775F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3472F, KeyframeAnimations.degreeVec(-72.5059F, 45.8366F, 80.2141F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3472F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.7296F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3472F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 21.4592F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3472F, KeyframeAnimations.posVec(-1.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3472F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -11.4592F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3472F, KeyframeAnimations.posVec(-1.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();



        public static final AnimationDefinition Koichi = AnimationDefinition.Builder.withLength(0.4167F)
                .addAnimation("head_part", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 11.4592F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -1.5F, -3.274F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(30.9127F, 9.9809F, -20.1729F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(-0.8F, -1.2F, -1.3F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(-1.5F, -0.7F, -1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 22.9183F, 34.3775F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -1.5F, -2.2F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -11.4592F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -1.5F, -2.137F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(11.4592F, 0.0F, 5.7296F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -1.5F, -3.274F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
        public static final AnimationDefinition Joseph = AnimationDefinition.Builder.withLength(2.0F)
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(5.7296F, -5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(17.1887F, -17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-5.7296F, 40.107F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-5.7296F, 80.2141F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-5.7296F, 68.7549F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-5.7296F, 80.2141F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-5.7296F, 68.7549F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(-5.7296F, 80.2141F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-5.7296F, 68.7549F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-5.7296F, 80.2141F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.7296F, 68.7549F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-5.7296F, 80.2141F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-5.7296F, 68.7549F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-5.7296F, -40.107F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7083F, KeyframeAnimations.degreeVec(-5.7296F, -40.107F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(5.7296F, -40.107F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, -22.9183F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0833F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5833F, KeyframeAnimations.degreeVec(28.6479F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7083F, KeyframeAnimations.degreeVec(28.6479F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(-114.5916F, -22.9183F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(-131.7803F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, -22.9183F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0833F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-28.6479F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7083F, KeyframeAnimations.degreeVec(-28.6479F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 13.8521F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-28.6479F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-200.5352F, 0.0F, -57.2958F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-200.5352F, 0.0F, 5.7296F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-114.5916F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7083F, KeyframeAnimations.degreeVec(-114.5916F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(-171.8873F, -57.2958F, -22.9183F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.posVec(4.9111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.posVec(0.5111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(5.4111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.posVec(0.5111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.posVec(5.4111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.5111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0833F, KeyframeAnimations.posVec(5.4111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1667F, KeyframeAnimations.posVec(0.5111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.posVec(5.4111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.posVec(0.5111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.4167F, KeyframeAnimations.posVec(5.4111F, -2.4F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.9111F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.9111F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-2.2918F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-200.5352F, 0.0F, 57.2958F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-200.5352F, 0.0F, -5.7296F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-5.7296F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(6.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(8.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(6.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.degreeVec(8.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(6.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0833F, KeyframeAnimations.degreeVec(8.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1667F, KeyframeAnimations.degreeVec(6.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(8.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(6.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.4167F, KeyframeAnimations.degreeVec(8.7704F, 0.0F, 85.9437F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-80.2141F, -80.2141F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7083F, KeyframeAnimations.degreeVec(-80.2141F, -80.2141F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(-80.2141F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.posVec(-1.837F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.posVec(-2.737F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(-6.137F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.posVec(-2.737F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.posVec(-6.137F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(-2.737F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0833F, KeyframeAnimations.posVec(-6.137F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1667F, KeyframeAnimations.posVec(-2.737F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.posVec(-6.137F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.posVec(-2.737F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.4167F, KeyframeAnimations.posVec(-6.137F, 0.963F, -3.274F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, -5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, -22.9183F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0833F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5833F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7083F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition watch = AnimationDefinition.Builder.withLength(0.4167F)
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-90.1244F, 42.0524F, -6.8205F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-92.6244F, 42.0524F, -6.8205F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-90.1244F, 42.0524F, -6.8205F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-92.6244F, 42.0524F, -6.8205F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-90.1244F, 42.0524F, -6.8205F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.375F, KeyframeAnimations.degreeVec(-92.6244F, 42.0524F, -6.8205F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-90.1244F, 42.0524F, -6.8205F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.125F, KeyframeAnimations.degreeVec(-101.9342F, -18.4731F, 98.3276F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
        public static final AnimationDefinition Wamuu = AnimationDefinition.Builder.withLength(2.0833F)
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(-28.6479F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(-28.6479F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.degreeVec(28.65F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, -4.9111F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.degreeVec(36.4592F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, -2.2F, 3.1111F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-28.6479F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, -1.6F, 2.7111F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, -51.5662F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, -51.5662F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-28.6479F, 0.0F, -17.19F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, -4.9111F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, 51.5662F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, 51.5662F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(-174.1792F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.degreeVec(-28.6479F, 0.0F, 17.19F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, -4.9111F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, -4.9111F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();



        public static final AnimationDefinition TORTURE_DANCE = AnimationDefinition.Builder.withLength(19.2083F).looping()
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, -45.84F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.degreeVec(0.0F, -34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.degreeVec(0.0F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.7917F, KeyframeAnimations.degreeVec(0.0F, -34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.2917F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.375F, KeyframeAnimations.degreeVec(-45.8366F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5F, KeyframeAnimations.degreeVec(-45.8366F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5833F, KeyframeAnimations.degreeVec(0.0F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.degreeVec(28.6479F, 17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.degreeVec(28.6479F, 17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.9167F, KeyframeAnimations.degreeVec(0.0F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.degreeVec(0.0F, 34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.degreeVec(0.0F, 34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.degreeVec(0.0F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.degreeVec(0.0F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.degreeVec(0.0F, 34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.degreeVec(0.0F, 34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.9167F, KeyframeAnimations.degreeVec(-17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.2083F, KeyframeAnimations.degreeVec(-17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.3333F, KeyframeAnimations.degreeVec(45.8366F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.4583F, KeyframeAnimations.degreeVec(45.8366F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.5833F, KeyframeAnimations.degreeVec(0.0F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.degreeVec(0.0F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.degreeVec(28.6479F, 17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.degreeVec(28.6479F, 17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.4583F, KeyframeAnimations.degreeVec(-45.8366F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.2083F, KeyframeAnimations.degreeVec(-45.8366F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.4167F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.8333F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.0417F, KeyframeAnimations.degreeVec(-51.5662F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.4583F, KeyframeAnimations.degreeVec(-51.5662F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.6667F, KeyframeAnimations.degreeVec(28.6479F, 17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.375F, KeyframeAnimations.degreeVec(28.6479F, 17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.5833F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.9583F, KeyframeAnimations.degreeVec(17.1887F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.1667F, KeyframeAnimations.degreeVec(-51.5662F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.5833F, KeyframeAnimations.degreeVec(-51.5662F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.7917F, KeyframeAnimations.degreeVec(28.6479F, 17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.5F, KeyframeAnimations.degreeVec(28.6479F, 17.1887F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.7083F, KeyframeAnimations.degreeVec(17.1887F, -34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.0F, KeyframeAnimations.degreeVec(17.1887F, -34.3775F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.2083F, KeyframeAnimations.degreeVec(0.0F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.posVec(-9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.posVec(-9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.posVec(9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.posVec(1.1F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.posVec(1.1F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-57.2958F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-57.3F, 57.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-77.3493F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-77.3493F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-57.2958F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-57.2958F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.degreeVec(-77.3493F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-77.3493F, 45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(-137.5099F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.2917F, KeyframeAnimations.degreeVec(-137.5099F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.375F, KeyframeAnimations.degreeVec(-5.7296F, 5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5F, KeyframeAnimations.degreeVec(-5.7296F, 5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5833F, KeyframeAnimations.degreeVec(-91.6732F, 5.73F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.degreeVec(-91.6732F, 5.73F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.degreeVec(-68.7549F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.degreeVec(-68.7549F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-40.107F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.75F, KeyframeAnimations.degreeVec(-40.107F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.9167F, KeyframeAnimations.degreeVec(-57.2958F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.degreeVec(-57.2958F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.degreeVec(-37.2423F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.degreeVec(-37.2423F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.degreeVec(-57.2958F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.degreeVec(-57.2958F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.degreeVec(-37.2423F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.degreeVec(-37.2423F, -45.8366F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.9167F, KeyframeAnimations.degreeVec(-137.5099F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.2083F, KeyframeAnimations.degreeVec(-137.5099F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.3333F, KeyframeAnimations.degreeVec(-5.7296F, -5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.4583F, KeyframeAnimations.degreeVec(-5.7296F, -5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.5833F, KeyframeAnimations.degreeVec(-91.6732F, 5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.degreeVec(-91.6732F, 5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.degreeVec(-68.7549F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.degreeVec(-68.7549F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.degreeVec(0.0F, -57.2958F, 28.6479F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.degreeVec(0.0F, -57.2958F, 28.6479F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.degreeVec(-40.107F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.375F, KeyframeAnimations.degreeVec(-40.107F, 28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-174.1792F, 28.6479F, -11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.625F, KeyframeAnimations.degreeVec(-174.1792F, 28.6479F, -11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.7917F, KeyframeAnimations.degreeVec(-174.1792F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.2083F, KeyframeAnimations.degreeVec(-174.1792F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.4167F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.8333F, KeyframeAnimations.degreeVec(-30.9397F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.0417F, KeyframeAnimations.degreeVec(-88.2355F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.4583F, KeyframeAnimations.degreeVec(-88.2355F, 28.6479F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.6667F, KeyframeAnimations.degreeVec(-68.7549F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.375F, KeyframeAnimations.degreeVec(-68.7549F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.5833F, KeyframeAnimations.degreeVec(-30.9397F, 0.0F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.9583F, KeyframeAnimations.degreeVec(-30.9397F, 0.0F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.1667F, KeyframeAnimations.degreeVec(-88.2355F, 0.0F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.5833F, KeyframeAnimations.degreeVec(-88.2355F, 0.0F, -40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.7917F, KeyframeAnimations.degreeVec(-68.7549F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.5F, KeyframeAnimations.degreeVec(-68.7549F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.7083F, KeyframeAnimations.degreeVec(-30.9397F, -57.2958F, 11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.0F, KeyframeAnimations.degreeVec(-30.9397F, -57.2958F, 11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.2083F, KeyframeAnimations.degreeVec(-57.2958F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.posVec(-9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.posVec(-9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.posVec(9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.637F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.637F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-57.2958F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-57.3F, 57.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-37.2423F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-37.2423F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-57.2958F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.degreeVec(-57.2958F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.degreeVec(-37.2423F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-37.2423F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(-137.5099F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.2917F, KeyframeAnimations.degreeVec(-137.5099F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.375F, KeyframeAnimations.degreeVec(-5.7296F, -5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5F, KeyframeAnimations.degreeVec(-5.7296F, -5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5833F, KeyframeAnimations.degreeVec(-91.6732F, -5.73F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.degreeVec(-91.6732F, -5.73F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.degreeVec(-68.7549F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.degreeVec(-68.7549F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.degreeVec(-40.107F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.75F, KeyframeAnimations.degreeVec(-40.107F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.9167F, KeyframeAnimations.degreeVec(-57.2958F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.degreeVec(-57.2958F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.degreeVec(-77.3493F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.degreeVec(-77.3493F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.degreeVec(-57.2958F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.degreeVec(-57.2958F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.degreeVec(-77.3493F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.degreeVec(-77.3493F, -57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.9167F, KeyframeAnimations.degreeVec(-137.5099F, 57.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.2083F, KeyframeAnimations.degreeVec(-137.5099F, 57.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.3333F, KeyframeAnimations.degreeVec(-5.7296F, 5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.4583F, KeyframeAnimations.degreeVec(-5.7296F, 5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.5833F, KeyframeAnimations.degreeVec(-91.6732F, -5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.degreeVec(-91.6732F, -5.7296F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.degreeVec(-68.7549F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.degreeVec(-68.7549F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.degreeVec(-91.6732F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.degreeVec(-91.6732F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.degreeVec(-40.107F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.375F, KeyframeAnimations.degreeVec(-40.107F, -28.6479F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-174.1792F, -28.6479F, 11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.625F, KeyframeAnimations.degreeVec(-174.1792F, -28.6479F, 11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.7917F, KeyframeAnimations.degreeVec(-174.1792F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.2083F, KeyframeAnimations.degreeVec(-174.1792F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.4167F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.8333F, KeyframeAnimations.degreeVec(-30.9397F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.0417F, KeyframeAnimations.degreeVec(-88.2355F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.4583F, KeyframeAnimations.degreeVec(-88.2355F, -28.6479F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.6667F, KeyframeAnimations.degreeVec(-68.7549F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.375F, KeyframeAnimations.degreeVec(-68.7549F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.5833F, KeyframeAnimations.degreeVec(-30.9397F, 0.0F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.9583F, KeyframeAnimations.degreeVec(-30.9397F, 0.0F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.1667F, KeyframeAnimations.degreeVec(-88.2355F, 0.0F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.5833F, KeyframeAnimations.degreeVec(-88.2355F, 0.0F, 40.107F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.7917F, KeyframeAnimations.degreeVec(-68.7549F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.5F, KeyframeAnimations.degreeVec(-68.7549F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.7083F, KeyframeAnimations.degreeVec(-30.9397F, 57.2958F, -11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.0F, KeyframeAnimations.degreeVec(-30.9397F, 57.2958F, -11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.2083F, KeyframeAnimations.degreeVec(-57.2958F, 57.2958F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.posVec(-9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.posVec(-9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.posVec(9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(7.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.7296F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.7296F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.posVec(-9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.posVec(-9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.posVec(9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.19F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 28.6479F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 28.6479F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 51.5662F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 51.5662F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.degreeVec(-22.9183F, 0.0F, -34.3775F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.degreeVec(-22.9183F, 0.0F, -34.3775F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.degreeVec(-22.9183F, 0.0F, -34.3775F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.degreeVec(-22.9183F, 0.0F, -34.3775F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.6667F, KeyframeAnimations.degreeVec(-22.9183F, 0.0F, -34.3775F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.375F, KeyframeAnimations.degreeVec(-22.9183F, 0.0F, -34.3775F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.7917F, KeyframeAnimations.degreeVec(-22.9183F, 0.0F, -34.3775F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.5F, KeyframeAnimations.degreeVec(-22.9183F, 0.0F, -34.3775F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.posVec(-9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.posVec(-9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.posVec(9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.posVec(0.0F, 0.1F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.posVec(0.0F, 0.1F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.19F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(2.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -28.6479F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -28.6479F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -51.5662F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -51.5662F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -11.4592F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(15.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(16.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(17.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.1887F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(18.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.9183F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(19.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.1887F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.5F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(0.7917F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.0F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.2917F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1.5F, KeyframeAnimations.posVec(-9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.0F, KeyframeAnimations.posVec(-9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.2083F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.5833F, KeyframeAnimations.posVec(-3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(3.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.4167F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.7083F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(4.9167F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.4167F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(5.7083F, KeyframeAnimations.posVec(9.8221F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(6.9583F, KeyframeAnimations.posVec(9.82F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.1667F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.5833F, KeyframeAnimations.posVec(3.274F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(7.7083F, KeyframeAnimations.posVec(-0.1822F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.0833F, KeyframeAnimations.posVec(-0.1822F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(8.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(9.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.0417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.4583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(10.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.2917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.7083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(11.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.5417F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(12.9583F, KeyframeAnimations.posVec(-6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(13.7917F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.2083F, KeyframeAnimations.posVec(6.5481F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(14.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
}
