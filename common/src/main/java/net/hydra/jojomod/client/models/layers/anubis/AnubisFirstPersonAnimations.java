package net.hydra.jojomod.client.models.layers.anubis;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.util.Mth;

public class AnubisFirstPersonAnimations {

    public static final AnimationDefinition Unsheath = AnimationDefinition.Builder.withLength(2.0F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,// +x forward, +y up, +z left, has to be large
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1, KeyframeAnimations.posVec(0, 0.75F, 20), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0, 0.75F, 20), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1, KeyframeAnimations.posVec(-75 * Mth.DEG_TO_RAD, 0, 50 * Mth.DEG_TO_RAD), AnimationChannel.Interpolations.LINEAR)
            ))

            .addAnimation("sheath", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0, 0.0F, -6.5F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Block = AnimationDefinition.Builder.withLength(0F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0F, KeyframeAnimations.degreeVec(-72.5926F, 43.6575F, -77.7873F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0F, KeyframeAnimations.posVec(-4.0F, 2.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition BarrageCharge = AnimationDefinition.Builder.withLength(2.0F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-53.6366F, 37.7244F, -2.3113F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2917F * 2, KeyframeAnimations.posVec(0.0F, 0.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, -3.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition BarrageDash = AnimationDefinition.Builder.withLength(0.6F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-53.6366F, 37.7244F, -2.3113F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(9.5697F, -57.9409F, -84.535F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(9.5697F, -57.9409F, -84.535F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, -0.0F, -0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.0F, -16.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();


    public static final AnimationDefinition Shieldbreak = AnimationDefinition.Builder.withLength(1.0F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-0.5F, -9.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition ShieldbreakHit = AnimationDefinition.Builder.withLength(0.6667F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, -9.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(3.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(3.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition ItemUnsheath = AnimationDefinition.Builder.withLength(2.0F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 5, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("sheath", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();


    public static final AnimationDefinition Attack = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(026.341F, 23.9275F, 39.3227F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(-26.6577F, -22.3816F, -37.2048F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.46F, KeyframeAnimations.degreeVec(-26.6577F, -22.3816F, -37.2048F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)

            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.46F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),

                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Attack2 = AnimationDefinition.Builder.withLength(0.4167F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(40.8423F, -22.3816F, 50.2048F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-6.8751F, 33.356F, -13.6866F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.0F, 4.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Attack3 = AnimationDefinition.Builder.withLength(0.65F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-6.8751F, 33.356F, -13.6866F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 57.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.244F, KeyframeAnimations.degreeVec(-12.003F, 5.7134F, -82.1412F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.460F, KeyframeAnimations.degreeVec(-12.003F, 5.7134F, -82.1412F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.65F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.244F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.460F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.65F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

  /*  public static final AnimationDefinition Attack2 = AnimationDefinition.Builder.withLength(0.45F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-26.6577F, -22.3816F, -37.2048F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-46.6577F, -22.3816F, 50.2048F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(55.6817F, -29.1526F, -83.0422F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.42F, KeyframeAnimations.degreeVec(55.6817F, -29.1526F, -83.0422F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(-32.1592F, -14.5763F, -41.5211F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),

                    new Keyframe(0.42F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build(); */


    public static final AnimationDefinition Pogo = AnimationDefinition.Builder.withLength(1.375F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-32.2661F, -2.8704F, -38.2897F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-53.6366F, 37.7244F, -2.3113F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.524F, KeyframeAnimations.degreeVec(-53.6366F, 37.7244F, -2.3113F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.65F, KeyframeAnimations.degreeVec(9.5697F, -57.9409F, -84.535F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0667F, KeyframeAnimations.degreeVec(9.57F, -57.94F, -84.54F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.275F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.5F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, -3.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.524F, KeyframeAnimations.posVec(0.0F, -3.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.65F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition Cleave = AnimationDefinition.Builder.withLength(1.75F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(55.9344F, -19.7426F, 23.0578F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.degreeVec(85.56F, 23.34F, -7.94F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(66.0476F, 100, -21.5581F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(78.55F, 130, -14.06F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3333F, KeyframeAnimations.degreeVec(78.55F, 130, -14.06F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5823F, KeyframeAnimations.degreeVec(78.55F, 130, -14.06F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5833F, KeyframeAnimations.degreeVec(78.55F, 100, -14.06F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6667F, KeyframeAnimations.degreeVec(104.28F, 80, 22.97F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0833F, KeyframeAnimations.posVec(-2.0F, -9.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(0.13F, -6.0F, -8.37F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1667F, KeyframeAnimations.posVec(-2.0F, -4.0F, -25), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2083F, KeyframeAnimations.posVec(-3, 0.0F, -32), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.posVec(-2, 0.0F, -35), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3333F, KeyframeAnimations.posVec(-1, 0.0F, -35), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5823F, KeyframeAnimations.posVec(0.0F, 0.0F, -35), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, -25), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6667F, KeyframeAnimations.posVec(0.0F, -5.0F, -10), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition UppercutCharge = AnimationDefinition.Builder.withLength(1.2917F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(3.8726F, -14.8205F, -78.2501F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(3.8726F, -14.8205F, -78.2501F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-6.00F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-6.00F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(-6.00F, 2.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(-6.00F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-6.00F, 2.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8333F, KeyframeAnimations.posVec(-6.00F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.posVec(-6.00F, 2.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-6.00F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0833F, KeyframeAnimations.posVec(-6.00F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1667F, KeyframeAnimations.posVec(-6.00F, 2.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.posVec(-6.00F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition UppercutRelease = AnimationDefinition.Builder.withLength(0.25F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.8726F, -14.8205F, -78.2501F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(11.3137F, -11.9738F, -6.2109F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(11.3137F, -11.9738F, -6.2109F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(-2.69F, 0.0F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(7.0F, -7.0F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(7.0F, -7.0F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.425F, KeyframeAnimations.degreeVec(0, -3.5F, 0), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM)


            ))
            .build();


    public static final AnimationDefinition SwordSpin = AnimationDefinition.Builder.withLength(1.5F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-74.8563F, -34.0544F, -8.6179F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5694F, KeyframeAnimations.degreeVec(-74.86F, -186.55F, -8.62F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-94.86F, -281.55F, -8.62F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6806F, KeyframeAnimations.degreeVec(-87.36F, -366.55F, 1.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7361F, KeyframeAnimations.degreeVec(-107.36F, -426.55F, 16.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, KeyframeAnimations.degreeVec(-267.43F, -426.55F, 189.13F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8611F, KeyframeAnimations.degreeVec(-74.86F, -186.55F, -8.62F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.degreeVec(-94.86F, -281.55F, -8.62F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9722F, KeyframeAnimations.degreeVec(-87.36F, -366.55F, 1.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0278F, KeyframeAnimations.degreeVec(-107.36F, -426.55F, 16.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-267.43F, -426.55F, 189.13F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1389F, KeyframeAnimations.degreeVec(-74.86F, -186.55F, -8.62F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1944F, KeyframeAnimations.degreeVec(-94.86F, -281.55F, -8.62F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.degreeVec(-87.36F, -366.55F, 1.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3056F, KeyframeAnimations.degreeVec(-107.36F, -426.55F, 16.38F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3611F, KeyframeAnimations.degreeVec(-267.43F, -452.02F, 189.13F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5139F, KeyframeAnimations.degreeVec(-360.0F, -360.0F, 360.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5694F, KeyframeAnimations.posVec(-17.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.posVec(-22.0F, 0.0F, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6806F, KeyframeAnimations.posVec(-14.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7361F, KeyframeAnimations.posVec(3.0F, 0.0F, -28.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7917F, KeyframeAnimations.posVec(2.56F, 0.0F, -13.87F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8611F, KeyframeAnimations.posVec(-17.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9167F, KeyframeAnimations.posVec(-22.0F, 0.0F, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9722F, KeyframeAnimations.posVec(-14.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0278F, KeyframeAnimations.posVec(3.0F, 0.0F, -28.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0833F, KeyframeAnimations.posVec(2.56F, 0.0F, -13.87F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1389F, KeyframeAnimations.posVec(-17.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1944F, KeyframeAnimations.posVec(-22.0F, 0.0F, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.posVec(-14.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3056F, KeyframeAnimations.posVec(3.0F, 0.0F, -28.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3611F, KeyframeAnimations.posVec(2.56F, 2.0F, -13.87F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5139F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition Flurry = AnimationDefinition.Builder.withLength(1.75F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(-16.2302F, 4.1228F, -79.5993F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-16.2302F, 4.1228F, -79.5993F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.4406F, 3.7492F, -87.2032F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.4406F, 3.7492F, -87.2032F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.625F, KeyframeAnimations.degreeVec(-13.9211F, 9.3754F, -60.1583F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(3.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5417F, KeyframeAnimations.posVec(3.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.625F, KeyframeAnimations.posVec(-5.00F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Stab = AnimationDefinition.Builder.withLength(1.375F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(8.6817F, -25.3117F, -116.0283F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5417F, KeyframeAnimations.degreeVec(0.1008F, -26.6659F, -96.5992F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.1008F, -26.6659F, -96.5992F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.625F, KeyframeAnimations.degreeVec(-15.1555F, -12.6956F, -95.8338F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.degreeVec(11.7877F, 14.7862F, -86.6933F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.999F, KeyframeAnimations.degreeVec(11.7877F, 14.7862F, -86.6933F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(11.7877F, 14.7862F, -86.6933F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(-12.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(-12.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(-9.33F, -0.11F, -0.89F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7083F, KeyframeAnimations.posVec(-3.0F, -4.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-4.0F, -4.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0407F, KeyframeAnimations.posVec(-4.0F, -4.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0417F, KeyframeAnimations.posVec(-4.0F, -4.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.125F, KeyframeAnimations.posVec(-4.0F, -4.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.25F, KeyframeAnimations.posVec(3.0F, -4.0F, -1.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition SwordThrow = AnimationDefinition.Builder.withLength(1.0F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 70.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.7F, KeyframeAnimations.posVec(-7.0F, -11.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.posVec(6.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Launch = AnimationDefinition.Builder.withLength(0.5833F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(55.9344F, -19.7426F, 23.0578F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(85.56F, 23.34F, -7.94F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(66.0476F, 124.3449F, -21.5581F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(78.55F, 156.84F, -14.06F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(78.55F, 156.84F, -14.06F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4157F, KeyframeAnimations.degreeVec(78.55F, 156.84F, -14.06F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(78.55F, 121.84F, -14.06F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(104.28F, 108.42F, 22.97F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(-6.0F, -9.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.13F, -6.0F, -8.37F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-2.0F, -4.0F, -25), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2083F, KeyframeAnimations.posVec(-4.82F, 0.0F, -32), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-4.0F, 0.0F, -35), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(-4.0F, 0.0F, -35), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4157F, KeyframeAnimations.posVec(-4.0F, 0.0F, -35), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(-4.0F, 0.0F, -25), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-2.0F, -5.0F, -10), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();


}
