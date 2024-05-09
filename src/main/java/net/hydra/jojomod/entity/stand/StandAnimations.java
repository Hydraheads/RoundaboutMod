package net.hydra.jojomod.entity.stand;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class StandAnimations {
    public static final Animation STAND_IDLE_FLOAT = net.minecraft.client.render.entity.animation.Animation.Builder.create(3.0f).looping()
            .addBoneAnimation("stand2",
                    new Transformation(Transformation.Targets.TRANSLATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("right_arm", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.0698F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0698F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(3.0F, AnimationHelper.createTranslationalVector(-0.0698F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("lower_right_arm", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.2967F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(-0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(3.0F, AnimationHelper.createTranslationalVector(-0.2967F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("left_arm", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.0698F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0698F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(3.0F, AnimationHelper.createTranslationalVector(-0.0698F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("lower_left_arm", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.2617F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(-0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(3.0F, AnimationHelper.createTranslationalVector(-0.2617F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("left_leg", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.3054F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(-0.3926F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(3.0F, AnimationHelper.createTranslationalVector(-0.3054F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("lower_left_leg", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.7417F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("right_leg", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.2617F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.1308F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.2617F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("lower_right_leg", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.6544F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .build();

}
