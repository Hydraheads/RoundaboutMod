package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GEFrogEntity extends Frog implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;
    private int lifespan = 600;

    private ItemStack sourceItem = ItemStack.EMPTY;

    public GEFrogEntity(EntityType<? extends Frog> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.lifespan > 0) {
                this.lifespan--;
                bounceAround();
            } else {
                this.die(this.damageSources().generic());
            }
        }
    }

    private void bounceAround() {
        if (this.onGround()) {
            if (this.getPose() == Pose.LONG_JUMPING) {
                this.setPose(Pose.STANDING);
            }
            if (this.getRandom().nextInt(15) == 0) {
                // this is actually a lot like diver down's spring legs code, except this time i also need to tell minecraft
                // "animate the frog jumping!!!!"
                double dx = (this.getRandom().nextDouble() - 0.5) * 1.5;
                double dz = (this.getRandom().nextDouble() - 0.5) * 1.5;

                this.setDeltaMovement(new Vec3(dx, 0.6, dz));
                this.setYRot((float) (Math.atan2(dz, dx) * (180F / Math.PI)) - 90.0F);
                this.yBodyRot = this.getYRot();

                this.setPose(Pose.LONG_JUMPING);
                this.playSound(SoundEvents.FROG_LONG_JUMP, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void die(DamageSource cause) {
        if (!this.level().isClientSide()) {
            dropSourceItem();
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ModParticles.VITAL_ESSENCE,
                        this.getX(), this.getY(0.5), this.getZ(),
                        20, 0.2, 0.2, 0.2, 0.05);
            }
            this.level().playSound(null, this.blockPosition(),
                    ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
        this.discard();
    }

    private void dropSourceItem() {
        if (!this.sourceItem.isEmpty()) {
            this.spawnAtLocation(this.sourceItem);
            this.sourceItem = ItemStack.EMPTY;
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
    public void setCreator(@Nullable LivingEntity creator) { this.creator = creator; }
    @Override
    public int getLifespan() { return lifespan; }
    @Override
    public void setLifespan(int ticks) { this.lifespan = ticks; }
    @Override
    public ItemStack getSourceItem() { return sourceItem; }
    @Override
    public void setSourceItem(ItemStack stack) { this.sourceItem = stack; }
}