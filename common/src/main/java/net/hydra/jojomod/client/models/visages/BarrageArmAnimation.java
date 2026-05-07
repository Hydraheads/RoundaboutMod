package net.hydra.jojomod.client.models.visages;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class BarrageArmAnimation {
    public static final AnimationDefinition BARRAGE = AnimationDefinition.Builder.withLength(0.2475F).looping()
            .addAnimation("BAM", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
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
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(12.5208F, -9.7506F, -20.4015F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(26.6937F, 3.2366F, -37.4267F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(15.5854F, 7.5957F, -67.603F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-1.0F, 1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(4.0F, -3.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(8.0F, -5.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.0522F, -35.0709F, -11.7525F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-2.7511F, 34.1966F, -1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(4.3304F, -0.5254F, -31.8511F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(3.3917F, -42.5591F, -12.2931F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.8F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.posVec(8.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-0.55F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(5.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(1.542F, 1.8758F, -72.9256F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(26.5118F, -8.5062F, -133.2966F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.degreeVec(4.3392F, 9.6759F, -151.791F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(0.0F, -5.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(-42.2337F, 56.3191F, -54.8646F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(1.542F, 1.8758F, -72.9256F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.45F, -4.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.posVec(5.35F, -2.54F, -17.56F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(9.41F, -0.41F, -20.67F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.posVec(14.38F, 2.45F, -26.74F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(13.38F, 2.45F, -17.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(9.25F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(0.45F, -4.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.0F, 2.0F,1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(28.0032F, 11.186F, 38.4243F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0165F, KeyframeAnimations.degreeVec(19.8415F, 8.9456F, 40.7804F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.degreeVec(-18.5432F, -33.1438F, 6.471F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.165F, KeyframeAnimations.degreeVec(6.0477F, 6.4309F, 18.359F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(24.4569F, 15.1619F, 31.8945F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-6.65F, -4.2F, -23.65F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0165F, KeyframeAnimations.posVec(-6.65F, -4.2F, -22.65F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.posVec(-7.0F, -3.0F, -18.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.posVec(-2.51F, 0.41F, 6.91F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(0.0F, 1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.posVec(-0.24F, -0.08F, -12.64F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-6.65F, -4.2F, -22.65F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.3304F, 0.5254F, 31.8511F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(22.52F, 26.2423F, 36.8892F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.degreeVec(22.0333F, 24.0203F, 37.2361F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.degreeVec(16.9867F, 21.1555F, 36.8328F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(8.1768F, 6.4647F, 48.1811F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(4.3304F, 0.5254F, 31.8511F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-0.45F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-6.0F, -2.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(-9.97F, -2.19F, -26.44F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-11.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.posVec(-7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-0.45F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM4", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.scaleVec(1.3821F, 0.0328F, 1.3821F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-58.2465F, -57.7261F, 88.5383F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(1.1928F, -2.1151F, 82.9288F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(43.9329F, -10.4314F, 152.308F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(5.0F, -10.0F, 100.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.25F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-1.45F, -6.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-10.0F, -2.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-14.0F, 1.0F, -22.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
}
