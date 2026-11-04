package net.hydra.jojomod.entity.gold_experience;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GEBatEntity extends Bat implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;
    private int lifespan = 600;

    private ItemStack sourceItem = ItemStack.EMPTY;

    public GEBatEntity(EntityType<? extends Bat> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.lifespan > 0) {
                this.lifespan--;
                baitTarget();
            } else {
                this.die(this.damageSources().generic());
            }
        }
    }

    private void baitTarget() {
        List<LivingEntity> nearby = this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(16.0D),
                this::isValidTarget
        );

        LivingEntity target = null;
        double closestDist = Double.MAX_VALUE;

        for (LivingEntity entity : nearby) {
            double dist = this.distanceToSqr(entity);
            if (dist < closestDist) {
                closestDist = dist;
                target = entity;
            }
        }

        if (target != null) {
            this.setResting(false);

            Vec3 targetLook = target.getLookAngle();
            Vec3 basePos = target.position()
                    .add(0, target.getEyeHeight(), 0)
                    .add(targetLook.scale(2.5));

            // make these bigger to make bats fly much further away
            double time = this.tickCount * 0.4;
            double offsetX = Math.sin(time) * 3.0;
            double offsetY = Math.cos(time * 0.7) * 3.0;
            double offsetZ = Math.cos(time * 1.3) * 3.0;

            Vec3 erraticPos = basePos.add(offsetX, offsetY, offsetZ);
            Vec3 flightPath = erraticPos.subtract(this.position());
            this.setDeltaMovement(flightPath.normalize().scale(0.45));

            this.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
        }
    }

    private boolean isValidTarget(LivingEntity entity) {
        return entity != this
                && entity != this.creator
                && entity.isAlive()
                && (entity instanceof Mob || entity instanceof Player)
                && !(entity instanceof GELifeform)
                && !(entity instanceof StandEntity);
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