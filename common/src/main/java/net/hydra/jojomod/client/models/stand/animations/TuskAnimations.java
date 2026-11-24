package net.hydra.jojomod.client.models.stand.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class TuskAnimations {

    public static final AnimationDefinition ACT_1_IDLE = AnimationDefinition.Builder.withLength(5.0F).looping()
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.9931F, -0.2614F, -2.9886F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7083F, KeyframeAnimations.degreeVec(-3.495F, 0.1831F, 2.9943F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.5F, KeyframeAnimations.degreeVec(-1.9931F, -0.2614F, -2.9886F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-4.9931F, -0.2614F, -2.9886F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.4955F, 0.261F, 1.9829F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7083F, KeyframeAnimations.degreeVec(-0.5077F, 0.113F, -5.004F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.5F, KeyframeAnimations.degreeVec(-7.4955F, 0.261F, 1.9829F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-7.4955F, 0.261F, 1.9829F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 37.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 25.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 37.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5833F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 17.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2083F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 6.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -3.6181F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("NW", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(15.567F, 43.2195F, 16.2274F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0417F, KeyframeAnimations.degreeVec(-7.4365F, 44.5119F, -10.5468F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(15.567F, 43.2195F, 16.2274F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("NE", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.6035F, -36.3033F, -12.3157F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.75F, KeyframeAnimations.degreeVec(-7.5897F, -36.835F, 12.5307F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(10.6035F, -36.3033F, -12.3157F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("SW", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-6.2503F, -39.5899F, 9.7517F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.25F, KeyframeAnimations.degreeVec(-17.7154F, -42.0382F, -9.5657F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-6.2503F, -39.5899F, 9.7517F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("SE", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-4.5662F, 42.3005F, -6.7673F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.75F, KeyframeAnimations.degreeVec(-20.1143F, 40.9529F, -0.217F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-4.5662F, 42.3005F, -6.7673F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("top", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2917F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -9.79F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("bottom_left", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("bottom_right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition ACT_2_IDLE = AnimationDefinition.Builder.withLength(5.0F).looping()
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -1.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("connector", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-33.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ball", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(38.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(35.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-48.1627F, 56.4803F, 32.0124F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-54.16F, 56.48F, 32.01F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-48.1627F, 56.4803F, 32.0124F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -77.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-48.1627F, -56.4803F, -32.0124F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-54.16F, -56.48F, -32.01F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-48.1627F, -56.4803F, -32.0124F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 77.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition ACT_3_IDLE = AnimationDefinition.Builder.withLength(5.0F).looping()
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -1.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -0.4F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.7768F, 12.5649F, -6.3977F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(3.2768F, 12.5649F, -6.3977F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(10.7768F, 12.5649F, -6.3977F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -0.4F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.7768F, -12.5649F, 6.3977F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(3.2768F, -12.5649F, 6.3977F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(10.7768F, -12.5649F, 6.3977F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-5.0F, 17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-10.0F, 17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_leg2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, -17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(-5.0F, -17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(-10.0F, -17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_leg2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition Barrage = AnimationDefinition.Builder.withLength(0.231F).looping()
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(7.4976F, 0.109F, -2.4976F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(4.9976F, 0.109F, -2.4976F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(4.9976F, -0.109F, 2.4976F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(4.9976F, -0.109F, 2.4976F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.4142F, -2.944F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(0.0F, -1.4142F, -2.944F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(0.0F, -1.4142F, -2.944F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(0.4407F, -9.9904F, -2.5385F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(0.4407F, -9.9904F, -2.5385F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(0.4407F, 9.9904F, 2.5385F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(0.4407F, 9.9904F, 2.5385F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.6014F, -1.9074F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(0.0F, -0.6014F, -1.9074F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(0.0F, -0.6014F, -1.9074F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(12.6417F, -7.151F, -2.2671F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(13.8436F, -21.4058F, -7.1F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(12.6417F, 7.151F, 2.2671F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(13.8436F, 21.4058F, 7.1F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(28.0521F, 18.1816F, 76.9133F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(-77.5542F, 29.8548F, 81.679F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(-43.9844F, 29.6725F, 100.9452F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(28.0521F, 18.1816F, 76.9133F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(38.81F, 35.3491F, 64.0894F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(38.81F, 35.3491F, 64.0894F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(28.0521F, 18.1816F, 76.9133F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-0.5692F, -3.1141F, -0.2297F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.posVec(-0.7437F, -3.7056F, -3.5008F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.posVec(-0.7437F, -3.7056F, -1.5008F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(-0.5692F, -3.1141F, -0.2297F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-0.57F, -3.11F, -0.23F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.posVec(-0.57F, -3.11F, -0.23F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-0.5692F, -3.1141F, -0.2297F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(-97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(-96.6607F, -3.4553F, -27.2989F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(-96.6607F, -3.4553F, -27.2989F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(-97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(28.0521F, -18.1816F, -76.9133F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(38.81F, -35.3491F, -64.0894F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(38.81F, -35.3491F, -64.0894F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(28.0521F, -18.1816F, -76.9133F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(-77.5542F, -29.8548F, -81.679F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(-43.9844F, -29.6725F, -100.9452F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(28.0521F, -18.1816F, -76.9133F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.5692F, -3.1141F, -0.2297F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.posVec(0.57F, -3.11F, -0.23F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.posVec(0.57F, -3.11F, -0.23F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(0.5692F, -3.1141F, -0.2297F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(0.7437F, -3.7056F, -3.5008F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.posVec(0.7437F, -3.7056F, -1.5008F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(0.5692F, -3.1141F, -0.2297F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(-96.6607F, 3.4553F, 27.2989F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(-96.6607F, 3.4553F, 27.2989F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(-97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(-97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(1.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(0.0F, 1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(0.0F, 1.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 35.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(0.0208F, -9.7506F, -20.4015F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(0.0F, 1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-2.7511F, 34.1966F, -1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(4.3304F, -0.5254F, -31.8511F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-0.55F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.3651F, 0.5498F, -35.4395F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(0.0F, -5.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(0.0F, -5.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(-2.7511F, 34.1966F, -1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(2.3651F, 0.5498F, -35.4395F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.45F, -2.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(5.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(5.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(8.25F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(1.45F, -2.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.3827F, 0.9239F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.4242F, 2.1955F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(0.0208F, 9.7506F, 20.4015F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-6.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.posVec(-6.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(0.0F, 1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-6.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.3304F, 0.5254F, 31.8511F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(4.3304F, 0.5254F, 31.8511F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-0.45F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-4.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.posVec(-7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-0.45F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(2.3651F, -0.5498F, 35.4395F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.25F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-1.45F, -2.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-5.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-5.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.675F, 1.675F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition DeathPunch = AnimationDefinition.Builder.withLength(1.5086F)
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -360.5F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3592F, KeyframeAnimations.degreeVec(16.5491F, 50.0083F, 19.3692F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8621F, KeyframeAnimations.degreeVec(16.5491F, 50.0083F, 19.3692F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1494F, KeyframeAnimations.degreeVec(16.55F, 50.01F, 19.37F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(21.1305F, 59.4826F, 24.9589F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(24.8588F, -64.1855F, -16.0956F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 11.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1796F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4368F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5086F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1796F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.4368F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5086F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0718F, KeyframeAnimations.degreeVec(-180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1437F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2155F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2874F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3592F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8261F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.898F, KeyframeAnimations.degreeVec(-5.0F, -40.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(-5.0F, -40.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2572F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(-10.6279F, 54.0935F, -7.626F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 3.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0718F, KeyframeAnimations.posVec(0.0F, 6.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1437F, KeyframeAnimations.posVec(0.0F, 6.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2155F, KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2874F, KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3592F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8261F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.898F, KeyframeAnimations.posVec(5.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2213F, KeyframeAnimations.posVec(5.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2572F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.posVec(-5.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1796F, KeyframeAnimations.degreeVec(-45.6575F, -7.4711F, 95.0428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1494F, KeyframeAnimations.degreeVec(-45.6575F, -7.4711F, 95.0428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(-23.1575F, -7.4711F, 95.0428F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1796F, KeyframeAnimations.posVec(-1.9F, -1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1494F, KeyframeAnimations.posVec(-1.9F, -1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2213F, KeyframeAnimations.posVec(-1.9F, -1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("lower_right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1796F, KeyframeAnimations.degreeVec(-77.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(-77.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.467F, KeyframeAnimations.degreeVec(0.0F, 20.0F, -15.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0776F, KeyframeAnimations.degreeVec(0.0F, 20.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(-10.0F, 20.0F, -15.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(19.0417F, 24.4163F, -17.533F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("lower_left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.467F, KeyframeAnimations.degreeVec(-62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0776F, KeyframeAnimations.degreeVec(-62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(-72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3233F, KeyframeAnimations.degreeVec(-33.963F, 32.7512F, -8.8514F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("lower_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3233F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3233F, KeyframeAnimations.degreeVec(-40.0F, -32.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(-40.0F, -32.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(-35.5009F, 21.0062F, -38.6113F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("lower_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3233F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("front", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3233F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5029F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6825F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9698F, KeyframeAnimations.degreeVec(-4.12F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(-65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5086F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("back", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3233F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5029F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6825F, KeyframeAnimations.degreeVec(-0.31F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9698F, KeyframeAnimations.degreeVec(-2.85F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(57.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5086F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3233F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5029F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6825F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -16.89F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9698F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.16F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -15.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -65.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5086F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3233F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5029F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6825F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.99F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9698F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -4.95F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2213F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2931F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 47.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5086F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();


}

