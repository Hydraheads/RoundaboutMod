package net.hydra.jojomod.client.models.layers.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class TuskAnimations {

    public static final AnimationDefinition NAIL_FLOAT = AnimationDefinition.Builder.withLength(0.5417F).looping()
            .addAnimation("bb_main", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, -180.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("bb_main", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Default  = AnimationDefinition.Builder.withLength(0.1667F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(2.5804F, -10.9463F, 17.1861F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-2.8754F, -11.1437F, 16.1609F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Shooting = AnimationDefinition.Builder.withLength(0.1667F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Shockwave = AnimationDefinition.Builder.withLength(0.6667F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(342.7251F, 5.3851F, -272.816F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(350.7038F, -15.5646F, -348.2242F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-1.5889F, -9.8091F, 16.0317F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-20.6125F, -6.4323F, -86.8436F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-11.255F, 18.4991F, -12.3531F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(1.8904F, -10.1646F, 16.6711F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Shoot = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(342.0701F, -2.2861F, -296.6367F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(344.2062F, -8.8955F, -318.265F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-21.2682F, 3.5639F, -61.1838F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-18.4745F, 11.2986F, -39.8546F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition FireBothRight = AnimationDefinition.Builder.withLength(0.5417F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(342.0701F, -2.2861F, -296.6367F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(344.2062F, -8.8955F, -318.265F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(344.2062F, -8.8955F, -318.265F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(344.2F, -8.89F, -318.26F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5417F, KeyframeAnimations.posVec(8.7321F, -19.0175F, 18.1954F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-21.2682F, 3.5639F, -61.1838F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-18.4745F, 11.2986F, -39.8546F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition FireBothLeft = AnimationDefinition.Builder.withLength(0.5417F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(342.0701F, -2.2861F, -296.6367F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(344.2062F, -8.8955F, -318.265F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-21.2682F, 3.5639F, -61.1838F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-18.4745F, 11.2986F, -39.8546F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-18.4745F, 11.2986F, -39.8546F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-18.93F, 10.5F, -42.27F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-8.2959F, -19.4236F, 19.2224F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition RaiseLeft = AnimationDefinition.Builder.withLength(0.1667F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.93F, 10.5F, -42.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.2959F, -19.4236F, 19.2224F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition RaiseRight = AnimationDefinition.Builder.withLength(0.1667F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2F, -8.89F, -318.26F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.7321F, -19.0175F, 18.1954F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Extend = AnimationDefinition.Builder.withLength(0.1667F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(344.2F, -8.89F, -318.26F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-1.5644F, -18.5812F, 14.9967F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-18.93F, 10.5F, -42.27F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(1.9044F, -18.9512F, 15.7467F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition ExtendPush = AnimationDefinition.Builder.withLength(0.3333F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2F, -8.89F, -318.26F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(344.2F, -8.89F, -318.26F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(344.2F, -8.89F, -318.26F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.5644F, -18.5812F, 14.9967F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(-2.3956F, -20.3346F, 14.5276F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(1.36F, -12.4342F, 16.6556F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.93F, 10.5F, -42.27F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-18.93F, 10.5F, -42.27F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-18.93F, 10.5F, -42.27F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.9044F, -18.9512F, 15.7467F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(2.7356F, -20.7046F, 15.2776F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-1.02F, -12.8042F, 17.4056F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition Launch = AnimationDefinition.Builder.withLength(0.3333F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(355.1382F, -17.425F, -363.7519F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(7.2598F, -17.7002F, 17.8848F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-5.9334F, 20.7556F, 3.398F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(-6.8395F, -18.0989F, 18.8557F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition SpinThrow = AnimationDefinition.Builder.withLength(0.375F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(342.9431F, -6.0572F, -308.5882F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(345.9449F, -11.4738F, -328.0783F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(344.2048F, -8.8918F, -318.2615F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(-0.0959F, -13.681F, 16.0666F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(1.362F, -12.4383F, 16.6483F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-20.3913F, 7.1278F, -51.798F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-16.8924F, 13.5827F, -32.5406F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-18.9274F, 10.5F, -42.2695F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(0.3997F, -14.0373F, 16.7085F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(-1.0187F, -12.8081F, 17.3978F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}
