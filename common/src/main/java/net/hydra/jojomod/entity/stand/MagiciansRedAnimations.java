package net.hydra.jojomod.entity.stand;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
public class MagiciansRedAnimations {
    public static final AnimationDefinition FLAMETHROWER_BARRAGE = AnimationDefinition.Builder.withLength(1.0F).looping()
		.addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.3F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-8.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.3F, 1.6F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-82.0F, -12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-85.0F, -12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-82.0F, -12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-82.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-85.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-82.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 18.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, -18.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition FLAMETHROWER_WINDUP = AnimationDefinition.Builder.withLength(1.1831F)
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(-22.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(-22.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.posVec(0.0F, -0.4F, 2.58F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.posVec(0.0F, -0.4F, 2.58F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.posVec(0.0F, -3.1F, 7.8F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.posVec(0.0F, -3.1F, 7.8F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.3F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(1.1831F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(-35.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(-35.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1317F, KeyframeAnimations.degreeVec(-21.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(-8.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.posVec(0.0F, -0.11F, 2.26F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.posVec(0.0F, -0.5F, 2.4F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.posVec(0.0F, -3.1F, 6.4F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.posVec(0.0F, -3.1F, 6.4F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1317F, KeyframeAnimations.posVec(0.0F, -1.4F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, -0.4F, 1.6F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1029F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 88.75F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2572F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 177.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 129.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 129.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.823F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1317F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(-82.0F, -12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1029F, KeyframeAnimations.posVec(0.1F, -1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2572F, KeyframeAnimations.posVec(-4.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.posVec(-2.1F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.posVec(-2.1F, -1.44F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.823F, KeyframeAnimations.posVec(0.3F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.posVec(0.3F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.posVec(0.3F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1317F, KeyframeAnimations.posVec(0.3F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.2572F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 42.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 42.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.823F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 106.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 106.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 106.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1317F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1029F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -88.75F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2572F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -177.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -129.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -129.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.823F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1317F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(-82.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1029F, KeyframeAnimations.posVec(-0.1F, -1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2572F, KeyframeAnimations.posVec(4.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.posVec(2.1F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.posVec(2.1F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7716F, KeyframeAnimations.posVec(0.5F, -1.44F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.823F, KeyframeAnimations.posVec(-0.3F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.posVec(-0.3F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.posVec(-0.3F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1317F, KeyframeAnimations.posVec(-0.3F, -0.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.2572F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4115F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.823F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -106.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -106.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -106.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1317F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1029F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(-4.3235F, 0.3779F, 4.3057F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(-4.3235F, 0.3779F, 4.3057F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 18.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(1.0802F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(1.0802F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1029F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1029F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(-4.3235F, -0.3779F, -4.3057F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(-4.3235F, -0.3779F, -4.3057F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, -18.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1029F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7202F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0802F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(1.0802F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.posVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("beak_bottom", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.5658F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8745F, KeyframeAnimations.degreeVec(32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1831F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
    public static final AnimationDefinition FireLashCombo1 = AnimationDefinition.Builder.withLength(0.4583F)
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, -35.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 75.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 75.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 75.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 75.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.4583F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -125.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-103.4157F, -1.0435F, -110.7041F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-81.3947F, 7.6729F, -120.1819F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -2.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-69.8413F, 7.0453F, 2.5782F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -12.0F, 0.75F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-8.0F, -12.0F, -6.1F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -12.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -24.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-5.1F, -22.98F, -2.04F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball12", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -36.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-7.2F, -35.82F, -2.55F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -21.6F, -0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -24.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball13", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -48.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-10.9F, -42.66F, -9.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-4.42F, -32.31F, 8.54F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -18.0F, 11.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -16.9F, 19.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball14", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -72.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(-1.14F, -75.43F, -2.72F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-5.8F, -53.77F, -13.87F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-3.3F, -14.83F, 29.09F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -2.67F, 23.28F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 12.0F, 30.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 11.3F, 41.9F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball15", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -60.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.54F, -66.33F, -8.22F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.86F, -32.06F, 24.43F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(1.0F, -11.4F, 20.1F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(1.0F, -3.1F, 29.7F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball16", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -2.8F, 34.88F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 11.77F, 22.62F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 30.0F, 25.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 30.0F, 38.1F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-16.76F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(15.08F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-21.29F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(24.36F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition FireLashCombo2 = AnimationDefinition.Builder.withLength(0.4583F)
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, -32.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, -32.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 75.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 75.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 25.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, -125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, -125.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 109.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 117.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(1.82F, 0.0F, 83.14F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, -27.5F, 54.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-26.6704F, 0.0F, 34.6308F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-37.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-14.2659F, 5.7832F, 25.2469F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(-0.38F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -17.2F, 30.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -47.0F, 5.77F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -44.58F, 13.92F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -36.03F, 6.63F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, -9.5F, 4.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(-0.7F, -21.8F, 10.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.53F, -10.51F, 18.43F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(1.1F, -8.72F, 23.37F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(3.03F, -10.84F, 7.35F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(5.3F, -13.7F, -4.3F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(3.0F, 12.8F, 43.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(2.78F, -24.08F, 24.43F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(2.66F, -34.04F, 23.91F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(2.17F, -28.88F, -8.29F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(1.6F, -21.1F, -20.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 22.4F, 46.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -38.6F, 19.59F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -51.66F, 2.8F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -23.12F, -20.89F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, -11.4F, -28.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(-1.5F, 1.1F, 46.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-0.16F, -17.06F, 27.55F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.45F, -22.14F, 28.13F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(2.54F, -22.42F, 7.11F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(5.0F, -22.0F, -12.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -14.79F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -6.34F, 9.35F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -2.08F, 10.2F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-78.49F, 0.0F, -78.76F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-67.1572F, 40.155F, -60.1335F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-81.7526F, 37.1997F, -74.9718F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-70.0F, 0.0F, -75.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -2.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -2.11F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-40.27F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-69.84F, 7.05F, 2.58F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-69.8413F, 7.0453F, 2.5782F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-74.23F, 12.19F, -1.38F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.125F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.scaleVec(-0.0141F, -0.0141F, -0.0141F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -9.82F, -8.96F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.33F, -1.72F, 0.66F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.33F, -1.72F, 0.66F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball12", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -22.74F, 5.65F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball13", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, -16.9F, 19.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -16.48F, 18.58F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball14", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 11.3F, 41.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 11.08F, 41.22F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball15", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(1.0F, -3.1F, 29.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.98F, -2.92F, 29.05F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball16", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 30.0F, 38.1F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 30.0F, 38.1F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 29.38F, 37.52F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-16.76F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-19.27F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(15.08F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(19.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-21.29F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-14.98F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(24.36F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(18.34F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition FireLashCombo3 = AnimationDefinition.Builder.withLength(0.2917F)
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(1.12F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.08F, 4.32F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, -1.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.6F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.17F, 3.96F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, -1.1F, 3.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 48.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-100.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-100.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.16F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-40.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-6.75F, -0.44F, -1.91F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(5.52F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(5.8F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -19.45F, 1.64F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -22.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(55.0F, 0.0F, -48.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-100.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-100.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.16F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-40.61F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-37.6F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-77.44F, -73.65F, 74.34F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-77.3126F, -77.7386F, 77.9124F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-79.6707F, -74.3105F, 80.523F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, 7.59F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-5.4F, 8.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(-0.0331F, -0.0331F, -0.0331F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -6.08F, -3.48F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -13.9F, -7.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.33F, -24.3F, 4.34F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -27.0F, 4.4F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball12", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.43F, -18.36F, -6.42F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -41.1F, -11.6F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball13", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.58F, -25.51F, -0.79F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -61.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball14", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.08F, -24.76F, 6.91F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -47.9F, 15.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball15", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.66F, -16.64F, -1.81F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(1.6F, -43.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("Fireball16", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.28F, -9.39F, -1.13F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.6F, -12.9F, 7.3F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-11.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-57.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(14.95F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition FireLashHidden = AnimationDefinition.Builder.withLength(0.0F).looping()
            .addAnimation("Fireballs", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("Fireballs2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();


    public static final AnimationDefinition Fireball_Windup = AnimationDefinition.Builder.withLength(1.0F)
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-8.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -0.8F, 2.6F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-13.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -0.6F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-54.0F, 7.0F, -43.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(1.3F, -1.7F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.3974F, 2.8654F, -88.4008F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-32.115F, -6.512F, 42.6808F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(-1.3F, -1.7F, -2.7F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-34.8974F, -2.8654F, 88.4008F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("beak_top", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("beak_bottom", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(1.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition Fireball_Barrage = AnimationDefinition.Builder.withLength(0.9167F)
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.0833F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-8.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.8F, 2.6F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -0.8F, 2.6F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -0.8F, 2.6F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -0.8F, 2.6F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, -0.8F, 2.6F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, -0.8F, 0.1F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("beak_top", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("beak_bottom", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.0833F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-13.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-13.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-13.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(-13.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-13.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.6F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -0.6F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, -0.6F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -0.6F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, -0.6F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.3F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-54.0F, 7.0F, -43.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(1.3F, -1.7F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.3974F, 2.8654F, -88.4008F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-32.115F, -6.512F, 42.6808F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.0F, KeyframeAnimations.posVec(-1.3F, -1.7F, -2.7F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-34.8974F, -2.8654F, 88.4008F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.8333F, KeyframeAnimations.degreeVec(21.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition Red_Bind_Grab = AnimationDefinition.Builder.withLength(1.4167F)
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 9.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 9.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(12.4366F, 0.9762F, -7.4366F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(12.4366F, 0.9762F, -7.4366F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(9.5972F, 47.3114F, 3.3187F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(9.5972F, 47.3114F, 3.3187F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(21.9659F, -21.7561F, 2.6201F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(21.9659F, -21.7561F, 2.6201F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(-14.9539F, -1.0809F, -4.8821F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-10.1318F, -38.7555F, -1.3526F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-10.1318F, -38.7555F, -1.3526F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(-0.7413F, 0.6948F, -1.7227F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(-0.7413F, 0.6948F, -1.7227F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(-0.4845F, 17.3287F, 2.3306F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(2.7068F, -2.1915F, 5.7812F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(2.7068F, -2.1915F, 5.7812F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(34.8161F, 14.9084F, 27.1982F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(34.8161F, 14.9084F, 27.1982F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-60.8579F, 61.2197F, 51.0082F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-65.7446F, 18.0388F, 15.5491F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(-0.0528F, -2.9053F, -1.232F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3333F, KeyframeAnimations.posVec(-0.0528F, -2.9053F, -1.232F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-98.419F, -5.1598F, -30.5729F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(-98.419F, -5.1598F, -30.5729F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3333F, KeyframeAnimations.degreeVec(2.3844F, -1.2817F, -76.9115F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3333F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-26.0537F, -21.6054F, -29.1612F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(-26.0537F, -21.6054F, -29.1612F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(36.372F, 31.2879F, -43.5374F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(36.372F, 31.2879F, -43.5374F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.3666F, -2.1966F, 0.2012F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.3666F, -2.1966F, 0.2012F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-74.3072F, 16.8855F, 4.6653F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(-74.3072F, 16.8855F, 4.6653F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-74.31F, 16.89F, 4.67F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-74.31F, 16.89F, 4.67F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-71.6218F, 23.0031F, -16.1822F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-71.6218F, 23.0031F, -16.1822F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.735F, 0.113F, -3.6685F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.735F, 0.113F, -3.6685F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(1.6678F, 0.2909F, -3.9963F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(1.6678F, 0.2909F, -3.9963F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(47.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-17.5F, -27.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(-17.5F, -27.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-5.1631F, -16.4789F, -12.4419F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(-5.1631F, -16.4789F, -12.4419F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(1.0F, 0.5F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(1.0F, 0.5F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.1207F, 0.6526F, -2.1387F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.1207F, 0.6526F, -2.1387F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
}
