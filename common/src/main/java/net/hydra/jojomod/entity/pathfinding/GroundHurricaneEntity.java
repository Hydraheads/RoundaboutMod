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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class GroundHurricaneEntity extends PathfinderMob {
    public GroundHurricaneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public GroundHurricaneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1, LivingEntity user) {
        super($$0, $$1);
        this.setUser(user);
    }

    int lifeSpan = 300;
    public GroundHurricaneEntity(Level $$1, LivingEntity user) {
        super(ModEntities.GROUND_HURRICANE, $$1);
        this.setUser(user);
    }
    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.3F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
    public void setSize(int idd) {
        this.getEntityData().set(SIZE, idd);
    }
    public int getSize() {
        return this.getEntityData().get(SIZE);
    }
    private static final EntityDataAccessor<Integer> SIZE =
            SynchedEntityData.defineId(GroundHurricaneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(GroundHurricaneEntity.class, EntityDataSerializers.INT);

    public LivingEntity standUser;
    public UUID standUserUUID;
    public boolean fireStormCreated = false;
    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }
    public LivingEntity getUser(){
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            return LE;
        }
        return null;
    }
    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }
    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }
    public boolean isEffectivelyInWater() {
        return this.wasTouchingWater;
    }
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.addBehaviourGoals();
    }
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 0);
        this.entityData.define(USER_ID, -1);
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
        if (!client) {
            if ($$0 != null) {
                if (MainUtil.cheapDistanceTo2(this.getX(), this.getZ(), this.standUser.getX(), this.standUser.getZ()) > 80
                        || !this.getUser().isAlive() || this.getUser().isRemoved()) {
                    this.discard();
                } else {
                    LivingEntity ent = null;
                    LivingEntity hurt = $$0.getLastHurtMob();
                    LivingEntity hurtBy = $$0.getLastHurtByMob();
                    if (hurt != null && !hurt.isRemoved() && hurt.isAlive()){
                        ent = hurt;
                    } else if (hurtBy != null && !hurtBy.isRemoved() && hurtBy.isAlive()){
                        ent = hurtBy;
                    }
                   this.setTarget(ent);
                    if (lifeSpan > 0){
                        lifeSpan--;
                    } else {
                        this.discard();
                    }
                }
            } else {
                this.discard();
            }
        }
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
