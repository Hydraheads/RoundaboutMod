package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GEFishEntity extends Salmon implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;
    private int lifespan = 600;

    private ItemStack sourceItem = ItemStack.EMPTY;

    private float lockedXRot = 0.0F;
    private float lockedYRot = 0.0F;
    private boolean hasLockedRotation = false;

    public GEFishEntity(EntityType<? extends Salmon> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.lifespan > 0) {
                this.lifespan--;

                if (this.hasLockedRotation) {
                    this.setXRot(this.lockedXRot);
                    this.setYRot(this.lockedYRot);
                    this.yBodyRot = this.lockedYRot;
                    this.yHeadRot = this.lockedYRot;
                }

                swimStraightAhead();
            } else {
                this.die(this.damageSources().generic());
            }
        }
    }

    private void swimStraightAhead() {
        // 2 second delay before swimming
        if (this.tickCount > 40) {
            if (this.isInWater()) {
                Vec3 forwardDir = Vec3.directionFromRotation(this.getXRot(), this.getYRot());
                this.setDeltaMovement(forwardDir.scale(0.4D));
            }
        } else {
            if (this.isInWater()) {
                this.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    @Override
    public boolean isInWater() {
        if (this.level().isClientSide()) {
            // used to make the fih not use the flop animation while it's skimming on the surface of water
            boolean isWaterBelow = this.level().getFluidState(this.blockPosition().below()).is(net.minecraft.tags.FluidTags.WATER);
            return super.isInWater() || isWaterBelow;
        }
        return super.isInWater();
    }

    @Override
    protected void registerGoals() {
        // lobotomize the fih
    }

    // let the players ride the fih
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide() && this.getPassengers().size() < 2 && !player.isPassenger()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    // up to 2 people can hold the fih
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    // "seat" positioning, so that players are behind the fih instead of on it
    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            //first passenger is right behind the fih, second passenger is behind the first
            int index = this.getPassengers().indexOf(passenger);
            float distanceBehind = 0.8F + (index * 0.8F);
            float angle = this.getYRot() * ((float) Math.PI / 180F);
            double offsetX = Math.sin(angle) * distanceBehind;
            double offsetZ = -Math.cos(angle) * distanceBehind;
            double offsetY = -0.2D;

            moveFunction.accept(passenger, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ);

            // camera lock
            passenger.setYRot(this.getYRot());
            passenger.setYBodyRot(this.getYRot());
        } else {
            super.positionRider(passenger, moveFunction);
        }
    }

    @Override
    public boolean isReflectingDamage() { return isReflectingDamage; }
    @Override
    public void setReflectingDamage(boolean reflecting) { this.isReflectingDamage = reflecting; }
    @Override
    @Nullable
    public LivingEntity getCreator() { return creator; }
    @Override
    public void setCreator(@Nullable LivingEntity creator) {
        this.creator = creator;
        // unique to the fish, it needs to face where the creator is facing
        if (creator != null) {
            this.lockedXRot = creator.getXRot();
            this.lockedYRot = creator.getYRot();
            this.hasLockedRotation = true;

            this.setXRot(this.lockedXRot);
            this.setYRot(this.lockedYRot);
            this.yBodyRot = this.lockedYRot;
            this.yHeadRot = this.lockedYRot;
        }
    }
    @Override
    public int getLifespan() { return lifespan; }
    @Override
    public void setLifespan(int ticks) { this.lifespan = ticks; }
    @Override
    public ItemStack getSourceItem() { return sourceItem; }
    @Override
    public void setSourceItem(ItemStack stack) { this.sourceItem = stack; }
}