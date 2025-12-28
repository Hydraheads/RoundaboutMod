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
    NONE((byte) 0, new GeneralPowers()),
    STAND((byte) 1, new GeneralPowers()),
    VAMPIRE((byte) 2, new GeneralPowers()),
    HAMON((byte) 3, new GeneralPowers()),
    SPIN((byte) 4, new GeneralPowers());

    public final byte id;
    public final GeneralPowers generalPowers;

    public static PowerTypes getPowerFromByte(byte bt){
        if (bt == VAMPIRE.id)
            return VAMPIRE;
        if (bt == HAMON.id)
            return HAMON;
        if (bt == SPIN.id)
            return SPIN;
        if (bt == STAND.id)
            return STAND;
        return NONE;
    }

    private PowerTypes(byte $$0, GeneralPowers $$1) {
        this.id = $$0;
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
            if (getPowerType(pl) == NONE.id)
                ((IPlayerEntity)pl).roundabout$setPower(STAND.id);
        }
    }
    public static void forceInitializeStandPower(Entity ent){
        if (ent instanceof Player pl){
            if (getPowerType(pl) == NONE.id)
                ((IPlayerEntity)pl).roundabout$setPower(STAND.id);
        }
    }

    public static boolean hasStandActive(Entity entity){
        if (entity instanceof LivingEntity LE){
            if (entity instanceof Player PL){
                if (getPowerType(PL) != STAND.id)
                    return false;
            }
            return ((StandUser)LE).roundabout$getActive();
        }
        return false;
    }
}
