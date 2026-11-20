package net.hydra.jojomod.client.models.stand.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class KingNothingAnimations {
    public static final AnimationDefinition DISINTEGRATE = AnimationDefinition.Builder.withLength(1.3333F)
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-3.0947F, -22.2971F, 8.11F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-0.7F, 2.0F, -1.3F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.72F, 19.8217F, -7.9753F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(0.3216F, -2.9826F, 0.0253F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-65.2071F, -24.0222F, 16.5766F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-28.4371F, 61.1766F, -32.4349F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(25.0313F, -25.2519F, -11.2664F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(2.7502F, 7.3008F, 1.1133F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-28.0756F, 11.0311F, 19.734F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, KeyframeAnimations.posVec(-0.7862F, 6.9555F, -0.049F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_left_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(25.0313F, -25.2519F, -11.2664F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(4.3165F, -4.5965F, 1.1133F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, -47.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(2.6933F, 3.2061F, -0.6835F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.posVec(-7.5076F, -0.9589F, -5.6317F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(5.3137F, 0.7283F, -10.9876F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(-7.6694F, -0.9502F, -3.5038F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.posVec(6.0696F, 0.8589F, -9.2402F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(1.3579F, -2.8451F, 2.8392F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-6.8838F, -3.9353F, 6.2841F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0833F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(6.0422F, 0.7455F, 1.9839F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(1.5252F, 0.1556F, 9.2782F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(6.3849F, -5.2768F, -11.5494F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(20.2846F, 8.7685F, -8.5002F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(8.9307F, -2.1379F, -10.8477F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(15.5393F, 18.7339F, 0.5778F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(-2.1235F, 2.4307F, -8.8647F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(13.025F, 23.1924F, -9.603F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(9.4693F, 12.0621F, 9.4254F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-11.3869F, -9.0127F, 4.8553F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(18.5972F, 8.9983F, 5.5833F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(3.0844F, 15.7111F, 6.0577F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw5", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(-7.2122F, -3.7239F, -6.3338F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(4.4685F, -12.6691F, -18.3519F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw6", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(17.766F, 0.2293F, -6.7318F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(7.3208F, 8.237F, 4.0265F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw7", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(11.8158F, -15.9767F, -2.4762F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(21.3532F, 0.9326F, -5.8088F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw8", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(2.7716F, 5.9106F, -17.6177F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-6.2405F, -5.3367F, 3.7885F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw9", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(15.3372F, -5.6853F, -7.5133F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-10.0836F, 24.8086F, 4.2577F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw10", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(5.6103F, -0.5468F, -2.0556F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(20.5016F, 16.2939F, -8.1034F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw11", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw12", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(8.5051F, 11.0451F, 6.2983F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-5.1217F, -5.5658F, 9.6304F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw12", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw13", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(-2.0118F, -0.9841F, -4.4704F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(25.0825F, 5.1989F, -5.3758F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw13", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw14", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(-1.1601F, -3.8632F, 3.5678F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(17.7125F, -8.1839F, -3.5103F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw14", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw15", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(-8.1044F, 4.7493F, -0.8736F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(2.9528F, -13.1322F, 3.4963F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw15", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw16", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(6.6849F, -5.9423F, 12.4097F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(5.8946F, 18.4645F, 3.0395F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw16", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9583F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-18.0281F, 12.2115F, -17.4891F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-25.0933F, 14.5751F, -9.3958F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(1.1698F, -12.6837F, -8.9306F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-15.5252F, -7.0898F, 10.2017F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(15.0109F, -1.9322F, 5.1903F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-6.015F, -1.223F, -28.313F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(4.2571F, 9.8641F, -14.9859F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-11.3817F, 21.4706F, 14.1739F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(-9.1318F, 3.1802F, 7.2454F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(3.9437F, 0.3023F, 5.9095F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw5", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(-11.0551F, 0.4174F, -12.313F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-6.3937F, 1.3574F, 5.0781F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw6", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-5.6466F, -0.8989F, -13.0118F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-21.703F, 4.4752F, 5.3859F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw7", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(11.3721F, -1.8412F, -9.396F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-12.6618F, 7.203F, -18.1768F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw8", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(-13.2472F, -9.9734F, -9.9016F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-17.7449F, -4.6361F, -10.4878F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw9", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-6.0587F, -4.0834F, -5.1592F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-11.9733F, -9.2356F, -6.7258F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw10", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(-20.1743F, 6.0643F, 1.1058F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-4.117F, -12.9898F, 3.2091F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw11", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw12", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(-15.3017F, 10.0271F, -1.8208F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-7.6544F, 10.0868F, -0.2258F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw12", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw13", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-17.1512F, 1.818F, -12.3098F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-12.8627F, 6.4667F, -2.9277F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw13", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw14", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(5.5039F, -6.041F, -4.818F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(2.2894F, -2.23F, -5.2398F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw14", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw15", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-10.3863F, 5.6397F, 2.3062F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-17.0425F, -0.1601F, 0.5485F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw15", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw16", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(4.5012F, -5.2636F, -13.1542F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-4.7253F, 6.2971F, -16.3636F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw16", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("layer", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("layer", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rlayer", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(-5.03F, 5.1908F, -0.3034F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rlayer", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("llayer", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(5.2314F, 2.3626F, 1.0249F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("llayer", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition RETURN = AnimationDefinition.Builder.withLength(1.3333F)
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-3.0947F, -22.2971F, 8.11F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-0.7F, 2.0F, -1.3F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-2.72F, 19.8217F, -7.9753F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.3216F, -2.9826F, 0.0253F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.4167F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-65.2071F, -24.0222F, 16.5766F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-28.4371F, 61.1766F, -32.4349F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.4583F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7917F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(25.0313F, -25.2519F, -11.2664F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(2.7502F, 7.3008F, 1.1133F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4583F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-28.0756F, 11.0311F, 19.734F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.375F, KeyframeAnimations.posVec(-0.7862F, 6.9555F, -0.049F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_left_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(25.0313F, -25.2519F, -11.2664F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(4.3165F, -4.5965F, 1.1133F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.2917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4583F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(0.0F, -47.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.625F, KeyframeAnimations.posVec(2.6933F, 3.2061F, -0.6835F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(5.3137F, 0.7283F, -10.9876F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.25F, KeyframeAnimations.posVec(-7.5076F, -0.9589F, -5.6317F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.25F, KeyframeAnimations.posVec(6.0696F, 0.8589F, -9.2402F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.posVec(-7.6694F, -0.9502F, -3.5038F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-6.8838F, -3.9353F, 6.2841F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.3333F, KeyframeAnimations.posVec(1.3579F, -2.8451F, 2.8392F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw2", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0833F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.375F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(1.5252F, 0.1556F, 9.2782F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.posVec(6.0422F, 0.7455F, 1.9839F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(20.2846F, 8.7685F, -8.5002F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.6667F, KeyframeAnimations.posVec(6.3849F, -5.2768F, -11.5494F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(15.5393F, 18.7339F, 0.5778F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.posVec(8.9307F, -2.1379F, -10.8477F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw2", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(13.025F, 23.1924F, -9.603F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.posVec(-2.1235F, 2.4307F, -8.8647F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-11.3869F, -9.0127F, 4.8553F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.posVec(9.4693F, 12.0621F, 9.4254F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw5", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(3.0844F, 15.7111F, 6.0577F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.posVec(18.5972F, 8.9983F, 5.5833F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw5", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(4.4685F, -12.6691F, -18.3519F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7917F, KeyframeAnimations.posVec(-7.2122F, -3.7239F, -6.3338F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw6", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.25F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw7", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(7.3208F, 8.237F, 4.0265F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.6667F, KeyframeAnimations.posVec(17.766F, 0.2293F, -6.7318F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw7", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw8", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(21.3532F, 0.9326F, -5.8088F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5833F, KeyframeAnimations.posVec(11.8158F, -15.9767F, -2.4762F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw8", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw9", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-6.2405F, -5.3367F, 3.7885F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5833F, KeyframeAnimations.posVec(2.7716F, 5.9106F, -17.6177F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw9", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw10", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-10.0836F, 24.8086F, 4.2577F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5833F, KeyframeAnimations.posVec(15.3372F, -5.6853F, -7.5133F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw10", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(20.5016F, 16.2939F, -8.1034F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.75F, KeyframeAnimations.posVec(5.6103F, -0.5468F, -2.0556F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw11", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw12", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-5.1217F, -5.5658F, 9.6304F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.6667F, KeyframeAnimations.posVec(8.5051F, 11.0451F, 6.2983F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw12", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(25.0825F, 5.1989F, -5.3758F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.posVec(-2.0118F, -0.9841F, -4.4704F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw13", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.2917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw14", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(17.7125F, -8.1839F, -3.5103F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7917F, KeyframeAnimations.posVec(-1.1601F, -3.8632F, 3.5678F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw14", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(2.9528F, -13.1322F, 3.4963F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5833F, KeyframeAnimations.posVec(-8.1044F, 4.7493F, -0.8736F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw15", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw16", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(5.8946F, 18.4645F, 3.0395F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5F, KeyframeAnimations.posVec(6.6849F, -5.9423F, 12.4097F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw16", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.2083F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-25.0933F, 14.5751F, -9.3958F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.posVec(-18.0281F, 12.2115F, -17.4891F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw2", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-15.5252F, -7.0898F, 10.2017F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.75F, KeyframeAnimations.posVec(1.1698F, -12.6837F, -8.9306F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw2", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-6.015F, -1.223F, -28.313F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.posVec(15.0109F, -1.9322F, 5.1903F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-11.3817F, 21.4706F, 14.1739F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.posVec(4.2571F, 9.8641F, -14.9859F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw5", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(3.9437F, 0.3023F, 5.9095F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5417F, KeyframeAnimations.posVec(-9.1318F, 3.1802F, 7.2454F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw5", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw6", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-6.3937F, 1.3574F, 5.0781F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.posVec(-11.0551F, 0.4174F, -12.313F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw6", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw7", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-21.703F, 4.4752F, 5.3859F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.75F, KeyframeAnimations.posVec(-5.6466F, -0.8989F, -13.0118F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw7", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw8", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-12.6618F, 7.203F, -18.1768F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5833F, KeyframeAnimations.posVec(11.3721F, -1.8412F, -9.396F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw8", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw9", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-17.7449F, -4.6361F, -10.4878F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5833F, KeyframeAnimations.posVec(-13.2472F, -9.9734F, -9.9016F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw9", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw10", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-11.9733F, -9.2356F, -6.7258F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.6667F, KeyframeAnimations.posVec(-6.0587F, -4.0834F, -5.1592F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw10", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw11", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-4.117F, -12.9898F, 3.2091F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.posVec(-20.1743F, 6.0643F, 1.1058F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw11", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.2917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.5417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw12", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-7.6544F, 10.0868F, -0.2258F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7917F, KeyframeAnimations.posVec(-15.3017F, 10.0271F, -1.8208F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw12", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw13", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-12.8627F, 6.4667F, -2.9277F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.6667F, KeyframeAnimations.posVec(-17.1512F, 1.818F, -12.3098F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw13", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw14", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(2.2894F, -2.23F, -5.2398F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.7083F, KeyframeAnimations.posVec(5.5039F, -6.041F, -4.818F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw14", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw15", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-17.0425F, -0.1601F, 0.5485F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.6667F, KeyframeAnimations.posVec(-10.3863F, 5.6397F, 2.3062F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw15", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw16", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.1667F, KeyframeAnimations.posVec(-4.7253F, 6.2971F, -16.3636F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.75F, KeyframeAnimations.posVec(4.5012F, -5.2636F, -13.1542F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw16", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("layer", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("layer", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.2917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rlayer", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(1.0417F, KeyframeAnimations.posVec(-5.03F, 5.1908F, -0.3034F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rlayer", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(1.0417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("llayer", new AnimationChannel(AnimationChannel.Targets.POSITION,
			new Keyframe(0.75F, KeyframeAnimations.posVec(5.2314F, 2.3626F, 1.0249F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("llayer", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.75F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition REACH = AnimationDefinition.Builder.withLength(1.3333F)
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-3.0947F, -22.2971F, 8.11F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-0.7F, 2.0F, -1.3F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.72F, 19.8217F, -7.9753F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(0.3216F, -2.9826F, 0.0253F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-88.3606F, 23.6037F, -5.2754F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(4.4471F, -5.4663F, -1.5307F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-28.4371F, 61.1766F, -32.4349F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_right_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(25.0313F, -25.2519F, -11.2664F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(2.7502F, 7.3008F, 1.1133F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-28.0756F, 11.0311F, 19.734F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, KeyframeAnimations.posVec(-0.7862F, 6.9555F, -0.049F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_left_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(25.0313F, -25.2519F, -11.2664F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(4.3165F, -4.5965F, 1.1133F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, -47.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(2.6933F, 3.2061F, -0.6835F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.posVec(-7.5076F, -0.9589F, -5.6317F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(5.3137F, 0.7283F, -10.9876F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(-7.6694F, -0.9502F, -3.5038F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.posVec(6.0696F, 0.8589F, -9.2402F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(1.3579F, -2.8451F, 2.8392F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-6.8838F, -3.9353F, 6.2841F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0833F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.posVec(6.0422F, 0.7455F, 1.9839F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(1.5252F, 0.1556F, 9.2782F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("jigsaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(-2.1235F, 2.4307F, -8.8647F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(13.025F, 23.1924F, -9.603F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(9.4693F, 12.0621F, 9.4254F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-11.3869F, -9.0127F, 4.8553F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(17.766F, 0.2293F, -6.7318F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(7.3208F, 8.237F, 4.0265F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw7", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(11.8158F, -15.9767F, -2.4762F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(21.3532F, 0.9326F, -5.8088F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rjigsaw8", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-18.0281F, 12.2115F, -17.4891F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-25.0933F, 14.5751F, -9.3958F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(1.1698F, -12.6837F, -8.9306F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-15.5252F, -7.0898F, 10.2017F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(15.0109F, -1.9322F, 5.1903F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-6.015F, -1.223F, -28.313F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(4.2571F, 9.8641F, -14.9859F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-11.3817F, 21.4706F, 14.1739F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw4", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(-9.1318F, 3.1802F, 7.2454F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(3.9437F, 0.3023F, 5.9095F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw5", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(-11.0551F, 0.4174F, -12.313F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-6.3937F, 1.3574F, 5.0781F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw6", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-5.6466F, -0.8989F, -13.0118F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-21.703F, 4.4752F, 5.3859F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw7", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(11.3721F, -1.8412F, -9.396F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-12.6618F, 7.203F, -18.1768F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw8", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(-13.2472F, -9.9734F, -9.9016F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-17.7449F, -4.6361F, -10.4878F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw9", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-6.0587F, -4.0834F, -5.1592F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-11.9733F, -9.2356F, -6.7258F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw10", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(-20.1743F, 6.0643F, 1.1058F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-4.117F, -12.9898F, 3.2091F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw11", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw12", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(-15.3017F, 10.0271F, -1.8208F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-7.6544F, 10.0868F, -0.2258F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw12", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw13", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-17.1512F, 1.818F, -12.3098F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-12.8627F, 6.4667F, -2.9277F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw13", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw14", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(5.5039F, -6.041F, -4.818F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(2.2894F, -2.23F, -5.2398F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw14", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw15", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-10.3863F, 5.6397F, 2.3062F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-17.0425F, -0.1601F, 0.5485F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw15", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw16", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(4.5012F, -5.2636F, -13.1542F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-4.7253F, 6.2971F, -16.3636F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ljigsaw16", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("layer", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("layer", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rlayer", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(-5.03F, 5.1908F, -0.3034F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("rlayer", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("llayer", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(5.2314F, 2.3626F, 1.0249F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("llayer", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
        public static final AnimationDefinition HIDE_HORNS = AnimationDefinition.Builder.withLength(0.0F)
                .addAnimation("rjigsaw10", new AnimationChannel(AnimationChannel.Targets.SCALE,
                        new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("ram_l", new AnimationChannel(AnimationChannel.Targets.SCALE,
                        new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("horn_l", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("horn_r", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("ram_r", new AnimationChannel(AnimationChannel.Targets.SCALE,
                        new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
}
