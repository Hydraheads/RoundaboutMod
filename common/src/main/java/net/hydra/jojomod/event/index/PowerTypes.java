package net.hydra.jojomod.event.index;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.projectile.BloodSplatterEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.powers.power_types.StandGeneralPowers;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static boolean hasHandsActive(Entity ent){
        //specifically stand arms
        if (ent instanceof LivingEntity livingEntity) {
            if (isUsingStand(livingEntity)) {
                return ((StandUser)livingEntity).roundabout$getStandPowers().hasHandsOut();
            }
        }
        return false;
    }

    public static boolean hasHandsForVisage(Entity ent){
        //specifically stand arms
        if (ent instanceof LivingEntity livingEntity) {
                return ((StandUser)livingEntity).roundabout$getStandPowers().hasHandsOut();
        }
        return false;
    }
    public static boolean hasHandsActiveRendering(Entity ent){
        //specifically stand arms
        if (ent instanceof LivingEntity livingEntity) {
            if (isUsingStand(livingEntity)) {
                return ((StandUser)livingEntity).roundabout$getStandPowers().hasHandsOutRendering();
            }
        }
        return true;
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

    public static Entity expStore = null;
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
    //0 = normal / time erase
    //1-5 = D4C merging
    //10 = time erase
    //11 = man in the mirror
    public static byte getPlaneOfExisting(Entity entity){
        if (entity != null){
            if (PowerTypes.isErasingTime(entity)){
                return 10;
            }
            if (entity instanceof FollowingStandEntity fse && fse.getFollowing() != null){
                return getPlaneOfExisting(fse.getFollowing());
            }
            return ((IGravityEntity)entity).roundabout$getExistPlane();
        }
        return 0;
    }
    public static byte getPlaneOfExisting2(Entity entity){
        if (entity != null){
            if (entity instanceof FollowingStandEntity fse && fse.getFollowing() != null){
                return getPlaneOfExisting2(fse.getFollowing());
            }
            return ((IGravityEntity)entity).roundabout$getExistPlane();
        }
        return 0;
    }
    public static void setTicksUntilGone(Entity entity, int ticks, byte worldId){
        if (entity != null){
            ((IEntityAndData)entity).rdbt$setTicksUntilGone(ticks);
            ((IEntityAndData)entity).rdbt$setNativeTo(worldId);
            ((IEntityAndData)entity).rdbt$setOriginWorld(worldId);
        }
    }
    public static void setPlaneOfExisting(Entity entity, byte plane){
        if (entity != null){
            ((IGravityEntity)entity).roundabout$setExistPlane(plane);
        }
    }
    public static void forcePlaneOfExisting(Entity entity, byte plane){
        if (entity instanceof LivingEntity LE){
            StandUser user = ((StandUser) LE);
            if (user.roundabout$isClashing()){
                user.roundabout$getStandPowers().endClash();
            }
            user.roundabout$tryPower(PowerIndex.NONE,true);
        }
        ((IGravityEntity)entity).roundabout$setExistPlane(plane);
    }
    public static void copyPlaneOfExisting(Entity from, Entity to){
        if (from != null && to != null){
            ((IGravityEntity)to).roundabout$setExistPlane(
                    ((IGravityEntity)from).roundabout$getExistPlane());
            ((IEntityAndData)to).rdbt$setForeignWorldTicks(
                    ((IEntityAndData)from).rdbt$getForeignWorldTicks());
            ((IEntityAndData)to).rdbt$setTicksUntilGone(
                    ((IEntityAndData)from).rdbt$getTicksUntilGone());
            ((IEntityAndData)to).rdbt$setNativeTo(
                    ((IEntityAndData)from).rdbt$getNativeTo());

        }
    }
    public static boolean isNativeToOurWorld(Entity entity){
        if (entity != null){

            return ((IEntityAndData)entity).rdbt$getNativeTo() == 0;

        }
        return false;
    }
    public static void tickIsNearAlt(Entity entity, Entity alt, int delayTime){
        if (alt != null && entity != null) {
            if (entity.level() instanceof ServerLevel sl) {
                if (entity.tickCount % 2 == 0) {
                    double random = (Math.random() * 1.2) - 0.6;
                    double random2 = (Math.random() * 1.2) - 0.6;
                    double random3 = (Math.random() * 1.2) - 0.6;

                    Vec3 center1 = (entity.getEyePosition().subtract(entity.getPosition(1)).scale(0.7)).add(entity.getPosition(1));
                    Vec3 center2 = (alt.getEyePosition().subtract(alt.getPosition(1)).scale(0.7)).add(alt.getPosition(1));
                    MainUtil.sendParticlesIfPossible(entity, sl,
                            ModParticles.MENGER, center2.x + random,
                            center2.y + random2, center2.z + random3,
                            0,
                            (center1.x - center2.x), (center1.y - center2.y), (center1.z - center2.z),
                            0.08);
                }

                if (!(alt instanceof Player)) {

                    float carryon = Math.min((1+(((float)delayTime)/50f)),7);
                    Vec3 db = RotationUtil.distanceBetween(alt,entity);
                    alt.setDeltaMovement(alt.getDeltaMovement().add(
                            db.x * (-0.012 * carryon),
                            0,
                            db.z * (-0.012 * carryon)
                    ));
                    alt.hurtMarked = true;
                    alt.hasImpulse = true;
                    if (delayTime > 20 && !PowerTypes.originatedFromOurWorld(alt) && (alt.distanceTo(entity) < 1.5 ||
                            delayTime >= 300)){
                        MainUtil.playSoundIfPossible(entity,entity.level(),null, entity.blockPosition(),
                                SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1F);
                        //Hear some of it across dimensions to know a collision happened
                        MainUtil.playSoundToAll(entity.level(),null, entity.blockPosition(),
                                ModSounds.D4C_EXPLOSION_EVENT, SoundSource.PLAYERS, 3.0F, 1F);

                        //MainUtil.sendParticlesIfPossible(entity,entity.level(),ModParticles.FIRE_CRUMBLE,
                        //        entity.getX(), entity.getY() + 1.0D, entity.getZ(),
                        //        5, 0.4,0.4, 0.4,0.01);

                        Vec3 position = entity.getPosition(1);
                        Vec3 position2 = entity.getEyePosition();
                        Vec3 position3 = entity.getEyePosition().subtract(entity.getPosition(1)).multiply(new Vec3(0.5F,
                                0.5F,0.5F));

                        MainUtil.sendParticlesIfPossible(entity,entity.level(),ParticleTypes.LARGE_SMOKE,
                                entity.getX(), entity.getY() + 1.0D, entity.getZ(),
                                15, 0.3,0.5, 0.3,0.01);
                        //See some of it across dimensions to know a collision happened
                        sl.sendParticles(ModParticles.MENGER,
                                entity.getEyePosition().x, entity.getEyePosition().y, entity.getEyePosition().z,
                                20, 0,0, 0,0.07);
                        float dmg= 30;
                        if (entity instanceof Player){
                            dmg = 15;
                        }
                        entity.hurt(ModDamageTypes.of(entity.level(),ModDamageTypes.D4C_COLLISION),
                                dmg);

                        alt.discard();
                    } else {
                        if (entity instanceof LivingEntity LE && alt instanceof LivingEntity LE2){
                            MainUtil.makeBleed(LE,0,200,null);
                            MainUtil.makeBleed(LE2,0,200,null);
                        }
                    }
                }
            }
        }

    }
    public static boolean originatedFromOurWorld(Entity entity){
        if (entity != null){

            return ((IEntityAndData)entity).rdbt$getNativeTo() == 0 && ((IEntityAndData)entity).rdbt$getOriginWorld() == 0;

        }
        return false;
    }


    @Nullable
    public static Entity findNearbyParallelCopy(Entity entity) {
        if (!(entity.level() instanceof ServerLevel sl)) {
            return null;
        }

        UUID parallelUUID = ((IEntityAndData) entity).rdbt$getNativeCopy();

        if (parallelUUID == null) {
            return null;
        }

        AABB box = entity.getBoundingBox().inflate(10.0D);

        List<Entity> nearby = sl.getEntities(
                entity,
                box,
                other ->
                        other != entity
                                && other.isAlive()
                                && MainUtil.canActuallyHitInvolved2(entity,other)
                                && (other.getUUID().equals(parallelUUID) ||
                                (((IEntityAndData) other).rdbt$getNativeCopy() != null &&
                                        ((IEntityAndData) other).rdbt$getNativeCopy().equals(parallelUUID)))
        );

        if (!nearby.isEmpty()) {
            return nearby.get(0);
        }

        return null;
    }

    public static int d4cWorldUptime(){
        return 200;
    }
    public static int getForeignWorldMaxTime(byte worldType){
        if (worldType == 0 || worldType == 10){
            return -1;
        }
        if (worldType <= 8){
            return d4cWorldUptime();
        }
        if (worldType == 11){
            return 400;
        }
        return 1000;
    }
    public static boolean canInteractInExistence(Entity entity){
        if (entity != null){
            byte plane = getPlaneOfExisting(entity);
            return plane == 11;
        }
        return false;
    }
    public static boolean isExistentiallyElsewhereTogether(Entity entity, Entity entityTwo){
        if (entity != null && entityTwo != null){
            byte p1 = getPlaneOfExisting(entity);
            byte p2 = getPlaneOfExisting(entityTwo);
            return ((p1 == p2) && p1 != 11);
        }
        return false;
    }
    public static boolean isExistentiallyElsewhereTogether2(Entity entity, Entity entityTwo){
        if (entity != null && entityTwo != null){
            byte p1 = getPlaneOfExisting2(entity);
            byte p2 = getPlaneOfExisting2(entityTwo);
            return ((p1 == p2) && p1 != 11);
        }
        return false;
    }

    public static boolean isInADifferentExistence(Entity entity, Entity entityTwo){
        if (entity != null && entityTwo != null){
            boolean ex1 = isExistentiallyElsewhere(entity);
            boolean ex2 = isExistentiallyElsewhere(entityTwo);
            if (ex1 && ex2){
                return !isExistentiallyElsewhereTogether(entity,entityTwo);
            } else if (ex1 || ex2) {
                return true;
            }
        }
        return false;
    }
    public static boolean isInADifferentExistenceNoTE(Entity entity, Entity entityTwo){
        if (entity != null && entityTwo != null){
            boolean ex1 = isExistentiallyElsewhere(entity);
            boolean ex2 = isExistentiallyElsewhere(entityTwo);
            if (ex1 && ex2){
                return !isExistentiallyElsewhereTogether2(entity,entityTwo);
            } else if (ex1 || ex2) {
                return true;
            }
        }
        return false;
    }
    //d4c parallel run + time erase + man in the mirror
    public static boolean isExistentiallyElsewhere(Entity entity){
        if (entity == null){
            return false;
        }
        if (entity.level().isClientSide()){
            if (entity instanceof KingCrimsonCloneEntity kcc){
                if (ClientUtil.isPlayer(kcc.getPlayer()) && !ConfigManager.getClientConfig().generalSettings.canSeeFatedSelf){
                    return true;
                }
            }
            if (entity instanceof BloodSplatterEntity bse && bse.getSplatterType() == 2){
                if (PowerTypes.isErasingTime(ClientUtil.getPlayer())){
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            if (entity instanceof BloodSplatterEntity bse && bse.getSplatterType() == 3){
                return true;
            }
        }

        if (entity instanceof FollowingStandEntity se) {
            if (se.getFollowing() != null){

                return isExistentiallyElsewhere(se.getFollowing());
            }
        } if (entity instanceof StandEntity se){
            if (se.getUser() != null){
                return isExistentiallyElsewhere(se.getUser());
            }
        }

        if (isErasingTime(entity)){
            return true;
        }
        if ((getPlaneOfExisting(entity)) != 0){
            return true;
        }
        return false;
    }
    public static boolean isErasingTime(Entity entity){
        if (entity instanceof Player pl){
            return ((StandUser)pl).roundabout$getStandPowers() instanceof PowersKingCrimson pkc &&
                    pkc.timeEraseActive;
        }
        return false;
    }
    public static boolean isInD4CWorld(Entity entity){
        byte exist = getPlaneOfExisting(entity);
        return exist >0 && exist <= 8;
    }
    public static boolean isInD4CWorldWithRender(Entity entity){
        byte exist = getPlaneOfExisting(entity);
        return exist >0 && exist <= 5;
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
