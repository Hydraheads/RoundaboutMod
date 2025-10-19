package net.hydra.jojomod.event.index;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public enum FateTypes {
    HUMAN((byte) 0, new FatePowers()),
    VAMPIRE((byte) 1, new VampireFate()),
    SHADOW_CREATURE((byte) 2, new FatePowers()),
    PILLAR_MAN((byte) 3, new FatePowers()),
    ULTIMATE_LIFEFORM((byte) 4, new FatePowers()),
    ROCK_HUMAN((byte) 5, new FatePowers()),
    GHOST((byte) 6, new FatePowers()),
    DOG((byte) 7, new FatePowers()),
    RAT((byte) 8, new FatePowers()),
    FALCON((byte) 9, new FatePowers()),
    PLANKTON_COLONY((byte) 10, new FatePowers());

    public final byte id;
    public final FatePowers fatePowers;

    private FateTypes(byte $$0, FatePowers $$1) {
        this.id = $$0;
        this.fatePowers = $$1;
    }
    public static FateTypes getFateFromByte(byte bt){
        if (bt == VAMPIRE.id)
            return VAMPIRE;
        if (bt == SHADOW_CREATURE.id)
            return SHADOW_CREATURE;
        if (bt == PILLAR_MAN.id)
            return PILLAR_MAN;
        if (bt == ULTIMATE_LIFEFORM.id)
            return ULTIMATE_LIFEFORM;
        if (bt == ROCK_HUMAN.id)
            return ROCK_HUMAN;
        if (bt == GHOST.id)
            return GHOST;
        if (bt == DOG.id)
            return DOG;
        if (bt == RAT.id)
            return RAT;
        if (bt == FALCON.id)
            return FALCON;
        if (bt == PLANKTON_COLONY.id)
            return PLANKTON_COLONY;
        return HUMAN;
    }
    public static boolean isHuman(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IPlayerEntity)PE).roundabout$getFate() <= HUMAN.id;
        }
        return false;
    }
    public static boolean isVampire(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IPlayerEntity)PE).roundabout$getFate() == VAMPIRE.id;
        }
        return false;
    }
    public static boolean hasBloodHunger(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IPlayerEntity)PE).roundabout$getFate() == VAMPIRE.id;
        }
        return false;
    }
    public static boolean canSeeInTheDark(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IPlayerEntity)PE).roundabout$getFate() == VAMPIRE.id;
        }
        return false;
    }
    public static boolean isTransforming(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IFatePlayer)PE).rdbt$isTransforming();
        }
        return false;
    }
    public static void setVampire(LivingEntity entity){
        if (entity instanceof Player PE){
            ((IPlayerEntity)PE).roundabout$setFate(VAMPIRE.id);
        }
    }
    public static void setHuman(LivingEntity entity){
        if (entity instanceof Player PE){
            ((IPlayerEntity)PE).roundabout$setFate(HUMAN.id);
        }
    }
}
