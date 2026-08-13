package net.hydra.jojomod.mixin.whitesnake;

import com.mojang.blaze3d.Blaze3D;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.client.WhitesnakeControlClient;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.SmoothDouble;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MemoryMouseHandler {
    @Inject(method = "turnPlayer()V", at = @At("HEAD"), cancellable = true)
    private void roundabout$memoryControlsView(CallbackInfo ci) {
        Entity stand = roundabout$getControlledStand();
        if (stand != null) {
            ci.cancel();
            roundabout$turnControlledStand(stand);
            return;
        }
        if (minecraft.player != null && !DiscItemData.hasPlayerControl(minecraft.player)) {
            ci.cancel();
        }
    }

    private void roundabout$turnControlledStand(Entity stand) {
        double time = Blaze3D.getTime();
        double elapsed = time - lastMouseEventTime;
        lastMouseEventTime = time;
        if (!isMouseGrabbed() || !minecraft.isWindowActive()) {
            accumulatedDX = 0.0D;
            accumulatedDY = 0.0D;
            return;
        }

        double sensitivity = minecraft.options.sensitivity().get() * 0.6F + 0.2F;
        double sensitivityCubed = sensitivity * sensitivity * sensitivity;
        double sensitivityScale = sensitivityCubed * 8.0D;
        double yawDelta;
        double pitchDelta;
        if (minecraft.options.smoothCamera) {
            yawDelta = smoothTurnX.getNewDeltaValue(accumulatedDX * sensitivityScale, elapsed * sensitivityScale);
            pitchDelta = smoothTurnY.getNewDeltaValue(accumulatedDY * sensitivityScale, elapsed * sensitivityScale);
        } else if (minecraft.options.getCameraType().isFirstPerson() && minecraft.player.isScoping()) {
            smoothTurnX.reset();
            smoothTurnY.reset();
            yawDelta = accumulatedDX * sensitivityCubed;
            pitchDelta = accumulatedDY * sensitivityCubed;
        } else {
            smoothTurnX.reset();
            smoothTurnY.reset();
            yawDelta = accumulatedDX * sensitivityScale;
            pitchDelta = accumulatedDY * sensitivityScale;
        }

        accumulatedDX = 0.0D;
        accumulatedDY = 0.0D;
        if (minecraft.options.invertYMouse().get()) pitchDelta = -pitchDelta;
        minecraft.getTutorial().onMouse(yawDelta, pitchDelta);
        WhitesnakeControlClient.turnCamera(stand, yawDelta, pitchDelta);
    }

    private Entity roundabout$getControlledStand() {
        LocalPlayer player = minecraft.player;
        if (player == null
                || !(((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers)
                || !powers.isPiloting()) return null;
        return powers.getPilotingStand();
    }

    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private SmoothDouble smoothTurnX;
    @Shadow
    @Final
    private SmoothDouble smoothTurnY;
    @Shadow
    private double accumulatedDX;
    @Shadow
    private double accumulatedDY;
    @Shadow
    private double lastMouseEventTime;
    @Shadow
    public abstract boolean isMouseGrabbed();
}
