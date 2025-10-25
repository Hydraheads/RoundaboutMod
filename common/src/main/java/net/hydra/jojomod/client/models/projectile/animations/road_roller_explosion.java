package net.hydra.jojomod.client.models.projectile.animations;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 5.0.2
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class road_roller_explosion {
	public static final AnimationDefinition animation = AnimationDefinition.Builder.withLength(10.9583F)
        .addAnimation("bb_main", new AnimationChannel(AnimationChannel.Targets.SCALE,
                new Keyframe(0.0417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.125F, KeyframeAnimations.scaleVec(1.0F, 0.6F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.2917F, KeyframeAnimations.scaleVec(1.0F, 0.6F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.375F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.4583F, KeyframeAnimations.scaleVec(1.0F, 0.6F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.5417F, KeyframeAnimations.scaleVec(2.5F, 2.6F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(0.5833F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
        ))
        .build();
}