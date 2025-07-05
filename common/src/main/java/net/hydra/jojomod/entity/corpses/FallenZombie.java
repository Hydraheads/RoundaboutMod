package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.goals.FallenZombieAttackGoal;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FallenZombie extends FallenMob{
    public FallenZombie(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FallenZombieAttackGoal(this, 1.0, true));
        this.addBehaviourGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 3).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
    @Override
    public InteractionResult interactAt(Player player, Vec3 location, InteractionHand intHand) {
        ItemStack plrItem = player.getItemInHand(intHand);
        ItemStack corpseItem = this.getMainHandItem();

        if (plrItem.is(Items.NAME_TAG)) {
            return InteractionResult.PASS;
        }
        if (player.isSpectator()) {
            return InteractionResult.SUCCESS;
        }
        if (player.level().isClientSide) {
            return InteractionResult.CONSUME;
        }
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND,plrItem);
        player.setItemInHand(intHand,corpseItem);
        return InteractionResult.SUCCESS;


    }
    @Override
    public boolean doHurtTarget(Entity $$0) {
        boolean $$1 = super.doHurtTarget($$0);
        if ($$1) {
            float $$2 = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < $$2 * 0.3F) {
                $$0.setSecondsOnFire(2 * (int)$$2);
            }
        }

        return $$1;
    }


    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getActivated()){
            return SoundEvents.ZOMBIE_AMBIENT;
        } else {
            return super.getAmbientSound();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if (this.getActivated()){
            return SoundEvents.ZOMBIE_HURT;
        } else {
            return super.getHurtSound($$0);
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (this.getActivated()){
            return SoundEvents.ZOMBIE_DEATH;
        } else {
            return super.getDeathSound();
        }
    }
    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }
    @Override
    protected void playStepSound(BlockPos $$0, BlockState $$1) {
        if (this.getActivated()) {
            this.playSound(this.getStepSound(), 0.15F, 1.0F);
        }
    }


    @Override
    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0 : -0.45;
    }
    @Override
    protected float getStandingEyeHeight(Pose $$0, EntityDimensions $$1) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
}
