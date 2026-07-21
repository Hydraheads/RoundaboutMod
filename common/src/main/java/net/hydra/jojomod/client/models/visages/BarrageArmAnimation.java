package net.hydra.jojomod.client.models.visages;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class BarrageArmAnimation {
    public static final AnimationDefinition BARRAGE = AnimationDefinition.Builder.withLength(0.2475F).looping()
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
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(12.5208F, -9.7506F, -20.4015F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(26.8746F, -0.1469F, -30.7299F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(17.2589F, 1.1578F, -45.9684F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-1.0F, 3.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(4.0F, -1.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(8.0F, -3.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(6.5259F, -34.653F, -17.8258F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(2.537F, -10.0994F, -10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-2.7511F, 34.1966F, -1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(4.3304F, -0.5254F, -31.8511F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2145F, KeyframeAnimations.degreeVec(6.014F, -28.417F, -20.8183F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(7.9252F, -42.0654F, -19.0233F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.posVec(8.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(7.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-2.55F, -1.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2145F, KeyframeAnimations.posVec(-1.48F, -1.86F, -15.76F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(3.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM)
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
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.45F, -6.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.066F, KeyframeAnimations.posVec(6.35F, -4.54F, -17.56F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.posVec(15.38F, 0.45F, -26.74F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(13.38F, 0.45F, -17.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(9.25F, 3.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(0.45F, -5.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("RightArmBAM3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(1.0F, 2.0F,1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(26.4732F, 14.6229F, 31.584F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0165F, KeyframeAnimations.degreeVec(19.8415F, 8.9456F, 40.7804F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.degreeVec(2.537F, 10.0994F, 10.4428F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.degreeVec(-2.7511F, -34.1966F, 1.1775F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.degreeVec(-18.5432F, -33.1438F, 6.471F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.165F, KeyframeAnimations.degreeVec(6.0477F, 6.4309F, 18.359F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(23.1294F, 17.1802F, 27.1311F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-5.65F, -1.2F, -22.65F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0165F, KeyframeAnimations.posVec(-6.65F, -3.2F, -19.65F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.033F, KeyframeAnimations.posVec(-7.0F, -3.0F, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-7.0F, 0.0F, 12.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.099F, KeyframeAnimations.posVec(-2.51F, 2.41F, 6.91F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(0.0F, 2.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.posVec(-1.24F, 0.92F, -12.64F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-4.65F, -1.2F, -22.65F), AnimationChannel.Interpolations.CATMULLROM)
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
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.55F, -1.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-2.0F, -2.0F, -20.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1155F, KeyframeAnimations.posVec(-9.97F, -2.19F, -26.44F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-11.0F, -2.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1815F, KeyframeAnimations.posVec(-6.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(0.55F, -1.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM)
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
                    new Keyframe(0.066F, KeyframeAnimations.degreeVec(1.1928F, -2.1151F, 82.9288F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.degreeVec(44.7918F, 3.6258F, 138.0197F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.degreeVec(5.0F, -10.0F, 100.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-7.25F, 1.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0495F, KeyframeAnimations.posVec(-2.16F, -3.97F, 3.46F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.posVec(-2.45F, -7.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.posVec(-10.0F, -4.0F, -21.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.posVec(-15.0F, 0.0F, -23.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("LeftArmBAM3", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.0825F, KeyframeAnimations.scaleVec(1.0F, 2.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1485F, KeyframeAnimations.scaleVec(1.675F, 0.2F, 1.675F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.231F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
}
