package net.hydra.jojomod.event.index;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
    public static boolean takesSunlightDamage(LivingEntity entity){
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



    public static boolean isInSunlight(LivingEntity ent) {
        Vec3 yes = ent.getEyePosition();
        Vec3 yes2 = ent.position();

        /**Vampires die under the sun, even under liquids*/
        int waterReach = ClientNetworking.getAppropriateConfig().vampireSettings.sunDamageUnderwaterReach;
        if (waterReach > 0) {
            for (var i = 0; i < waterReach; i++) {
                if (ent.level().getBlockState(BlockPos.containing(yes)).liquid()) {
                    yes = yes.add(0, 1, 0);
                } else {
                    i = 100;
                }
            }
        }

        long timeOfDay = ent.level().getDayTime() % 24000L;
        boolean isDay = timeOfDay < 12000L; // 0–12000 = day, 12000–24000 = night
        BlockPos atVec = BlockPos.containing(yes);
        BlockPos atVec2 = BlockPos.containing(yes2);
        if ((ent.level().canSeeSky(atVec) || ent.level().canSeeSky(atVec2)) &&
                ent.level().dimension().location().getPath().equals("overworld") &&
                isDay
        ) {
            return true;
        }
        return false;
    }
}
