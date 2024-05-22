package net.hydra.jojomod.sound;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    /** Registers mod sounds... but JSON files are still needed to complete registration!*/
    public static final ResourceLocation SUMMON_SOUND_ID = new ResourceLocation("roundabout:summon_sound");
    public static final ResourceLocation TERRIER_SOUND_ID = new ResourceLocation("roundabout:terrier_pass");
    public static final ResourceLocation WORLD_SUMMON_SOUND_ID = new ResourceLocation("roundabout:summon_world");
    public static final ResourceLocation STAR_SUMMON_SOUND_ID = new ResourceLocation("roundabout:summon_star");
    public static final ResourceLocation PUNCH_1_SOUND_ID = new ResourceLocation("roundabout:punch_sfx1");
    public static final ResourceLocation PUNCH_2_SOUND_ID = new ResourceLocation("roundabout:punch_sfx2");
    public static final ResourceLocation PUNCH_3_SOUND_ID = new ResourceLocation("roundabout:punch_sfx3");
    public static final ResourceLocation PUNCH_4_SOUND_ID = new ResourceLocation("roundabout:punch_sfx4");
    public static final ResourceLocation STAND_THEWORLD_MUDA1_SOUND_ID = new ResourceLocation("roundabout:stand_theworld_muda1");
    public static final ResourceLocation STAND_THEWORLD_MUDA2_SOUND_ID = new ResourceLocation("roundabout:stand_theworld_muda2");
    public static final ResourceLocation STAND_THEWORLD_MUDA3_SOUND_ID = new ResourceLocation("roundabout:stand_theworld_muda3");
    public static final ResourceLocation STAND_THEWORLD_MUDA4_SOUND_ID = new ResourceLocation("roundabout:stand_theworld_muda4");
    public static final ResourceLocation STAND_BARRAGE_WINDUP_ID = new ResourceLocation("roundabout:stand_barrage_windup");
    public static final ResourceLocation STAND_BARRAGE_MISS_ID = new ResourceLocation("roundabout:stand_barrage_miss");
    public static final ResourceLocation STAND_BARRAGE_BLOCK_ID = new ResourceLocation("roundabout:stand_barrage_block");
    public static final ResourceLocation STAND_BARRAGE_HIT_ID = new ResourceLocation("roundabout:stand_barrage_hit");
    public static final ResourceLocation STAND_BARRAGE_HIT2_ID = new ResourceLocation("roundabout:stand_barrage_hit2");
    public static final ResourceLocation STAND_BARRAGE_END_ID = new ResourceLocation("roundabout:stand_barrage_end");
    public static final ResourceLocation STAND_GUARD_SOUND_ID = new ResourceLocation("roundabout:stand_guard");
    public static final ResourceLocation MELEE_GUARD_SOUND_ID = new ResourceLocation("roundabout:melee_guard");
    public static final ResourceLocation HIT_1_SOUND_ID = new ResourceLocation("roundabout:hit_sfx1");
    public static SoundEvent SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_SOUND_ID);
    public static SoundEvent WORLD_SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(WORLD_SUMMON_SOUND_ID);
    public static SoundEvent STAR_SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_SUMMON_SOUND_ID);
    public static SoundEvent TERRIER_SOUND_EVENT = SoundEvent.createVariableRangeEvent(TERRIER_SOUND_ID);
    public static SoundEvent PUNCH_1_SOUND_EVENT = SoundEvent.createVariableRangeEvent(PUNCH_1_SOUND_ID);
    public static SoundEvent PUNCH_2_SOUND_EVENT = SoundEvent.createVariableRangeEvent(PUNCH_2_SOUND_ID);
    public static SoundEvent PUNCH_3_SOUND_EVENT = SoundEvent.createVariableRangeEvent(PUNCH_3_SOUND_ID);
    public static SoundEvent PUNCH_4_SOUND_EVENT = SoundEvent.createVariableRangeEvent(PUNCH_4_SOUND_ID);
    public static SoundEvent STAND_GUARD_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_GUARD_SOUND_ID);
    public static SoundEvent MELEE_GUARD_SOUND_EVENT = SoundEvent.createVariableRangeEvent(MELEE_GUARD_SOUND_ID);
    public static SoundEvent HIT_1_SOUND_EVENT = SoundEvent.createVariableRangeEvent(HIT_1_SOUND_ID);
    public static SoundEvent STAND_THEWORLD_MUDA1_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA1_SOUND_ID);
    public static SoundEvent STAND_THEWORLD_MUDA2_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA2_SOUND_ID);
    public static SoundEvent STAND_THEWORLD_MUDA3_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA3_SOUND_ID);
    public static SoundEvent STAND_THEWORLD_MUDA4_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA4_SOUND_ID);
    public static SoundEvent STAND_BARRAGE_WINDUP_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_WINDUP_ID);
    public static SoundEvent STAND_BARRAGE_BLOCK_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_BLOCK_ID);
    public static SoundEvent STAND_BARRAGE_MISS_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_MISS_ID);
    public static SoundEvent STAND_BARRAGE_HIT_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_HIT_ID);
    public static SoundEvent STAND_BARRAGE_HIT2_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_HIT2_ID);
    public static SoundEvent STAND_BARRAGE_END_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_END_ID);

    public static void registerSoundEvents(){
        Registry.register(BuiltInRegistries.SOUND_EVENT, SUMMON_SOUND_ID, SUMMON_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, TERRIER_SOUND_ID, TERRIER_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAR_SUMMON_SOUND_ID, STAR_SUMMON_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, WORLD_SUMMON_SOUND_ID, WORLD_SUMMON_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, PUNCH_1_SOUND_ID, PUNCH_1_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, PUNCH_2_SOUND_ID, PUNCH_2_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, PUNCH_3_SOUND_ID, PUNCH_3_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, PUNCH_4_SOUND_ID, PUNCH_4_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_GUARD_SOUND_ID, STAND_GUARD_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, MELEE_GUARD_SOUND_ID, MELEE_GUARD_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, HIT_1_SOUND_ID, HIT_1_SOUND_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_THEWORLD_MUDA1_SOUND_ID, STAND_THEWORLD_MUDA1_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_THEWORLD_MUDA2_SOUND_ID, STAND_THEWORLD_MUDA2_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_THEWORLD_MUDA3_SOUND_ID, STAND_THEWORLD_MUDA3_SOUND_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_THEWORLD_MUDA4_SOUND_ID, STAND_THEWORLD_MUDA4_SOUND_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_BARRAGE_WINDUP_ID, STAND_BARRAGE_WINDUP_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_BARRAGE_MISS_ID, STAND_BARRAGE_MISS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_BARRAGE_BLOCK_ID, STAND_BARRAGE_BLOCK_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_BARRAGE_HIT_ID, STAND_BARRAGE_HIT_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_BARRAGE_HIT2_ID, STAND_BARRAGE_HIT2_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, STAND_BARRAGE_END_ID, STAND_BARRAGE_END_EVENT);
    }

    private static SoundEvent registerSoundEvent(String name){
        ResourceLocation id = new ResourceLocation(Roundabout.MOD_ID,name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
