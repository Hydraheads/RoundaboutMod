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
    public static final RegistryObject<SoundEvent> STAND_ARROW_CHARGE_EVENT =
            register(ModSounds.STAND_ARROW_CHARGE, ModSounds.STAND_ARROW_CHARGE_ID);
    public static final RegistryObject<SoundEvent> STAND_ARROW_USE_EVENT =
            register(ModSounds.STAND_ARROW_USE, ModSounds.STAND_ARROW_USE_ID);
    public static final RegistryObject<SoundEvent> TERRIER_SOUND_EVENT =
            register(ModSounds.TERRIER_SOUND, ModSounds.TERRIER_SOUND_ID);
    public static final RegistryObject<SoundEvent> WORLD_SUMMON_SOUND_EVENT =
            register(ModSounds.WORLD_SUMMON_SOUND, ModSounds.WORLD_SUMMON_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAR_SUMMON_SOUND_EVENT =
            register(ModSounds.STAR_SUMMON_SOUND, ModSounds.STAR_SUMMON_SOUND_ID);
    public static final RegistryObject<SoundEvent> KNIFE_THROW_SOUND_EVENT =
            register(ModSounds.KNIFE_THROW_SOUND, ModSounds.KNIFE_THROW_SOUND_ID);
    public static final RegistryObject<SoundEvent> KNIFE_BUNDLE_THROW_SOUND_EVENT =
            register(ModSounds.KNIFE_BUNDLE_THROW_SOUND, ModSounds.KNIFE_BUNDLE_THROW_SOUND_ID);
    public static final RegistryObject<SoundEvent> KNIFE_IMPACT_EVENT =
            register(ModSounds.KNIFE_IMPACT, ModSounds.KNIFE_IMPACT_ID);
    public static final RegistryObject<SoundEvent> KNIFE_IMPACT_GROUND_EVENT =
            register(ModSounds.KNIFE_IMPACT_GROUND, ModSounds.KNIFE_IMPACT_GROUND_ID);
    public static final RegistryObject<SoundEvent> LOCACACA_PETRIFY_EVENT =
            register(ModSounds.LOCACACA_PETRIFY, ModSounds.LOCACACA_PETRIFY_ID);
    public static final RegistryObject<SoundEvent> LOCACACA_FUSION_EVENT =
            register(ModSounds.LOCACACA_FUSION, ModSounds.LOCACACA_FUSION_ID);
    public static final RegistryObject<SoundEvent> PUNCH_1_SOUND_EVENT =
            register(ModSounds.PUNCH_1_SOUND, ModSounds.PUNCH_1_SOUND_ID);
    public static final RegistryObject<SoundEvent> PUNCH_2_SOUND_EVENT =
            register(ModSounds.PUNCH_2_SOUND, ModSounds.PUNCH_2_SOUND_ID);
    public static final RegistryObject<SoundEvent> PUNCH_3_SOUND_EVENT =
            register(ModSounds.PUNCH_3_SOUND, ModSounds.PUNCH_3_SOUND_ID);
    public static final RegistryObject<SoundEvent> PUNCH_4_SOUND_EVENT =
            register(ModSounds.PUNCH_4_SOUND, ModSounds.PUNCH_4_SOUND_ID);
    public static final RegistryObject<SoundEvent> DODGE_EVENT =
            register(ModSounds.DODGE, ModSounds.DODGE_ID);
    public static final RegistryObject<SoundEvent> STAND_LEAP_EVENT =
            register(ModSounds.STAND_LEAP, ModSounds.STAND_LEAP_ID);
    public static final RegistryObject<SoundEvent> FALL_BRACE_EVENT =
            register(ModSounds.FALL_BRACE, ModSounds.FALL_BRACE_ID);
    public static final RegistryObject<SoundEvent> TIME_SNAP_EVENT =
            register(ModSounds.TIME_SNAP, ModSounds.TIME_SNAP_ID);
    public static final RegistryObject<SoundEvent> THE_WORLD_ASSAULT_EVENT =
            register(ModSounds.THE_WORLD_ASSAULT, ModSounds.THE_WORLD_ASSAULT_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA1_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA1_SOUND, ModSounds.STAND_THEWORLD_MUDA1_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA2_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA2_SOUND, ModSounds.STAND_THEWORLD_MUDA2_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA3_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA3_SOUND, ModSounds.STAND_THEWORLD_MUDA3_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA4_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA4_SOUND, ModSounds.STAND_THEWORLD_MUDA4_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAND_THEWORLD_MUDA5_SOUND_EVENT =
            register(ModSounds.STAND_THEWORLD_MUDA5_SOUND, ModSounds.STAND_THEWORLD_MUDA5_SOUND_ID);
    public static final RegistryObject<SoundEvent> STAR_PLATINUM_ORA_EVENT =
            register(ModSounds.STAR_PLATINUM_ORA, ModSounds.STAR_PLATINUM_ORA_ID);
    public static final RegistryObject<SoundEvent> STAR_PLATINUM_ORA_RUSH_EVENT =
            register(ModSounds.STAR_PLATINUM_ORA_RUSH, ModSounds.STAR_PLATINUM_ORA_RUSH_ID);
    public static final RegistryObject<SoundEvent> STAR_PLATINUM_SCOPE_EVENT =
            register(ModSounds.STAR_PLATINUM_SCOPE, ModSounds.STAR_PLATINUM_SCOPE_ID);
    public static final RegistryObject<SoundEvent> STAR_PLATINUM_ORA_RUSH_2_EVENT =
            register(ModSounds.STAR_PLATINUM_ORA_RUSH_2, ModSounds.STAR_PLATINUM_ORA_RUSH_2_ID);
    public static final RegistryObject<SoundEvent> STAR_PLATINUM_TIMESTOP =
            register(ModSounds.STAR_PLATINUM_TIMESTOP, ModSounds.STAR_PLATINUM_TIMESTOP_ID);
    public static final RegistryObject<SoundEvent> STAR_PLATINUM_TIMESTOP_2 =
            register(ModSounds.STAR_PLATINUM_TIMESTOP_2, ModSounds.STAR_PLATINUM_TIMESTOP_2_ID);
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
    public static final RegistryObject<SoundEvent> TIME_STOP_THE_WORLD2_EVENT =
            register(ModSounds.TIME_STOP_THE_WORLD2, ModSounds.TIME_STOP_THE_WORLD2_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_THE_WORLD3_EVENT =
            register(ModSounds.TIME_STOP_THE_WORLD3, ModSounds.TIME_STOP_THE_WORLD3_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_RESUME_THE_WORLD_EVENT =
            register(ModSounds.TIME_STOP_RESUME_THE_WORLD, ModSounds.TIME_STOP_RESUME_THE_WORLD_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_RESUME_THE_WORLD2_EVENT =
            register(ModSounds.TIME_STOP_RESUME_THE_WORLD2, ModSounds.TIME_STOP_RESUME_THE_WORLD2_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_VOICE_THE_WORLD_EVENT =
            register(ModSounds.TIME_STOP_VOICE_THE_WORLD, ModSounds.TIME_STOP_VOICE_THE_WORLD_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_VOICE_THE_WORLD2_EVENT =
            register(ModSounds.TIME_STOP_VOICE_THE_WORLD2, ModSounds.TIME_STOP_VOICE_THE_WORLD2_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_VOICE_THE_WORLD3_EVENT =
            register(ModSounds.TIME_STOP_VOICE_THE_WORLD3, ModSounds.TIME_STOP_VOICE_THE_WORLD3_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_CHARGE_THE_WORLD_EVENT =
            register(ModSounds.TIME_STOP_CHARGE_THE_WORLD, ModSounds.TIME_STOP_CHARGE_THE_WORLD_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_STAR_PLATINUM_EVENT =
            register(ModSounds.TIME_STOP_STAR_PLATINUM, ModSounds.TIME_STOP_STAR_PLATINUM_ID);
    public static final RegistryObject<SoundEvent> TIME_RESUME_EVENT =
            register(ModSounds.TIME_RESUME, ModSounds.TIME_RESUME_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_IMPACT_EVENT =
            register(ModSounds.TIME_STOP_IMPACT, ModSounds.TIME_STOP_IMPACT_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_IMPACT2_EVENT =
            register(ModSounds.TIME_STOP_IMPACT2, ModSounds.TIME_STOP_IMPACT2_ID);
    public static final RegistryObject<SoundEvent> TIME_STOP_TICKING =
            register(ModSounds.TIME_STOP_TICKING, ModSounds.TIME_STOP_TICKING_ID);
    public static final RegistryObject<SoundEvent> CAN_BOUNCE =
            register(ModSounds.CAN_BOUNCE, ModSounds.CAN_BOUNCE_ID);
    public static final RegistryObject<SoundEvent> CAN_BOUNCE_END =
            register(ModSounds.CAN_BOUNCE_END, ModSounds.CAN_BOUNCE_END_ID);
    public static final RegistryObject<SoundEvent> GASOLINE_EXPLOSION =
            register(ModSounds.GASOLINE_EXPLOSION, ModSounds.GASOLINE_EXPLOSION_ID);
    public static final RegistryObject<SoundEvent> GAS_CAN_THROW =
            register(ModSounds.GAS_CAN_THROW, ModSounds.GAS_CAN_THROW_ID);
    public static final RegistryObject<SoundEvent> MATCH_THROW =
            register(ModSounds.MATCH_THROW, ModSounds.MATCH_THROW_ID);
    public static final RegistryObject<SoundEvent> HARPOON_THROW =
            register(ModSounds.HARPOON_THROW, ModSounds.HARPOON_THROW_ID);
    public static final RegistryObject<SoundEvent> HARPOON_HIT =
            register(ModSounds.HARPOON_HIT, ModSounds.HARPOON_HIT_ID);
    public static final RegistryObject<SoundEvent> HARPOON_CRIT =
            register(ModSounds.HARPOON_CRIT, ModSounds.HARPOON_CRIT_ID);
    public static final RegistryObject<SoundEvent> HARPOON_GROUND =
            register(ModSounds.HARPOON_GROUND, ModSounds.HARPOON_GROUND_ID);
    public static final RegistryObject<SoundEvent> HARPOON_RETURN =
            register(ModSounds.HARPOON_RETURN, ModSounds.HARPOON_RETURN_ID);
    public static final RegistryObject<SoundEvent> GLAIVE_CHARGE =
            register(ModSounds.GLAIVE_CHARGE, ModSounds.GLAIVE_CHARGE_ID);
    public static final RegistryObject<SoundEvent> GLAIVE_ATTACK =
            register(ModSounds.GLAIVE_ATTACK, ModSounds.GLAIVE_ATTACK_ID);
    public static final RegistryObject<SoundEvent> BLOCK_GRAB =
            register(ModSounds.BLOCK_GRAB, ModSounds.BLOCK_GRAB_ID);
    public static final RegistryObject<SoundEvent> BLOCK_THROW =
            register(ModSounds.BLOCK_THROW, ModSounds.BLOCK_THROW_ID);
    public static final RegistryObject<SoundEvent> BALL_BEARING_SHOT =
            register(ModSounds.BALL_BEARING_SHOT, ModSounds.BALL_BEARING_SHOT_ID);
    public static final RegistryObject<SoundEvent> HALLELUJAH =
            register(ModSounds.HALLELUJAH, ModSounds.HALLELUJAH_ID);
    public static final RegistryObject<SoundEvent> TORTURE_DANCE =
            register(ModSounds.TORTURE_DANCE, ModSounds.TORTURE_DANCE_ID);

    public static RegistryObject<SoundEvent> register(String id, ResourceLocation id2){
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(id2));
    }
}
