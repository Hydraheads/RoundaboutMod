package net.hydra.jojomod.registry;

import net.hydra.jojomod.event.ModGamerules;
import net.minecraft.world.level.GameRules;

public class ForgeGamerules {
    public static void registerGamerules(){
        ModGamerules.SBA_STAND_GRIEFING = GameRules.register("sbaStandGriefing",
                GameRules.Category.MISC, GameRules.BooleanValue.create(true));
        ModGamerules.SBA_STAND_GRIEFING_OBTAINMENT = GameRules.register("sbaStandGriefingObtainment",
                GameRules.Category.MISC, GameRules.BooleanValue.create(true));
        ModGamerules.SBA_STAND_LEVELING = GameRules.register("sbaStandLeveling",
                GameRules.Category.MISC, GameRules.BooleanValue.create(true));
        ModGamerules.SBA_MOB_STAND_FIRE_SPREADS = GameRules.register("sbaMonsterStandFireSpreads",
                GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
        ModGamerules.SBA_KEEP_STANDS_ON_DEATH = GameRules.register("sbaKeepStandsOnDeath",
                GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        ModGamerules.SBA_STAND_REDSTONE_INTERFERENCE = GameRules.register("sbaStandRedstoneInterference",
                GameRules.Category.MISC, GameRules.BooleanValue.create(true));
        ModGamerules.SBA_ALLOW_ENTITY_GRAB = GameRules.register("sbaMobGrabbing",
                GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        ModGamerules.SBA_WORTHY_MOB_SPAWNS = GameRules.register("sbaSpawnWorthyMobs",
                GameRules.Category.MOBS, GameRules.BooleanValue.create(true));
        ModGamerules.SBA_STAND_USER_MOB_SPAWNS = GameRules.register("sbaSpawnStandUserMobs",
                GameRules.Category.MOBS,GameRules.BooleanValue.create(true));
        ModGamerules.SBA_ROAD_ROLLER_BREAK_ICE = GameRules.register("sbaRoadRollerBreakIce",
                GameRules.Category.MOBS,GameRules.BooleanValue.create(true));
        ModGamerules.SBA_LOSE_FATE_ON_DEATH = GameRules.register("sbaLoseFateOnDeath",
                GameRules.Category.PLAYER,GameRules.BooleanValue.create(false));
    }
}
