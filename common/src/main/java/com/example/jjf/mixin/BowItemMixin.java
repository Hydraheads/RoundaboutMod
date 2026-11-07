package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravitySystem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(
            method = "releaseUsing",
            at = @At("TAIL")
    )
    private void jjf$bowRecoil(
            ItemStack bow,
            Level level,
            LivingEntity shooter,
            int remainingTicks,
            CallbackInfo ci
    ) {
        if (!(shooter instanceof ZeroGravityAccess access)) {
            return;
        }

        if (!access.jjf$getZeroGravityState().isZeroGravity()) {
            return;
        }

        int chargeTicks =
                bow.getUseDuration() - remainingTicks;

        float power =
                BowItem.getPowerForTime(chargeTicks);

        if (power < 0.1F) {
            return;
        }

        double recoilStrength =
                0.025D + (0.05D * power);

        ZeroGravitySystem.applyRecoilImpulse(
                shooter,
                ZeroGravitySystem.getRecoilDirection(shooter),
                recoilStrength
        );
    }
}