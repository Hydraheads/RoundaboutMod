package net.hydra.jojomod.mixin.whitesnake.memory;

import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.access.DiscBearer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class MemoryLocalPlayerInput {
    @Shadow public Input input;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void roundabout$blockPreviousControlledInput(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (!DiscItemData.hasPlayerControl(player)) clearControlledInput(player, true);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/Input;tick(ZF)V", shift = At.Shift.AFTER))
    private void roundabout$blockRefreshedControlledInput(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (!DiscItemData.hasPlayerControl(player)) clearControlledInput(player, false);
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void roundabout$finishBlockingControlledInput(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (!DiscItemData.hasPlayerControl(player)) clearControlledInput(player, false);
    }

    private void clearControlledInput(LocalPlayer player, boolean updateView) {
        input.leftImpulse = 0;
        input.forwardImpulse = 0;
        input.jumping = false;
        input.shiftKeyDown = false;
        player.xxa = 0;
        player.zza = 0;
        player.setSprinting(false);
        if (updateView) updateControlledView(player);
    }

    private void updateControlledView(LocalPlayer player) {
        DiscBearer bearer = (DiscBearer) player;
        if (DiscItemData.isLobotomized(player)) {
            player.setXRot(90.0F);
            player.setYHeadRot(player.getYRot());
        }
    }
}
