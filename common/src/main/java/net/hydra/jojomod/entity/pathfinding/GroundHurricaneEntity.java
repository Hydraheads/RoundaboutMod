package net.hydra.jojomod.entity.pathfinding;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.ConcealedFlameObjectEntity;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class GroundHurricaneEntity extends GroundPathfindingStandAttackEntity {
    public GroundHurricaneEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public GroundHurricaneEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1, LivingEntity user) {
        super($$0, $$1);
        this.setUser(user);
    }
    public GroundHurricaneEntity(Level $$1, LivingEntity user) {
        super(ModEntities.GROUND_HURRICANE, $$1);
        this.setUser(user);
    }

    public void setSize(int idd) {
        this.getEntityData().set(SIZE, idd);
    }
    public int getSize() {
        return this.getEntityData().get(SIZE);
    }
    private static final EntityDataAccessor<Integer> SIZE =
            SynchedEntityData.defineId(GroundHurricaneEntity.class, EntityDataSerializers.INT);
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
    public void burst(PowersMagiciansRed PMR){
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.blockPosition(), ModSounds.CROSSFIRE_EXPLODE_EVENT,
                    SoundSource.PLAYERS, 2F, 1F);

            if (fireStormCreated){
                ((ServerLevel) this.level()).sendParticles(PMR.getFlameParticle(), this.getX(),
                        this.getY() + 0.5, this.getZ(),
                        250,
                        0.01, 0.01, 0.01,
                        0.15);
            } else {
                ((ServerLevel) this.level()).sendParticles(PMR.getFlameParticle(), this.getX(),
                        this.getY() + 0.5, this.getZ(),
                        200,
                        0.01, 0.01, 0.01,
                        0.1);
            }
        }
    }
    @Override
    public boolean doHurtTarget(Entity $$0) {
        LivingEntity user = this.getUser();
        if (user != null &&
                ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
            burst(PMR);
            if ($$0 instanceof LivingEntity LE){
                PMR.addEXP(7,LE);

                MainUtil.knockShieldPlusStand($$0, 20);
            }

            CrossfireHurricaneEntity.blastEntity($$0, this,
                    this.getSize(), user, true, PMR,fireStormCreated);
        }
        this.discard();
        return true;
    }

    @Override
    public void tick() {
        boolean client = this.level().isClientSide();
        LivingEntity $$0 = this.getUser();
        super.tick();
        if (!client && this.tickCount %2 == 0) {
            if ($$0 != null && ((StandUser) $$0).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                Vec3 $$2 = this.getDeltaMovement();
                double $$3 = this.getX() + $$2.x;
                double $$4 = this.getY() + $$2.y;
                double $$5 = this.getZ() + $$2.z;
                float xrand = (float) (Math.random()*1 - 0.5);
                float zrand = (float) (Math.random()*1 - 0.5);
                this.level().addParticle(PMR.getFlameParticle(), $$3, $$4 + 0.5, $$5, 0.0, 0.0, 0.0);

                ((ServerLevel) this.level()).sendParticles(PMR.getFlameParticle(), $$3,
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
