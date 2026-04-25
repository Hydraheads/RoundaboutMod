package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class Zombiefish extends PathfinderMob {
    public Entity controller;
    public UUID controller2;
    private static final EntityDataAccessor<Integer> CONTROLLER =
            SynchedEntityData.defineId(Zombiefish.class, EntityDataSerializers.INT);

    public Zombiefish(EntityType<? extends Zombiefish> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    public double getMyRidingOffset() {
        return 0.1;
    }

    @Override
    protected float getStandingEyeHeight(Pose $$0, EntityDimensions $$1) {
        return 0.13F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.33).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos $$0, BlockState $$1) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (this.isInvulnerableTo($$0)) {
            return false;
        } else {

            return super.hurt($$0, $$1);
        }
    }

    public LivingEntity autoTarget;
    public LivingEntity autoTarget2;

    @Override
    public void setYBodyRot(float $$0) {
        this.setYRot($$0);
        super.setYBodyRot($$0);
    }

    @Override
    public float getWalkTargetValue(BlockPos $$0, LevelReader $$1) {
        return InfestedBlock.isCompatibleHostBlock($$1.getBlockState($$0.below())) ? 10.0F : super.getWalkTargetValue($$0, $$1);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void setTarget(@Nullable LivingEntity $$0) {
        if ($$0 != null && controller != null && controller.is($$0)){
            return;
        } else {
            super.setTarget($$0);
        }
    }
    public void setLastHurtByPlayer(@Nullable Player $$0) {
        if ($$0 != null && controller != null && controller.is($$0)){
            return;
        } else {
            super.setLastHurtByPlayer($$0);
        }
    }

    @Override
    public boolean doHurtTarget(Entity $$0) {
        Boolean bool = super.doHurtTarget($$0);
        if (bool){
            if ($$0 instanceof LivingEntity LE){
                LE.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0), this);
            }
        }
        return bool;
    }

    public void setLastHurtByMob(@Nullable LivingEntity $$0) {
        if ($$0 != null && controller != null && controller.is($$0)){
            return;
        } else {
            super.setLastHurtMob($$0);
        }
    }

    public void setLastHurtMob(Entity $$0) {
        if ($$0 != null && controller != null && controller.is($$0)){
            return;
        } else {
            super.setLastHurtMob($$0);
        }
    }

    public Entity getRealController() {
        if (this.controller != null){
            return controller;
        } else{
            int ct = this.getEntityData().get(CONTROLLER);
            if (ct > 0){
                return this.level().getEntity(ct);
            }
        }
        return null;
    }
    public int getController() {
        return this.getEntityData().get(CONTROLLER);
    }

    public void setController(int controller){
        this.entityData.set(CONTROLLER, controller);
    }
    public void setController(Entity controller){
        this.controller = controller;
        if (controller !=null){
            controller2 = controller.getUUID();
            this.entityData.set(CONTROLLER, controller.getId());
        } else {
            this.entityData.set(CONTROLLER, 0);

        }
    }
    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        if (this.controller != null) {
            $$0.putUUID("Controller", this.controller.getUUID());
        }
        $$0.putInt("Lifespan",lifespan);
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        UUID $$2;
        if ($$0.hasUUID("Controller")) {
            $$2 = $$0.getUUID("Controller");
            if (this.level() instanceof ServerLevel SE){
                controller2 = $$2;
                this.setController(SE.getEntity($$2));
            }
        }
        lifespan = $$0.getInt("Lifespan");
    }

    public int lifespan = 0;

    @Override
    public void tick(){
        this.yBodyRot = this.getYRot();
        if (!this.level().isClientSide()) {
            lifespan++;
            if (lifespan > 1200){
                discard();
            }
            if (controller != null){
                controller = this.level().getEntity(getController());
            } else {
                if (controller2 != null){
                    setController(((ServerLevel)this.level()).getEntity(controller2));
                }
            }

            if (this.getTarget() != null && (!this.getTarget().isAlive() || this.getTarget().isRemoved() ||
                    (controller != null && controller.is(getTarget()))
            )
            ){
                this.setTarget(null);
                this.setLastHurtByMob(null);
                this.setLastHurtByPlayer(null);
                this.setAggressive(false);
                ((StandUser)this).roundabout$deeplyRemoveAttackTarget();
            }

            if (controller == null || controller.isRemoved() || !controller.isAlive()){
                this.setController(null);
            } else {
                if (controller.getId() != this.getController()){
                    this.setController(controller.getId());
                }
                if (controller instanceof LivingEntity LE) {
                    autoTarget = LE.getLastHurtByMob();
                    autoTarget2 = LE.getLastHurtMob();
                    if (autoTarget instanceof Zombiefish fm && fm.getController() == this.getController()){
                        autoTarget = null;
                    }
                    if (autoTarget2 instanceof Zombiefish fm && fm.getController() == this.getController()){
                        autoTarget2 = null;
                    }
                    boolean check1 = (this.getTarget() != autoTarget) || autoTarget == null;
                    boolean check2 = (this.getTarget() != autoTarget2) || autoTarget2 == null;

                    if (check1 && check2) {
                        if (autoTarget2 != null && (LE.tickCount - LE.getLastHurtMobTimestamp()) < 200 &&
                                !(autoTarget2 instanceof Player PE && PE.isCreative())) {
                            if (!(controller != null && autoTarget2.is(controller))) {
                                if (autoTarget2 instanceof Player PL) {
                                    setLastHurtByPlayer(PL);
                                }
                                setLastHurtByMob(autoTarget2);
                                setTarget(autoTarget2);
                            }
                        } else if (autoTarget != null && (LE.tickCount - LE.getLastHurtByMobTimestamp()) < 200 &&
                                !(autoTarget instanceof Player PE && PE.isCreative())) {
                            if (!(controller != null && autoTarget.is(controller))) {
                                if (autoTarget instanceof Player PL) {
                                    setLastHurtByPlayer(PL);
                                }
                                setLastHurtByMob(autoTarget);
                                setTarget(autoTarget);
                            }
                        }
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(CONTROLLER)) {
            super.defineSynchedData();
            this.entityData.define(CONTROLLER, -1);
        }
    }

    @Override
    public boolean killedEntity(ServerLevel $$0, LivingEntity $$1) {
        if (controller != null)
            return controller.killedEntity($$0,$$1);
        return true;
    }
}
