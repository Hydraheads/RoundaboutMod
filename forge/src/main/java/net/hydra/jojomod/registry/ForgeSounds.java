package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.commands.Commands;
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
    public static final RegistryObject<SoundEvent> INHALE_EVENT =
            register(ModSounds.INHALE, ModSounds.INHALE_ID);
    public static final RegistryObject<SoundEvent> SIGN_HIT_EVENT =
            register(ModSounds.SIGN_HIT, ModSounds.SIGN_HIT_ID);
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
    public static final RegistryObject<SoundEvent> FOG_MORPH_EVENT =
            register(ModSounds.FOG_MORPH, ModSounds.FOG_MORPH_ID);
    public static final RegistryObject<SoundEvent> CACKLE_EVENT =
            register(ModSounds.CACKLE, ModSounds.CACKLE_ID);
    public static final RegistryObject<SoundEvent> AIR_BUBBLE_EVENT =
            register(ModSounds.AIR_BUBBLE, ModSounds.AIR_BUBBLE_ID);
    public static final RegistryObject<SoundEvent> BUBBLE_HOVERED_OVER_EVENT =
            register(ModSounds.BUBBLE_HOVERED_OVER, ModSounds.BUBBLE_HOVERED_OVER_ID);
    public static final RegistryObject<SoundEvent> GO_BEYOND_LAUNCH_EVENT =
            register(ModSounds.GO_BEYOND_LAUNCH, ModSounds.GO_BEYOND_LAUNCH_ID);
    public static final RegistryObject<SoundEvent> GO_BEYOND_HIT_EVENT =
            register(ModSounds.GO_BEYOND_HIT, ModSounds.GO_BEYOND_HIT_ID);
    public static final RegistryObject<SoundEvent> BUBBLE_CREATE_EVENT =
            register(ModSounds.BUBBLE_CREATE, ModSounds.BUBBLE_CREATE_ID);
    public static final RegistryObject<SoundEvent> BIG_BUBBLE_CREATE_EVENT =
            register(ModSounds.BIG_BUBBLE_CREATE, ModSounds.BIG_BUBBLE_CREATE_ID);
    public static final RegistryObject<SoundEvent> BUBBLE_PLUNDER_EVENT =
            register(ModSounds.BUBBLE_PLUNDER, ModSounds.BUBBLE_PLUNDER_ID);
    public static final RegistryObject<SoundEvent> SOFT_AND_WET_BARRAGE_EVENT =
            register(ModSounds.SOFT_AND_WET_BARRAGE, ModSounds.SOFT_AND_WET_BARRAGE_ID);
    public static final RegistryObject<SoundEvent> SOFT_AND_WET_BARRAGE_2_EVENT =
            register(ModSounds.SOFT_AND_WET_BARRAGE_2, ModSounds.SOFT_AND_WET_BARRAGE_2_ID);
    public static final RegistryObject<SoundEvent> WATER_ENCASE_EVENT =
            register(ModSounds.WATER_ENCASE, ModSounds.WATER_ENCASE_ID);
    public static final RegistryObject<SoundEvent> SOFT_AND_WET_KICK_EVENT =
            register(ModSounds.SOFT_AND_WET_KICK, ModSounds.SOFT_AND_WET_KICK_ID);
    public static final RegistryObject<SoundEvent> BUBBLE_POP_EVENT =
            register(ModSounds.BUBBLE_POP, ModSounds.BUBBLE_POP_ID);
    public static final RegistryObject<SoundEvent> EXPLOSIVE_BUBBLE_POP_EVENT =
            register(ModSounds.EXPLOSIVE_BUBBLE_POP, ModSounds.EXPLOSIVE_BUBBLE_POP_ID);
    public static final RegistryObject<SoundEvent> EXPLOSIVE_BUBBLE_SHOT_EVENT =
            register(ModSounds.EXPLOSIVE_BUBBLE_SHOT, ModSounds.EXPLOSIVE_BUBBLE_SHOT_ID);
    public static final RegistryObject<SoundEvent> EXPLOSIVE_BUBBLE_SWITCH_EVENT =
            register(ModSounds.EXPLOSIVE_BUBBLE_SWITCH, ModSounds.EXPLOSIVE_BUBBLE_SWITCH_ID);
    public static final RegistryObject<SoundEvent> EXPLOSIVE_SPIN_MODE_EVENT =
            register(ModSounds.EXPLOSIVE_SPIN_MODE, ModSounds.EXPLOSIVE_SPIN_MODE_ID);
    public static final RegistryObject<SoundEvent> EXPLOSIVE_BUBBLE_SWITCH_OFF_EVENT =
            register(ModSounds.EXPLOSIVE_BUBBLE_SWITCH_OFF, ModSounds.EXPLOSIVE_BUBBLE_SWITCH_OFF_ID);
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
    public static final RegistryObject<SoundEvent> BODY_BAG_EVENT =
            register(ModSounds.BODY_BAG, ModSounds.BODY_BAG_ID);
    public static final RegistryObject<SoundEvent> JUSTICE_SELECT_EVENT =
            register(ModSounds.JUSTICE_SELECT, ModSounds.JUSTICE_SELECT_ID);
    public static final RegistryObject<SoundEvent> JUSTICE_SELECT_ATTACK_EVENT =
            register(ModSounds.JUSTICE_SELECT_ATTACK, ModSounds.JUSTICE_SELECT_ATTACK_ID);
    public static final RegistryObject<SoundEvent> DODGE_EVENT =
            register(ModSounds.DODGE, ModSounds.DODGE_ID);
    public static final RegistryObject<SoundEvent> IMPALE_CHARGE =
            register(ModSounds.IMPALE_CHARGE, ModSounds.IMPALE_CHARGE_ID);
    public static final RegistryObject<SoundEvent> IMPALE_HIT =
            register(ModSounds.IMPALE_HIT, ModSounds.IMPALE_HIT_ID);
    public static final RegistryObject<SoundEvent> STAND_LEAP_EVENT =
            register(ModSounds.STAND_LEAP, ModSounds.STAND_LEAP_ID);
    public static final RegistryObject<SoundEvent> FALL_BRACE_EVENT =
            register(ModSounds.FALL_BRACE, ModSounds.FALL_BRACE_ID);
    public static final RegistryObject<SoundEvent> TIME_SNAP_EVENT =
            register(ModSounds.TIME_SNAP, ModSounds.TIME_SNAP_ID);
    public static final RegistryObject<SoundEvent> THE_WORLD_ASSAULT_EVENT =
            register(ModSounds.THE_WORLD_ASSAULT, ModSounds.THE_WORLD_ASSAULT_ID);
    public static final RegistryObject<SoundEvent> THE_WORLD_WRY_EVENT =
            register(ModSounds.THE_WORLD_WRY, ModSounds.THE_WORLD_WRY_ID);
    public static final RegistryObject<SoundEvent> SUMMON_SOFT_AND_WET_EVENT =
            register(ModSounds.SUMMON_SOFT_AND_WET, ModSounds.SUMMON_SOFT_AND_WET_ID);
    public static final RegistryObject<SoundEvent> THE_WORLD_MUDA_EVENT =
            register(ModSounds.THE_WORLD_MUDA, ModSounds.THE_WORLD_MUDA_ID);
    public static final RegistryObject<SoundEvent> SUMMON_JUSTICE =
            register(ModSounds.SUMMON_JUSTICE, ModSounds.SUMMON_JUSTICE_ID);
    public static final RegistryObject<SoundEvent> SUMMON_JUSTICE_2 =
            register(ModSounds.SUMMON_JUSTICE_2, ModSounds.SUMMON_JUSTICE_2_ID);
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
    public static final RegistryObject<SoundEvent> STAR_FINGER =
            register(ModSounds.STAR_FINGER, ModSounds.STAR_FINGER_ID);
    public static final RegistryObject<SoundEvent> STAR_FINGER_2 =
            register(ModSounds.STAR_FINGER_2, ModSounds.STAR_FINGER_2_ID);
    public static final RegistryObject<SoundEvent> STAR_FINGER_SILENT =
            register(ModSounds.STAR_FINGER_SILENT, ModSounds.STAR_FINGER_SILENT_ID);
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
    public static final RegistryObject<SoundEvent> EXPLOSIVE_PUNCH_EVENT =
            register(ModSounds.EXPLOSIVE_PUNCH, ModSounds.EXPLOSIVE_PUNCH_ID);
    public static final RegistryObject<SoundEvent> FIRE_BLAST_EVENT =
            register(ModSounds.FIRE_BLAST, ModSounds.FIRE_BLAST_ID);
    public static final RegistryObject<SoundEvent> FIRESTORM_EVENT =
            register(ModSounds.FIRESTORM, ModSounds.FIRESTORM_ID);
    public static final RegistryObject<SoundEvent> FIRE_WHOOSH_EVENT =
            register(ModSounds.FIRE_WHOOSH, ModSounds.FIRE_WHOOSH_ID);
    public static final RegistryObject<SoundEvent> CROSSFIRE_SHOOT_EVENT =
            register(ModSounds.CROSSFIRE_SHOOT, ModSounds.CROSSFIRE_SHOOT_ID);
    public static final RegistryObject<SoundEvent> CROSSFIRE_EXPLODE_EVENT =
            register(ModSounds.CROSSFIRE_EXPLODE, ModSounds.CROSSFIRE_EXPLODE_ID);
    public static final RegistryObject<SoundEvent> MAGICIANS_RED_CRY_EVENT =
            register(ModSounds.MAGICIANS_RED_CRY, ModSounds.MAGICIANS_RED_CRY_ID);
    public static final RegistryObject<SoundEvent> MAGICIANS_RED_CRY_2_EVENT =
            register(ModSounds.MAGICIANS_RED_CRY_2, ModSounds.MAGICIANS_RED_CRY_2_ID);
    public static final RegistryObject<SoundEvent> MAGICIANS_RED_CRY_3_EVENT =
            register(ModSounds.MAGICIANS_RED_CRY_3, ModSounds.MAGICIANS_RED_CRY_3_ID);
    public static final RegistryObject<SoundEvent> MAGICIANS_RED_CHARGE_EVENT =
            register(ModSounds.MAGICIANS_RED_CHARGE, ModSounds.MAGICIANS_RED_CHARGE_ID);
    public static final RegistryObject<SoundEvent> FLAMETHROWER_EVENT =
            register(ModSounds.FLAMETHROWER, ModSounds.FLAMETHROWER_ID);
    public static final RegistryObject<SoundEvent> STAND_FLAME_HIT =
            register(ModSounds.STAND_FLAME_HIT, ModSounds.STAND_FLAME_HIT_ID);
    public static final RegistryObject<SoundEvent> STAND_FIREBALL_SHOOT =
            register(ModSounds.FIREBALL_SHOOT, ModSounds.FIREBALL_SHOOT_ID);
    public static final RegistryObject<SoundEvent> STAND_FIREBALL_HIT =
            register(ModSounds.FIREBALL_HIT, ModSounds.FIREBALL_HIT_ID);
    public static final RegistryObject<SoundEvent> LASSO =
            register(ModSounds.LASSO, ModSounds.LASSO_ID);
    public static final RegistryObject<SoundEvent> FIRE_STRIKE_EVENT =
            register(ModSounds.FIRE_STRIKE, ModSounds.FIRE_STRIKE_ID);
    public static final RegistryObject<SoundEvent> FIRE_STRIKE_LAST_EVENT =
            register(ModSounds.FIRE_STRIKE_LAST, ModSounds.FIRE_STRIKE_LAST_ID);
    public static final RegistryObject<SoundEvent> TWAU_BARRAGE_EVENT =
            register(ModSounds.TWAU_BARRAGE, ModSounds.TWAU_BARRAGE_ID);
    public static final RegistryObject<SoundEvent> TWAU_BARRAGE_2_EVENT =
            register(ModSounds.TWAU_BARRAGE_2, ModSounds.TWAU_BARRAGE_2_ID);
    public static final RegistryObject<SoundEvent> TWAU_MUDA_EVENT =
            register(ModSounds.TWAU_MUDA, ModSounds.TWAU_MUDA_ID);
    public static final RegistryObject<SoundEvent> TWAU_MUDA_2_EVENT =
            register(ModSounds.TWAU_MUDA_2, ModSounds.TWAU_MUDA_2_ID);
    public static final RegistryObject<SoundEvent> TWAU_MUDA_3_EVENT =
            register(ModSounds.TWAU_MUDA_3, ModSounds.TWAU_MUDA_3_ID);
    public static final RegistryObject<SoundEvent> TWAU_WRY_EVENT =
            register(ModSounds.TWAU_WRY, ModSounds.TWAU_WRY_ID);
    public static final RegistryObject<SoundEvent> TWAU_THE_WORLD_EVENT =
            register(ModSounds.TWAU_THE_WORLD, ModSounds.TWAU_THE_WORLD_ID);
    public static final RegistryObject<SoundEvent> TWAU_HEY_EVENT =
            register(ModSounds.TWAU_HEY, ModSounds.TWAU_HEY_ID);
    public static final RegistryObject<SoundEvent> TWAU_TIMESTOP_EVENT =
            register(ModSounds.TWAU_TIMESTOP, ModSounds.TWAU_TIMESTOP_ID);
    public static final RegistryObject<SoundEvent> TWAU_TIMESTOP_2_EVENT =
            register(ModSounds.TWAU_TIMESTOP_2, ModSounds.TWAU_TIMESTOP_2_ID);
    public static final RegistryObject<SoundEvent> TWAU_USHA_EVENT =
            register(ModSounds.TWAU_USHA, ModSounds.TWAU_USHA_ID);
    public static final RegistryObject<SoundEvent> DSP_SUMMON_EVENT =
            register(ModSounds.DSP_SUMMON, ModSounds.DSP_SUMMON_ID);
    public static final RegistryObject<SoundEvent> OVA_PLATINUM_BARRAGE_EVENT =
            register(ModSounds.OVA_PLATINUM_BARRAGE, ModSounds.OVA_PLATINUM_BARRAGE_ID);
    public static final RegistryObject<SoundEvent> OVA_PLATINUM_ORA =
            register(ModSounds.OVA_PLATINUM_ORA, ModSounds.OVA_PLATINUM_ORA_ID);
    public static final RegistryObject<SoundEvent> OVA_PLATINUM_ORA_2 =
            register(ModSounds.OVA_PLATINUM_ORA_2, ModSounds.OVA_PLATINUM_ORA_2_ID);
    public static final RegistryObject<SoundEvent> OVA_PLATINUM_ORA_3 =
            register(ModSounds.OVA_PLATINUM_ORA_3, ModSounds.OVA_PLATINUM_ORA_3_ID);
    public static final RegistryObject<SoundEvent> OVA_PLATINUM_ORA_4 =
            register(ModSounds.OVA_PLATINUM_ORA_4, ModSounds.OVA_PLATINUM_ORA_4_ID);
    public static final RegistryObject<SoundEvent> OVA_MUDA_EVENT =
            register(ModSounds.OVA_MUDA, ModSounds.OVA_MUDA_ID);
    public static final RegistryObject<SoundEvent> OVA_MUDA_2_EVENT =
            register(ModSounds.OVA_MUDA_2, ModSounds.OVA_MUDA_2_ID);
    public static final RegistryObject<SoundEvent> OVA_BARRAGE_EVENT =
            register(ModSounds.OVA_BARRAGE, ModSounds.OVA_BARRAGE_ID);
    public static final RegistryObject<SoundEvent> OVA_BARRAGE_2_EVENT =
            register(ModSounds.OVA_BARRAGE_2, ModSounds.OVA_BARRAGE_2_ID);
    public static final RegistryObject<SoundEvent> OVA_THE_WORLD_EVENT =
            register(ModSounds.OVA_THE_WORLD, ModSounds.OVA_THE_WORLD_ID);
    public static final RegistryObject<SoundEvent> OVA_THE_WORLD_2_EVENT =
            register(ModSounds.OVA_THE_WORLD_2, ModSounds.OVA_THE_WORLD_2_ID);
    public static final RegistryObject<SoundEvent> OVA_SHORT_TS_EVENT =
            register(ModSounds.OVA_SHORT_TS, ModSounds.OVA_SHORT_TS_ID);
    public static final RegistryObject<SoundEvent> OVA_LONG_TS_EVENT =
            register(ModSounds.OVA_LONG_TS, ModSounds.OVA_LONG_TS_ID);
    public static final RegistryObject<SoundEvent> OVA_SP_EVENT =
            register(ModSounds.OVA_SP_TS, ModSounds.OVA_SP_TS_ID);
    public static final RegistryObject<SoundEvent> OVA_TIME_RESUME_EVENT =
            register(ModSounds.OVA_TIME_RESUME, ModSounds.OVA_TIME_RESUME_ID);
    public static final RegistryObject<SoundEvent> OVA_SUMMON_EVENT =
            register(ModSounds.OVA_SUMMON, ModSounds.OVA_SUMMON_ID);
    public static final RegistryObject<SoundEvent> SUMMON_MAGICIAN_EVENT =
            register(ModSounds.SUMMON_MAGICIAN, ModSounds.SUMMON_MAGICIAN_ID);
    public static final RegistryObject<SoundEvent> SNAP_EVENT =
            register(ModSounds.SNAP, ModSounds.SNAP_ID);
    public static final RegistryObject<SoundEvent> OVA_SUMMON_THE_WORLD_EVENT =
            register(ModSounds.OVA_SUMMON_THE_WORLD, ModSounds.OVA_SUMMON_THE_WORLD_ID);
    public static final RegistryObject<SoundEvent> LEVELUP_EVENT =
            register(ModSounds.LEVELUP, ModSounds.LEVELUP_ID);
    public static final RegistryObject<SoundEvent> UNLOCK_SKIN_EVENT =
            register(ModSounds.UNLOCK_SKIN, ModSounds.UNLOCK_SKIN_ID);


    public static final RegistryObject<SoundEvent> ARCADE_TIMESTOP_EVENT =
            register(ModSounds.ARCADE_TIMESTOP, ModSounds.ARCADE_TIMESTOP_ID);
    public static final RegistryObject<SoundEvent> ARCADE_TIMESTOP_2_EVENT =
            register(ModSounds.ARCADE_TIMESTOP_2, ModSounds.ARCADE_TIMESTOP_2_ID);
    public static final RegistryObject<SoundEvent> ARCADE_MUDA_EVENT =
            register(ModSounds.ARCADE_MUDA, ModSounds.ARCADE_MUDA_ID);
    public static final RegistryObject<SoundEvent> ARCADE_MUDA_2_EVENT =
            register(ModSounds.ARCADE_MUDA_2, ModSounds.ARCADE_MUDA_2_ID);
    public static final RegistryObject<SoundEvent> ARCADE_MUDA_3_EVENT =
            register(ModSounds.ARCADE_MUDA_3, ModSounds.ARCADE_MUDA_3_ID);
    public static final RegistryObject<SoundEvent> ARCADE_URI_EVENT =
            register(ModSounds.ARCADE_URI, ModSounds.ARCADE_URI_ID);
    public static final RegistryObject<SoundEvent> ARCADE_IMPALE_EVENT =
            register(ModSounds.ARCADE_IMPALE, ModSounds.ARCADE_IMPALE_ID);
    public static final RegistryObject<SoundEvent> ARCADE_TIME_RESUME_EVENT =
            register(ModSounds.ARCADE_TIME_RESUME, ModSounds.ARCADE_TIME_RESUME_ID);
    public static final RegistryObject<SoundEvent> ARCADE_LONG_TS_EVENT =
            register(ModSounds.ARCADE_LONG_TS, ModSounds.ARCADE_LONG_TS_ID);
    public static final RegistryObject<SoundEvent> ARCADE_SHORT_TS_EVENT =
            register(ModSounds.ARCADE_SHORT_TS, ModSounds.ARCADE_SHORT_TS_ID);
    public static final RegistryObject<SoundEvent> ARCADE_ORA_EVENT =
            register(ModSounds.ARCADE_ORA, ModSounds.ARCADE_ORA_ID);
    public static final RegistryObject<SoundEvent> ARCADE_ORA_2_EVENT =
            register(ModSounds.ARCADE_ORA_2, ModSounds.ARCADE_ORA_2_ID);
    public static final RegistryObject<SoundEvent> ARCADE_ORA_3_EVENT =
            register(ModSounds.ARCADE_ORA_3, ModSounds.ARCADE_ORA_3_ID);
    public static final RegistryObject<SoundEvent> ARCADE_STAR_PLATINUM_BARRAGE_EVENT =
            register(ModSounds.ARCADE_STAR_PLATINUM_BARRAGE, ModSounds.ARCADE_STAR_PLATINUM_BARRAGE_ID);
    public static final RegistryObject<SoundEvent> ARCADE_STAR_PLATINUM_TIME_STOP_EVENT =
            register(ModSounds.ARCADE_STAR_PLATINUM_TIME_STOP, ModSounds.ARCADE_STAR_PLATINUM_TIME_STOP_ID);
    public static final RegistryObject<SoundEvent> ARCADE_STAR_PLATINUM_SHORT_TS_EVENT =
            register(ModSounds.ARCADE_STAR_PLATINUM_SHORT_TS, ModSounds.ARCADE_STAR_PLATINUM_SHORT_TS_ID);
    public static final RegistryObject<SoundEvent> ARCADE_STAR_FINGER_EVENT =
            register(ModSounds.ARCADE_STAR_FINGER, ModSounds.ARCADE_STAR_FINGER_ID);
    public static final RegistryObject<SoundEvent> CINDERELLA_SUMMON_EVENT =
            register(ModSounds.CINDERELLA_SUMMON, ModSounds.CINDERELLA_SUMMON_ID);
    public static final RegistryObject<SoundEvent> CINDERELLA_ATTACK_EVENT =
            register(ModSounds.CINDERELLA_ATTACK, ModSounds.CINDERELLA_ATTACK_ID);
    public static final RegistryObject<SoundEvent> CINDERELLA_SPARKLE_EVENT =
            register(ModSounds.CINDERELLA_SPARKLE, ModSounds.CINDERELLA_SPARKLE_ID);
    public static final RegistryObject<SoundEvent> CINDERELLA_FAIL_EVENT =
            register(ModSounds.CINDERELLA_FAIL, ModSounds.CINDERELLA_FAIL_ID);
    public static final RegistryObject<SoundEvent> CINDERELLA_VISAGE_CREATION_EVENT =
            register(ModSounds.CINDERELLA_VISAGE_CREATION, ModSounds.CINDERELLA_VISAGE_CREATION_ID);


    public static final RegistryObject<SoundEvent> THE_WORLD_OVER_HEAVEN_EVENT =
            register(ModSounds.THE_WORLD_OVER_HEAVEN, ModSounds.THE_WORLD_OVER_HEAVEN_ID);
    public static final RegistryObject<SoundEvent> EXPLOSIVE_BAT_EVENT =
            register(ModSounds.EXPLOSIVE_BAT, ModSounds.EXPLOSIVE_BAT_ID);
    public static final RegistryObject<SoundEvent> FINAL_KICK_EVENT =
            register(ModSounds.FINAL_KICK, ModSounds.FINAL_KICK_ID);
    public static final RegistryObject<SoundEvent> DREAD_SUMMON_EVENT =
            register(ModSounds.DREAD_SUMMON, ModSounds.DREAD_SUMMON_ID);
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
    public static final RegistryObject<SoundEvent> FOG_CLONE =
            register(ModSounds.FOG_CLONE, ModSounds.FOG_CLONE_ID);
    public static final RegistryObject<SoundEvent> POP =
            register(ModSounds.POP, ModSounds.POP_ID);
    public static final RegistryObject<SoundEvent> BLOCK_GRAB =
            register(ModSounds.BLOCK_GRAB, ModSounds.BLOCK_GRAB_ID);
    public static final RegistryObject<SoundEvent> BLOCK_THROW =
            register(ModSounds.BLOCK_THROW, ModSounds.BLOCK_THROW_ID);
    public static final RegistryObject<SoundEvent> ITEM_CATCH =
            register(ModSounds.ITEM_CATCH, ModSounds.ITEM_CATCH_ID);
    public static final RegistryObject<SoundEvent> BALL_BEARING_SHOT =
            register(ModSounds.BALL_BEARING_SHOT, ModSounds.BALL_BEARING_SHOT_ID);
    public static final RegistryObject<SoundEvent> DIO_HOHO =
            register(ModSounds.DIO_HOHO, ModSounds.DIO_HOHO_ID);
    public static final RegistryObject<SoundEvent> DIO_DEATH =
            register(ModSounds.DIO_DEATH, ModSounds.DIO_DEATH_ID);
    public static final RegistryObject<SoundEvent> DIO_HOHO_2 =
            register(ModSounds.DIO_HOHO_2, ModSounds.DIO_HOHO_2_ID);
    public static final RegistryObject<SoundEvent> DIO_DEATH_2 =
            register(ModSounds.DIO_DEATH_2, ModSounds.DIO_DEATH_2_ID);
    public static final RegistryObject<SoundEvent> DIO_KUREI =
            register(ModSounds.DIO_KUREI, ModSounds.DIO_KUREI_ID);
    public static final RegistryObject<SoundEvent> DIO_HURT_1 =
            register(ModSounds.DIO_HURT_1, ModSounds.DIO_HURT_1_ID);
    public static final RegistryObject<SoundEvent> DIO_HURT_2 =
            register(ModSounds.DIO_HURT_2, ModSounds.DIO_HURT_2_ID);
    public static final RegistryObject<SoundEvent> DIO_HURT_3 =
            register(ModSounds.DIO_HURT_3, ModSounds.DIO_HURT_3_ID);
    public static final RegistryObject<SoundEvent> DIO_HURT_4 =
            register(ModSounds.DIO_HURT_4, ModSounds.DIO_HURT_4_ID);
    public static final RegistryObject<SoundEvent> DIO_LAUGH =
            register(ModSounds.DIO_LAUGH, ModSounds.DIO_LAUGH_ID);
    public static final RegistryObject<SoundEvent> DIO_SUBERASHI =
            register(ModSounds.DIO_SUBERASHI, ModSounds.DIO_SUBERASHI_ID);
    public static final RegistryObject<SoundEvent> DIO_APPROACHING_ME =
            register(ModSounds.DIO_APPROACHING_ME, ModSounds.DIO_APPROACHING_ME_ID);
    public static final RegistryObject<SoundEvent> DIO_WRY =
            register(ModSounds.DIO_WRY, ModSounds.DIO_WRY_ID);
    public static final RegistryObject<SoundEvent> DIO_SHINE =
            register(ModSounds.DIO_SHINE, ModSounds.DIO_SHINE_ID);
    public static final RegistryObject<SoundEvent> DIO_CHECKMATE =
            register(ModSounds.DIO_CHECKMATE, ModSounds.DIO_CHECKMATE_ID);
    public static final RegistryObject<SoundEvent> DIO_NANI =
            register(ModSounds.DIO_NANI, ModSounds.DIO_NANI_ID);
    public static final RegistryObject<SoundEvent> DIO_KONO_DIO =
            register(ModSounds.DIO_KONO_DIO, ModSounds.DIO_KONO_DIO_ID);
    public static final RegistryObject<SoundEvent> DIO_NO_WAY =
            register(ModSounds.DIO_NO_WAY, ModSounds.DIO_NO_WAY_ID);
    public static final RegistryObject<SoundEvent> DIO_ATTACK =
            register(ModSounds.DIO_ATTACK, ModSounds.DIO_ATTACK_ID);
    public static final RegistryObject<SoundEvent> DIO_ATTACK_2 =
            register(ModSounds.DIO_ATTACK_2, ModSounds.DIO_ATTACK_2_ID);
    public static final RegistryObject<SoundEvent> DIO_THE_WORLD =
            register(ModSounds.DIO_THE_WORLD, ModSounds.DIO_THE_WORLD_ID);
    public static final RegistryObject<SoundEvent> DIO_THE_WORLD_2 =
            register(ModSounds.DIO_THE_WORLD_2, ModSounds.DIO_THE_WORLD_2_ID);
    public static final RegistryObject<SoundEvent> DIO_THE_WORLD_3 =
            register(ModSounds.DIO_THE_WORLD_3, ModSounds.DIO_THE_WORLD_3_ID);
    public static final RegistryObject<SoundEvent> DIO_THE_WORLD_4 =
            register(ModSounds.DIO_THE_WORLD_4, ModSounds.DIO_THE_WORLD_4_ID);
    public static final RegistryObject<SoundEvent> DIO_TAUNT =
            register(ModSounds.DIO_TAUNT, ModSounds.DIO_TAUNT_ID);
    public static final RegistryObject<SoundEvent> DIO_INTERESTING =
            register(ModSounds.DIO_INTERESTING, ModSounds.DIO_INTERESTING_ID);
    public static final RegistryObject<SoundEvent> DIO_JOTARO =
            register(ModSounds.DIO_JOTARO, ModSounds.DIO_JOTARO_ID);
    public static final RegistryObject<SoundEvent> DIO_JOTARO_2 =
            register(ModSounds.DIO_JOTARO_2, ModSounds.DIO_JOTARO_2_ID);
    public static final RegistryObject<SoundEvent> JOTARO_HURT_1 =
            register(ModSounds.JOTARO_HURT_1, ModSounds.JOTARO_HURT_1_ID);
    public static final RegistryObject<SoundEvent> JOTARO_HURT_2 =
            register(ModSounds.JOTARO_HURT_2, ModSounds.JOTARO_HURT_2_ID);
    public static final RegistryObject<SoundEvent> JOTARO_HURT_3 =
            register(ModSounds.JOTARO_HURT_3, ModSounds.JOTARO_HURT_3_ID);
    public static final RegistryObject<SoundEvent> JOTARO_HURT_4 =
            register(ModSounds.JOTARO_HURT_4, ModSounds.JOTARO_HURT_4_ID);
    public static final RegistryObject<SoundEvent> JOTARO_HURT_5 =
            register(ModSounds.JOTARO_HURT_5, ModSounds.JOTARO_HURT_5_ID);
    public static final RegistryObject<SoundEvent> JOTARO_DEATH_1 =
            register(ModSounds.JOTARO_DEATH_1, ModSounds.JOTARO_DEATH_1_ID);
    public static final RegistryObject<SoundEvent> JOTARO_DEATH_2 =
            register(ModSounds.JOTARO_DEATH_2, ModSounds.JOTARO_DEATH_2_ID);
    public static final RegistryObject<SoundEvent> JOTARO_GRUNT =
            register(ModSounds.JOTARO_GRUNT, ModSounds.JOTARO_GRUNT_ID);
    public static final RegistryObject<SoundEvent> JOTARO_OI_OI =
            register(ModSounds.JOTARO_OI_OI, ModSounds.JOTARO_OI_OI_ID);
    public static final RegistryObject<SoundEvent> JOTARO_STAR_PLATINUM_1 =
            register(ModSounds.JOTARO_STAR_PLATINUM_1, ModSounds.JOTARO_STAR_PLATINUM_1_ID);
    public static final RegistryObject<SoundEvent> JOTARO_STAR_PLATINUM_2 =
            register(ModSounds.JOTARO_STAR_PLATINUM_2, ModSounds.JOTARO_STAR_PLATINUM_2_ID);
    public static final RegistryObject<SoundEvent> JOTARO_STAR_PLATINUM_3 =
            register(ModSounds.JOTARO_STAR_PLATINUM_3, ModSounds.JOTARO_STAR_PLATINUM_3_ID);
    public static final RegistryObject<SoundEvent> JOTARO_STAR_PLATINUM_4 =
            register(ModSounds.JOTARO_STAR_PLATINUM_4, ModSounds.JOTARO_STAR_PLATINUM_4_ID);
    public static final RegistryObject<SoundEvent> JOTARO_PISSED_OFF =
            register(ModSounds.JOTARO_PISSED_OFF, ModSounds.JOTARO_PISSED_OFF_ID);
    public static final RegistryObject<SoundEvent> JOTARO_JUDGEMENT =
            register(ModSounds.JOTARO_JUDGEMENT, ModSounds.JOTARO_JUDGEMENT_ID);
    public static final RegistryObject<SoundEvent> JOTARO_YARE_YARE =
            register(ModSounds.JOTARO_YARE_YARE, ModSounds.JOTARO_YARE_YARE_ID);
    public static final RegistryObject<SoundEvent> JOTARO_YARE_YARE_2 =
            register(ModSounds.JOTARO_YARE_YARE_2, ModSounds.JOTARO_YARE_YARE_2_ID);
    public static final RegistryObject<SoundEvent> JOTARO_YARE_YARE_3 =
            register(ModSounds.JOTARO_YARE_YARE_3, ModSounds.JOTARO_YARE_YARE_3_ID);
    public static final RegistryObject<SoundEvent> JOTARO_YARE_YARE_4 =
            register(ModSounds.JOTARO_YARE_YARE_4, ModSounds.JOTARO_YARE_YARE_4_ID);
    public static final RegistryObject<SoundEvent> JOTARO_YARE_YARE_5 =
            register(ModSounds.JOTARO_YARE_YARE_5, ModSounds.JOTARO_YARE_YARE_5_ID);
    public static final RegistryObject<SoundEvent> JOTARO_FINISHER =
            register(ModSounds.JOTARO_FINISHER, ModSounds.JOTARO_FINISHER_ID);
    public static final RegistryObject<SoundEvent> JOTARO_YAMERO =
            register(ModSounds.JOTARO_YAMERO, ModSounds.JOTARO_YAMERO_ID);
    public static final RegistryObject<SoundEvent> JOTARO_YARO =
            register(ModSounds.JOTARO_YARO, ModSounds.JOTARO_YARO_ID);
    public static final RegistryObject<SoundEvent> JOTARO_ATTACK_1 =
            register(ModSounds.JOTARO_ATTACK_1, ModSounds.JOTARO_ATTACK_1_ID);
    public static final RegistryObject<SoundEvent> JOTARO_ATTACK_2 =
            register(ModSounds.JOTARO_ATTACK_2, ModSounds.JOTARO_ATTACK_2_ID);
    public static final RegistryObject<SoundEvent> JOTARO_ATTACK_3 =
            register(ModSounds.JOTARO_ATTACK_3, ModSounds.JOTARO_ATTACK_3_ID);
    public static final RegistryObject<SoundEvent> JOTARO_DIO =
            register(ModSounds.JOTARO_DIO, ModSounds.JOTARO_DIO_ID);
    public static final RegistryObject<SoundEvent> JOTARO_GETTING_CLOSER =
            register(ModSounds.JOTARO_GETING_CLOSER, ModSounds.JOTARO_GETING_CLOSER_ID);
    public static final RegistryObject<SoundEvent> FEMALE_ZOMBIE_HURT =
            register(ModSounds.FEMALE_ZOMBIE_HURT, ModSounds.FEMALE_ZOMBIE_HURT_ID);
    public static final RegistryObject<SoundEvent> FEMALE_ZOMBIE_DEATH =
            register(ModSounds.FEMALE_ZOMBIE_DEATH, ModSounds.FEMALE_ZOMBIE_DEATH_ID);
    public static final RegistryObject<SoundEvent> FEMALE_ZOMBIE_AMBIENT =
            register(ModSounds.FEMALE_ZOMBIE_AMBIENT, ModSounds.FEMALE_ZOMBIE_AMBIENT_ID);
    public static final RegistryObject<SoundEvent> AESTHETICIAN_EXHALE =
            register(ModSounds.AESTHETICIAN_EXHALE, ModSounds.AESTHETICIAN_EXHALE_ID);
    public static final RegistryObject<SoundEvent> HALLELUJAH =
            register(ModSounds.HALLELUJAH, ModSounds.HALLELUJAH_ID);
    public static final RegistryObject<SoundEvent> TORTURE_DANCE =
            register(ModSounds.TORTURE_DANCE, ModSounds.TORTURE_DANCE_ID);

    public static RegistryObject<SoundEvent> register(String id, ResourceLocation id2){
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(id2));
    }
}
