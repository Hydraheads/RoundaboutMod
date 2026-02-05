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
        addSound(ModSounds.STAND_ARROW_CHARGE_ID, ModSounds.STAND_ARROW_CHARGE_EVENT);
        addSound(ModSounds.STAND_ARROW_USE_ID, ModSounds.STAND_ARROW_USE_EVENT);
        addSound(ModSounds.SIGN_HIT_ID, ModSounds.SIGN_HIT_EVENT);
        addSound(ModSounds.TERRIER_SOUND_ID, ModSounds.TERRIER_SOUND_EVENT);
        addSound(ModSounds.STAR_SUMMON_SOUND_ID, ModSounds.STAR_SUMMON_SOUND_EVENT);
        addSound(ModSounds.SUMMON_MANDOM_ID, ModSounds.SUMMON_MANDOM_EVENT);
        addSound(ModSounds.MANDOM_REWIND_ID, ModSounds.MANDOM_REWIND_EVENT);
        addSound(ModSounds.WORLD_SUMMON_SOUND_ID, ModSounds.WORLD_SUMMON_SOUND_EVENT);
        addSound(ModSounds.PUNCH_1_SOUND_ID, ModSounds.PUNCH_1_SOUND_EVENT);
        addSound(ModSounds.PUNCH_2_SOUND_ID, ModSounds.PUNCH_2_SOUND_EVENT);
        addSound(ModSounds.PUNCH_3_SOUND_ID, ModSounds.PUNCH_3_SOUND_EVENT);
        addSound(ModSounds.PUNCH_4_SOUND_ID, ModSounds.PUNCH_4_SOUND_EVENT);
        addSound(ModSounds.BODY_BAG_ID, ModSounds.BODY_BAG_EVENT);
        addSound(ModSounds.JUSTICE_SELECT_ID, ModSounds.JUSTICE_SELECT_EVENT);
        addSound(ModSounds.JUSTICE_SELECT_ATTACK_ID, ModSounds.JUSTICE_SELECT_ATTACK_EVENT);
        addSound(ModSounds.STAND_GUARD_SOUND_ID, ModSounds.STAND_GUARD_SOUND_EVENT);
        addSound(ModSounds.MELEE_GUARD_SOUND_ID, ModSounds.MELEE_GUARD_SOUND_EVENT);
        addSound(ModSounds.HIT_1_SOUND_ID, ModSounds.HIT_1_SOUND_EVENT);
        addSound(ModSounds.INHALE_ID, ModSounds.INHALE_EVENT);

        addSound(ModSounds.TWAU_BARRAGE_ID, ModSounds.TWAU_BARRAGE_EVENT);
        addSound(ModSounds.TWAU_BARRAGE_2_ID, ModSounds.TWAU_BARRAGE_2_EVENT);
        addSound(ModSounds.TWAU_MUDA_ID, ModSounds.TWAU_MUDA_EVENT);
        addSound(ModSounds.TWAU_MUDA_2_ID, ModSounds.TWAU_MUDA_2_EVENT);
        addSound(ModSounds.TWAU_MUDA_3_ID, ModSounds.TWAU_MUDA_3_EVENT);
        addSound(ModSounds.TWAU_WRY_ID, ModSounds.TWAU_WRY_EVENT);
        addSound(ModSounds.TWAU_HEY_ID, ModSounds.TWAU_HEY_EVENT);
        addSound(ModSounds.TWAU_TIMESTOP_ID, ModSounds.TWAU_TIMESTOP_EVENT);
        addSound(ModSounds.TWAU_TIMESTOP_2_ID, ModSounds.TWAU_TIMESTOP_2_EVENT);
        addSound(ModSounds.TWAU_USHA_ID, ModSounds.TWAU_USHA_EVENT);
        addSound(ModSounds.TWAU_THE_WORLD_ID, ModSounds.TWAU_THE_WORLD_EVENT);
        addSound(ModSounds.OVA_PLATINUM_BARRAGE_ID, ModSounds.OVA_PLATINUM_BARRAGE_EVENT);
        addSound(ModSounds.OVA_PLATINUM_ORA_ID, ModSounds.OVA_PLATINUM_ORA_EVENT);
        addSound(ModSounds.OVA_PLATINUM_ORA_2_ID, ModSounds.OVA_PLATINUM_ORA_2_EVENT);
        addSound(ModSounds.OVA_PLATINUM_ORA_3_ID, ModSounds.OVA_PLATINUM_ORA_3_EVENT);
        addSound(ModSounds.OVA_PLATINUM_ORA_4_ID, ModSounds.OVA_PLATINUM_ORA_4_EVENT);
        addSound(ModSounds.DSP_SUMMON_ID, ModSounds.DSP_SUMMON_EVENT);

        addSound(ModSounds.OVA_BARRAGE_ID, ModSounds.OVA_BARRAGE_EVENT);
        addSound(ModSounds.OVA_BARRAGE_2_ID, ModSounds.OVA_BARRAGE_2_EVENT);
        addSound(ModSounds.OVA_MUDA_ID, ModSounds.OVA_MUDA_EVENT);
        addSound(ModSounds.OVA_MUDA_2_ID, ModSounds.OVA_MUDA_2_EVENT);
        addSound(ModSounds.OVA_THE_WORLD_ID, ModSounds.OVA_THE_WORLD_EVENT);
        addSound(ModSounds.OVA_THE_WORLD_2_ID, ModSounds.OVA_THE_WORLD_2_EVENT);
        addSound(ModSounds.OVA_SUMMON_THE_WORLD_ID, ModSounds.OVA_SUMMON_THE_WORLD_EVENT);
        addSound(ModSounds.OVA_TIME_RESUME_ID, ModSounds.OVA_TIME_RESUME_EVENT);
        addSound(ModSounds.OVA_SHORT_TS_ID, ModSounds.OVA_SHORT_TS_EVENT);
        addSound(ModSounds.OVA_LONG_TS_ID, ModSounds.OVA_LONG_TS_EVENT);
        addSound(ModSounds.OVA_SP_TS_ID, ModSounds.OVA_SP_TS_EVENT);
        addSound(ModSounds.CROSSFIRE_SHOOT_ID, ModSounds.CROSSFIRE_SHOOT_EVENT);
        addSound(ModSounds.CROSSFIRE_EXPLODE_ID, ModSounds.CROSSFIRE_EXPLODE_EVENT);
        addSound(ModSounds.MAGICIANS_RED_CRY_ID, ModSounds.MAGICIANS_RED_CRY_EVENT);
        addSound(ModSounds.MAGICIANS_RED_CRY_2_ID, ModSounds.MAGICIANS_RED_CRY_2_EVENT);
        addSound(ModSounds.MAGICIANS_RED_CRY_3_ID, ModSounds.MAGICIANS_RED_CRY_3_EVENT);
        addSound(ModSounds.MAGICIANS_RED_CHARGE_ID, ModSounds.MAGICIANS_RED_CHARGE_EVENT);
        addSound(ModSounds.FLAMETHROWER_ID, ModSounds.FLAMETHROWER_EVENT);
        addSound(ModSounds.STAND_FLAME_HIT_ID, ModSounds.STAND_FLAME_HIT_EVENT);
        addSound(ModSounds.FIREBALL_SHOOT_ID, ModSounds.FIREBALL_SHOOT_EVENT);
        addSound(ModSounds.FIREBALL_HIT_ID, ModSounds.FIREBALL_HIT_EVENT);
        addSound(ModSounds.LASSO_ID, ModSounds.LASSO_EVENT);

        addSound(ModSounds.LEVELUP_ID, ModSounds.LEVELUP_EVENT);

        addSound(ModSounds.KNIFE_THROW_SOUND_ID, ModSounds.KNIFE_THROW_SOUND_EVENT);
        addSound(ModSounds.KNIFE_BUNDLE_THROW_SOUND_ID, ModSounds.KNIFE_BUNDLE_THROW_SOUND_EVENT);
        addSound(ModSounds.KNIFE_IMPACT_ID, ModSounds.KNIFE_IMPACT_EVENT);
        addSound(ModSounds.KNIFE_IMPACT_GROUND_ID, ModSounds.KNIFE_IMPACT_GROUND_EVENT);
        addSound(ModSounds.SOFT_AND_WET_KICK_ID, ModSounds.SOFT_AND_WET_KICK_EVENT);
        addSound(ModSounds.WATER_ENCASE_ID, ModSounds.WATER_ENCASE_EVENT);

        addSound(ModSounds.FOG_MORPH_ID, ModSounds.FOG_MORPH_EVENT);
        addSound(ModSounds.CACKLE_ID, ModSounds.CACKLE_EVENT);
        addSound(ModSounds.AIR_BUBBLE_ID, ModSounds.AIR_BUBBLE_EVENT);
        addSound(ModSounds.BUBBLE_HOVERED_OVER_ID, ModSounds.BUBBLE_HOVERED_OVER_EVENT);
        addSound(ModSounds.GO_BEYOND_HIT_ID, ModSounds.GO_BEYOND_HIT_EVENT);
        addSound(ModSounds.GO_BEYOND_LAUNCH_ID, ModSounds.GO_BEYOND_LAUNCH_EVENT);
        addSound(ModSounds.BUBBLE_POP_ID, ModSounds.BUBBLE_POP_EVENT);

        addSound(ModSounds.EXPLOSIVE_BUBBLE_POP_ID, ModSounds.EXPLOSIVE_BUBBLE_POP_EVENT);
        addSound(ModSounds.EXPLOSIVE_BUBBLE_SHOT_ID, ModSounds.EXPLOSIVE_BUBBLE_SHOT_EVENT);
        addSound(ModSounds.EXPLOSIVE_BUBBLE_SWITCH_ID, ModSounds.EXPLOSIVE_BUBBLE_SWITCH_EVENT);
        addSound(ModSounds.EXPLOSIVE_BUBBLE_SWITCH_OFF_ID, ModSounds.EXPLOSIVE_BUBBLE_SWITCH_OFF_EVENT);
        addSound(ModSounds.EXPLOSIVE_SPIN_MODE_ID, ModSounds.EXPLOSIVE_SPIN_MODE_EVENT);

        addSound(ModSounds.CREAM_SUMMON_ID, ModSounds.CREAM_SUMMON_EVENT);
        addSound(ModSounds.CREAM_VOID_ATTACK_ID, ModSounds.CREAM_VOID_ATTACK_EVENT);

        addSound(ModSounds.SNUBNOSE_FIRE_ID, ModSounds.SNUBNOSE_FIRE_EVENT);
        addSound(ModSounds.SNUBNOSE_DRY_FIRE_ID, ModSounds.SNUBNOSE_DRY_FIRE_EVENT);
        addSound(ModSounds.SNUBNOSE_RELOAD_ID, ModSounds.SNUBNOSE_RELOAD_EVENT);

        addSound(ModSounds.TOMMY_FIRE_ID, ModSounds.TOMMY_FIRE_EVENT);
        addSound(ModSounds.TOMMY_RELOAD_ID, ModSounds.TOMMY_RELOAD_EVENT);

        addSound(ModSounds.COLT_FIRE_ID, ModSounds.COLT_FIRE_EVENT);
        addSound(ModSounds.COLT_RELOAD_ID, ModSounds.COLT_RELOAD_EVENT);

        addSound(ModSounds.JACKAL_FIRE_ID, ModSounds.JACKAL_FIRE_EVENT);
        addSound(ModSounds.JACKAL_RELOAD_ID, ModSounds.JACKAL_RELOAD_EVENT);

        addSound(ModSounds.BULLET_PENTRATION_ID, ModSounds.BULLET_PENTRATION_EVENT);
        addSound(ModSounds.BULLET_RICOCHET_ID, ModSounds.BULLET_RICOCHET_EVENT);

        addSound(ModSounds.ROAD_ROLLER_AMBIENT_ID, ModSounds.ROAD_ROLLER_AMBIENT_EVENT);
        addSound(ModSounds.ROAD_ROLLER_EXPLOSION_ID, ModSounds.ROAD_ROLLER_EXPLOSION_EVENT);
        addSound(ModSounds.ROAD_ROLLER_MIXING_ID, ModSounds.ROAD_ROLLER_MIXING_EVENT);

        addSound(ModSounds.BUBBLE_CREATE_ID, ModSounds.BUBBLE_CREATE_EVENT);
        addSound(ModSounds.BLOOD_SUCK_ID, ModSounds.BLOOD_SUCK_EVENT);
        addSound(ModSounds.BLOOD_SPEED_ID, ModSounds.BLOOD_SPEED_EVENT);
        addSound(ModSounds.BLOOD_SUCK_DRAIN_ID, ModSounds.BLOOD_SUCK_DRAIN_EVENT);
        addSound(ModSounds.BIG_BUBBLE_CREATE_ID, ModSounds.BIG_BUBBLE_CREATE_EVENT);
        addSound(ModSounds.BUBBLE_PLUNDER_ID, ModSounds.BUBBLE_PLUNDER_EVENT);
        addSound(ModSounds.SOFT_AND_WET_BARRAGE_ID, ModSounds.SOFT_AND_WET_BARRAGE_EVENT);
        addSound(ModSounds.SOFT_AND_WET_BARRAGE_2_ID, ModSounds.SOFT_AND_WET_BARRAGE_2_EVENT);
        addSound(ModSounds.HYPNOSIS_ID, ModSounds.HYPNOSIS_EVENT);
        addSound(ModSounds.VAMPIRE_AWAKEN_ID, ModSounds.VAMPIRE_AWAKEN_EVENT);

        addSound(ModSounds.LOCACACA_PETRIFY_ID, ModSounds.LOCACACA_PETRIFY_EVENT);
        addSound(ModSounds.LOCACACA_FUSION_ID, ModSounds.LOCACACA_FUSION_EVENT);

        addSound(ModSounds.DODGE_ID, ModSounds.DODGE_EVENT);
        addSound(ModSounds.STAND_LEAP_ID, ModSounds.STAND_LEAP_EVENT);
        addSound(ModSounds.FALL_BRACE_ID, ModSounds.FALL_BRACE_EVENT);

        addSound(ModSounds.TIME_SNAP_ID, ModSounds.TIME_SNAP_EVENT);

        addSound(ModSounds.IMPALE_CHARGE_ID, ModSounds.IMPALE_CHARGE_EVENT);
        addSound(ModSounds.IMPALE_HIT_ID, ModSounds.IMPALE_HIT_EVENT);

        addSound(ModSounds.SUMMON_SOFT_AND_WET_ID, ModSounds.SUMMON_SOFT_AND_WET_EVENT);
        addSound(ModSounds.THE_WORLD_ASSAULT_ID, ModSounds.THE_WORLD_ASSAULT_EVENT);
        addSound(ModSounds.THE_WORLD_WRY_ID, ModSounds.THE_WORLD_WRY_EVENT);
        addSound(ModSounds.THE_WORLD_MUDA_ID, ModSounds.THE_WORLD_MUDA_EVENT);

        addSound(ModSounds.STAND_THEWORLD_MUDA1_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT);
        addSound(ModSounds.STAND_THEWORLD_MUDA2_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA2_SOUND_EVENT);
        addSound(ModSounds.STAND_THEWORLD_MUDA3_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT);
        addSound(ModSounds.STAND_THEWORLD_MUDA4_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA4_SOUND_EVENT);
        addSound(ModSounds.STAND_THEWORLD_MUDA5_SOUND_ID, ModSounds.STAND_THEWORLD_MUDA5_SOUND_EVENT);

        addSound(ModSounds.STAR_FINGER_ID, ModSounds.STAR_FINGER_EVENT);
        addSound(ModSounds.STAR_FINGER_2_ID, ModSounds.STAR_FINGER_2_EVENT);
        addSound(ModSounds.STAR_FINGER_SILENT_ID, ModSounds.STAR_FINGER_SILENT_EVENT);
        addSound(ModSounds.STAR_PLATINUM_ORA_ID, ModSounds.STAR_PLATINUM_ORA_SOUND_EVENT);
        addSound(ModSounds.STAR_PLATINUM_ORA_2_ID, ModSounds.STAR_PLATINUM_ORA_2_SOUND_EVENT);
        addSound(ModSounds.STAR_PLATINUM_ORA_3_ID, ModSounds.STAR_PLATINUM_ORA_3_SOUND_EVENT);
        addSound(ModSounds.STAR_PLATINUM_ORA_RUSH_ID, ModSounds.STAR_PLATINUM_ORA_RUSH_SOUND_EVENT);
        addSound(ModSounds.STAR_PLATINUM_ORA_RUSH_2_ID, ModSounds.STAR_PLATINUM_ORA_RUSH_2_SOUND_EVENT);
        addSound(ModSounds.STAR_PLATINUM_SCOPE_ID, ModSounds.STAR_PLATINUM_SCOPE_EVENT);
        addSound(ModSounds.STAR_PLATINUM_TIMESTOP_ID, ModSounds.STAR_PLATINUM_TIMESTOP_SOUND_EVENT);
        addSound(ModSounds.STAR_PLATINUM_TIMESTOP_2_ID, ModSounds.STAR_PLATINUM_TIMESTOP_2_SOUND_EVENT);

        addSound(ModSounds.STAND_BARRAGE_WINDUP_ID, ModSounds.STAND_BARRAGE_WINDUP_EVENT);
        addSound(ModSounds.STAND_BARRAGE_MISS_ID, ModSounds.STAND_BARRAGE_MISS_EVENT);
        addSound(ModSounds.STAND_BARRAGE_BLOCK_ID, ModSounds.STAND_BARRAGE_BLOCK_EVENT);
        addSound(ModSounds.STAND_BARRAGE_HIT_ID, ModSounds.STAND_BARRAGE_HIT_EVENT);
        addSound(ModSounds.STAND_BARRAGE_END_BLOCK_ID, ModSounds.STAND_BARRAGE_END_BLOCK_EVENT);
        addSound(ModSounds.STAND_BARRAGE_HIT2_ID, ModSounds.STAND_BARRAGE_HIT2_EVENT);
        addSound(ModSounds.STAND_BARRAGE_END_ID, ModSounds.STAND_BARRAGE_END_EVENT);

        addSound(ModSounds.EXPLOSIVE_PUNCH_ID, ModSounds.EXPLOSIVE_PUNCH_EVENT);
        addSound(ModSounds.SNAP_ID, ModSounds.SNAP_EVENT);
        addSound(ModSounds.FIRESTORM_ID, ModSounds.FIRESTORM_EVENT);
        addSound(ModSounds.FIRE_BLAST_ID, ModSounds.FIRE_BLAST_EVENT);
        addSound(ModSounds.FIRE_WHOOSH_ID, ModSounds.FIRE_WHOOSH_EVENT);
        addSound(ModSounds.FIRE_STRIKE_ID, ModSounds.FIRE_STRIKE_EVENT);
        addSound(ModSounds.FIRE_STRIKE_LAST_ID, ModSounds.FIRE_STRIKE_LAST_EVENT);
        addSound(ModSounds.EXPLOSIVE_BAT_ID, ModSounds.EXPLOSIVE_BAT_EVENT);
        addSound(ModSounds.OVA_SUMMON_ID, ModSounds.OVA_SUMMON_EVENT);
        addSound(ModSounds.SUMMON_MAGICIAN_ID, ModSounds.SUMMON_MAGICIAN_EVENT);
        addSound(ModSounds.SUMMON_JUSTICE_ID, ModSounds.SUMMON_JUSTICE_EVENT);
        addSound(ModSounds.SUMMON_JUSTICE_2_ID, ModSounds.SUMMON_JUSTICE_2_EVENT);
        addSound(ModSounds.FINAL_KICK_ID, ModSounds.FINAL_KICK_EVENT);
        addSound(ModSounds.DREAD_SUMMON_ID, ModSounds.DREAD_SUMMON_EVENT);

        addSound(ModSounds.TIME_STOP_THE_WORLD_ID, ModSounds.TIME_STOP_THE_WORLD_EVENT);
        addSound(ModSounds.TIME_STOP_RESUME_THE_WORLD_ID, ModSounds.TIME_STOP_RESUME_THE_WORLD_EVENT);
        addSound(ModSounds.TIME_STOP_RESUME_THE_WORLD2_ID, ModSounds.TIME_STOP_RESUME_THE_WORLD2_EVENT);
        addSound(ModSounds.TIME_STOP_THE_WORLD2_ID, ModSounds.TIME_STOP_THE_WORLD2_EVENT);
        addSound(ModSounds.TIME_STOP_THE_WORLD3_ID, ModSounds.TIME_STOP_THE_WORLD3_EVENT);
        addSound(ModSounds.TIME_STOP_VOICE_THE_WORLD_ID, ModSounds.TIME_STOP_VOICE_THE_WORLD_EVENT);
        addSound(ModSounds.TIME_STOP_VOICE_THE_WORLD2_ID, ModSounds.TIME_STOP_VOICE_THE_WORLD2_EVENT);
        addSound(ModSounds.TIME_STOP_VOICE_THE_WORLD3_ID, ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT);
        addSound(ModSounds.TIME_STOP_CHARGE_THE_WORLD_ID, ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT);
        addSound(ModSounds.TIME_STOP_STAR_PLATINUM_ID, ModSounds.TIME_STOP_STAR_PLATINUM_EVENT);
        addSound(ModSounds.TIME_RESUME_ID, ModSounds.TIME_RESUME_EVENT);
        addSound(ModSounds.TIME_STOP_IMPACT_ID, ModSounds.TIME_STOP_IMPACT_EVENT);
        addSound(ModSounds.TIME_STOP_IMPACT2_ID, ModSounds.TIME_STOP_IMPACT2_EVENT);

        addSound(ModSounds.ARCADE_TIMESTOP_ID, ModSounds.ARCADE_TIMESTOP_EVENT);
        addSound(ModSounds.ARCADE_TIMESTOP_2_ID, ModSounds.ARCADE_TIMESTOP_2_EVENT);
        addSound(ModSounds.ARCADE_MUDA_ID, ModSounds.ARCADE_MUDA_EVENT);
        addSound(ModSounds.ARCADE_MUDA_2_ID, ModSounds.ARCADE_MUDA_2_EVENT);
        addSound(ModSounds.ARCADE_MUDA_3_ID, ModSounds.ARCADE_MUDA_3_EVENT);
        addSound(ModSounds.ARCADE_IMPALE_ID, ModSounds.ARCADE_IMPALE_EVENT);
        addSound(ModSounds.ARCADE_LONG_TS_ID, ModSounds.ARCADE_LONG_TS_EVENT);
        addSound(ModSounds.ARCADE_SHORT_TS_ID, ModSounds.ARCADE_SHORT_TS_EVENT);
        addSound(ModSounds.ARCADE_TIME_RESUME_ID, ModSounds.ARCADE_TIME_RESUME_EVENT);
        addSound(ModSounds.ARCADE_URI_ID, ModSounds.ARCADE_URI_EVENT);

        addSound(ModSounds.ARCADE_ORA_ID, ModSounds.ARCADE_ORA_EVENT);
        addSound(ModSounds.ARCADE_ORA_2_ID, ModSounds.ARCADE_ORA_2_EVENT);
        addSound(ModSounds.ARCADE_ORA_3_ID, ModSounds.ARCADE_ORA_3_EVENT);
        addSound(ModSounds.ARCADE_STAR_PLATINUM_TIME_STOP_ID, ModSounds.ARCADE_STAR_PLATINUM_TIME_STOP_EVENT);
        addSound(ModSounds.ARCADE_STAR_PLATINUM_SHORT_TS_ID, ModSounds.ARCADE_STAR_PLATINUM_SHORT_TS_EVENT);
        addSound(ModSounds.ARCADE_STAR_PLATINUM_BARRAGE_ID, ModSounds.ARCADE_STAR_PLATINUM_BARRAGE_EVENT);
        addSound(ModSounds.ARCADE_STAR_FINGER_ID, ModSounds.ARCADE_STAR_FINGER_EVENT);

        addSound(ModSounds.CINDERELLA_SUMMON_ID, ModSounds.CINDERELLA_SUMMON_EVENT);
        addSound(ModSounds.CINDERELLA_ATTACK_ID, ModSounds.CINDERELLA_ATTACK_EVENT);
        addSound(ModSounds.CINDERELLA_SPARKLE_ID, ModSounds.CINDERELLA_SPARKLE_EVENT);
        addSound(ModSounds.CINDERELLA_FAIL_ID, ModSounds.CINDERELLA_FAIL_EVENT);
        addSound(ModSounds.CINDERELLA_VISAGE_CREATION_ID, ModSounds.CINDERELLA_VISAGE_CREATION_EVENT);

        addSound(ModSounds.RATT_SUMMON_ID, ModSounds.RATT_SUMMON_EVENT);
        addSound(ModSounds.RATT_PLACE_ID, ModSounds.RATT_PLACE_EVENT);
        addSound(ModSounds.RATT_SCOPE_ID, ModSounds.RATT_SCOPE_EVENT);
        addSound(ModSounds.RATT_DESCOPE_ID, ModSounds.RATT_DESCOPE_EVENT);
        addSound(ModSounds.RATT_DART_THUNK_ID, ModSounds.RATT_DART_THUNK_EVENT);
        addSound(ModSounds.RATT_DART_IMPACT_ID, ModSounds.RATT_DART_IMPACT_EVENT);
        addSound(ModSounds.RATT_LOADING_ID, ModSounds.RATT_LOADING_EVENT);
        addSound(ModSounds.RATT_FIRING_ID, ModSounds.RATT_FIRING_EVENT);
        addSound(ModSounds.RATT_LEAP_ID, ModSounds.RATT_LEAP_EVENT);
        addSound(ModSounds.RATT_MODE_CHANGE_ID, ModSounds.RATT_MODE_CHANGE_EVENT);

        addSound(ModSounds.ANUBIS_POSSESSION_ID, ModSounds.ANUBIS_POSSESSION_EVENT);
        addSound(ModSounds.ANUBIS_SUMMON_ID, ModSounds.ANUBIS_SUMMON_EVENT);
        addSound(ModSounds.ANUBIS_ALLURING_ID, ModSounds.ANUBIS_ALLURING_EVENT);
        addSound(ModSounds.ANUBIS_RAGING_ID, ModSounds.ANUBIS_RAGING_EVENT);
        addSound(ModSounds.ANUBIS_BACKFLIP_ID, ModSounds.ANUBIS_BACKFLIP_EVENT);
        addSound(ModSounds.ANUBIS_POGO_LAUNCH_ID, ModSounds.ANUBIS_POGO_LAUNCH_EVENT);
        addSound(ModSounds.ANUBIS_POGO_HIT_ID, ModSounds.ANUBIS_POGO_HIT_EVENT);
        addSound(ModSounds.ANUBIS_BARRAGE_1_ID, ModSounds.ANUBIS_BARRAGE_1_EVENT);
        addSound(ModSounds.ANUBIS_BARRAGE_1_HIT_ID, ModSounds.ANUBIS_BARRAGE_1_HIT_EVENT);
        addSound(ModSounds.ANUBIS_BARRAGE_END_ID, ModSounds.ANUBIS_BARRAGE_END_EVENT);
        addSound(ModSounds.ANUBIS_SWING_ID, ModSounds.ANUBIS_SWING_EVENT);
        addSound(ModSounds.ANUBIS_CROUCH_SWING_ID, ModSounds.ANUBIS_CROUCH_SWING_EVENT);
        addSound(ModSounds.ANUBIS_DOUBLE_CUT_ID, ModSounds.ANUBIS_DOUBLE_CUT_EVENT);
        addSound(ModSounds.ANUBIS_THRUST_CUT_ID, ModSounds.ANUBIS_THRUST_CUT_EVENT);
        addSound(ModSounds.ANUBIS_THRUST_MISS_ID, ModSounds.ANUBIS_THRUST_MISS_EVENT);
        addSound(ModSounds.ANUBIS_UPPERCUT_ID, ModSounds.ANUBIS_UPPERCUT_EVENT);
        addSound(ModSounds.ANUBIS_SHIELDBREAK_ID, ModSounds.ANUBIS_SHIELDBREAK_EVENT);
        addSound(ModSounds.ANUBIS_EXTRA_ID, ModSounds.ANUBIS_EXTRA_EVENT);



        addSound(ModSounds.KILLER_QUEEN_BARRAGE_ID, ModSounds.KILLER_QUEEN_BARRAGE_EVENT);
        addSound(ModSounds.KILLER_QUEEN_SUMMON_ID, ModSounds.KILLER_QUEEN_SUMMON_EVENT);

        addSound(ModSounds.THE_WORLD_OVER_HEAVEN_ID, ModSounds.THE_WORLD_OVER_HEAVEN_EVENT);
        addSound(ModSounds.UNLOCK_SKIN_ID, ModSounds.UNLOCK_SKIN_EVENT);

        addSound(ModSounds.TIME_STOP_TICKING_ID, ModSounds.TIME_STOP_TICKING_EVENT);
        addSound(ModSounds.CAN_BOUNCE_ID, ModSounds.CAN_BOUNCE_EVENT);
        addSound(ModSounds.CAN_BOUNCE_END_ID, ModSounds.CAN_BOUNCE_END_EVENT);
        addSound(ModSounds.GASOLINE_EXPLOSION_ID, ModSounds.GASOLINE_EXPLOSION_EVENT);
        addSound(ModSounds.GAS_CAN_THROW_ID, ModSounds.GAS_CAN_THROW_EVENT);
        addSound(ModSounds.MATCH_THROW_ID, ModSounds.MATCH_THROW_EVENT);
        addSound(ModSounds.HARPOON_THROW_ID, ModSounds.HARPOON_THROW_EVENT);
        addSound(ModSounds.HARPOON_HIT_ID, ModSounds.HARPOON_HIT_EVENT);
        addSound(ModSounds.HARPOON_CRIT_ID, ModSounds.HARPOON_CRIT_EVENT);
        addSound(ModSounds.HARPOON_GROUND_ID, ModSounds.HARPOON_GROUND_EVENT);
        addSound(ModSounds.HARPOON_RETURN_ID, ModSounds.HARPOON_RETURN_EVENT);
        addSound(ModSounds.BLOOD_REGEN_FINISH_ID, ModSounds.BLOOD_REGEN_FINISH_EVENT);
        addSound(ModSounds.BLOOD_REGEN_ID, ModSounds.BLOOD_REGEN_EVENT);

        addSound(ModSounds.BOWLER_HAT_AIM_ID, ModSounds.BOWLER_HAT_AIM_SOUND_EVENT);
        addSound(ModSounds.BOWLER_HAT_FLY_ID, ModSounds.BOWLER_HAT_FLY_SOUND_EVENT);
        addSound(ModSounds.VAMPIRE_DASH_ID, ModSounds.VAMPIRE_DASH_EVENT);
        addSound(ModSounds.VAMPIRE_GLEAM_ID, ModSounds.VAMPIRE_GLEAM_EVENT);

        addSound(ModSounds.GLAIVE_CHARGE_ID, ModSounds.GLAIVE_CHARGE_EVENT);
        addSound(ModSounds.GLAIVE_ATTACK_ID, ModSounds.GLAIVE_ATTACK_EVENT);
        addSound(ModSounds.FOG_CLONE_ID, ModSounds.FOG_CLONE_EVENT);
        addSound(ModSounds.POP_ID, ModSounds.POP_EVENT);

        addSound(ModSounds.BLOCK_GRAB_ID, ModSounds.BLOCK_GRAB_EVENT);
        addSound(ModSounds.BLOCK_THROW_ID, ModSounds.BLOCK_THROW_EVENT);
        addSound(ModSounds.BALL_BEARING_SHOT_ID, ModSounds.BALL_BEARING_SHOT_EVENT);
        addSound(ModSounds.EVIL_AURA_BLAST_ID, ModSounds.EVIL_AURA_BLAST_EVENT);
        addSound(ModSounds.AURA_IMPACT_ID, ModSounds.AURA_IMPACT_EVENT);
        addSound(ModSounds.ITEM_CATCH_ID, ModSounds.ITEM_CATCH_EVENT);

        addSound(ModSounds.HEY_YA_1_ID, ModSounds.HEY_YA_1_EVENT);
        addSound(ModSounds.HEY_YA_2_ID, ModSounds.HEY_YA_2_EVENT);
        addSound(ModSounds.HEY_YA_3_ID, ModSounds.HEY_YA_3_EVENT);
        addSound(ModSounds.HEY_YA_4_ID, ModSounds.HEY_YA_4_EVENT);
        addSound(ModSounds.HEY_YA_5_ID, ModSounds.HEY_YA_5_EVENT);
        addSound(ModSounds.HEY_YA_6_ID, ModSounds.HEY_YA_6_EVENT);
        addSound(ModSounds.HEY_YA_7_ID, ModSounds.HEY_YA_7_EVENT);
        addSound(ModSounds.HEY_YA_SUMMON_ID, ModSounds.HEY_YA_SUMMON_EVENT);
        addSound(ModSounds.SURVIVOR_SUMMON_ID, ModSounds.SURVIVOR_SUMMON_EVENT);
        addSound(ModSounds.SURVIVOR_SHOCK_ID, ModSounds.SURVIVOR_SHOCK_EVENT);
        addSound(ModSounds.SURVIVOR_PLACE_ID, ModSounds.SURVIVOR_PLACE_EVENT);
        addSound(ModSounds.SURVIVOR_REMOVE_ID, ModSounds.SURVIVOR_REMOVE_EVENT);
        addSound(ModSounds.ACHTUNG_BURST_ID, ModSounds.ACHTUNG_BURST_EVENT);
        addSound(ModSounds.SUMMON_ACHTUNG_ID, ModSounds.SUMMON_ACHTUNG_EVENT);
        addSound(ModSounds.SUMMON_DIVER_DOWN_ID, ModSounds.SUMMON_DIVER_DOWN_EVENT);
        addSound(ModSounds.SUMMON_WALKING_ID, ModSounds.SUMMON_WALKING_EVENT);
        addSound(ModSounds.HEEL_RAISE_ID, ModSounds.HEEL_RAISE_EVENT);
        addSound(ModSounds.HEEL_STOMP_ID, ModSounds.HEEL_STOMP_EVENT);
        addSound(ModSounds.SPIKE_HIT_ID, ModSounds.SPIKE_HIT_EVENT);
        addSound(ModSounds.SPIKE_MISS_ID, ModSounds.SPIKE_MISS_EVENT);
        addSound(ModSounds.VAMPIRE_DRAIN_ID, ModSounds.VAMPIRE_DRAIN_EVENT);
        addSound(ModSounds.VAMPIRE_CRUMBLE_ID, ModSounds.VAMPIRE_CRUMBLE_EVENT);
        addSound(ModSounds.VAMPIRE_WALL_GRIP_ID, ModSounds.VAMPIRE_WALL_GRIP_EVENT);
        addSound(ModSounds.HEARTBEAT_ID, ModSounds.HEARTBEAT_EVENT);
        addSound(ModSounds.HEARTBEAT2_ID, ModSounds.HEARTBEAT2_EVENT);
        addSound(ModSounds.HEARTBEAT3_ID, ModSounds.HEARTBEAT3_EVENT);
        addSound(ModSounds.VAMPIRE_MESSAGE_ID, ModSounds.VAMPIRE_MESSAGE_EVENT);

        addSound(ModSounds.EXTEND_SPIKES_ID, ModSounds.EXTEND_SPIKES_EVENT);
        addSound(ModSounds.WALL_LATCH_ID, ModSounds.WALL_LATCH_EVENT);
        addSound(ModSounds.STONE_MASK_ACTIVATE_ID, ModSounds.STONE_MASK_ACTIVATE_EVENT);

        addSound(ModSounds.DIO_HOHO_ID, ModSounds.DIO_HOHO_EVENT);
        addSound(ModSounds.DIO_HOHO_2_ID, ModSounds.DIO_HOHO_2_EVENT);
        addSound(ModSounds.DIO_DEATH_ID, ModSounds.DIO_DEATH_EVENT);
        addSound(ModSounds.DIO_DEATH_2_ID, ModSounds.DIO_DEATH_2_EVENT);
        addSound(ModSounds.DIO_KUREI_ID, ModSounds.DIO_KUREI_EVENT);
        addSound(ModSounds.DIO_HURT_1_ID, ModSounds.DIO_HURT_1_EVENT);
        addSound(ModSounds.DIO_HURT_2_ID, ModSounds.DIO_HURT_2_EVENT);
        addSound(ModSounds.DIO_HURT_3_ID, ModSounds.DIO_HURT_3_EVENT);
        addSound(ModSounds.DIO_HURT_4_ID, ModSounds.DIO_HURT_4_EVENT);
        addSound(ModSounds.DIO_SUBERASHI_ID, ModSounds.DIO_SUBERASHI_EVENT);
        addSound(ModSounds.DIO_LAUGH_ID, ModSounds.DIO_LAUGH_EVENT);
        addSound(ModSounds.DIO_APPROACHING_ME_ID, ModSounds.DIO_APPROACHING_ME_EVENT);
        addSound(ModSounds.DIO_WRY_ID, ModSounds.DIO_WRY_EVENT);
        addSound(ModSounds.DIO_SHINE_ID, ModSounds.DIO_SHINE_EVENT);
        addSound(ModSounds.DIO_CHECKMATE_ID, ModSounds.DIO_CHECKMATE_EVENT);
        addSound(ModSounds.DIO_NANI_ID, ModSounds.DIO_NANI_EVENT);
        addSound(ModSounds.DIO_KONO_DIO_ID, ModSounds.DIO_KONO_DIO_EVENT);
        addSound(ModSounds.DIO_NO_WAY_ID, ModSounds.DIO_NO_WAY_EVENT);
        addSound(ModSounds.DIO_ATTACK_ID, ModSounds.DIO_ATTACK_EVENT);
        addSound(ModSounds.DIO_ATTACK_2_ID, ModSounds.DIO_ATTACK_2_EVENT);
        addSound(ModSounds.DIO_THE_WORLD_ID, ModSounds.DIO_THE_WORLD_EVENT);
        addSound(ModSounds.DIO_THE_WORLD_2_ID, ModSounds.DIO_THE_WORLD_2_EVENT);
        addSound(ModSounds.DIO_THE_WORLD_3_ID, ModSounds.DIO_THE_WORLD_3_EVENT);
        addSound(ModSounds.DIO_THE_WORLD_4_ID, ModSounds.DIO_THE_WORLD_4_EVENT);
        addSound(ModSounds.DIO_TAUNT_ID, ModSounds.DIO_TAUNT_EVENT);
        addSound(ModSounds.DIO_INTERESTING_ID, ModSounds.DIO_INTERESTING_EVENT);
        addSound(ModSounds.DIO_JOTARO_ID, ModSounds.DIO_JOTARO_EVENT);
        addSound(ModSounds.DIO_JOTARO_2_ID, ModSounds.DIO_JOTARO_2_EVENT);

        addSound(ModSounds.DIEGO_HO_ID, ModSounds.DIEGO_HO_EVENT);
        addSound(ModSounds.DIEGO_HO_2_ID, ModSounds.DIEGO_HO_2_EVENT);
        addSound(ModSounds.DIEGO_DEATH_ID, ModSounds.DIEGO_DEATH_EVENT);
        addSound(ModSounds.DIEGO_DEATH_2_ID, ModSounds.DIEGO_DEATH_2_EVENT);
        addSound(ModSounds.DIEGO_KUREI_ID, ModSounds.DIEGO_KUREI_EVENT);
        addSound(ModSounds.DIEGO_HURT_1_ID, ModSounds.DIEGO_HURT_1_EVENT);
        addSound(ModSounds.DIEGO_HURT_2_ID, ModSounds.DIEGO_HURT_2_EVENT);
        addSound(ModSounds.DIEGO_HURT_3_ID, ModSounds.DIEGO_HURT_3_EVENT);
        addSound(ModSounds.DIEGO_HURT_4_ID, ModSounds.DIEGO_HURT_4_EVENT);
        addSound(ModSounds.DIEGO_LAUGH_ID, ModSounds.DIEGO_LAUGH_EVENT);
        addSound(ModSounds.DIEGO_WRY_ID, ModSounds.DIEGO_WRY_EVENT);
        addSound(ModSounds.DIEGO_SHINE_ID, ModSounds.DIEGO_SHINE_EVENT);
        addSound(ModSounds.DIEGO_CHECKMATE_ID, ModSounds.DIEGO_CHECKMATE_EVENT);
        addSound(ModSounds.DIEGO_NANI_ID, ModSounds.DIEGO_NANI_EVENT);
        addSound(ModSounds.DIEGO_KONO_DIEGO_ID, ModSounds.DIEGO_KONO_DIEGO_EVENT);
        addSound(ModSounds.DIEGO_NO_WAY_ID, ModSounds.DIEGO_NO_WAY_EVENT);
        addSound(ModSounds.DIEGO_ATTACK_ID, ModSounds.DIEGO_ATTACK_EVENT);
        addSound(ModSounds.DIEGO_ATTACK_2_ID, ModSounds.DIEGO_ATTACK_2_EVENT);
        addSound(ModSounds.DIEGO_THE_WORLD_ID, ModSounds.DIEGO_THE_WORLD_EVENT);
        addSound(ModSounds.DIEGO_THE_WORLD_2_ID, ModSounds.DIEGO_THE_WORLD_2_EVENT);
        addSound(ModSounds.DIEGO_THE_WORLD_3_ID, ModSounds.DIEGO_THE_WORLD_3_EVENT);
        addSound(ModSounds.DIEGO_THE_WORLD_4_ID, ModSounds.DIEGO_THE_WORLD_4_EVENT);
        addSound(ModSounds.DIEGO_TAUNT_ID, ModSounds.DIEGO_TAUNT_EVENT);
        addSound(ModSounds.DIEGO_INTERESTING_ID, ModSounds.DIEGO_INTERESTING_EVENT);

        addSound(ModSounds.JOTARO_GRUNT_ID, ModSounds.JOTARO_GRUNT_EVENT);
        addSound(ModSounds.JOTARO_OI_OI_ID, ModSounds.JOTARO_OI_OI_EVENT);
        addSound(ModSounds.JOTARO_PISSED_OFF_ID, ModSounds.JOTARO_PISSED_OFF_EVENT);
        addSound(ModSounds.JOTARO_JUDGEMENT_ID, ModSounds.JOTARO_JUDGEMENT_EVENT);
        addSound(ModSounds.JOTARO_YARE_YARE_ID, ModSounds.JOTARO_YARE_YARE_EVENT);
        addSound(ModSounds.JOTARO_YARE_YARE_2_ID, ModSounds.JOTARO_YARE_YARE_2_EVENT);
        addSound(ModSounds.JOTARO_YARE_YARE_3_ID, ModSounds.JOTARO_YARE_YARE_3_EVENT);
        addSound(ModSounds.JOTARO_YARE_YARE_4_ID, ModSounds.JOTARO_YARE_YARE_4_EVENT);
        addSound(ModSounds.JOTARO_YARE_YARE_5_ID, ModSounds.JOTARO_YARE_YARE_5_EVENT);
        addSound(ModSounds.JOTARO_DEATH_1_ID, ModSounds.JOTARO_DEATH_1_EVENT);
        addSound(ModSounds.JOTARO_DEATH_2_ID, ModSounds.JOTARO_DEATH_2_EVENT);
        addSound(ModSounds.JOTARO_STAR_PLATINUM_1_ID, ModSounds.JOTARO_STAR_PLATINUM_1_EVENT);
        addSound(ModSounds.JOTARO_STAR_PLATINUM_2_ID, ModSounds.JOTARO_STAR_PLATINUM_2_EVENT);
        addSound(ModSounds.JOTARO_STAR_PLATINUM_3_ID, ModSounds.JOTARO_STAR_PLATINUM_3_EVENT);
        addSound(ModSounds.JOTARO_STAR_PLATINUM_4_ID, ModSounds.JOTARO_STAR_PLATINUM_4_EVENT);
        addSound(ModSounds.JOTARO_HURT_1_ID, ModSounds.JOTARO_HURT_1_EVENT);
        addSound(ModSounds.JOTARO_HURT_2_ID, ModSounds.JOTARO_HURT_2_EVENT);
        addSound(ModSounds.JOTARO_HURT_3_ID, ModSounds.JOTARO_HURT_3_EVENT);
        addSound(ModSounds.JOTARO_HURT_4_ID, ModSounds.JOTARO_HURT_4_EVENT);
        addSound(ModSounds.JOTARO_HURT_5_ID, ModSounds.JOTARO_HURT_5_EVENT);
        addSound(ModSounds.JOTARO_ATTACK_1_ID, ModSounds.JOTARO_ATTACK_1_EVENT);
        addSound(ModSounds.JOTARO_ATTACK_2_ID, ModSounds.JOTARO_ATTACK_2_EVENT);
        addSound(ModSounds.JOTARO_ATTACK_3_ID, ModSounds.JOTARO_ATTACK_3_EVENT);
        addSound(ModSounds.JOTARO_YARO_ID, ModSounds.JOTARO_YARO_EVENT);
        addSound(ModSounds.JOTARO_YAMERO_ID, ModSounds.JOTARO_YAMERO_EVENT);
        addSound(ModSounds.JOTARO_FINISHER_ID, ModSounds.JOTARO_FINISHER_EVENT);
        addSound(ModSounds.JOTARO_DIO_ID, ModSounds.JOTARO_DIO_EVENT);
        addSound(ModSounds.JOTARO_GETING_CLOSER_ID, ModSounds.JOTARO_GETING_CLOSER_EVENT);
        addSound(ModSounds.FEMALE_ZOMBIE_AMBIENT_ID, ModSounds.FEMALE_ZOMBIE_AMBIENT_EVENT);
        addSound(ModSounds.FEMALE_ZOMBIE_HURT_ID, ModSounds.FEMALE_ZOMBIE_HURT_EVENT);
        addSound(ModSounds.FEMALE_ZOMBIE_DEATH_ID, ModSounds.FEMALE_ZOMBIE_DEATH_EVENT);
        addSound(ModSounds.AESTHETICIAN_EXHALE_ID, ModSounds.AESTHETICIAN_EXHALE_EVENT);
        addSound(ModSounds.VAMPIRE_DIVE_ID, ModSounds.VAMPIRE_DIVE_EVENT);

        addSound(ModSounds.TORTURE_DANCE_ID, ModSounds.TORTURE_DANCE_EVENT);
        addSound(ModSounds.HALLELUJAH_ID, ModSounds.HALLELUJAH_EVENT);
        addSound(ModSounds.VSONG_SILENT_REVERIES_ID, ModSounds.VSONG_SILENT_REVERIES_EVENT);
        addSound(ModSounds.VSONG_TWISTED_ID, ModSounds.VSONG_TWISTED_EVENT);
        addSound(ModSounds.VSONG_GOTHIC_ORGAN_ID, ModSounds.VSONG_GOTHIC_ORGAN_EVENT);
        addSound(ModSounds.VSONG_DAMNABLE_CEREMONY_ID, ModSounds.VSONG_DAMNABLE_CEREMONY_EVENT);
        addSound(ModSounds.VSONG_BLOODCURDLING_MOMENTS_ID, ModSounds.VSONG_BLOODCURDLING_MOMENTS_EVENT);

        addSound(ModSounds.FLESH_BUD_ID, ModSounds.FLESH_BUD_EVENT);
        addSound(ModSounds.FLESH_BUD_REMOVAL_ID, ModSounds.FLESH_BUD_REMOVAL_EVENT);
        addSound(ModSounds.HAIR_TOGGLE_ID, ModSounds.HAIR_TOGGLE_EVENT);
        addSound(ModSounds.HAIR_SHARPEN_ID, ModSounds.HAIR_SHARPEN_EVENT);
        addSound(ModSounds.FULL_FREEZE_ID, ModSounds.FULL_FREEZE_EVENT);
        addSound(ModSounds.ICE_BREAKER_ID, ModSounds.ICE_BREAKER_EVENT);
    }
}
