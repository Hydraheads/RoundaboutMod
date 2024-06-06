package net.hydra.jojomod.entity;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IProjectileAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class TimeMovingProjectile
{
    public static void tick(Projectile projectile){
        Vec3 $$1 = projectile.getDeltaMovement();
        if (projectile.xRotO == 0.0F && projectile.yRotO == 0.0F) {
            double $$2 = $$1.horizontalDistance();
            projectile.setYRot((float)(Mth.atan2($$1.x, $$1.z) * 180.0F / (float)Math.PI));
            projectile.setXRot((float)(Mth.atan2($$1.y, $$2) * 180.0F / (float)Math.PI));
            projectile.yRotO = projectile.getYRot();
            projectile.xRotO = projectile.getXRot();
        }

        BlockPos $$3 = projectile.blockPosition();
        BlockState $$4 = projectile.level().getBlockState($$3);
        if (!$$4.isAir()) {
            VoxelShape $$5 = $$4.getCollisionShape(projectile.level(), $$3);
            if (!$$5.isEmpty()) {
                Vec3 $$6 = projectile.position();

                for (AABB $$7 : $$5.toAabbs()) {
                    if ($$7.move($$3).contains($$6)) {
                        if (projectile instanceof AbstractArrow) {
                            ((IAbstractArrowAccess)projectile).roundaboutSetInGround(true);
                            ((IProjectileAccess)projectile).setRoundaboutIsTimeStopCreated(false);
                        }
                        break;
                    }
                }
            }
        }

        if (projectile instanceof AbstractArrow) {
            if (((AbstractArrow)projectile).shakeTime > 0) {
                ((AbstractArrow)projectile).shakeTime--;
            }
        }

        if (projectile.isInWaterOrRain() || $$4.is(Blocks.POWDER_SNOW)) {
            projectile.clearFire();
        }

        if (projectile instanceof AbstractArrow && ((IAbstractArrowAccess)projectile).roundaboutGetInGround()){
            return;
        }



        float speedMod = ((IProjectileAccess) projectile).getRoundaboutSpeedMultiplier();
        Vec3 position = new Vec3(projectile.getX(),projectile.getY(),projectile.getZ());
        Vec3 reducedDelta = projectile.getDeltaMovement().multiply(speedMod,speedMod,speedMod);

        double $$14 = reducedDelta.x;
        double $$15 = reducedDelta.y;
        double $$16 = reducedDelta.z;

        double $$21 = reducedDelta.horizontalDistance();


        if (speedMod > 0.01) {

            Vec3 pos = position;
            Vec3 pos2 = position.add(projectile.getDeltaMovement()).add(projectile.getDeltaMovement()).add(reducedDelta);
            float inflateDist = (float) Math.max(Math.max(reducedDelta.x, reducedDelta.y), reducedDelta.z) * 2;
            HitResult mobHit = ProjectileUtil.getEntityHitResult(
                    projectile.level(), projectile, pos, pos2, projectile.getBoundingBox().expandTowards(projectile.getDeltaMovement()).inflate(1 + inflateDist), ((IProjectileAccess) projectile)::roundaboutCanHitEntity
            );
            if (mobHit != null) {
                speedMod *= 0.7F;
            }

            reducedDelta = projectile.getDeltaMovement().multiply(speedMod, speedMod, speedMod);
            pos2 = position.add(projectile.getDeltaMovement()).add(reducedDelta);
            HitResult mobHit2 = ProjectileUtil.getEntityHitResult(
                    projectile.level(), projectile, pos, pos2, projectile.getBoundingBox().expandTowards(projectile.getDeltaMovement()).inflate(1), ((IProjectileAccess) projectile)::roundaboutCanHitEntity
            );
            if (mobHit2 != null) {
                speedMod *= 0.6F;

            }
            if (mobHit2 != null && mobHit != null) {
                if (speedMod <= 0.1) {
                    speedMod = 0.11F;
                }
                ((IProjectileAccess) projectile).setRoundaboutSpeedMultiplier(speedMod);
            }
        }



        Vec3 $$8 = projectile.position();
        Vec3 $$9 = $$8.add(projectile.getDeltaMovement());
        HitResult $$10 = projectile.level().clip(new ClipContext($$8, $$9, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile));
        if ($$10.getType() != HitResult.Type.MISS) {
            $$9 = $$10.getLocation();
        }

        if (projectile instanceof AbstractArrow) {
            while (!projectile.isRemoved()) {
                EntityHitResult $$11 = findHitEntity2(projectile,$$8, $$9, projectile.getDeltaMovement());
                if ($$11 != null) {
                    $$10 = $$11;
                }

                if ($$10 != null && $$10.getType() == HitResult.Type.ENTITY) {
                    Entity $$12 = ((EntityHitResult) $$10).getEntity();
                    Entity $$13 = projectile.getOwner();
                    if ($$12 instanceof Player && $$13 instanceof Player && !((Player) $$13).canHarmPlayer((Player) $$12)) {
                        $$10 = null;
                        $$11 = null;
                    }
                }

                if ($$10 != null && $$10.getType() == HitResult.Type.BLOCK) {
                    ((IProjectileAccess) projectile).setRoundaboutSpeedMultiplier(0);
                    ((IProjectileAccess)projectile).setRoundaboutIsTimeStopCreated(false);
                } else if ($$10 != null && $$10.getType() == HitResult.Type.ENTITY) {
                    ((IProjectileAccess) projectile).setRoundaboutSpeedMultiplier(0);
                    ((IProjectileAccess)projectile).setRoundaboutIsTimeStopCreated(false);

                }
                if ($$11 == null || ((IAbstractArrowAccess)projectile).roundaboutGetPierceLevel() <= 0) {
                    break;
                }

                $$10 = null;
            }
        }

        speedMod = ((IProjectileAccess) projectile).getRoundaboutSpeedMultiplier();
        reducedDelta = projectile.getDeltaMovement().multiply(speedMod,speedMod,speedMod);
        if (speedMod > 0.01) {
            ((IProjectileAccess) projectile).setRoundaboutSpeedMultiplier(speedMod* 0.87F);
            projectile.setPos(position.x + reducedDelta.x, position.y + reducedDelta.y, position.z + reducedDelta.z);
        } else {
            ((IProjectileAccess) projectile).setRoundaboutIsTimeStopCreated(false);
        }
    }

    protected static float lerpRotation(float $$0, float $$1) {
        while ($$1 - $$0 < -180.0F) {
            $$0 -= 360.0F;
        }

        while ($$1 - $$0 >= 180.0F) {
            $$0 += 360.0F;
        }

        return Mth.lerp(0.2F, $$0, $$1);
    }


    @Nullable
    protected static EntityHitResult findHitEntity2(Projectile projectile, Vec3 $$0, Vec3 $$1, Vec3 delta) {
        return ProjectileUtil.getEntityHitResult(
                projectile.level(), projectile, $$0, $$1, projectile.getBoundingBox().expandTowards(delta).inflate(1.0), ((IProjectileAccess) projectile)::roundaboutCanHitEntity
        );
    }
}
