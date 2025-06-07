package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.entity.goals.FallenRangedAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FallenSkeleton extends FallenMob implements RangedAttackMob  {
    public FallenSkeleton(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 1).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
    public void setActivated(boolean bool){
        if (bool){
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        super.setActivated(bool);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new FallenRangedAttackGoal<>(this, 1.0, 20, 15.0F));
        this.addBehaviourGoals();
    }
    @Override
    public String getData(){
        return "skeleton";
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


    @Override
    public void performRangedAttack(LivingEntity $$0, float $$1) {
        ItemStack $$2 = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow $$3 = this.getArrow($$2, $$1);
        double $$4 = $$0.getX() - this.getX();
        double $$5 = $$0.getY(0.3333333333333333) - $$3.getY();
        double $$6 = $$0.getZ() - this.getZ();
        double $$7 = Math.sqrt($$4 * $$4 + $$6 * $$6);
        $$3.shoot($$4, $$5 + $$7 * 0.2F, $$6, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity($$3);
    }

    protected AbstractArrow getArrow(ItemStack $$0, float $$1) {
        return ProjectileUtil.getMobArrow(this, $$0, $$1);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem $$0) {
        return $$0 == Items.BOW;
    }

}

