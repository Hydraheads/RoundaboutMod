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
    public static final Identifier PUNCH_1_SOUND_ID = new Identifier("roundabout:punch_sfx1");
    public static final Identifier PUNCH_2_SOUND_ID = new Identifier("roundabout:punch_sfx2");
    public static final Identifier PUNCH_3_SOUND_ID = new Identifier("roundabout:punch_sfx3");
    public static final Identifier PUNCH_4_SOUND_ID = new Identifier("roundabout:punch_sfx4");
    public static final Identifier STAND_THEWORLD_MUDA1_SOUND_ID = new Identifier("roundabout:stand_theworld_muda1");
    public static final Identifier STAND_THEWORLD_MUDA2_SOUND_ID = new Identifier("roundabout:stand_theworld_muda2");
    public static final Identifier STAND_THEWORLD_MUDA3_SOUND_ID = new Identifier("roundabout:stand_theworld_muda3");
    public static final Identifier STAND_THEWORLD_MUDA4_SOUND_ID = new Identifier("roundabout:stand_theworld_muda4");
    public static final Identifier STAND_BARRAGE_WINDUP_ID = new Identifier("roundabout:stand_barrage_windup");
    public static final Identifier STAND_BARRAGE_MISS_ID = new Identifier("roundabout:stand_barrage_miss");
    public static final Identifier STAND_BARRAGE_BLOCK_ID = new Identifier("roundabout:stand_barrage_block");
    public static final Identifier STAND_BARRAGE_HIT_ID = new Identifier("roundabout:stand_barrage_hit");
    public static final Identifier STAND_BARRAGE_HIT2_ID = new Identifier("roundabout:stand_barrage_hit2");
    public static final Identifier STAND_BARRAGE_END_ID = new Identifier("roundabout:stand_barrage_end");
    public static final Identifier STAND_GUARD_SOUND_ID = new Identifier("roundabout:stand_guard");
    public static final Identifier MELEE_GUARD_SOUND_ID = new Identifier("roundabout:melee_guard");
    public static final Identifier HIT_1_SOUND_ID = new Identifier("roundabout:hit_sfx1");
    public static SoundEvent SUMMON_SOUND_EVENT = SoundEvent.of(SUMMON_SOUND_ID);
    public static SoundEvent WORLD_SUMMON_SOUND_EVENT = SoundEvent.of(WORLD_SUMMON_SOUND_ID);
    public static SoundEvent STAR_SUMMON_SOUND_EVENT = SoundEvent.of(STAR_SUMMON_SOUND_ID);
    public static SoundEvent TERRIER_SOUND_EVENT = SoundEvent.of(TERRIER_SOUND_ID);
    public static SoundEvent PUNCH_1_SOUND_EVENT = SoundEvent.of(PUNCH_1_SOUND_ID);
    public static SoundEvent PUNCH_2_SOUND_EVENT = SoundEvent.of(PUNCH_2_SOUND_ID);
    public static SoundEvent PUNCH_3_SOUND_EVENT = SoundEvent.of(PUNCH_3_SOUND_ID);
    public static SoundEvent PUNCH_4_SOUND_EVENT = SoundEvent.of(PUNCH_4_SOUND_ID);
    public static SoundEvent STAND_GUARD_SOUND_EVENT = SoundEvent.of(STAND_GUARD_SOUND_ID);
    public static SoundEvent MELEE_GUARD_SOUND_EVENT = SoundEvent.of(MELEE_GUARD_SOUND_ID);
    public static SoundEvent HIT_1_SOUND_EVENT = SoundEvent.of(HIT_1_SOUND_ID);
    public static SoundEvent STAND_THEWORLD_MUDA1_SOUND_EVENT = SoundEvent.of(STAND_THEWORLD_MUDA1_SOUND_ID);
    public static SoundEvent STAND_THEWORLD_MUDA2_SOUND_EVENT = SoundEvent.of(STAND_THEWORLD_MUDA2_SOUND_ID);
    public static SoundEvent STAND_THEWORLD_MUDA3_SOUND_EVENT = SoundEvent.of(STAND_THEWORLD_MUDA3_SOUND_ID);
    public static SoundEvent STAND_THEWORLD_MUDA4_SOUND_EVENT = SoundEvent.of(STAND_THEWORLD_MUDA4_SOUND_ID);
    public static SoundEvent STAND_BARRAGE_WINDUP_EVENT = SoundEvent.of(STAND_BARRAGE_WINDUP_ID);
    public static SoundEvent STAND_BARRAGE_BLOCK_EVENT = SoundEvent.of(STAND_BARRAGE_BLOCK_ID);
    public static SoundEvent STAND_BARRAGE_MISS_EVENT = SoundEvent.of(STAND_BARRAGE_MISS_ID);
    public static SoundEvent STAND_BARRAGE_HIT_EVENT = SoundEvent.of(STAND_BARRAGE_HIT_ID);
    public static SoundEvent STAND_BARRAGE_HIT2_EVENT = SoundEvent.of(STAND_BARRAGE_HIT2_ID);
    public static SoundEvent STAND_BARRAGE_END_EVENT = SoundEvent.of(STAND_BARRAGE_END_ID);

    public static void registerSoundEvents(){
        Registry.register(Registries.SOUND_EVENT, SUMMON_SOUND_ID, SUMMON_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, TERRIER_SOUND_ID, TERRIER_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAR_SUMMON_SOUND_ID, STAR_SUMMON_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, WORLD_SUMMON_SOUND_ID, WORLD_SUMMON_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, PUNCH_1_SOUND_ID, PUNCH_1_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, PUNCH_2_SOUND_ID, PUNCH_2_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, PUNCH_3_SOUND_ID, PUNCH_3_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, PUNCH_4_SOUND_ID, PUNCH_4_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_GUARD_SOUND_ID, STAND_GUARD_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, MELEE_GUARD_SOUND_ID, MELEE_GUARD_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, HIT_1_SOUND_ID, HIT_1_SOUND_EVENT);

        Registry.register(Registries.SOUND_EVENT, STAND_THEWORLD_MUDA1_SOUND_ID, STAND_THEWORLD_MUDA1_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_THEWORLD_MUDA2_SOUND_ID, STAND_THEWORLD_MUDA2_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_THEWORLD_MUDA3_SOUND_ID, STAND_THEWORLD_MUDA3_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_THEWORLD_MUDA4_SOUND_ID, STAND_THEWORLD_MUDA4_SOUND_EVENT);

        Registry.register(Registries.SOUND_EVENT, STAND_BARRAGE_WINDUP_ID, STAND_BARRAGE_WINDUP_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_BARRAGE_MISS_ID, STAND_BARRAGE_MISS_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_BARRAGE_BLOCK_ID, STAND_BARRAGE_BLOCK_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_BARRAGE_HIT_ID, STAND_BARRAGE_HIT_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_BARRAGE_HIT2_ID, STAND_BARRAGE_HIT2_EVENT);
        Registry.register(Registries.SOUND_EVENT, STAND_BARRAGE_END_ID, STAND_BARRAGE_END_EVENT);
    }

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(RoundaboutMod.MOD_ID,name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
