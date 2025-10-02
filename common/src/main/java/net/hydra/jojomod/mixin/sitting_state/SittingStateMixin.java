package net.hydra.jojomod.mixin.sitting_state;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.hydra.jojomod.util.SittingState;

@Mixin(Player.class)
public abstract class SittingStateMixin implements SittingState {

    @Unique
    private boolean sitting = false;

    @Unique
    public boolean isSitting() {
        return sitting;
    }

    @Unique
    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void disableMovement(Vec3 $$0, CallbackInfo ci) {
        if (this.isSitting()) {
            ((Player)(Object)this).setDeltaMovement(Vec3.ZERO);
            ci.cancel();
        }
    }

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    private void sittingPose(CallbackInfo ci) {
        System.out.println("Wow1");
        SittingState state = (SittingState) (Object) this;
        if (!state.isSitting()) {
            System.out.println("Wow2");
            ((Player) (Object) this).setPose(Pose.SITTING);
        }
    }
}
