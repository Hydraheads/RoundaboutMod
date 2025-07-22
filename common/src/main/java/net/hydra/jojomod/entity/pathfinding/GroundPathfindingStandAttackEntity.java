package net.hydra.jojomod.entity.pathfinding;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class GroundPathfindingStandAttackEntity extends PathfinderMob {
    public GroundPathfindingStandAttackEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
    int lifeSpan = 300;

    @Override
    protected void checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3) {
    }
    public int getLifeSpan(){
        return lifeSpan;
    }
    public void setLifeSpan(int lifeSpan){
        this.lifeSpan = lifeSpan;
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.3F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(GroundPathfindingStandAttackEntity.class, EntityDataSerializers.INT);

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
        this.entityData.define(USER_ID, -1);
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
    @Override
    public boolean doHurtTarget(Entity $$0) {
        return false;
    }


    @Override
    public void doPush(Entity $$0) {
    }
    @Override
    public void push(Entity $$0) {
    }
    @Override
    public boolean isAttackable() {
        return false;
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
    }
}
