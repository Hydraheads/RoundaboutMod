package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GEBrambleEntity extends Entity {
    private static final EntityDataAccessor<Integer> LIFE_TICKS = SynchedEntityData.defineId(GEBrambleEntity.class, EntityDataSerializers.INT);

    private LivingEntity owner;
    private UUID ownerUUID;
    private boolean dealtDamage = false;

    // Life cycle duration in ticks
    public static final int MAX_LIFE = 30;
    public static final int DAMAGE_TICK = 6;
    public static final int WILT_TICKS = 8; // Start wilting 8 ticks before MAX_LIFE

    public final AnimationState growAnimationState = new AnimationState();
    public final AnimationState wiltAnimationState = new AnimationState();

    public GEBrambleEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
        }
    }

    public LivingEntity getOwner() {
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
        this.entityData.define(LIFE_TICKS, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            int currentTicks = this.entityData.get(LIFE_TICKS) + 1;
            this.entityData.set(LIFE_TICKS, currentTicks);

            // Trigger damage impact on DAMAGE_TICK
            if (currentTicks == DAMAGE_TICK && !dealtDamage) {
                triggerImpact();
            }

            // Despawn on server when max life reached
            if (currentTicks >= MAX_LIFE) {
                this.discard();
            }
        } else {
            // Setup client-side animation state updates (matching GoldExperienceEntity logic)
            setupAnimationStates();

            // Client particles
            if (this.getLifeTicks() == DAMAGE_TICK) {
                this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY() + 0.8, this.getZ(), 0.0, 0.2, 0.0);
            }
        }
    }

    private void setupAnimationStates() {
        int ticks = getLifeTicks();

        if (ticks >= (MAX_LIFE - WILT_TICKS)) {
            this.growAnimationState.stop();
            this.wiltAnimationState.startIfStopped(this.tickCount);
        } else {
            this.growAnimationState.startIfStopped(this.tickCount);
            this.wiltAnimationState.stop();
        }
    }

    private void triggerImpact() {
        this.dealtDamage = true;
        this.level().playSound(null, this.blockPosition(), SoundEvents.EVOKER_FANGS_ATTACK, SoundSource.PLAYERS, 0.8F, 1.1F);

        LivingEntity caster = getOwner();
        UUID casterUUID = this.ownerUUID != null ? this.ownerUUID : (caster != null ? caster.getUUID() : null);

        AABB hitBox = this.getBoundingBox().inflate(0.5D, 0.8D, 0.5D);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, hitBox, e -> {
            if (!e.isAlive() || e.isSpectator()) return false;
            //don't hit the user
            if (caster != null && e.is(caster)) return false;
            if (casterUUID != null && casterUUID.equals(e.getUUID())) return false;
            //it took me wayy too long to realize that the reason why you kept getting hit after using this move was because the vines hit GE itself
            if (e instanceof StandEntity stand) {
                LivingEntity standUser = stand.getUser();
                if (standUser != null) {
                    if (caster != null && standUser.is(caster)) return false;
                    if (casterUUID != null && casterUUID.equals(standUser.getUUID())) return false;
                }
            }
            return true;
        });

        //need special logic to exclude someone from being hit twice, because their stand might have gotten hit
        Set<LivingEntity> damagedVictims = new HashSet<>();

        for (LivingEntity target : targets) {
            LivingEntity actualVictim = target;

            if (target instanceof StandEntity stand) {
                if (stand.getUser() != null) {
                    actualVictim = stand.getUser();
                }
            }
            if (!damagedVictims.contains(actualVictim)) {
                if (DamageHandler.StandDamageEntity(target, 3.0F, caster != null ? caster : this)) {
                    MainUtil.makeBleed(actualVictim, 0, 200, null);
                    damagedVictims.add(actualVictim);
                }
            }
        }
    }

    public int getLifeTicks() {
        return this.entityData.get(LIFE_TICKS);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        this.entityData.set(LIFE_TICKS, tag.getInt("LifeTicks"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        tag.putInt("LifeTicks", this.getLifeTicks());
    }
}