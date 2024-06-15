package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ForgeSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Roundabout.MOD_ID);

    public static final RegistryObject<SoundEvent> SUMMON_SOUND_EVENT =
            register(ModSounds.SUMMON_SOUND, ModSounds.SUMMON_SOUND_ID);
    public static final RegistryObject<SoundEvent> TERRIER_SOUND_EVENT =
            register(ModSounds.TERRIER_SOUND, ModSounds.TERRIER_SOUND_ID);
    public static final RegistryObject<SoundEvent> WORLD_SUMMON_SOUND_EVENT =
            register(ModSounds.WORLD_SUMMON_SOUND, ModSounds.WORLD_SUMMON_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAR_SUMMON_SOUND_EVENT =
            register(ModSounds.STAR_SUMMON_SOUND, ModSounds.STAR_SUMMON_SOUND_ID);
    public static final RegistryObject<SoundEvent> PUNCH_1_SOUND_EVENT =
            register(ModSounds.PUNCH_1_SOUND, ModSounds.PUNCH_1_SOUND_ID);
    public static final RegistryObject<SoundEvent> PUNCH_2_SOUND_EVENT =
            register(ModSounds.PUNCH_2_SOUND, ModSounds.PUNCH_2_SOUND_ID);
    public static final RegistryObject<SoundEvent> PUNCH_3_SOUND_EVENT =
            register(ModSounds.PUNCH_3_SOUND, ModSounds.PUNCH_3_SOUND_ID);
    public static final RegistryObject<SoundEvent> PUNCH_4_SOUND_EVENT =
            register(ModSounds.PUNCH_4_SOUND, ModSounds.PUNCH_4_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA1_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA1_SOUND, ModSounds.STAND_THEWORLD_MUDA1_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA2_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA2_SOUND, ModSounds.STAND_THEWORLD_MUDA2_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA3_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA3_SOUND, ModSounds.STAND_THEWORLD_MUDA3_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA4_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA4_SOUND, ModSounds.STAND_THEWORLD_MUDA4_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_BARRAGE_WINDUP_EVENT =
            register(ModSounds.STAND_BARRAGE_WINDUP, ModSounds.STAND_BARRAGE_WINDUP_ID);
    public static final RegistryObject<SoundEvent> STAND_BARRAGE_MISS_EVENT =
            register(ModSounds.STAND_BARRAGE_MISS, ModSounds.STAND_BARRAGE_MISS_ID);
    public static final RegistryObject<SoundEvent> STAND_BARRAGE_BLOCK_EVENT =
            register(ModSounds.STAND_BARRAGE_BLOCK, ModSounds.STAND_BARRAGE_BLOCK_ID);
    public static final RegistryObject<SoundEvent> STAND_BARRAGE_HIT_EVENT =
            register(ModSounds.STAND_BARRAGE_HIT, ModSounds.STAND_BARRAGE_HIT_ID);
    public static final RegistryObject<SoundEvent> STAND_BARRAGE_END_BLOCK_EVENT =
            register(ModSounds.STAND_BARRAGE_END_BLOCK, ModSounds.STAND_BARRAGE_END_BLOCK_ID);
    public static final RegistryObject<SoundEvent> STAND_BARRAGE_HIT2_EVENT =
            register(ModSounds.STAND_BARRAGE_HIT2, ModSounds.STAND_BARRAGE_HIT2_ID);
    public static final RegistryObject<SoundEvent> STAND_BARRAGE_END_EVENT =
            register(ModSounds.STAND_BARRAGE_END, ModSounds.STAND_BARRAGE_END_ID);
    public static final RegistryObject<SoundEvent> STAND_GUARD_SOUND_EVENT =
            register(ModSounds.STAND_GUARD_SOUND, ModSounds.STAND_GUARD_SOUND_ID);
    public static final RegistryObject<SoundEvent> MELEE_GUARD_SOUND_EVENT =
            register(ModSounds.MELEE_GUARD_SOUND, ModSounds.MELEE_GUARD_SOUND_ID);
    public static final RegistryObject<SoundEvent> HIT_1_SOUND_EVENT =
            register(ModSounds.HIT_1_SOUND, ModSounds.HIT_1_SOUND_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_THE_WORLD_EVENT =
            register(ModSounds.TIME_STOP_THE_WORLD, ModSounds.TIME_STOP_THE_WORLD_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_STAR_PLATINUM_EVENT =
            register(ModSounds.TIME_STOP_STAR_PLATINUM, ModSounds.TIME_STOP_STAR_PLATINUM_ID);
    public static final RegistryObject<SoundEvent> TIME_RESUME_EVENT =
            register(ModSounds.TIME_RESUME, ModSounds.TIME_RESUME_ID);

    public static RegistryObject<SoundEvent> register(String id, ResourceLocation id2){
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(id2));
    }
}
