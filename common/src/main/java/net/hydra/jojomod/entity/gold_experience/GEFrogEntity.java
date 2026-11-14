package net.hydra.jojomod.entity.gold_experience;

import com.mojang.serialization.Dynamic;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GEFrogEntity extends Frog implements GELifeform {
    private boolean isReflectingDamage = false;
    private LivingEntity creator;
    private int lifespan = 600;

    private ItemStack sourceItem = ItemStack.EMPTY;

    private int tonguePullTicks = 0;
    private boolean hasPulled = false;

    public GEFrogEntity(EntityType<? extends Frog> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        // lobotomize the fruh
        return this.brainProvider().makeBrain(dynamic);
    }

    @Override
    protected void customServerAiStep() {
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.lifespan > 0) {
                this.lifespan--;
                pullTarget();
            } else {
                this.die(this.damageSources().generic());
            }
        }
    }

    private void pullTarget() {
        Optional<Entity> currentTarget = this.getTongueTarget();

        if (this.hasPulled) {
            if (currentTarget.isEmpty()) {
                this.die(this.damageSources().generic());
                return;
            }

            Entity target = currentTarget.get();

            if (!(target instanceof LivingEntity livingTarget)
                    || !isValidTarget(livingTarget)
                    || this.tonguePullTicks <= 0
                    || this.distanceToSqr(livingTarget) <= 2.25D) {
                this.eraseTongueTarget();
                this.setPose(Pose.STANDING);
                this.die(this.damageSources().generic());
                return;
            }

            this.lookAt(EntityAnchorArgument.Anchor.EYES, livingTarget.getEyePosition());

            // GET OVER HERE!!!
            Vec3 toFrog = this.position().add(0.0D, 0.35D, 0.0D)
                    .subtract(livingTarget.position().add(0.0D, livingTarget.getBbHeight() * 0.4D, 0.0D));
            double dist = toFrog.length();
            if (dist > 0.001D) {
                Vec3 pullDir = toFrog.normalize();
                double pullSpeed = Math.min(1.35D, 0.55D + dist * 0.06D);
                double lift = livingTarget.onGround() ? 0.28D : 0.08D;

                livingTarget.setDeltaMovement(new Vec3(
                        pullDir.x * pullSpeed,
                        Math.max(pullDir.y * pullSpeed, lift),
                        pullDir.z * pullSpeed
                ));
                livingTarget.hurtMarked = true;

                if (livingTarget instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
                }
            }

            this.tonguePullTicks--;
            if (this.tonguePullTicks <= 0) {
                // KILL ITSELF!!!!!
                this.eraseTongueTarget();
                this.setPose(Pose.STANDING);
                this.die(this.damageSources().generic());
            }
            return;
        }

        // summon windup before pulling
        if (this.tickCount >= 5) {
            LivingEntity target = findTarget();
            if (target != null) {
                this.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
                this.setTongueTarget(target);
                this.setPose(Pose.USING_TONGUE);
                this.tonguePullTicks = 12;
                this.hasPulled = true;
                this.playSound(SoundEvents.FROG_TONGUE, 1.2F, 0.9F);
            }
        }
    }

    @Nullable
    private LivingEntity findTarget() {
        double maxRange = 10.0D;
        double maxRangeSq = maxRange * maxRange;
        double closestDist = Double.MAX_VALUE;
        LivingEntity closestTarget = null;

        for (LivingEntity entity : this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(maxRange),
                this::isValidTarget)) {

            double dist = this.distanceToSqr(entity);
            if (dist <= maxRangeSq && dist < closestDist && this.hasLineOfSight(entity)) {
                closestDist = dist;
                closestTarget = entity;
            }
        }

        return closestTarget;
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