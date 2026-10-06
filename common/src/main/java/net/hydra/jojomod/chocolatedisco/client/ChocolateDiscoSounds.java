package net.hydra.jojomod.chocolatedisco.client;

import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;

public class ChocolateDiscoSounds {

    public static final SoundEvent DISCO_SUMMON = ModSounds.DISCO_SUMMON_EVENT;
    public static final SoundEvent DISCO_MENU = ModSounds.DISCO_MENU_EVENT;
    public static final SoundEvent DISCO_SELECT = ModSounds.DISCO_SELECT_EVENT;
    public static final SoundEvent DISCO_TELEPORT = ModSounds.DISCO_TELEPORT_EVENT;
    public static final SoundEvent BUILDING_MODE = ModSounds.BUILDING_MODE_EVENT;
    public static final SoundEvent QUEUE_ITEM = ModSounds.QUEUE_ITEM_EVENT;
    public static final SoundEvent QUEUE_EMPTY = ModSounds.QUEUE_EMPTY_EVENT;
    public static final SoundEvent DISCO_LOCK = ModSounds.DISCO_LOCK_EVENT;

    private static boolean suppressNextDiscoSummonSound = false;

    public static void suppressNextDiscoSummonSound() {
        suppressNextDiscoSummonSound = true;
    }

    public static boolean consumeSuppressedDiscoSummonSound() {
        if (!suppressNextDiscoSummonSound) {
            return false;
        }
        suppressNextDiscoSummonSound = false;
        return true;
    }

    public static void register() {
        // Sound events are registered by the platform registries.
    }
}
