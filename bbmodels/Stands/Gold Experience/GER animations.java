// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 5.2.2
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class unknownAnimation {
	public static final AnimationDefinition Barrage Windup = AnimationDefinition.Builder.withLength(1.5F)
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-9.9202F, 1.0834F, 9.9346F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-9.9202F, 1.0834F, 9.9346F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -0.1305F, -0.9914F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -0.1305F, -0.9914F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(7.7616F, 14.8687F, 2.0031F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(7.7616F, 14.8687F, 2.0031F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-169.1845F, 70.6598F, -64.3597F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-169.1845F, 70.6598F, -64.3597F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-44.3384F, 7.5713F, 9.9752F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-44.3384F, 7.5713F, 9.9752F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(7.5284F, -4.9571F, -0.6542F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(7.5284F, -4.9571F, -0.6542F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-102.7183F, -21.2862F, -10.5858F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-102.7183F, -21.2862F, -10.5858F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.2578F, -0.9442F, -0.2051F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.posVec(0.2578F, -0.9442F, -0.2051F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-18.5484F, 19.0376F, -6.246F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(-18.5484F, 19.0376F, -6.246F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition Barrage = AnimationDefinition.Builder.withLength(0.165F).looping()
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.9375F, 1.5017F, 4.7696F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(-4.9375F, -1.5017F, -4.7696F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-4.9375F, 1.5017F, 4.7696F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(-4.9375F, 1.5017F, 4.7696F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0311F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0311F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.posVec(0.0F, -1.2519F, -2.9875F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0311F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0311F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0311F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0311F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(25.5598F, -23.5711F, -14.5895F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(25.5598F, -23.5711F, -14.5895F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(25.5598F, 23.5711F, 14.5895F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(25.5598F, 23.5711F, 14.5895F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.0964F, 48.3661F, 70.436F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-69.1284F, 37.1324F, 80.5675F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(-69.1284F, 37.1324F, 80.5675F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-34.0964F, 48.3661F, 70.436F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-0.1126F, 48.9105F, 59.1056F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-0.1126F, 48.9105F, 59.1056F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(-34.0964F, 48.3661F, 70.436F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(-1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.posVec(-1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.posVec(-1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.posVec(-1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(-1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.posVec(-1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(-1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-74.7617F, -11.621F, 4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-19.7617F, -11.621F, 4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(-19.7617F, -11.621F, 4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-74.7617F, -11.621F, 4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-102.2617F, -11.621F, 4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-102.2617F, -11.621F, 4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(-74.7617F, -11.621F, 4.3447F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.0157F, 2.3842F, -0.7522F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-0.0159F, -9.8842F, 0.7543F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(-0.0159F, -9.8842F, 0.7543F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-0.0157F, -2.3842F, 0.7522F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-0.0159F, 9.8842F, -0.7543F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-0.0159F, 9.8842F, -0.7543F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(-0.0157F, 2.3842F, -0.7522F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-0.0157F, -2.3842F, 0.7522F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(-0.0157F, -2.3842F, 0.7522F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-0.0628F, 4.768F, -1.507F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-0.0628F, 4.768F, -1.507F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-37.6769F, -9.9616F, -3.6162F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-37.6769F, -9.9616F, -3.6162F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(-37.6769F, -9.9616F, -3.6162F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-37.6769F, -9.9616F, -3.6162F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-37.6769F, -9.9616F, -3.6162F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-37.6769F, -9.9616F, -3.6162F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(-37.6769F, -9.9616F, -3.6162F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(87.3734F, 0.8787F, -0.6557F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(87.3734F, 0.8787F, -0.6557F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(87.3734F, 0.8787F, -0.6557F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(87.3734F, 0.8787F, -0.6557F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1414F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.342F, 2.0603F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.posVec(0.0F, 1.342F, 2.0603F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.posVec(0.0F, 1.342F, 2.0603F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.posVec(0.0F, 1.342F, 2.0603F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(0.0F, 1.342F, 2.0603F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1414F, KeyframeAnimations.posVec(0.0F, 1.342F, 2.0603F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(0.0F, 1.342F, 2.0603F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1414F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.2F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.degreeVec(0.0208F, -9.7506F, -20.4015F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.posVec(0.0F, 1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.degreeVec(-2.7511F, 34.1966F, -1.1775F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(4.3304F, -0.5254F, -31.8511F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.posVec(7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(-0.55F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.3651F, 0.5498F, -35.4395F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.degreeVec(0.0F, -5.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(0.0F, -5.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-2.7511F, 34.1966F, -1.1775F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(2.3651F, 0.5498F, -35.4395F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(1.45F, -2.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.posVec(5.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.posVec(5.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(8.25F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(1.45F, -2.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(0.0208F, 9.7506F, 20.4015F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(-6.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.posVec(-6.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.posVec(-7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(0.0F, 1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(-6.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.3304F, 0.5254F, 31.8511F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(4.3304F, 0.5254F, 31.8511F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.45F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.posVec(-4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(-4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.posVec(-7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(-0.45F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.degreeVec(2.3651F, -0.5498F, 35.4395F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(-8.25F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.posVec(-1.45F, -2.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(-5.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(-5.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0589F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(14.66F, -3.2114F, 12.0868F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(14.66F, -3.2114F, 12.0868F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(14.66F, -3.2114F, 12.0868F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(14.66F, -3.2114F, 12.0868F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(14.66F, -3.2114F, 12.0868F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(14.66F, -3.2114F, 12.0868F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(14.66F, -3.2114F, 12.0868F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(19.3832F, -2.4011F, 14.724F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(19.3832F, -2.4011F, 14.724F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(19.3832F, -2.4011F, 14.724F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(19.3832F, -2.4011F, 14.724F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.0964F, -48.3661F, -70.436F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-0.1126F, -48.9105F, -59.1056F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(-0.1126F, -48.9105F, -59.1056F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-34.0964F, -48.3661F, -70.436F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-69.1284F, -37.1324F, -80.5675F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-69.1284F, -37.1324F, -80.5675F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(-34.0964F, -48.3661F, -70.436F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.posVec(1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.posVec(1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.posVec(1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.posVec(1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.posVec(1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.posVec(1.1495F, -1.9016F, -0.2503F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-74.7617F, 11.621F, -4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0236F, KeyframeAnimations.degreeVec(-102.2617F, 11.621F, -4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0471F, KeyframeAnimations.degreeVec(-102.2617F, 11.621F, -4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-74.7617F, 11.621F, -4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1061F, KeyframeAnimations.degreeVec(-19.7617F, 11.621F, -4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1296F, KeyframeAnimations.degreeVec(-19.7617F, 11.621F, -4.3447F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.165F, KeyframeAnimations.degreeVec(-74.7617F, 11.621F, -4.3447F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition RTZ = AnimationDefinition.Builder.withLength(0.5F)
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(5.0429F, -7.4713F, -0.6574F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(2.5F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.5F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(1.122F, -1.0712F, 0.2295F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(-0.4739F, 1.7483F, -0.0717F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(-0.4739F, 1.7483F, -0.0717F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(40.8356F, -27.3753F, -12.2698F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.5661F, -0.6951F, -1.6595F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -0.0436F, 0.4981F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.0436F, 0.4981F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(15.0284F, -4.9571F, -0.6542F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-36.1973F, -10.5479F, -14.0439F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-65.5019F, 9.1314F, 30.1731F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-65.5019F, 9.1314F, 30.1731F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.3007F, -0.7566F, -0.5806F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(-0.3333F, -1.2773F, -0.7679F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.018F, -1.0541F, 0.1414F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.9914F, 0.1305F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -40.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-7.5F, -47.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-7.5F, -47.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-5.0408F, 7.6892F, 1.8333F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-5.0408F, 7.6892F, 1.8333F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(15.2207F, 9.656F, 2.6129F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(15.2207F, 9.656F, 2.6129F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(12.6866F, -9.7606F, 0.3144F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-17.3956F, -10.895F, -9.6972F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-17.3956F, -10.895F, -9.6972F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(29.1475F, -7.4355F, 13.0643F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(29.1475F, -7.4355F, 13.0643F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(45.5041F, -14.4776F, -18.9671F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(7.5F, -25.0F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(7.5F, -25.0F, -17.5F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.4567F, -1.3384F, 0.012F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-74.8704F, -3.8221F, 30.1569F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 30.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 30.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition Laser Windup = AnimationDefinition.Builder.withLength(0.625F)
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0607F, 12.4879F, 0.554F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0607F, 12.4879F, 0.554F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(-0.25F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(-0.25F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(22.5F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(22.5F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.1F, -0.1913F, -0.451F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.1F, -0.1913F, -0.451F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-12.3964F, -1.6189F, -29.8241F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-12.3964F, -1.6189F, -29.8241F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(5.0767F, 9.9616F, 0.8804F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(5.0767F, 9.9616F, 0.8804F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, -20.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, -20.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-61.8241F, -26.1476F, 2.2418F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-61.8241F, -26.1476F, 2.2418F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-89.1305F, 41.1908F, -42.1586F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-89.1305F, 41.1908F, -42.1586F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(-0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(-0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition Laser Shot = AnimationDefinition.Builder.withLength(0.625F)
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0607F, 12.4879F, 0.554F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-2.4393F, 12.4879F, 0.554F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0607F, 12.4879F, 0.554F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.25F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-0.25F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(-0.25F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.5F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(11.9464F, -13.0294F, 2.5052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(16.9464F, -13.0294F, 2.5052F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.1F, -0.1913F, -0.451F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.1F, -0.1913F, -0.451F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.1F, -0.1913F, -0.451F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.8805F, 9.9616F, 17.5767F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.8805F, 9.9616F, 17.5767F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.3964F, -1.6189F, -29.8241F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-12.257F, -13.5108F, -37.4924F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-12.257F, -13.5108F, -37.4924F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0767F, 9.9616F, 0.8804F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(2.5767F, 9.9616F, 0.8804F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(2.5767F, 9.9616F, 0.8804F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -20.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-4.0879F, -20.1981F, -7.6538F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-4.0879F, -20.1981F, -7.6538F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-61.8241F, -26.1476F, 2.2418F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-70.6714F, -16.7474F, 5.9129F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-69.3241F, -26.1476F, 2.2418F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-89.1305F, 41.1908F, -42.1586F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-85.3502F, 65.9731F, -50.6973F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-94.4627F, 48.5383F, -49.5421F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(-0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.posVec(-0.5373F, 0.8306F, -0.1465F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.1F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();
}