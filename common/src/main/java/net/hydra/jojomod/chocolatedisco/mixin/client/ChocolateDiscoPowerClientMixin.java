package net.hydra.jojomod.chocolatedisco.mixin.client;

import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoAnimationState;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoGridTransform;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoHud;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSelfDiscoOverlay;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSelectionState;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSounds;
import net.hydra.jojomod.chocolatedisco.network.ChocolateDiscoNetworking;
import net.zetalasis.networking.message.api.ModMessageEvents;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoClient;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.stand.powers.elements.PowerContext;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.hydra.jojomod.chocolatedisco.PowersChocolateDisco;

@Mixin(PowersChocolateDisco.class)
public class ChocolateDiscoPowerClientMixin {

    /*
     * Prevent Shift+X from toggling twice during the same game tick.
     */
    private static long lastBuildingModeToggleTick = -1L;



    @Inject(
            method = "powerActivate",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void chocolateDisco$handleAbility(
            PowerContext context,
            CallbackInfo ci
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        ChocolateDiscoSounds.suppressNextDiscoSummonSound();

        // =====================================================
        // X - SELECT DESTINATION TILE
        // =====================================================

        if (context == PowerContext.SKILL_2_NORMAL) {

            if (ChocolateDiscoNetworking.isRedirectActive(
                    minecraft.player.getUUID()
            )) {
                ci.cancel();
                return;
            }

            minecraft.player.playSound(
                    ChocolateDiscoSounds.DISCO_MENU,
                    1.0F,
                    1.0F
            );

            ChocolateDiscoHud.open();

            ci.cancel();
            return;
        }


        // =====================================================
// SHIFT+X - TOGGLE BUILDING MODE
// =====================================================

        if (context == PowerContext.SKILL_2_CROUCH) {

            if (ChocolateDiscoNetworking.isRedirectActive(
                    minecraft.player.getUUID()
            )) {
                ci.cancel();
                return;
            }

            long currentTick =
                    minecraft.level != null
                            ? minecraft.level.getGameTime()
                            : -1L;

            /*
             * If Roundabout sends the same crouch activation more
             * than once during the same tick, ignore the duplicate.
             */
            if (currentTick == lastBuildingModeToggleTick) {
                ci.cancel();
                return;
            }

            lastBuildingModeToggleTick =
                    currentTick;


            boolean buildingMode =
                    !ChocolateDiscoSelectionState.isBuildingMode();

            ChocolateDiscoSelectionState.setBuildingMode(
                    buildingMode
            );


            minecraft.player.playSound(
                    ChocolateDiscoSounds.BUILDING_MODE,
                    1.0F,
                    1.0F
            );


            ModMessageEvents.sendToServer(
                    ChocolateDiscoNetworking.BUILDING_MODE_STATE,
                    buildingMode
            );


            minecraft.player.displayClientMessage(
                    Component.literal(
                            buildingMode
                                    ? "Building Mode: ON"
                                    : "Building Mode: OFF"
                    ),
                    true
            );


            ci.cancel();
            return;
        }


        // =====================================================
        // SHIFT+C - LOCK / UNLOCK GRID
        // =====================================================

        if (context == PowerContext.SKILL_3_CROUCH) {

            if (ChocolateDiscoNetworking.isRedirectActive(
                    minecraft.player.getUUID()
            )) {
                ci.cancel();
                return;
            }

            ChocolateDiscoGridTransform.toggleLock(
                    minecraft.player
            );

            boolean locked =
                    ChocolateDiscoGridTransform.isLocked(
                            minecraft.player
                    );

            minecraft.player.playSound(
                    ChocolateDiscoSounds.DISCO_LOCK,
                    1.0F,
                    1.0F
            );


            /*
             * Keep the client-side selection state synchronized
             * with the visual grid.
             */
            boolean largeGrid =
                    locked;

            if (
                    ChocolateDiscoSelectionState.isLargeGrid()
                            != largeGrid
            ) {
                ChocolateDiscoSelectionState.toggleGridSize();
            }


            /*
             * Send the grid lock state and transform to the server.
             */
            ChocolateDiscoGridTransform.Transform transform =
                    ChocolateDiscoGridTransform.get(
                            minecraft.player
                    );

            double transformX = transform == null ? minecraft.player.getX() : transform.x;
            double transformY = transform == null ? minecraft.player.getY() : transform.y;
            double transformZ = transform == null ? minecraft.player.getZ() : transform.z;
            float transformYaw = transform == null ? minecraft.player.getYRot() : transform.yaw;

            ModMessageEvents.sendToServer(
                    ChocolateDiscoNetworking.GRID_LOCK_STATE,
                    locked,
                    transformX,
                    transformY,
                    transformZ,
                    transformYaw
            );


            minecraft.player.displayClientMessage(
                    Component.literal(
                            locked
                                    ? "Grid locked: 15x15"
                                    : "Grid unlocked: 7x7"
                    ),
                    true
            );


            ci.cancel();
            return;
        }


        // =====================================================
        // V - QUEUE / EMPTY QUEUE
        // =====================================================

        if (context == PowerContext.SKILL_4_NORMAL) {

            if (ChocolateDiscoNetworking.isRedirectActive(
                    minecraft.player.getUUID()
            )) {
                ci.cancel();
                return;
            }

            ModMessageEvents.sendToServer(
                    ChocolateDiscoNetworking.QUEUE_ITEM
            );

            ci.cancel();
            return;
        }


        // =====================================================
        // SHIFT+V - SELF DISCO / REDIRECT PROJECTILES
        // =====================================================

        if (context == PowerContext.SKILL_4_CROUCH) {

            if (ChocolateDiscoNetworking.isRedirectActive(
                    minecraft.player.getUUID()
            )) {
                ci.cancel();
                return;
            }

            /*
             * Check Roundabout's native cooldown.
             */
            if (((PowersChocolateDisco) (Object) this)
                    .onCooldown(PowerIndex.SKILL_4_SNEAK)) {

                ci.cancel();
                return;
            }


            minecraft.player.playSound(
                    ChocolateDiscoSounds.DISCO_TELEPORT,
                    1.0F,
                    1.0F
            );


            ChocolateDiscoAnimationState.startSelfDisco(
                    minecraft.player.getUUID()
            );

            minecraft.player.stopUsingItem();
            minecraft.options.keyUse.setDown(false);

            ((PowersChocolateDisco) (Object) this)
                    .animateStand((byte) 1);

            ChocolateDiscoSelfDiscoOverlay.start();


            ModMessageEvents.sendToServer(
                    ChocolateDiscoNetworking.REDIRECT_PROJECTILES
            );


            ci.cancel();
            return;
        }


        // =====================================================
        // Z - ACTIVATE CHOCOLATE DISCO
        // =====================================================

        if (
                context == PowerContext.SKILL_1_NORMAL
                        || context == PowerContext.SKILL_1_CROUCH
        ) {

            if (ChocolateDiscoNetworking.isRedirectActive(
                    minecraft.player.getUUID()
            )) {
                ci.cancel();
                return;
            }

            PowersChocolateDisco powers =
                    (PowersChocolateDisco) (Object) this;


            /*
             * Z and Shift+Z share the same cooldown.
             */
            if (powers.onCooldown(
                    PowerIndex.SKILL_1
            )) {

                ci.cancel();
                return;
            }


            /*
             * Do not allow another Z activation while the
             * Chocolate Disco activation animation is active.
             */
            if (ChocolateDiscoAnimationState.isActive(
                    minecraft.player.getUUID()
            )) {

                ci.cancel();
                return;
            }


            ChocolateDiscoGridTransform.Transform transform =
                    ChocolateDiscoGridTransform.get(
                            minecraft.player
                    );

            if (transform == null) {
                ci.cancel();
                return;
            }


            /*
             * Start the existing Chocolate Disco animation.
             */
            ChocolateDiscoAnimationState.start(
                    minecraft.player.getUUID()
            );

            minecraft.player.stopUsingItem();
            minecraft.options.keyUse.setDown(false);


            int column =
                    ChocolateDiscoSelectionState
                            .getSelectedColumn();

            int row =
                    ChocolateDiscoSelectionState
                            .getSelectedRow();


            boolean highTeleport =
                    context == PowerContext.SKILL_1_CROUCH;


            /*
             * 40 ticks = 2 seconds.
             */
            powers.setCooldown(
                    PowerIndex.SKILL_1,
                    40
            );


            minecraft.player.playSound(
                    ChocolateDiscoSounds.DISCO_TELEPORT,
                    1.0F,
                    1.0F
            );


            ModMessageEvents.sendToServer(
                    ChocolateDiscoNetworking.TELEPORT_PROJECTILES,
                    column,
                    row,
                    highTeleport,
                    transform.x,
                    transform.y,
                    transform.z,
                    transform.yaw
            );


            ci.cancel();
            return;
        }

    }
}