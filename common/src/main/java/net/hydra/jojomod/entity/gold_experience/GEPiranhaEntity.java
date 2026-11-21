package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.mobs.Piranha;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GEPiranhaEntity extends Piranha implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;
    private int lifespan = 600;

    private static final EntityDataAccessor<ItemStack> DATA_SOURCE_ITEM =
            SynchedEntityData.defineId(GEFoxEntity.class, EntityDataSerializers.ITEM_STACK);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SOURCE_ITEM, ItemStack.EMPTY);
    }

    public GEPiranhaEntity(EntityType<? extends Piranha> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
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
                if (this.isInWater()) {
                    this.getNavigation().moveTo(target, 1.5D);
                }
            }

            if (this.isInWater()) {
                //manual movement override for speed
                Path path = this.getNavigation().getPath();
                if (path != null && !path.isDone()) {
                    Vec3 nextWaypoint = path.getNextEntityPos(this);
                    Vec3 swimDir = nextWaypoint.subtract(this.position()).normalize();
                    this.setDeltaMovement(swimDir.scale(0.35D));
                }
            }

            this.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());

            // you literally can't hit the piranha, but it can hit you. i'm making it kill itself after attacking someone
            if (this.distanceToSqr(target) <= 2.0D) {
                this.doHurtTarget(target);
                this.die(this.damageSources().generic());
            }
        } else {
            // lobotomizes the fih if there's no target
            if (this.tickCount % 10 == 0) {
                this.getNavigation().stop();
                if (this.isInWater()) {
                    this.setDeltaMovement(Vec3.ZERO);
                }
            }
        }
    }

    @Nullable
    private LivingEntity findTarget() {
        if (this.getCreator() == null) {
            return null;
        }

        // last entity that the creator hit
        LivingEntity lastHurt = this.getCreator().getLastHurtMob();
        if (lastHurt != null && lastHurt.isAlive() && isValidTarget(lastHurt)) {
            return lastHurt;
        }

        // last entity that hit the creator
        LivingEntity attacker = this.getCreator().getLastHurtByMob();
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
                && (this.creatorUUID != null && !this.creatorUUID.equals(entity.getUUID()))
                && entity.isAlive()
                && (entity instanceof Mob || entity instanceof Player)
                && !(entity instanceof GELifeform)
                && !(entity instanceof StandEntity);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            this.level().broadcastEntityEvent(this, (byte) 4);

            if (target instanceof LivingEntity livingTarget) {
                // what if instead of 10 seconds of bleed, it did 8 seconds of bleed :thinking:
                MainUtil.makeBleed(livingTarget, 0, 200, this);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isReflectingDamage() { return isReflectingDamage; }
    @Override
    public void setReflectingDamage(boolean reflecting) { this.isReflectingDamage = reflecting; }
    @Override
    public int getLifespan() { return lifespan; }
    @Override
    public void setLifespan(int ticks) { this.lifespan = ticks; }
    @Override
    public void setSourceItem(ItemStack stack) {this.entityData.set(DATA_SOURCE_ITEM, stack);}
    @Override
    public ItemStack getSourceItem() {return this.entityData.get(DATA_SOURCE_ITEM);}
    @Nullable
    private UUID creatorUUID;

    @Override
    @Nullable
    public UUID getCreatorUUID() {
        return this.creatorUUID;
    }

    @Override
    public void setCreatorUUID(@Nullable UUID uuid) {
        this.creatorUUID = uuid;
    }

    @Override
    @Nullable
    public LivingEntity getCreator() {
        if (this.creatorUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.creatorUUID);
            if (entity instanceof LivingEntity living) {
                return living;
            }
        }
        return null;
    }

    @Override
    public void setCreator(@Nullable LivingEntity creator) {
        this.creatorUUID = (creator != null) ? creator.getUUID() : null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.creatorUUID != null) {
            tag.putUUID("CreatorUUID", this.creatorUUID);
        }
        tag.putInt("GELifespan", this.lifespan);
        if (!this.getSourceItem().isEmpty()) {
            tag.put("GESourceItem", this.getSourceItem().save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("CreatorUUID")) {
            this.creatorUUID = tag.getUUID("CreatorUUID");
        }
        if (tag.contains("GELifespan")) {
            this.lifespan = tag.getInt("GELifespan");
        }
        if (tag.contains("GESourceItem")) {
            this.setSourceItem(ItemStack.of(tag.getCompound("GESourceItem")));
        }
    }
}