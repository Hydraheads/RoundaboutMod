package net.hydra.jojomod.registry;

import net.hydra.jojomod.event.ModGamerules;
import net.minecraft.world.level.GameRules;

public class ForgeGamerules {
    public static void registerGamerules(){
        ModGamerules.ROUNDABOUT_STAND_GRIEFING = GameRules.register("roundaboutStandGriefing",
                GameRules.Category.MISC, GameRules.BooleanValue.create(true));
        ModGamerules.ROUNDABOUT_STAND_LEVELING = GameRules.register("roundaboutStandLeveling",
                GameRules.Category.MISC, GameRules.BooleanValue.create(true));
        ModGamerules.ROUNDABOUT_MOB_STAND_FIRE_SPREADS = GameRules.register("roundaboutMonsterStandFireSpreads",
                GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
        ModGamerules.ROUNDABOUT_KEEP_STANDS_ON_DEATH = GameRules.register("roundaboutKeepStandsOnDeath",
                GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        ModGamerules.ROUNDABOUT_STAND_REDSTONE_INTERFERENCE = GameRules.register("roundaboutStandRedstoneInterference",
                GameRules.Category.MISC, GameRules.BooleanValue.create(true));
        ModGamerules.ROUNDABOUT_ALLOW_ENTITY_GRAB = GameRules.register("roundaboutMobGrabbing",
                GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        ModGamerules.ROUNDABOUT_WORTHY_MOB_SPAWNS = GameRules.register("roundaboutSpawnWorthyMobs",
                GameRules.Category.MOBS, GameRules.BooleanValue.create(true));
        ModGamerules.ROUNDABOUT_STAND_USER_MOB_SPAWNS = GameRules.register("roundaboutSpawnStandUserMobs",
                GameRules.Category.MOBS,GameRules.BooleanValue.create(true));
        ModGamerules.ROUNDABOUT_ROAD_ROLLER_BREAK_ICE = GameRules.register("roundaboutRoadRollerBreakIce",
                GameRules.Category.MOBS,GameRules.BooleanValue.create(true));
    }
}
