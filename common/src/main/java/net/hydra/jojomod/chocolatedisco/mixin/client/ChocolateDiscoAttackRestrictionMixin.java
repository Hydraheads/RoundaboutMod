package net.hydra.jojomod.chocolatedisco.mixin.client;

import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoAnimationState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.UUID;

@Mixin(Minecraft.class)
public class ChocolateDiscoAttackRestrictionMixin {

    /*
     * ================================================================
     * BLOCK LEFT-CLICK / ATTACK
     * ================================================================
     */

    @Inject(
            method = "startAttack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chocolateDisco$blockAttack(
            CallbackInfoReturnable<Boolean> cir
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();

        if (isChocolateDiscoAbilityActive(minecraft)) {
            cir.setReturnValue(false);
        }
    }

    /*
     * ================================================================
     * BLOCK RIGHT-CLICK / ITEM USE
     * ================================================================
     */

    @Inject(
            method = "startUseItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chocolateDisco$blockItemUse(
            CallbackInfo ci
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();

        if (isChocolateDiscoAbilityActive(minecraft)) {
            ci.cancel();
        }
    }

    /*
     * ================================================================
     * FORCE STOP ACTIVE ITEM USE
     * ================================================================
     */

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void chocolateDisco$stopItemUse(
            CallbackInfo ci
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();

        LocalPlayer player =
                minecraft.player;

        if (player == null) {
            return;
        }

        if (!isChocolateDiscoAbilityActive(minecraft)) {
            return;
        }

        /*
         * Force Minecraft to stop the player's active item use.
         *
         * This handles a shield that was ALREADY raised when
         * Chocolate Disco was activated.
         */
        if (player.isUsingItem()) {
            minecraft.gameMode.releaseUsingItem(player);
        }

        /*
         * Also release the physical use key so Minecraft cannot
         * immediately start using the shield again.
         */
        minecraft.options.keyUse.setDown(false);
    }

    /*
     * ================================================================
     * CHOCOLATE DISCO ABILITY STATE
     * ================================================================
     */

    private static boolean isChocolateDiscoAbilityActive(
            Minecraft minecraft
    ) {

        LocalPlayer player =
                minecraft.player;

        if (player == null) {
            return false;
        }

        UUID playerUUID =
                player.getUUID();

        if (
                ChocolateDiscoAnimationState.isActive(
                        playerUUID
                )
        ) {
            return true;
        }

        return ChocolateDiscoAnimationState.isSelfDiscoActive(
                playerUUID
        );
    }
}