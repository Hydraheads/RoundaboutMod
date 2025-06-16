package net.hydra.jojomod.entity.pathfinding;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetExplosiveBubbleEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class GroundBubbleEntity extends GroundPathfindingStandAttackEntity {
    public GroundBubbleEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public GroundBubbleEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1, LivingEntity user) {
        super($$0, $$1);
        this.setUser(user);
    }
    public GroundBubbleEntity(Level $$1, LivingEntity user) {
        super(ModEntities.GROUND_BUBBLE, $$1);
        this.setUser(user);
    }

    public void setSize(int idd) {
        this.getEntityData().set(SIZE, idd);
    }
    public int getSize() {
        return this.getEntityData().get(SIZE);
    }
    private static final EntityDataAccessor<Integer> SIZE =
            SynchedEntityData.defineId(GroundBubbleEntity.class, EntityDataSerializers.INT);
    public boolean fireStormCreated = false;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 0);
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
    public void burst(PowersSoftAndWet PWW){
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_SHOT_EVENT,
                    SoundSource.PLAYERS, 2F, 0.9F);

            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.level().getBlockState(this.getOnPos())),
                    this.getOnPos().getX()+0.5, this.getOnPos().getY()+0.5, this.getOnPos().getZ()+0.5,
                    30, 0.2, 0.05, 0.2, 0.3);
        }
    }

    @Override
    public void tick() {
        boolean client = this.level().isClientSide();
        LivingEntity $$0 = this.getUser();
        super.tick();
        if (!client) {
            Entity targ = this.getTarget();
            if (targ != null && this.getTarget().distanceTo(this) < 5) {
                if ($$0 != null && ((StandUser) $$0).roundabout$getStandPowers() instanceof PowersSoftAndWet PWW) {
                    SoftAndWetExplosiveBubbleEntity bubble = PWW.getExplosiveBubble();
                    if (bubble != null){
                        bubble.setSped(PWW.getExplosiveSpeed());
                        bubble.setPos(this.position().add(0,0.1F,0));
                        bubble.setMadeWithBarrage(true);
                        bubble.setDeltaMovement(targ.getEyePosition().subtract(bubble.position()).normalize().scale(PWW.getExplosiveSpeed()));
                        PWW.bubbleListInit();
                        PWW.bubbleList.add(bubble);
                        this.level().addFreshEntity(bubble);

                        this.level().playSound(null, this.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_SHOT_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));

                    }
                }
                this.discard();
            } else {
                if (this.tickCount % 2 == 0) {
                    Vec3 $$2 = this.getDeltaMovement();
                    double $$3 = this.getX() + $$2.x;
                    double $$4 = this.getY() + $$2.y;
                    double $$5 = this.getZ() + $$2.z;
                    float xrand = (float) (Math.random() * 1 - 0.5);
                    float zrand = (float) (Math.random() * 1 - 0.5);
                    ((ServerLevel) this.level()).sendParticles(ModParticles.PLUNDER, $$3,
                            $$4 + 0.1, $$5,
                            0,
                            xrand,
                            0.5,
                            zrand,
                            0.1);
                }
            }
        }
    }
}
