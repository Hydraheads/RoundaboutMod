package net.hydra.jojomod.mixin.jumpin_jack_flash;

import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityAccess;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityState;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravitySystem;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Unique
    private boolean jjf$wasJumping = false;

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void jjf$handleZeroGravityInput(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (!(player instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            jjf$wasJumping = false;
            return;
        }

        boolean jumping = player.input.jumping;

        if (jumping && !jjf$wasJumping) {
            if (ZeroGravitySystem.isTouchingGround(player)) {
                ZeroGravitySystem.groundPushOff(player);
            }
        }

        jjf$wasJumping = jumping;
    }
}