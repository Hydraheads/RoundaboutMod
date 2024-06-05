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


        Vec3 $$8 = projectile.position();
        Vec3 $$9 = $$8.add($$1);
        HitResult $$10 = projectile.level().clip(new ClipContext($$8, $$9, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile));
        if ($$10.getType() != HitResult.Type.MISS) {
            $$9 = $$10.getLocation();
        }

        if (projectile instanceof AbstractArrow) {
            while (!projectile.isRemoved()) {
                EntityHitResult $$11 = ((IAbstractArrowAccess)projectile).roundaboutFindHitEntity($$8, $$9);
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
                    ((IProjectileAccess) projectile).roundaboutOnHit($$10);
                    projectile.hasImpulse = true;
                } else if ($$10 != null && $$10.getType() == HitResult.Type.BLOCK) {
                    ((IProjectileAccess) projectile).setRoundaboutSpeedMultiplier(0);
                    ((IProjectileAccess)projectile).setRoundaboutIsTimeStopCreated(false);

                }
                if ($$11 == null || ((IAbstractArrowAccess)projectile).roundaboutGetPierceLevel() <= 0) {
                    break;
                }

                $$10 = null;
            }
        }


        $$1 = projectile.getDeltaMovement();
        double $$14 = $$1.x;
        double $$15 = $$1.y;
        double $$16 = $$1.z;

        double $$18 = projectile.getX() + $$14;
        double $$19 = projectile.getY() + $$15;
        double $$20 = projectile.getZ() + $$16;
        double $$21 = $$1.horizontalDistance();
        projectile.setYRot((float)(Mth.atan2($$14, $$16) * 180.0F / (float)Math.PI));

        projectile.setXRot((float)(Mth.atan2($$15, $$21) * 180.0F / (float)Math.PI));
        projectile.setXRot(lerpRotation(projectile.xRotO, projectile.getXRot()));
        projectile.setYRot(lerpRotation(projectile.yRotO, projectile.getYRot()));
        float $$22 = 0.99F;
        float $$23 = 0.05F;


        float speedMod = ((IProjectileAccess) projectile).getRoundaboutSpeedMultiplier();
        Vec3 position = new Vec3(projectile.getX(),projectile.getY(),projectile.getZ());

        Vec3 reducedDelta = projectile.getDeltaMovement();
        reducedDelta =  reducedDelta.multiply(speedMod,speedMod,speedMod);

        Vec3 pos = position;
        Vec3 pos2 = position.add(projectile.getDeltaMovement()).add(reducedDelta);
        float inflateDist = (float) Math.max(Math.max(reducedDelta.x, reducedDelta.y), reducedDelta.z);
        HitResult mobHit =  ProjectileUtil.getEntityHitResult(
                projectile.level(), projectile, pos, pos2, projectile.getBoundingBox().expandTowards(projectile.getDeltaMovement()).inflate(1+inflateDist), ((IProjectileAccess) projectile)::roundaboutCanHitEntity
        );
        if (mobHit != null){
            ((IProjectileAccess) projectile).setRoundaboutSpeedMultiplier((float) (((IProjectileAccess) projectile).getRoundaboutSpeedMultiplier()*0.4));
        }

        if (speedMod > 0.01) {
            ((IProjectileAccess) projectile).setRoundaboutSpeedMultiplier((float) (speedMod*= 0.85F));
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



}
