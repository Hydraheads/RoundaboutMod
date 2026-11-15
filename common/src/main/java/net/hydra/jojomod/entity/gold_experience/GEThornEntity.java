package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GEThornEntity extends Entity {
    private static final EntityDataAccessor<Boolean> IS_WILTING = SynchedEntityData.defineId(GEThornEntity.class, EntityDataSerializers.BOOLEAN);

    private UUID ownerUUID;
    private final static int MAX_DESPAWN_TIME = 30;
    private int despawnTimer = MAX_DESPAWN_TIME;
    private LivingEntity owner;
    private final static int WILT_TICKS = 5;
    public final AnimationState growAnimationState = new AnimationState();
    public final AnimationState wiltAnimationState = new AnimationState();

    public GEThornEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            if (!this.entityData.get(IS_WILTING)) {
                LivingEntity caster = getOwner();
                UUID casterUUID = this.ownerUUID != null ? this.ownerUUID : (caster != null ? caster.getUUID() : null);
                Set<LivingEntity> damagedVictims = new HashSet<>();

                for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
                    if (!target.isAlive() || target.isSpectator()) continue;
                    if (PowerTypes.isInADifferentExistence(target, this)) continue;
                    if (target.invulnerableTime > 10) continue;

                    // user exclusion
                    if (caster != null && target.is(caster)) continue;
                    if (casterUUID != null && casterUUID.equals(target.getUUID())) continue;

                    // stand pair to user stuff
                    if (target instanceof StandEntity stand) {
                        LivingEntity standUser = stand.getUser();
                        if (standUser != null) {
                            if (caster != null && standUser.is(caster)) continue;
                            if (casterUUID != null && casterUUID.equals(standUser.getUUID())) continue;
                        }
                    }

                    LivingEntity actualVictim = (target instanceof StandEntity stand && stand.getUser() != null)
                            ? stand.getUser()
                            : target;
                    if (damagedVictims.contains(actualVictim)) continue;

                    Vec3 prevVelocity = target.getDeltaMovement();
                    if (DamageHandler.StandDamageEntity(target, 0.6F, caster)) {
                        target.setDeltaMovement(prevVelocity);
                        target.hurtMarked = true;
                        damagedVictims.add(actualVictim);
                    }
                }
            }

            boolean groundBarrageActive = false;
            if (this.ownerUUID != null && getOwner() instanceof StandUser standUser) {
                if (standUser.roundabout$getStandPowers() instanceof PowersGoldExperience gePowers) {
                    groundBarrageActive = gePowers.isGroundBarraging();
                }
            }

            if (groundBarrageActive) {
                this.despawnTimer = MAX_DESPAWN_TIME;
                this.entityData.set(IS_WILTING, false);
            } else {
                this.despawnTimer--;
                if (this.despawnTimer <= WILT_TICKS) {
                    this.entityData.set(IS_WILTING, true);
                }
                if (this.despawnTimer <= 0) {
                    this.discard();
                }
            }
        } else {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.entityData.get(IS_WILTING)) {
            this.growAnimationState.stop();
            this.wiltAnimationState.startIfStopped(this.tickCount);
        } else {
            this.growAnimationState.startIfStopped(this.tickCount);
            this.wiltAnimationState.stop();
        }
    }

    @Nullable
    private LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity ent = serverLevel.getEntity(this.ownerUUID);
            if (ent instanceof LivingEntity living) {
                this.owner = living;
            }
        }
        return this.owner;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_WILTING, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
        }
        this.despawnTimer = compoundTag.getInt("DespawnTimer");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }
        compoundTag.putInt("DespawnTimer", this.despawnTimer);
    }
}