// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 5.2.1
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class unknownAnimation {
	public static final AnimationDefinition mobPlantArmsMode = AnimationDefinition.Builder.withLength(3.3333F)
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-144.8974F, 4.0941F, 2.8728F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(-155.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.125F, KeyframeAnimations.degreeVec(-82.1548F, 14.7668F, 2.664F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.2083F, KeyframeAnimations.degreeVec(-74.6548F, 14.7668F, 2.664F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.2917F, KeyframeAnimations.degreeVec(-79.6548F, 14.7668F, 2.664F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, -1.26F, -0.26F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.875F, KeyframeAnimations.posVec(0.0F, -1.26F, -0.26F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.875F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.2083F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition shaArmsSend = AnimationDefinition.Builder.withLength(0.6667F)
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-9.9161F, -1.2988F, -7.3873F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-29.816F, -2.154F, -9.7675F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-29.816F, -2.154F, -9.7675F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-2.32F, -2.15F, -9.77F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-4.82F, -2.15F, -9.77F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-14.66F, 3.2114F, 12.0868F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-14.9495F, -1.2958F, -4.8308F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-14.9481F, 1.2843F, 4.8272F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition shaArmsShoot = AnimationDefinition.Builder.withLength(0.7917F)
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 25.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(46.2409F, 6.6078F, -44.0561F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-68.2593F, -4.6024F, -16.185F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-64.1196F, -7.984F, -14.9395F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-14.9455F, 1.2926F, 4.8304F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition btdDetonation = AnimationDefinition.Builder.withLength(1.7083F)
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-14.7822F, 2.576F, 9.6658F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-45.1839F, 29.2852F, 36.8253F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-47.5959F, 19.5377F, 26.0967F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(-29.3452F, -5.2813F, -3.053F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-29.6582F, -2.8164F, 1.3122F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.4583F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();
}