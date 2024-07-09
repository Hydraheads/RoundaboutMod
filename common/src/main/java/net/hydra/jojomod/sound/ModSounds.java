package net.hydra.jojomod.sound;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    /** Defines sound files... but JSON files are still needed to complete registration!
     * Also, forge and Fabric must register the sounds.*/
    public static final String SUMMON_SOUND = "summon_sound";
    public static final ResourceLocation SUMMON_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_SOUND);
    public static SoundEvent SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_SOUND_ID);

    public static final String TERRIER_SOUND = "terrier_pass";
    public static final ResourceLocation TERRIER_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TERRIER_SOUND);
    public static SoundEvent TERRIER_SOUND_EVENT = SoundEvent.createVariableRangeEvent(TERRIER_SOUND_ID);

    public static final String WORLD_SUMMON_SOUND = "summon_world";
    public static final ResourceLocation WORLD_SUMMON_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+WORLD_SUMMON_SOUND);
    public static SoundEvent WORLD_SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(WORLD_SUMMON_SOUND_ID);

    public static final String STAR_SUMMON_SOUND = "summon_star";
    public static final ResourceLocation STAR_SUMMON_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_SUMMON_SOUND);
    public static SoundEvent STAR_SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_SUMMON_SOUND_ID);

    public static final String KNIFE_THROW_SOUND = "knife_throw";
    public static final ResourceLocation KNIFE_THROW_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KNIFE_THROW_SOUND);
    public static SoundEvent KNIFE_THROW_SOUND_EVENT = SoundEvent.createVariableRangeEvent(KNIFE_THROW_SOUND_ID);
    public static final String KNIFE_BUNDLE_THROW_SOUND = "knife_bundle_throw";
    public static final ResourceLocation KNIFE_BUNDLE_THROW_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KNIFE_BUNDLE_THROW_SOUND);
    public static SoundEvent KNIFE_BUNDLE_THROW_SOUND_EVENT = SoundEvent.createVariableRangeEvent(KNIFE_BUNDLE_THROW_SOUND_ID);
    public static final String KNIFE_IMPACT = "knife_impact";
    public static final ResourceLocation KNIFE_IMPACT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KNIFE_IMPACT);
    public static SoundEvent KNIFE_IMPACT_EVENT = SoundEvent.createVariableRangeEvent(KNIFE_IMPACT_ID);

    public static final String PUNCH_1_SOUND = "punch_sfx1";
    public static final ResourceLocation PUNCH_1_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PUNCH_1_SOUND);
    public static SoundEvent PUNCH_1_SOUND_EVENT = SoundEvent.createVariableRangeEvent(PUNCH_1_SOUND_ID);

    public static final String PUNCH_2_SOUND = "punch_sfx2";
    public static final ResourceLocation PUNCH_2_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PUNCH_2_SOUND);
    public static SoundEvent PUNCH_2_SOUND_EVENT = SoundEvent.createVariableRangeEvent(PUNCH_2_SOUND_ID);

    public static final String PUNCH_3_SOUND = "punch_sfx3";
    public static final ResourceLocation PUNCH_3_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PUNCH_3_SOUND);
    public static SoundEvent PUNCH_3_SOUND_EVENT = SoundEvent.createVariableRangeEvent(PUNCH_3_SOUND_ID);

    public static final String PUNCH_4_SOUND = "punch_sfx4";
    public static final ResourceLocation PUNCH_4_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PUNCH_4_SOUND);
    public static SoundEvent PUNCH_4_SOUND_EVENT = SoundEvent.createVariableRangeEvent(PUNCH_4_SOUND_ID);

    public static final String STAND_THEWORLD_MUDA1_SOUND = "stand_theworld_muda1";
    public static final ResourceLocation STAND_THEWORLD_MUDA1_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_THEWORLD_MUDA1_SOUND);
    public static SoundEvent STAND_THEWORLD_MUDA1_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA1_SOUND_ID);

    public static final String STAND_THEWORLD_MUDA2_SOUND = "stand_theworld_muda2";
    public static final ResourceLocation STAND_THEWORLD_MUDA2_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_THEWORLD_MUDA2_SOUND);
    public static SoundEvent STAND_THEWORLD_MUDA2_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA2_SOUND_ID);

    public static final String STAND_THEWORLD_MUDA3_SOUND = "stand_theworld_muda3";
    public static final ResourceLocation STAND_THEWORLD_MUDA3_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_THEWORLD_MUDA3_SOUND);
    public static SoundEvent STAND_THEWORLD_MUDA3_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA3_SOUND_ID);

    public static final String STAND_THEWORLD_MUDA4_SOUND = "stand_theworld_muda4";
    public static final ResourceLocation STAND_THEWORLD_MUDA4_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_THEWORLD_MUDA4_SOUND);
    public static SoundEvent STAND_THEWORLD_MUDA4_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA4_SOUND_ID);

    public static final String STAND_THEWORLD_MUDA5_SOUND = "stand_theworld_muda5";
    public static final ResourceLocation STAND_THEWORLD_MUDA5_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_THEWORLD_MUDA5_SOUND);
    public static SoundEvent STAND_THEWORLD_MUDA5_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_THEWORLD_MUDA5_SOUND_ID);

    public static final String STAND_BARRAGE_WINDUP = "stand_barrage_windup";
    public static final ResourceLocation STAND_BARRAGE_WINDUP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_BARRAGE_WINDUP);
    public static SoundEvent STAND_BARRAGE_WINDUP_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_WINDUP_ID);

    public static final String STAND_BARRAGE_MISS = "stand_barrage_miss";
    public static final ResourceLocation STAND_BARRAGE_MISS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_BARRAGE_MISS);
    public static SoundEvent STAND_BARRAGE_MISS_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_MISS_ID);

    public static final String STAND_BARRAGE_BLOCK = "stand_barrage_block";
    public static final ResourceLocation STAND_BARRAGE_BLOCK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_BARRAGE_BLOCK);
    public static SoundEvent STAND_BARRAGE_BLOCK_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_BLOCK_ID);

    public static final String STAND_BARRAGE_HIT = "stand_barrage_hit";
    public static final ResourceLocation STAND_BARRAGE_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_BARRAGE_HIT);
    public static SoundEvent STAND_BARRAGE_HIT_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_HIT_ID);
    public static final String STAND_BARRAGE_END_BLOCK = "stand_barrage_end_block";
    public static final ResourceLocation STAND_BARRAGE_END_BLOCK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_BARRAGE_END_BLOCK);
    public static SoundEvent STAND_BARRAGE_END_BLOCK_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_END_BLOCK_ID);

    public static final String STAND_BARRAGE_HIT2 = "stand_barrage_hit2";
    public static final ResourceLocation STAND_BARRAGE_HIT2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_BARRAGE_HIT2);
    public static SoundEvent STAND_BARRAGE_HIT2_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_HIT2_ID);

    public static final String STAND_BARRAGE_END = "stand_barrage_end";
    public static final ResourceLocation STAND_BARRAGE_END_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_BARRAGE_END);
    public static SoundEvent STAND_BARRAGE_END_EVENT = SoundEvent.createVariableRangeEvent(STAND_BARRAGE_END_ID);

    public static final String STAND_GUARD_SOUND = "stand_guard";
    public static final ResourceLocation STAND_GUARD_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_GUARD_SOUND);
    public static SoundEvent STAND_GUARD_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAND_GUARD_SOUND_ID);

    public static final String MELEE_GUARD_SOUND = "melee_guard";
    public static final ResourceLocation MELEE_GUARD_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MELEE_GUARD_SOUND);
    public static SoundEvent MELEE_GUARD_SOUND_EVENT = SoundEvent.createVariableRangeEvent(MELEE_GUARD_SOUND_ID);

    public static final String HIT_1_SOUND = "hit_sfx1";
    public static final ResourceLocation HIT_1_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HIT_1_SOUND);
    public static SoundEvent HIT_1_SOUND_EVENT = SoundEvent.createVariableRangeEvent(HIT_1_SOUND_ID);

    public static final String TIME_STOP_THE_WORLD = "timestop_the_world";
    public static final ResourceLocation TIME_STOP_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_THE_WORLD);
    public static SoundEvent TIME_STOP_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_THE_WORLD_ID);


    public static final String TIME_STOP_THE_WORLD2 = "timestop_the_world2";
    public static final ResourceLocation TIME_STOP_THE_WORLD2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_THE_WORLD2);
    public static SoundEvent TIME_STOP_THE_WORLD2_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_THE_WORLD2_ID);
    public static final String TIME_STOP_THE_WORLD3 = "timestop_the_world3";
    public static final ResourceLocation TIME_STOP_THE_WORLD3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_THE_WORLD3);
    public static SoundEvent TIME_STOP_THE_WORLD3_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_THE_WORLD3_ID);

    public static final String TIME_STOP_VOICE_THE_WORLD = "timestop_voice_theworld";
    public static final ResourceLocation TIME_STOP_VOICE_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_VOICE_THE_WORLD);
    public static SoundEvent TIME_STOP_VOICE_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_VOICE_THE_WORLD_ID);
    public static final String TIME_STOP_VOICE_THE_WORLD2 = "timestop_voice_theworld2";
    public static final ResourceLocation TIME_STOP_VOICE_THE_WORLD2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_VOICE_THE_WORLD2);
    public static SoundEvent TIME_STOP_VOICE_THE_WORLD2_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_VOICE_THE_WORLD2_ID);


    public static final String TIME_STOP_VOICE_THE_WORLD3 = "timestop_voice_theworld3";
    public static final ResourceLocation TIME_STOP_VOICE_THE_WORLD3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_VOICE_THE_WORLD3);
    public static SoundEvent TIME_STOP_VOICE_THE_WORLD3_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_VOICE_THE_WORLD3_ID);

    public static final String TIME_STOP_TICKING = "timestop_ticking";
    public static final ResourceLocation TIME_STOP_TICKING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_TICKING);
    public static SoundEvent TIME_STOP_TICKING_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_TICKING_ID);

    public static final String TIME_STOP_CHARGE_THE_WORLD = "timestop_charge_theworld";
    public static final ResourceLocation TIME_STOP_CHARGE_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_CHARGE_THE_WORLD);
    public static SoundEvent TIME_STOP_CHARGE_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_CHARGE_THE_WORLD_ID);

    public static final String TIME_STOP_RESUME_THE_WORLD = "timestop_resume_theworld";
    public static final ResourceLocation TIME_STOP_RESUME_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_RESUME_THE_WORLD);
    public static SoundEvent TIME_STOP_RESUME_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_RESUME_THE_WORLD_ID);

    public static final String TIME_STOP_RESUME_THE_WORLD2 = "timestop_resume_theworld2";
    public static final ResourceLocation TIME_STOP_RESUME_THE_WORLD2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_RESUME_THE_WORLD2);
    public static SoundEvent TIME_STOP_RESUME_THE_WORLD2_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_RESUME_THE_WORLD2_ID);

    public static final String TIME_STOP_IMPACT = "timestop_impact";
    public static final ResourceLocation TIME_STOP_IMPACT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_IMPACT);
    public static SoundEvent TIME_STOP_IMPACT_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_IMPACT_ID);

    public static final String TIME_STOP_IMPACT2 = "timestop_impact2";
    public static final ResourceLocation TIME_STOP_IMPACT2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_IMPACT2);
    public static SoundEvent TIME_STOP_IMPACT2_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_IMPACT2_ID);

    public static final String TIME_STOP_STAR_PLATINUM = "timestop_star_platinum";
    public static final ResourceLocation TIME_STOP_STAR_PLATINUM_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_STOP_STAR_PLATINUM);
    public static SoundEvent TIME_STOP_STAR_PLATINUM_EVENT = SoundEvent.createVariableRangeEvent(TIME_STOP_STAR_PLATINUM_ID);

    public static final String TIME_RESUME = "timeresume";
    public static final ResourceLocation TIME_RESUME_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_RESUME);
    public static SoundEvent TIME_RESUME_EVENT = SoundEvent.createVariableRangeEvent(TIME_RESUME_ID);

    public static final String CAN_BOUNCE = "can_bounce";
    public static final ResourceLocation CAN_BOUNCE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CAN_BOUNCE);
    public static SoundEvent CAN_BOUNCE_EVENT = SoundEvent.createVariableRangeEvent(CAN_BOUNCE_ID);

    public static final String CAN_BOUNCE_END = "can_bounce_end";
    public static final ResourceLocation CAN_BOUNCE_END_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CAN_BOUNCE_END);
    public static SoundEvent CAN_BOUNCE_END_EVENT = SoundEvent.createVariableRangeEvent(CAN_BOUNCE_END_ID);

    public static final String GASOLINE_EXPLOSION = "gasoline_explosion";
    public static final ResourceLocation GASOLINE_EXPLOSION_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GASOLINE_EXPLOSION);
    public static SoundEvent GASOLINE_EXPLOSION_EVENT = SoundEvent.createVariableRangeEvent(GASOLINE_EXPLOSION_ID);
    public static final String GAS_CAN_THROW = "gas_can_throw";
    public static final ResourceLocation GAS_CAN_THROW_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GAS_CAN_THROW);
    public static SoundEvent GAS_CAN_THROW_EVENT = SoundEvent.createVariableRangeEvent(GAS_CAN_THROW_ID);


    public static void registerSoundEvents(){
    }

    private static SoundEvent registerSoundEvent(String name){
        ResourceLocation id = new ResourceLocation(Roundabout.MOD_ID,name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
