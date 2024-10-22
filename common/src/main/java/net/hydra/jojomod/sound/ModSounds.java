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
    public static final String STAND_ARROW_CHARGE = "stand_arrow_charge";
    public static final ResourceLocation STAND_ARROW_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_ARROW_CHARGE);
    public static SoundEvent STAND_ARROW_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(STAND_ARROW_CHARGE_ID);
    public static final String STAND_ARROW_USE = "stand_arrow_use";
    public static final ResourceLocation STAND_ARROW_USE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_ARROW_USE);
    public static SoundEvent STAND_ARROW_USE_EVENT = SoundEvent.createVariableRangeEvent(STAND_ARROW_USE_ID);

    public static final String INHALE = "inhale";
    public static final ResourceLocation INHALE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+INHALE);
    public static SoundEvent INHALE_EVENT = SoundEvent.createVariableRangeEvent(INHALE_ID);
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

    public static final String KNIFE_IMPACT_GROUND = "knife_impact_ground";
    public static final ResourceLocation KNIFE_IMPACT_GROUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KNIFE_IMPACT_GROUND);
    public static SoundEvent KNIFE_IMPACT_GROUND_EVENT = SoundEvent.createVariableRangeEvent(KNIFE_IMPACT_GROUND_ID);

    public static final String LOCACACA_PETRIFY = "locacaca_petrify";
    public static final ResourceLocation LOCACACA_PETRIFY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+LOCACACA_PETRIFY);
    public static SoundEvent LOCACACA_PETRIFY_EVENT = SoundEvent.createVariableRangeEvent(LOCACACA_PETRIFY_ID);

    public static final String LOCACACA_FUSION = "locacaca_fusion";
    public static final ResourceLocation LOCACACA_FUSION_ID = new ResourceLocation(Roundabout.MOD_ID+":"+LOCACACA_FUSION);
    public static SoundEvent LOCACACA_FUSION_EVENT = SoundEvent.createVariableRangeEvent(LOCACACA_FUSION_ID);

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

    public static final String DODGE = "dodge";
    public static final ResourceLocation DODGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DODGE);
    public static SoundEvent DODGE_EVENT = SoundEvent.createVariableRangeEvent(DODGE_ID);
    public static final String STAND_LEAP = "stand_leap";
    public static final ResourceLocation STAND_LEAP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_LEAP);
    public static SoundEvent STAND_LEAP_EVENT = SoundEvent.createVariableRangeEvent(STAND_LEAP_ID);
    public static final String FALL_BRACE = "fall_brace";
    public static final ResourceLocation FALL_BRACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FALL_BRACE);
    public static SoundEvent FALL_BRACE_EVENT = SoundEvent.createVariableRangeEvent(FALL_BRACE_ID);

    public static final String TIME_SNAP = "time_snap";
    public static final ResourceLocation TIME_SNAP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TIME_SNAP);
    public static SoundEvent TIME_SNAP_EVENT = SoundEvent.createVariableRangeEvent(TIME_SNAP_ID);
    public static final String IMPALE_CHARGE = "impale_charge";
    public static final ResourceLocation IMPALE_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+IMPALE_CHARGE);
    public static SoundEvent IMPALE_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(IMPALE_CHARGE_ID);
    public static final String IMPALE_HIT = "impale_hit";
    public static final ResourceLocation IMPALE_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+IMPALE_HIT);
    public static SoundEvent IMPALE_HIT_EVENT = SoundEvent.createVariableRangeEvent(IMPALE_HIT_ID);

    public static final String THE_WORLD_ASSAULT = "the_world_assault";
    public static final ResourceLocation THE_WORLD_ASSAULT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+THE_WORLD_ASSAULT);
    public static SoundEvent THE_WORLD_ASSAULT_EVENT = SoundEvent.createVariableRangeEvent(THE_WORLD_ASSAULT_ID);

    public static final String THE_WORLD_WRY = "the_world_wry";
    public static final ResourceLocation THE_WORLD_WRY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+THE_WORLD_WRY);
    public static SoundEvent THE_WORLD_WRY_EVENT = SoundEvent.createVariableRangeEvent(THE_WORLD_WRY_ID);

    public static final String THE_WORLD_MUDA = "the_world_muda";
    public static final ResourceLocation THE_WORLD_MUDA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+THE_WORLD_MUDA);
    public static SoundEvent THE_WORLD_MUDA_EVENT = SoundEvent.createVariableRangeEvent(THE_WORLD_MUDA_ID);

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

    public static final String STAR_FINGER = "star_finger";
    public static final ResourceLocation STAR_FINGER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_FINGER);
    public static SoundEvent STAR_FINGER_EVENT = SoundEvent.createVariableRangeEvent(STAR_FINGER_ID);
    public static final String STAR_FINGER_2 = "star_finger_2";
    public static final ResourceLocation STAR_FINGER_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_FINGER_2);
    public static SoundEvent STAR_FINGER_2_EVENT = SoundEvent.createVariableRangeEvent(STAR_FINGER_2_ID);

    public static final String STAR_FINGER_SILENT = "star_finger_silent";
    public static final ResourceLocation STAR_FINGER_SILENT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_FINGER_SILENT);
    public static SoundEvent STAR_FINGER_SILENT_EVENT = SoundEvent.createVariableRangeEvent(STAR_FINGER_SILENT_ID);
    public static final String STAR_PLATINUM_ORA = "star_platinum_ora";
    public static final ResourceLocation STAR_PLATINUM_ORA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_PLATINUM_ORA);
    public static SoundEvent STAR_PLATINUM_ORA_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_PLATINUM_ORA_ID);

    public static final String STAR_PLATINUM_ORA_2 = "star_platinum_ora_2";
    public static final ResourceLocation STAR_PLATINUM_ORA_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_PLATINUM_ORA_2);
    public static SoundEvent STAR_PLATINUM_ORA_2_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_PLATINUM_ORA_2_ID);
    public static final String STAR_PLATINUM_ORA_3 = "star_platinum_ora_3";
    public static final ResourceLocation STAR_PLATINUM_ORA_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_PLATINUM_ORA_3);
    public static SoundEvent STAR_PLATINUM_ORA_3_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_PLATINUM_ORA_3_ID);


    public static final String STAR_PLATINUM_ORA_RUSH = "star_platinum_ora_rush";
    public static final ResourceLocation STAR_PLATINUM_ORA_RUSH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_PLATINUM_ORA_RUSH);
    public static SoundEvent STAR_PLATINUM_ORA_RUSH_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_PLATINUM_ORA_RUSH_ID);

    public static final String STAR_PLATINUM_ORA_RUSH_2 = "star_platinum_ora_rush_2";
    public static final ResourceLocation STAR_PLATINUM_ORA_RUSH_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_PLATINUM_ORA_RUSH_2);
    public static SoundEvent STAR_PLATINUM_ORA_RUSH_2_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_PLATINUM_ORA_RUSH_2_ID);

    public static final String STAR_PLATINUM_SCOPE = "star_platinum_scope";
    public static final ResourceLocation STAR_PLATINUM_SCOPE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_PLATINUM_SCOPE);
    public static SoundEvent STAR_PLATINUM_SCOPE_EVENT = SoundEvent.createVariableRangeEvent(STAR_PLATINUM_SCOPE_ID);

    public static final String STAR_PLATINUM_TIMESTOP = "star_platinum_timestop";
    public static final ResourceLocation STAR_PLATINUM_TIMESTOP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_PLATINUM_TIMESTOP);
    public static SoundEvent STAR_PLATINUM_TIMESTOP_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_PLATINUM_TIMESTOP_ID);

    public static final String STAR_PLATINUM_TIMESTOP_2 = "star_platinum_timestop_2";
    public static final ResourceLocation STAR_PLATINUM_TIMESTOP_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_PLATINUM_TIMESTOP_2);
    public static SoundEvent STAR_PLATINUM_TIMESTOP_2_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_PLATINUM_TIMESTOP_2_ID);

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

    public static final String EXPLOSIVE_PUNCH = "explosive_punch";
    public static final ResourceLocation EXPLOSIVE_PUNCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EXPLOSIVE_PUNCH);
    public static SoundEvent EXPLOSIVE_PUNCH_EVENT = SoundEvent.createVariableRangeEvent(EXPLOSIVE_PUNCH_ID);

    public static final String FINAL_KICK = "final_kick";
    public static final ResourceLocation FINAL_KICK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FINAL_KICK);
    public static SoundEvent FINAL_KICK_EVENT = SoundEvent.createVariableRangeEvent(FINAL_KICK_ID);

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
    public static final String MATCH_THROW = "match_throw";
    public static final ResourceLocation MATCH_THROW_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MATCH_THROW);
    public static SoundEvent MATCH_THROW_EVENT = SoundEvent.createVariableRangeEvent(MATCH_THROW_ID);

    public static final String HARPOON_THROW = "harpoon_throw";
    public static final ResourceLocation HARPOON_THROW_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HARPOON_THROW);
    public static SoundEvent HARPOON_THROW_EVENT = SoundEvent.createVariableRangeEvent(HARPOON_THROW_ID);
    public static final String HARPOON_HIT = "harpoon_hit";
    public static final ResourceLocation HARPOON_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HARPOON_HIT);
    public static SoundEvent HARPOON_HIT_EVENT = SoundEvent.createVariableRangeEvent(HARPOON_HIT_ID);
    public static final String HARPOON_CRIT = "harpoon_crit";
    public static final ResourceLocation HARPOON_CRIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HARPOON_CRIT);
    public static SoundEvent HARPOON_CRIT_EVENT = SoundEvent.createVariableRangeEvent(HARPOON_CRIT_ID);
    public static final String HARPOON_GROUND = "harpoon_ground";
    public static final ResourceLocation HARPOON_GROUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HARPOON_GROUND);
    public static SoundEvent HARPOON_GROUND_EVENT = SoundEvent.createVariableRangeEvent(HARPOON_GROUND_ID);
    public static final String HARPOON_RETURN = "harpoon_return";
    public static final ResourceLocation HARPOON_RETURN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HARPOON_RETURN);
    public static SoundEvent HARPOON_RETURN_EVENT = SoundEvent.createVariableRangeEvent(HARPOON_RETURN_ID);

    public static final String GLAIVE_CHARGE = "glaive_charge";
    public static final ResourceLocation GLAIVE_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GLAIVE_CHARGE);
    public static SoundEvent GLAIVE_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(GLAIVE_CHARGE_ID);
    public static final String GLAIVE_ATTACK = "glaive_attack";
    public static final ResourceLocation GLAIVE_ATTACK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GLAIVE_ATTACK);
    public static SoundEvent GLAIVE_ATTACK_EVENT = SoundEvent.createVariableRangeEvent(GLAIVE_ATTACK_ID);


    public static final String BLOCK_GRAB = "block_grab";
    public static final ResourceLocation BLOCK_GRAB_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLOCK_GRAB);
    public static SoundEvent BLOCK_GRAB_EVENT = SoundEvent.createVariableRangeEvent(BLOCK_GRAB_ID);
    public static final String BLOCK_THROW = "block_throw";
    public static final ResourceLocation BLOCK_THROW_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLOCK_THROW);
    public static SoundEvent BLOCK_THROW_EVENT = SoundEvent.createVariableRangeEvent(BLOCK_THROW_ID);
    public static final String ITEM_CATCH = "star_platinum_catch";
    public static final ResourceLocation ITEM_CATCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ITEM_CATCH);
    public static SoundEvent ITEM_CATCH_EVENT = SoundEvent.createVariableRangeEvent(ITEM_CATCH_ID);



    public static final String BALL_BEARING_SHOT = "ball_bearing_shot";
    public static final ResourceLocation BALL_BEARING_SHOT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BALL_BEARING_SHOT);
    public static SoundEvent BALL_BEARING_SHOT_EVENT = SoundEvent.createVariableRangeEvent(BALL_BEARING_SHOT_ID);

    public static final String TORTURE_DANCE = "torture_dance";
    public static final ResourceLocation TORTURE_DANCE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TORTURE_DANCE);
    public static SoundEvent TORTURE_DANCE_EVENT = SoundEvent.createVariableRangeEvent(TORTURE_DANCE_ID);
    public static final String HALLELUJAH = "hallelujah";
    public static final ResourceLocation HALLELUJAH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HALLELUJAH);
    public static SoundEvent HALLELUJAH_EVENT = SoundEvent.createVariableRangeEvent(HALLELUJAH_ID);


    public static void registerSoundEvents(){
    }

    private static SoundEvent registerSoundEvent(String name){
        ResourceLocation id = new ResourceLocation(Roundabout.MOD_ID,name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
