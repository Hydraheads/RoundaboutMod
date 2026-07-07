package net.hydra.jojomod.entity.mobs;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.goals.StrayCatBegGoal;
import net.hydra.jojomod.entity.goals.TerrierBegGoal;
import net.hydra.jojomod.entity.projectile.StrayCatAirBubble;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class StrayCatEntity extends TamableAnimal implements RangedAttackMob {
    public StrayCatEntity(EntityType<? extends TamableAnimal> $$0, Level $$1) {
        super($$0, $$1);
    }

    private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(Items.COD, Items.SALMON);

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
        super.defineSynchedData();
        this.entityData.define(BREED, (byte) 0);
        this.entityData.define(ANIM, (byte) 0);
        this.entityData.define(POTTED, false);
        this.entityData.define(INTERESTED, false);
        this.entityData.define(SLEEPING, false);
    }

    public void setBreed(byte b) { this.entityData.set(BREED, b); }
    public byte getBreed() { return this.entityData.get(BREED); }

    public void setAnim(byte a) { this.entityData.set(ANIM, a); }
    public byte getAnim() { return this.entityData.get(ANIM); }

    public void setPotted(boolean pot) {
        this.entityData.set(POTTED, pot);
    }
    public boolean getPotted() {return this.entityData.get(POTTED); }
    public void setInterested(boolean pot) {
        this.entityData.set(INTERESTED, pot);
    }
    public boolean getInterested() {return this.entityData.get(INTERESTED); }
    public void setSleeping(boolean pot) {
        this.entityData.set(SLEEPING, pot);
    }
    public boolean getSleeping() {return this.entityData.get(SLEEPING); }

    public static AttributeSupplier.Builder createAttributes() {
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

    public byte getBubbleSkin() { return 0; }

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
             if (this.shootWindup == -1) {
                 if (getSleeping()) {
                     setAnim(SLEEP);
                 } else {
                     setAnim(IDLE);
                 }

                 if (getInterested() && !getSleeping()) {
                     setAnim(BEGGING);
                 }
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
        return isYummy(stack);
    }

    @Override
    public void knockback(double x, double y, double z) {

    }

    public boolean isYummy(ItemStack stack) {
        return TEMPT_INGREDIENT.test(stack);
    }

    @Override
    public InteractionResult mobInteract(Player $$0, InteractionHand $$1) {
        ItemStack $$2 = $$0.getItemInHand($$1);
        Item $$3 = $$2.getItem();
        if (this.level().isClientSide) {
            boolean $$4 = this.isOwnedBy($$0) || this.isTame() || this.isYummy($$2) && !this.isTame();
            //boolean $$4 = this.isOwnedBy($$0) || this.isTame() || $$2.is(Items.BONE) && !this.isTame() && !this.isAngry();
            return $$4 ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.isTame()) {
            if (this.isFood($$2) && this.getHealth() < this.getMaxHealth()) {
                if (!$$0.getAbilities().instabuild) {
                    $$2.shrink(1);
                }

                this.heal((float)$$3.getFoodProperties().getNutrition());
                return InteractionResult.SUCCESS;
            } else {
                if ($$2.is(Items.FLOWER_POT)) {
                    if (this.isOwnedBy($$0)) {
                        if (!$$0.getAbilities().instabuild) {
                            $$2.shrink(1);
                        }


                        return InteractionResult.SUCCESS;
                    }
                }
                /*if ($$3 instanceof DyeItem) {
                    DyeItem $$5 = (DyeItem)$$3;
                    if (this.isOwnedBy($$0)) {
                        DyeColor $$6 = $$5.getDyeColor();
                        if ($$6 != this.getCollarColor()) {
                            this.setCollarColor($$6);
                            if (!$$0.getAbilities().instabuild) {
                                $$2.shrink(1);
                            }

                            return InteractionResult.SUCCESS;
                        }

                        return super.mobInteract($$0, $$1);
                    }
                }*/

                InteractionResult $$7 = super.mobInteract($$0, $$1);
                return $$7;
            }
        } else if (isYummy($$2)) {
            if (!$$0.getAbilities().instabuild) {
                $$2.shrink(1);
            }

            if (this.random.nextInt(3) == 0) {
                this.tame($$0);
                this.setTarget((LivingEntity)null);
                //this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract($$0, $$1);
        }
    }


    @Override
    protected void registerGoals() {
        //super.registerGoals();
        this.goalSelector.addGoal(1, new StrayCatSleepGoal(this));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 0D, 75, 83, 6.5F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new StrayCatBegGoal(this, 8.0f));
        this.goalSelector.addGoal(1, new StrayCatSleepGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(4, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(5, new StrayCatBegGoal(this, 8.0f));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<Player>(this, Player.class, false, null));
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
        if (this.shouldSleep()) {
            return SoundEvents.CAT_PURR;
        }
        if (this.getInterested()) {
            return SoundEvents.CAT_BEG_FOR_FOOD;
        }

        if (this.getTarget() != null && this.getTarget().isAlive()) {
            return SoundEvents.CAT_HISS;
        }

        return SoundEvents.CAT_STRAY_AMBIENT;
    }
    protected SoundEvent getHurtSound(DamageSource p_34195_) {
        return SoundEvents.CAT_HISS;
    }
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    @Override public void push(Entity ent) { }
    @Override public void doPush(Entity ent) { }

    public boolean shouldSleep() {
        BlockPos pos = this.getOnPos();
        //BlockState state = this.level().getBlockState(pos);
        long dayTime = this.level().getDayTime() % 24000;

        if ((dayTime >= 13000 && dayTime <= 23750) || this.level().isRainingAt(pos)) {
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
            bubble.setSped(0.2f);
            bubble.setOwner(this);
            bubble.setSkin(this.getBubbleSkin());
            bubble.setFollowOwnerView(true);

            Vec3 addToPosition = new Vec3(0, this.getEyeHeight() * 0.85f, 0);
            Direction direction = ((IGravityEntity) this).roundabout$getGravityDirection();
            if (direction != Direction.DOWN) {
                addToPosition = RotationUtil.vecPlayerToWorld(addToPosition, direction);
            }
            Vec3 pos = this.getPosition(1).add(addToPosition.x, addToPosition.y, addToPosition.z).add(this.getForward().scale(this.getBbWidth() * 1));
            bubble.setPos(pos.x(), pos.y(), pos.z());
            bubble.shootFromRotationDeltaAgnostic(this, this.getXRot(), this.getYRot(), 1.0F, 0.2f, 0);
            //bubble.shootFromRotation(P, P.getXRot(), P.getYRot(), -0.5F, SPEED, 0.00f);

            this.level().addFreshEntity(bubble);
        }
    }




    static public class StrayCatSleepGoal extends Goal {
        StrayCatEntity stray;


        public StrayCatSleepGoal(StrayCatEntity stray) {
            this.stray = stray;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
        }


        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void start() {
            this.stray.setTarget(null);
        }

        public void stop() { }

        public boolean requiresUpdateEveryTick() {
            return false;
        }

        @Override
        public boolean canUse() {
            return this.stray.shouldSleep();
        }

        @Override
        public boolean canContinueToUse() {
            return this.stray.shouldSleep();
        }
    }
}
