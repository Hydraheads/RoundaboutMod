package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.goals.CorpseMeleeAttackGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;

public class FallenVillager extends FallenMob{
    public FallenVillager(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.275).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 2.75).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if ($$0.is(DamageTypeTags.IS_PROJECTILE)){
            return super.hurt($$0, (float) ($$1*
                    (ClientNetworking.getAppropriateConfig().justiceSettings.villagerCorpseProjectileResilienceDamageTaken *0.01)));
        } else {
            return super.hurt($$0,$$1);
        }
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CorpseMeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.addBehaviourGoals();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getActivated()){
            return SoundEvents.VILLAGER_AMBIENT;
        } else {
            return super.getAmbientSound();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if (this.getActivated()){
            return SoundEvents.VILLAGER_HURT;
        } else {
            return super.getHurtSound($$0);
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (this.getActivated()){
            return SoundEvents.VILLAGER_DEATH;
        } else {
            return super.getDeathSound();
        }
    }
    @Override
    public String getData(){
        return "villager";
    }
}
