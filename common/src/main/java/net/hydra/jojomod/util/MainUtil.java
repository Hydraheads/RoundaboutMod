package net.hydra.jojomod.util;


import com.google.common.collect.Sets;
import com.google.common.primitives.Floats;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.gui.FogInventoryMenu;
import net.hydra.jojomod.client.gui.PowerInventoryMenu;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainUtil {
    /**Additional math functions for the mod.*/

    /** This version of interpolation accommodates speed multipliers so you can control how
     * fast something moves from point A to point B.
     * Ex: the speed a stand tilts in*/
    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * (end - start))*multiplier;
    }

    public static float controlledLerpAngleDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * Mth.wrapDegrees(end - start))*multiplier;
    }public static float controlledLerpRadianDegrees(float delta, float start, float end, float multiplier) {
        delta = Math.min(delta,1);
        return start + (delta * wrapRadians(end - start))*multiplier;
    }
    public static final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(20.0);
    public static final TargetingConditions followTargetting = TargetingConditions.forCombat().range(50.0);

    public static double getWorthyOdds(Mob mob) {
        if (mob instanceof Warden || mob instanceof WitherBoss || mob instanceof EnderDragon){
            return 0;
        }
        return ClientNetworking.getAppropriateConfig().worthyMobOdds;
    }
    public static double getStandUserOdds(Mob mob) {
        if (mob instanceof Warden || mob instanceof WitherBoss || mob instanceof EnderDragon
                || mob instanceof Vex){
            return 0;
        } else if (mob instanceof AbstractVillager){
            return ClientNetworking.getAppropriateConfig().standUserVillagerOdds;
        }
        return ClientNetworking.getAppropriateConfig().standUserOdds;
    }
    public static double getWorthyBreedBonus(Mob mob) {
        if (mob instanceof AbstractVillager){
            return ClientNetworking.getAppropriateConfig().userAndWorthyBreedingOddsBonus *ClientNetworking.getAppropriateConfig().multiplyAboveForVillagerBreeding;
        }
        return ClientNetworking.getAppropriateConfig().userAndWorthyBreedingOddsBonus;
    }
    public static double getStandUserBreedBonus(Mob mob) {
        if (mob instanceof AbstractVillager){
            return ClientNetworking.getAppropriateConfig().userAndWorthyBreedingOddsBonus *ClientNetworking.getAppropriateConfig().multiplyAboveForVillagerBreeding;
        }
        return ClientNetworking.getAppropriateConfig().userAndWorthyBreedingOddsBonus;
    }
    public static LivingEntity homeOnWorthy(Level level, Vec3 vec3, double range) {
        List<Entity> EntitiesInRange = genHitbox(level, vec3.x, vec3.y,
                vec3.z, range, range, range);
        List<Entity> hitEntities = new ArrayList<>(EntitiesInRange) {
        };
        LivingEntity mm = null;
        double distance = -1;
        for (Entity value : hitEntities) {
            if (value instanceof LivingEntity mb){
                if (canGrantStand(mb) && (distance == -1 || mb.distanceToSqr(vec3) < distance)){
                    mm = mb;
                    distance = mb.distanceToSqr(vec3);
                }
            }
        }
        return mm;
    }

    public static ItemStack saveToDiscData(LivingEntity ent, ItemStack stack){
        if (ent instanceof Player PE && !(stack.getItem() instanceof MaxStandDiscItem)) {
            IPlayerEntity IPE = ((IPlayerEntity) PE);
            stack.getOrCreateTagElement("Memory").putByte("Level", (byte) Math.max(IPE.roundabout$getStandLevel()-1,0));
            stack.getOrCreateTagElement("Memory").putInt("Experience",IPE.roundabout$getStandExp());
            stack.getOrCreateTagElement("Memory").putBoolean("BonusSkin",IPE.roundabout$getUnlockedBonusSkin());
        }
        stack.getOrCreateTagElement("Memory").putByte("Skin",((StandUser)ent).roundabout$getStandSkin());
        stack.getOrCreateTagElement("Memory").putByte("Pose",((StandUser)ent).roundabout$getIdlePos());
        return stack;
    }

    public static void handleChangeItem(Player player, byte context, ItemStack stack, byte context2, Vector3f vec) {
        if (context2 == PacketDataIndex.USE_CORPSE_BAG) {
        if (player.getInventory().contains(stack) || stack.is(ModItems.CREATIVE_BODY_BAG)) {
            ItemStack item;
            int zombies = 1;
            int skeletons = 1;
            int spiders = 1;
            int villagers = 1;
            int creepers = 1;
            if (!stack.is(ModItems.CREATIVE_BODY_BAG)){
                item = player.getInventory().getItem((player.getInventory().findSlotMatchingItem(stack)));
                CompoundTag $$1 = item.getOrCreateTagElement("bodies");
                zombies = $$1.getInt("zombie");
                skeletons = $$1.getInt("skeleton");
                spiders = $$1.getInt("spider");
                villagers = $$1.getInt("villager");
                creepers = $$1.getInt("creeper");
            }
                FallenMob fm = null;
                int yElevation = 0;
                if (context == Corpses.ZOMBIE.id) {
                    zombies--;
                    if (zombies >= 0) {
                        fm = ModEntities.FALLEN_ZOMBIE.create(player.level());
                    }
                } else if (context == Corpses.SKELETON.id) {
                    skeletons--;
                    if (skeletons >= 0) {
                        fm = ModEntities.FALLEN_SKELETON.create(player.level());
                    }
                } else if (context == Corpses.SPIDER.id) {
                    spiders--;
                    if (spiders >= 0) {
                        fm = ModEntities.FALLEN_SPIDER.create(player.level());
                    }
                } else if (context == Corpses.VILLAGER.id) {
                    villagers--;
                    if (villagers >= 0) {
                        fm = ModEntities.FALLEN_VILLAGER.create(player.level());
                    }
                } else if (context == Corpses.CREEPER.id) {
                    creepers--;
                    if (creepers >= 0) {
                        fm = ModEntities.FALLEN_CREEPER.create(player.level());
                    }
                }

                if (fm != null) {
                    fm.setPos(vec.x, vec.y + yElevation, vec.z);
                    fm.placer = player;
                    fm.setPhasesFull(true);
                    fm.tickThroughPlacerStart();
                    fm.setForcedRotation(player.getYRot() % 360);
                    fm.setYRot(player.getYRot() % 360);
                    fm.setYBodyRot(player.yBodyRot % 360);
                    player.level().addFreshEntity(fm);
                    if (!stack.is(ModItems.CREATIVE_BODY_BAG)) {
                        item = player.getInventory().getItem((player.getInventory().findSlotMatchingItem(stack)));
                        if (!player.isCreative() && player instanceof ServerPlayer SP) {
                            if (context == Corpses.ZOMBIE.id) {
                                item.getOrCreateTagElement("bodies").putInt("zombie", zombies);
                            } else if (context == Corpses.SKELETON.id) {
                                item.getOrCreateTagElement("bodies").putInt("skeleton", skeletons);
                            } else if (context == Corpses.SPIDER.id) {
                                item.getOrCreateTagElement("bodies").putInt("spider", spiders);
                            } else if (context == Corpses.VILLAGER.id) {
                                item.getOrCreateTagElement("bodies").putInt("villager", villagers);
                            } else if (context == Corpses.CREEPER.id) {
                                item.getOrCreateTagElement("bodies").putInt("creeper", creepers);
                            }
                        }
                    }
                }
        }
        }
    }

    public static void handleSetCreativeModeSlot(Player player, int integer, ItemStack stack, byte context) {
        if (context == PacketDataIndex.ADD_FOG_ITEM) {
            boolean flag = integer < 0;
            ItemStack itemstack = stack;
            if (!itemstack.isItemEnabled(player.level().enabledFeatures())) {
                return;
            }

            CompoundTag compoundtag = BlockItem.getBlockEntityData(itemstack);
            if (!itemstack.isEmpty() && compoundtag != null && compoundtag.contains("x") && compoundtag.contains("y") && compoundtag.contains("z")) {
                BlockPos blockpos = BlockEntity.getPosFromTag(compoundtag);
                if (player.level().isLoaded(blockpos)) {
                    BlockEntity blockentity = player.level().getBlockEntity(blockpos);
                    if (blockentity != null) {
                        blockentity.saveToItem(itemstack);
                    }
                }
            }

            boolean flag1 = integer >= 1 && integer <= 45;
            boolean flag2 = itemstack.isEmpty() || itemstack.getDamageValue() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();
            if (flag1 && flag2) {
                player.inventoryMenu.getSlot(integer).setByPlayer(itemstack);
                player.inventoryMenu.broadcastChanges();
            } else if (flag && flag2) {
                player.drop(itemstack, true);
            }

        }
    }
    public static void extractDiscData(LivingEntity ent, StandDiscItem SD, ItemStack stack){
        StandUser user = ((StandUser)ent);
        CompoundTag $$4 = stack.getTagElement("Memory");
        if ($$4 != null) {
            if (SD instanceof MaxStandDiscItem) {
                if (ent instanceof Player PE) {
                    IPlayerEntity IPE = ((IPlayerEntity) PE);
                    IPE.roundabout$setStandLevel(SD.standPowers.getMaxLevel());
                }
            } else {
                if (ent instanceof Player PE) {
                    IPlayerEntity IPE = ((IPlayerEntity) PE);
                    if ($$4.contains("Level")) {
                        byte lvl = (byte) ($$4.getByte("Level") + 1);
                        lvl = (byte) Mth.clamp(lvl, 1, SD.standPowers.getMaxLevel());
                        IPE.roundabout$setStandLevel(lvl);

                        if ($$4.contains("Experience")) {
                            int exp = (byte) ($$4.getInt("Experience"));
                            exp = (byte) Mth.clamp(exp, 0, SD.standPowers.getExpForLevelUp(lvl));
                            IPE.roundabout$setStandExp(exp);
                        } else {
                            IPE.roundabout$setStandExp(0);
                        }
                    } else {
                        IPE.roundabout$setStandLevel((byte)1);
                        IPE.roundabout$setStandExp(0);
                    }
                    if ($$4.contains("BonusSkin")) {
                        IPE.roundabout$setUnlockedBonusSkin($$4.getBoolean("BonusSkin"));
                    } else {
                        IPE.roundabout$setUnlockedBonusSkin(false);
                    }
                }
            }

            if ($$4.contains("Skin")) {
                byte skn = ($$4.getByte("Skin"));
                user.roundabout$setStandSkin(skn);
            } else {
                user.roundabout$setStandSkin((byte) 0);
            }
            if ($$4.contains("Pose")) {
                byte skn = ($$4.getByte("Pose"));
                user.roundabout$setIdlePosX(skn);
            } else {
                user.roundabout$setIdlePosX((byte) 0);
            }
        } else {
            if (ent instanceof Player PE) {
                IPlayerEntity IPE = ((IPlayerEntity) PE);
                IPE.roundabout$setStandLevel((byte) 1);
                IPE.roundabout$setStandExp(0);
                IPE.roundabout$setUnlockedBonusSkin(false);
            };
            user.roundabout$setStandSkin((byte) 0);
            user.roundabout$setIdlePosX((byte) 0);
        }

    }
    public static LivingEntity homeOnFlier(Level level, Vec3 vec3, double range, Entity owner) {
        List<Entity> EntitiesInRange = genHitbox(level, vec3.x, vec3.y,
                vec3.z, range, range, range);
        List<Entity> hitEntities = new ArrayList<>(EntitiesInRange) {
        };
        for (Entity value : hitEntities) {
            if (value instanceof LivingEntity mb){
                if ((mb.isFallFlying() || mb instanceof Phantom) && !(mb instanceof StandEntity)){
                    if (owner != null && owner.getUUID() == mb.getUUID()) {
                    } else {
                        return mb;
                    }
                }
            }
        }
        return null;
    }
    public static boolean getMobBleed(Entity Mob) {
        if (ClientNetworking.getAppropriateConfig().disableBleedingAndBloodSplatters){
            return false;
        }

        if (Mob instanceof LivingEntity){
            return Mob instanceof Zombie || (Mob instanceof Animal && !(Mob instanceof SkeletonHorse) && !(Mob instanceof ZombieHorse))
                    || Mob instanceof Villager
                    || Mob instanceof AbstractIllager || Mob instanceof Creeper || Mob instanceof Player
                    || Mob instanceof Spider || Mob instanceof EnderDragon || Mob instanceof EnderMan;
        }
        return false;
    }
    public static void makeMobBleed(Entity target) {
            int variety = (int) Math.round(Math.random() * 4);
            Block modBlock = ModBlocks.BLOOD_SPLATTER;
            if (MainUtil.hasBlueBlood(target)) {
                modBlock = ModBlocks.BLUE_BLOOD_SPLATTER;
            } else if (MainUtil.hasEnderBlood(target)) {
                modBlock = ModBlocks.ENDER_BLOOD_SPLATTER;
            }

            if (variety != 1) {
                MainUtil.setSplatter(target.level(), target.getOnPos(), (int) Math.floor(Math.random() * 3) - 1, 0, modBlock.defaultBlockState().
                        setValue(ModBlocks.BLOOD_LEVEL, Integer.valueOf((int) Math.round(Math.random() * 3))));
            }
            if (variety != 2) {
                MainUtil.setSplatter(target.level(), target.getOnPos(), (int) Math.floor(Math.random() * 3) - 1, -1, modBlock.defaultBlockState().
                        setValue(ModBlocks.BLOOD_LEVEL, Integer.valueOf((int) Math.round(Math.random() * 3))));
            }
            if (variety != 3) {
                MainUtil.setSplatter(target.level(), target.getOnPos(), (int) Math.floor(Math.random() * 3) - 1, 1, modBlock.defaultBlockState().
                        setValue(ModBlocks.BLOOD_LEVEL, Integer.valueOf((int) Math.round(Math.random() * 3))));
            }
    }
    public static boolean hasBlueBlood(Entity target){
        if (target instanceof Spider || target instanceof Bee || target instanceof Silverfish){
            return true;
        } else {
            return false;
        }
    }
    public static boolean hasEnderBlood(Entity target){
        if (target instanceof EnderMan || target instanceof Endermite || target instanceof EnderDragon){
            return true;
        } else {
            return false;
        }
    }

    private static boolean containsInvalidValues(double $$0, double $$1, double $$2, float $$3, float $$4) {
        return Double.isNaN($$0) || Double.isNaN($$1) || Double.isNaN($$2) || !Floats.isFinite($$4) || !Floats.isFinite($$3);
    }

    private static double clampHorizontal(double $$0) {
        return Mth.clamp($$0, -3.0E7, 3.0E7);
    }

    private static double clampVertical(double $$0) {
        return Mth.clamp($$0, -2.0E7, 2.0E7);
    }
    public static void handleMovePilot(double getX, double getY, double getZ, float getYRot, float getXRot,
                                       Player player, int entityInt) {
        Entity entity = player.level().getEntity(entityInt);
        if (entity != null) {
            if (containsInvalidValues(getX, getY, getZ, getYRot, getXRot)) {
            } else {
                if (entity != player) {
                    ServerLevel serverlevel = (ServerLevel) player.level();
                    double d0 = entity.getX();
                    double d1 = entity.getY();
                    double d2 = entity.getZ();
                    double d3 = clampHorizontal(getX);
                    double d4 = clampVertical(getY);
                    double d5 = clampHorizontal(getZ);
                    float f = Mth.wrapDegrees(getYRot);
                    float f1 = Mth.wrapDegrees(getXRot);

                    boolean flag = serverlevel.noCollision(entity, entity.getBoundingBox().deflate(0.0625D));
                    boolean flag1 = entity.verticalCollisionBelow;
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingentity = (LivingEntity) entity;
                        if (livingentity.onClimbable()) {
                            livingentity.resetFallDistance();
                        }
                    }

                    entity.absMoveTo(d3, d4, d5, f, f1);
                    entity.moveTo(d3, d4, d5, f, f1);
                }

            }
        }
    }
    public static boolean canCauseRejection(Entity ent){
        if (ent instanceof Mob ME){
            if (!(ME instanceof WitherBoss) && !(ME instanceof EnderDragon) && !(ME instanceof Warden)){
                if (((StandUser)ME).roundabout$getStandDisc().isEmpty()){
                    return true;
                }
            }
        } else if (ent instanceof Player PE){
            if (PE.experienceLevel < ClientNetworking.getAppropriateConfig().levelsToGetStand && ((StandUser) PE).roundabout$getStandDisc().isEmpty()){
                return true;
            }
        }
        return false;
    }
    public static boolean canGrantStand(Entity ent){
        if (ent instanceof Mob ME){
            if (!(ME instanceof WitherBoss) && !(ME instanceof EnderDragon) && !(ME instanceof Warden)
                    && !(ME instanceof StandEntity)){
                if (((StandUser)ME).roundabout$getStandDisc().isEmpty()){
                    return ((IMob)ME).roundabout$isWorthy();
                }
            }
        } else if (ent instanceof Player PL){
            if (ClientNetworking.getAppropriateConfig().canAwakenOtherPlayersWithArrows){
                if (((StandUser)PL).roundabout$getStandDisc().isEmpty()){
                    return PL.experienceLevel >= ClientNetworking.getAppropriateConfig().levelsToGetStand;
                }
            }
        }
        return false;
    }

    public static float gasDamageMultiplier(){
        return (float) (0.83F * (ClientNetworking.getAppropriateConfig().damageMultipliers.gasolineExplosion *0.01));
    }

    public static void makeBleed(Entity entity, int level, int ticks, Entity source){
        if (ClientNetworking.getAppropriateConfig().disableBleedingAndBloodSplatters){
            return;
        }
        if (getMobBleed(entity)){
            ((StandUser)entity).roundabout$setBleedLevel(level);
            ((LivingEntity)entity).addEffect(new MobEffectInstance(ModEffects.BLEED, ticks, level), source);
        }
    }

    /**Imported Chrosu fruit TP code for ender blood*/
    public static void randomChorusTeleport(LivingEntity entity){
        Level $$1 = entity.level();
        if (!$$1.isClientSide) {
            double $$4 = entity.getX();
            double $$5 = entity.getY();
            double $$6 = entity.getZ();

            for (int $$7 = 0; $$7 < 16; $$7++) {
                double $$8 = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
                double $$9 = Mth.clamp(
                        entity.getY() + (double) (entity.getRandom().nextInt(16) - 8),
                        (double) $$1.getMinBuildHeight(),
                        (double) ($$1.getMinBuildHeight() + ((ServerLevel) $$1).getLogicalHeight() - 1)
                );
                double $$10 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }

                Vec3 $$11 = entity.position();
                if (randomTeleport(entity,$$8, $$9, $$10, true)) {
                    entity.hurtMarked = true;
                    entity.hasImpulse = true;
                    $$1.gameEvent(GameEvent.TELEPORT, $$11, GameEvent.Context.of(entity));
                    SoundEvent $$12 = entity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    $$1.playSound(null, $$4, $$5, $$6, $$12, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.playSound($$12, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }

    public static boolean randomTeleport(LivingEntity ent, double $$0, double $$1, double $$2, boolean $$3) {
        double $$4 = ent.getX();
        double $$5 = ent.getY();
        double $$6 = ent.getZ();
        double $$7 = $$1;
        boolean $$8 = false;
        BlockPos $$9 = BlockPos.containing($$0, $$1, $$2);
        Level $$10 = ent.level();
        if ($$10.hasChunkAt($$9)) {
            boolean $$11 = false;

            while (!$$11 && $$9.getY() > $$10.getMinBuildHeight()) {
                BlockPos $$12 = $$9.below();
                BlockState $$13 = $$10.getBlockState($$12);
                if ($$13.blocksMotion()) {
                    $$11 = true;
                } else {
                    $$7--;
                    $$9 = $$12;
                }
            }

            if ($$11) {
                ent.teleportTo($$0, $$7, $$2);
                if (ent instanceof Player){
                    ((IEntityAndData)ent).roundabout$setQVec2Params(new Vec3($$0, $$7, $$2));
                }
                if ($$10.noCollision(ent) && !$$10.containsAnyLiquid(ent.getBoundingBox())) {
                    $$8 = true;
                }
            }
        }

        if (!$$8) {
            ent.teleportTo($$4, $$5, $$6);
            if (ent instanceof Player){
                ((IEntityAndData)ent).roundabout$setQVec2Params(new Vec3($$0, $$7, $$2));
            }
            return false;
        } else {
            if ($$3) {
                $$10.broadcastEntityEvent(ent, (byte)46);
            }

            if (ent instanceof PathfinderMob) {
                ((PathfinderMob)ent).getNavigation().stop();
            }

            return true;
        }
    }

    public static List<Entity> genHitbox(Level level, double startX, double startY, double startZ, double radiusX, double radiusY, double radiusZ) {
        double k = Mth.floor(startX - radiusX);
        double l = Mth.floor(startX + radiusX);
        double r = (startY - radiusY);
        double s = (startY + radiusY);
        double t = (startZ - radiusZ);
        double u = (startZ + radiusZ);
        return level.getEntities(null, new AABB(k, r, t, l, s, u));
    }

    public static List<net.minecraft.world.entity.Entity> hitbox(List<net.minecraft.world.entity.Entity> entities){

        List<net.minecraft.world.entity.Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if (!value.showVehicleHealth() || value.isInvulnerable() || !value.isAlive() || value instanceof StandEntity){
                hitEntities.remove(value);
            }
        }
        return hitEntities;
    }
    public static List<net.minecraft.world.entity.Entity> hitboxGas(List<net.minecraft.world.entity.Entity> entities){

        List<net.minecraft.world.entity.Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if ((!value.showVehicleHealth() && !(value instanceof GasolineCanEntity)) || value.isInvulnerable() || !value.isAlive()){
                hitEntities.remove(value);
            }
        }
        return hitEntities;
    }

    public static float wrapRadians(float radians) {
        radians *= Mth.RAD_TO_DEG;
        float f = radians % 360.0f;
        if (f >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return (f*Mth.DEG_TO_RAD);
    }

    public static Vec3 getMoveRelative(float $$0, Vec3 $$1, Entity ett) {
        return getInputVector($$1, $$0, ett.getYRot());
    }

    private static Vec3 getInputVector(Vec3 $$0, float $$1, float $$2) {
        double $$3 = $$0.lengthSqr();
        if ($$3 < 1.0E-7) {
            return Vec3.ZERO;
        } else {
            Vec3 $$4 = ($$3 > 1.0 ? $$0.normalize() : $$0).scale((double)$$1);
            float $$5 = Mth.sin($$2 * (float) (Math.PI / 180.0));
            float $$6 = Mth.cos($$2 * (float) (Math.PI / 180.0));
            return new Vec3($$4.x * (double)$$6 - $$4.z * (double)$$5, $$4.y, $$4.z * (double)$$6 + $$4.x * (double)$$5);
        }
    }

    public static double fixAngle(float angle){
       return (Math.abs(angle) % 360);
    }

    public static void takeUnresistableKnockbackWithY(Entity entity, double strength, double x, double y, double z) {
        entity.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(x, y, z).normalize().scale(strength);
        entity.setDeltaMovement(- vec3d2.x,
                -vec3d2.y,
                - vec3d2.z);
        entity.hasImpulse = true;
    }
    public static void takeUnresistableKnockbackWithYBias(Entity entity, double strength, double x, double y, double z, float yBias) {
        entity.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(x, y, z).normalize().scale(strength);
        Vec3 vec3d3 = vec3d2.multiply(yBias,1,yBias);
        entity.setDeltaMovement(- vec3d3.x,
                -vec3d3.y,
                - vec3d3.z);
        entity.hasImpulse = true;
    }
    public static void takeUnresistableKnockbackWithY2(Entity entity,double x, double y, double z) {
        entity.hurtMarked = true;
        entity.setDeltaMovement( x,
                y,
                z);
        entity.hasImpulse = true;
    }

    public static void ejectInFront(StandEntity stand){
        if (stand.getFirstPassenger() != null && stand.getUser() != null){
            Entity entity = stand.getFirstPassenger();
            stand.ejectPassengers();
            if (entity.level().dimensionTypeId() == stand.getUser().level().dimensionTypeId()) {
                if (entity instanceof Player ent) {
                    ((IEntityAndData) ent).roundabout$setQVec2Params(new Vec3(stand.getUser().getX(), stand.getUser().getY(), stand.getUser().getZ()));
                } else {
                    entity.dismountTo(stand.getUser().getX(), stand.getUser().getY(), stand.getUser().getZ());
                }
            }
        }
    }

    public static void knockback(Entity entity, double d, double e, double f) {
        if (entity instanceof LivingEntity le && (d *= 1.0 - le.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) <= 0.0) {
            return;
        }
        entity.hasImpulse = true;
        Vec3 vec3 = entity.getDeltaMovement();
        Vec3 vec32 = new Vec3(e, 0.0, f).normalize().scale(d);
        entity.setDeltaMovement(vec3.x / 2.0 - vec32.x, entity.onGround() ? Math.min(0.4, vec3.y / 2.0 + d) : vec3.y, vec3.z / 2.0 - vec32.z);
        entity.hurtMarked = true;
    }
    public static void knockbackWithoutBumpUp(Entity entity, double d, double e, double f) {
        if (entity instanceof LivingEntity le && (d *= 1.0 - le.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) <= 0.0) {
            return;
        }
        entity.hasImpulse = true;
        Vec3 vec3 = entity.getDeltaMovement();
        Vec3 vec32 = new Vec3(e, 0.0, f).normalize().scale(d);
        entity.setDeltaMovement(vec3.x / 2.0 - vec32.x, vec3.y, vec3.z / 2.0 - vec32.z);
        entity.hurtMarked = true;
    }

    public static double lengthdir_x(double length, double angle) {
        return length * (Math.cos(toRadians(angle))) * -1;
    }

    public static double lengthdir_z(double length, double angle) {
        return length * (Math.sin(toRadians(angle)));
    }

    public static double toRadians(double angle) {
        return angle * (Math.PI / 180);
    }

    public static double cheapDistanceTo(double x,double y,double z,double x2,double y2,double z2){
        double mdist = 0;
        double cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(y-y2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }
    public static double cheapDistanceTo2(double x,double z,double x2,double z2){
        double mdist = 0;
        double cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }
    public static int cheapDistanceTo2(int x,int z,int x2,int z2){
        int mdist = 0;
        int cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }

    public static boolean isPlayerNearby(Vec3 pos, Level level, double range, int exemptID) {
        if (level instanceof ServerLevel) {
            ServerLevel serverWorld = ((ServerLevel) level);
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = serverWorld.players().get(j);

                if (serverPlayerEntity.level() != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (serverPlayerEntity.getId() != exemptID && blockPos.closerToCenterThan(pos, range)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void gasExplode(BlockState blk, ServerLevel level, BlockPos blkPos, int iteration, int hitRadius, int blockRadius, float power){
        if (!level.isClientSide){
            level.sendParticles(ParticleTypes.LAVA, blkPos.getX() + 0.5, blkPos.getY(), blkPos.getZ() + 0.5,
                    2, 0.0, 0.0, 0.0, 0.4);
            if (iteration == 0){
                SoundEvent $$6 = ModSounds.GASOLINE_EXPLOSION_EVENT;
                level.playSound(null, blkPos, $$6, SoundSource.BLOCKS, 10F, 1F);
                level.sendParticles(ParticleTypes.EXPLOSION, blkPos.getX()+0.5F, blkPos.getY()+0.5F, blkPos.getZ()+0.5F,
                        1, 0.1, 0.1, 0.1, 0.2);
            }

            List<Entity> entities = MainUtil.hitboxGas(MainUtil.genHitbox(level, blkPos.getX(), blkPos.getY(),
                    blkPos.getZ(), hitRadius, hitRadius+1, hitRadius));
            if (!entities.isEmpty()) {
                DamageSource $$5 = ModDamageTypes.of(level,ModDamageTypes.GASOLINE_EXPLOSION);
                for (Entity value : entities) {
                    if (value instanceof GasolineCanEntity){
                        value.remove(Entity.RemovalReason.DISCARDED);
                        gasExplode(null, level, value.getOnPos(), iteration + 1, 1, blockRadius, power);
                        break;
                    }
                    if (!value.fireImmune()) {
                        value.setSecondsOnFire(15);
                        float np = power;
                        if (value instanceof LivingEntity){
                            ((StandUser)value).roundabout$setGasolineTime(-1);
                            if (value instanceof Player){
                                np *= 0.31F;
                            }
                            int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, (LivingEntity) value);
                            np = (float) (np * (1-(f*0.045)));
                        }
                        if (value instanceof LivingEntity && ((LivingEntity)value).hasEffect(MobEffects.FIRE_RESISTANCE)){
                            MobEffectInstance instance = ((LivingEntity)value).getEffect(MobEffects.FIRE_RESISTANCE);
                            ((LivingEntity)value).removeEffect(MobEffects.FIRE_RESISTANCE);
                            value.hurt($$5,np);
                            ((LivingEntity)value).addEffect(instance);
                        } else {
                            value.hurt($$5,np);
                        }
                    }
                }
            }
        }

        if (blk != null) {
            level.removeBlock(blkPos, false);
        }

        Set<BlockPos> gasList = Sets.newHashSet();
        if (!level.isClientSide && iteration < 8) {
            for (int x = -blockRadius; x < blockRadius; x++) {
                for (int y = -blockRadius; y < blockRadius; y++) {
                    for (int z = -blockRadius; z < blockRadius; z++) {
                        BlockPos blkPo2 = new BlockPos(blkPos.getX() + x, blkPos.getY()+y, blkPos.getZ() + z);
                        BlockState state = level.getBlockState(blkPo2);
                        Block block = state.getBlock();

                        if (block instanceof GasolineBlock) {
                            boolean ignited = state.getValue(GasolineBlock.IGNITED);
                            if (!ignited){
                                state = state.setValue(GasolineBlock.IGNITED, Boolean.valueOf(true));
                                level.setBlock(blkPo2, state, 1);
                                gasList.add(blkPo2);
                            }
                        }
                    }
                }
            }
            if (!gasList.isEmpty()) {
                for (BlockPos gasPuddle : gasList) {
                    BlockState state = level.getBlockState(gasPuddle);
                    gasExplode(state, level, gasPuddle, iteration + 1, hitRadius, blockRadius, power);
                }
            }
        }


    }


    /**For new Locacaca mechanics and rendering*/
    public static LivingEntity getStoneTarget(Level $$0, LivingEntity $$1){
        List<Entity> entities = MainUtil.hitbox(MainUtil.genHitbox($$0, $$1.getX(), $$1.getY(),
                $$1.getZ(), 5, 5, 5));
        double maxDistance = 5;
        LivingEntity target = null;
        if (!entities.isEmpty()) {
            for (Entity value : entities) {
                if (value instanceof LivingEntity && value.getUUID() != $$1.getUUID() && !(value instanceof StandEntity)) {
                    double distance = value.position().distanceTo($$1.position());
                    if (distance <= maxDistance && ((StandUser)value).roundabout$getLocacacaCurse() < 0){
                        target = (LivingEntity) value;
                        maxDistance = distance;
                    }
                }
            }
        }

        return target;
    }

    public static Entity getTargetEntity(LivingEntity User, float distance){
        return getTargetEntity(User,distance,25);
    }
    public static Entity getTargetEntity(LivingEntity User, float distance, int angle){
        /*First, attempts to hit what you are looking at*/
            getDistanceOut(User, distance, false);
        Entity targetEntity = rayCastEntity(User,distance);
        /*If that fails, attempts to hit the nearest entity in a spherical radius in front of you*/
        if (targetEntity == null) {
            float halfReach = (float) (distance*0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(User, halfReach);
            targetEntity = AttackHitboxNear(User, GrabHitbox(User, DamageHandler.genHitbox(User, pointVec.x, pointVec.y,
                    pointVec.z, halfReach, halfReach, halfReach), distance, angle),distance);
        }
        return targetEntity;
    }

    public static float getDistanceOut(LivingEntity entity, float range, boolean offset){
        float distanceFront = getRayDistance(entity, range);
        if (offset) {
            Entity targetEntity = rayCastEntity(entity,range);
            if (targetEntity != null && targetEntity.distanceTo(entity) < distanceFront) {
                distanceFront = targetEntity.distanceTo(entity);
            }
            distanceFront -= 1;
            distanceFront = Math.max(Math.min(distanceFront, 1.7F), 0.4F);
        }
        return distanceFront;
    }

    public static float getRayDistance(LivingEntity entity, float range){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        HitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        if (blockHit.getType() != HitResult.Type.MISS){
            return Mth.sqrt((float) entity.distanceToSqr(blockHit.getLocation()));
        }
        return range;
    }


    public static boolean isThrownBlockItem(Item item){
        if (item instanceof BlockItem){
            Block blk = ((BlockItem)item).getBlock();
            if (item instanceof ItemNameBlockItem || blk instanceof BushBlock
                    || blk instanceof WebBlock  || blk instanceof BarbedWireBundleBlock
                    || blk instanceof TorchBlock
                    || blk instanceof FrogspawnBlock
                    || blk instanceof CauldronBlock
                    || blk instanceof BellBlock
                    || blk instanceof SnowLayerBlock
                    || blk instanceof TurtleEggBlock
                    || blk instanceof CarpetBlock
                    || blk instanceof SugarCaneBlock
                    || blk instanceof GrowingPlantHeadBlock
                    || blk instanceof StereoBlock
                    || blk instanceof FlowerPotBlock
                    || blk instanceof DaylightDetectorBlock
                    || blk instanceof VineBlock
                    || blk instanceof SnifferEggBlock
                    || blk instanceof BasePressurePlateBlock
                    || blk instanceof PowderSnowBlock
                    || blk instanceof SkullBlock
                    || blk instanceof RodBlock
                    || blk instanceof HorizontalDirectionalBlock
                    || (blk instanceof SimpleWaterloggedBlock && !(blk instanceof LeavesBlock))){
                return false;
            }
            return true;
        }
        return false;
    }

    public static List<Entity> GrabHitbox(LivingEntity User, List<Entity> entities, float maxDistance, int angle){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if (!value.showVehicleHealth() || value.isInvulnerable() || !value.isAlive() || (User.isPassenger() && User.getVehicle().getUUID() == value.getUUID())){
                hitEntities.remove(value);
            } else {
                if (!(angleDistance(getLookAtEntityYaw(User, value), (User.getYHeadRot()%360f)) <= angle && angleDistance(getLookAtEntityPitch(User, value), User.getXRot()) <= angle)){
                    hitEntities.remove(value);
                }
            }
        }
        return hitEntities;
    }

    public static int getTargetEntityId(LivingEntity User, float distance){
        return getTargetEntityId(User,distance,25);
    }

    public static int getTargetEntityId(LivingEntity User, float distance, int angle){
        Entity targetEntity = getTargetEntity(User, distance, angle);
        int id;
        if (targetEntity != null) {
            id = targetEntity.getId();
        } else {
            id = -1;
        }
        return id;
    }
    public static boolean isStandDamage(DamageSource sauce){
        if (sauce.is(ModDamageTypes.STAND) || sauce.is(ModDamageTypes.PENETRATING_STAND) || sauce.is(ModDamageTypes.STAR_FINGER)){
            return true;
        }
        return false;
    }


    /**Returns the vertical angle between two mobs*/
    public static float getLookAtEntityPitch(Entity user, Entity targetEntity) {
        double f;
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        if (targetEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)targetEntity;
            f = livingEntity.getEyeY() - user.getEyeY();
        } else {
            f = (targetEntity.getBoundingBox().minY + targetEntity.getBoundingBox().maxY) / 2.0 - user.getEyeY();
        }
        double g = Math.sqrt(d * d + e * e);
        return (float)(-(Mth.atan2(f, g) * 57.2957763671875));
    }

    /**Returns the horizontal angle between two mobs*/
    public static float getLookAtEntityYaw(Entity user, Entity targetEntity) {
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        return (float)(Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
    }

    public static float angleDistance(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 360;       // This is either the distance or 360 - distance
        float distance = phi > 180 ? 360 - phi : phi;
        return distance;
    }

    public static Entity AttackHitboxNear(Entity User, List<Entity> entities, float distance) {
        float nearestDistance = -1;
        Entity nearestMob = null;

        if (entities != null) {
            for (Entity value : entities) {
                if (!value.isInvulnerable() && value.isAlive() && value.getUUID() != User.getUUID()) {
                    float distanceTo = value.distanceTo(User);
                    if ((nearestDistance < 0 || distanceTo < nearestDistance) && distanceTo <= distance) {
                        nearestDistance = distanceTo;
                        nearestMob = value;
                    }
                }
            }
        }
        return nearestMob;
    }
    public static Entity rayCastEntity(Entity entityX, float reach){
        float tickDelta = 0;
        if (entityX.level().isClientSide()) {
            tickDelta = ClientUtil.getDelta();
        }
        Vec3 vec3d = entityX.getEyePosition(tickDelta);

        Vec3 vec3d2 = entityX.getViewVector(1.0f);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        float f = 1.0f;
        AABB box = new AABB(vec3d.x+reach, vec3d.y+reach, vec3d.z+reach, vec3d.x-reach, vec3d.y-reach, vec3d.z-reach);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(entityX, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.isPickable() && !entity.isInvulnerable(), reach*reach);
        if (entityHitResult != null){
            Entity hitResult = entityHitResult.getEntity();
            if (hitResult.isAlive() && !hitResult.isRemoved()) {
                return hitResult;
            }
        }
        return null;
    }

    /**Guard breaking*/
    public static StandUser getUserData(LivingEntity User){
        return ((StandUser) User);
    }
    public static boolean knockShield(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    StandUser standUser= getUserData((LivingEntity) entity);
                    if (!standUser.roundabout$isGuarding()) {
                        if (entity instanceof Player){
                            ItemStack itemStack = ((LivingEntity) entity).getUseItem();
                            Item item = itemStack.getItem();
                            if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                                ((LivingEntity) entity).releaseUsingItem();
                                ((Player) entity).stopUsingItem();
                            }
                            ((Player) entity).getCooldowns().addCooldown(Items.SHIELD, duration);
                            entity.level().broadcastEntityEvent(entity, EntityEvent.SHIELD_DISABLED);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean knockShieldPlusStand(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    StandUser standUser= getUserData((LivingEntity) entity);
                    if (standUser.roundabout$isGuarding()) {
                        if (!standUser.roundabout$getGuardBroken()){
                            standUser.roundabout$breakGuard();
                        }
                    }
                    if (entity instanceof Player){
                        ItemStack itemStack = ((LivingEntity) entity).getUseItem();
                        Item item = itemStack.getItem();
                        if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                            ((LivingEntity) entity).releaseUsingItem();
                            ((Player) entity).stopUsingItem();
                        }
                        ((Player) entity).getCooldowns().addCooldown(Items.SHIELD, duration);
                        entity.level().broadcastEntityEvent(entity, EntityEvent.SHIELD_DISABLED);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**Code for determining if it is appropriate to place a splatter down*/
    public static boolean canPlaceSplatter(Level level,BlockPos pos, int offsetX, int offsetY, int offsetZ){
        BlockPos blk =  new BlockPos(pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);

        if (level.isEmptyBlock(blk)) {
            BlockPos $$8 = blk.below();
            if (level.getBlockState($$8).isFaceSturdy(level, $$8, Direction.UP)) {
                return true;
            }
        }

        return false;
    }

    public static void setSplatter(Level level,BlockPos pos, int offsetX, int offsetZ, BlockState state){
        BlockPos blockPos = null;
        if (canPlaceSplatter(level, pos, offsetX, +1, offsetZ)) {
            blockPos = new BlockPos(pos.getX() + offsetX, pos.getY() + 1, pos.getZ() + offsetZ);
        } else if (canPlaceSplatter(level, pos, offsetX, +2, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY() + 2,pos.getZ()+offsetZ);
        } else if (canPlaceSplatter(level, pos, offsetX, 0, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY(),pos.getZ()+offsetZ);
        } else if (canPlaceSplatter(level, pos, offsetX, -1, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY()-1,pos.getZ()+offsetZ);
        }
        //if (this.level().getBlockState(pos).getBlock())
        if (blockPos != null) {
            level.setBlockAndUpdate(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()), state);
        }
    }
    private static final TargetingConditions OFFER_TARGER_CONTEXT = TargetingConditions.forNonCombat().range(64.0D);

    public static void handShake(ServerPlayer player){
        Networking.sendConfigToPlayer(player);
    }

    /**A generalized packet for sending bytes to the server. Context is what to do with the data byte*/

    public static void handleBytePacketC2S(Player player, byte data, byte context){
        if (context == PacketDataIndex.BYTE_UPDATE_COOLDOWN) {
            ((StandUser) player).roundabout$getStandPowers().setCooldown(data, -1);
        } else if (context == PacketDataIndex.BYTE_STRIKE_POSE) {
            ((IPlayerEntity) player).roundabout$SetPoseEmote(data);
        } else if (context == PacketDataIndex.BYTE_CORPSE_TACTICS) {
            if (((StandUser) player).roundabout$getStandPowers() instanceof PowersJustice PJ){
                PJ.justiceTacticsUse(data);
            }
        } else if (context == PacketDataIndex.BYTE_CHANGE_MORPH) {
            if (ShapeShifts.getShiftFromByte(data) == ShapeShifts.VILLAGER){
                byte totalMorph = 0;
                Villager ent = player.level().getNearestEntity(Villager.class, OFFER_TARGER_CONTEXT, player, player.getX(), player.getY(), player.getZ(),player.getBoundingBox().inflate(12.0D, 2.0D, 12.0D));
                if (ent != null){
                    VillagerType VT = ent.getVillagerData().getType();
                    VillagerProfession VP = ent.getVillagerData().getProfession();
                    totalMorph = (byte) (ShapeShifts.getByteFromType(VT) + ShapeShifts.getByteFromProfession(VP));
                } else {
                    VillagerType VT = VillagerType.byBiome(player.level().getBiome(player.blockPosition()));
                    totalMorph = (byte) (ShapeShifts.getByteFromType(VT) + 1);
                }
                ((IPlayerEntity) player).roundabout$setShapeShiftExtraData(totalMorph);
            }
            ((IPlayerEntity) player).roundabout$shapeShift();
            ((IPlayerEntity) player).roundabout$setShapeShift(data);
        } else if (context == PacketDataIndex.BYTE_CHANGE_MORPH) {

        }
    }
    /**A generalized packet for sending bytes to the server. Context is what to do with the data byte*/
    public static void handleSingleBytePacketC2S(Player player, byte context){
        if (context == PacketDataIndex.SINGLE_BYTE_SILENT_SUMMON) {
            ((StandUser) player).roundabout$summonStand(player.level(), false, false);

        } else if (context == PacketDataIndex.SINGLE_BYTE_SKIN_LEFT) {
                StandUser user = ((StandUser) player);
                user.roundabout$getStandPowers().getSkinInDirection(false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_SKIN_RIGHT) {
            StandUser user = ((StandUser) player);
            user.roundabout$getStandPowers().getSkinInDirection(true);
        } else if (context == PacketDataIndex.SINGLE_BYTE_IDLE_LEFT) {
            StandUser user = ((StandUser) player);
            user.roundabout$getStandPowers().getPoseInDirection(false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_IDLE_RIGHT){
            StandUser user = ((StandUser) player);
            user.roundabout$getStandPowers().getPoseInDirection(true);
        } else if (context == PacketDataIndex.SINGLE_BYTE_OPEN_POWER_INVENTORY) {
            StandUser standUser = ((StandUser) player);
            standUser.roundabout$getStandPowers().setCooldown(context,-1);
            IPlayerEntity iplay = ((IPlayerEntity) player);
            byte unlocked = 0;
            if (iplay.roundabout$getUnlockedBonusSkin()){
                unlocked = 1;
            }

            ModPacketHandler.PACKET_ACCESS.sendBundlePacket(((ServerPlayer) player), PacketDataIndex.S2C_BUNDLE_POWER_INV,
                    standUser.roundabout$getStandSkin(), unlocked, (byte) 0);
            player.containerMenu = new PowerInventoryMenu(player.getInventory(), !player.level().isClientSide, player);
            ((IPlayerEntityServer)player).roundabout$initMenu(player.containerMenu);
        } else if (context == PacketDataIndex.SINGLE_BYTE_OPEN_FOG_INVENTORY) {
            player.containerMenu = new FogInventoryMenu(player.getInventory(), !player.level().isClientSide, player);
            ((IPlayerEntityServer)player).roundabout$initMenu(player.containerMenu);
        } else if (context == PacketDataIndex.SINGLE_BYTE_GLAIVE_START_SOUND) {
            ((StandUser) player).roundabout$getStandPowers().playSoundsIfNearby(SoundIndex.GLAIVE_CHARGE, 10, false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_ITEM_STOP_SOUND) {
            ((StandUser) player).roundabout$getStandPowers().stopSoundsIfNearby(SoundIndex.ITEM_GROUP, 30,false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_STAND_ARROW_START_SOUND) {
            ((StandUser) player).roundabout$getStandPowers().playSoundsIfNearby(SoundIndex.STAND_ARROW_CHARGE, 10, false);
        } else if (context == PacketDataIndex.SINGLE_BYTE_SCOPE) {
            if (player != null && ((StandUser)player).roundabout$getStand() instanceof StarPlatinumEntity SE){
                SE.setScoping(true);
                if (ClientNetworking.getAppropriateConfig().starPlatinumScopeUsesPotionEffectForNightVision) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000000, 0, false, false), null);
                }
            }
        } else if (context == PacketDataIndex.SINGLE_BYTE_SCOPE_OFF) {
            if (player != null && ((StandUser)player).roundabout$getStand() instanceof StarPlatinumEntity SE){
                SE.setScoping(false);
                if (ClientNetworking.getAppropriateConfig().starPlatinumScopeUsesPotionEffectForNightVision) {
                    MobEffectInstance ME = player.getEffect(MobEffects.NIGHT_VISION);
                    if (ME != null && ME.getDuration() >= 100000) {
                        player.removeEffect(MobEffects.NIGHT_VISION);
                    }
                }
            }
        } else if (context == PacketDataIndex.SINGLE_BYTE_FORWARD_BARRAGE) {
            if (player != null){
                ((StandUser)player).roundabout$getStandPowers().forwardBarrage = true;
                ((StandUser)player).roundabout$getStandPowers().moveStarted = true;
                ((StandUser)player).roundabout$getStandPowers().poseStand(OffsetIndex.LOOSE);
                StandEntity SE = ((StandUser)player).roundabout$getStand();
                if (SE != null){
                    SE.setPos(player.getPosition(0).add(player.getForward().scale(2)).add(0,0.2F,0));
                    SE.setYRot(player.getYHeadRot() % 360);
                }
            }
        } else if (context == PacketDataIndex.STAND_MOVE_UPDATE) {
            if (player != null) {
                ((StandUser) player).roundabout$getStandPowers().updateMove();
            }
        }
    }

    /**A generalized packet for sending floats to the server. Context is what to do with the data byte*/
    public static void handleFloatPacketC2S(Player player, float data, byte context){
        if (context == PacketDataIndex.FLOAT_VELOCITY_BARBED_WIRE) {
            if (player.getVehicle() != null){
                if (player.getVehicle().hurt(ModDamageTypes.of(player.level(), ModDamageTypes.BARBED_WIRE), data)){
                    MainUtil.makeBleed(player.getVehicle(),0,200,null);
                }
            } else {
                if (player.hurt(ModDamageTypes.of(player.level(), ModDamageTypes.BARBED_WIRE), data)){
                    MainUtil.makeBleed(player,0,200,null);
                }
            }
        } else if (context == PacketDataIndex.FLOAT_STAR_FINGER_SIZE) {
            if (((StandUser)player).roundabout$getStand() instanceof StarPlatinumEntity SP){
                SP.setFingerLength(data);
            }
        } else if (context == PacketDataIndex.FLOAT_DISTANCE_OUT) {
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setDistanceOut(data);
        } else if (context == PacketDataIndex.FLOAT_SIZE_PERCENT) {
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setSizePercent(data);
        } else if (context == PacketDataIndex.FLOAT_IDLE_ROTATION) {
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setIdleRotation(data);
        } else if (context == PacketDataIndex.FLOAT_IDLE_Y_OFFSET) {
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setIdleYOffset(data);
        } else if (context == PacketDataIndex.FLOAT_UPDATE_STAND_MOVE) {
            ((StandUser)player).roundabout$getStandPowers().updateMove(data);
        }
    }
    @SuppressWarnings("deprecation")
    public static void handleIntPacketC2S(Player player, int data, byte context){
        if (context == PacketDataIndex.INT_GLAIVE_TARGET){
            Entity target = player.level().getEntity(data);

            target.hurt(ModDamageTypes.of(player.level(), ModDamageTypes.GLAIVE), data);
        } else if (context == PacketDataIndex.INT_TS_TIME){
            if (((StandUser)player).roundabout$getStandPowers().getChargedTSTicks() > data) {
                ((StandUser)player).roundabout$getStandPowers().setChargedTSTicks(data);
            }
        } else if (context == PacketDataIndex.INT_RIDE_TICKS){
            ((StandUser)player).roundabout$setRestrainedTicks(data);
        } else if (context == PacketDataIndex.INT_STAND_ATTACK){
            Entity target = player.level().getEntity(data);
            ((StandUser)player).roundabout$getStandPowers().handleStandAttack(player,target);
        } else if (context == PacketDataIndex.INT_STAND_ATTACK_2){
            Entity target = player.level().getEntity(data);
            ((StandUser)player).roundabout$getStandPowers().handleStandAttack2(player,target);
        } else if (context == PacketDataIndex.INT_ANCHOR_PLACE){
            IPlayerEntity ple = (IPlayerEntity) player;
            ple.roundabout$setAnchorPlace(data);
        } else if (context == PacketDataIndex.INT_UPDATE_MOVE){
            ((StandUser)player).roundabout$getStandPowers().updateIntMove(data);
        } else if (context == PacketDataIndex.INT_UPDATE_PILOT){
            StandEntity SE = ((StandUser)player).roundabout$getStand();
            if (SE != null){
                BlockPos veci3 = BlockPos.containing(new Vec3(SE.getX(), SE.getY() + SE.getEyeHeight(), SE.getZ()));
                BlockState bl3 = SE.level().getBlockState(veci3);
                if (!(bl3.isSolid() && bl3.getBlock().isCollisionShapeFullBlock(bl3,player.level(),veci3))){
                    ((StandUser)player).roundabout$getStandPowers().setPiloting(data);
                }
            } else {
                ((StandUser)player).roundabout$getStandPowers().setPiloting(data);
            }
        }
    }

    public static void syncCooldownsForAttacks(int attackTime, int attackTimeMax, int attackTimeDuring,
                                               byte activePower, byte activePowerPhase, Player pl){

        StandPowers powers = ((StandUser) pl).roundabout$getStandPowers();

        if (powers.activePower != activePower){
            if (activePower == PowerIndex.NONE){
                powers.setAttackTimeDuring(-1);
            } else {
                powers.setAttackTimeDuring(0);
            }
        }
        powers.updateMovesFromPacket(activePower);
        powers.setActivePower(activePower);
        powers.kickStartClient();
    }

}
