package net.hydra.jojomod.util;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HeatUtil {
    public static boolean isHot(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat() > 0;
        }
        return false;
    }
    public static boolean isCold(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat() < 0;
        }
        return false;
    }
    public static int getHeat(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat();
        }
        return 0;
    }

    public static boolean isArmsFrozen(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat() <= -33;
        }
        return false;
    }
    public static boolean isLegsFrozen(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat() <= -66;
        }
        return false;
    }
    public static boolean isTotallyFrozen(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat() <= -100;
        }
        return false;
    }

    public static boolean isSweating(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat() >= 33;
        }
        return false;
    }
    public static boolean isCooking(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat() >= 66;
        }
        return false;
    }
    public static boolean isBurning(Entity entity){
        if (entity instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getHeat() >= 100;
        }
        return false;
    }


    public static void addHeat(Entity entity, int amt){
        if (entity instanceof LivingEntity LE){
            StandUser su = ((StandUser)LE);
            int heat = su.roundabout$getHeat();
            heat = Mth.clamp(heat+amt,-110,110);
            su.roundabout$setHeat(heat);
        }
    }

    public static void tickHeat(Entity entity){
        if (entity instanceof LivingEntity LE){
            StandUser su = ((StandUser)LE);
            int heat = su.roundabout$getHeat();
            if (heat < 0){
                if (entity.tickCount%15==0){
                    heat = Mth.clamp(heat+1,-110,110);
                    su.roundabout$setHeat(heat);
                }
            } else if (heat > 0) {
                if (entity.tickCount%15==0){
                    heat = Mth.clamp(heat-1,-110,110);
                    su.roundabout$setHeat(heat);
                }
            }
        }
    }
}
