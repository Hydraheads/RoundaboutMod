package net.hydra.jojomod.event.powers.jumpingJackFlash;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public final class SpreadContactSystem {

    private static final int SPREAD_ARM_TICKS = 4;
    private static final int SPREAD_DURATION_TICKS = 20 * 60;
    private static final int RESTORATION_TICKS = 20;

    private static final double OWNER_ESCAPE_DISTANCE = 25.0D;
    private static final int OWNER_ESCAPE_TICKS = 20 * 3;

    private static final double CONTACT_DISTANCE = 0.40D;
    private static final double CONTACT_PUSH = 0.045D;
    private static final float MAX_SPREAD_RADIUS = 20.0F;
    private static final int SPREAD_PULSE_TICKS = 10;
    private static final int MAX_TARGETS_PER_PULSE = 12;

    private SpreadContactSystem() {
    }

    public static void tick(LivingEntity entity, ZeroGravityState state) {
        if (entity.level().isClientSide() || !state.isSpread()) {
            return;
        }
        if (state.getZeroGravityTicks() >= SPREAD_DURATION_TICKS
                || isEscapingOwner(entity, state)) {
            endSpread(entity, state);
            return;
        }
        int age = state.getZeroGravityTicks();
        float progress = Math.min(1.0F, age / (float) SPREAD_DURATION_TICKS);
        float radius = (float) (CONTACT_DISTANCE
                + (MAX_SPREAD_RADIUS - CONTACT_DISTANCE) * progress);
        state.setRadius(radius);
        if (age < SPREAD_ARM_TICKS || age % SPREAD_PULSE_TICKS != 0) {
            return;
        }
        AABB area = entity.getBoundingBox().inflate(radius);
        List<LivingEntity> targets = entity.level().getEntitiesOfClass(
                LivingEntity.class, area,
                target -> target != entity && target.isAlive() && !target.isSpectator()
        );
        UUID ownerUUID = state.getOwnerUUID();
        int infected = 0;
        for (LivingEntity target : targets) {
            if (target.distanceToSqr(entity) > radius * radius) {
                continue;
            }
            if (!(target instanceof ZeroGravityAccess access)) {
                continue;
            }
            ZeroGravityState targetState = access.jjf$getZeroGravityState();
            if (targetState.isSpread()) {
                continue;
            }
            infect(entity, target, targetState, ownerUUID);
            if (++infected >= MAX_TARGETS_PER_PULSE) {
                break;
            }
        }
    }

    private static boolean isEscapingOwner(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        UUID ownerUUID =
                state.getOwnerUUID();

        if (!(entity.level() instanceof ServerLevel server)) {
            return false;
        }

        Entity owner =
                ownerUUID == null
                        ? null
                        : server.getEntity(ownerUUID);

        boolean tooFar =
                owner == null
                        || !owner.isAlive()
                        || entity.distanceToSqr(owner)
                        > OWNER_ESCAPE_DISTANCE
                        * OWNER_ESCAPE_DISTANCE;

        if (!tooFar) {
            state.resetOutsideOwnerRangeTicks();
            return false;
        }

        state.incrementOutsideOwnerRangeTicks();

        return state.getOutsideOwnerRangeTicks()
                >= OWNER_ESCAPE_TICKS;
    }

    private static void endSpread(
            LivingEntity entity,
            ZeroGravityState state
    ) {
        state.clearZeroGravity();

        state.setRestorationTicks(
                RESTORATION_TICKS
        );

        entity.setNoGravity(false);
        entity.fallDistance = 0.0F;
    }

    private static void infect(
            LivingEntity source,
            LivingEntity target,
            ZeroGravityState state,
            UUID ownerUUID
    ) {
        state.setSource(
                ZeroGravitySource.SPREAD
        );

        state.setOwnerUUID(
                ownerUUID
        );

        state.resetZeroGravityTicks();
        state.resetAirPressureTicks();
        state.resetOutsideOwnerRangeTicks();

        target.setNoGravity(true);
        target.fallDistance = 0.0F;

        Vec3 direction =
                target.position()
                        .subtract(
                                source.position()
                        );

        if (direction.lengthSqr() < 0.0001D) {
            direction =
                    source.getDeltaMovement();
        }

        if (direction.lengthSqr() < 0.0001D) {
            direction =
                    new Vec3(
                            0.0D,
                            1.0D,
                            0.0D
                    );
        }

        direction = direction.normalize();

        Vec3 movement =
                target.getDeltaMovement();

        target.setDeltaMovement(
                movement.x
                        + direction.x * CONTACT_PUSH,
                movement.y
                        + Math.max(
                        0.02D,
                        direction.y * CONTACT_PUSH
                ),
                movement.z
                        + direction.z * CONTACT_PUSH
        );

        target.hasImpulse = true;
        target.hurtMarked = true;
    }
}