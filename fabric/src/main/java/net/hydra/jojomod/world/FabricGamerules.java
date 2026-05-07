package net.hydra.jojomod.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.hydra.jojomod.event.ModGamerules;
import net.minecraft.world.level.GameRules;

public class FabricGamerules {
    public static void registerGamerules(){
        ModGamerules.ROUNDABOUT_STAND_GRIEFING = GameRuleRegistry.register("roundaboutStandGriefing",
                GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
        ModGamerules.ROUNDABOUT_MOB_STAND_FIRE_SPREADS = GameRuleRegistry.register("roundaboutMonsterStandFireSpreads",
                GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));
        ModGamerules.ROUNDABOUT_STAND_LEVELING = GameRuleRegistry.register("roundaboutStandLeveling",
                GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
        ModGamerules.ROUNDABOUT_KEEP_STANDS_ON_DEATH = GameRuleRegistry.register("roundaboutKeepStandsOnDeath",
                GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
        ModGamerules.ROUNDABOUT_STAND_REDSTONE_INTERFERENCE = GameRuleRegistry.register("roundaboutStandRedstoneInterference",
                GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
        ModGamerules.ROUNDABOUT_ALLOW_ENTITY_GRAB = GameRuleRegistry.register("roundaboutMobGrabbing",
                GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
        ModGamerules.ROUNDABOUT_WORTHY_MOB_SPAWNS = GameRuleRegistry.register("roundaboutSpawnWorthyMobs",
                GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
        ModGamerules.ROUNDABOUT_STAND_USER_MOB_SPAWNS = GameRuleRegistry.register("roundaboutSpawnStandUserMobs",
                GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
        ModGamerules.ROUNDABOUT_ROAD_ROLLER_BREAK_ICE = GameRuleRegistry.register("roundaboutRoadRollerBreakIce",
                GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
        ModGamerules.ROUNDABOUT_LOSE_FATE_ON_DEATH = GameRuleRegistry.register("roundaboutLoseFateOnDeath",
                GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    }
}
