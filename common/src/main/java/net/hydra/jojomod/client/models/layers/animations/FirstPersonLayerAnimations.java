package net.hydra.jojomod.client.models.layers.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class FirstPersonLayerAnimations {
    public static final AnimationDefinition bubble_aim = AnimationDefinition.Builder.withLength(0.0417F).looping()
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.5497F, -16.0F, -31.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-4.0F, -0.5F, -1.45F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.5F, 16.0F, 31.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, -0.5F, -1.45F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition animation = AnimationDefinition.Builder.withLength(0.0F)

            .build();


    public static final AnimationDefinition bubble_aim_recoil = AnimationDefinition.Builder.withLength(0.4167F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.5497F, -16.0F, -31.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-21.559F, -19.9258F, -40.672F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-4.5497F, -16.0F, -31.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-4.0F, -0.5F, -1.45F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(-7.0F, 0.5F, -0.45F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-4.0F, -0.5F, -1.45F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.5F, 16.0F, 31.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-22.3278F, 18.9844F, 38.2157F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-4.5F, 16.0F, 31.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, -0.5F, -1.45F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(6.0F, 0.5F, -1.45F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(4.0F, -0.5F, -1.45F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition offsetCorrect = AnimationDefinition.Builder.withLength(0.0F).looping()
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}
