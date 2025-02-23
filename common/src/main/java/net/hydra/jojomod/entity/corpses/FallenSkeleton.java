package net.hydra.jojomod.entity.corpses;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FallenSkeleton extends FallenMob implements RangedAttackMob  {
    public FallenSkeleton(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5D).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 1).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public String getData(){
        return "skeleton";
    }
    @Override
    public void performRangedAttack(LivingEntity var1, float var2) {
    }


    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getActivated()){
            return SoundEvents.SKELETON_AMBIENT;
        } else {
            return super.getAmbientSound();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if (this.getActivated()){
            return SoundEvents.SKELETON_HURT;
        } else {
            return super.getHurtSound($$0);
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (this.getActivated()){
            return SoundEvents.SKELETON_DEATH;
        } else {
            return super.getDeathSound();
        }
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }
    @Override
    protected void playStepSound(BlockPos $$0, BlockState $$1) {
        if (this.getActivated()) {
            this.playSound(this.getStepSound(), 0.15F, 1.0F);
        }
    }
    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
}

