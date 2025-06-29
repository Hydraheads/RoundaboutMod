// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 4.12.5
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author hydra
 */
public class ringo_mandomAnimation {
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