// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 4.12.4
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class unknownAnimation {
	public static final AnimationDefinition ArmIdle = AnimationDefinition.Builder.withLength(3.0F).looping()
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(14.9943F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5417F, KeyframeAnimations.degreeVec(7.4943F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(14.9943F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.4944F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.4993F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-22.4943F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-17.4993F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.4963F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(1.25F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(2.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(2.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("no_arms", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(2.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition ArmIdle2 = AnimationDefinition.Builder.withLength(3.0F).looping()
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9992F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-11.0008F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-18.9992F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-36.9997F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-16.9997F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-36.9997F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9992F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-11.0008F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-18.9992F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-36.9997F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-16.9997F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-36.9997F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(14.9943F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5417F, KeyframeAnimations.degreeVec(7.4943F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(14.9943F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.4944F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.4993F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-22.4943F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-17.4993F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.4963F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(1.25F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(2.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(2.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("no_arms", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(2.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();
}