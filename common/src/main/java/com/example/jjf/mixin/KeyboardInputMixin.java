package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravityState;
import com.example.jjf.zero_gravity.ZeroGravitySystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("TAIL"))
    private void jjf$limitZeroGravityAirControl(
            boolean slowDown,
            float slowDownFactor,
            CallbackInfo ci
    ) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        if (!(player instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            return;
        }

        // Surface movement stays normal.
        if (ZeroGravitySystem.isTouchingGround(player)) {
            return;
        }

        // Free-floating in 0G:
        // WASD can influence you slightly, but cannot freely fly you around.
        this.leftImpulse *= 0.08F;
        this.forwardImpulse *= 0.08F;
    }

}