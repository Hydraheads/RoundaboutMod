package net.hydra.jojomod.entity.stand;

import java.util.List;

import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DiverDownEntity extends FollowingStandEntity {

    public DiverDownEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte PART_6 = 0,
            BETA_DIVER = 1,
            KELP = 2,
            GRAY = 3,
            WHITE = 4,
            PURPLE = 5,
            KHAKI = 6,
            YELLOW = 7,
            BLUE = 8,
            ORANGE = 9,
            PINK = 10,
            INVERSION = 11,
            FIGURE = 12,
            EYECATCH = 13,
            ARTWORK = 14,
            MANGA = 15,
            VOLUME_4 = 16,
            SPINE_ART = 17,
            WORLD_DIVER = 18,
            DIVER_DROWNED = 19,
            SECCO = 20;

    public final AnimationState hideFists = new AnimationState();
    public final AnimationState mobDive = new AnimationState();
    public final AnimationState mobDiveWindup = new AnimationState();
    public final AnimationState energyStorage = new AnimationState();
    public final AnimationState energyStorageWindup = new AnimationState();
    public final AnimationState phasePunchWindup = new AnimationState();
    public final AnimationState phasePunch = new AnimationState();
    public final AnimationState groundBarrage = new AnimationState();
    public final AnimationState chestRummage = new AnimationState();
    public final AnimationState transferWindup = new AnimationState();
    public final AnimationState transfer = new AnimationState();
    public final AnimationState groundDive = new AnimationState();
    public final AnimationState diverZip = new AnimationState();
    public final AnimationState diverZipIdle = new AnimationState();

    private static final float DIVER_ZIP_BLEND_STEP = 0.25F;
    private float diverZipBlend = 0.0F;
    private float diverZipBlendOld = 0.0F;

    public float getDiverZipBlend(float partialTick) {
        return Mth.lerp(partialTick, this.diverZipBlendOld, this.diverZipBlend);
    }

    public static final byte
            MOB_DIVE = 51,
            ENERGY_STORAGE_WINDUP = 52,
            ENERGY_STORAGE = 53,
            PHASE_PUNCH_WINDUP = 54,
            PHASE_PUNCH = 55,
            GROUND_BARRAGE = 56,
            CHEST_RUMMAGE = 57,
            TRANSFER_WINDUP = 58,
            TRANSFER = 59,
            GROUND_DIVE = 60,
            DIVER_ZIP_IDLE = 61,
            DIVER_ZIP = 62;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        //fix for the idle bugging out thing
        byte animation = getAnimation();
        if (animation != BARRAGE && animation != GROUND_BARRAGE) {
            this.hideFists.startIfStopped(this.tickCount);
        } else {
            this.hideFists.stop();
        }
        if (animation == MOB_DIVE) {
            this.mobDive.startIfStopped(this.tickCount);
        } else {
            this.mobDive.stop();
        }
        if (animation == ENERGY_STORAGE_WINDUP) {
            this.energyStorageWindup.startIfStopped(this.tickCount);
        } else {
            this.energyStorageWindup.stop();
        }
        if (animation == ENERGY_STORAGE) {
            this.energyStorage.startIfStopped(this.tickCount);
        } else {
            this.energyStorage.stop();
        }
        if (animation == PHASE_PUNCH_WINDUP) {
            this.phasePunchWindup.startIfStopped(this.tickCount);
        } else {
            this.phasePunchWindup.stop();
        }
        if (animation == PHASE_PUNCH) {
            this.phasePunch.startIfStopped(this.tickCount);
        } else {
            this.phasePunch.stop();
        }
        if (animation == GROUND_BARRAGE) {
            this.groundBarrage.startIfStopped(this.tickCount);
        } else {
            this.groundBarrage.stop();
        }
        if (this.getAnimation() == CHEST_RUMMAGE) {
            this.chestRummage.startIfStopped(this.tickCount);
        } else {
            this.chestRummage.stop();
        }
        if (this.getAnimation() == TRANSFER_WINDUP) {
            this.transferWindup.startIfStopped(this.tickCount);
        } else {
            this.transferWindup.stop();
        }
        if (this.getAnimation() == TRANSFER) {
            this.transfer.startIfStopped(this.tickCount);
        } else {
            this.transfer.stop();
        }
        if (this.getAnimation() == GROUND_DIVE) {
            this.groundDive.startIfStopped(this.tickCount);
        } else {
            this.groundDive.stop();
        }
        boolean isZipping = this.getAnimation() == DIVER_ZIP_IDLE || this.getAnimation() == DIVER_ZIP;
        boolean isMoving = false;
        if (getUser() != null) {
            isMoving = Math.abs(getUser().xxa) > 0.01F || Math.abs(getUser().zza) > 0.01F;
        }

        diverZipBlendOld = diverZipBlend;
        if (!isZipping) {
            diverZipBlend = 0.0F;
            this.diverZipIdle.stop();
            this.diverZip.stop();
        } else {
            float target = isMoving ? 1.0F : 0.0F;
            diverZipBlend = Mth.clamp(diverZipBlend + Mth.clamp(target - diverZipBlend, -DIVER_ZIP_BLEND_STEP, DIVER_ZIP_BLEND_STEP), 0.0F, 1.0F);

            this.diverZipIdle.startIfStopped(this.tickCount);
            this.diverZip.startIfStopped(this.tickCount);
        }
    }

    @Override
    public boolean isInvulnerable() {
        // Invulnerable in pilot unless rummaging a chest
        if (isRemoteControlled() && this.getAnimation() != CHEST_RUMMAGE) {
            return true;
        }
        return super.isInvulnerable();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FELL_OUT_OF_WORLD) || source.is(DamageTypes.GENERIC_KILL)) {
            return super.hurt(source, amount);
        }
        // Blocks any incoming damage/transfer to user while in pilot unless rummaging
        if (isRemoteControlled() && this.getAnimation() != CHEST_RUMMAGE) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean lockPos() {
        return !isRemoteControlled();
    }

    @Override
    public boolean standHasGravity() {
        return isRemoteControlled();
    }

    /*
     * Overriding hasNoPhysics lets the stand go through walls
     */
    @Override
    public boolean hasNoPhysics() {
        return !isRemoteControlled() || super.hasNoPhysics();
    }

    /* autostep
     * need to think about if this should be 1.0, or 1.4F so it can go up things
     * like carpeted fences and stuff.
     * will first try out this move with friends, and balance accordingly.
     * @see net.minecraft.world.entity.LivingEntity#maxUpStep()
     */
    @Override
    public float maxUpStep() {
        return 2.0F;
    }

    /*
     * Overriding move lets diver down go through walls, but not floors.
     * Autosteps up 1-block ledges/slabs/stairs when there is headroom above,
     * but phases horizontally through full walls.
     * 
     * This method checks every tick if diver down should step up. Normally this
     * would be
     * laggy, but it's 1 entity so it shouldn't be too bad.
     * 
     * basically, it's minecraft's movement system but without those pesky wall
     * boundaries and steps.
     */
    @Override
    public void move(MoverType moverType, Vec3 movement) {
        if (isRemoteControlled()) {
            double dx = movement.x;
            double dz = movement.z;

            // Floor collision check for downward gravity
            Vec3 yOnly = new Vec3(0, movement.y, 0);
            Vec3 collidedY = Entity.collideBoundingBox(this, yOnly, this.getBoundingBox(), this.level(), List.of());

            double stepY = 0.0;
            float maxStep = this.maxUpStep();

            if (maxStep > 0.0F && (dx != 0.0 || dz != 0.0)) {
                // Check blocks at the horizontal destination
                AABB targetBox = this.getBoundingBox().move(dx, 0, dz);

                // finds highest solid using maxStep
                double highestGround = this.getY();
                Iterable<VoxelShape> collisions = this.level().getBlockCollisions(this, targetBox);
                for (VoxelShape shape : collisions) {
                    if (!shape.isEmpty()) {
                        double shapeTop = shape.bounds().maxY;
                        if (shapeTop > highestGround && shapeTop <= this.getY() + maxStep + 0.05) {
                            highestGround = shapeTop;
                        }
                    }
                }

                double stepHeight = highestGround - this.getY();
                if (stepHeight > 0.05 && stepHeight <= maxStep + 0.05) {
                    // check if there's collision above the block
                    AABB clearanceBox = new AABB(
                            targetBox.minX, highestGround, targetBox.minZ,
                            targetBox.maxX, highestGround + this.getBbHeight(), targetBox.maxZ);
                    // If there is no collision, it steps up, if there is no collision, it phases
                    if (this.level().noCollision(this, clearanceBox)) {
                        stepY = stepHeight;
                    }
                }
            }

            double finalY;
            if (stepY > 0.0) {
                // Stepping up a block
                finalY = stepY;
            } else if (this.onGround() && (dx != 0.0 || dz != 0.0)) {
                // snaps straight down on the ground
                AABB targetBox = this.getBoundingBox().move(dx, 0, dz);
                Vec3 downStep = Entity.collideBoundingBox(this, new Vec3(0, -1.0, 0),
                        targetBox, this.level(), List.of());
                finalY = (downStep.y < 0.0) ? downStep.y : collidedY.y;
            } else {
                // do you believe in gravitY?
                finalY = collidedY.y;
            }
            // moves the stand
            this.setPos(this.getX() + dx, this.getY() + finalY, this.getZ() + dz);
            // set on ground when touching the floor
            boolean hitFloor = (collidedY.y != movement.y && movement.y < 0.0);
            this.setOnGround(hitFloor || stepY > 0.0);
            return;
        }
        super.move(moverType, movement);
    }

    // """borrowed""" from justice pilot
    @Override
    public boolean isControlledByLocalInstance() {
        LivingEntity user = this.getUser();
        if (user != null) {
            Entity ent = this.getUserData(user).roundabout$getStandPowers().getPilotingStand();
            if (ent != null && ent.is(this)) {
                return (user instanceof Player $$0 ? $$0.isLocalPlayer() : this.isEffectiveAi());
            }
        }
        return super.isControlledByLocalInstance();
    }

    // also """borrowed""" from justice
    @Override
    public void travel(Vec3 vec3) {
        super.travel(vec3);
        if (this.isControlledByLocalInstance()) {
            if (this.getUser() instanceof Player PE && this.level().isClientSide()) {
                C2SPacketUtil.updatePilot(this);
            }
        }
    }

    protected static final EntityDataAccessor<Boolean> SUBMERGED = SynchedEntityData.defineId(
            DiverDownEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUBMERGED, false);
    }

    public void setSubmerged(boolean submerged) {
        this.entityData.set(SUBMERGED, submerged);
    }

    public boolean isSubmerged() {
        return this.entityData.get(SUBMERGED);
    }
}