package com.example.jjf.mixin;

import com.example.jjf.client.JJFPlayerWallPose;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravityState;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity>
        extends HumanoidModel<T> {

    @Shadow
    @Final
    public ModelPart leftSleeve;

    @Shadow
    @Final
    public ModelPart rightSleeve;

    @Shadow
    @Final
    public ModelPart leftPants;

    @Shadow
    @Final
    public ModelPart rightPants;

    @Shadow
    @Final
    public ModelPart jacket;


    public PlayerModelMixin(ModelPart root) {
        super(root);
    }


    @Inject(
            method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
            at = @At("TAIL")
    )
    private void jjf$wallSuctionPose(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (!state.isSuctioned()) {
            return;
        }

        Direction surface =
                state.getSuctionSurface();

        if (surface == null) {
            return;
        }

        PlayerModel<?> model =
                (PlayerModel<?>) (Object) this;

        if (surface == Direction.UP) {

            int flatDirection =
                    JJFPlayerWallPose.getFlatCeilingDirection(entity);

            if (flatDirection != 0) {
                JJFPlayerWallPose.applyFlatCeilingCling(model);
            } else {
                JJFPlayerWallPose.applyCeilingHang(model);
            }

        } else if (surface == Direction.DOWN) {

            return;

        } else {

            JJFPlayerWallPose.applyWallPose(
                    model,
                    entity,
                    surface
            );
        }

        // Make the outer skin layers follow the new pose
        rightSleeve.copyFrom(this.rightArm);
        leftSleeve.copyFrom(this.leftArm);

        rightPants.copyFrom(this.rightLeg);
        leftPants.copyFrom(this.leftLeg);

        jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }
}