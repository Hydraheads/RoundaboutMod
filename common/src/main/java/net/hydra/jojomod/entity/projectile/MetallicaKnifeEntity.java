package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MetallicaKnifeEntity extends KnifeEntity {

    private static final EntityDataAccessor<Boolean> WAITING = SynchedEntityData.defineId(MetallicaKnifeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAUNCHED = SynchedEntityData.defineId(MetallicaKnifeEntity.class, EntityDataSerializers.BOOLEAN);

    public MetallicaKnifeEntity(EntityType<? extends KnifeEntity> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public MetallicaKnifeEntity(Level level, LivingEntity shooter) {
        super(ModEntities.METALLICA_KNIFE, level, shooter);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WAITING, false);
        this.entityData.define(LAUNCHED, false);
    }

    public void setWaiting(boolean wait) {
        this.entityData.set(WAITING, wait);
    }

    public boolean isWaiting() {
        return this.entityData.get(WAITING);
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 2 && this.entityData.get(LAUNCHED)) {
            this.discard();
        }

        if (isWaiting()) {
            this.setNoGravity(true);
            this.noPhysics = true;

            if (this.tickCount < 20) {
                double bob = Math.sin(this.tickCount * 0.3) * 0.03;
                if (this.tickCount < 5) {
                    this.setDeltaMovement(0, 0.2, 0);
                } else {
                    this.setDeltaMovement(0, bob, 0);
                }
                this.setPos(this.getX(), this.getY() + this.getDeltaMovement().y, this.getZ());
            }
            else {
                Entity owner = this.getOwner();
                if (owner instanceof LivingEntity livingOwner) {
                    if (this.distanceTo(livingOwner) > 20.0) {
                        this.setWaiting(false);
                        return;
                    }

                    Vec3 targetPos;
                    Entity targetEnt = MainUtil.getTargetEntity(livingOwner, 40.0f);

                    if (targetEnt != null) {
                        targetPos = targetEnt.getBoundingBox().getCenter();
                    } else {
                        Vec3 eyePos = livingOwner.getEyePosition();
                        Vec3 viewVec = livingOwner.getViewVector(1.0F);
                        targetPos = eyePos.add(viewVec.scale(40.0));
                    }

                    Vec3 currentPos = this.position();
                    Vec3 dir = targetPos.subtract(currentPos).normalize();
                    double speed = 0.45;
                    Vec3 movement = dir.scale(speed);
                    this.setDeltaMovement(movement);

                    Vec3 nextPos = currentPos.add(movement);

                    HitResult hitResult = this.level().clip(new ClipContext(currentPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

                    if (hitResult.getType() != HitResult.Type.MISS) {
                        nextPos = hitResult.getLocation();
                    }

                    EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                            this.level(),
                            this,
                            currentPos,
                            nextPos,
                            this.getBoundingBox().expandTowards(movement).inflate(1.0D),
                            this::canHitEntity
                    );

                    if (entityHitResult != null) {
                        hitResult = entityHitResult;
                    }

                    if (hitResult.getType() != HitResult.Type.MISS) {
                        if (hitResult.getType() == HitResult.Type.BLOCK) {
                            this.setWaiting(false);
                            this.setPos(hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
                            this.onHit(hitResult);
                            return;
                        } else {
                            this.onHit(hitResult);
                        }
                    }

                    if (this.isRemoved()) return;

                    this.setPos(nextPos.x, nextPos.y, nextPos.z);

                    double dx = movement.x;
                    double dy = movement.y;
                    double dz = movement.z;
                    double horizontalDist = Math.sqrt(dx * dx + dz * dz);

                    this.setYRot((float)(Mth.atan2(dx, dz) * (double)(180F / (float)Math.PI)));
                    this.setXRot((float)(Mth.atan2(dy, horizontalDist) * (double)(180F / (float)Math.PI)));

                    this.yRotO = this.getYRot();
                    this.xRotO = this.getXRot();

                } else {
                    this.setWaiting(false);
                }
            }

            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }

            if (!this.level().isClientSide && this.tickCount > 400) {
                this.discard();
            }
            return;
        } else {
            this.noPhysics = false;
            this.setNoGravity(false);
        }

        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity target = result.getEntity();
        Entity owner = this.getOwner();

        if (target instanceof LivingEntity livingTarget && owner instanceof LivingEntity) {
            ResourceLocation bleedLoc = new ResourceLocation(Roundabout.MOD_ID, "bleed");
            if (BuiltInRegistries.MOB_EFFECT.containsKey(bleedLoc)) {
                livingTarget.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.get(bleedLoc), 100, 0), owner);
            }
            MainUtil.takeKnockbackWithY(livingTarget, 0.5, this.getX() - livingTarget.getX(), 0, this.getZ() - livingTarget.getZ());
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsWaiting", isWaiting());
        tag.putBoolean("Launched", this.entityData.get(LAUNCHED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setWaiting(tag.getBoolean("IsWaiting"));
        this.entityData.set(LAUNCHED, tag.getBoolean("Launched"));
    }
}