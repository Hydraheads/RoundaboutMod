package net.hydra.jojomod.entity.mobs;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

public class Piranha extends AbstractSchoolingFish {
    public Piranha(EntityType<? extends Piranha> $$0, Level $$1) {
        super($$0, $$1);
    }

    public final AnimationState attackAnimationState = new AnimationState();

    private byte ATTACK = 4;

    @Override
    protected void registerGoals() {
        // eeeek bitey fish :(
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4D, true));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        // other goals, can't call super because panic goal is higher than the biter fish goal
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 40) {
            @Override
            public boolean canUse() {
                return Piranha.this.canRandomSwim() && super.canUse();
            }
        });
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
    }

    // manual anger trigger on death, because setAlertOthers doesn't work on 1 shots
    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        Entity attacker = cause.getEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
            List<Piranha> nearbyPiranhas = this.level().getEntitiesOfClass(Piranha.class, this.getBoundingBox().inflate(16.0D));

            for (Piranha piranha : nearbyPiranhas) {
                if (piranha.isAlive() && piranha.getTarget() == null) {
                    piranha.setTarget(livingAttacker);
                }
            }
        }
    }

    @Override
    public boolean isFollower() {
        if (this.getTarget() != null && this.getTarget().isAlive()) {
            return false;
        }
        return super.isFollower();
    }

    @Override
    public boolean canBeFollowed() {
        if (this.getTarget() != null && this.getTarget().isAlive()) {
            return false;
        }
        return super.canBeFollowed();
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity livingTarget) {
                int durationInSeconds = 0;

                // bleed scales with difficulty, like cave spiders
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    durationInSeconds = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    durationInSeconds = 15;
                }

                if (durationInSeconds > 0) {
                    MainUtil.makeBleed(target, 0, durationInSeconds * 20, this);
                }
            }
            this.level().broadcastEntityEvent(this, ATTACK);
            return true;
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == ATTACK) {
            this.attackAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.PIRANHA_BUCKET);
    }

    @Override
    public int getMaxSchoolSize() {
        return 5;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        return SoundEvents.SALMON_HURT;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSchoolingFish.createAttributes().add(Attributes.ATTACK_DAMAGE, 1.5D);
    }
}
