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
    public static final String SIGN_HIT = "sign_hit";
    public static final ResourceLocation SIGN_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SIGN_HIT);
    public static SoundEvent SIGN_HIT_EVENT = SoundEvent.createVariableRangeEvent(SIGN_HIT_ID);

    public static final String OVA_THE_WORLD = "ova_the_world";
    public static final ResourceLocation OVA_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_THE_WORLD);
    public static SoundEvent OVA_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(OVA_THE_WORLD_ID);
    public static final String OVA_THE_WORLD_2 = "ova_the_world_2";
    public static final ResourceLocation OVA_THE_WORLD_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_THE_WORLD_2);
    public static SoundEvent OVA_THE_WORLD_2_EVENT = SoundEvent.createVariableRangeEvent(OVA_THE_WORLD_2_ID);
    public static final String OVA_BARRAGE = "ova_barrage";
    public static final ResourceLocation OVA_BARRAGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_BARRAGE);
    public static SoundEvent OVA_BARRAGE_EVENT = SoundEvent.createVariableRangeEvent(OVA_BARRAGE_ID);
    public static final String OVA_BARRAGE_2 = "ova_barrage_2";
    public static final ResourceLocation OVA_BARRAGE_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_BARRAGE_2);
    public static SoundEvent OVA_BARRAGE_2_EVENT = SoundEvent.createVariableRangeEvent(OVA_BARRAGE_2_ID);
    public static final String OVA_MUDA = "ova_muda";
    public static final ResourceLocation OVA_MUDA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_MUDA);
    public static SoundEvent OVA_MUDA_EVENT = SoundEvent.createVariableRangeEvent(OVA_MUDA_ID);
    public static final String OVA_MUDA_2 = "ova_muda_2";
    public static final ResourceLocation OVA_MUDA_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_MUDA_2);
    public static SoundEvent OVA_MUDA_2_EVENT = SoundEvent.createVariableRangeEvent(OVA_MUDA_2_ID);
    public static final String OVA_SUMMON_THE_WORLD = "ova_summon_the_world";
    public static final ResourceLocation OVA_SUMMON_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_SUMMON_THE_WORLD);
    public static SoundEvent OVA_SUMMON_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(OVA_SUMMON_THE_WORLD_ID);
    public static final String OVA_TIME_RESUME = "ova_time_resume";
    public static final ResourceLocation OVA_TIME_RESUME_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_TIME_RESUME);
    public static SoundEvent OVA_TIME_RESUME_EVENT = SoundEvent.createVariableRangeEvent(OVA_TIME_RESUME_ID);
    public static final String OVA_SHORT_TS = "ova_short_ts";
    public static final ResourceLocation OVA_SHORT_TS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_SHORT_TS);
    public static SoundEvent OVA_SHORT_TS_EVENT = SoundEvent.createVariableRangeEvent(OVA_SHORT_TS_ID);
    public static final String OVA_LONG_TS = "ova_long_ts";
    public static final ResourceLocation OVA_LONG_TS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_LONG_TS);
    public static SoundEvent OVA_LONG_TS_EVENT = SoundEvent.createVariableRangeEvent(OVA_LONG_TS_ID);
    public static final String OVA_SP_TS = "ova_sp_ts";
    public static final ResourceLocation OVA_SP_TS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_SP_TS);
    public static SoundEvent OVA_SP_TS_EVENT = SoundEvent.createVariableRangeEvent(OVA_SP_TS_ID);
    public static final String TWAU_HEY = "twau_hey";
    public static final ResourceLocation TWAU_HEY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_HEY);
    public static SoundEvent TWAU_HEY_EVENT = SoundEvent.createVariableRangeEvent(TWAU_HEY_ID);
    public static final String TWAU_THE_WORLD = "twau_the_world";
    public static final ResourceLocation TWAU_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_THE_WORLD);
    public static SoundEvent TWAU_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(TWAU_THE_WORLD_ID);
    public static final String TWAU_TIMESTOP = "twau_timestop";
    public static final ResourceLocation TWAU_TIMESTOP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_TIMESTOP);
    public static SoundEvent TWAU_TIMESTOP_EVENT = SoundEvent.createVariableRangeEvent(TWAU_TIMESTOP_ID);
    public static final String TWAU_TIMESTOP_2 = "twau_timestop_2";
    public static final ResourceLocation TWAU_TIMESTOP_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_TIMESTOP_2);
    public static SoundEvent TWAU_TIMESTOP_2_EVENT = SoundEvent.createVariableRangeEvent(TWAU_TIMESTOP_2_ID);
    public static final String TWAU_USHA = "twau_usha";
    public static final ResourceLocation TWAU_USHA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_USHA);
    public static SoundEvent TWAU_USHA_EVENT = SoundEvent.createVariableRangeEvent(TWAU_USHA_ID);
    public static final String TWAU_MUDA = "twau_muda";
    public static final ResourceLocation TWAU_MUDA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_MUDA);
    public static SoundEvent TWAU_MUDA_EVENT = SoundEvent.createVariableRangeEvent(TWAU_MUDA_ID);
    public static final String TWAU_MUDA_2 = "twau_muda_2";
    public static final ResourceLocation TWAU_MUDA_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_MUDA_2);
    public static SoundEvent TWAU_MUDA_2_EVENT = SoundEvent.createVariableRangeEvent(TWAU_MUDA_2_ID);
    public static final String TWAU_MUDA_3 = "twau_muda_3";
    public static final ResourceLocation TWAU_MUDA_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_MUDA_3);
    public static SoundEvent TWAU_MUDA_3_EVENT = SoundEvent.createVariableRangeEvent(TWAU_MUDA_3_ID);
    public static final String TWAU_BARRAGE = "twau_barrage";
    public static final ResourceLocation TWAU_BARRAGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_BARRAGE);
    public static SoundEvent TWAU_BARRAGE_EVENT = SoundEvent.createVariableRangeEvent(TWAU_BARRAGE_ID);
    public static final String TWAU_BARRAGE_2 = "twau_barrage_2";
    public static final ResourceLocation TWAU_BARRAGE_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_BARRAGE_2);
    public static SoundEvent TWAU_BARRAGE_2_EVENT = SoundEvent.createVariableRangeEvent(TWAU_BARRAGE_2_ID);
    public static final String TWAU_WRY = "twau_wry";
    public static final ResourceLocation TWAU_WRY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TWAU_WRY);
    public static SoundEvent TWAU_WRY_EVENT = SoundEvent.createVariableRangeEvent(TWAU_WRY_ID);
    public static final String OVA_PLATINUM_BARRAGE = "ova_platinum_barrage";
    public static final ResourceLocation OVA_PLATINUM_BARRAGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_PLATINUM_BARRAGE);
    public static SoundEvent OVA_PLATINUM_BARRAGE_EVENT = SoundEvent.createVariableRangeEvent(OVA_PLATINUM_BARRAGE_ID);
    public static final String OVA_PLATINUM_ORA = "ova_platinum_ora";
    public static final ResourceLocation OVA_PLATINUM_ORA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_PLATINUM_ORA);
    public static SoundEvent OVA_PLATINUM_ORA_EVENT = SoundEvent.createVariableRangeEvent(OVA_PLATINUM_ORA_ID);
    public static final String OVA_PLATINUM_ORA_2 = "ova_platinum_ora_2";
    public static final ResourceLocation OVA_PLATINUM_ORA_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_PLATINUM_ORA_2);
    public static SoundEvent OVA_PLATINUM_ORA_2_EVENT = SoundEvent.createVariableRangeEvent(OVA_PLATINUM_ORA_2_ID);
    public static final String OVA_PLATINUM_ORA_3 = "ova_platinum_ora_3";
    public static final ResourceLocation OVA_PLATINUM_ORA_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_PLATINUM_ORA_3);
    public static SoundEvent OVA_PLATINUM_ORA_3_EVENT = SoundEvent.createVariableRangeEvent(OVA_PLATINUM_ORA_3_ID);
    public static final String OVA_PLATINUM_ORA_4 = "ova_platinum_ora_4";
    public static final ResourceLocation OVA_PLATINUM_ORA_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_PLATINUM_ORA_4);
    public static SoundEvent OVA_PLATINUM_ORA_4_EVENT = SoundEvent.createVariableRangeEvent(OVA_PLATINUM_ORA_4_ID);
    public static final String WORLD_SUMMON_SOUND = "summon_world";
    public static final ResourceLocation WORLD_SUMMON_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+WORLD_SUMMON_SOUND);
    public static SoundEvent WORLD_SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(WORLD_SUMMON_SOUND_ID);
    public static final String DSP_SUMMON = "dsp_summon";
    public static final ResourceLocation DSP_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DSP_SUMMON);
    public static SoundEvent DSP_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(DSP_SUMMON_ID);

    public static final String STAR_SUMMON_SOUND = "summon_star";
    public static final ResourceLocation STAR_SUMMON_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_SUMMON_SOUND);
    public static SoundEvent STAR_SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_SUMMON_SOUND_ID);

    public static final String SUMMON_MANDOM = "summon_mandom";
    public static final ResourceLocation SUMMON_MANDOM_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_MANDOM);
    public static SoundEvent SUMMON_MANDOM_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_MANDOM_ID);

    public static final String MANDOM_REWIND = "mandom_rewind";
    public static final ResourceLocation MANDOM_REWIND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MANDOM_REWIND);
    public static SoundEvent MANDOM_REWIND_EVENT = SoundEvent.createVariableRangeEvent(MANDOM_REWIND_ID);

    public static final String KNIFE_THROW_SOUND = "knife_throw";
    public static final ResourceLocation KNIFE_THROW_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KNIFE_THROW_SOUND);
    public static SoundEvent KNIFE_THROW_SOUND_EVENT = SoundEvent.createVariableRangeEvent(KNIFE_THROW_SOUND_ID);
    public static final String KNIFE_BUNDLE_THROW_SOUND = "knife_bundle_throw";
    public static final ResourceLocation KNIFE_BUNDLE_THROW_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KNIFE_BUNDLE_THROW_SOUND);
    public static SoundEvent KNIFE_BUNDLE_THROW_SOUND_EVENT = SoundEvent.createVariableRangeEvent(KNIFE_BUNDLE_THROW_SOUND_ID);
    public static final String KNIFE_IMPACT = "knife_impact";
    public static final ResourceLocation KNIFE_IMPACT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KNIFE_IMPACT);
    public static SoundEvent KNIFE_IMPACT_EVENT = SoundEvent.createVariableRangeEvent(KNIFE_IMPACT_ID);
    public static final String FOG_MORPH = "fog_morph";
    public static final ResourceLocation FOG_MORPH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FOG_MORPH);
    public static SoundEvent FOG_MORPH_EVENT = SoundEvent.createVariableRangeEvent(FOG_MORPH_ID);
    public static final String CACKLE = "cackle";
    public static final ResourceLocation CACKLE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CACKLE);
    public static SoundEvent CACKLE_EVENT = SoundEvent.createVariableRangeEvent(CACKLE_ID);
    public static final String BUBBLE_HOVERED_OVER = "bubble_hover_over";
    public static final ResourceLocation BUBBLE_HOVERED_OVER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BUBBLE_HOVERED_OVER);
    public static SoundEvent BUBBLE_HOVERED_OVER_EVENT = SoundEvent.createVariableRangeEvent(BUBBLE_HOVERED_OVER_ID);

    public static final String GO_BEYOND_LAUNCH = "go_beyond_launch";
    public static final ResourceLocation GO_BEYOND_LAUNCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GO_BEYOND_LAUNCH);
    public static SoundEvent GO_BEYOND_LAUNCH_EVENT = SoundEvent.createVariableRangeEvent(GO_BEYOND_LAUNCH_ID);
    public static final String GO_BEYOND_HIT = "go_beyond_hit";
    public static final ResourceLocation GO_BEYOND_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GO_BEYOND_HIT);
    public static SoundEvent GO_BEYOND_HIT_EVENT = SoundEvent.createVariableRangeEvent(GO_BEYOND_HIT_ID);

    public static final String AIR_BUBBLE = "air_bubble";
    public static final ResourceLocation AIR_BUBBLE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+AIR_BUBBLE);
    public static SoundEvent AIR_BUBBLE_EVENT = SoundEvent.createVariableRangeEvent(AIR_BUBBLE_ID);
    public static final String BUBBLE_POP = "bubble_pop";
    public static final ResourceLocation BUBBLE_POP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BUBBLE_POP);
    public static SoundEvent BUBBLE_POP_EVENT = SoundEvent.createVariableRangeEvent(BUBBLE_POP_ID);

    public static final String EXPLOSIVE_BUBBLE_POP = "explosive_bubble_pop";
    public static final ResourceLocation EXPLOSIVE_BUBBLE_POP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EXPLOSIVE_BUBBLE_POP);
    public static SoundEvent EXPLOSIVE_BUBBLE_POP_EVENT = SoundEvent.createVariableRangeEvent(EXPLOSIVE_BUBBLE_POP_ID);
    public static final String EXPLOSIVE_BUBBLE_SHOT = "explosive_bubble_shot";
    public static final ResourceLocation EXPLOSIVE_BUBBLE_SHOT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EXPLOSIVE_BUBBLE_SHOT);
    public static SoundEvent EXPLOSIVE_BUBBLE_SHOT_EVENT = SoundEvent.createVariableRangeEvent(EXPLOSIVE_BUBBLE_SHOT_ID);
    public static final String EXPLOSIVE_BUBBLE_SWITCH = "explosive_bubble_switch";
    public static final ResourceLocation EXPLOSIVE_BUBBLE_SWITCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EXPLOSIVE_BUBBLE_SWITCH);
    public static SoundEvent EXPLOSIVE_BUBBLE_SWITCH_EVENT = SoundEvent.createVariableRangeEvent(EXPLOSIVE_BUBBLE_SWITCH_ID);
    public static final String EXPLOSIVE_BUBBLE_SWITCH_OFF = "explosive_bubble_switch_off";
    public static final ResourceLocation EXPLOSIVE_BUBBLE_SWITCH_OFF_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EXPLOSIVE_BUBBLE_SWITCH_OFF);
    public static SoundEvent EXPLOSIVE_BUBBLE_SWITCH_OFF_EVENT = SoundEvent.createVariableRangeEvent(EXPLOSIVE_BUBBLE_SWITCH_OFF_ID);
    public static final String EXPLOSIVE_SPIN_MODE = "explosive_spin_mode";
    public static final ResourceLocation EXPLOSIVE_SPIN_MODE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EXPLOSIVE_SPIN_MODE);
    public static SoundEvent EXPLOSIVE_SPIN_MODE_EVENT = SoundEvent.createVariableRangeEvent(EXPLOSIVE_SPIN_MODE_ID);

    public static final String BUBBLE_CREATE = "bubble_create";
    public static final ResourceLocation BUBBLE_CREATE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BUBBLE_CREATE);
    public static SoundEvent BUBBLE_CREATE_EVENT = SoundEvent.createVariableRangeEvent(BUBBLE_CREATE_ID);
    public static final String BIG_BUBBLE_CREATE = "big_bubble_create";
    public static final ResourceLocation BIG_BUBBLE_CREATE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BIG_BUBBLE_CREATE);
    public static SoundEvent BIG_BUBBLE_CREATE_EVENT = SoundEvent.createVariableRangeEvent(BIG_BUBBLE_CREATE_ID);

    public static final String BUBBLE_PLUNDER = "bubble_plunder";
    public static final ResourceLocation BUBBLE_PLUNDER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BUBBLE_PLUNDER);
    public static SoundEvent BUBBLE_PLUNDER_EVENT = SoundEvent.createVariableRangeEvent(BUBBLE_PLUNDER_ID);
    public static final String SOFT_AND_WET_BARRAGE = "soft_and_wet_barrage";
    public static final ResourceLocation SOFT_AND_WET_BARRAGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SOFT_AND_WET_BARRAGE);
    public static SoundEvent SOFT_AND_WET_BARRAGE_EVENT = SoundEvent.createVariableRangeEvent(SOFT_AND_WET_BARRAGE_ID);
    public static final String SOFT_AND_WET_BARRAGE_2 = "soft_and_wet_barrage_2";
    public static final ResourceLocation SOFT_AND_WET_BARRAGE_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SOFT_AND_WET_BARRAGE_2);
    public static SoundEvent SOFT_AND_WET_BARRAGE_2_EVENT = SoundEvent.createVariableRangeEvent(SOFT_AND_WET_BARRAGE_2_ID);

    public static final String WATER_ENCASE = "water_encase";
    public static final ResourceLocation WATER_ENCASE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+WATER_ENCASE);
    public static SoundEvent WATER_ENCASE_EVENT = SoundEvent.createVariableRangeEvent(WATER_ENCASE_ID);

    public static final String SOFT_AND_WET_KICK = "soft_and_wet_kick";
    public static final ResourceLocation SOFT_AND_WET_KICK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SOFT_AND_WET_KICK);
    public static SoundEvent SOFT_AND_WET_KICK_EVENT = SoundEvent.createVariableRangeEvent(SOFT_AND_WET_KICK_ID);
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

    public static final String BODY_BAG = "body_bag";
    public static final ResourceLocation BODY_BAG_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BODY_BAG);
    public static SoundEvent BODY_BAG_EVENT = SoundEvent.createVariableRangeEvent(BODY_BAG_ID);

    public static final String JUSTICE_SELECT = "justice_select";
    public static final ResourceLocation JUSTICE_SELECT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JUSTICE_SELECT);
    public static SoundEvent JUSTICE_SELECT_EVENT = SoundEvent.createVariableRangeEvent(JUSTICE_SELECT_ID);

    public static final String JUSTICE_SELECT_ATTACK = "justice_select_attack";
    public static final ResourceLocation JUSTICE_SELECT_ATTACK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JUSTICE_SELECT_ATTACK);
    public static SoundEvent JUSTICE_SELECT_ATTACK_EVENT = SoundEvent.createVariableRangeEvent(JUSTICE_SELECT_ATTACK_ID);

    public static final String DODGE = "dodge";
    public static final ResourceLocation DODGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DODGE);
    public static SoundEvent DODGE_EVENT = SoundEvent.createVariableRangeEvent(DODGE_ID);
    public static final String STAND_LEAP = "stand_leap";
    public static final ResourceLocation STAND_LEAP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_LEAP);
    public static SoundEvent STAND_LEAP_EVENT = SoundEvent.createVariableRangeEvent(STAND_LEAP_ID);
    public static final String FALL_BRACE = "fall_brace";
    public static final ResourceLocation FALL_BRACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FALL_BRACE);
    public static SoundEvent FALL_BRACE_EVENT = SoundEvent.createVariableRangeEvent(FALL_BRACE_ID);

    public static final String LEVELUP = "levelup";
    public static final ResourceLocation LEVELUP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+LEVELUP);
    public static SoundEvent LEVELUP_EVENT = SoundEvent.createVariableRangeEvent(LEVELUP_ID);
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

    public static final String SUMMON_SOFT_AND_WET = "summon_soft_and_wet";
    public static final ResourceLocation SUMMON_SOFT_AND_WET_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_SOFT_AND_WET);
    public static SoundEvent SUMMON_SOFT_AND_WET_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_SOFT_AND_WET_ID);

    public static final String SUMMON_JUSTICE = "summon_justice";
    public static final ResourceLocation SUMMON_JUSTICE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_JUSTICE);
    public static SoundEvent SUMMON_JUSTICE_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_JUSTICE_ID);

    public static final String SUMMON_JUSTICE_2 = "summon_justice_2";
    public static final ResourceLocation SUMMON_JUSTICE_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_JUSTICE_2);
    public static SoundEvent SUMMON_JUSTICE_2_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_JUSTICE_2_ID);

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
    public static final String SNAP = "snap";
    public static final ResourceLocation SNAP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SNAP);
    public static SoundEvent SNAP_EVENT = SoundEvent.createVariableRangeEvent(SNAP_ID);
    public static final String FIRE_BLAST = "fire_blast";
    public static final ResourceLocation FIRE_BLAST_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FIRE_BLAST);
    public static SoundEvent FIRE_BLAST_EVENT = SoundEvent.createVariableRangeEvent(FIRE_BLAST_ID);
    public static final String FIRESTORM = "firestorm";
    public static final ResourceLocation FIRESTORM_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FIRESTORM);
    public static SoundEvent FIRESTORM_EVENT = SoundEvent.createVariableRangeEvent(FIRESTORM_ID);
    public static final String CROSSFIRE_SHOOT = "crossfire_shoot";
    public static final ResourceLocation CROSSFIRE_SHOOT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CROSSFIRE_SHOOT);
    public static SoundEvent CROSSFIRE_SHOOT_EVENT = SoundEvent.createVariableRangeEvent(CROSSFIRE_SHOOT_ID);
    public static final String CROSSFIRE_EXPLODE = "crossfire_explode";
    public static final ResourceLocation CROSSFIRE_EXPLODE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CROSSFIRE_EXPLODE);
    public static SoundEvent CROSSFIRE_EXPLODE_EVENT = SoundEvent.createVariableRangeEvent(CROSSFIRE_EXPLODE_ID);
    public static final String MAGICIANS_RED_CRY = "magicians_red_cry";
    public static final ResourceLocation MAGICIANS_RED_CRY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MAGICIANS_RED_CRY);
    public static SoundEvent MAGICIANS_RED_CRY_EVENT = SoundEvent.createVariableRangeEvent(MAGICIANS_RED_CRY_ID);
    public static final String MAGICIANS_RED_CRY_2 = "magicians_red_cry_2";
    public static final ResourceLocation MAGICIANS_RED_CRY_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MAGICIANS_RED_CRY_2);
    public static SoundEvent MAGICIANS_RED_CRY_2_EVENT = SoundEvent.createVariableRangeEvent(MAGICIANS_RED_CRY_2_ID);
    public static final String MAGICIANS_RED_CRY_3 = "magicians_red_cry_3";
    public static final ResourceLocation MAGICIANS_RED_CRY_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MAGICIANS_RED_CRY_3);
    public static SoundEvent MAGICIANS_RED_CRY_3_EVENT = SoundEvent.createVariableRangeEvent(MAGICIANS_RED_CRY_3_ID);
    public static final String MAGICIANS_RED_CHARGE = "magicians_red_charge";
    public static final ResourceLocation MAGICIANS_RED_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MAGICIANS_RED_CHARGE);
    public static SoundEvent MAGICIANS_RED_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(MAGICIANS_RED_CHARGE_ID);
    public static final String STAND_FLAME_HIT = "stand_flame_hit";
    public static final ResourceLocation STAND_FLAME_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAND_FLAME_HIT);
    public static SoundEvent STAND_FLAME_HIT_EVENT = SoundEvent.createVariableRangeEvent(STAND_FLAME_HIT_ID);
    public static final String FIREBALL_SHOOT = "fireball_shoot";
    public static final ResourceLocation FIREBALL_SHOOT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FIREBALL_SHOOT);
    public static SoundEvent FIREBALL_SHOOT_EVENT = SoundEvent.createVariableRangeEvent(FIREBALL_SHOOT_ID);
    public static final String FIREBALL_HIT = "fireball_hit";
    public static final ResourceLocation FIREBALL_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FIREBALL_HIT);
    public static SoundEvent FIREBALL_HIT_EVENT = SoundEvent.createVariableRangeEvent(FIREBALL_HIT_ID);
    public static final String LASSO = "lasso";
    public static final ResourceLocation LASSO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+LASSO);
    public static SoundEvent LASSO_EVENT = SoundEvent.createVariableRangeEvent(LASSO_ID);
    public static final String FLAMETHROWER = "flamethrower";
    public static final ResourceLocation FLAMETHROWER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FLAMETHROWER);
    public static SoundEvent FLAMETHROWER_EVENT = SoundEvent.createVariableRangeEvent(FLAMETHROWER_ID);
    public static final String FIRE_WHOOSH = "fire_whoosh";
    public static final ResourceLocation FIRE_WHOOSH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FIRE_WHOOSH);
    public static SoundEvent FIRE_WHOOSH_EVENT = SoundEvent.createVariableRangeEvent(FIRE_WHOOSH_ID);
    public static final String FIRE_STRIKE = "fire_strike";
    public static final ResourceLocation FIRE_STRIKE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FIRE_STRIKE);
    public static SoundEvent FIRE_STRIKE_EVENT = SoundEvent.createVariableRangeEvent(FIRE_STRIKE_ID);
    public static final String FIRE_STRIKE_LAST = "fire_strike_last";
    public static final ResourceLocation FIRE_STRIKE_LAST_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FIRE_STRIKE_LAST);
    public static SoundEvent FIRE_STRIKE_LAST_EVENT = SoundEvent.createVariableRangeEvent(FIRE_STRIKE_LAST_ID);
    public static final String EXPLOSIVE_BAT = "explosive_bat";
    public static final ResourceLocation EXPLOSIVE_BAT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EXPLOSIVE_BAT);
    public static SoundEvent EXPLOSIVE_BAT_EVENT = SoundEvent.createVariableRangeEvent(EXPLOSIVE_BAT_ID);
    public static final String OVA_SUMMON = "ova_summon";
    public static final ResourceLocation OVA_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+OVA_SUMMON);
    public static SoundEvent OVA_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(OVA_SUMMON_ID);

    public static final String SUMMON_MAGICIAN = "summon_magician";
    public static final ResourceLocation SUMMON_MAGICIAN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_MAGICIAN);
    public static SoundEvent SUMMON_MAGICIAN_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_MAGICIAN_ID);

    public static final String FINAL_KICK = "final_kick";
    public static final ResourceLocation FINAL_KICK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FINAL_KICK);
    public static SoundEvent FINAL_KICK_EVENT = SoundEvent.createVariableRangeEvent(FINAL_KICK_ID);

    public static final String DREAD_SUMMON = "dread_summon";
    public static final ResourceLocation DREAD_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DREAD_SUMMON);
    public static SoundEvent DREAD_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(DREAD_SUMMON_ID);

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
    public static final String THE_WORLD_OVER_HEAVEN = "the_world_over_heaven";
    public static final ResourceLocation THE_WORLD_OVER_HEAVEN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+THE_WORLD_OVER_HEAVEN);
    public static SoundEvent THE_WORLD_OVER_HEAVEN_EVENT = SoundEvent.createVariableRangeEvent(THE_WORLD_OVER_HEAVEN_ID);


    public static final String  ARCADE_TIMESTOP = "arcade_timestop";
    public static final ResourceLocation ARCADE_TIMESTOP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_TIMESTOP);
    public static SoundEvent ARCADE_TIMESTOP_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_TIMESTOP_ID);
    public static final String  ARCADE_TIMESTOP_2 = "arcade_timestop_2";
    public static final ResourceLocation ARCADE_TIMESTOP_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_TIMESTOP_2);
    public static SoundEvent ARCADE_TIMESTOP_2_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_TIMESTOP_2_ID);
    public static final String  ARCADE_MUDA = "arcade_muda";
    public static final ResourceLocation ARCADE_MUDA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_MUDA);
    public static SoundEvent ARCADE_MUDA_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_MUDA_ID);
    public static final String  ARCADE_MUDA_2 = "arcade_muda_2";
    public static final ResourceLocation ARCADE_MUDA_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_MUDA_2);
    public static SoundEvent ARCADE_MUDA_2_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_MUDA_2_ID);
    public static final String  ARCADE_MUDA_3 = "arcade_muda_3";
    public static final ResourceLocation ARCADE_MUDA_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_MUDA_3);
    public static SoundEvent ARCADE_MUDA_3_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_MUDA_3_ID);
    public static final String  ARCADE_URI = "arcade_uri";
    public static final ResourceLocation ARCADE_URI_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_URI);
    public static SoundEvent ARCADE_URI_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_URI_ID);
    public static final String  ARCADE_BARRAGE = "arcade_barrage";
    public static final ResourceLocation ARCADE_BARRAGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_BARRAGE);
    public static SoundEvent ARCADE_BARRAGE_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_BARRAGE_ID);
    public static final String  ARCADE_IMPALE = "arcade_impale";
    public static final ResourceLocation ARCADE_IMPALE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_IMPALE);
    public static SoundEvent ARCADE_IMPALE_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_IMPALE_ID);
    public static final String  ARCADE_TIME_RESUME = "arcade_time_resume";
    public static final ResourceLocation ARCADE_TIME_RESUME_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_TIME_RESUME);
    public static SoundEvent ARCADE_TIME_RESUME_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_TIME_RESUME_ID);
    public static final String  ARCADE_LONG_TS = "arcade_long_ts";
    public static final ResourceLocation ARCADE_LONG_TS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_LONG_TS);
    public static SoundEvent ARCADE_LONG_TS_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_LONG_TS_ID);
    public static final String  ARCADE_SHORT_TS = "arcade_short_ts";
    public static final ResourceLocation ARCADE_SHORT_TS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_SHORT_TS);
    public static SoundEvent ARCADE_SHORT_TS_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_SHORT_TS_ID);


    public static final String  ARCADE_ORA = "arcade_ora_1";
    public static final ResourceLocation ARCADE_ORA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_ORA);
    public static SoundEvent ARCADE_ORA_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_ORA_ID);
    public static final String  ARCADE_ORA_2 = "arcade_ora_2";
    public static final ResourceLocation ARCADE_ORA_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_ORA_2);
    public static SoundEvent ARCADE_ORA_2_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_ORA_2_ID);
    public static final String  ARCADE_ORA_3 = "arcade_ora_3";
    public static final ResourceLocation ARCADE_ORA_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_ORA_3);
    public static SoundEvent ARCADE_ORA_3_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_ORA_3_ID);
    public static final String  ARCADE_STAR_PLATINUM_BARRAGE = "arcade_star_platinum_barrage";
    public static final ResourceLocation ARCADE_STAR_PLATINUM_BARRAGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_STAR_PLATINUM_BARRAGE);
    public static SoundEvent ARCADE_STAR_PLATINUM_BARRAGE_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_STAR_PLATINUM_BARRAGE_ID);
    public static final String  ARCADE_STAR_PLATINUM_TIME_STOP = "arcade_star_platinum_time_stop";
    public static final ResourceLocation ARCADE_STAR_PLATINUM_TIME_STOP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_STAR_PLATINUM_TIME_STOP);
    public static SoundEvent ARCADE_STAR_PLATINUM_TIME_STOP_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_STAR_PLATINUM_TIME_STOP_ID);
    public static final String  ARCADE_STAR_PLATINUM_SHORT_TS = "arcade_star_platinum_short_ts";
    public static final ResourceLocation ARCADE_STAR_PLATINUM_SHORT_TS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_STAR_PLATINUM_SHORT_TS);
    public static SoundEvent ARCADE_STAR_PLATINUM_SHORT_TS_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_STAR_PLATINUM_SHORT_TS_ID);
    public static final String  ARCADE_STAR_FINGER = "arcade_star_finger";
    public static final ResourceLocation ARCADE_STAR_FINGER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ARCADE_STAR_FINGER);
    public static SoundEvent ARCADE_STAR_FINGER_EVENT = SoundEvent.createVariableRangeEvent(ARCADE_STAR_FINGER_ID);

    public static final String CINDERELLA_SUMMON = "cinderella_summon";
    public static final ResourceLocation CINDERELLA_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CINDERELLA_SUMMON);
    public static SoundEvent CINDERELLA_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(CINDERELLA_SUMMON_ID);
    public static final String CINDERELLA_ATTACK = "cinderella_attack";
    public static final ResourceLocation CINDERELLA_ATTACK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CINDERELLA_ATTACK);
    public static SoundEvent CINDERELLA_ATTACK_EVENT = SoundEvent.createVariableRangeEvent(CINDERELLA_ATTACK_ID);

    public static final String CINDERELLA_SPARKLE = "cinderella_sparkle";
    public static final ResourceLocation CINDERELLA_SPARKLE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CINDERELLA_SPARKLE);
    public static SoundEvent CINDERELLA_SPARKLE_EVENT = SoundEvent.createVariableRangeEvent(CINDERELLA_SPARKLE_ID);
    public static final String CINDERELLA_FAIL = "cinderella_fail";
    public static final ResourceLocation CINDERELLA_FAIL_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CINDERELLA_FAIL);
    public static SoundEvent CINDERELLA_FAIL_EVENT = SoundEvent.createVariableRangeEvent(CINDERELLA_FAIL_ID);

    public static final String CINDERELLA_VISAGE_CREATION = "cinderella_visage_creation";
    public static final ResourceLocation CINDERELLA_VISAGE_CREATION_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CINDERELLA_VISAGE_CREATION);
    public static SoundEvent CINDERELLA_VISAGE_CREATION_EVENT = SoundEvent.createVariableRangeEvent(CINDERELLA_VISAGE_CREATION_ID);

    public static final String RATT_SUMMON = "ratt_summon";
    public static final ResourceLocation RATT_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_SUMMON);
    public static SoundEvent RATT_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(RATT_SUMMON_ID);
    public static final String RATT_PLACE = "ratt_place";
    public static final ResourceLocation RATT_PLACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_PLACE);
    public static SoundEvent RATT_PLACE_EVENT = SoundEvent.createVariableRangeEvent(RATT_PLACE_ID);
    public static final String RATT_SCOPE = "ratt_scope";
    public static final ResourceLocation RATT_SCOPE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_SCOPE);
    public static SoundEvent RATT_SCOPE_EVENT = SoundEvent.createVariableRangeEvent(RATT_SCOPE_ID);
    public static final String RATT_DESCOPE = "ratt_descope";
    public static final ResourceLocation RATT_DESCOPE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_DESCOPE);
    public static SoundEvent RATT_DESCOPE_EVENT = SoundEvent.createVariableRangeEvent(RATT_DESCOPE_ID);
    public static final String RATT_DART_THUNK = "ratt_dart_thunk";
    public static final ResourceLocation RATT_DART_THUNK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_DART_THUNK);
    public static SoundEvent RATT_DART_THUNK_EVENT = SoundEvent.createVariableRangeEvent(RATT_DART_THUNK_ID);
    public static final String RATT_DART_IMPACT = "ratt_dart_impact";
    public static final ResourceLocation RATT_DART_IMPACT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_DART_IMPACT);
    public static SoundEvent RATT_DART_IMPACT_EVENT = SoundEvent.createVariableRangeEvent(RATT_DART_THUNK_ID);
    public static final String RATT_LOADING = "ratt_loading";
    public static final ResourceLocation RATT_LOADING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_LOADING);
    public static SoundEvent RATT_LOADING_EVENT = SoundEvent.createVariableRangeEvent(RATT_LOADING_ID);
    public static final String RATT_FIRING = "ratt_firing";
    public static final ResourceLocation RATT_FIRING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_FIRING);
    public static SoundEvent RATT_FIRING_EVENT = SoundEvent.createVariableRangeEvent(RATT_FIRING_ID);

    public static final String UNLOCK_SKIN = "unlock_skin";
    public static final ResourceLocation UNLOCK_SKIN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+UNLOCK_SKIN);
    public static SoundEvent UNLOCK_SKIN_EVENT = SoundEvent.createVariableRangeEvent(UNLOCK_SKIN_ID);

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

    public static final String BOWLER_HAT_AIM_SOUND = "bowler_hat_aim";
    public static final ResourceLocation BOWLER_HAT_AIM_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BOWLER_HAT_AIM_SOUND);
    public static SoundEvent BOWLER_HAT_AIM_SOUND_EVENT = SoundEvent.createVariableRangeEvent(BOWLER_HAT_AIM_ID);
    public static final String BOWLER_HAT_FLY_SOUND = "bowler_hat_fly";
    public static final ResourceLocation BOWLER_HAT_FLY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BOWLER_HAT_FLY_SOUND);
    public static SoundEvent BOWLER_HAT_FLY_SOUND_EVENT = SoundEvent.createVariableRangeEvent(BOWLER_HAT_FLY_ID);

    public static final String GLAIVE_CHARGE = "glaive_charge";
    public static final ResourceLocation GLAIVE_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GLAIVE_CHARGE);
    public static SoundEvent GLAIVE_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(GLAIVE_CHARGE_ID);
    public static final String GLAIVE_ATTACK = "glaive_attack";
    public static final ResourceLocation GLAIVE_ATTACK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GLAIVE_ATTACK);
    public static SoundEvent GLAIVE_ATTACK_EVENT = SoundEvent.createVariableRangeEvent(GLAIVE_ATTACK_ID);

    public static final String FOG_CLONE = "fog_clone";
    public static final ResourceLocation FOG_CLONE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FOG_CLONE);
    public static SoundEvent FOG_CLONE_EVENT = SoundEvent.createVariableRangeEvent(FOG_CLONE_ID);
    public static final String POP = "pop";
    public static final ResourceLocation POP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+POP);
    public static SoundEvent POP_EVENT = SoundEvent.createVariableRangeEvent(POP_ID);


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



    public static final String HEY_YA_1 = "hey_ya_1";
    public static final ResourceLocation HEY_YA_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEY_YA_1);
    public static SoundEvent HEY_YA_1_EVENT = SoundEvent.createVariableRangeEvent(HEY_YA_1_ID);
    public static final String HEY_YA_2 = "hey_ya_2";
    public static final ResourceLocation HEY_YA_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEY_YA_2);
    public static SoundEvent HEY_YA_2_EVENT = SoundEvent.createVariableRangeEvent(HEY_YA_2_ID);
    public static final String HEY_YA_3 = "hey_ya_3";
    public static final ResourceLocation HEY_YA_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEY_YA_3);
    public static SoundEvent HEY_YA_3_EVENT = SoundEvent.createVariableRangeEvent(HEY_YA_3_ID);
    public static final String HEY_YA_4 = "hey_ya_4";
    public static final ResourceLocation HEY_YA_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEY_YA_4);
    public static SoundEvent HEY_YA_4_EVENT = SoundEvent.createVariableRangeEvent(HEY_YA_4_ID);
    public static final String HEY_YA_5 = "hey_ya_5";
    public static final ResourceLocation HEY_YA_5_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEY_YA_5);
    public static SoundEvent HEY_YA_5_EVENT = SoundEvent.createVariableRangeEvent(HEY_YA_5_ID);
    public static final String HEY_YA_6 = "hey_ya_6";
    public static final ResourceLocation HEY_YA_6_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEY_YA_6);
    public static SoundEvent HEY_YA_6_EVENT = SoundEvent.createVariableRangeEvent(HEY_YA_6_ID);
    public static final String HEY_YA_7 = "hey_ya_7";
    public static final ResourceLocation HEY_YA_7_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEY_YA_7);
    public static SoundEvent HEY_YA_7_EVENT = SoundEvent.createVariableRangeEvent(HEY_YA_7_ID);

    public static final String HEY_YA_SUMMON = "hey_ya_summon";
    public static final ResourceLocation HEY_YA_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEY_YA_SUMMON);
    public static SoundEvent HEY_YA_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(HEY_YA_SUMMON_ID);

    public static final String SUMMON_ACHTUNG = "summon_achtung";
    public static final ResourceLocation SUMMON_ACHTUNG_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_ACHTUNG);
    public static SoundEvent SUMMON_ACHTUNG_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_ACHTUNG_ID);
    public static final String SURVIVOR_SUMMON = "survivor_summon";
    public static final ResourceLocation SURVIVOR_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SURVIVOR_SUMMON);
    public static SoundEvent SURVIVOR_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(SURVIVOR_SUMMON_ID);

    public static final String SURVIVOR_SHOCK = "survivor_shock";
    public static final ResourceLocation SURVIVOR_SHOCK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SURVIVOR_SHOCK);
    public static SoundEvent SURVIVOR_SHOCK_EVENT = SoundEvent.createVariableRangeEvent(SURVIVOR_SHOCK_ID);

    public static final String SURVIVOR_PLACE = "survivor_place";
    public static final ResourceLocation SURVIVOR_PLACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SURVIVOR_PLACE);
    public static SoundEvent SURVIVOR_PLACE_EVENT = SoundEvent.createVariableRangeEvent(SURVIVOR_PLACE_ID);

    public static final String SURVIVOR_REMOVE = "survivor_remove";
    public static final ResourceLocation SURVIVOR_REMOVE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SURVIVOR_REMOVE);
    public static SoundEvent SURVIVOR_REMOVE_EVENT = SoundEvent.createVariableRangeEvent(SURVIVOR_REMOVE_ID);


    public static final String ACHTUNG_BURST = "achtung_burst";
    public static final ResourceLocation ACHTUNG_BURST_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ACHTUNG_BURST);
    public static SoundEvent ACHTUNG_BURST_EVENT = SoundEvent.createVariableRangeEvent(ACHTUNG_BURST_ID);


    public static final String SUMMON_DIVER_DOWN = "summon_diver_down";
    public static final ResourceLocation SUMMON_DIVER_DOWN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_DIVER_DOWN);
    public static SoundEvent SUMMON_DIVER_DOWN_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_DIVER_DOWN_ID);


    public static final String EXTEND_SPIKES = "extend_spikes";
    public static final ResourceLocation EXTEND_SPIKES_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EXTEND_SPIKES);
    public static SoundEvent EXTEND_SPIKES_EVENT = SoundEvent.createVariableRangeEvent(EXTEND_SPIKES_ID);
    public static final String WALL_LATCH = "wall_latch";
    public static final ResourceLocation WALL_LATCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+WALL_LATCH);
    public static SoundEvent WALL_LATCH_EVENT = SoundEvent.createVariableRangeEvent(WALL_LATCH_ID);


    public static final String STONE_MASK_ACTIVATE = "stone_mask_activate";
    public static final ResourceLocation STONE_MASK_ACTIVATE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STONE_MASK_ACTIVATE);
    public static SoundEvent STONE_MASK_ACTIVATE_EVENT = SoundEvent.createVariableRangeEvent(STONE_MASK_ACTIVATE_ID);


    public static final String SUMMON_WALKING = "summon_walking";
    public static final ResourceLocation SUMMON_WALKING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_WALKING);
    public static SoundEvent SUMMON_WALKING_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_WALKING_ID);

    public static final String HEEL_RAISE = "heel_raise";
    public static final ResourceLocation HEEL_RAISE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEEL_RAISE);
    public static SoundEvent HEEL_RAISE_EVENT = SoundEvent.createVariableRangeEvent(HEEL_RAISE_ID);
    public static final String HEEL_STOMP = "heel_stomp";
    public static final ResourceLocation HEEL_STOMP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEEL_STOMP);
    public static SoundEvent HEEL_STOMP_EVENT = SoundEvent.createVariableRangeEvent(HEEL_STOMP_ID);


    public static final String SPIKE_HIT = "spike_hit";
    public static final ResourceLocation SPIKE_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SPIKE_HIT);
    public static SoundEvent SPIKE_HIT_EVENT = SoundEvent.createVariableRangeEvent(SPIKE_HIT_ID);
    public static final String SPIKE_MISS = "spike_miss";
    public static final ResourceLocation SPIKE_MISS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SPIKE_MISS);
    public static SoundEvent SPIKE_MISS_EVENT = SoundEvent.createVariableRangeEvent(SPIKE_MISS_ID);

    public static final String VAMPIRE_DRAIN = "vampire_drain";
    public static final ResourceLocation VAMPIRE_DRAIN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_DRAIN);
    public static SoundEvent VAMPIRE_DRAIN_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_DRAIN_ID);



    public static final String DIO_HOHO = "dio_hoho";
    public static final ResourceLocation DIO_HOHO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_HOHO);
    public static SoundEvent DIO_HOHO_EVENT = SoundEvent.createVariableRangeEvent(DIO_HOHO_ID);

    public static final String DIO_DEATH = "dio_death";
    public static final ResourceLocation DIO_DEATH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_DEATH);
    public static SoundEvent DIO_DEATH_EVENT = SoundEvent.createVariableRangeEvent(DIO_DEATH_ID);
    public static final String DIO_HOHO_2 = "dio_hoho_2";
    public static final ResourceLocation DIO_HOHO_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_HOHO_2);
    public static SoundEvent DIO_HOHO_2_EVENT = SoundEvent.createVariableRangeEvent(DIO_HOHO_2_ID);

    public static final String DIO_DEATH_2 = "dio_death_2";
    public static final ResourceLocation DIO_DEATH_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_DEATH_2);
    public static SoundEvent DIO_DEATH_2_EVENT = SoundEvent.createVariableRangeEvent(DIO_DEATH_2_ID);

    public static final String DIO_SUBERASHI = "dio_suberashi";
    public static final ResourceLocation DIO_SUBERASHI_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_SUBERASHI);
    public static SoundEvent DIO_SUBERASHI_EVENT = SoundEvent.createVariableRangeEvent(DIO_SUBERASHI_ID);

    public static final String DIO_LAUGH = "dio_laugh";
    public static final ResourceLocation DIO_LAUGH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_LAUGH);
    public static SoundEvent DIO_LAUGH_EVENT = SoundEvent.createVariableRangeEvent(DIO_LAUGH_ID);

    public static final String DIO_APPROACHING_ME = "dio_approaching_me";
    public static final ResourceLocation DIO_APPROACHING_ME_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_APPROACHING_ME);
    public static SoundEvent DIO_APPROACHING_ME_EVENT = SoundEvent.createVariableRangeEvent(DIO_APPROACHING_ME_ID);


    public static final String DIO_KUREI = "dio_kurei";
    public static final ResourceLocation DIO_KUREI_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_KUREI);
    public static SoundEvent DIO_KUREI_EVENT = SoundEvent.createVariableRangeEvent(DIO_KUREI_ID);

    public static final String DIO_HURT_1 = "dio_hurt_1";
    public static final ResourceLocation DIO_HURT_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_HURT_1);
    public static SoundEvent DIO_HURT_1_EVENT = SoundEvent.createVariableRangeEvent(DIO_HURT_1_ID);

    public static final String DIO_HURT_2 = "dio_hurt_2";
    public static final ResourceLocation DIO_HURT_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_HURT_2);
    public static SoundEvent DIO_HURT_2_EVENT = SoundEvent.createVariableRangeEvent(DIO_HURT_2_ID);

    public static final String DIO_HURT_3 = "dio_hurt_3";
    public static final ResourceLocation DIO_HURT_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_HURT_3);
    public static SoundEvent DIO_HURT_3_EVENT = SoundEvent.createVariableRangeEvent(DIO_HURT_3_ID);

    public static final String DIO_HURT_4 = "dio_hurt_4";
    public static final ResourceLocation DIO_HURT_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_HURT_4);
    public static SoundEvent DIO_HURT_4_EVENT = SoundEvent.createVariableRangeEvent(DIO_HURT_4_ID);


    public static final String DIO_WRY = "dio_wry";
    public static final ResourceLocation DIO_WRY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_WRY);
    public static SoundEvent DIO_WRY_EVENT = SoundEvent.createVariableRangeEvent(DIO_WRY_ID);

    public static final String DIO_SHINE = "dio_shine";
    public static final ResourceLocation DIO_SHINE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_SHINE);
    public static SoundEvent DIO_SHINE_EVENT = SoundEvent.createVariableRangeEvent(DIO_SHINE_ID);
    public static final String DIO_CHECKMATE = "dio_checkmate";
    public static final ResourceLocation DIO_CHECKMATE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_CHECKMATE);
    public static SoundEvent DIO_CHECKMATE_EVENT = SoundEvent.createVariableRangeEvent(DIO_CHECKMATE_ID);
    public static final String DIO_NANI = "dio_nani";
    public static final ResourceLocation DIO_NANI_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_NANI);
    public static SoundEvent DIO_NANI_EVENT = SoundEvent.createVariableRangeEvent(DIO_NANI_ID);
    public static final String DIO_KONO_DIO = "dio_kono_dio";
    public static final ResourceLocation DIO_KONO_DIO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_KONO_DIO);
    public static SoundEvent DIO_KONO_DIO_EVENT = SoundEvent.createVariableRangeEvent(DIO_KONO_DIO_ID);
    public static final String DIO_NO_WAY = "dio_no_way";
    public static final ResourceLocation DIO_NO_WAY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_NO_WAY);
    public static SoundEvent DIO_NO_WAY_EVENT = SoundEvent.createVariableRangeEvent(DIO_NO_WAY_ID);
    public static final String DIO_ATTACK = "dio_attack";
    public static final ResourceLocation DIO_ATTACK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_ATTACK);
    public static SoundEvent DIO_ATTACK_EVENT = SoundEvent.createVariableRangeEvent(DIO_ATTACK_ID);
    public static final String DIO_ATTACK_2 = "dio_attack_2";
    public static final ResourceLocation DIO_ATTACK_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_ATTACK_2);
    public static SoundEvent DIO_ATTACK_2_EVENT = SoundEvent.createVariableRangeEvent(DIO_ATTACK_2_ID);

    public static final String DIO_TAUNT = "dio_taunt";
    public static final ResourceLocation DIO_TAUNT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_TAUNT);
    public static SoundEvent DIO_TAUNT_EVENT = SoundEvent.createVariableRangeEvent(DIO_TAUNT_ID);
    public static final String DIO_THE_WORLD = "dio_the_world";
    public static final ResourceLocation DIO_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_THE_WORLD);
    public static SoundEvent DIO_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(DIO_THE_WORLD_ID);
    public static final String DIO_THE_WORLD_2 = "dio_the_world_2";
    public static final ResourceLocation DIO_THE_WORLD_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_THE_WORLD_2);
    public static SoundEvent DIO_THE_WORLD_2_EVENT = SoundEvent.createVariableRangeEvent(DIO_THE_WORLD_2_ID);
    public static final String DIO_THE_WORLD_3 = "dio_the_world_3";
    public static final ResourceLocation DIO_THE_WORLD_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_THE_WORLD_3);
    public static SoundEvent DIO_THE_WORLD_3_EVENT = SoundEvent.createVariableRangeEvent(DIO_THE_WORLD_3_ID);
    public static final String DIO_THE_WORLD_4 = "dio_the_world_4";
    public static final ResourceLocation DIO_THE_WORLD_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_THE_WORLD_4);
    public static SoundEvent DIO_THE_WORLD_4_EVENT = SoundEvent.createVariableRangeEvent(DIO_THE_WORLD_4_ID);
    public static final String DIO_JOTARO = "dio_jotaro";
    public static final ResourceLocation DIO_JOTARO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_JOTARO);
    public static SoundEvent DIO_JOTARO_EVENT = SoundEvent.createVariableRangeEvent(DIO_JOTARO_ID);
    public static final String DIO_JOTARO_2 = "dio_jotaro_2";
    public static final ResourceLocation DIO_JOTARO_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_JOTARO_2);
    public static SoundEvent DIO_JOTARO_2_EVENT = SoundEvent.createVariableRangeEvent(DIO_JOTARO_2_ID);
    public static final String DIO_INTERESTING = "dio_interesting";
    public static final ResourceLocation DIO_INTERESTING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIO_INTERESTING);
    public static SoundEvent DIO_INTERESTING_EVENT = SoundEvent.createVariableRangeEvent(DIO_INTERESTING_ID);


    public static final String DIEGO_HO = "diego_hoho";
    public static final ResourceLocation DIEGO_HO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_HO);
    public static SoundEvent DIEGO_HO_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_HO_ID);

    public static final String DIEGO_DEATH = "diego_death";
    public static final ResourceLocation DIEGO_DEATH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_DEATH);
    public static SoundEvent DIEGO_DEATH_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_DEATH_ID);
    public static final String DIEGO_HO_2 = "diego_hoho_2";
    public static final ResourceLocation DIEGO_HO_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_HO_2);
    public static SoundEvent DIEGO_HO_2_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_HO_2_ID);

    public static final String DIEGO_DEATH_2 = "diego_death_2";
    public static final ResourceLocation DIEGO_DEATH_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_DEATH_2);
    public static SoundEvent DIEGO_DEATH_2_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_DEATH_2_ID);

    public static final String DIEGO_LAUGH = "diego_laugh";
    public static final ResourceLocation DIEGO_LAUGH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_LAUGH);
    public static SoundEvent DIEGO_LAUGH_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_LAUGH_ID);

    public static final String DIEGO_KUREI = "diego_kurei";
    public static final ResourceLocation DIEGO_KUREI_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_KUREI);
    public static SoundEvent DIEGO_KUREI_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_KUREI_ID);

    public static final String DIEGO_HURT_1 = "diego_hurt_1";
    public static final ResourceLocation DIEGO_HURT_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_HURT_1);
    public static SoundEvent DIEGO_HURT_1_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_HURT_1_ID);

    public static final String DIEGO_HURT_2 = "diego_hurt_2";
    public static final ResourceLocation DIEGO_HURT_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_HURT_2);
    public static SoundEvent DIEGO_HURT_2_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_HURT_2_ID);

    public static final String DIEGO_HURT_3 = "diego_hurt_3";
    public static final ResourceLocation DIEGO_HURT_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_HURT_3);
    public static SoundEvent DIEGO_HURT_3_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_HURT_3_ID);

    public static final String DIEGO_HURT_4 = "diego_hurt_4";
    public static final ResourceLocation DIEGO_HURT_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_HURT_4);
    public static SoundEvent DIEGO_HURT_4_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_HURT_4_ID);


    public static final String DIEGO_WRY = "diego_wry";
    public static final ResourceLocation DIEGO_WRY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_WRY);
    public static SoundEvent DIEGO_WRY_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_WRY_ID);

    public static final String DIEGO_SHINE = "diego_shine";
    public static final ResourceLocation DIEGO_SHINE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_SHINE);
    public static SoundEvent DIEGO_SHINE_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_SHINE_ID);
    public static final String DIEGO_CHECKMATE = "diego_checkmate";
    public static final ResourceLocation DIEGO_CHECKMATE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_CHECKMATE);
    public static SoundEvent DIEGO_CHECKMATE_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_CHECKMATE_ID);
    public static final String DIEGO_NANI = "diego_nani";
    public static final ResourceLocation DIEGO_NANI_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_NANI);
    public static SoundEvent DIEGO_NANI_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_NANI_ID);
    public static final String DIEGO_KONO_DIO = "diego_kono_diego";
    public static final ResourceLocation DIEGO_KONO_DIEGO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_KONO_DIO);
    public static SoundEvent DIEGO_KONO_DIEGO_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_KONO_DIEGO_ID);
    public static final String DIEGO_NO_WAY = "diego_no_way";
    public static final ResourceLocation DIEGO_NO_WAY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_NO_WAY);
    public static SoundEvent DIEGO_NO_WAY_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_NO_WAY_ID);
    public static final String DIEGO_ATTACK = "diego_attack";
    public static final ResourceLocation DIEGO_ATTACK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_ATTACK);
    public static SoundEvent DIEGO_ATTACK_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_ATTACK_ID);
    public static final String DIEGO_ATTACK_2 = "diego_attack_2";
    public static final ResourceLocation DIEGO_ATTACK_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_ATTACK_2);
    public static SoundEvent DIEGO_ATTACK_2_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_ATTACK_2_ID);

    public static final String DIEGO_TAUNT = "diego_taunt";
    public static final ResourceLocation DIEGO_TAUNT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_TAUNT);
    public static SoundEvent DIEGO_TAUNT_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_TAUNT_ID);
    public static final String DIEGO_THE_WORLD = "diego_the_world";
    public static final ResourceLocation DIEGO_THE_WORLD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_THE_WORLD);
    public static SoundEvent DIEGO_THE_WORLD_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_THE_WORLD_ID);
    public static final String DIEGO_THE_WORLD_2 = "diego_the_world_2";
    public static final ResourceLocation DIEGO_THE_WORLD_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_THE_WORLD_2);
    public static SoundEvent DIEGO_THE_WORLD_2_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_THE_WORLD_2_ID);
    public static final String DIEGO_THE_WORLD_3 = "diego_the_world_3";
    public static final ResourceLocation DIEGO_THE_WORLD_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_THE_WORLD_3);
    public static SoundEvent DIEGO_THE_WORLD_3_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_THE_WORLD_3_ID);
    public static final String DIEGO_THE_WORLD_4 = "diego_the_world_4";
    public static final ResourceLocation DIEGO_THE_WORLD_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_THE_WORLD_4);
    public static SoundEvent DIEGO_THE_WORLD_4_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_THE_WORLD_4_ID);
   public static final String DIEGO_INTERESTING = "diego_interesting";
    public static final ResourceLocation DIEGO_INTERESTING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DIEGO_INTERESTING);
    public static SoundEvent DIEGO_INTERESTING_EVENT = SoundEvent.createVariableRangeEvent(DIEGO_INTERESTING_ID);


    public static final String JOTARO_YARE_YARE = "jotaro_yare_yare";
    public static final ResourceLocation JOTARO_YARE_YARE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_YARE_YARE);
    public static SoundEvent JOTARO_YARE_YARE_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_YARE_YARE_ID);
    public static final String JOTARO_YARE_YARE_2 = "jotaro_yare_yare_2";
    public static final ResourceLocation JOTARO_YARE_YARE_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_YARE_YARE_2);
    public static SoundEvent JOTARO_YARE_YARE_2_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_YARE_YARE_2_ID);
    public static final String JOTARO_YARE_YARE_3 = "jotaro_yare_yare_3";
    public static final ResourceLocation JOTARO_YARE_YARE_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_YARE_YARE_3);
    public static SoundEvent JOTARO_YARE_YARE_3_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_YARE_YARE_3_ID);
    public static final String JOTARO_YARE_YARE_4 = "jotaro_yare_yare_4";
    public static final ResourceLocation JOTARO_YARE_YARE_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_YARE_YARE_4);
    public static SoundEvent JOTARO_YARE_YARE_4_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_YARE_YARE_4_ID);
    public static final String JOTARO_YARE_YARE_5 = "jotaro_yare_yare_5";
    public static final ResourceLocation JOTARO_YARE_YARE_5_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_YARE_YARE_5);
    public static SoundEvent JOTARO_YARE_YARE_5_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_YARE_YARE_5_ID);
    public static final String JOTARO_PISSED_OFF = "jotaro_pissed_off";
    public static final ResourceLocation JOTARO_PISSED_OFF_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_PISSED_OFF);
    public static SoundEvent JOTARO_PISSED_OFF_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_PISSED_OFF_ID);
    public static final String JOTARO_OI_OI = "jotaro_oi_oi";
    public static final ResourceLocation JOTARO_OI_OI_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_OI_OI);
    public static SoundEvent JOTARO_OI_OI_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_OI_OI_ID);
    public static final String JOTARO_GRUNT = "jotaro_grunt";
    public static final ResourceLocation JOTARO_GRUNT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_GRUNT);
    public static SoundEvent JOTARO_GRUNT_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_GRUNT_ID);
    public static final String JOTARO_JUDGEMENT = "jotaro_judgement";
    public static final ResourceLocation JOTARO_JUDGEMENT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_JUDGEMENT);
    public static SoundEvent JOTARO_JUDGEMENT_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_JUDGEMENT_ID);
    public static final String JOTARO_STAR_PLATINUM_1 = "jotaro_star_platinum_1";
    public static final ResourceLocation JOTARO_STAR_PLATINUM_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_STAR_PLATINUM_1);
    public static SoundEvent JOTARO_STAR_PLATINUM_1_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_STAR_PLATINUM_1_ID);
    public static final String JOTARO_STAR_PLATINUM_2 = "jotaro_star_platinum_2";
    public static final ResourceLocation JOTARO_STAR_PLATINUM_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_STAR_PLATINUM_2);
    public static SoundEvent JOTARO_STAR_PLATINUM_2_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_STAR_PLATINUM_2_ID);
    public static final String JOTARO_STAR_PLATINUM_3 = "jotaro_star_platinum_3";
    public static final ResourceLocation JOTARO_STAR_PLATINUM_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_STAR_PLATINUM_3);
    public static SoundEvent JOTARO_STAR_PLATINUM_3_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_STAR_PLATINUM_3_ID);
    public static final String JOTARO_STAR_PLATINUM_4 = "jotaro_star_platinum_4";
    public static final ResourceLocation JOTARO_STAR_PLATINUM_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_STAR_PLATINUM_4);
    public static SoundEvent JOTARO_STAR_PLATINUM_4_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_STAR_PLATINUM_4_ID);
    public static final String JOTARO_HURT_1 = "jotaro_hurt_1";
    public static final ResourceLocation JOTARO_HURT_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_HURT_1);
    public static SoundEvent JOTARO_HURT_1_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_HURT_1_ID);
    public static final String JOTARO_HURT_2 = "jotaro_hurt_2";
    public static final ResourceLocation JOTARO_HURT_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_HURT_2);
    public static SoundEvent JOTARO_HURT_2_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_HURT_2_ID);
    public static final String JOTARO_HURT_3 = "jotaro_hurt_3";
    public static final ResourceLocation JOTARO_HURT_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_HURT_3);
    public static SoundEvent JOTARO_HURT_3_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_HURT_3_ID);
    public static final String JOTARO_HURT_4 = "jotaro_hurt_4";
    public static final ResourceLocation JOTARO_HURT_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_HURT_4);
    public static SoundEvent JOTARO_HURT_4_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_HURT_4_ID);
    public static final String JOTARO_HURT_5 = "jotaro_hurt_5";
    public static final ResourceLocation JOTARO_HURT_5_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_HURT_5);
    public static SoundEvent JOTARO_HURT_5_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_HURT_5_ID);
    public static final String JOTARO_DEATH_1 = "jotaro_death_1";
    public static final ResourceLocation JOTARO_DEATH_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_DEATH_1);
    public static SoundEvent JOTARO_DEATH_1_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_DEATH_1_ID);
    public static final String JOTARO_DEATH_2 = "jotaro_death_2";
    public static final ResourceLocation JOTARO_DEATH_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_DEATH_2);
    public static SoundEvent JOTARO_DEATH_2_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_DEATH_2_ID);
    public static final String JOTARO_FINISHER = "jotaro_finisher";
    public static final ResourceLocation JOTARO_FINISHER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_FINISHER);
    public static SoundEvent JOTARO_FINISHER_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_FINISHER_ID);
    public static final String JOTARO_ATTACK_1 = "jotaro_attack_1";
    public static final ResourceLocation JOTARO_ATTACK_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_ATTACK_1);
    public static SoundEvent JOTARO_ATTACK_1_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_ATTACK_1_ID);
    public static final String JOTARO_ATTACK_2 = "jotaro_attack_2";
    public static final ResourceLocation JOTARO_ATTACK_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_ATTACK_2);
    public static SoundEvent JOTARO_ATTACK_2_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_ATTACK_2_ID);
    public static final String JOTARO_ATTACK_3 = "jotaro_attack_3";
    public static final ResourceLocation JOTARO_ATTACK_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_ATTACK_3);
    public static SoundEvent JOTARO_ATTACK_3_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_ATTACK_3_ID);
    public static final String JOTARO_YARO = "jotaro_yaro";
    public static final ResourceLocation JOTARO_YARO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_YARO);
    public static SoundEvent JOTARO_YARO_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_YARO_ID);
    public static final String JOTARO_YAMERO = "jotaro_yamero";
    public static final ResourceLocation JOTARO_YAMERO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_YAMERO);
    public static SoundEvent JOTARO_YAMERO_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_YAMERO_ID);
    public static final String JOTARO_DIO = "jotaro_dio";
    public static final ResourceLocation JOTARO_DIO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_DIO);
    public static SoundEvent JOTARO_DIO_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_DIO_ID);
    public static final String JOTARO_GETING_CLOSER = "jotaro_getting_closer";
    public static final ResourceLocation JOTARO_GETING_CLOSER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JOTARO_GETING_CLOSER);
    public static SoundEvent JOTARO_GETING_CLOSER_EVENT = SoundEvent.createVariableRangeEvent(JOTARO_GETING_CLOSER_ID);

    public static final String FEMALE_ZOMBIE_HURT = "female_zombie_hurt";
    public static final ResourceLocation FEMALE_ZOMBIE_HURT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FEMALE_ZOMBIE_HURT);
    public static SoundEvent FEMALE_ZOMBIE_HURT_EVENT = SoundEvent.createVariableRangeEvent(FEMALE_ZOMBIE_HURT_ID);
    public static final String FEMALE_ZOMBIE_AMBIENT = "female_zombie_ambient";
    public static final ResourceLocation FEMALE_ZOMBIE_AMBIENT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FEMALE_ZOMBIE_AMBIENT);
    public static SoundEvent FEMALE_ZOMBIE_AMBIENT_EVENT = SoundEvent.createVariableRangeEvent(FEMALE_ZOMBIE_AMBIENT_ID);
    public static final String FEMALE_ZOMBIE_DEATH = "female_zombie_death";
    public static final ResourceLocation FEMALE_ZOMBIE_DEATH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FEMALE_ZOMBIE_DEATH);
    public static SoundEvent FEMALE_ZOMBIE_DEATH_EVENT = SoundEvent.createVariableRangeEvent(FEMALE_ZOMBIE_DEATH_ID);
    public static final String AESTHETICIAN_EXHALE = "aesthetician_exhale";
    public static final ResourceLocation AESTHETICIAN_EXHALE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+AESTHETICIAN_EXHALE);
    public static SoundEvent AESTHETICIAN_EXHALE_EVENT = SoundEvent.createVariableRangeEvent(AESTHETICIAN_EXHALE_ID);


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
