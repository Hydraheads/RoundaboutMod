package net.hydra.jojomod.event.index;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.powers.GeneralPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public enum PowerTypes {
    NONE(new GeneralPowers()),
    STAND(new GeneralPowers()),
    VAMPIRE(new GeneralPowers()),
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
}
