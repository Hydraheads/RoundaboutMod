package net.hydra.jojomod.registry;

import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class FabricSounds {

    public static void addSound(ResourceLocation ID, SoundEvent Event){
        Registry.register(BuiltInRegistries.SOUND_EVENT,ID, Event);
    }

    public static void register(){

        addSound(ModSounds.SUMMON_SOUND_ID, ModSounds.SUMMON_SOUND_EVENT);
        addSound(ModSounds.TERRIER_SOUND_ID, ModSounds.TERRIER_SOUND_EVENT);
        addSound(ModSounds.STAR_SUMMON_SOUND_ID, ModSounds.STAR_SUMMON_SOUND_EVENT);
        addSound(ModSounds.WORLD_SUMMON_SOUND_ID, ModSounds.WORLD_SUMMON_SOUND_EVENT);
        addSound(ModSounds.PUNCH_1_SOUND_ID, ModSounds.PUNCH_1_SOUND_EVENT);
        addSound(ModSounds.PUNCH_2_SOUND_ID, ModSounds.PUNCH_2_SOUND_EVENT);
        addSound(ModSounds.PUNCH_3_SOUND_ID, ModSounds.PUNCH_3_SOUND_EVENT);
        addSound(ModSounds.PUNCH_4_SOUND_ID, ModSounds.PUNCH_4_SOUND_EVENT);
        addSound(ModSounds.STAND_GUARD_SOUND_ID, ModSounds.STAND_GUARD_SOUND_EVENT);
        addSound(ModSounds.MELEE_GUARD_SOUND_ID, ModSounds.MELEE_GUARD_SOUND_EVENT);
        addSound(ModSounds.HIT_1_SOUND_ID, ModSounds.HIT_1_SOUND_EVENT);

        addSound(ModSounds.STAND_THEWORLD_MUDA1_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT);
        addSound(ModSounds.STAND_THEWORLD_MUDA2_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA2_SOUND_EVENT);
        addSound(ModSounds.STAND_THEWORLD_MUDA3_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT);
        addSound(ModSounds.STAND_THEWORLD_MUDA4_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA4_SOUND_EVENT);

        addSound(ModSounds.STAND_BARRAGE_WINDUP_ID, ModSounds.STAND_BARRAGE_WINDUP_EVENT);
        addSound(ModSounds.STAND_BARRAGE_MISS_ID, ModSounds.STAND_BARRAGE_MISS_EVENT);
        addSound(ModSounds.STAND_BARRAGE_BLOCK_ID, ModSounds.STAND_BARRAGE_BLOCK_EVENT);
        addSound(ModSounds.STAND_BARRAGE_HIT_ID, ModSounds.STAND_BARRAGE_HIT_EVENT);
        addSound(ModSounds.STAND_BARRAGE_HIT2_ID, ModSounds.STAND_BARRAGE_HIT2_EVENT);
        addSound(ModSounds.STAND_BARRAGE_END_ID, ModSounds.STAND_BARRAGE_END_EVENT);
    }
}
