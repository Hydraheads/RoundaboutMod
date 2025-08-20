package net.hydra.jojomod.mixin.gravity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.BegGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BegGoal.class)
public abstract class GravityBegGoalMixin {
    @Shadow @Nullable private Player player;

    @Shadow @Final private Wolf wolf;

    @Shadow private int lookTime;

    @Inject(
            method = "tick()V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$tick(CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.player);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();
        this.wolf.getLookControl().setLookAt(this.player.getEyePosition().x, this.player.getEyePosition().y, this.player.getEyePosition().z, 10.0F, (float)this.wolf.getMaxHeadXRot());
        this.lookTime--;
    }
}
