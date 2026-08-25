package net.hydra.jojomod.entity;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.entity.goals.AvoidLearnedAnnihilatorGoal;
import net.hydra.jojomod.entity.goals.D4CMeleeAttackGoal;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class D4CCloneEntity extends CloneEntity {

    public int timer = 0;
    public D4CCloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        ((GroundPathNavigation)this.getNavigation()).setCanPassDoors(true);
    }

    protected static final EntityDataAccessor<Integer> STRATEGY = SynchedEntityData.defineId(D4CCloneEntity.class,
            EntityDataSerializers.INT);
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(
                1,
                new AvoidLearnedAnnihilatorGoal(this)
        );

        this.goalSelector.addGoal(2, new D4CMeleeAttackGoal(this, 1.0D, false));

        this.goalSelector.addGoal(3, new FloatGoal(this));

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        addBehaviourGoals();
    }

    public void addBehaviourGoals() {
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(
                this,
                Mob.class,
                5,
                false,
                false,
                entity -> entity instanceof Enemy
                        && !(entity instanceof Creeper)
                        && canFocusOnFighting(entity)
        ));
    }


    public boolean canFocusOnFighting(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }

        if (isBeingAnnihilated()) {
            return false;
        }

        return true;
    }

    public static final int STRATEGY_NORMAL = 0;
    public static final int STRATEGY_BACKING_AWAY = 1;
    public static final int STRATEGY_FLEEING = 2;
    public static final int STRATEGY_ACKNOWLEDGING = 3;
    public static final int STRATEGY_AVOIDING = 4;

    public int acknowledgeTicks = 0;
    public int sneakCooldown = 0;
    public int sneakTicks = 0;
    public int annihilationTicks = 0;
    public int jumpCooldown = 20;

    @Nullable
    private UUID learnedAnnihilator;



    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        Entity annihilator = getAnnihilating();

        // =========================================================
        // CURRENTLY BEING ANNIHILATED
        // =========================================================

        if (annihilator != null && annihilator.isAlive()) {

            // Remember who did this to us.
            learnedAnnihilator = annihilator.getUUID();

            annihilationTicks++;


            if (annihilationTicks < 35) {
                setStrategy(STRATEGY_NORMAL);
                return;
            }

            if (annihilationTicks < 75) {
                setStrategy(STRATEGY_BACKING_AWAY);
                backAwayFrom(annihilator);
                return;
            }

            setStrategy(STRATEGY_FLEEING);
            fleeFrom(annihilator);

            return;
        }


        if (getStrategy() == STRATEGY_FLEEING && learnedAnnihilator != null) {
            startAcknowledging();
            return;
        }


        if (getStrategy() == STRATEGY_ACKNOWLEDGING) {

            Entity learned = getLearnedAnnihilator();

            if (learned != null && learned.isAlive()) {
                acknowledgeAnnihilator(learned);
            } else {
                setStrategy(STRATEGY_NORMAL);
            }

            return;
        }


        if (learnedAnnihilator != null) {

            Entity learned = getLearnedAnnihilator();

            if (learned != null && learned.isAlive()) {
                setStrategy(STRATEGY_AVOIDING);
                avoidAnnihilator(learned);
                return;
            }
        }


        annihilationTicks = 0;
        setStrategy(STRATEGY_NORMAL);
    }

    public boolean figuredItOut(){
        return annihilationTicks >= 75;
    }
    private void startAcknowledging() {
        setStrategy(STRATEGY_ACKNOWLEDGING);

        acknowledgeTicks = 40 + this.getRandom().nextInt(41);

        sneaksRemaining = 0;
        float sneakChance = this.getRandom().nextFloat();

        if (sneakChance < 0.2F) {
            sneaksRemaining = 0; // no greeting
        } else if (sneakChance < 0.4F) {
            sneaksRemaining = 1;
        } else {
            sneaksRemaining = 2;
        }

        sneakCooldown = 20 + this.getRandom().nextInt(10);
        sneakTicks = 0;

        this.getNavigation().stop();

        this.setDeltaMovement(
                0,
                this.getDeltaMovement().y,
                0
        );

        stopSneaking();
    }
    private void stopSneaking() {
        this.setShiftKeyDown(false);

        if (this.getPose() == Pose.CROUCHING) {
            this.setPose(Pose.STANDING);
        }

        sneakTicks = 0;
    }
    private int sneaksRemaining = 0;
    @Nullable
    public Entity getLearnedAnnihilator() {
        if (learnedAnnihilator == null) {
            return null;
        }

        return ((ServerLevel) this.level()).getEntity(learnedAnnihilator);
    }
    private void faceEntity(Entity entity) {
        Vec3 look = entity.getEyePosition().subtract(this.getEyePosition());

        if (look.lengthSqr() < 0.001D) {
            return;
        }

        float yaw = (float)(Mth.atan2(
                look.z,
                look.x
        ) * (180F / Math.PI)) - 90F;

        float pitch = (float)-(Mth.atan2(
                look.y,
                Math.sqrt(look.x * look.x + look.z * look.z)
        ) * (180F / Math.PI));

        this.setYRot(yaw);
        this.setYHeadRot(yaw);
        this.setYBodyRot(yaw);

        this.yRotO = yaw;

        this.setXRot(pitch);
    }
    private void acknowledgeAnnihilator(Entity entity) {

        if (entity == null || !entity.isAlive()) {
            stopSneaking();
            setStrategy(STRATEGY_NORMAL);
            return;
        }

        this.getNavigation().stop();

        this.setDeltaMovement(
                0,
                this.getDeltaMovement().y-0.1,
                0
        );

        faceEntity(entity);

        acknowledgeTicks--;

        if (sneakTicks > 0) {

            sneakTicks--;

            this.setShiftKeyDown(true);
            this.setPose(Pose.CROUCHING);

            return;
        }

        this.setShiftKeyDown(false);

        if (this.getPose() == Pose.CROUCHING) {
            this.setPose(Pose.STANDING);
        }

        if (sneaksRemaining > 0) {

            sneakCooldown--;

            if (sneakCooldown <= 0) {

                sneakTicks = 2 + this.getRandom().nextInt(3);

                sneaksRemaining--;

                sneakCooldown = 8 + this.getRandom().nextInt(14);
            }
        }

        if (acknowledgeTicks <= 0) {

            stopSneaking();

            setStrategy(STRATEGY_AVOIDING);
        }
    }

    private Vec3 fleeDirection = Vec3.ZERO;
    private int fleeDirectionCooldown = 0;
    private void backAwayFrom(Entity entity) {
        Vec3 away = this.position().subtract(entity.position());

        if (away.lengthSqr() < 0.0001D) {
            return;
        }

        away = away.normalize();

        // Don't go absolutely full speed yet.
        double speed = 0.08D;

        Vec3 movement = new Vec3(
                away.x * speed,
                this.getDeltaMovement().y-0.1,
                away.z * speed
        );

        this.setDeltaMovement(movement);
    }

    private void fleeFrom(Entity entity) {

        if (fleeDirectionCooldown <= 0 || fleeDirection.lengthSqr() < 0.001D) {

            Vec3 away = this.position().subtract(entity.position());

            if (away.lengthSqr() < 0.0001D) {
                return;
            }

            away = away.normalize();

            double side = (this.getRandom().nextDouble() - 0.5D) * 1.2D;

            fleeDirection = new Vec3(
                    away.x - away.z * side,
                    -0.1,
                    away.z + away.x * side
            ).normalize();

            fleeDirectionCooldown = 10 + this.getRandom().nextInt(21);

            // Only set a new navigation target when we
            // actually choose a new escape direction.
            double distance = 12.0D + this.getRandom().nextDouble() * 8.0D;

            this.getNavigation().moveTo(
                    this.getX() + fleeDirection.x * distance,
                    this.getY(),
                    this.getZ() + fleeDirection.z * distance,
                    1.5D
            );
        } else {
            fleeDirectionCooldown--;
        }

        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        if (this.onGround() && jumpCooldown <= 0) {
            this.jumpFromGround();

            jumpCooldown = 10 + this.getRandom().nextInt(200);
        }
    }
    private void avoidAnnihilator(Entity entity) {
        double distance = this.distanceTo(entity);

        // We're comfortably outside the danger zone.
        // Do nothing.
        if (distance > 8.0D) {
            return;
        }

        Vec3 away = this.position().subtract(entity.position());

        if (away.lengthSqr() < 0.001D) {
            return;
        }

        away = away.normalize();

        // Only move far enough to get back outside the boundary.
        double distanceToMove = Math.max(1.5D, 8.0D - distance);

        double targetX = this.getX() + away.x * distanceToMove;
        double targetZ = this.getZ() + away.z * distanceToMove;

        this.getNavigation().moveTo(
                targetX,
                this.getY(),
                targetZ,
                1.0D
        );
    }


    public boolean isBeingAnnihilated(){
        return ((IEntityAndData)this).rdbt$getNearAlt()  != null;
    }
    public Entity getAnnihilating(){
        return ((IEntityAndData)this).rdbt$getNearAlt();
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(STRATEGY)) {
            super.defineSynchedData();
            this.entityData.define(STRATEGY, 0);
        }
    }

    public int getStrategy(){
        return entityData.get(STRATEGY);
    }
    public void setStrategy(int strategy){
        if (strategy != STRATEGY_ACKNOWLEDGING){
            stopSneaking();
        }
        entityData.set(STRATEGY,strategy);
    }



    @Override
    public void remove(RemovalReason $$0) {
        super.remove($$0);
    }

    @Override
    protected void jumpFromGround() {
        Vec3 $$0 = this.getDeltaMovement();
        this.setDeltaMovement($$0.x, (double)this.getJumpPower(), $$0.z);
        if (this.isSprinting()) {
            float $$1 = this.getYRot() * (float) (Math.PI / 180.0);
            this.setDeltaMovement(this.getDeltaMovement().add((double)(-Mth.sin($$1) * 0.4F), 0, (double)(Mth.cos($$1) * 0.4F)));
        }

        this.hasImpulse = true;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.27).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 1).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
}
