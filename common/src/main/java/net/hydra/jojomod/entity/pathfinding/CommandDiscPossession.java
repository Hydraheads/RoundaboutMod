package net.hydra.jojomod.entity.pathfinding;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public final class CommandDiscPossession extends GroundPathfindingStandAttackEntity {
    private static final EntityDataAccessor<Integer> TARGET_ID =
            SynchedEntityData.defineId(CommandDiscPossession.class, EntityDataSerializers.INT);
    private static final int DURATION = 100;
    private static final double ATTACK_RANGE_SQR = 9.0D;
    public static final double MAX_TARGET_DISTANCE_SQR = 1600.0D;
    private boolean attacking;

    public CommandDiscPossession(EntityType<? extends CommandDiscPossession> type, Level level) {
        super(type, level);
        setLifeSpan(DURATION);
        setMaxUpStep(1.5F);
    }

    public CommandDiscPossession(Level level, ServerPlayer player, LivingEntity target) {
        this(ModEntities.COMMAND_DISC_POSSESSION, level);
        setUser(player);
        setCommandTarget(target);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5F)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TARGET_ID, -1);
    }

    public void setCommandTarget(LivingEntity target) {
        entityData.set(TARGET_ID, target.getId());
        setTarget(target);
    }

    @Nullable
    public LivingEntity getCommandTarget() {
        Entity target = level().getEntity(entityData.get(TARGET_ID));
        return target instanceof LivingEntity living ? living : null;
    }

    @Override
    @Nullable
    protected LivingEntity findTarget(LivingEntity user) {
        return getCommandTarget();
    }

    @Override
    protected void addBehaviourGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8D, true));
    }

    @Override
    public void tick() {
        LivingEntity user = getUser();
        LivingEntity target = getCommandTarget();
        if (!level().isClientSide()
                && (!(user instanceof ServerPlayer player) || !player.isAlive() || player.isRemoved()
                || ((StandUser) player).roundabout$getPossessor() != this
                || target == null || !target.isAlive() || target.isRemoved() || target == player
                || target.level() != level() || distanceToSqr(target) > MAX_TARGET_DISTANCE_SQR)) {
            discard();
            return;
        }
        if (level().isClientSide()) setTarget(target);
        super.tick();
        if (level().isClientSide() || isRemoved() || !(user instanceof ServerPlayer player)) return;
        if (player.getVehicle() != this && !player.startRiding(this, true)) {
            discard();
            return;
        }

        getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (player.distanceToSqr(target) > ATTACK_RANGE_SQR || !player.hasLineOfSight(target)) {
            return;
        }

        getNavigation().stop();
        if (player.getAttackStrengthScale(0.5F) >= 1.0F) {
            forcePlayerAttack(player, target);
        }
    }

    public boolean isAttacking() {
        return attacking;
    }

    private void forcePlayerAttack(ServerPlayer player, LivingEntity target) {
        attacking = true;
        try {
            player.swing(InteractionHand.MAIN_HAND, true);
            player.attack(target);
        } finally {
            attacking = false;
        }
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction move) {
        if (passenger == getUser()) {
            move.accept(passenger, getX(), getY(), getZ());
        } else {
            super.positionRider(passenger, move);
        }
    }

    @Override
    public void onEnd() {
        discard();
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide() && standUser instanceof StandUser user
                && user.roundabout$getPossessor() == this) {
            user.roundabout$setPossessor(null);
        }
        super.remove(reason);
    }
}
