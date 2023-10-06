package net.hydra.jojomod.sound;

import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    /** Registers mod sounds... but JSON files are still needed to complete registration!*/
    public static final Identifier SUMMON_SOUND_ID = new Identifier("roundabout:summon_sound");
    public static final Identifier TERRIER_SOUND_ID = new Identifier("roundabout:terrier_pass");
    public static final Identifier WORLD_SUMMON_SOUND_ID = new Identifier("roundabout:summon_world");
    public static final Identifier STAR_SUMMON_SOUND_ID = new Identifier("roundabout:summon_star");
    public static SoundEvent SUMMON_SOUND_EVENT = SoundEvent.of(SUMMON_SOUND_ID);
    public static SoundEvent WORLD_SUMMON_SOUND_EVENT = SoundEvent.of(WORLD_SUMMON_SOUND_ID);
    public static SoundEvent STAR_SUMMON_SOUND_EVENT = SoundEvent.of(STAR_SUMMON_SOUND_ID);
    public static SoundEvent TERRIER_SOUND_EVENT = SoundEvent.of(TERRIER_SOUND_ID);

    public static void registerSoundEvents(){
        Registry.register(Registries.SOUND_EVENT, SUMMON_SOUND_ID, SUMMON_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, TERRIER_SOUND_ID, TERRIER_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAR_SUMMON_SOUND_ID, STAR_SUMMON_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, WORLD_SUMMON_SOUND_ID, WORLD_SUMMON_SOUND_EVENT);
    }

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(RoundaboutMod.MOD_ID,name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
