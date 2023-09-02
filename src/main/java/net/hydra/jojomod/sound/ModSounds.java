package net.hydra.jojomod.sound;

import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static SoundEvent SUMMON_SOUND = registerSoundEvent("summon_sound");
    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(RoundaboutMod.MOD_ID,name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
