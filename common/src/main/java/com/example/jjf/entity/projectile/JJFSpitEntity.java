package com.example.jjf.entity.projectile;

import com.example.jjf.registry.ModEntities;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravitySource;
import com.example.jjf.zero_gravity.ZeroGravityState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class JJFSpitEntity extends ThrowableProjectile {

    private static final double MAX_RANGE = 20.0D;
    private static final int MAX_LIFE_TICKS = 40;

    private Vec3 startPos;

    public JJFSpitEntity(
            EntityType<? extends JJFSpitEntity> type,
            Level level
    ) {
        super(type, level);
    }

    public JJFSpitEntity(
            Level level,
            LivingEntity owner
    ) {
        super(
                ModEntities.JJF_SPIT,
                owner,
                level
        );
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected float getGravity() {
        return 0.01F;
    }

    @Override
    public void tick() {
        if (startPos == null) {
            startPos = position();
        }

        super.tick();

        if (level().isClientSide()) {
            level().addParticle(
                    ParticleTypes.SPLASH,
                    getX(),
                    getY(),
                    getZ(),
                    0.0D,
                    0.0D,
                    0.0D
            );
        }

        if (tickCount >= MAX_LIFE_TICKS) {
            discard();
            return;
        }

        if (startPos != null
                && position().distanceTo(startPos) >= MAX_RANGE) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(
            EntityHitResult result
    ) {
        if (level().isClientSide()) {
            return;
        }

        Entity target =
                result.getEntity();

        Entity owner =
                getOwner();

        if (!(target instanceof LivingEntity living)) {
            discard();
            return;
        }

        if (owner != null
                && owner.getUUID().equals(living.getUUID())) {
            discard();
            return;
        }

        if (!(living instanceof ZeroGravityAccess access)) {
            discard();
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (state.getSource()
                == ZeroGravitySource.USER_TOGGLE) {
            discard();
            return;
        }

        if (state.getSource()
                == ZeroGravitySource.SPREAD) {
            discard();
            return;
        }

        state.setSource(
                ZeroGravitySource.SPREAD
        );

        if (owner != null) {
            state.setOwnerUUID(
                    owner.getUUID()
            );
        }

        state.resetZeroGravityTicks();
        state.resetAirPressureTicks();
        state.setRestorationTicks(0);

        living.setNoGravity(true);
        living.fallDistance = 0.0F;

        Vec3 motion =
                getDeltaMovement();

        if (motion.lengthSqr() > 0.0001D) {
            Vec3 push =
                    motion.normalize()
                            .scale(0.12D);

            living.setDeltaMovement(
                    living.getDeltaMovement()
                            .add(
                                    push.x,
                                    0.035D,
                                    push.z
                            )
            );

            living.hasImpulse = true;
            living.hurtMarked = true;
        }

        spawnImpactParticles(
                living
        );

        discard();
    }

    @Override
    protected void onHitBlock(
            BlockHitResult result
    ) {
        if (!level().isClientSide()) {
            spawnMissParticles();
        }

        discard();
    }

    private void spawnImpactParticles(
            LivingEntity target
    ) {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }

        server.sendParticles(
                ParticleTypes.SPLASH,
                target.getX(),
                target.getY()
                        + target.getBbHeight() * 0.5D,
                target.getZ(),
                12,
                0.25D,
                0.25D,
                0.25D,
                0.06D
        );
    }

    private void spawnMissParticles() {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }

        server.sendParticles(
                ParticleTypes.SPLASH,
                getX(),
                getY(),
                getZ(),
                8,
                0.15D,
                0.15D,
                0.15D,
                0.04D
        );
    }
}