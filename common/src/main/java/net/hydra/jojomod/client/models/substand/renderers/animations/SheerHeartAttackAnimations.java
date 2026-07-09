package net.hydra.jojomod.client.models.substand.renderers.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class SheerHeartAttackAnimations {
    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(2.2083F)

            .build();

    public static final AnimationDefinition MOVING = AnimationDefinition.Builder.withLength(0.2083F).looping()
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.2F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}
