package net.hydra.jojomod.event.index;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public enum FateTypes {
    HUMAN((byte) 0),
    VAMPIRE((byte) 1),
    SHADOW_CREATURE((byte) 2),
    PILLAR_MAN((byte) 3),
    ULTIMATE_LIFEFORM((byte) 4),
    ROCK_HUMAN((byte) 5),
    GHOST((byte) 6),
    DOG((byte) 7),
    RAT((byte) 8),
    FALCON((byte) 9),
    PLANKTON_COLONY((byte) 10);

    public final byte id;

    private FateTypes(byte $$0) {
        this.id = $$0;
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
