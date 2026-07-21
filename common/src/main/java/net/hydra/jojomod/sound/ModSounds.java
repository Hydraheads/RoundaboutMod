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
    public static final String BONE_CHOMP = "bone_chomp";
    public static final ResourceLocation BONE_CHOMP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BONE_CHOMP);
    public static SoundEvent BONE_CHOMP_EVENT = SoundEvent.createVariableRangeEvent(BONE_CHOMP_ID);

    public static final String SKIP_TIME_1 = "skip_time_1";
    public static final ResourceLocation SKIP_TIME_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SKIP_TIME_1);
    public static SoundEvent SKIP_TIME_1_EVENT = SoundEvent.createVariableRangeEvent(SKIP_TIME_1_ID);

    public static final String SKIP_TIME_2 = "skip_time_2";
    public static final ResourceLocation SKIP_TIME_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SKIP_TIME_2);
    public static SoundEvent SKIP_TIME_2_EVENT = SoundEvent.createVariableRangeEvent(SKIP_TIME_2_ID);


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


    public static final String SUMMON_CALIFORNIA = "summon_california";
    public static final ResourceLocation SUMMON_CALIFORNIA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_CALIFORNIA);
    public static SoundEvent SUMMON_CALIFORNIA_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_CALIFORNIA_ID);

    public static final String CHESS_PLACE = "chess_place";
    public static final ResourceLocation CHESS_PLACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CHESS_PLACE);
    public static SoundEvent CHESS_PLACE_EVENT = SoundEvent.createVariableRangeEvent(CHESS_PLACE_ID);

    public static final String CHESS_BREAK = "chess_break";
    public static final ResourceLocation CHESS_BREAK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CHESS_BREAK);
    public static SoundEvent CHESS_BREAK_EVENT = SoundEvent.createVariableRangeEvent(CHESS_BREAK_ID);

    public static final String CKB_STEAL = "ckb_steal";
    public static final ResourceLocation CKB_STEAL_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CKB_STEAL);
    public static SoundEvent CKB_STEAL_EVENT = SoundEvent.createVariableRangeEvent(CKB_STEAL_ID);

    public static final String CKB_YES = "ckb_yes";
    public static final ResourceLocation CKB_YES_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CKB_YES);
    public static SoundEvent CKB_YES_EVENT = SoundEvent.createVariableRangeEvent(CKB_YES_ID);

    public static final String CKB_NO = "ckb_no";
    public static final ResourceLocation CKB_NO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CKB_NO);
    public static SoundEvent CKB_NO_EVENT = SoundEvent.createVariableRangeEvent(CKB_NO_ID);

    public static final String CKB_ATTACK = "ckb_attack";
    public static final ResourceLocation CKB_ATTACK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CKB_ATTACK);
    public static SoundEvent CKB_ATTACK_EVENT = SoundEvent.createVariableRangeEvent(CKB_ATTACK_ID);

    public static final String CKB_TILE = "ckb_tile";
    public static final ResourceLocation CKB_TILE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CKB_TILE);
    public static SoundEvent CKB_TILE_EVENT = SoundEvent.createVariableRangeEvent(CKB_TILE_ID);

    public static final String SUMMON_OASIS = "summon_oasis";
    public static final ResourceLocation SUMMON_OASIS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_OASIS);
    public static SoundEvent SUMMON_OASIS_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_OASIS_ID);

    public static final String CKB_PLACE = "ckb_place";
    public static final ResourceLocation CKB_PLACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CKB_PLACE);
    public static SoundEvent CKB_PLACE_EVENT = SoundEvent.createVariableRangeEvent(CKB_PLACE_ID);

    public static final String CHESS_PIECE = "chess_piece";
    public static final ResourceLocation CHESS_PIECE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CHESS_PIECE);
    public static SoundEvent CHESS_PIECE_EVENT = SoundEvent.createVariableRangeEvent(CHESS_PIECE_ID);

    public static final String HEART_SPARKLE = "heart_sparkle";
    public static final ResourceLocation HEART_SPARKLE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEART_SPARKLE);
    public static SoundEvent HEART_SPARKLE_EVENT = SoundEvent.createVariableRangeEvent(HEART_SPARKLE_ID);


    public static final String STAR_SUMMON_SOUND = "summon_star";
    public static final ResourceLocation STAR_SUMMON_SOUND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STAR_SUMMON_SOUND);
    public static SoundEvent STAR_SUMMON_SOUND_EVENT = SoundEvent.createVariableRangeEvent(STAR_SUMMON_SOUND_ID);

    public static final String SUMMON_MANDOM = "summon_mandom";
    public static final ResourceLocation SUMMON_MANDOM_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_MANDOM);
    public static SoundEvent SUMMON_MANDOM_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_MANDOM_ID);

    public static final String WHITE_ALBUM_SUMMON = "white_album_summon";
    public static final ResourceLocation WHITE_ALBUM_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+WHITE_ALBUM_SUMMON);
    public static SoundEvent WHITE_ALBUM_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(WHITE_ALBUM_SUMMON_ID);

    public static final String SKATE_EQUIP = "skate_equip";
    public static final ResourceLocation SKATE_EQUIP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SKATE_EQUIP);
    public static SoundEvent SKATE_EQUIP_EVENT = SoundEvent.createVariableRangeEvent(SKATE_EQUIP_ID);
    public static final String SKATE_RETRACT = "skate_retract";
    public static final ResourceLocation SKATE_RETRACT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SKATE_RETRACT);
    public static SoundEvent SKATE_RETRACT_EVENT = SoundEvent.createVariableRangeEvent(SKATE_RETRACT_ID);

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

    public static final String HAMMER_CLINK = "hammer_clink";
    public static final ResourceLocation HAMMER_CLINK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HAMMER_CLINK);
    public static SoundEvent HAMMER_CLINK_EVENT = SoundEvent.createVariableRangeEvent(HAMMER_CLINK_ID);

    public static final String AIR_BUBBLE = "air_bubble";
    public static final ResourceLocation AIR_BUBBLE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+AIR_BUBBLE);
    public static SoundEvent AIR_BUBBLE_EVENT = SoundEvent.createVariableRangeEvent(AIR_BUBBLE_ID);
    public static final String BUBBLE_POP = "bubble_pop";
    public static final ResourceLocation BUBBLE_POP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BUBBLE_POP);
    public static SoundEvent BUBBLE_POP_EVENT = SoundEvent.createVariableRangeEvent(BUBBLE_POP_ID);

    public static final String ICE_SKATING = "ice_skating";
    public static final ResourceLocation ICE_SKATING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ICE_SKATING);
    public static SoundEvent ICE_SKATING_EVENT = SoundEvent.createVariableRangeEvent(ICE_SKATING_ID);
    public static final String SKATING_LAND = "skating_land";
    public static final ResourceLocation SKATING_LAND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SKATING_LAND);
    public static SoundEvent SKATING_LAND_EVENT = SoundEvent.createVariableRangeEvent(SKATING_LAND_ID);

    public static final String ICE_RISES = "ice_wall";
    public static final ResourceLocation ICE_RISES_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ICE_RISES);
    public static SoundEvent ICE_RISES_EVENT = SoundEvent.createVariableRangeEvent(ICE_RISES_ID);

    public static final String BLOCK_FREEZE = "block_freeze";
    public static final ResourceLocation BLOCK_FREEZE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLOCK_FREEZE);
    public static SoundEvent BLOCK_FREEZE_EVENT = SoundEvent.createVariableRangeEvent(BLOCK_FREEZE_ID);

    public static final String ICE_BLAST_CHARGE = "ice_blast_charge";
    public static final ResourceLocation ICE_BLAST_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ICE_BLAST_CHARGE);
    public static SoundEvent ICE_BLAST_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(ICE_BLAST_CHARGE_ID);

    public static final String COLD_SHOT = "cold_shot";
    public static final ResourceLocation COLD_SHOT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+COLD_SHOT);
    public static SoundEvent COLD_SHOT_EVENT = SoundEvent.createVariableRangeEvent(COLD_SHOT_ID);

    public static final String GENTLY_WEEPS = "gently_weeps";
    public static final ResourceLocation GENTLY_WEEPS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GENTLY_WEEPS);
    public static SoundEvent GENTLY_WEEPS_EVENT = SoundEvent.createVariableRangeEvent(GENTLY_WEEPS_ID);

    public static final String DING = "ding";
    public static final ResourceLocation DING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+DING);
    public static SoundEvent DING_EVENT = SoundEvent.createVariableRangeEvent(DING_ID);




    public static final String BANISH = "banish";
    public static final ResourceLocation BANISH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BANISH);
    public static SoundEvent BANISH_EVENT = SoundEvent.createVariableRangeEvent(BANISH_ID);

    public static final String IRON_BALL_BOUNCE = "iron_ball_bounce";
    public static final ResourceLocation IRON_BALL_BOUNCE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+IRON_BALL_BOUNCE);
    public static SoundEvent IRON_BALL_BOUNCE_EVENT = SoundEvent.createVariableRangeEvent(IRON_BALL_BOUNCE_ID);

    public static final String IRON_BALL_IMPACT = "iron_ball_impact";
    public static final ResourceLocation IRON_BALL_IMPACT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+IRON_BALL_IMPACT);
    public static SoundEvent IRON_BALL_IMPACT_EVENT = SoundEvent.createVariableRangeEvent(IRON_BALL_IMPACT_ID);

    public static final String UV_BLAST = "uv_blast";
    public static final ResourceLocation UV_BLAST_ID = new ResourceLocation(Roundabout.MOD_ID+":"+UV_BLAST);
    public static SoundEvent UV_BLAST_EVENT = SoundEvent.createVariableRangeEvent(UV_BLAST_ID);


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

    public static final String CREAM_SUMMON = "cream_summon";
    public static final ResourceLocation CREAM_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CREAM_SUMMON);
    public static SoundEvent CREAM_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(CREAM_SUMMON_ID);

    public static final String CREAM_VOID_ATTACK = "cream_void_attack";
    public static final ResourceLocation CREAM_VOID_ATTACK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CREAM_VOID_ATTACK);
    public static SoundEvent CREAM_VOID_ATTACK_EVENT = SoundEvent.createVariableRangeEvent(CREAM_VOID_ATTACK_ID);

    public static final String EMPEROR_SUMMON = "emperor_summon";
    public static final ResourceLocation EMPEROR_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EMPEROR_SUMMON);
    public static SoundEvent EMPEROR_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(EMPEROR_SUMMON_ID);
    public static final String EMPEROR_SHOOT = "emperor_shoot";
    public static final ResourceLocation EMPEROR_SHOOT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EMPEROR_SHOOT);
    public static SoundEvent EMPEROR_SHOOT_EVENT = SoundEvent.createVariableRangeEvent(EMPEROR_SHOOT_ID);
    public static final String EMPEROR_IMPACT = "emperor_impact";
    public static final ResourceLocation EMPEROR_IMPACT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EMPEROR_IMPACT);
    public static SoundEvent EMPEROR_IMPACT_EVENT = SoundEvent.createVariableRangeEvent(EMPEROR_IMPACT_ID);

    public static final String MANHATTAN_SUMMON = "manhattan_summoning";
    public static final ResourceLocation MANHATTAN_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MANHATTAN_SUMMON);
    public static SoundEvent MANHATTAN_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(MANHATTAN_SUMMON_ID);
    public static final String MANHATTAN_DODGING = "manhattan_rain_dodging";
    public static final ResourceLocation MANHATTAN_DODGING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MANHATTAN_DODGING);
    public static SoundEvent MANHATTAN_DODGING_EVENT = SoundEvent.createVariableRangeEvent(MANHATTAN_DODGING_ID);
    public static final String MANHATTAN_VISION = "manhattan_vision";
    public static final ResourceLocation MANHATTAN_VISION_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MANHATTAN_VISION);
    public static SoundEvent MANHATTAN_VISION_EVENT = SoundEvent.createVariableRangeEvent(MANHATTAN_VISION_ID);

    public static final String BLACK_SABBATH_SUMMON = "summon_black_sabbath";
    public static final ResourceLocation BLACK_SABBATH_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLACK_SABBATH_SUMMON);
    public static SoundEvent BLACK_SABBATH_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(BLACK_SABBATH_SUMMON_ID);
    public static final String TURNING_ON_LIGHTER = "lighter_click";
    public static final ResourceLocation TURNING_ON_LIGHTER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TURNING_ON_LIGHTER);
    public static SoundEvent TURNING_ON_LIGHTER_EVENT = SoundEvent.createVariableRangeEvent(TURNING_ON_LIGHTER_ID);


    public static final String CENTURY_BOY_SUMMON = "century_boy_summon";
    public static final ResourceLocation CENTURY_BOY_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CENTURY_BOY_SUMMON);
    public static SoundEvent CENTURY_BOY_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(CENTURY_BOY_SUMMON_ID);
    public static final String CENTURY_BOY_HIT = "century_boy_hit";
    public static final ResourceLocation CENTURY_BOY_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CENTURY_BOY_HIT);
    public static SoundEvent CENTURY_BOY_HIT_EVENT = SoundEvent.createVariableRangeEvent(CENTURY_BOY_HIT_ID);
    public static final String CENTURY_BOY_NORMAL_STANCE = "century_boy_normal_stance";
    public static final ResourceLocation CENTURY_BOY_NORMAL_STANCE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CENTURY_BOY_NORMAL_STANCE);
    public static SoundEvent CENTURY_BOY_NORMAL_STANCE_EVENT = SoundEvent.createVariableRangeEvent(CENTURY_BOY_NORMAL_STANCE_ID);
    public static final String CENTURY_BOY_PROPEL_STANCE = "century_boy_propel_stance";
    public static final ResourceLocation CENTURY_BOY_PROPEL_STANCE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CENTURY_BOY_PROPEL_STANCE);
    public static SoundEvent CENTURY_BOY_PROPEL_STANCE_EVENT = SoundEvent.createVariableRangeEvent(CENTURY_BOY_PROPEL_STANCE_ID);
    public static final String CENTURY_BOY_OUTPUT_STANCE = "century_boy_output_stance";
    public static final ResourceLocation CENTURY_BOY_OUTPUT_STANCE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CENTURY_BOY_OUTPUT_STANCE);
    public static SoundEvent CENTURY_BOY_OUTPUT_STANCE_EVENT = SoundEvent.createVariableRangeEvent(CENTURY_BOY_OUTPUT_STANCE_ID);
    public static final String CENTURY_BOY_GROUND_STANCE = "century_boy_ground_stance";
    public static final ResourceLocation CENTURY_BOY_GROUND_STANCE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CENTURY_BOY_GROUND_STANCE);
    public static SoundEvent CENTURY_BOY_GROUND_STANCE_EVENT = SoundEvent.createVariableRangeEvent(CENTURY_BOY_GROUND_STANCE_ID);

    public static final String SNUBNOSE_FIRE = "snubnose_fire";
    public static final ResourceLocation SNUBNOSE_FIRE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SNUBNOSE_FIRE);
    public static SoundEvent SNUBNOSE_FIRE_EVENT = SoundEvent.createVariableRangeEvent(SNUBNOSE_FIRE_ID);
    public static final String SNUBNOSE_DRY_FIRE = "snubnose_dry_fire";
    public static final ResourceLocation SNUBNOSE_DRY_FIRE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SNUBNOSE_DRY_FIRE);
    public static SoundEvent SNUBNOSE_DRY_FIRE_EVENT = SoundEvent.createVariableRangeEvent(SNUBNOSE_DRY_FIRE_ID);
    public static final String SNUBNOSE_RELOAD = "snubnose_reload";
    public static final ResourceLocation SNUBNOSE_RELOAD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SNUBNOSE_RELOAD);
    public static SoundEvent SNUBNOSE_RELOAD_EVENT = SoundEvent.createVariableRangeEvent(SNUBNOSE_RELOAD_ID);

    public static final String TOMMY_FIRE = "tommy_fire";
    public static final ResourceLocation TOMMY_FIRE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TOMMY_FIRE);
    public static SoundEvent TOMMY_FIRE_EVENT = SoundEvent.createVariableRangeEvent(TOMMY_FIRE_ID);
    public static final String TOMMY_RELOAD = "tommy_reload";
    public static final ResourceLocation TOMMY_RELOAD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TOMMY_RELOAD);
    public static SoundEvent TOMMY_RELOAD_EVENT = SoundEvent.createVariableRangeEvent(TOMMY_RELOAD_ID);

    public static final String COLT_FIRE = "colt_fire";
    public static final ResourceLocation COLT_FIRE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+COLT_FIRE);
    public static SoundEvent COLT_FIRE_EVENT = SoundEvent.createVariableRangeEvent(COLT_FIRE_ID);
    public static final String COLT_RELOAD = "colt_reload";
    public static final ResourceLocation COLT_RELOAD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+COLT_RELOAD);
    public static SoundEvent COLT_RELOAD_EVENT = SoundEvent.createVariableRangeEvent(COLT_RELOAD_ID);

    public static final String JACKAL_FIRE = "jackal_fire";
    public static final ResourceLocation JACKAL_FIRE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JACKAL_FIRE);
    public static SoundEvent JACKAL_FIRE_EVENT = SoundEvent.createVariableRangeEvent(JACKAL_FIRE_ID);
    public static final String JACKAL_RELOAD = "jackal_reload";
    public static final ResourceLocation JACKAL_RELOAD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+JACKAL_RELOAD);
    public static SoundEvent JACKAL_RELOAD_EVENT = SoundEvent.createVariableRangeEvent(JACKAL_RELOAD_ID);

    public static final String BULLET_PENTRATION = "bullet_penetration";
    public static final ResourceLocation BULLET_PENTRATION_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BULLET_PENTRATION);
    public static SoundEvent BULLET_PENTRATION_EVENT = SoundEvent.createVariableRangeEvent(BULLET_PENTRATION_ID);

    public static final String BULLET_RICOCHET = "bullet_ricochet";
    public static final ResourceLocation BULLET_RICOCHET_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BULLET_RICOCHET);
    public static SoundEvent BULLET_RICOCHET_EVENT = SoundEvent.createVariableRangeEvent(BULLET_RICOCHET_ID);

    public static final String MAGIC_DING = "magic_ding";
    public static final ResourceLocation MAGIC_DING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+MAGIC_DING);
    public static SoundEvent MAGIC_DING_EVENT = SoundEvent.createVariableRangeEvent(MAGIC_DING_ID);



    public static final String ROAD_ROLLER_AMBIENT = "road_roller_ambient";
    public static final ResourceLocation ROAD_ROLLER_AMBIENT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ROAD_ROLLER_AMBIENT);
    public static SoundEvent ROAD_ROLLER_AMBIENT_EVENT = SoundEvent.createVariableRangeEvent(ROAD_ROLLER_AMBIENT_ID);

    public static final String ROAD_ROLLER_EXPLOSION = "road_roller_explosion";
    public static final ResourceLocation ROAD_ROLLER_EXPLOSION_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ROAD_ROLLER_EXPLOSION);
    public static SoundEvent ROAD_ROLLER_EXPLOSION_EVENT = SoundEvent.createVariableRangeEvent(ROAD_ROLLER_EXPLOSION_ID);

    public static final String ROAD_ROLLER_MIXING = "road_roller_mixing";
    public static final ResourceLocation ROAD_ROLLER_MIXING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ROAD_ROLLER_MIXING);
    public static SoundEvent ROAD_ROLLER_MIXING_EVENT = SoundEvent.createVariableRangeEvent(ROAD_ROLLER_MIXING_ID);

    public static final String SUMMON_ANUBIS = "summon_anubis";
    public static final ResourceLocation SUMMON_ANUBIS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_ANUBIS);
    public static SoundEvent SUMMON_ANUBIS_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_ANUBIS_ID);


    public static final String ANUBIS_UNSHEATHE = "anubis_unsheathe";
    public static final ResourceLocation ANUBIS_UNSHEATHE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ANUBIS_UNSHEATHE);
    public static SoundEvent ANUBIS_UNSHEATHE_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_UNSHEATHE_ID);

    public static final String BLOOD_SUCK = "blood_suck";
    public static final ResourceLocation BLOOD_SUCK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLOOD_SUCK);
    public static SoundEvent BLOOD_SUCK_EVENT = SoundEvent.createVariableRangeEvent(BLOOD_SUCK_ID);

    public static final String BLOOD_SPEED = "vampire_speed";
    public static final ResourceLocation BLOOD_SPEED_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLOOD_SPEED);
    public static SoundEvent BLOOD_SPEED_EVENT = SoundEvent.createVariableRangeEvent(BLOOD_SPEED_ID);

    public static final String BLOOD_SUCK_DRAIN = "blood_suck_crit";
    public static final ResourceLocation BLOOD_SUCK_DRAIN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLOOD_SUCK_DRAIN);
    public static SoundEvent BLOOD_SUCK_DRAIN_EVENT = SoundEvent.createVariableRangeEvent(BLOOD_SUCK_DRAIN_ID);

    public static final String HYPNOSIS = "hypnosis";
    public static final ResourceLocation HYPNOSIS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HYPNOSIS);
    public static SoundEvent HYPNOSIS_EVENT = SoundEvent.createVariableRangeEvent(HYPNOSIS_ID);

    public static final String VAMPIRE_AWAKEN = "vampire_awaken";
    public static final ResourceLocation VAMPIRE_AWAKEN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_AWAKEN);
    public static SoundEvent VAMPIRE_AWAKEN_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_AWAKEN_ID);



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

    public static final String FLUFF_BRACE_INIT = "fluff_brace_init";
    public static final ResourceLocation FLUFF_BRACE_INIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FLUFF_BRACE_INIT);
    public static SoundEvent FLUFF_BRACE_INIT_EVENT = SoundEvent.createVariableRangeEvent(FLUFF_BRACE_INIT_ID);
    public static final String FLUFF_FALL_BRACE = "fluff_fall_brace";
    public static final ResourceLocation FLUFF_FALL_BRACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FLUFF_FALL_BRACE);
    public static SoundEvent FLUFF_FALL_BRACE_EVENT = SoundEvent.createVariableRangeEvent(FLUFF_FALL_BRACE_ID);


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

    public static final String KILLER_QUEEN_IMPALE = "killer_queen_impale";
    public static final ResourceLocation KILLER_QUEEN_IMPALE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_IMPALE);
    public static SoundEvent KILLER_QUEEN_IMPALE_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_IMPALE_ID);
    public static final String KILLER_QUEEN_PUNCH = "killer_queen_punch";
    public static final ResourceLocation KILLER_QUEEN_PUNCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_PUNCH);
    public static SoundEvent KILLER_QUEEN_PUNCH_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_PUNCH_ID);

    public static final String KILLER_QUEEN_PUNCH_1 = "killer_queen_punch_1";
    public static final ResourceLocation KILLER_QUEEN_PUNCH_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_PUNCH_1);
    public static SoundEvent KILLER_QUEEN_PUNCH_1_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_PUNCH_1_ID);
    public static final String KILLER_QUEEN_PUNCH_2 = "killer_queen_punch_2";
    public static final ResourceLocation KILLER_QUEEN_PUNCH_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_PUNCH_2);
    public static SoundEvent KILLER_QUEEN_PUNCH_2_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_PUNCH_2_ID);
    public static final String KILLER_QUEEN_HEAVY_PUNCH = "killer_queen_heavy_punch";
    public static final ResourceLocation KILLER_QUEEN_HEAVY_PUNCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_HEAVY_PUNCH);
    public static SoundEvent KILLER_QUEEN_HEAVY_PUNCH_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_HEAVY_PUNCH_ID);

    public static final String KILLER_QUEEN_SHIBA = "killer_queen_shiba";
    public static final ResourceLocation KILLER_QUEEN_SHIBA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHIBA);
    public static SoundEvent KILLER_QUEEN_SHIBA_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHIBA_ID);
    public static final String KILLER_QUEEN_SHIBABA = "killer_queen_shibaba";
    public static final ResourceLocation KILLER_QUEEN_SHIBABA_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHIBABA);
    public static SoundEvent KILLER_QUEEN_SHIBABA_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHIBABA_ID);
    public static final String KILLER_QUEEN_SHA_SUMMON = "sha_summon";
    public static final ResourceLocation KILLER_QUEEN_SHA_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHA_SUMMON);
    public static SoundEvent KILLER_QUEEN_SHA_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHA_SUMMON_ID);
    public static final String KILLER_QUEEN_SHA_MOVING = "sha_moving";
    public static final ResourceLocation KILLER_QUEEN_SHA_MOVING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHA_MOVING);
    public static SoundEvent KILLER_QUEEN_SHA_MOVING_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHA_MOVING_ID);
    public static final String KILLER_QUEEN_SHA_DEDEDEDE = "sha_dededede";
    public static final ResourceLocation KILLER_QUEEN_SHA_DEDEDEDE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHA_DEDEDEDE);
    public static SoundEvent KILLER_QUEEN_SHA_DEDEDEDE_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHA_DEDEDEDE_ID);

    public static final String KILLER_QUEEN_SHA_CRACKED_KOCCHI = "sha_cracked_kocchi";
    public static final ResourceLocation KILLER_QUEEN_SHA_CRACKED_KOCCHI_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHA_CRACKED_KOCCHI);
    public static SoundEvent KILLER_QUEEN_SHA_CRACKED_KOCCHI_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHA_CRACKED_KOCCHI_ID);
    public static final String KILLER_QUEEN_SHA_CRACKED_DEDE = "sha_cracked_dede";
    public static final ResourceLocation KILLER_QUEEN_SHA_CRACKED_DEDE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHA_CRACKED_DEDE);
    public static SoundEvent KILLER_QUEEN_SHA_CRACKED_DEDE_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHA_CRACKED_DEDE_ID);

    public static final String KILLER_QUEEN_SHA_KOCCHI_1 = "sha_kocchi_1";
    public static final ResourceLocation KILLER_QUEEN_SHA_KOCCHI_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHA_KOCCHI_1);
    public static SoundEvent KILLER_QUEEN_SHA_KOCCHI_1_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHA_KOCCHI_1_ID);
    public static final String KILLER_QUEEN_SHA_KOCCHI_2 = "sha_kocchi_2";
    public static final ResourceLocation KILLER_QUEEN_SHA_KOCCHI_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SHA_KOCCHI_2);
    public static SoundEvent KILLER_QUEEN_SHA_KOCCHI_2_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SHA_KOCCHI_2_ID);
    public static final String KILLER_QUEEN_BARRAGE = "killer_queen_barrage";
    public static final ResourceLocation KILLER_QUEEN_BARRAGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_BARRAGE);
    public static SoundEvent KILLER_QUEEN_BARRAGE_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_BARRAGE_ID);

    public static final String CREEPER_QUEEN_SUMMON = "summon_creeper_queen";
    public static final ResourceLocation CREEPER_QUEEN_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+CREEPER_QUEEN_SUMMON);
    public static SoundEvent CREEPER_QUEEN_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(CREEPER_QUEEN_SUMMON_ID);

    public static final String KILLER_QUEEN_SUMMON_DARK = "summon_killer_queen_dark";
    public static final ResourceLocation KILLER_QUEEN_SUMMON_DARK_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SUMMON_DARK);
    public static SoundEvent KILLER_QUEEN_SUMMON_DARK_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SUMMON_DARK_ID);

    public static final String KILLER_QUEEN_SUMMON = "summon_killer_queen";
    public static final ResourceLocation KILLER_QUEEN_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_SUMMON);
    public static SoundEvent KILLER_QUEEN_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_SUMMON_ID);


    public static final String KILLER_QUEEN_DETONATE = "killer_queen_bomb_detonate";
    public static final ResourceLocation KILLER_QUEEN_DETONATE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_DETONATE);
    public static SoundEvent KILLER_QUEEN_DETONATE_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_DETONATE_ID);
    public static final String KILLER_QUEEN_EXPLOSION = "killer_queen_explosion";
    public static final ResourceLocation KILLER_QUEEN_EXPLOSION_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_EXPLOSION);
    public static SoundEvent KILLER_QUEEN_EXPLOSION_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_EXPLOSION_ID);
    public static final String KILLER_QUEEN_BUBBLE_LAUNCH = "killer_queen_bubble_launch";
    public static final ResourceLocation KILLER_QUEEN_BUBBLE_LAUNCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+KILLER_QUEEN_BUBBLE_LAUNCH);
    public static SoundEvent KILLER_QUEEN_BUBBLE_LAUNCH_EVENT = SoundEvent.createVariableRangeEvent(KILLER_QUEEN_BUBBLE_LAUNCH_ID);

    public static final String SHA_JUMP = "sha_jump";
    public static final ResourceLocation SHA_JUMP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SHA_JUMP);
    public static SoundEvent SHA_JUMP_EVENT = SoundEvent.createVariableRangeEvent(SHA_JUMP_ID);
    public static final String SHA_MOVING = "sha_moving";
    public static final ResourceLocation SHA_MOVING_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SHA_MOVING);
    public static SoundEvent SHA_MOVING_EVENT = SoundEvent.createVariableRangeEvent(SHA_MOVING_ID);
    public static final String STRAY_CAT_BUBBLE_POP = "straycat_bubble_pop";
    public static final ResourceLocation STRAY_CAT_BUBBLE_POP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STRAY_CAT_BUBBLE_POP);
    public static SoundEvent STRAY_CAT_BUBBLE_POP_EVENT = SoundEvent.createVariableRangeEvent(STRAY_CAT_BUBBLE_POP_ID);
    public static final String STRAY_CAT_BUBBLE_SOUND_1 = "straycat_bubble_sound";
    public static final ResourceLocation STRAY_CAT_BUBBLE_SOUND_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STRAY_CAT_BUBBLE_SOUND_1);
    public static SoundEvent STRAY_CAT_BUBBLE_SOUND_1_EVENT = SoundEvent.createVariableRangeEvent(STRAY_CAT_BUBBLE_SOUND_1_ID);
    public static final String STRAY_CAT_BUBBLE_SOUND_2 = "straycat_bubble_sound_2";
    public static final ResourceLocation STRAY_CAT_BUBBLE_SOUND_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STRAY_CAT_BUBBLE_SOUND_2);
    public static SoundEvent STRAY_CAT_BUBBLE_SOUND_2_EVENT = SoundEvent.createVariableRangeEvent(STRAY_CAT_BUBBLE_SOUND_2_ID);
    public static final String STRAY_CAT_BUBBLE_REDIRECT_1 = "straycat_bubble_redirect";
    public static final ResourceLocation STRAY_CAT_BUBBLE_REDIRECT_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STRAY_CAT_BUBBLE_REDIRECT_1);
    public static SoundEvent STRAY_CAT_BUBBLE_REDIRECT_1_EVENT = SoundEvent.createVariableRangeEvent(STRAY_CAT_BUBBLE_SOUND_1_ID);
    public static final String STRAY_CAT_BUBBLE_REDIRECT_2 = "straycat_bubble_redirect_2";
    public static final ResourceLocation STRAY_CAT_BUBBLE_REDIRECT_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+STRAY_CAT_BUBBLE_REDIRECT_2);
    public static SoundEvent STRAY_CAT_BUBBLE_REDIRECT_2_EVENT = SoundEvent.createVariableRangeEvent(STRAY_CAT_BUBBLE_SOUND_2_ID);

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


    public static final String PLANET_WAVES_SUMMON = "planet_waves_summon";
    public static final ResourceLocation PLANET_WAVES_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PLANET_WAVES_SUMMON);
    public static SoundEvent PLANET_WAVES_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(PLANET_WAVES_SUMMON_ID);

    public static final String PLANET_WAVES_METEOR_SHOWER = "planet_waves_meteor_shower";
    public static final ResourceLocation PLANET_WAVES_METEOR_SHOWER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PLANET_WAVES_METEOR_SHOWER);
    public static SoundEvent PLANET_WAVES_METEOR_SHOWER_EVENT = SoundEvent.createVariableRangeEvent(PLANET_WAVES_METEOR_SHOWER_ID);

    public static final String PLANET_WAVES_BIG_METEOR = "planet_waves_big_meteor";
    public static final ResourceLocation PLANET_WAVES_BIG_METEOR_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PLANET_WAVES_BIG_METEOR);
    public static SoundEvent PLANET_WAVES_BIG_METEOR_EVENT = SoundEvent.createVariableRangeEvent(PLANET_WAVES_BIG_METEOR_ID);

    public static final String PLANET_WAVES_DISINTEGRATION = "planet_waves_disintegration";
    public static final ResourceLocation PLANET_WAVES_DISINTEGRATION_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PLANET_WAVES_DISINTEGRATION);
    public static SoundEvent PLANET_WAVES_DISINTEGRATION_EVENT = SoundEvent.createVariableRangeEvent(PLANET_WAVES_DISINTEGRATION_ID);

    public static final String PLANET_WAVES_TARGET = "planet_waves_target";
    public static final ResourceLocation PLANET_WAVES_TARGET_ID = new ResourceLocation(Roundabout.MOD_ID+":"+PLANET_WAVES_TARGET);
    public static SoundEvent PLANET_WAVES_TARGET_EVENT = SoundEvent.createVariableRangeEvent(PLANET_WAVES_TARGET_ID);


    public static final String RATT_SUMMON = "ratt_summon";
    public static final ResourceLocation RATT_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_SUMMON);
    public static SoundEvent RATT_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(RATT_SUMMON_ID);
    public static final String RATT_PLACE = "ratt_place";
    public static final ResourceLocation RATT_PLACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_PLACE);
    public static SoundEvent RATT_PLACE_EVENT = SoundEvent.createVariableRangeEvent(RATT_PLACE_ID);
    public static final String RATT_DEPLACE = "ratt_deplace";
    public static final ResourceLocation RATT_DEPLACE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_DEPLACE);
    public static SoundEvent RATT_DEPLACE_EVENT = SoundEvent.createVariableRangeEvent(RATT_DEPLACE_ID);
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
    public static final String RATT_LEAP = "ratt_leap";
    public static final ResourceLocation RATT_LEAP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_LEAP);
    public static SoundEvent RATT_LEAP_EVENT = SoundEvent.createVariableRangeEvent(RATT_LEAP_ID);
    public static final String RATT_MODE_CHANGE = "ratt_mode_change";
    public static final ResourceLocation RATT_MODE_CHANGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RATT_MODE_CHANGE);
    public static SoundEvent RATT_MODE_CHANGE_EVENT = SoundEvent.createVariableRangeEvent(RATT_MODE_CHANGE_ID);

    public static final String ANUBIS_POSSESSION = "anubis_possession";
    public static final ResourceLocation ANUBIS_POSSESSION_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_POSSESSION);
    public static SoundEvent ANUBIS_POSSESSION_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_POSSESSION_ID);
    public static final String ANUBIS_SUMMON = "anubis_summon";
    public static final ResourceLocation ANUBIS_SUMMON_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_SUMMON);
    public static SoundEvent ANUBIS_SUMMON_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_SUMMON_ID);
    public static final String ANUBIS_ALLURING = "anubis_alluring";
    public static final ResourceLocation ANUBIS_ALLURING_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_ALLURING);
    public static SoundEvent ANUBIS_ALLURING_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_ALLURING_ID);
    public static final String ANUBIS_RAGING = "anubis_raging";
    public static final ResourceLocation ANUBIS_RAGING_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_RAGING);
    public static SoundEvent ANUBIS_RAGING_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_RAGING_ID);
    public static final String ANUBIS_BACKFLIP = "anubis_backflip";
    public static final ResourceLocation ANUBIS_BACKFLIP_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_BACKFLIP);
    public static SoundEvent ANUBIS_BACKFLIP_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_BACKFLIP_ID);
    public static final String ANUBIS_POGO_LAUNCH = "anubis_pogo_launch";
    public static final ResourceLocation ANUBIS_POGO_LAUNCH_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_POGO_LAUNCH);
    public static SoundEvent ANUBIS_POGO_LAUNCH_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_POGO_LAUNCH_ID);
    public static final String ANUBIS_POGO_HIT = "anubis_pogo_hit";
    public static final ResourceLocation ANUBIS_POGO_HIT_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_POGO_HIT);
    public static SoundEvent ANUBIS_POGO_HIT_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_POGO_HIT_ID);
    public static final String ANUBIS_BARRAGE_1 = "anubis_barrage_1";
    public static final ResourceLocation ANUBIS_BARRAGE_1_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_BARRAGE_1);
    public static SoundEvent ANUBIS_BARRAGE_1_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_BARRAGE_1_ID);
    public static final String ANUBIS_BARRAGE_1_HIT = "anubis_barrage_1_hit";
    public static final ResourceLocation ANUBIS_BARRAGE_1_HIT_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_BARRAGE_1_HIT);
    public static SoundEvent ANUBIS_BARRAGE_1_HIT_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_BARRAGE_1_HIT_ID);
    public static final String ANUBIS_BARRAGE_END = "anubis_barrage_end";
    public static final ResourceLocation ANUBIS_BARRAGE_END_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_BARRAGE_END);
    public static SoundEvent ANUBIS_BARRAGE_END_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_BARRAGE_END_ID);
    public static final String ANUBIS_SWING = "anubis_swing";
    public static final ResourceLocation ANUBIS_SWING_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_SWING);
    public static SoundEvent ANUBIS_SWING_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_SWING_ID);
    public static final String ANUBIS_CROUCH_SWING = "anubis_crouch_swing";
    public static final ResourceLocation ANUBIS_CROUCH_SWING_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_CROUCH_SWING);
    public static SoundEvent ANUBIS_CROUCH_SWING_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_CROUCH_SWING_ID);
    public static final String ANUBIS_DOUBLE_CUT = "anubis_double_cut";
    public static final ResourceLocation ANUBIS_DOUBLE_CUT_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_DOUBLE_CUT);
    public static SoundEvent ANUBIS_DOUBLE_CUT_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_DOUBLE_CUT_ID);
    public static final String ANUBIS_THRUST_CUT = "anubis_thrust_cut";
    public static final ResourceLocation ANUBIS_THRUST_CUT_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_THRUST_CUT);
    public static SoundEvent ANUBIS_THRUST_CUT_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_THRUST_CUT_ID);
    public static final String ANUBIS_THRUST_MISS = "anubis_thrust_miss";
    public static final ResourceLocation ANUBIS_THRUST_MISS_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_THRUST_MISS);
    public static SoundEvent ANUBIS_THRUST_MISS_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_THRUST_MISS_ID);
    public static final String ANUBIS_UPPERCUT = "anubis_uppercut";
    public static final ResourceLocation ANUBIS_UPPERCUT_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_UPPERCUT);
    public static SoundEvent ANUBIS_UPPERCUT_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_UPPERCUT_ID);
    public static final String ANUBIS_SHIELDBREAK = "anubis_shieldbreak";
    public static final ResourceLocation ANUBIS_SHIELDBREAK_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_SHIELDBREAK);
    public static SoundEvent ANUBIS_SHIELDBREAK_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_SHIELDBREAK_ID);
    public static final String ANUBIS_EXTRA = "anubis_extra";
    public static final ResourceLocation ANUBIS_EXTRA_ID = new ResourceLocation(Roundabout.MOD_ID,ANUBIS_EXTRA);
    public static SoundEvent ANUBIS_EXTRA_EVENT = SoundEvent.createVariableRangeEvent(ANUBIS_EXTRA_ID);



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

    public static final String BLOOD_REGEN = "blood_regen";
    public static final ResourceLocation BLOOD_REGEN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLOOD_REGEN);
    public static SoundEvent BLOOD_REGEN_EVENT = SoundEvent.createVariableRangeEvent(BLOOD_REGEN_ID);
    public static final String BLOOD_REGEN_FINISH = "blood_regen_finish";
    public static final ResourceLocation BLOOD_REGEN_FINISH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+BLOOD_REGEN_FINISH);
    public static SoundEvent BLOOD_REGEN_FINISH_EVENT = SoundEvent.createVariableRangeEvent(BLOOD_REGEN_FINISH_ID);

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


    public static final String VAMPIRE_DASH = "vampire_dash";
    public static final ResourceLocation VAMPIRE_DASH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_DASH);
    public static SoundEvent VAMPIRE_DASH_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_DASH_ID);
    public static final String VAMPIRE_GLEAM = "vampire_gleam";
    public static final ResourceLocation VAMPIRE_GLEAM_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_GLEAM);
    public static SoundEvent VAMPIRE_GLEAM_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_GLEAM_ID);

    public static final String ICY_WIND = "icy_wind";
    public static final ResourceLocation ICY_WIND_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ICY_WIND);
    public static SoundEvent ICY_WIND_EVENT = SoundEvent.createVariableRangeEvent(ICY_WIND_ID);


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


    public static final String EVIL_AURA_BLAST = "evil_aura_blast";
    public static final ResourceLocation EVIL_AURA_BLAST_ID = new ResourceLocation(Roundabout.MOD_ID+":"+EVIL_AURA_BLAST);
    public static SoundEvent EVIL_AURA_BLAST_EVENT = SoundEvent.createVariableRangeEvent(EVIL_AURA_BLAST_ID);
    public static final String AURA_IMPACT = "aura_impact";
    public static final ResourceLocation AURA_IMPACT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+AURA_IMPACT);
    public static SoundEvent AURA_IMPACT_EVENT = SoundEvent.createVariableRangeEvent(AURA_IMPACT_ID);

    public static final String VAMPIRE_CAMO = "vampire_camo";
    public static final ResourceLocation VAMPIRE_CAMO_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_CAMO);
    public static SoundEvent VAMPIRE_CAMO_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_CAMO_ID);



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


    public static final String ZOMBIE_CHARGE = "zombie_charge";
    public static final ResourceLocation ZOMBIE_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ZOMBIE_CHARGE);
    public static SoundEvent ZOMBIE_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(ZOMBIE_CHARGE_ID);

    public static final String GOAT_CHARGE = "goat_charge";
    public static final ResourceLocation GOAT_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GOAT_CHARGE);
    public static SoundEvent GOAT_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(GOAT_CHARGE_ID);

    public static final String GOAT_DASH = "goat_dash";
    public static final ResourceLocation GOAT_DASH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+GOAT_DASH);
    public static SoundEvent GOAT_DASH_EVENT = SoundEvent.createVariableRangeEvent(GOAT_DASH_ID);

    public static final String ACHTUNG_BURST = "achtung_burst";
    public static final ResourceLocation ACHTUNG_BURST_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ACHTUNG_BURST);
    public static SoundEvent ACHTUNG_BURST_EVENT = SoundEvent.createVariableRangeEvent(ACHTUNG_BURST_ID);


    public static final String RIPPER_EYES_CHARGE = "ripper_eyes_charge";
    public static final ResourceLocation RIPPER_EYES_CHARGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RIPPER_EYES_CHARGE);
    public static SoundEvent RIPPER_EYES_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(RIPPER_EYES_CHARGE_ID);
    public static final String RIPPER_EYES_SHORT = "ripper_eyes_short";
    public static final ResourceLocation RIPPER_EYES_SHORT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RIPPER_EYES_SHORT);
    public static SoundEvent RIPPER_EYES_SHORT_EVENT = SoundEvent.createVariableRangeEvent(RIPPER_EYES_SHORT_ID);
    public static final String RIPPER_EYES_BEAM = "ripper_eyes_beam";
    public static final ResourceLocation RIPPER_EYES_BEAM_ID = new ResourceLocation(Roundabout.MOD_ID+":"+RIPPER_EYES_BEAM);
    public static SoundEvent RIPPER_EYES_BEAM_EVENT = SoundEvent.createVariableRangeEvent(RIPPER_EYES_BEAM_ID);


    public static final String SUMMON_DIVER_DOWN = "summon_diver_down";
    public static final ResourceLocation SUMMON_DIVER_DOWN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_DIVER_DOWN);
    public static SoundEvent SUMMON_DIVER_DOWN_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_DIVER_DOWN_ID);

    public static final String SUMMON_GREEN_DAY = "summon_green_day";
    public static final ResourceLocation SUMMON_GREEN_DAY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+SUMMON_GREEN_DAY);
    public static SoundEvent SUMMON_GREEN_DAY_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_GREEN_DAY_ID);

    public static final String GREEN_DAY_ARM_SPIN = "green_day_arm_spin";
    public static final ResourceLocation GREEN_DAY_ARM_SPIN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ GREEN_DAY_ARM_SPIN);
    public static SoundEvent GREEN_DAY_ARM_SPIN_EVENT = SoundEvent.createVariableRangeEvent(GREEN_DAY_ARM_SPIN_ID);

    public static final String GREEN_DAY_SPLIT = "green_day_split";
    public static final ResourceLocation GREEN_DAY_SPLIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ GREEN_DAY_SPLIT);
    public static SoundEvent GREEN_DAY_SPLIT_EVENT = SoundEvent.createVariableRangeEvent(GREEN_DAY_SPLIT_ID);

    public static final String GREEN_DAY_MOLD_SPREAD = "green_day_mold_spread";
    public static final ResourceLocation GREEN_DAY_MOLD_SPREAD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ GREEN_DAY_MOLD_SPREAD);
    public static SoundEvent GREEN_DAY_MOLD_SPREAD_EVENT = SoundEvent.createVariableRangeEvent(GREEN_DAY_MOLD_SPREAD_ID);

    public static final String GREEN_DAY_STITCH = "green_day_stitch";
    public static final ResourceLocation GREEN_DAY_STITCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ GREEN_DAY_STITCH);
    public static SoundEvent GREEN_DAY_STITCH_EVENT = SoundEvent.createVariableRangeEvent(GREEN_DAY_STITCH_ID);

    public static final String SUMMON_KING_CRIMSON = "summon_king_crimson";
    public static final ResourceLocation SUMMON_KING_CRIMSON_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ SUMMON_KING_CRIMSON);
    public static SoundEvent SUMMON_KING_CRIMSON_EVENT = SoundEvent.createVariableRangeEvent(SUMMON_KING_CRIMSON_ID);

    public static final String EPITAPH_ACTIVATE = "epitaph_activate";
    public static final ResourceLocation EPITAPH_ACTIVATE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ EPITAPH_ACTIVATE);
    public static SoundEvent EPITAPH_ACTIVATE_EVENT = SoundEvent.createVariableRangeEvent(EPITAPH_ACTIVATE_ID);

    public static final String EPITAPH_FADE = "epitaph_fade";
    public static final ResourceLocation EPITAPH_FADE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ EPITAPH_FADE);
    public static SoundEvent EPITAPH_FADE_EVENT = SoundEvent.createVariableRangeEvent(EPITAPH_FADE_ID);


    public static final String KING_CRIMSON_PUNCH = "king_crimson_punch";
    public static final ResourceLocation KING_CRIMSON_PUNCH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ KING_CRIMSON_PUNCH);
    public static SoundEvent KING_CRIMSON_PUNCH_EVENT = SoundEvent.createVariableRangeEvent(KING_CRIMSON_PUNCH_ID);
    public static final String KING_CRIMSON_IMPALE = "king_crimson_impale";
    public static final ResourceLocation KING_CRIMSON_IMPALE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ KING_CRIMSON_IMPALE);
    public static SoundEvent KING_CRIMSON_IMPALE_EVENT = SoundEvent.createVariableRangeEvent(KING_CRIMSON_IMPALE_ID);

    public static final String KING_CRIMSON_PUNCH_2 = "king_crimson_punch_2";
    public static final ResourceLocation KING_CRIMSON_PUNCH_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ KING_CRIMSON_PUNCH_2);
    public static SoundEvent KING_CRIMSON_PUNCH_2_EVENT = SoundEvent.createVariableRangeEvent(KING_CRIMSON_PUNCH_2_ID);
    public static final String KING_CRIMSON_PUNCH_3 = "king_crimson_punch_3";
    public static final ResourceLocation KING_CRIMSON_PUNCH_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ KING_CRIMSON_PUNCH_3);
    public static SoundEvent KING_CRIMSON_PUNCH_3_EVENT = SoundEvent.createVariableRangeEvent(KING_CRIMSON_PUNCH_3_ID);
    public static final String KING_CRIMSON_PUNCH_4 = "king_crimson_punch_4";
    public static final ResourceLocation KING_CRIMSON_PUNCH_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ KING_CRIMSON_PUNCH_4);
    public static SoundEvent KING_CRIMSON_PUNCH_4_EVENT = SoundEvent.createVariableRangeEvent(KING_CRIMSON_PUNCH_4_ID);
    public static final String KING_CRIMSON_PUNCH_5 = "king_crimson_punch_5";
    public static final ResourceLocation KING_CRIMSON_PUNCH_5_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ KING_CRIMSON_PUNCH_5);
    public static SoundEvent KING_CRIMSON_PUNCH_5_EVENT = SoundEvent.createVariableRangeEvent(KING_CRIMSON_PUNCH_5_ID);

    public static final String KING_CRIMSON_BARRAGE_HIT = "king_crimson_barrage_hit";
    public static final ResourceLocation KING_CRIMSON_BARRAGE_HIT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ KING_CRIMSON_BARRAGE_HIT);
    public static SoundEvent KING_CRIMSON_BARRAGE_HIT_EVENT = SoundEvent.createVariableRangeEvent(KING_CRIMSON_BARRAGE_HIT_ID);


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

    public static final String VAMPIRE_CRUMBLE = "vampire_crumble";
    public static final ResourceLocation VAMPIRE_CRUMBLE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_CRUMBLE);
    public static SoundEvent VAMPIRE_CRUMBLE_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_CRUMBLE_ID);

    public static final String VAMPIRE_WALL_GRIP = "vampire_wall_grip";
    public static final ResourceLocation VAMPIRE_WALL_GRIP_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_WALL_GRIP);
    public static SoundEvent VAMPIRE_WALL_GRIP_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_WALL_GRIP_ID);

    //eraweB
    public static final String VAMPIRE_MESSAGE = "vampire_message";
    public static final ResourceLocation VAMPIRE_MESSAGE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_MESSAGE);
    public static SoundEvent VAMPIRE_MESSAGE_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_MESSAGE_ID);



    public static final String HEARTBEAT = "heartbeat";
    public static final ResourceLocation HEARTBEAT_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEARTBEAT);
    public static SoundEvent HEARTBEAT_EVENT = SoundEvent.createVariableRangeEvent(HEARTBEAT_ID);
    public static final String HEARTBEAT2= "heartbeat2";
    public static final ResourceLocation HEARTBEAT2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEARTBEAT2);
    public static SoundEvent HEARTBEAT2_EVENT = SoundEvent.createVariableRangeEvent(HEARTBEAT2_ID);
    public static final String HEARTBEAT3 = "heartbeat3";
    public static final ResourceLocation HEARTBEAT3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HEARTBEAT3);
    public static SoundEvent HEARTBEAT3_EVENT = SoundEvent.createVariableRangeEvent(HEARTBEAT3_ID);



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

    public static final String VAMPIRE_DIVE = "vampire_dive";
    public static final ResourceLocation VAMPIRE_DIVE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VAMPIRE_DIVE);
    public static SoundEvent VAMPIRE_DIVE_EVENT = SoundEvent.createVariableRangeEvent(VAMPIRE_DIVE_ID);


    public static final String TORTURE_DANCE = "torture_dance";
    public static final ResourceLocation TORTURE_DANCE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+TORTURE_DANCE);
    public static SoundEvent TORTURE_DANCE_EVENT = SoundEvent.createVariableRangeEvent(TORTURE_DANCE_ID);
    public static final String HALLELUJAH = "hallelujah";
    public static final ResourceLocation HALLELUJAH_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HALLELUJAH);
    public static SoundEvent HALLELUJAH_EVENT = SoundEvent.createVariableRangeEvent(HALLELUJAH_ID);

    public static final String FLESH_BUD = "flesh_bud_goes";
    public static final ResourceLocation FLESH_BUD_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FLESH_BUD);
    public static SoundEvent FLESH_BUD_EVENT = SoundEvent.createVariableRangeEvent(FLESH_BUD_ID);
    public static final String FLESH_BUD_REMOVAL = "flesh_bud_removal";
    public static final ResourceLocation FLESH_BUD_REMOVAL_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FLESH_BUD_REMOVAL);
    public static SoundEvent FLESH_BUD_REMOVAL_EVENT = SoundEvent.createVariableRangeEvent(FLESH_BUD_REMOVAL_ID);
    public static final String HAIR_TOGGLE = "hair_toggle";
    public static final ResourceLocation HAIR_TOGGLE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HAIR_TOGGLE);
    public static SoundEvent HAIR_TOGGLE_EVENT = SoundEvent.createVariableRangeEvent(HAIR_TOGGLE_ID);

    public static final String HAIR_SHARPEN = "hair_sharpen";
    public static final ResourceLocation HAIR_SHARPEN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+HAIR_SHARPEN);
    public static SoundEvent HAIR_SHARPEN_EVENT = SoundEvent.createVariableRangeEvent(HAIR_SHARPEN_ID);

    public static final String FULL_FREEZE = "full_freeze";
    public static final ResourceLocation FULL_FREEZE_ID = new ResourceLocation(Roundabout.MOD_ID+":"+FULL_FREEZE);
    public static SoundEvent FULL_FREEZE_EVENT = SoundEvent.createVariableRangeEvent(FULL_FREEZE_ID);

    public static final String ICE_BREAKER = "ice_breaker";
    public static final ResourceLocation ICE_BREAKER_ID = new ResourceLocation(Roundabout.MOD_ID+":"+ICE_BREAKER);
    public static SoundEvent ICE_BREAKER_EVENT = SoundEvent.createVariableRangeEvent(ICE_BREAKER_ID);

    public static final String COMBAT_PUNCH_1 = "combat_punch_1";
    public static final ResourceLocation COMBAT_PUNCH_1_ID = new ResourceLocation(Roundabout.MOD_ID+":"+COMBAT_PUNCH_1);
    public static SoundEvent COMBAT_PUNCH_1_EVENT = SoundEvent.createVariableRangeEvent(COMBAT_PUNCH_1_ID);
    public static final String COMBAT_PUNCH_2 = "combat_punch_2";
    public static final ResourceLocation COMBAT_PUNCH_2_ID = new ResourceLocation(Roundabout.MOD_ID+":"+COMBAT_PUNCH_2);
    public static SoundEvent COMBAT_PUNCH_2_EVENT = SoundEvent.createVariableRangeEvent(COMBAT_PUNCH_2_ID);
    public static final String COMBAT_PUNCH_3 = "combat_punch_3";
    public static final ResourceLocation COMBAT_PUNCH_3_ID = new ResourceLocation(Roundabout.MOD_ID+":"+COMBAT_PUNCH_3);
    public static SoundEvent COMBAT_PUNCH_3_EVENT = SoundEvent.createVariableRangeEvent(COMBAT_PUNCH_3_ID);
    public static final String COMBAT_PUNCH_4 = "combat_punch_4";
    public static final ResourceLocation COMBAT_PUNCH_4_ID = new ResourceLocation(Roundabout.MOD_ID+":"+COMBAT_PUNCH_4);
    public static SoundEvent COMBAT_PUNCH_4_EVENT = SoundEvent.createVariableRangeEvent(COMBAT_PUNCH_4_ID);

    public static final String VSONG_SILENT_REVERIES = "vsong_silent_reveries";
    public static final ResourceLocation VSONG_SILENT_REVERIES_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VSONG_SILENT_REVERIES);
    public static SoundEvent VSONG_SILENT_REVERIES_EVENT = SoundEvent.createVariableRangeEvent(VSONG_SILENT_REVERIES_ID);

    public static final String VSONG_TWISTED = "vsong_twisted";
    public static final ResourceLocation VSONG_TWISTED_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VSONG_TWISTED);
    public static SoundEvent VSONG_TWISTED_EVENT = SoundEvent.createVariableRangeEvent(VSONG_TWISTED_ID);

    public static final String VSONG_GOTHIC_ORGAN = "vsong_gothic_organ";
    public static final ResourceLocation VSONG_GOTHIC_ORGAN_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VSONG_GOTHIC_ORGAN);
    public static SoundEvent VSONG_GOTHIC_ORGAN_EVENT = SoundEvent.createVariableRangeEvent(VSONG_GOTHIC_ORGAN_ID);

    public static final String VSONG_BLOODCURDLING_MOMENTS = "vsong_bloodcurdling_moments";
    public static final ResourceLocation VSONG_BLOODCURDLING_MOMENTS_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VSONG_BLOODCURDLING_MOMENTS);
    public static SoundEvent VSONG_BLOODCURDLING_MOMENTS_EVENT = SoundEvent.createVariableRangeEvent(VSONG_BLOODCURDLING_MOMENTS_ID);

    public static final String VSONG_DAMNABLE_CEREMONY = "vsong_damnable_ceremony";
    public static final ResourceLocation VSONG_DAMNABLE_CEREMONY_ID = new ResourceLocation(Roundabout.MOD_ID+":"+VSONG_DAMNABLE_CEREMONY);
    public static SoundEvent VSONG_DAMNABLE_CEREMONY_EVENT = SoundEvent.createVariableRangeEvent(VSONG_DAMNABLE_CEREMONY_ID);



    public static void registerSoundEvents(){
    }

    private static SoundEvent registerSoundEvent(String name){
        ResourceLocation id = new ResourceLocation(Roundabout.MOD_ID,name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}
