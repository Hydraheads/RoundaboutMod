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
                    new Keyframe(1, KeyframeAnimations.posVec(-75* Mth.DEG_TO_RAD, 0,50*Mth.DEG_TO_RAD), AnimationChannel.Interpolations.LINEAR)
            ))

            .addAnimation("sheath", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(1F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0,0.0F, -6.5F), AnimationChannel.Interpolations.LINEAR)
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
                    new Keyframe(0.2917F*2, KeyframeAnimations.posVec(0.0F, 0.5F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
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


    public static final AnimationDefinition Attack = AnimationDefinition.Builder.withLength(0.45F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(026.341F, 23.9275F, 39.3227F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1875F, KeyframeAnimations.degreeVec(-26.6577F, -22.3816F, -37.2048F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(-26.6577F, -22.3816F, -37.2048F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)

            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.35F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition Attack2 = AnimationDefinition.Builder.withLength(0.45F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-26.6577F, -22.3816F, -37.2048F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-46.6577F, -22.3816F, 50.2048F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1875F, KeyframeAnimations.degreeVec(55.6817F, -29.1526F, -83.0422F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(55.6817F, -29.1526F, -83.0422F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(-32.1592F, -14.5763F, -41.5211F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),

                    new Keyframe(0.35F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition SneakAttack = AnimationDefinition.Builder.withLength(0.45F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-51.341F, 23.9275F, 39.3227F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1875F, KeyframeAnimations.degreeVec(-78.749F, -52.177F, 7.4436F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(-78.749F, -52.177F, 7.4436F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),

                    new Keyframe(0.35F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition SneakAttack2 = AnimationDefinition.Builder.withLength(0.55F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-78.749F, -52.177F, 7.4436F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.125F, KeyframeAnimations.degreeVec(-78.749F, -52.177F, 7.4436F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(40.8423F, -22.3816F, 50.2048F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3125F, KeyframeAnimations.degreeVec(65.6249F, 33.356F, -13.6866F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(65.6249F, 33.356F, -13.6866F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-32.1702F, 65.9679F, -124.2527F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.55F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3125F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),

                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -14.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.55F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition DoubleCut = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(55.6817F, 029.1526F, -83.0422F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.10415F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 57.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.14585F, KeyframeAnimations.degreeVec(-11.1689F, 7.2218F, -74.7471F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.29165F, KeyframeAnimations.degreeVec(-53.1606F, -34.7947F, 55.4174F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.35F, KeyframeAnimations.degreeVec(55.6817F, -29.1526F, -83.0422F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(55.6817F, -29.1526F, -83.0422F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.575F, KeyframeAnimations.degreeVec(-42.1592F, -14.5763F, -41.5211F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.65F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.10415F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.14585F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.29165F, KeyframeAnimations.posVec(0.0F, -4.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.35F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),

                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.65F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition Uppercut = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(55.6817F, -29.1526F, -83.0422F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(3.8726F, -14.8205F, -78.2501F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(11.3137F, -11.9738F, -6.2109F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(11.3137F, -11.9738F, -6.2109F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.55F, KeyframeAnimations.degreeVec(-26.6478F, -3.5478F, -1.8403F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-0.69F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(0.5F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.5F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)

            ))
            .build();

    public static final AnimationDefinition Thrust = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(65.6249F, 33.356F, -13.6866F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(2.1302F, 13.0106F, -84.1002F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(3.8364F, 12.6197F, -76.4188F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.433F, KeyframeAnimations.degreeVec(3.8364F, 12.6197F, -76.4188F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.633F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("stand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -30.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-7.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.posVec(1.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),

                    new Keyframe(0.433F, KeyframeAnimations.posVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.533F, KeyframeAnimations.posVec(-0.5F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.633F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

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
}
