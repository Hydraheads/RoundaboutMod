package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravitySystem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(
            method = "performShooting",
            at = @At("TAIL")
    )
    private static void jjf$crossbowRecoil(
            Level level,
            LivingEntity shooter,
            InteractionHand hand,
            ItemStack crossbow,
            float velocity,
            float accuracy,
            CallbackInfo ci
    ) {
        if (!(shooter instanceof ZeroGravityAccess access)) {
            return;
        }

        if (!access.jjf$getZeroGravityState().isZeroGravity()) {
            return;
        }

        ZeroGravitySystem.applyRecoilImpulse(
                shooter,
                ZeroGravitySystem.getRecoilDirection(shooter),
                0.10D
        );
    }
}