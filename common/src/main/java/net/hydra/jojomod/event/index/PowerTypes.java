package net.hydra.jojomod.event.index;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.powers.power_types.StandGeneralPowers;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public enum PowerTypes {
    NONE(new GeneralPowers()),
    STAND(new StandGeneralPowers()),
    VAMPIRE(new VampireGeneralPowers()),
    HAMON(new GeneralPowers()),
    SPIN(new GeneralPowers());

    public final GeneralPowers generalPowers;

    public static PowerTypes getPowerFromByte(byte bt){
        if (bt == VAMPIRE.ordinal())
            return VAMPIRE;
        if (bt == HAMON.ordinal())
            return HAMON;
        if (bt == SPIN.ordinal())
            return SPIN;
        if (bt == STAND.ordinal())
            return STAND;
        return NONE;
    }

    private PowerTypes(GeneralPowers $$1) {
        this.generalPowers = $$1;
    }

    public static byte getPowerType(Entity ent){
        if (ent instanceof Player pl){
            return ((IPlayerEntity)pl).roundabout$getPower();
        }
        return 0;
    }
    public static void setPowerType(Entity ent, byte type){
        if (ent instanceof Player pl){
            ((IPlayerEntity)pl).roundabout$setPower(type);
        }
    }
    public static void setPowerTypeWithPenalty(Entity ent, byte type){
        if (ent instanceof Player pl){
            ((IPlayerEntity)pl).roundabout$setPowerWithPenalty(type);
        }
    }


    public static void initializeStandPower(Entity ent){
        if (ent instanceof Player pl){
            if (getPowerType(pl) == NONE.ordinal())
                ((IPlayerEntity)pl).roundabout$setPower((byte) STAND.ordinal());
        }
    }
    public static void forceInitializeStandPower(Entity ent){
        if (ent instanceof Player pl){
            if (getPowerType(pl) == NONE.ordinal())
                ((IPlayerEntity)pl).roundabout$setPower((byte) STAND.ordinal());
        }
    }

    public static boolean canKeepGuardPos(Entity ent){
        if (ent instanceof Player pl){
            if (isUsingPower(ent)){
                return ((IPowersPlayer)pl).rdbt$getPowers().activePower == PowerIndex.GUARD;
            }  else if (isUsingStand(ent)){
                return ((StandUser)pl).roundabout$getStandPowers().activePower  == PowerIndex.GUARD;
            }
        }
        return false;
    }
    public static boolean canKeepBarrageChargePos(Entity ent){
        if (ent instanceof Player pl){
            if (isUsingPower(ent)){
                return ((IPowersPlayer)pl).rdbt$getPowers().activePower == PowerIndex.BARRAGE_CHARGE;
            }  else if (isUsingStand(ent)){
                return ((StandUser)pl).roundabout$getStandPowers().activePower  == PowerIndex.BARRAGE_CHARGE;
            }
        }
        return false;
    }
    public static boolean canKeepBarragePos(Entity ent){
        if (ent instanceof Player pl){
            if (isUsingPower(ent)){
                return ((IPowersPlayer)pl).rdbt$getPowers().activePower == PowerIndex.BARRAGE;
            }  else if (isUsingStand(ent)){
                return ((StandUser)pl).roundabout$getStandPowers().activePower  == PowerIndex.BARRAGE;
            }
        }
        return false;
    }
    public static boolean canChargeShotPos(Entity ent){
        if (ent instanceof Player pl){
            if (isUsingStand(ent)){
                return ((StandUser)pl).roundabout$getStandPowers().activePower  == PowerIndex.EXTRA;
            }
        }
        return false;
    }

    public static boolean isBrawling(Entity ent){
        if (ent instanceof Player pl){
            if (isUsingPower(ent)){
                return ((IPowersPlayer)pl).rdbt$getPowers().isBrawling();
            }  else if (isUsingStand(ent)){
                return ((StandUser)pl).roundabout$getStandPowers().isBrawling();
            }
        }
        return false;
    }
    public static boolean isBrawlAttacking(Entity ent){
        if (ent instanceof Player pl){
            if (isUsingPower(ent)){
                return ((IPowersPlayer)pl).rdbt$getPowers().isBrawling() &&
                        ((IPowersPlayer)pl).rdbt$getPowers().getActivePower() != PowerIndex.NONE;
            }
        }
        return false;
    }
    public static boolean isBrawlButNotAttacking(Entity ent){
        if (ent instanceof Player pl){
            if (isUsingPower(ent)){
                return ((IPowersPlayer)pl).rdbt$getPowers().isBrawling() &&
                        (((IPowersPlayer)pl).rdbt$getPowers().getActivePower() == PowerIndex.NONE ||
                        ((IPowersPlayer)pl).rdbt$getPowers().getActivePower() == PowerIndex.BRAWL_ATTACK);
            } if (isUsingStand(ent)){
                return ((StandUser)pl).roundabout$getStandPowers().isBrawling() &&
                        (((StandUser)pl).roundabout$getStandPowers().getActivePower() == PowerIndex.NONE ||
                                ((StandUser)pl).roundabout$getStandPowers().getActivePower() == PowerIndex.BRAWL_ATTACK);
            }
        }
        return false;
    }


    public static boolean isUsingPower(Entity ent){
        if (ent instanceof Player pl){
            if (((StandUser)pl).roundabout$getActive()){
                return (getPowerFromByte(getPowerType(ent)) != PowerTypes.STAND
                && getPowerFromByte(getPowerType(ent)) != PowerTypes.NONE);
            }
        }
        return false;
    }

    public static boolean isUsingStand(Entity ent){
        if (ent instanceof Player pl){
            if (((StandUser)pl).roundabout$getActive()){
                return (getPowerFromByte(getPowerType(ent)) == PowerTypes.STAND);
            }
        } else if (ent instanceof LivingEntity LE){
            return ((StandUser)LE).roundabout$getActive();
        }
        return false;
    }

    public static List<PowerTypes> getAvailablePowers(Player pl){
        List<PowerTypes> powerList = new ArrayList<>();
        if (pl != null) {
            if (FateTypes.isVampire(pl)) {
                powerList.add(VAMPIRE);
            }
            StandUser user = ((StandUser) pl);
            boolean hasStand = user.roundabout$hasAStand();
            if (hasStand){
                powerList.add(STAND);
            }
        }
        return powerList;
    }

    public static boolean hasStandActivelyEquipped(Entity entity){
        if (entity instanceof LivingEntity LE){
            if (entity instanceof Player PL){
                return getPowerType(PL) == STAND.ordinal();
            }
            return ((StandUser)LE).roundabout$hasAStand();
        }
        return false;
    }

    public static boolean hasPowerActivelyEquipped(Entity entity){
        if (entity instanceof LivingEntity LE){
            if (entity instanceof Player PL){
                return getPowerType(PL) != STAND.ordinal() && getPowerType(PL) != NONE.ordinal();
            }
        }
        return false;
    }

    public static boolean hasStandActive(Entity entity){
        if (entity instanceof LivingEntity LE){
            if (entity instanceof Player PL){
                if (getPowerType(PL) != STAND.ordinal())
                    return false;
            }
            return ((StandUser)LE).roundabout$getActive();
        }
        return false;
    }

    public static boolean isMiningStand(Entity entity) {
        if (entity instanceof LivingEntity LE) {
            StandUser user = (StandUser) LE;
            if (user.roundabout$getStandPowers() != null) {
                return user.roundabout$getStandPowers().isMiningStand();
            }
        }
        return false;
    }

    public static boolean hasPowerActive(Entity entity){
        if (entity instanceof LivingEntity LE){
            if (entity instanceof Player PL){
                if (getPowerType(PL) == STAND.ordinal() || getPowerType(PL) == NONE.ordinal())
                    return false;
            }
            return ((StandUser)LE).roundabout$getActive();
        }
        return false;
    }

    public static boolean canHavePower(Entity entity, byte bt){
        if (entity instanceof Player pl) {
            if (bt == STAND.ordinal()) {
                if (((StandUser) entity).roundabout$hasAStand()) {
                    return true;
                }
            } else if (bt == VAMPIRE.ordinal()) {
                if (FateTypes.isVampire(pl)) {
                    return true;
                }
            }
        }
        return false;
    }

    //When you switch out of vampire, you should lose vampire powers for instance
    public static void fixPowers(Entity entity){
        if (entity instanceof Player pl) {
            byte bt = getPowerType(pl);
            if (bt == STAND.ordinal()){
                if (!((StandUser)entity).roundabout$hasAStand()){
                    ((StandUser)entity).roundabout$setActive(false);
                    setPowerType(entity, (byte) NONE.ordinal());
                }
            } else if (bt == VAMPIRE.ordinal()){
                if (!FateTypes.isVampire(pl)){
                    ((StandUser)entity).roundabout$setActive(false);
                    setPowerType(entity, (byte) NONE.ordinal());
                }
            }

            if (bt == NONE.ordinal()){
                if (((StandUser)entity).roundabout$hasAStand()){
                    setPowerType(entity, (byte) STAND.ordinal());
                } else if (FateTypes.isVampire(pl)){
                    setPowerType(entity, (byte) VAMPIRE.ordinal());
                }
            }
        }
    }
}
