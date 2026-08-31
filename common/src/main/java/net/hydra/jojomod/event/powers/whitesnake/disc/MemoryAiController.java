package net.hydra.jojomod.event.powers.whitesnake.disc;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class MemoryAiController {
    private static final ThreadLocal<Boolean> AI_ATTACK = ThreadLocal.withInitial(() -> false);

    private MemoryAiController() {
    }

    public static boolean isAiAttack() {
        return AI_ATTACK.get();
    }

    public static void forcePlayerAttack(ServerPlayer player, LivingEntity target) {
        AI_ATTACK.set(true);
        try {
            player.swing(InteractionHand.MAIN_HAND, true);
            player.attack(target);
        } finally {
            AI_ATTACK.set(false);
        }
    }

    public static void tick(LivingEntity entity) {
        if (entity.level().isClientSide() || !entity.isAlive()) return;
        DiscBearer bearer = (DiscBearer) entity;
        if (!bearer.roundabout$hasMemoryDisc()) return;
        byte personality = bearer.roundabout$getMemoryPersonality();
        if (entity instanceof Mob mob && WhitesnakeDiscUtil.hasForeignMemory(entity)) tickMob(mob, personality);
    }

    public static void tickBlankMob(Mob mob) {
        mob.setTarget(null);
        ((StandUser) mob).roundabout$deeplyRemoveAttackTarget();
        if (mob.getNavigation().isDone() && mob.getRandom().nextInt(20) == 0) {
            double angle = mob.getRandom().nextDouble() * Math.PI * 2.0D;
            double distance = 3.0D + mob.getRandom().nextDouble() * 7.0D;
            Vec3 destination = mob.position().add(Math.cos(angle) * distance,
                    mob.getRandom().nextInt(5) - 2, Math.sin(angle) * distance);
            mob.getNavigation().moveTo(destination.x, destination.y, destination.z, 0.7D);
        }
        if (mob.getRandom().nextInt(40) == 0) {
            float yaw = mob.getYRot() + mob.getRandom().nextFloat() * 120.0F - 60.0F;
            mob.getLookControl().setLookAt(mob.getX() - Mth.sin(yaw * Mth.DEG_TO_RAD) * 4.0D,
                    mob.getEyeY(), mob.getZ() + Mth.cos(yaw * Mth.DEG_TO_RAD) * 4.0D);
        }
    }

    private static void tickMob(Mob mob, byte personality) {
        if (mob.isNoAi()) mob.setNoAi(false);
        if (personality == MemoryPersonality.HORSE) {
            mob.setTarget(null);
            return;
        }
        LivingEntity tameOwner = getTameOwner(mob);
        LivingEntity target = tameOwner == null ? chooseTarget(mob, personality)
                : chooseTameTarget(mob, tameOwner);
        if (target == mob) target = null;
        if (tameOwner != null && target == null) {
            mob.setTarget(null);
            double distance = mob.distanceToSqr(tameOwner);
            if (distance > 9.0D) mob.getNavigation().moveTo(tameOwner, distance > 144.0D ? 1.25D : 1.0D);
            else mob.getNavigation().stop();
            return;
        }
        if (tameOwner == null && (personality == MemoryPersonality.PASSIVE
                || personality == MemoryPersonality.PLAYER)) {
            mob.setTarget(null);
            return;
        }
        if (target == null) {
            mob.setTarget(null);
            return;
        }
        if (personality == MemoryPersonality.CREEPER && !(mob instanceof Creeper)) {
            mob.setTarget(null);
        } else {
            mob.setTarget(target);
        }
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.getNavigation().moveTo(target, 1.05D);
        double reach = mob.getBbWidth() + target.getBbWidth() + 1.0D;
        if (personality != MemoryPersonality.CREEPER
                && mob.distanceToSqr(target) <= reach * reach && mob.tickCount % 20 == 0) {
            mob.swing(InteractionHand.MAIN_HAND);
            target.hurt(mob.damageSources().mobAttack(mob), 3.0F);
        }
    }

    private static LivingEntity getTameOwner(LivingEntity entity) {
        String ownerId = ((DiscBearer) entity).roundabout$getMemoryTameOwnerId();
        if (ownerId == null || ownerId.isEmpty() || !(entity.level() instanceof ServerLevel server)) return null;
        try {
            UUID id = UUID.fromString(ownerId);
            Entity owner = server.getEntity(id);
            if (!(owner instanceof LivingEntity)) owner = server.getServer().getPlayerList().getPlayer(id);
            return owner instanceof LivingEntity living && living.isAlive() ? living : null;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static LivingEntity chooseTameTarget(LivingEntity entity, LivingEntity owner) {
        LivingEntity target = owner.getLastHurtMob();
        if (target != null && target != entity && target.isAlive()) return target;
        target = owner.getLastHurtByMob();
        return target != null && target != entity && target.isAlive() ? target : null;
    }

    public static boolean isTameMemoryTarget(LivingEntity entity, LivingEntity target) {
        LivingEntity owner = getTameOwner(entity);
        return owner != null && chooseTameTarget(entity, owner) == target;
    }

    private static LivingEntity chooseTarget(LivingEntity entity, byte personality) {
        if (personality == MemoryPersonality.NEUTRAL) {
            LivingEntity attacker = entity.getLastHurtByMob();
            return attacker != null && attacker.isAlive() ? attacker : null;
        }
        if (personality != MemoryPersonality.HOSTILE && personality != MemoryPersonality.ZOMBIE
                && personality != MemoryPersonality.CREEPER) return null;
        double range = 24.0D;
        if (WhitesnakeDiscUtil.canCarrySightDisc(entity)
                && !((DiscBearer) entity).roundabout$hasSightDisc()) {
            LivingEntity attacker = entity.getLastHurtByMob();
            if (attacker != null && attacker.isAlive()) return attacker;
            AttributeInstance followRange = entity.getAttribute(Attributes.FOLLOW_RANGE);
            range = (followRange == null ? range : followRange.getValue()) * 0.07D;
        }
        LivingEntity nearest = null;
        double nearestDistance = range * range;
        for (Player candidate : entity.level().players()) {
            if (candidate == entity || candidate.isCreative() || candidate.isSpectator() || !candidate.isAlive()) {
                continue;
            }
            double distance = entity.distanceToSqr(candidate);
            if (distance <= nearestDistance) {
                nearest = candidate;
                nearestDistance = distance;
            }
        }
        if (personality == MemoryPersonality.ZOMBIE) {
            for (Villager candidate : entity.level().getEntitiesOfClass(Villager.class,
                    entity.getBoundingBox().inflate(range), LivingEntity::isAlive)) {
                if (candidate == entity) continue;
                double distance = entity.distanceToSqr(candidate);
                if (distance <= nearestDistance) {
                    nearest = candidate;
                    nearestDistance = distance;
                }
            }
        }
        return nearest;
    }

}
