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

        addSound(ModSounds.KNIFE_THROW_SOUND_ID, ModSounds.KNIFE_THROW_SOUND_EVENT);
        addSound(ModSounds.KNIFE_BUNDLE_THROW_SOUND_ID, ModSounds.KNIFE_BUNDLE_THROW_SOUND_EVENT);
        addSound(ModSounds.KNIFE_IMPACT_ID, ModSounds.KNIFE_IMPACT_EVENT);
        addSound(ModSounds.KNIFE_IMPACT_GROUND_ID, ModSounds.KNIFE_IMPACT_GROUND_EVENT);

        addSound(ModSounds.LOCACACA_PETRIFY_ID, ModSounds.LOCACACA_PETRIFY_EVENT);
        addSound(ModSounds.LOCACACA_FUSION_ID, ModSounds.LOCACACA_FUSION_EVENT);

        addSound(ModSounds.DODGE_ID, ModSounds.DODGE_EVENT);
        addSound(ModSounds.STAND_LEAP_ID, ModSounds.STAND_LEAP_EVENT);
        addSound(ModSounds.FALL_BRACE_ID, ModSounds.FALL_BRACE_EVENT);

        addSound(ModSounds.TIME_SNAP_ID, ModSounds.TIME_SNAP_EVENT);

        addSound(ModSounds.IMPALE_CHARGE_ID, ModSounds.IMPALE_CHARGE_EVENT);
        addSound(ModSounds.IMPALE_HIT_ID, ModSounds.IMPALE_HIT_EVENT);

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
        addSound(ModSounds.EXPLOSIVE_BAT_ID, ModSounds.EXPLOSIVE_BAT_EVENT);
        addSound(ModSounds.OVA_SUMMON_ID, ModSounds.OVA_SUMMON_EVENT);
        addSound(ModSounds.FINAL_KICK_ID, ModSounds.FINAL_KICK_EVENT);

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

        addSound(ModSounds.BLOCK_GRAB_ID, ModSounds.BLOCK_GRAB_EVENT);
        addSound(ModSounds.BLOCK_THROW_ID, ModSounds.BLOCK_THROW_EVENT);
        addSound(ModSounds.BALL_BEARING_SHOT_ID, ModSounds.BALL_BEARING_SHOT_EVENT);
        addSound(ModSounds.ITEM_CATCH_ID, ModSounds.ITEM_CATCH_EVENT);

        addSound(ModSounds.TORTURE_DANCE_ID, ModSounds.TORTURE_DANCE_EVENT);
        addSound(ModSounds.HALLELUJAH_ID, ModSounds.HALLELUJAH_EVENT);
    }
}
