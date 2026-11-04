
package com.example.jjf.mixin;

import com.example.jjf.client.models.stand.JumpinJackFlashModel;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JumpinJackFlashModel.class, remap = false)
public class JJFSpinAnimationMixin {

    @Shadow @Final
    private ModelPart upper_chest;

    @Shadow @Final
    private ModelPart right_arm;

    @Shadow @Final
    private ModelPart left_arm;

    @Shadow @Final
    private ModelPart lower_right_arm;

    @Shadow @Final
    private ModelPart lower_left_arm;

    @Shadow @Final
    private ModelPart right_spin_on;

    @Shadow @Final
    private ModelPart left_spin_on;

    @Inject(method = "setupAnim", at = @At("TAIL"), remap = false)
    private void jjf$spinAnimation(
            FollowingStandEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        boolean spinning =
                entity.getAnimation() == StandEntity.BARRAGE_CHARGE;

        boolean releasing =
                entity.getAnimation() == StandEntity.BARRAGE_FINISHER;

        ((JumpinJackFlashModel<?>) (Object) this)
                .setCuffsSpinning(spinning || releasing);

        right_spin_on.yRot = 0.0F;
        left_spin_on.yRot = 0.0F;

        if (spinning) {
            float spin = ageInTicks * 1.6F;

            right_arm.xRot = -1.57F;
            left_arm.xRot = -1.57F;

            right_arm.yRot = 0.0F;
            left_arm.yRot = 0.0F;

            right_arm.zRot = 0.0F;
            left_arm.zRot = 0.0F;

            lower_right_arm.xRot = 0.0F;
            lower_left_arm.xRot = 0.0F;

            lower_right_arm.yRot = 0.0F;
            lower_left_arm.yRot = 0.0F;

            lower_right_arm.zRot = 0.0F;
            lower_left_arm.zRot = 0.0F;

            right_spin_on.yRot = spin;
            left_spin_on.yRot = -spin;

            upper_chest.xRot = 0.0F;
        }

        if (releasing) {
            right_arm.xRot = -1.45F;
            left_arm.xRot = -1.45F;

            right_arm.yRot = -0.22F;
            left_arm.yRot = 0.22F;

            lower_right_arm.xRot = 0.0F;
            lower_left_arm.xRot = 0.0F;

            lower_right_arm.yRot = 0.0F;
            lower_left_arm.yRot = 0.0F;

            upper_chest.xRot = 0.16F;
        }
    }
}
