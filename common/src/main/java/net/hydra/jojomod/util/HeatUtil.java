package net.hydra.jojomod.util;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
    public static boolean isBodyFrozen(Entity entity){
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
    public static void resetHeat(Entity entity) {
        if (entity instanceof LivingEntity LE) {
            StandUser su = ((StandUser) LE);
            su.roundabout$setHeat(0);
        }
    }
    public static void setHeat(Entity entity, int heat) {
        if (entity instanceof LivingEntity LE) {
            StandUser su = ((StandUser) LE);
            su.roundabout$setHeat(Mth.clamp(heat,-110,110));
        }
    }

    public static void addHeat(Entity entity, int amt){
        if (entity instanceof LivingEntity LE){
            StandUser su = ((StandUser)LE);
            int heat = su.roundabout$getHeat();
            int prevHeat = heat;
            heat = Mth.clamp(heat+amt,-110,110);
            if (heat <= -100 && amt < 0){
                heat = -110;
                if (prevHeat > -100){
                    entity.level().playSound(
                            null,
                            entity.blockPosition(),
                            ModSounds.FULL_FREEZE_EVENT,
                            SoundSource.PLAYERS,
                            1.0F,
                            (float) ( 1.0F+Math.random()*0.01F));
                }
            }
            if (heat >= 100 && amt > 0){
                heat = 110;
            }
            su.roundabout$setHeat(heat);
        }
    }

    public static void tickHeat(Entity entity){
        if (entity instanceof LivingEntity LE){
            if (LE instanceof Player PE && PE.isCreative()){
                if (getHeat(PE) != 0) {
                    resetHeat(PE);
                }
                return;
            }
            StandUser su = ((StandUser)LE);
            int heat = su.roundabout$getHeat();
            if (heat < 0){
                if (entity.tickCount%10==0 || entity.isOnFire() || su.roundabout$isOnStandFire()){
                    int sub = 1;
                    heat = Mth.clamp(heat+sub,-110,0);
                    su.roundabout$setHeat(heat);
                } if (heat <= -100){
                    AbilityScapeBasis.setDazed(LE,(byte)3);
                    LE.hurtMarked = true;
                    LE.hasImpulse = true;
                    LE.setDeltaMovement(RotationUtil.vecPlayerToWorld(new Vec3(0,-0.4,0),
                            ((IGravityEntity)entity).roundabout$getGravityDirection()));
                }
            } else if (heat > 0) {
                if (entity.tickCount%10==0){
                    heat = Mth.clamp(heat-1,0,110);
                    su.roundabout$setHeat(heat);
                }
            }
        }
    }

    public static float getSlowdown(Entity entity){
        if (entity instanceof LivingEntity LE){
            if (entity instanceof  Player) {
                int heat = Mth.clamp(Math.abs(getHeat(entity)), 0, 100);
                float maxSlowdown = 0.17f;
                return (maxSlowdown * 0.01f) * heat;
            } else {
                int heat = Mth.clamp(Math.abs(getHeat(entity)), 0, 100);
                float maxSlowdown = 0.4f;
                return (maxSlowdown * 0.01f) * heat;
            }
        }
        return 0;
    }
}
