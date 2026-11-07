package com.example.jjf.zero_gravity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Pose;
import com.example.jjf.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;

public class ZeroGravitySystem {

    private static final double MAX_HORIZONTAL_SPEED = 1.2D;
    private static final double MAX_VERTICAL_SPEED = 0.8D;

    private static final double AIR_DRAG = 1.0D;
    private static final double GROUND_DRAG = 0.995D;

    private static final double SURFACE_ATTACH_DISTANCE = 0.16D;
    private static final double SURFACE_HOLD_DISTANCE = 0.25D;

    private static final double SURFACE_EDGE_INSET = 0.07D;
    private static final double SURFACE_OVERLAP = 0.025D;

    private static final double SUCTION_STRENGTH = 0.035D;
    private static final double SUCTION_MOVE_SPEED = 0.08D;
    private static final double SUCTION_RELEASE_STRENGTH = 0.4D;
    private static final double GROUND_SUCTION_RELEASE_STRENGTH = 0.20D;
    private static final double HORIZONTAL_BALANCE = 1.55D;


    private static final double SPREAD_KNOCKBACK_HORIZONTAL = 1.85D;
    private static final double SPREAD_KNOCKBACK_VERTICAL = 1.35D;


    public static void tickZeroGravity(LivingEntity entity) {
        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            return;
        }

        state.incrementZeroGravityTicks();
        state.tickSwingImpulseCooldown();


        if (state.getSource() == ZeroGravitySource.SPREAD
                && !(entity instanceof Player)) {
            tickSpreadMob(entity, state);
        }

        entity.setNoGravity(true);
        entity.fallDistance = 0.0F;

        Vec3 motion = entity.getDeltaMovement();

        double x = motion.x;
        double y = motion.y;
        double z = motion.z;

        if (state.isSafeUserZeroGravity()
                && state.getZeroGravityTicks() <= 10
                && y > 0.04D) {
            y = 0.04D;
        }

        if (isTouchingGround(entity)) {
            x *= GROUND_DRAG;
            z *= GROUND_DRAG;

            if (y < 0.0D) {
                y = 0.0D;
            }
        } else {
            x *= AIR_DRAG;
            y *= AIR_DRAG;
            z *= AIR_DRAG;
        }

        x = clamp(x, -MAX_HORIZONTAL_SPEED, MAX_HORIZONTAL_SPEED);
        y = clamp(y, -MAX_VERTICAL_SPEED, MAX_VERTICAL_SPEED);
        z = clamp(z, -MAX_HORIZONTAL_SPEED, MAX_HORIZONTAL_SPEED);

        entity.setDeltaMovement(x, y, z);

        if (entity instanceof Player player
                && state.isSafeUserZeroGravity()) {
            tickSuction(player, state);
        }
    }

    public static void amplifySpreadKnockback(
            LivingEntity entity
    ) {
        if (entity.level().isClientSide()) {
            return;
        }

        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (state.getSource() != ZeroGravitySource.SPREAD) {
            return;
        }

        Vec3 motion =
                entity.getDeltaMovement();

        double x = clamp(
                motion.x * SPREAD_KNOCKBACK_HORIZONTAL,
                -MAX_HORIZONTAL_SPEED,
                MAX_HORIZONTAL_SPEED
        );

        double y = clamp(
                motion.y * SPREAD_KNOCKBACK_VERTICAL,
                -MAX_VERTICAL_SPEED,
                MAX_VERTICAL_SPEED
        );

        double z = clamp(
                motion.z * SPREAD_KNOCKBACK_HORIZONTAL,
                -MAX_HORIZONTAL_SPEED,
                MAX_HORIZONTAL_SPEED
        );

        entity.setDeltaMovement(
                x,
                y,
                z
        );

        entity.hasImpulse = true;
        entity.hurtMarked = true;
    }

    private static void tickSpreadMob(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        if (!(entity instanceof Mob mob)) {
            return;
        }

        mob.getNavigation().stop();
        mob.setZza(0.0F);
        mob.setXxa(0.0F);
        mob.setSpeed(0.0F);

        if (state.getZeroGravityTicks() != 1) {
            return;
        }

        Vec3 motion =
                entity.getDeltaMovement();

        if (motion.horizontalDistanceSqr() > 0.0025D) {
            return;
        }

        Vec3 look =
                entity.getLookAngle();

        Vec3 direction =
                new Vec3(
                        look.x,
                        0.0D,
                        look.z
                );

        if (direction.lengthSqr() < 0.0001D) {
            return;
        }

        direction =
                direction.normalize();

        entity.setDeltaMovement(
                direction.x * 0.08D,
                0.025D,
                direction.z * 0.08D
        );

        entity.hasImpulse = true;
    }


    public static void applyRecoilImpulse(
            LivingEntity entity,
            Vec3 direction,
            double strength
    ) {
        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        if (!access.jjf$getZeroGravityState().isZeroGravity()) {
            return;
        }

        if (direction.lengthSqr() < 0.0001D) {
            return;
        }

        Vec3 normalized = direction.normalize();

        Vec3 impulse = new Vec3(
                normalized.x * strength * HORIZONTAL_BALANCE,
                normalized.y * strength,
                normalized.z * strength * HORIZONTAL_BALANCE
        );

        Vec3 current = entity.getDeltaMovement();
        Vec3 result = current.add(impulse);

        entity.setDeltaMovement(
                clamp(result.x, -MAX_HORIZONTAL_SPEED, MAX_HORIZONTAL_SPEED),
                clamp(result.y, -MAX_VERTICAL_SPEED, MAX_VERTICAL_SPEED),
                clamp(result.z, -MAX_HORIZONTAL_SPEED, MAX_HORIZONTAL_SPEED)
        );

        entity.hasImpulse = true;
    }


    public static void groundPushOff(Player player) {
        if (!(player instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            return;
        }

        if (!isTouchingGround(player)) {
            return;
        }

        Vec3 motion = player.getDeltaMovement();

        Vec3 horizontalMotion = new Vec3(
                motion.x,
                0.0D,
                motion.z
        );

        Vec3 direction;

        if (horizontalMotion.lengthSqr() > 0.01D) {
            direction = horizontalMotion.normalize();
        } else {
            Vec3 look = player.getLookAngle();

            direction = new Vec3(
                    look.x,
                    0.0D,
                    look.z
            );

            if (direction.lengthSqr() > 0.0001D) {
                direction = direction.normalize();
            } else {
                direction = Vec3.ZERO;
            }
        }

        double horizontalBoost = 0.24D;
        double verticalBoost = 0.18D;

        player.setDeltaMovement(
                motion.x + direction.x * horizontalBoost,
                verticalBoost,
                motion.z + direction.z * horizontalBoost
        );

        player.hasImpulse = true;
    }
    public static void applyJJFPunchImpulse(
            LivingEntity attacker,
            LivingEntity target,
            boolean heavy
    ) {
        if (!(target instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (state.getSource() != ZeroGravitySource.SPREAD) {
            return;
        }

        Vec3 direction =
                target.position()
                        .subtract(attacker.position());

        Vec3 horizontal =
                new Vec3(
                        direction.x,
                        0.0D,
                        direction.z
                );

        if (horizontal.lengthSqr() < 0.0001D) {
            Vec3 look =
                    attacker.getLookAngle();

            horizontal =
                    new Vec3(
                            look.x,
                            0.0D,
                            look.z
                    );
        }

        if (horizontal.lengthSqr() < 0.0001D) {
            return;
        }

        horizontal =
                horizontal.normalize();

        double horizontalStrength =
                heavy ? 0.42D : 0.22D;

        double verticalStrength =
                heavy ? 0.20D : 0.10D;

        Vec3 motion =
                target.getDeltaMovement();

        target.setDeltaMovement(
                clamp(
                        motion.x
                                + horizontal.x
                                * horizontalStrength,
                        -MAX_HORIZONTAL_SPEED,
                        MAX_HORIZONTAL_SPEED
                ),
                clamp(
                        motion.y
                                + verticalStrength,
                        -MAX_VERTICAL_SPEED,
                        MAX_VERTICAL_SPEED
                ),
                clamp(
                        motion.z
                                + horizontal.z
                                * horizontalStrength,
                        -MAX_HORIZONTAL_SPEED,
                        MAX_HORIZONTAL_SPEED
                )
        );

        target.hasImpulse = true;
        target.hurtMarked = true;
    }

    public static void applyLauncherImpactImpulse(
            LivingEntity target,
            Vec3 projectileMotion,
            double strength
    ) {
        if (!(target instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (state.getSource()
                != ZeroGravitySource.SPREAD) {
            return;
        }

        if (projectileMotion.lengthSqr()
                < 0.0001D) {
            return;
        }

        Vec3 direction =
                projectileMotion.normalize();

        Vec3 current =
                target.getDeltaMovement();

        Vec3 impulse =
                new Vec3(
                        direction.x
                                * strength
                                * HORIZONTAL_BALANCE,
                        direction.y
                                * strength,
                        direction.z
                                * strength
                                * HORIZONTAL_BALANCE
                );

        Vec3 result =
                current.add(
                        impulse
                );

        target.setDeltaMovement(
                clamp(
                        result.x,
                        -MAX_HORIZONTAL_SPEED,
                        MAX_HORIZONTAL_SPEED
                ),
                clamp(
                        result.y,
                        -MAX_VERTICAL_SPEED,
                        MAX_VERTICAL_SPEED
                ),
                clamp(
                        result.z,
                        -MAX_HORIZONTAL_SPEED,
                        MAX_HORIZONTAL_SPEED
                )
        );

        target.hasImpulse = true;
        target.hurtMarked = true;
    }


    private static void tickSuction(
            Player player,
            ZeroGravityState state
    ) {
        if (state.isSuctioned()) {
            Direction surface = state.getSuctionSurface();

            if (surface == null) {
                state.setSuctioned(false);
                return;
            }

            if (!player.isShiftKeyDown()) {
                releaseSuction(player, surface, state);

                state.setSuctioned(false);
                state.setSuctionSurface(null);
                return;
            }

            if (!isStillTouchingSurface(player, surface)) {
                Direction newSurface =
                        chooseCornerSurface(player, surface);

                if (newSurface != null) {
                    state.setSuctionSurface(newSurface);

                    applySuctionMovement(
                            player,
                            newSurface
                    );

                    return;
                }

                state.setSuctioned(false);
                state.setSuctionSurface(null);
                return;
            }

            applySuctionMovement(player, surface);
            return;
        }


        if (!player.isShiftKeyDown()) {
            return;
        }

        Direction surface = chooseSuctionSurface(player);

        if (surface != null) {
            state.setSuctioned(true);
            state.setSuctionSurface(surface);

            beginSuction(player, surface);
        }
    }
    private static Direction chooseCornerSurface(
            LivingEntity entity,
            Direction oldSurface
    ) {
        Vec3 motion = entity.getDeltaMovement();
        Vec3 look = entity.getLookAngle();

        Direction bestDirection = null;
        double bestScore = -Double.MAX_VALUE;

        for (Direction direction : Direction.values()) {
            if (direction == oldSurface) {
                continue;
            }


            if (!isStillTouchingSurface(entity, direction)) {
                continue;
            }

            Vec3 normal = directionVector(direction);

            double lookScore =
                    look.dot(normal) * 0.55D;

            double motionScore =
                    motion.dot(normal) * 0.30D;

            double score =
                    lookScore + motionScore;

            if (direction.getAxis() != oldSurface.getAxis()) {
                score += 0.15D;
            }

            if (score > bestScore) {
                bestScore = score;
                bestDirection = direction;
            }
        }

        return bestDirection;
    }


    private static void beginSuction(
            Player player,
            Direction surface
    ) {
        Vec3 normal = directionVector(surface);

        player.setDeltaMovement(
                normal.scale(SUCTION_STRENGTH)
        );

        player.fallDistance = 0.0F;
        player.hasImpulse = true;
    }


    private static void applySuctionMovement(
            Player player,
            Direction surface
    ) {
        Vec3 normal = directionVector(surface);

        if (surface == Direction.UP) {
            player.setDeltaMovement(
                    normal.scale(SUCTION_STRENGTH)
            );

            player.fallDistance = 0.0F;
            player.hasImpulse = true;
            return;
        }

        Vec3 look = player.getLookAngle();

        Vec3 alongSurface = look.subtract(
                normal.scale(look.dot(normal))
        );

        Vec3 movement = Vec3.ZERO;

        if (alongSurface.lengthSqr() > 0.0001D) {
            alongSurface = alongSurface.normalize();

            movement = alongSurface.scale(
                    SUCTION_MOVE_SPEED * player.zza
            );
        }

        Vec3 suction =
                normal.scale(SUCTION_STRENGTH);

        player.setDeltaMovement(
                movement.add(suction)
        );

        player.fallDistance = 0.0F;
        player.hasImpulse = true;
    }


    private static void releaseSuction(
            Player player,
            Direction surface,
            ZeroGravityState state
    ) {
        Vec3 look = player.getLookAngle();

        if (look.lengthSqr() < 0.0001D) {
            return;
        }
        if (!player.level().isClientSide
                && surface != Direction.DOWN
                && state.isSuctionLaunchSoundEnabled()
                && state.getSuctionLaunchSoundCooldown() <= 0) {
            player.level().playSound(
                    null,
                    player.blockPosition(),
                    ModSounds.SUCTION_LAUNCH,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            state.setSuctionLaunchSoundCooldown(30);
        }

        Vec3 direction = look.normalize();

        double strength = surface == Direction.DOWN
                ? GROUND_SUCTION_RELEASE_STRENGTH
                : SUCTION_RELEASE_STRENGTH;

        player.setDeltaMovement(
                direction.x * strength * HORIZONTAL_BALANCE,
                direction.y * strength,
                direction.z * strength * HORIZONTAL_BALANCE
        );

        player.hasImpulse = true;
    }

    private static Direction chooseSuctionSurface(
            LivingEntity entity
    ) {
        Vec3 motion = entity.getDeltaMovement();
        Vec3 look = entity.getLookAngle();

        if (look.y > 0.25D
                && isTouchingSurface(entity, Direction.UP)) {
            return Direction.UP;
        }

        if (look.y < -0.25D
                && isTouchingSurface(entity, Direction.DOWN)) {
            return Direction.DOWN;
        }

        Direction bestDirection = null;
        double bestScore = -Double.MAX_VALUE;

        for (Direction direction : Direction.values()) {
            if (!isTouchingSurface(entity, direction)) {
                continue;
            }

            Vec3 normal = directionVector(direction);

            double motionScore = motion.dot(normal);
            double lookScore = look.dot(normal) * 0.25D;

            double score = motionScore + lookScore;

            if (score > bestScore) {
                bestScore = score;
                bestDirection = direction;
            }
        }

        return bestDirection;
    }


    public static boolean isTouchingGround(LivingEntity entity) {
        return isTouchingSurface(entity, Direction.DOWN);
    }


    public static boolean isTouchingCeiling(LivingEntity entity) {
        return isTouchingSurface(entity, Direction.UP);
    }


    public static boolean isTouchingSurface(
            LivingEntity entity,
            Direction direction
    ) {
        AABB probe = makeSurfaceProbe(
                entity,
                direction,
                SURFACE_ATTACH_DISTANCE
        );

        return !entity.level().noCollision(entity, probe);
    }


    private static boolean isStillTouchingSurface(
            LivingEntity entity,
            Direction direction
    ) {
        AABB probe = makeSurfaceProbe(
                entity,
                direction,
                SURFACE_HOLD_DISTANCE
        );

        return !entity.level().noCollision(entity, probe);
    }


    private static AABB makeSurfaceProbe(
            LivingEntity entity,
            Direction direction,
            double distance
    ) {
        AABB box = entity.getBoundingBox();

        double inset = SURFACE_EDGE_INSET;
        double overlap = SURFACE_OVERLAP;

        return switch (direction) {
            case DOWN -> new AABB(
                    box.minX + inset,
                    box.minY - distance,
                    box.minZ + inset,
                    box.maxX - inset,
                    box.minY + overlap,
                    box.maxZ - inset
            );

            case UP -> {
                double standingHeight =
                        entity.getDimensions(Pose.STANDING).height;

                double currentHeight =
                        entity.getBbHeight();

                double crouchDifference =
                        Math.max(0.0D, standingHeight - currentHeight);

                double ceilingY =
                        box.maxY + crouchDifference;

                yield new AABB(
                        box.minX + inset,
                        ceilingY - overlap,
                        box.minZ + inset,
                        box.maxX - inset,
                        ceilingY + distance,
                        box.maxZ - inset
                );
            }

            case NORTH -> new AABB(
                    box.minX + inset,
                    box.minY + inset,
                    box.minZ - distance,
                    box.maxX - inset,
                    box.maxY - inset,
                    box.minZ + overlap
            );

            case SOUTH -> new AABB(
                    box.minX + inset,
                    box.minY + inset,
                    box.maxZ - overlap,
                    box.maxX - inset,
                    box.maxY - inset,
                    box.maxZ + distance
            );

            case WEST -> new AABB(
                    box.minX - distance,
                    box.minY + inset,
                    box.minZ + inset,
                    box.minX + overlap,
                    box.maxY - inset,
                    box.maxZ - inset
            );

            case EAST -> new AABB(
                    box.maxX - overlap,
                    box.minY + inset,
                    box.minZ + inset,
                    box.maxX + distance,
                    box.maxY - inset,
                    box.maxZ - inset
            );
        };
    }

    public static Direction getTouchingWall(LivingEntity entity) {
        if (isTouchingSurface(entity, Direction.NORTH)) {
            return Direction.NORTH;
        }

        if (isTouchingSurface(entity, Direction.SOUTH)) {
            return Direction.SOUTH;
        }

        if (isTouchingSurface(entity, Direction.WEST)) {
            return Direction.WEST;
        }

        if (isTouchingSurface(entity, Direction.EAST)) {
            return Direction.EAST;
        }

        return null;
    }


    public static Direction getTouchingSurface(LivingEntity entity) {
        Direction wall = getTouchingWall(entity);

        if (wall != null) {
            return wall;
        }

        if (isTouchingCeiling(entity)) {
            return Direction.UP;
        }

        if (isTouchingGround(entity)) {
            return Direction.DOWN;
        }

        return null;
    }


    public static boolean isTouchingAnySurface(LivingEntity entity) {
        return getTouchingSurface(entity) != null;
    }


    private static Vec3 directionVector(Direction direction) {
        return new Vec3(
                direction.getStepX(),
                direction.getStepY(),
                direction.getStepZ()
        );
    }


    private static double clamp(
            double value,
            double min,
            double max
    ) {
        return Math.max(min, Math.min(max, value));
    }


    public static void applyImpulse(
            LivingEntity entity,
            Vec3 direction,
            double strength
    ) {
        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            return;
        }

        if (direction.lengthSqr() < 0.0001D) {
            return;
        }

        Vec3 normalized = direction.normalize();

        Vec3 impulse = new Vec3(
                normalized.x * strength * HORIZONTAL_BALANCE,
                normalized.y * strength,
                normalized.z * strength * HORIZONTAL_BALANCE
        );

        Vec3 current = entity.getDeltaMovement();

        Vec3 result = current.add(impulse);

        double x = clamp(
                result.x,
                -MAX_HORIZONTAL_SPEED,
                MAX_HORIZONTAL_SPEED
        );

        double y = clamp(
                result.y,
                -MAX_VERTICAL_SPEED,
                MAX_VERTICAL_SPEED
        );

        double z = clamp(
                result.z,
                -MAX_HORIZONTAL_SPEED,
                MAX_HORIZONTAL_SPEED
        );

        entity.setDeltaMovement(x, y, z);
        entity.hasImpulse = true;
    }


    public static void applySwingImpulse(LivingEntity entity) {
        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            return;
        }

        if (state.getSwingImpulseCooldown() > 0) {
            return;
        }

        if (entity instanceof Player player) {
            HitResult hit = player.pick(
                    4.5D,
                    0.0F,
                    false
            );

            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;

                BlockState blockState =
                        player.level().getBlockState(
                                blockHit.getBlockPos()
                        );

                if (blockState.isCollisionShapeFullBlock(
                        player.level(),
                        blockHit.getBlockPos()
                )) {
                    Vec3 recoilDirection =
                            directionVector(
                                    blockHit.getDirection()
                            );

                    applyImpulse(
                            player,
                            recoilDirection,
                            0.14D
                    );

                    state.setSwingImpulseCooldown(10);
                    return;
                }
            }
        }

        double strength =
                getSwingImpulseStrength(
                        entity.getMainHandItem()
                );

        Vec3 recoilDirection =
                getRecoilDirection(entity);

        applyRecoilImpulse(
                entity,
                recoilDirection,
                strength
        );

        state.setSwingImpulseCooldown(8);
    }


    private static double getSwingImpulseStrength(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.006D;
        }

        if (stack.getItem() instanceof BowItem
                || stack.getItem() instanceof CrossbowItem) {
            return 0.010D;
        }

        if (stack.getItem() instanceof SwordItem
                || stack.getItem() instanceof DiggerItem) {
            return 0.032D;
        }

        return 0.012D;
    }


    public static void tickState(LivingEntity entity) {
        if (!(entity instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();

        state.tickSuctionLaunchSoundCooldown();

        if (state.isRestoringWeight()) {
            tickWeightRestoration(entity, state);
        }
    }


    private static void tickWeightRestoration(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        if (entity.onGround()) {
            state.setRestorationTicks(0);
            entity.fallDistance = 0.0F;
            return;
        }

        Vec3 motion = entity.getDeltaMovement();

        double maxFallSpeed = -0.12D;

        if (entity.isCrouching()) {
            maxFallSpeed = -0.30D;
        }

        double y = motion.y;

        if (y < maxFallSpeed) {
            y = maxFallSpeed;
        }

        entity.setDeltaMovement(
                motion.x,
                y,
                motion.z
        );

        entity.fallDistance = 0.0F;
        entity.hasImpulse = true;
    }


    public static Vec3 getRecoilDirection(LivingEntity entity) {
        Vec3 look = entity.getLookAngle();

        Vec3 direction = new Vec3(
                -look.x,
                -look.y * 0.35D,
                -look.z
        );

        if (direction.lengthSqr() < 0.0001D) {
            return Vec3.ZERO;
        }

        return direction.normalize();
    }
}