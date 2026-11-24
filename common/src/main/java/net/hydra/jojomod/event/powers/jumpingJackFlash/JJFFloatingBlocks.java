package net.hydra.jojomod.event.powers.jumpingJackFlash;

import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.util.MainUtil;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class JJFFloatingBlocks {
    public static final String REAL_TAG = "jjf_zerog_block";
    public static final String FAKE_TAG = "jjf_zerog_copy";

    private JJFFloatingBlocks() {
    }

    public interface SyncedFloatingBlock {
        boolean jjf$isFloating();
        UUID jjf$getFloatOwner();
        void jjf$setFloatOwner(UUID owner);
    }

    public static boolean isFloating(FallingBlockEntity block) {
        if (block.level().isClientSide()) {
            return ((SyncedFloatingBlock) block).jjf$isFloating();
        }
        return block.getTags().contains(REAL_TAG) || block.getTags().contains(FAKE_TAG);
    }

    public static void carryRiders(FallingBlockEntity block, AABB before, Vec3 movement) {
        double top = block.getBoundingBox().maxY;
        AABB deck = new AABB(before.minX, before.maxY - 0.25D, before.minZ,
                before.maxX, before.maxY + 0.06D, before.maxZ);
        for (LivingEntity rider : block.level().getEntitiesOfClass(LivingEntity.class, deck)) {
            double feet = rider.getBoundingBox().minY;
            if (rider.noPhysics || rider.isPassenger() || rider.isSpectator()
                    || feet < deck.minY || rider.getDeltaMovement().y > 0.01D
                    || (block.level().isClientSide() && !rider.isControlledByLocalInstance())) {
                continue;
            }
            if (rider instanceof ZeroGravityAccess access
                    && access.jjf$getZeroGravityState().suctionBlockId == block.getId()) {
                if (!block.level().isClientSide()
                        && access.jjf$getZeroGravityState().getSuctionSurface() == net.minecraft.core.Direction.DOWN) {
                    touchPlatform(block, rider);
                }
                continue;
            }

            rider.move(MoverType.SHULKER, new Vec3(movement.x, top - feet, movement.z));
            AABB body = rider.getBoundingBox();
            AABB platform = block.getBoundingBox();
            if (Math.abs(body.minY - top) <= 0.001D
                    && body.maxX > platform.minX && body.minX < platform.maxX
                    && body.maxZ > platform.minZ && body.minZ < platform.maxZ) {
                Vec3 velocity = rider.getDeltaMovement();
                if (velocity.y < 0.0D) {
                    rider.setDeltaMovement(velocity.x, 0.0D, velocity.z);
                }
                if (!block.level().isClientSide()) {
                    touchPlatform(block, rider);
                }
                rider.setOnGround(true);
                rider.fallDistance = 0.0F;
            }
        }
    }

    private static void touchPlatform(FallingBlockEntity block, LivingEntity rider) {
        if (!(rider instanceof Player) || !(rider instanceof ZeroGravityAccess access)) {
            return;
        }
        UUID owner = ((SyncedFloatingBlock) block).jjf$getFloatOwner();
        ZeroGravityState state = access.jjf$getZeroGravityState();
        if (owner == null) {
            return;
        }
        if (owner.equals(rider.getUUID())) {
            state.lastOwnPlatformTick = rider.tickCount;
            return;
        }
        if (state.isSpread()) {
            return;
        }
        if (state.getSource() != ZeroGravitySource.FLOATING_BLOCK) {
            state.resumeUserGravity = state.isSafeUserZeroGravity();
            state.setSource(ZeroGravitySource.FLOATING_BLOCK);
            state.resetZeroGravityTicks();
            state.setRestorationTicks(0);
        }
        state.pressureBlock = block.getUUID();
        state.setOwnerUUID(owner);
        rider.setNoGravity(true);
        rider.fallDistance = 0;
    }

    static void tickExposure(LivingEntity rider, ZeroGravityState state) {
        if (state.lastOwnPlatformTick != Integer.MIN_VALUE
                && rider.tickCount - state.lastOwnPlatformTick > 1) {
            state.lastOwnPlatformTick = Integer.MIN_VALUE;
            if (!rider.onGround() && !state.isZeroGravity()) {
                state.setRestorationTicks(20);
            }
        }
        if (state.getSource() != ZeroGravitySource.FLOATING_BLOCK
                || !(rider.level() instanceof ServerLevel level)) {
            return;
        }
        Entity anchor = state.pressureBlock == null ? null : level.getEntity(state.pressureBlock);
        if (anchor instanceof FallingBlockEntity block && block.isAlive() && isFloating(block)
                && rider.position().distanceToSqr(block.getBoundingBox().getCenter()) <= 25.0D) {
            return;
        }
        boolean resumeToggle = state.resumeUserGravity;
        state.clearZeroGravity();
        if (resumeToggle) {
            state.setSource(ZeroGravitySource.USER_TOGGLE);
            state.setOwnerUUID(rider.getUUID());
        } else {
            rider.setNoGravity(false);
            state.setRestorationTicks(20);
        }
        rider.fallDistance = 0;
    }

    public static boolean floatBlock(ServerLevel level, BlockPos pos, Entity owner) {
        if (owner == null || !level.hasChunkAt(pos)) {
            return false;
        }
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.hasBlockEntity() || !state.getFluidState().isEmpty()
                || state.getDestroySpeed(level, pos) < 0.0F
                || !state.isCollisionShapeFullBlock(level, pos)
                || MainUtil.isBlockBlacklisted(state)) {
            return false;
        }
        if (owner instanceof Player player && !MainUtil.canInteractWithBlock(player, pos)) {
            return false;
        }

        boolean canGrief = level.getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING)
                && MainUtil.getIsGamemodeApproriateForGrief(owner);
        FallingBlockEntity block;
        if (canGrief) {
            block = FallingBlockEntity.fall(level, pos, state);
            block.addTag(REAL_TAG);
        } else {
            block = EntityType.FALLING_BLOCK.create(level);
            if (block == null) {
                return false;
            }
            CompoundTag tag = block.saveWithoutId(new CompoundTag());
            tag.put("BlockState", NbtUtils.writeBlockState(state));
            tag.putInt("Time", 1);
            tag.putBoolean("DropItem", false);
            block.load(tag);
            block.setPos(pos.getX() + 0.5D, pos.getY() + 1.05D, pos.getZ() + 0.5D);
            block.setStartPos(pos);
            block.dropItem = false;
            block.addTag(FAKE_TAG);
            level.addFreshEntity(block);
        }
        ((SyncedFloatingBlock) block).jjf$setFloatOwner(owner.getUUID());
        block.setNoGravity(true);
        block.setDeltaMovement(owner.getLookAngle().scale(0.07D).add(0.0D, 0.07D, 0.0D));
        block.hasImpulse = true;
        block.hurtMarked = true;
        return true;
    }

    public static boolean knockBack(FallingBlockEntity block, Vec3 direction, double force) {
        if (!isFloating(block) || block.level().isClientSide() || direction.lengthSqr() < 0.0001D) {
            return false;
        }
        Vec3 impulse = direction.normalize().scale(Math.min(1.1D, Math.max(0.0D, force)));
        Vec3 velocity = block.getDeltaMovement().add(impulse);
        double maxSpeed = 1.6D;
        if (velocity.lengthSqr() > maxSpeed * maxSpeed) {
            velocity = velocity.normalize().scale(maxSpeed);
        }
        block.setDeltaMovement(velocity);
        block.hasImpulse = true;
        block.hurtMarked = true;
        return true;
    }

    public static void knockBackCone(Player player, double force) {
        if (player.level().isClientSide()) {
            return;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        for (FallingBlockEntity block : player.level().getEntitiesOfClass(FallingBlockEntity.class,
                player.getBoundingBox().inflate(5.0D), JJFFloatingBlocks::isFloating)) {
            Vec3 toward = block.getBoundingBox().getCenter().subtract(eye);
            double distance = toward.lengthSqr();
            if (distance > 25.0D || distance < 0.01D || toward.normalize().dot(look) < 0.70D) {
                continue;
            }
            knockBack(block, look.add(0.0D, 0.05D, 0.0D), force);
        }
    }

    public static void nudgeNearby(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        AABB region = player.getBoundingBox().inflate(5.0D);
        FallingBlockEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (FallingBlockEntity block : player.level().getEntitiesOfClass(FallingBlockEntity.class, region,
                JJFFloatingBlocks::isFloating)) {
            Vec3 toward = block.getBoundingBox().getCenter().subtract(eye);
            double distance = toward.lengthSqr();
            if (distance > 25.0D || distance < 0.01D || toward.normalize().dot(look) < 0.88D) {
                continue;
            }
            if (distance < nearestDistance) {
                nearest = block;
                nearestDistance = distance;
            }
        }
        if (nearest != null) {
            knockBack(nearest, look.add(0.0D, 0.06D, 0.0D), 0.60D);
        }
    }
}
