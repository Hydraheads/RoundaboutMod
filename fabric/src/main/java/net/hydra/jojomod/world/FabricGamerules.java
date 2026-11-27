package net.hydra.jojomod.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.hydra.jojomod.event.ModGamerules;
import net.minecraft.world.level.GameRules;

public class FabricGamerules {
    public static void registerGamerules(){
        ModGamerules.SBA_STAND_GRIEFING = GameRuleRegistry.register("sbaStandGriefing",
                GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_STAND_GRIEFING_OBTAINMENT = GameRuleRegistry.register("sbaStandGriefingObtainment",
                GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_MOB_STAND_FIRE_SPREADS = GameRuleRegistry.register("sbaMonsterStandFireSpreads",
                GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));
        ModGamerules.SBA_STAND_LEVELING = GameRuleRegistry.register("sbaStandLeveling",
                GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_KEEP_STANDS_ON_DEATH = GameRuleRegistry.register("sbaKeepStandsOnDeath",
                GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_STAND_REDSTONE_INTERFERENCE = GameRuleRegistry.register("sbaStandRedstoneInterference",
                GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_ALLOW_ENTITY_GRAB = GameRuleRegistry.register("sbaMobGrabbing",
                GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_WORTHY_MOB_SPAWNS = GameRuleRegistry.register("sbaSpawnWorthyMobs",
                GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_STAND_USER_MOB_SPAWNS = GameRuleRegistry.register("sbaSpawnStandUserMobs",
                GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_ROAD_ROLLER_BREAK_ICE = GameRuleRegistry.register("sbaRoadRollerBreakIce",
                GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
        ModGamerules.SBA_LOSE_FATE_ON_DEATH = GameRuleRegistry.register("sbaLoseFateOnDeath",
                GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    }
}
