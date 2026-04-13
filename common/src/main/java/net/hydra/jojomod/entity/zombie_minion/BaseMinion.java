package net.hydra.jojomod.entity.zombie_minion;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.goals.*;
import net.hydra.jojomod.event.index.Tactics;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class BaseMinion extends Monster {
    public Entity controller;
    public UUID controller2;
    private static final EntityDataAccessor<Integer> CONTROLLER =
            SynchedEntityData.defineId(BaseMinion.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> TARGET_TACTIC =
            SynchedEntityData.defineId(BaseMinion.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> MOVEMENT_TACTIC =
            SynchedEntityData.defineId(BaseMinion.class, EntityDataSerializers.BYTE);

    public BaseMinion(EntityType<? extends BaseMinion> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
    }
    public void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(7, new MinionStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new MinionFollowCommanderGoal(this, 1.0, 10.0F, 1.5F, false));
        this.targetSelector.addGoal(1, new MinionTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::canGetMadAt));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, this::canGetMadAt));
   }

    public boolean canGetMadAt(LivingEntity $$0) {
        if (!this.canAttack($$0)) {
            return false;
        } else {
            return (
                    (this.getTargetTactic() == Tactics.HUNT_PLAYERS.id && $$0.getType() == EntityType.PLAYER && !(this.controller != null && $$0.is(this.controller))) ||
                            (this.getTargetTactic() == Tactics.HUNT_MONSTERS.id && $$0 instanceof Enemy && !($$0 instanceof Creeper) && !(this.controller != null && $$0.is(this.controller)))
            );
        }
    }


    public byte getTargetTactic() {
        return this.getEntityData().get(TARGET_TACTIC);
    }
    public void setTargetTactic(byte byt){
        this.entityData.set(TARGET_TACTIC, byt);
    }
    public byte getMovementTactic() {
        return this.getEntityData().get(MOVEMENT_TACTIC);
    }
    public void setMovementTactic(byte byt){
        this.entityData.set(MOVEMENT_TACTIC, byt);
    }
    @Override
    protected InteractionResult mobInteract(Player $$0, InteractionHand $$1) {
        if (!$$0.isCrouching()){
            if (getController() == $$0.getId()){
                if ($$0.level().isClientSide()){
                    ClientUtil.setZombieMinionScreen();
                }
                return InteractionResult.PASS;
            }
        }
        return super.mobInteract($$0,$$1);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 24)
                .add(Attributes.ATTACK_DAMAGE, 5).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource $$0) {
        return SoundEvents.VINDICATOR_HURT;
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

    public static boolean checkSilverfishSpawnRules(EntityType<Silverfish> $$0, LevelAccessor $$1, MobSpawnType $$2, BlockPos $$3, RandomSource $$4) {
        if (checkAnyLightMonsterSpawnRules($$0, $$1, $$2, $$3, $$4)) {
            Player $$5 = $$1.getNearestPlayer((double)$$3.getX() + 0.5, (double)$$3.getY() + 0.5, (double)$$3.getZ() + 0.5, 5.0, true);
            return $$5 == null;
        } else {
            return false;
        }
    }


    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
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
        $$0.putByte("moveTactic",getMovementTactic());
        $$0.putByte("targetTactic",getTargetTactic());
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
        this.setTargetTactic($$0.getByte("targetTactic"));
        this.setMovementTactic($$0.getByte("moveTactic"));
        lifespan = $$0.getInt("Lifespan");
    }

    public int lifespan = 0;

    @Override
    public void tick(){
        this.yBodyRot = this.getYRot();
        if (!this.level().isClientSide()) {
            lifespan++;
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
                    if (autoTarget instanceof net.hydra.jojomod.entity.Zombiefish fm && fm.getController() == this.getController()){
                        autoTarget = null;
                    }
                    if (autoTarget2 instanceof net.hydra.jojomod.entity.Zombiefish fm && fm.getController() == this.getController()){
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
            this.entityData.define(TARGET_TACTIC, (byte) 0);
            this.entityData.define(MOVEMENT_TACTIC, (byte) 0);
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
