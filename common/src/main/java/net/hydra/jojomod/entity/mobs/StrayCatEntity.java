package net.hydra.jojomod.entity.mobs;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.goals.StrayCatBegGoal;
import net.hydra.jojomod.entity.goals.TerrierBegGoal;
import net.hydra.jojomod.entity.projectile.StrayCatAirBubble;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BegGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class StrayCatEntity extends TamableAnimal implements RangedAttackMob {
    public StrayCatEntity(EntityType<? extends TamableAnimal> $$0, Level $$1) {
        super($$0, $$1);
    }

    private static final EntityDataAccessor<Byte> BREED = SynchedEntityData.defineId(StrayCatEntity.class,
            EntityDataSerializers.BYTE);;
    private static final EntityDataAccessor<Byte> ANIM = SynchedEntityData.defineId(StrayCatEntity.class,
            EntityDataSerializers.BYTE);;
    private static final EntityDataAccessor<Boolean> POTTED = SynchedEntityData.defineId(StrayCatEntity.class,
            EntityDataSerializers.BOOLEAN);;
    private static final EntityDataAccessor<Boolean> INTERESTED = SynchedEntityData.defineId(StrayCatEntity.class,
            EntityDataSerializers.BOOLEAN);;
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(StrayCatEntity.class,
            EntityDataSerializers.BOOLEAN);;

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BREED, (byte) 0);
        this.entityData.define(ANIM, (byte) 0);
        this.entityData.define(POTTED, false);
        this.entityData.define(INTERESTED, false);
        this.entityData.define(SLEEPING, false);
    }

    public void setBreed(byte b) { this.entityData.define(BREED, b); }
    public byte getBreed() { return this.entityData.get(BREED); }

    public void setAnim(byte a) { this.entityData.define(ANIM, a); }
    public byte getAnim() { return this.entityData.get(ANIM); }

    public void setPotted(boolean pot) {
        this.entityData.define(POTTED, pot);
    }
    public boolean getPotted() {return this.entityData.get(POTTED); }
    public void setInterested(boolean pot) {
        this.entityData.define(INTERESTED, pot);
    }
    public boolean getInterested() {return this.entityData.get(INTERESTED); }
    public void setSleeping(boolean pot) {
        this.entityData.define(SLEEPING, pot);
    }
    public boolean getSleeping() {return this.entityData.get(SLEEPING); }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.0F).add(Attributes.MAX_HEALTH, 18.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    public final AnimationState idle = new AnimationState();
    public final AnimationState unpotted = new AnimationState();
    public final AnimationState shooting = new AnimationState();
    public final AnimationState sleeping = new AnimationState();
    public final AnimationState sleepingPotted = new AnimationState();
    public final AnimationState begging = new AnimationState();

    public static final byte
        IDLE = 0,
        SHOOTING = 1,
        SLEEP = 2,
        BEGGING = 3;

    private static final int shootWindupMax = 10;
    private int shootWindup = shootWindupMax;

    public void setupAnimationStates() {
        if (this.getPotted()) {
            unpotted.stop();
        }else {
            unpotted.startIfStopped(this.tickCount);
        }

        byte animation = this.getAnim();

        if (animation == IDLE) {
            idle.startIfStopped(this.tickCount);
        }else {
            idle.stop();
        }

        if (animation == SHOOTING) {
            shooting.startIfStopped(this.tickCount);
        }else {
            shooting.stop();
        }

        if (animation == BEGGING) {
            begging.startIfStopped(this.tickCount);
        }else {
            begging.stop();
        }

        if (animation == SLEEP) {
            if (this.getPotted()) {
                sleeping.stop();
                sleepingPotted.startIfStopped(this.tickCount);
            }else {
                sleepingPotted.stop();
                sleeping.startIfStopped(this.tickCount);
            }

        }else {
            sleeping.stop();
            sleepingPotted.stop();
        }

    }


    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()){
            setupAnimationStates();
        } else {
             if (shootWindup > 0 ) {
                 shootWindup--;
             }else if (shootWindup == 0) {
                 setAnim(IDLE);
                 shootWindup = -1;
             }

            setSleeping(this.shouldSleep());
             if (getSleeping()) {
                 setAnim(SLEEP);
             }else if (getInterested()) {
                 setAnim(BEGGING);
             }

            // detect things lol
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item.isEdible() && (Objects.requireNonNull(item.getFoodProperties()).isMeat() || stack.is(ModItems.COFFEE_GUM));
    }

    @Override
    protected void registerGoals() {
        //super.registerGoals();
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 0D, 30, 7.5F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new StrayCatBegGoal(this, 8.0f));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<Player>(this, Player.class, false, null));
    }

    /*
    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {

    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }
    */
    // TODO add sounds :>
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAT_STRAY_AMBIENT;
    }
    protected SoundEvent getHurtSound(DamageSource p_34195_) {
        return SoundEvents.CAT_HISS;
    }
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    public boolean shouldSleep() {
        BlockPos pos = this.getOnPos();
        BlockState state = this.level().getBlockState(pos);

        if (state.getLightBlock(this.level(), pos) > 2 || this.level().isRainingAt(pos)) {
            return true;
        }

        return false;
    }

    public boolean canBeLeashed(Player $$0) { return false; }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        if (this.getSleeping() || this.getInterested()) {
            return;
        }

        StrayCatAirBubble bubble = ModEntities.STRAY_CAT_AIRBUBBLE.create(this.level());
        if (bubble != null) {

        }
    }
}
