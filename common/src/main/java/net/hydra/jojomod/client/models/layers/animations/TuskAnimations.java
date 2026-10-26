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


    public static final AnimationDefinition LEFT_ARM_ENTER = AnimationDefinition.Builder.withLength(0.5833F)
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(-117.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-190.2473F, -78.8834F, 94.4265F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(-262.2685F, -23.8242F, 177.3558F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-214.9576F, 75.0655F, 228.1283F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(-9.1894F, 36.6643F, 406.7195F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(0.0F, -20.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(-5.0F, -20.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(-5.0F, -22.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-3.0F, -22.56F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(-1.0F, -24.56F, 4.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5556F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-90.0F, -65.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(90.0F, -27.5F, -180.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(102.5F, 37.5F, -180.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(0.7562F, 60.825F, -246.6464F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(0.0F, -19.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(-5.0F, -21.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(-5.0F, -22.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-3.0F, -22.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(-1.0F, -25.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-74.4644F, -66.5534F, -8.6931F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-171.7097F, -39.2732F, 77.0371F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(-177.9033F, -39.9541F, 86.7378F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-147.5284F, -87.0056F, 55.6485F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(-266.7461F, -39.0635F, 176.1037F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-282.4276F, 10.8803F, 178.6416F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-399.6141F, 70.4514F, 129.8326F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(1.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(1.0F, -17.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(-6.0F, -19.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(-13.0F, -19.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-13.0F, -19.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-6.0F, -25.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(-5.0F, -25.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.scaleVec(0.7F, 0.8F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-149.3966F, -44.057F, 93.1966F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0278F, KeyframeAnimations.degreeVec(-109.9972F, 19.9618F, 41.6901F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(-81.7808F, 13.9955F, -14.4327F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-99.2808F, 13.9955F, -14.4327F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(-100.2031F, -27.9464F, -7.3478F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-260.4778F, -18.929F, 164.7158F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(-317.7546F, 64.806F, 143.9825F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(1.0F, -16.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(1.0F, -19.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(5.0F, -20.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(3.0F, -20.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(0.0F, -21.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(90.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(50.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-80.0091F, -9.7024F, -3.4542F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(65.4653F, -65.6804F, -159.1685F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(58.3901F, 31.8161F, -187.9355F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(115.89F, 126.82F, -175.44F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -10.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(0.0F, -5.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(6.0F, -8.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(-2.0F, -8.0F, 18.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-15.0F, -11.0F, 11.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(-13.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(90.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(50.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-72.659F, 31.7843F, 23.6093F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(-80.5831F, -40.3478F, 1.4145F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(71.8021F, -29.0179F, -168.4587F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(-83.6223F, 64.272F, -381.7462F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -10.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(0.0F, -5.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(9.0F, -9.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(4.0F, -9.0F, 16.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-6.0F, -10.0F, 13.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(-9.0F, -6.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition LEFT_ARM_EXIT = AnimationDefinition.Builder.withLength(0.2917F)
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-9.1894F, 36.6643F, 406.7195F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1458F, KeyframeAnimations.degreeVec(-112.1045F, 16.5966F, 364.8901F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(33.1717F, 3.7739F, 355.0172F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 360.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -24.56F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1458F, KeyframeAnimations.posVec(-1.0F, -20.56F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -9.5F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.7562F, 60.825F, -246.6464F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-95.0494F, 61.1403F, -344.6036F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.2865F, 0.0F, -360.0238F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -360.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, -25.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(-1.27F, -23.81F, 2.94F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -9.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-170.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-170.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.degreeVec(-190.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-74.8765F, -7.2431F, -1.9515F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-13.2746F, -19.5065F, 4.5044F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -25.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(-5.0F, -22.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.posVec(-5.12F, -21.12F, -0.81F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-4.0F, -18.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -13.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.scaleVec(0.5F, 0.1F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.scaleVec(0.6F, 0.7F, 0.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-317.7546F, 64.806F, 143.9825F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-499.7286F, 42.6248F, -29.31F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.degreeVec(-494.9179F, -7.692F, -17.0727F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-370.2389F, 27.0772F, 5.3697F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-360.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -21.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.posVec(0.0F, -17.0F, 4.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -13.0F, -6.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(115.89F, 126.82F, -175.44F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.degreeVec(280.0126F, 15.9796F, -0.7289F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(310.0914F, 10.9937F, 12.3734F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(416.4836F, -11.2277F, 11.4675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(360.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-13.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.posVec(-11.0F, -7.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-2.79F, -5.0F, -9.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(2.0F, -7.0F, -13.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-83.6223F, 64.272F, -381.7462F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.degreeVec(-196.3712F, 77.534F, -491.834F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-68.8245F, 27.9856F, -370.4938F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(12.8264F, 5.754F, -361.9525F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -360.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-9.0F, -6.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.posVec(-8.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-4.0F, -6.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(-1.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition RIGHT_ARM_ENTER = AnimationDefinition.Builder.withLength(0.5833F)
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(-117.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-190.2473F, 78.8834F, -94.4265F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(-262.2685F, 23.8242F, -177.3558F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-214.9576F, -75.0655F, -228.1283F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(-9.1894F, -36.6643F, -406.7195F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(0.0F, -20.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(5.0F, -20.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(5.0F, -22.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(3.0F, -22.56F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(1.0F, -24.56F, 4.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-90.0F, 65.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(90.0F, 27.5F, 180.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(102.5F, -37.5F, 180.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(0.7562F, -60.825F, 246.6464F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(0.0F, -19.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(5.0F, -21.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(5.0F, -22.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(3.0F, -22.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(1.0F, -25.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-149.3966F, 44.057F, -93.1966F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0278F, KeyframeAnimations.degreeVec(-109.9972F, -19.9618F, -41.6901F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(-81.7808F, -13.9955F, 14.4327F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-99.2808F, -13.9955F, 14.4327F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(-100.2031F, 27.9464F, 7.3478F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-260.4778F, 18.929F, -164.7158F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(-317.7546F, -64.806F, -143.9825F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(-1.0F, -16.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(-1.0F, -19.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(-5.0F, -20.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-3.0F, -20.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(0.0F, -21.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-74.4644F, 66.5534F, 8.6931F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-171.7097F, 39.2732F, -77.0371F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(-177.9033F, 39.9541F, -86.7378F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-147.5284F, 87.0056F, -55.6485F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(-266.7461F, 39.0635F, -176.1037F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-282.4276F, -10.8803F, -178.6416F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-399.6141F, -70.4514F, -129.8326F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-1.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(-1.0F, -17.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(6.0F, -19.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(13.0F, -19.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(13.0F, -19.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(6.0F, -25.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(5.0F, -25.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.4167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.scaleVec(0.7F, 0.8F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(90.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(50.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-72.659F, -31.7843F, -23.6093F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(-80.5831F, 40.3478F, -1.4145F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(71.8021F, 29.0179F, 168.4587F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(-83.6223F, -64.272F, 381.7462F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -10.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(0.0F, -5.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(-9.0F, -9.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(-4.0F, -9.0F, 16.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(6.0F, -10.0F, 13.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(9.0F, -6.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(90.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.degreeVec(50.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.degreeVec(-80.0091F, 9.7024F, 3.4542F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.degreeVec(65.4653F, 65.6804F, 159.1685F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(58.3901F, -31.8161F, 187.9355F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.degreeVec(115.89F, -126.82F, 175.44F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, -10.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2222F, KeyframeAnimations.posVec(0.0F, -5.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(-6.0F, -8.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3611F, KeyframeAnimations.posVec(2.0F, -8.0F, 18.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(15.0F, -11.0F, 11.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5556F, KeyframeAnimations.posVec(13.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition RIGHT_ARM_EXIT = AnimationDefinition.Builder.withLength(0.2917F)
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-9.1894F, -36.6643F, -406.7195F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1458F, KeyframeAnimations.degreeVec(-112.1045F, -16.5966F, -364.8901F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(33.1717F, -3.7739F, -355.0172F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -360.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -24.56F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1458F, KeyframeAnimations.posVec(1.0F, -20.56F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -9.5F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 0.9F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.7562F, -60.825F, 246.6464F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-95.0494F, -61.1403F, 344.6036F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.2865F, 0.0F, 360.0238F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 360.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -25.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(1.27F, -23.81F, 2.94F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -9.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-317.7546F, -64.806F, -143.9825F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-499.7286F, -42.6248F, 29.31F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.degreeVec(-494.9179F, 7.692F, 17.0727F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-370.2389F, -27.0772F, -5.3697F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(-360.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -21.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.posVec(0.0F, -17.0F, 4.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -13.0F, -6.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-170.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-170.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.degreeVec(-190.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-74.8765F, 7.2431F, 1.9515F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-13.2746F, 19.5065F, -4.5044F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(5.0F, -25.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(5.0F, -22.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.posVec(5.12F, -21.12F, -0.81F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(4.0F, -18.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -13.0F, -6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.scaleVec(0.5F, 0.1F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.scaleVec(0.6F, 0.7F, 0.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-83.6223F, -64.272F, 381.7462F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.degreeVec(-196.3712F, -77.534F, 491.834F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-68.8245F, -27.9856F, 370.4938F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(12.8264F, -5.754F, 361.9525F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 360.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(9.0F, -6.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.posVec(8.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(4.0F, -6.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(1.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(115.89F, -126.82F, 175.44F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.degreeVec(280.0126F, -15.9796F, 0.7289F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(310.0914F, -10.9937F, -12.3734F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.degreeVec(416.4836F, 11.2277F, -11.4675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(360.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(13.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1042F, KeyframeAnimations.posVec(11.0F, -7.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(2.79F, -5.0F, -9.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(-2.0F, -7.0F, -13.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition FLATTEN = AnimationDefinition.Builder.withLength(2.0571F)
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 32.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0571F, KeyframeAnimations.degreeVec(-21.1975F, 25.2308F, -42.2963F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.degreeVec(-68.6975F, 25.2308F, -42.2963F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.degreeVec(-117.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.degreeVec(-190.2473F, -78.8834F, 94.4265F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.degreeVec(-262.2685F, -23.8242F, 177.3558F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.degreeVec(-303.3059F, 30.5279F, 181.3368F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(-334.1259F, 66.4171F, 199.3002F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.degreeVec(-182.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.degreeVec(-182.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.degreeVec(-218.26F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.posVec(0.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.posVec(0.0F, -20.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.posVec(-5.0F, -20.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.posVec(-5.0F, -22.56F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.posVec(-3.0F, -22.56F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(-1.0F, -23.56F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.posVec(0.0F, -23.56F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.posVec(0.0F, -18.7F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.posVec(0.0F, -9.58F, 10.56F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.2286F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.scaleVec(0.7F, 1.0F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.3F, 0.5F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.scaleVec(0.5F, 1.1F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.degreeVec(-90.0F, -65.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.degreeVec(90.0F, -27.5F, -180.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.degreeVec(102.5F, 37.5F, -180.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(31.8161F, 64.4622F, -217.9046F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.degreeVec(58.4337F, 73.74F, -133.4846F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.degreeVec(-177.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.degreeVec(-177.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.posVec(0.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.posVec(0.0F, -19.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.posVec(-5.0F, -21.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.posVec(-5.0F, -22.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.posVec(-3.0F, -22.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(-1.0F, -24.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.posVec(0.0F, -28.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.posVec(0.0F, -25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.posVec(0.0F, -25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.posVec(0.0F, -9.06F, 11.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2857F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.7F, 1.1F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.scaleVec(0.5F, 0.8F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5143F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.scaleVec(0.4F, 0.6F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-154.2993F, 31.7723F, -47.571F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0571F, KeyframeAnimations.degreeVec(-143.9945F, -18.747F, 23.8585F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.degreeVec(-171.7097F, -39.2732F, 77.0371F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.degreeVec(-177.9033F, -39.9541F, 86.7378F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.degreeVec(-147.5284F, -87.0056F, 55.6485F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.degreeVec(-266.7461F, -39.0635F, 176.1037F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.degreeVec(-282.4276F, 10.8803F, 178.6416F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(-420.984F, 53.8778F, 68.8833F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.degreeVec(-399.6141F, 70.4514F, 129.8326F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.degreeVec(-181.8438F, 1.6887F, 42.4728F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.degreeVec(-181.8438F, 1.6887F, 42.4728F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.degreeVec(-124.3218F, 0.2082F, 120.7045F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.degreeVec(-68.369F, -28.0039F, 7.8831F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0571F, KeyframeAnimations.posVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.posVec(1.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.posVec(1.0F, -17.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.posVec(-6.0F, -19.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.posVec(-13.0F, -19.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.posVec(-13.0F, -19.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(-9.0F, -19.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.posVec(-6.0F, -23.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.posVec(-4.0F, -19.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.posVec(-4.0F, -19.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.posVec(0.53F, -16.48F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.posVec(-2.29F, -9.41F, 7.67F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.3429F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.7F, 1.0F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5143F, KeyframeAnimations.scaleVec(0.4F, 0.4F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5714F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.scaleVec(0.7F, 0.4F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.scaleVec(0.8F, 0.8F, 0.8F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.degreeVec(-81.7808F, 13.9955F, -14.4327F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.degreeVec(-99.2808F, 13.9955F, -14.4327F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.degreeVec(-100.2031F, -27.9464F, -7.3478F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.degreeVec(-260.4778F, -18.929F, 164.7158F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(-304.0973F, 47.5386F, 177.8008F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.degreeVec(-250.8842F, 44.9134F, 272.2532F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5143F, KeyframeAnimations.degreeVec(-229.0772F, 69.2653F, 297.537F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.degreeVec(-181.8438F, -1.6887F, -42.4728F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.degreeVec(-181.8438F, -1.6887F, -42.4728F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.degreeVec(-124.3217F, -0.2083F, -120.7045F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.degreeVec(-68.369F, 28.0039F, -7.8831F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.posVec(1.0F, -16.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.posVec(1.0F, -19.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.posVec(5.0F, -20.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.posVec(3.0F, -20.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(3.0F, -19.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.posVec(6.0F, -23.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5143F, KeyframeAnimations.posVec(6.0F, -23.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.posVec(4.0F, -19.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.posVec(4.0F, -19.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.posVec(-0.53F, -16.48F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.posVec(2.29F, -9.41F, 7.67F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.3429F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.7F, 1.0F, 0.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.scaleVec(0.5F, 0.8F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5143F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.scaleVec(0.7F, 0.4F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8857F, KeyframeAnimations.scaleVec(0.8F, 0.8F, 0.8F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0571F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.degreeVec(90.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.degreeVec(50.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.degreeVec(-80.0091F, -9.7024F, -3.4542F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.degreeVec(65.4653F, -65.6804F, -159.1685F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.degreeVec(58.3901F, 31.8161F, -187.9355F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(93.3901F, 31.8161F, -187.9355F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.degreeVec(99.1797F, 71.6415F, -181.0044F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5143F, KeyframeAnimations.degreeVec(255.8976F, 25.6509F, 7.3118F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5714F, KeyframeAnimations.degreeVec(257.2411F, -3.3944F, 17.2135F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.degreeVec(257.2411F, -3.3944F, 17.2135F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.degreeVec(186.11F, -22.36F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.degreeVec(328.61F, -22.36F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.degreeVec(360.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0571F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.posVec(0.0F, -10.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.posVec(0.0F, -5.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.posVec(6.0F, -8.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.posVec(-2.0F, -8.0F, 18.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.posVec(-15.0F, -11.0F, 11.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(-15.0F, -7.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.posVec(-4.0F, -7.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5143F, KeyframeAnimations.posVec(-1.0F, -9.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5714F, KeyframeAnimations.posVec(-1.0F, -11.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.posVec(-1.0F, -11.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.posVec(-0.38F, 5.24F, -0.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(-0.15F, -0.36F, -0.09F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0571F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.degreeVec(90.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.degreeVec(50.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.degreeVec(-72.659F, 31.7843F, 23.6093F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.degreeVec(-80.5831F, -40.3478F, 1.4145F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.degreeVec(71.8021F, -29.0179F, -168.4587F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(86.6512F, -0.2709F, -177.4423F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.degreeVec(60.8788F, 59.5614F, -183.1733F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5714F, KeyframeAnimations.degreeVec(266.1098F, 22.3586F, 0.3008F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.degreeVec(266.1098F, 22.3586F, 0.3008F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8286F, KeyframeAnimations.degreeVec(186.11F, 22.36F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.degreeVec(328.61F, 22.36F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.degreeVec(360.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0571F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1143F, KeyframeAnimations.posVec(0.0F, -10.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1714F, KeyframeAnimations.posVec(0.0F, -5.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2286F, KeyframeAnimations.posVec(9.0F, -9.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2857F, KeyframeAnimations.posVec(4.0F, -9.0F, 16.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3429F, KeyframeAnimations.posVec(-6.0F, -10.0F, 13.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(-10.0F, -6.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4571F, KeyframeAnimations.posVec(1.0F, -6.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5143F, KeyframeAnimations.posVec(2.0F, -10.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5714F, KeyframeAnimations.posVec(1.0F, -10.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7714F, KeyframeAnimations.posVec(1.0F, -10.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9429F, KeyframeAnimations.posVec(0.38F, 5.24F, -0.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.15F, -0.43F, -0.15F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0571F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

}
