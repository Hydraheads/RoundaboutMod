package net.hydra.jojomod.event.index;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.powers.power_types.StandGeneralPowers;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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

    public static boolean isBrawling(Entity ent){
        if (ent instanceof Player pl){
            if (isUsingPower(ent)){
                return ((IPowersPlayer)pl).rdbt$getPowers().isBrawling();
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
                        ((IPowersPlayer)pl).rdbt$getPowers().getActivePower() == PowerIndex.NONE;
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
}
