package net.hydra.jojomod.entity.badCompanyTroops;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.goals.badCompanyGoals.*;
import net.hydra.jojomod.entity.projectile.BadCompanyBulletEntity;
import net.hydra.jojomod.event.index.BadCompanyTactics;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class BCSoldier extends BCTroop implements RangedAttackMob {
    public BCSoldier(EntityType<? extends BCTroop> entityType, Level world) {
        super(entityType, world);
    }

    private static final EntityDataAccessor<Boolean> BUNDLE = SynchedEntityData.defineId(BCSoldier.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PARACHUTE_STATUS = SynchedEntityData.defineId(BCSoldier.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> CURRENT_ANIM = SynchedEntityData.defineId(BCSoldier.class, EntityDataSerializers.BYTE);


    public static final byte
            IDLE_GUN = 0,
            WALK_GUN = 1,
            SHOOT_GUN = 2;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BUNDLE, false);
        this.entityData.define(PARACHUTE_STATUS, false);
        this.entityData.define(CURRENT_ANIM, IDLE_GUN);
    }

    public void setBundle(boolean value) {
        this.entityData.set(BUNDLE, value);
    }
    public boolean getBundle() {
        return this.entityData.get(BUNDLE);
    }

    public void setAnim(byte value) {
        this.entityData.set(CURRENT_ANIM, value);
    }
    public byte getAnim() {
        return this.entityData.get(CURRENT_ANIM);
    }


    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.17F).add(Attributes.MAX_HEALTH, 4.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }


    public final static float width = 0.35f;
    public final static float height = 0.32f;
    public boolean isDisplay = false;
    public boolean isShooting = false;

    public final AnimationState releaseParachute = new AnimationState();
    public final AnimationState closeParachute = new AnimationState();
    public final AnimationState idleWithGun = new AnimationState();
    public final AnimationState walkWithGun = new AnimationState();
    public final AnimationState shootGun = new AnimationState();
    public final AnimationState runWithGun = new AnimationState();

    private boolean hasParachuteOff = false;
    private boolean wasParachuteOff = false;
    private int parachuteTicks = -1;

    @Override
    public void setupAnimationStates() {
        //super.setupAnimationStates();
        if (this.getUser() != null) {
            byte animation = this.getAnim();

            if (animation == WALK_GUN) {
                walkWithGun.startIfStopped(tickCount);
            }else {
                walkWithGun.stop();
            }

            if (animation == IDLE_GUN) {
                idleWithGun.startIfStopped(tickCount);
            }else {
                idleWithGun.stop();
            }
            if (animation == SHOOT_GUN) {
                shootGun.startIfStopped(tickCount);
            }else {
                shootGun.stop();
            }

            if (!onGround() && !(isInWater() || isInLava()) && !isDisplay) {
                releaseParachute.startIfStopped(tickCount);
                closeParachute.stop();
                hasParachuteOff = true;
            }else {
                releaseParachute.stop();
                closeParachute.startIfStopped(tickCount);
                hasParachuteOff = false;
            }
        }
    }

    private float attackRange = 4.5f;

    public void tick() {
        super.tick();

        if (hasParachuteOff) { parachuteTicks++; }
        else { parachuteTicks = -1; }

        hasParachuteOff = !onGround() && !(isInWater() || isInLava()) && !isDisplay;

        if (hasParachuteOff && !wasParachuteOff) {
            level().playSound(null, blockPosition(), ModSounds.BC_PARACHUTE_OPEN_EVENT, SoundSource.AMBIENT, 0.5f, 1.0f);
        }/*else if (hasParachuteOff) {
            if (parachuteTicks % 80 == 10) {
                level().playSound(null, blockPosition(), ModSounds.BC_PARACHUTE_LOOP_EVENT, SoundSource.AMBIENT, 0.5f, 1.0f);
            }
        }*/


        wasParachuteOff = hasParachuteOff;

        if (!level().isClientSide) {
            if (getNavigation().isInProgress()) {
                setAnim(WALK_GUN);
            } else {
                if (shootingTicks > 0) {
                    setAnim(SHOOT_GUN);
                }else {
                    setAnim(IDLE_GUN);
                }
            }
        }
        shootingTicks--;

        if (chargeTicks > -1) {
            chargeTicks--;
            if (chargeTicks <= 0) {
                bulletsLeft = getBulletsAmount();
                chargeTicks = -1;
            }
        }
    }


    public void aiStep() {
        super.aiStep();
        Vec3 $$0 = this.getDeltaMovement();
        if (!this.onGround() && $$0.y < 0.0F) {
            this.setDeltaMovement($$0.multiply(1.0F, 0.3, 1.0F));
        }
        $$0 = this.getDeltaMovement();
        if (shootingTicks > 0) {
            this.setDeltaMovement($$0.multiply(0.01F, 1.0F, 0.01F));
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new ReturnToLeaderGoal(this, 1));
        goalSelector.addGoal(2, new UserHurtTargetGoal(this));
        goalSelector.addGoal(2, new RangedAttackGoal(this, 0.8, 5, attackRange) {
            @Override
            public boolean canUse() {
                return super.canUse() && getCurrentTactic() != BadCompanyTactics.RETURN.id;
            }
        });
    }

    public double getAttributeValue(Attribute $$0) {
        if (getBundle()){
            if ($$0.equals(Attributes.MAX_HEALTH)) {
                return this.getAttributes().getValue($$0) * 6;
            }else if ($$0.equals(Attributes.MOVEMENT_SPEED)) {
                return this.getAttributes().getValue($$0) * 0.8f;
            }
        }
        return this.getAttributes().getValue($$0);
    }

    @Override
    public boolean isAttackable() { return true; }

    @Override  public boolean isPickable() { return true; }

    public boolean canStandBeHurt() {return true;}

    @Override
    public void performRangedAttack(LivingEntity target, float v) {
        //shootAt(target);
    }

    public boolean shootAt(LivingEntity target) {
        getLookControl().setLookAt(target, 30.0F, 30.0F);
        return shootAtPosition(target.getEyePosition(1), false);
    }
    public boolean shootAtPosition(Vec3 target) {
        return shootAtPosition(target, true);
    }
    public boolean shootAtPosition(Vec3 target, boolean look) {
        if (look) getLookControl().setLookAt(target);

        getNavigation().stop();
        shootingTicks = getFireCooldownMax()+5;
        if (bulletsLeft <= 0 || fireCooldown > 0) { return false; }

        if (getBundle()) {
            shootSingleBulletAtPosition(getEyePosition(1).add(0.3f, 0.0f, 0.65f * 0.3f), target);
            shootSingleBulletAtPosition(getEyePosition(1).add(0f, 0.0f, 1.15f * 0.3f), target);
            shootSingleBulletAtPosition(getEyePosition(1).add(-0.3f, 0.0f, 0.65f * 0.3f), target);

            shootSingleBulletAtPosition(getEyePosition(1).add(0.3f, 0.0f, -0.65f * 0.3f), target);
            shootSingleBulletAtPosition(getEyePosition(1).add(0f, 0.0f, -1.15f * 0.3f), target);
            shootSingleBulletAtPosition(getEyePosition(1).add(-0.3f, 0.0f, -0.65 * 0.3f), target);

        }else {
            shootSingleBulletAtPosition(getEyePosition(1), target);
        }

        bulletsLeft--;
        if (bulletsLeft <= 0) { chargeTicks = getRechargeTime(); }
        fireCooldown = getFireCooldownMax();
        return true;
    }

    public void shootSingleBulletAt(Vec3 positionShoot, LivingEntity target) {
        shootSingleBulletAtPosition(positionShoot, target.getEyePosition(1));
    }

    public void shootSingleBulletAtPosition(Vec3 positionShoot, Vec3 targetPosition) {
        BadCompanyBulletEntity bullet = new BadCompanyBulletEntity(level(), getUser());
        level().addFreshEntity(bullet);
        bullet.absMoveTo(positionShoot.x, positionShoot.y, positionShoot.z);
        bullet.shoot(targetPosition.x - positionShoot.x, targetPosition.y - positionShoot.y, targetPosition.z - positionShoot.z, 3.5F, 1.25F);
        level().playSound(null, this.blockPosition(), getFireSound(), SoundSource.PLAYERS, getFireSoundVolume(), getFireSoundPitch());
    }

    public SoundEvent getFireSound() { return ModSounds.TOMMY_FIRE_EVENT; }

    public float getFireSoundVolume() { return 7.0F; }

    public float getFireSoundPitch() {
        Random random = new Random();
        return random.nextFloat(1.7F - 1.5F) + 1.5F;
    }

    public int getRechargeTime() {
        return 50 + (getBundle() ? 50 : 0);
    }

    public int getBulletsAmount() { return 12; }
    private int chargeTicks = -1;
    public int bulletsLeft = getBulletsAmount();
    public int shootingTicks = 0;

    @Override
    public boolean performAttack() {
        if (getTarget() != null) {

            return shootAt(getTarget());
        }
        return false;
    }
}
