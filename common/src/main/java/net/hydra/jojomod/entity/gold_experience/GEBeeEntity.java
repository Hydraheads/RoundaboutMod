package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

public class GEBeeEntity extends Bee implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;
    private int lifespan = 600;

    private ItemStack sourceItem = ItemStack.EMPTY;

    public GEBeeEntity(EntityType<? extends Bee> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 60, false);
        this.navigation = new FlyingPathNavigation(this, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.lifespan > 0) {
                this.lifespan--;
                gotoTarget();
            } else {
                this.die(this.damageSources().generic());
            }
        }
    }

    private void gotoTarget() {
        LivingEntity target = findTarget();

        if (target != null) {
            // pathfinds to the nearest target
            if (this.tickCount % 3 == 0) {
                this.getNavigation().moveTo(target, 1.0D);
            }

            Path path = this.getNavigation().getPath();
            if (path != null && !path.isDone()) {
                //normal bee flight was too slow, so i'm hijacking the pathfinding list and moving the bee really fast manually
                Vec3 nextWaypoint = path.getNextEntityPos(this);
                Vec3 flightDir = nextWaypoint.subtract(this.position()).normalize();
                this.setDeltaMovement(flightDir.scale(0.45D));
            }

            this.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());

            // reverts into an item
            if (this.distanceToSqr(target) <= 1.44D) {
                this.die(this.damageSources().generic());
            }
        } else {
            // lobotomizes the bee if there's no target
            if (this.tickCount % 10 == 0) {
                this.getNavigation().stop();
                this.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    @Nullable
    private LivingEntity findTarget() {
        if (this.creator == null) {
            return null;
        }

        // last entity that the creator hit
        LivingEntity lastHurt = this.creator.getLastHurtMob();
        if (lastHurt != null && lastHurt.isAlive() && isValidTarget(lastHurt)) {
            return lastHurt;
        }

        // last entity that hit the creator
        LivingEntity attacker = this.creator.getLastHurtByMob();
        if (attacker != null && attacker.isAlive() && isValidTarget(attacker)) {
            return attacker;
        }

        // fallback using bat nearest mob tracking, in case none of the above are valid
        double closestDist = Double.MAX_VALUE;
        LivingEntity closestTarget = null;

        for (LivingEntity entity : this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(24.0D),
                this::isValidTarget)) {

            double dist = this.distanceToSqr(entity);
            if (dist < closestDist) {
                closestDist = dist;
                closestTarget = entity;
            }
        }

        return closestTarget;
    }

    private boolean isValidTarget(LivingEntity entity) {
        return entity != this
                && entity != this.creator
                && entity.isAlive()
                && (entity instanceof Mob || entity instanceof Player)
                && !(entity instanceof GELifeform)
                && !(entity instanceof StandEntity);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // lobotomize the bee so that it won't try to fight the target pathfinding
        this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
    }

    @Override
    public boolean isReflectingDamage() { return isReflectingDamage; }
    @Override
    public void setReflectingDamage(boolean reflecting) { this.isReflectingDamage = reflecting; }
    @Override
    @Nullable
    public LivingEntity getCreator() { return creator; }
    @Override
    public void setCreator(@Nullable LivingEntity creator) { this.creator = creator; }
    @Override
    public int getLifespan() { return lifespan; }
    @Override
    public void setLifespan(int ticks) { this.lifespan = ticks; }
    @Override
    public ItemStack getSourceItem() { return sourceItem; }
    @Override
    public void setSourceItem(ItemStack stack) { this.sourceItem = stack; }
}