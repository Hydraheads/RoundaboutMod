package net.hydra.jojomod.sound;

import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final Identifier SUMMON_SOUND_ID = new Identifier("roundabout:summon_sound");
    public static SoundEvent SUMMON_SOUND_EVENT = SoundEvent.of(SUMMON_SOUND_ID);

    public static void registerSoundEvents(){
        Registry.register(Registries.SOUND_EVENT, SUMMON_SOUND_ID, SUMMON_SOUND_EVENT);
    }

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(RoundaboutMod.MOD_ID,name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}