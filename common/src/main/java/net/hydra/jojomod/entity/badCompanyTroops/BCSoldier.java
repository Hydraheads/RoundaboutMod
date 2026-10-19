package net.hydra.jojomod.entity.badCompanyTroops;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.FakeItemEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BCSoldier extends BCTroop{
    public BCSoldier(EntityType<? extends BCTroop> entityType, Level world) {
        super(entityType, world);
    }

    private static final EntityDataAccessor<Boolean> BUNDLE = SynchedEntityData.defineId(BCSoldier.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PARACHUTE_STATUS = SynchedEntityData.defineId(BCSoldier.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BUNDLE, false);
        this.entityData.define(PARACHUTE_STATUS, false);
    }

    public void setBundle(boolean value) {
        this.entityData.set(BUNDLE, value);

        /*if (value) {
            setBoundingBox(EntityDimensions.scalable(height, width*2.8f).makeBoundingBox(position()));
        }*/
    }
    public boolean getBundle() {

        return this.entityData.get(BUNDLE);
    }


    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.4F).add(Attributes.MAX_HEALTH, 4.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public int getTroopValue(){
        if (getBundle()) {
            return 6;
        }
        return 1;
    }

    public static float width = 0.275f;
    public static float height = 0.32f;
    public boolean isDisplay = false;

    public final AnimationState releaseParachute = new AnimationState();
    public final AnimationState closeParachute = new AnimationState();
    public final AnimationState idleWithGun = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            byte animation = this.getAnimation();

            idleWithGun.startIfStopped(tickCount);
            if (!onGround() && !(isInWater() || isInLava()) && !isDisplay) {
                releaseParachute.startIfStopped(tickCount);
                closeParachute.stop();
            }else {
                releaseParachute.stop();
                closeParachute.startIfStopped(tickCount);
            }
        }
    }


    public void aiStep() {
        super.aiStep();
        Vec3 $$0 = this.getDeltaMovement();
        if (!this.onGround() && $$0.y < 0.0F) {
            this.setDeltaMovement($$0.multiply(1.0F, 0.3, 1.0F));
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new FloatGoal(this));
    }

    /*
        @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (getBundle()) {
            return EntityDimensions.scalable(height, width*2.8f);
        }
        return super.getDimensions(pose);
    }*/
    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public boolean canStandBeHurt() {return true;}

}
