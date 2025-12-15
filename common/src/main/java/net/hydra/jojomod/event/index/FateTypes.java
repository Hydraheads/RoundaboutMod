package net.hydra.jojomod.event.index;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public enum FateTypes {
    HUMAN((byte) 0, new FatePowers()),
    VAMPIRE((byte) 1, new VampireFate()),
    ZOMBIE((byte) 2, new FatePowers()),
    SHADOW_CREATURE((byte) 3, new FatePowers()),
    PILLAR_MAN((byte) 4, new FatePowers()),
    ULTIMATE_LIFEFORM((byte) 5, new FatePowers()),
    ROCK_HUMAN((byte) 6, new FatePowers()),
    GHOST((byte) 7, new FatePowers()),
    DOG((byte) 8, new FatePowers()),
    RAT((byte) 9, new FatePowers()),
    FALCON((byte) 10, new FatePowers()),
    PLANKTON_COLONY((byte) 11, new FatePowers());

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
        if (entity instanceof Mob mb && ((IMob)mb).roundabout$isVampire())
            return true;
        return false;
    }
    public static float getDamageResist(LivingEntity entity, DamageSource source, float amt){
        if (entity instanceof Player PE){
            return ((IFatePlayer)PE).rdbt$getFatePowers().getDamageReduction(source,amt);
        }
        if (entity instanceof Mob mb && ((IMob)mb).roundabout$isVampire()) {
            if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)){
                return 0.15F;
            }
            if (source.is(DamageTypes.ARROW) || source.is(ModDamageTypes.BULLET)){
                return 0.2F;
            }
        }
        return 0;
    }
    public static float getDamageAdd(LivingEntity entity, DamageSource source, float amt){
        if (source.getEntity() != null) {
            if (source.getEntity() instanceof Player PE) {
                return ((IFatePlayer) PE).rdbt$getFatePowers().getDamageAdd(source, amt,entity);
            }
            if (source.getEntity() instanceof Mob mb && ((IMob) mb).roundabout$isVampire()) {
                if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)) {
                    return 0.4F;
                }
            }
        }
        return 0;
    }
    public static boolean isEvil(LivingEntity entity){
        if (entity instanceof Player PE){
            Byte fate = ((IPlayerEntity)PE).roundabout$getFate();
            return fate == VAMPIRE.id || fate == ZOMBIE.id || fate == PILLAR_MAN.id || fate == ULTIMATE_LIFEFORM.id;
        }
        return false;
    }
    public static boolean isDaggerUpgraded(LivingEntity entity){
        if (entity instanceof Player PE){
            Byte fate = ((IPlayerEntity)PE).roundabout$getFate();
            return fate == VAMPIRE.id;
        }
        return false;
    }
    public static boolean isScary(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IPlayerEntity)PE).roundabout$getFate() == ZOMBIE.id;
        }
        return false;
    }
    public static boolean isVampireStrong(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IPlayerEntity)PE).roundabout$getFate() == VAMPIRE.id;
        }
        return false;
    }
    public static boolean hasBloodHunger(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IPlayerEntity)PE).roundabout$getFate() == VAMPIRE.id;
        }
        if (entity instanceof Mob mb && ((IMob)mb).roundabout$isVampire())
            return true;
        return false;
    }
    public static boolean canSeeInTheDark(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IFatePlayer)PE).rdbt$getFatePowers() instanceof VampiricFate VP &&
                    VP.isVisionOn();
        }
        return false;
    }
    public static boolean takesSunlightDamage(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IPlayerEntity)PE).roundabout$getFate() == VAMPIRE.id;
        }
        if (entity instanceof Mob mb && ((IMob)mb).roundabout$isVampire())
            return true;
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
    public static float getJumpHeightAddon(LivingEntity entity){
        if (entity instanceof Player PE){
            return ((IFatePlayer)PE).rdbt$getFatePowers().getJumpHeightAddon();
        }
        return 0;
    }
    public static float getJumpHeightPower(LivingEntity entity, boolean isOverOriginal){
        if (entity instanceof Player PE){
            return ((IFatePlayer)PE).rdbt$getFatePowers().getJumpHeightPower(isOverOriginal);
        }
        return 0;
    }




    public static boolean isInSunlight(LivingEntity ent) {
        //You don't take sun damage in stopped time (like the ova)
        if (!((TimeStop) ent.level()).inTimeStopRange(ent)) {
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
            boolean isDay = timeOfDay < 12555L || timeOfDay > 23360; // 0–12000 = day, 12000–24000 = night
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
        return false;
    }
}
