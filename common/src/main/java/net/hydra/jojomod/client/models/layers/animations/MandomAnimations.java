package net.hydra.jojomod.client.models.layers.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class MandomAnimations {
        public static final AnimationDefinition back = AnimationDefinition.Builder.withLength(0.0F).looping()
                .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-92.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(7.0F, -7.575F, 7.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.9532F, -8.5039F, -6.0374F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("tentacles", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.4375F, -1.5018F, -4.7697F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
}
