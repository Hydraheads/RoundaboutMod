package net.hydra.jojomod.entity.stand;

import java.util.List;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.event.powers.StandUser;
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
            LAVA_DIVER = 1,
            RED_DIVER = 2,
            ORANGE_DIVER = 3,
            TREASURE_DIVER = 4,
            BIRTHDAY_DIVER = 5,
            FIRE_DIVER = 6;

    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    public final AnimationState diverzip = new AnimationState();
    public final AnimationState hideLegEntirely = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {

            if (this.getAnimation() != 12) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }

            if (this.getAnimation() != 80) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }

            if (this.getAnimation() == 42) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }

            if (this.getAnimation() == 43) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }
        }
    }

    @Override
    public boolean lockPos() {
        return !isRemoteControlled();
    }

    /*
     * Overriding hasNoPhysics lets the stand go through walls
     */
    @Override
    public boolean hasNoPhysics() {
        return !isRemoteControlled() || super.hasNoPhysics();
    }

    @Override
    public float maxUpStep() {
        return 1.0F;
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
}