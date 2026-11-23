package net.hydra.jojomod.mixin.jumpin_jack_flash;

import net.hydra.jojomod.event.powers.jumpingJackFlash.JJFLauncherAccess;
import net.hydra.jojomod.event.powers.jumpingJackFlash.JJFLauncherState;
import net.hydra.jojomod.event.powers.jumpingJackFlash.LauncherAmmoSync;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityAccess;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityState;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravitySync;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerZeroGravitySyncMixin {

    @Unique
    private boolean jjf$wasSyncingGravity;

    @Unique
    private boolean jjf$sentInitialPressureSnapshot;

    @Unique
    private int jjf$lastAmmoCount = Integer.MIN_VALUE;

    @Unique
    private ItemStack jjf$lastAmmoIcon = ItemStack.EMPTY;

    @Inject(method = "tick", at = @At("TAIL"))
    private void jjf$syncGravityWithPlayer(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (player.connection == null) {
            return;
        }
        if (player instanceof ZeroGravityAccess access) {
            ZeroGravityState state = access.jjf$getZeroGravityState();
        if (!jjf$sentInitialPressureSnapshot) {
            ZeroGravitySync.send(player, state);
            jjf$sentInitialPressureSnapshot = true;
        }
            boolean isActive = state.isZeroGravity() || state.isRestoringWeight() || state.isCommandPressureActive();
            if (isActive != jjf$wasSyncingGravity || (isActive && player.tickCount % 2 == 0)) {
                ZeroGravitySync.send(player, state);
            }
            jjf$wasSyncingGravity = isActive;
        }
        if (player instanceof JJFLauncherAccess access) {
            JJFLauncherState launcher = access.jjf$getLauncherState();
            int count = launcher.getLoadedCount();
            ItemStack icon = launcher.getLoadedItem();
            if (count != jjf$lastAmmoCount
                    || !ItemStack.isSameItemSameTags(icon, jjf$lastAmmoIcon)
                    || player.tickCount % 200 == 0) {
                LauncherAmmoSync.send(player, launcher);
                jjf$lastAmmoCount = count;
                jjf$lastAmmoIcon = icon;
            }
        }
    }
}
