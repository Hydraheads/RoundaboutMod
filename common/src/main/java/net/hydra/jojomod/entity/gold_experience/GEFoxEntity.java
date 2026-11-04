package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GEFoxEntity extends Fox implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;
    private int lifespan = 600;
    private ItemStack sourceItem = ItemStack.EMPTY;
    private int jumpCooldown = 0;

    public GEFoxEntity(EntityType<? extends Fox> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.jumpCooldown > 0) {
                this.jumpCooldown--;
            }

            if (this.lifespan > 0) {
                this.lifespan--;
                retrieveItem();
            } else {
                this.die(this.damageSources().generic());
            }
        }
    }

    private void retrieveItem() {
        Entity target = findTarget();
        if (target != null) {
            double dx = target.getX() - this.getX();
            double dz = target.getZ() - this.getZ();
            double dy = Math.abs(target.getY() - this.getY());
            double horizDistSq = dx * dx + dz * dz;

            if (this.tickCount % 2 == 0 && !this.isPouncing()) {
                this.getNavigation().moveTo(target, 1.25D);
            }

            // high jump detection
            Path path = this.getNavigation().getPath();
            boolean needsToJump = false;

            if (dy > 0.6D) {
                if (this.horizontalCollision) {
                    needsToJump = true;
                } else if (path != null && !path.isDone()) {
                    Vec3 nextWaypoint = path.getNextEntityPos(this);
                    if (nextWaypoint.y > this.getY() + 0.5D) {
                        needsToJump = true;
                    }
                } else if (target.getY() > this.getY() + 0.6D && horizDistSq <= 16.0D) {
                    needsToJump = true;
                }
            } else if (this.horizontalCollision) {
                needsToJump = true;
            }

            if (needsToJump && this.onGround() && !this.isPouncing() && this.jumpCooldown <= 0) {
                this.setIsPouncing(true);
                this.jumpCooldown = 20;

                Vec3 dir = target.position().subtract(this.position()).normalize();
                double jumpHeight = Math.min(1.0D, Math.max(0.65D, (target.getY() - this.getY()) * 0.35D + 0.5D));
                this.setDeltaMovement(dir.x * 0.5D, jumpHeight, dir.z * 0.5D);
                this.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
            }

            if (this.isPouncing()) {
                Vec3 velocity = this.getDeltaMovement();
                double horizontalDistance = velocity.horizontalDistance();

                float targetYaw = (float)(Mth.atan2(-velocity.x, velocity.z) * (double)(180F / (float)Math.PI));
                this.setYRot(targetYaw);
                this.yBodyRot = targetYaw;
                this.yHeadRot = targetYaw;

                float targetPitch = (float)(-Mth.atan2(velocity.y, horizontalDistance) * (double)(180F / (float)Math.PI));
                this.setXRot(targetPitch);

                if (this.onGround() && velocity.y <= 0.0D) {
                    this.setIsPouncing(false);
                }
            }

            if (horizDistSq <= 4.0D && dy <= 1.5D) {
                this.setIsPouncing(false);

                if (target instanceof ItemEntity itemEntity) {
                    // picks up items
                    ItemStack stackToPickup = itemEntity.getItem().copy();
                    this.setItemSlot(EquipmentSlot.MAINHAND, stackToPickup);
                    itemEntity.discard();
                    this.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
                } else if (target == this.creator) {
                    // when it reaches the user, KILL ITSELF!!!!
                    this.die(this.damageSources().generic());
                }
            }
        } else {
            // fox lobootmy corportation
            if (this.tickCount % 10 == 0) {
                this.getNavigation().stop();
                this.setDeltaMovement(Vec3.ZERO);
                this.setIsPouncing(false);
            }
        }
    }

    @Nullable
    private Entity findTarget() {
        if (this.creator == null) {
            return null;
        }
        ItemStack heldItem = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!heldItem.isEmpty()) {
            // return the creator as the target if it has a thing
            return this.creator;
        }
        double closestDist = Double.MAX_VALUE;
        ItemEntity closestItem = null;
        // finds nearby items within 30 blocks
        for (ItemEntity entity : this.level().getEntitiesOfClass(
                ItemEntity.class,
                this.getBoundingBox().inflate(30.0D),
                item -> item.isAlive() && !item.getItem().isEmpty())) {
            double dist = this.distanceToSqr(entity);
            if (dist < closestDist) {
                closestDist = dist;
                closestItem = entity;
            }
        }
        return closestItem;
    }

    @Override
    protected void registerGoals() {
        // lobotomize the fox
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