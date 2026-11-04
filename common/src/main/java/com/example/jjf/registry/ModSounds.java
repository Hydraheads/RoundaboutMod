package com.example.jjf.registry;

import com.example.jjf.JJFMod;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {

    public static SoundEvent
            JUMPIN_JACK_FLASH_SUMMON;

    public static SoundEvent
            SUCTION_LAUNCH;

    public static SoundEvent
            LAUNCHER_SHOOT;

    public static SoundEvent
            LAUNCHER_BURST;

    private ModSounds() {
    }

    public static SoundEvent create(
            String path
    ) {
        return SoundEvent.createVariableRangeEvent(
                JJFMod.id(
                        path
                )
        );
    }
}
