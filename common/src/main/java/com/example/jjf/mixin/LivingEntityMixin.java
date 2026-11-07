package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.AirPressureSystem;
import com.example.jjf.zero_gravity.SpreadContactSystem;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravityState;
import com.example.jjf.zero_gravity.ZeroGravitySystem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ZeroGravityAccess {

    @Unique
    private final ZeroGravityState jjf$zeroGravityState =
            new ZeroGravityState();

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void jjf$tickZeroGravity(
            CallbackInfo ci
    ) {
        LivingEntity entity =
                (LivingEntity) (Object) this;

        ZeroGravityState state =
                jjf$zeroGravityState;

        ZeroGravitySystem.tickState(
                entity
        );

        ZeroGravitySystem.tickZeroGravity(
                entity
        );

        AirPressureSystem.tick(
                entity,
                state
        );

        SpreadContactSystem.tick(
                entity,
                state
        );
    }

    @Override
    public ZeroGravityState jjf$getZeroGravityState() {
        return jjf$zeroGravityState;
    }

    @Inject(
            method = "knockback",
            at = @At("TAIL")
    )
    private void jjf$zeroGravityKnockback(
            double strength,
            double x,
            double z,
            CallbackInfo ci
    ) {
        LivingEntity entity =
                (LivingEntity) (Object) this;

        ZeroGravitySystem.amplifySpreadKnockback(
                entity
        );
    }

    @Inject(
            method = "swing(Lnet/minecraft/world/InteractionHand;Z)V",
            at = @At("HEAD")
    )
    private void jjf$zeroGravitySwingImpulse(
            InteractionHand hand,
            boolean updateSelf,
            CallbackInfo ci
    ) {
        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }

        LivingEntity entity =
                (LivingEntity) (Object) this;

        ZeroGravitySystem.applySwingImpulse(
                entity
        );
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    private void jjf$saveZeroGravityState(
            CompoundTag tag,
            CallbackInfo ci
    ) {
        tag.putBoolean(
                "JJFZeroGravityActive",
                jjf$zeroGravityState
                        .isZeroGravity()
        );
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    private void jjf$loadZeroGravityState(
            CompoundTag tag,
            CallbackInfo ci
    ) {
        if (!tag.getBoolean(
                "JJFZeroGravityActive"
        )) {
            return;
        }

        LivingEntity entity =
                (LivingEntity) (Object) this;

        jjf$zeroGravityState
                .clearZeroGravity();

        jjf$zeroGravityState
                .setRestorationTicks(
                        0
                );

        entity.setNoGravity(
                false
        );

        entity.fallDistance =
                0.0F;

        entity.setDeltaMovement(
                Vec3.ZERO
        );

        entity.hasImpulse =
                true;
    }
}