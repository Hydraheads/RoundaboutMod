package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookAtPlayerGoal.class)
public class GravityLookAtPlayerGoalMixin {
    @Shadow @Nullable protected Entity lookAt;

    @Shadow @Final private boolean onlyHorizontal;

    @Shadow @Final protected Mob mob;

    @Shadow private int lookTime;

    @Inject(
            method = "tick()V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$tick(CallbackInfo ci) {
        if (this.lookAt.isAlive()) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.lookAt);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

            double $$0 = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyePosition().y;
            this.mob.getLookControl().setLookAt(this.lookAt.getEyePosition().x, $$0, this.lookAt.getEyePosition().z);
            this.lookTime--;
        }
    }
}
