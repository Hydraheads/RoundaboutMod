package net.hydra.jojomod.client;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

public final class DiverDownControlsClient {
    private static CameraType previousCameraType = null;
    private static boolean cameraActive = false;
    private static boolean isChestScreenCurrentlyOpen = false;

    private DiverDownControlsClient() {
    }

    //save the current camera of the user
    public static void enter(Entity stand) {
        if (stand == null) return;
        Minecraft mc = Minecraft.getInstance();

        if (previousCameraType == null) {
            previousCameraType = mc.options.getCameraType();
        }

        mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);

        if (mc.player != null && mc.getCameraEntity() != mc.player) {
            mc.setCameraEntity(mc.player);
        }

        ClientUtil.setCameraEntity(stand);
        cameraActive = true;
    }

    // restore the previous camera state
    public static void exit() {
        ClientUtil.setCameraEntity(null);
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null) {
            mc.setCameraEntity(mc.player);
        }

        restoreCameraType(mc);
        cameraActive = false;
        isChestScreenCurrentlyOpen = false;
    }

    // failsafe just in case
    public static void clear() {
        exit();
    }

    // keep camera in third person while diving
    public static void enforceCamera(Entity stand) {
        if (stand == null) return;
        Minecraft mc = Minecraft.getInstance();

        ClientUtil.synchToCamera(stand);

        if (mc.options.getCameraType() != CameraType.THIRD_PERSON_BACK) {
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }

    // checks for containers that are currently open
    public static boolean isScreenOpen() {
        return Minecraft.getInstance().screen != null;
    }

    // plays the chest/barrel closing sound
    public static void handleChestAudio(boolean isBarrel) {
        Minecraft mc = Minecraft.getInstance();
        boolean hasScreenNow = mc.screen != null;

        if (hasScreenNow) {
            isChestScreenCurrentlyOpen = true;
        } else if (isChestScreenCurrentlyOpen) {
            isChestScreenCurrentlyOpen = false;
            SoundEvent closeSound = isBarrel ? SoundEvents.BARREL_CLOSE : SoundEvents.CHEST_CLOSE;
            if (mc.player != null) {
                mc.player.playSound(closeSound, 1.0F, 1.0F);
            }
        }
    }

    private static void restoreCameraType(Minecraft mc) {
        CameraType restore = (previousCameraType != null) ? previousCameraType : CameraType.FIRST_PERSON;
        mc.options.setCameraType(restore);
        previousCameraType = null;
    }
}