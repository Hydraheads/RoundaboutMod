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

        addSound(ModSounds.FOG_MORPH_ID, ModSounds.FOG_MORPH_EVENT);
        addSound(ModSounds.CACKLE_ID, ModSounds.CACKLE_EVENT);
        addSound(ModSounds.BUBBLE_HOVERED_OVER_ID, ModSounds.BUBBLE_HOVERED_OVER_EVENT);
        addSound(ModSounds.BUBBLE_POP_ID, ModSounds.BUBBLE_POP_EVENT);
        addSound(ModSounds.BUBBLE_CREATE_ID, ModSounds.BUBBLE_CREATE_EVENT);

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

        addSound(ModSounds.GLAIVE_CHARGE_ID, ModSounds.GLAIVE_CHARGE_EVENT);
        addSound(ModSounds.GLAIVE_ATTACK_ID, ModSounds.GLAIVE_ATTACK_EVENT);
        addSound(ModSounds.FOG_CLONE_ID, ModSounds.FOG_CLONE_EVENT);
        addSound(ModSounds.POP_ID, ModSounds.POP_EVENT);

        addSound(ModSounds.BLOCK_GRAB_ID, ModSounds.BLOCK_GRAB_EVENT);
        addSound(ModSounds.BLOCK_THROW_ID, ModSounds.BLOCK_THROW_EVENT);
        addSound(ModSounds.BALL_BEARING_SHOT_ID, ModSounds.BALL_BEARING_SHOT_EVENT);
        addSound(ModSounds.ITEM_CATCH_ID, ModSounds.ITEM_CATCH_EVENT);

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

        addSound(ModSounds.TORTURE_DANCE_ID, ModSounds.TORTURE_DANCE_EVENT);
        addSound(ModSounds.HALLELUJAH_ID, ModSounds.HALLELUJAH_EVENT);
    }
}
