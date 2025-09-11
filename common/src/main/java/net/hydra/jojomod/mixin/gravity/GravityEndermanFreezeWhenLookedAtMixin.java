package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        targets = "net.minecraft.world.entity.monster.EnderMan$EndermanFreezeWhenLookedAt"
)
public abstract class GravityEndermanFreezeWhenLookedAtMixin {
    @Shadow @Nullable private LivingEntity target;
    @Shadow
    @Final
    private EnderMan enderman;

    @Inject(
            method = "tick()V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$redirect_tick_getEyeY_0(CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.target);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();
        this.enderman.getLookControl().setLookAt(target.getEyePosition().x, target.getEyePosition().y, target.getEyePosition().z);
    }
}
