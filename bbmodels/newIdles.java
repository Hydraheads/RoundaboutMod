// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 5.2.1
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class unknownAnimation {
	public static final AnimationDefinition kneel = AnimationDefinition.Builder.withLength(9.0909F).looping()
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.5454F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(9.0909F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0374F, 4.9238F, 0.8704F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(5.0374F, 4.9238F, 0.8704F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(10.0374F, 4.9238F, 0.8704F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-13.1199F, -58.5063F, -17.1482F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(-18.1199F, -58.5063F, -17.1482F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(-13.1199F, -58.5063F, -17.1482F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.0872F, -0.9962F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.posVec(0.0F, -0.0872F, -0.9962F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(55.4891F, 65.1277F, 14.9099F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(65.4492F, 67.6027F, 25.7743F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(55.4891F, 65.1277F, 14.9099F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.0436F, 0.999F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.posVec(0.0F, -0.0436F, 0.999F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(-77.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0959F, 0.7964F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.posVec(0.0F, 0.0959F, 0.7964F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-79.8489F, 9.8466F, 1.7538F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(-84.8489F, 9.8466F, 1.7538F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(-79.8489F, 9.8466F, 1.7538F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(105.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -17.5F, -2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(-5.0F, -17.5F, -2.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(0.0F, -17.5F, -2.5F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.5454F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(9.0909F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition brave = AnimationDefinition.Builder.withLength(5.4054F).looping()
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.degreeVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(2.4905F, -0.2178F, 12.4953F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.9108F, 49.7214F, 9.9433F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(-2.0892F, 49.7214F, 9.9433F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.degreeVec(3.9108F, 49.7214F, 9.9433F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.121F, -0.9917F, 0.0441F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.posVec(-0.121F, -0.9917F, 0.0441F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.5857F, 22.5107F, -87.7833F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(-90.5591F, 14.8255F, -87.7022F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4F, KeyframeAnimations.degreeVec(-90.5857F, 22.5107F, -87.7833F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.3242F, 1.6189F, -12.3964F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(7.1566F, 2.2494F, -17.3593F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.degreeVec(7.3242F, 1.6189F, -12.3964F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(5.9918F, 0.3134F, -2.9836F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.degreeVec(6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.1469F, 0.6151F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.posVec(0.0F, -0.1469F, 0.6151F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(-42.4998F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4F, KeyframeAnimations.degreeVec(-45.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4F, KeyframeAnimations.degreeVec(72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, -17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(-7.0F, -17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.degreeVec(-10.0F, -17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.7027F, KeyframeAnimations.degreeVec(14.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.4054F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition reborn = AnimationDefinition.Builder.withLength(4.2553F).looping()
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.0F, 62.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(-4.0F, 62.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1277F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, -46.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1277F, KeyframeAnimations.degreeVec(3.0F, -46.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(5.0F, -46.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.2278F, 12.1331F, 14.1949F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1277F, KeyframeAnimations.degreeVec(16.2278F, 12.1331F, 14.1949F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(10.2278F, 12.1331F, 14.1949F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(-9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -5.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1277F, KeyframeAnimations.degreeVec(-6.0F, -5.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(0.0F, -5.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.1844F, -1.1882F, 0.9613F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.posVec(-0.1844F, -1.1882F, 0.9613F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 75.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1277F, KeyframeAnimations.degreeVec(-90.0F, 82.5F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(-90.0F, 75.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.0767F, 9.9616F, 0.8804F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1277F, KeyframeAnimations.degreeVec(10.0767F, 9.9616F, 0.8804F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(7.0767F, 9.9616F, 0.8804F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-60.3749F, -11.1506F, -0.6432F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.1277F, KeyframeAnimations.degreeVec(-58.1139F, -11.1554F, -2.2336F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(-60.3749F, -11.1506F, -0.6432F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.2553F, KeyframeAnimations.degreeVec(72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition patriot = AnimationDefinition.Builder.withLength(3.3333F).looping()
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.4811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(7.4811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-97.5967F, 36.5107F, 26.4487F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-92.5967F, 36.5107F, 26.4487F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-97.5967F, 36.5107F, 26.4487F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.5373F, -0.8434F, -1.7321F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.posVec(0.5373F, -0.8434F, -1.7321F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-61.7509F, 16.7796F, -28.2491F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-69.2509F, 16.7796F, -28.2491F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-61.7509F, 16.7796F, -28.2491F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.0F, 62.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(27.5F, 62.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(35.0F, 62.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.8505F, 0.0F, 2.6975F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.posVec(0.8505F, 0.0F, 2.6975F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.0726F, 1.0282F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.posVec(0.0F, -0.0726F, 1.0282F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-2.5F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(5.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-72.4843F, -2.3842F, -0.7522F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(-64.9843F, -2.3842F, -0.7522F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(-72.4843F, -2.3842F, -0.7522F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.6667F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.3333F, KeyframeAnimations.degreeVec(87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition relaxed = AnimationDefinition.Builder.withLength(4.8F).looping()
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3284F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 17.5F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-47.3912F, 3.6842F, 3.3826F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(-47.3912F, 3.6842F, 3.3826F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-63.8826F, -19.9575F, -7.102F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3F, KeyframeAnimations.degreeVec(-68.8826F, -19.9575F, -7.102F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(-63.8826F, -19.9575F, -7.102F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3F, KeyframeAnimations.degreeVec(-62.4776F, -2.2174F, -1.1549F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(-62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.1305F, -0.9914F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(4.8F, KeyframeAnimations.posVec(0.0F, 0.1305F, -0.9914F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.6973F, 0.061F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.posVec(0.6973F, 0.061F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-109.5748F, 18.4368F, -23.8176F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3F, KeyframeAnimations.degreeVec(-110.1898F, 16.0067F, -23.9997F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(-109.5748F, 18.4368F, -23.8176F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(88.3216F, 13.2721F, 7.0531F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(88.3216F, 13.2721F, 7.0531F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-13.2475F, -21.8917F, 1.3734F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.3F, KeyframeAnimations.degreeVec(-13.2475F, -21.8917F, -3.6266F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(-13.2475F, -21.8917F, 1.3734F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.8F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();
}