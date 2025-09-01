package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MoveControl.class)
public abstract class GravityMoveControlMixin implements Control {

    @Shadow
    @Final
    protected Mob mob;


    @Shadow protected double speedModifier;

    @Shadow protected abstract float rotlerp(float f, float g, float h);

    @Shadow protected double wantedX;
    @Shadow protected double wantedZ;
    @Shadow protected double wantedY;
    @Shadow protected float strafeForwards;
    @Shadow protected float strafeRight;

    @Shadow protected abstract boolean isWalkable(float f, float g);

    @Unique
    protected byte rdbt$operation;

    @Inject(
            method = "strafe",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rdbt$strafe(float $$0, float $$1, CallbackInfo ci) {
        if (this.mob != null) {
            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
            if (dir == Direction.DOWN)
                return;
            ci.cancel();
            rdbt$operation = RDBT_STRAFE;
            this.strafeForwards = $$0;
            this.strafeRight = $$1;
            this.speedModifier = 0.25;
        }
    }
    @Inject(
            method = "setWantedPosition(DDDD)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rdbt$setWantedPosition(double $$0, double $$1, double $$2, double $$3,CallbackInfo ci) {
        if (this.mob != null) {
            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
            if (dir == Direction.DOWN)
                return;

            ci.cancel();
            this.wantedX = $$0;
            this.wantedY = $$1;
            this.wantedZ = $$2;
            this.speedModifier = $$3;
            if (rdbt$operation != RDBT_JUMPING) {
                rdbt$operation = RDBT_MOVE_TO;
            }
        }
    }
    @Inject(
            method = "tick",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rdbt$tick(CallbackInfo ci) {
        if (this.mob != null) {
            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
            if (dir == Direction.DOWN)
                return;
            ci.cancel();

            if (rdbt$operation ==RDBT_STRAFE) {
                float $$0 = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float $$1 = (float)this.speedModifier * $$0;
                float $$2 = this.strafeForwards;
                float $$3 = this.strafeRight;
                float $$4 = Mth.sqrt($$2 * $$2 + $$3 * $$3);
                if ($$4 < 1.0F) {
                    $$4 = 1.0F;
                }

                $$4 = $$1 / $$4;
                $$2 *= $$4;
                $$3 *= $$4;
                float $$5 = Mth.sin(this.mob.getYRot() * (float) (Math.PI / 180.0));
                float $$6 = Mth.cos(this.mob.getYRot() * (float) (Math.PI / 180.0));
                float $$7 = $$2 * $$6 - $$3 * $$5;
                float $$8 = $$3 * $$6 + $$2 * $$5;
                if (!this.isWalkable($$7, $$8)) {
                    this.strafeForwards = 1.0F;
                    this.strafeRight = 0.0F;
                }

                this.mob.setSpeed($$1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                rdbt$operation = RDBT_WAIT;
            } else if (rdbt$operation == RDBT_MOVE_TO) {
                rdbt$operation = RDBT_WAIT;

                boolean debug = true;

                LivingEntity entity = this.mob.getTarget();
                if (debug) {
                    //this.mob.setPos(this.wantedX,this.wantedY,this.wantedZ);
                    if (entity != null){
                        this.wantedX = entity.getX();
                        this.wantedY = entity.getY();
                        this.wantedZ = entity.getZ();
                    }
                    //this.mob.setPos(this.wantedX,this.wantedY,this.wantedZ);



                    //return;
                }

                double newX = this.wantedX - this.mob.getX();
                double newZ = this.wantedZ - this.mob.getZ();
                double newY = this.wantedY - this.mob.getY();


                double $$12 = newX * newX + newY * newY + newZ * newZ;
                if ($$12 < 2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                    return;
                }

                Vec3 newFlip = new Vec3(newX,newY,newZ);

                float $$13 = (float)(Mth.atan2(newFlip.z, newFlip.x) * 180.0F / (float)Math.PI) - 90.0F;


                if (debug) {
                    //this.mob.setPos(this.wantedX,this.wantedY,this.wantedZ);
                    if (entity != null) {
                        this.mob.lookAt(entity,30,30);
                    }
                } else {
                    Vec2 vec2 = new Vec2($$13,this.mob.getXRot());
                    //vec2 = RotationUtil.rotPlayerToWorld(vec2,dir);
                    this.mob.setYRot(this.rotlerp(this.mob.getYRot(), vec2.x, 90.0F));
                }
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));



                BlockPos $$14 = this.mob.blockPosition();
                BlockState $$15 = this.mob.level().getBlockState($$14);
                VoxelShape $$16 = $$15.getCollisionShape(this.mob.level(), $$14);

                boolean check = this.mob.getY() < $$16.max(Direction.Axis.Y) + (double)$$14.getY();
                if (dir == Direction.NORTH)
                    check = this.mob.getZ() < $$16.max(Direction.Axis.Z) + (double)$$14.getZ();
                if (dir == Direction.SOUTH)
                    check = this.mob.getZ() > $$16.max(Direction.Axis.Z) + (double)$$14.getZ();
                if (dir == Direction.WEST)
                    check = this.mob.getX() < $$16.max(Direction.Axis.X) + (double)$$14.getX();
                if (dir == Direction.EAST)
                    check = this.mob.getX() > $$16.max(Direction.Axis.X) + (double)$$14.getX();
                if (dir == Direction.UP)
                    check = this.mob.getY() > $$16.max(Direction.Axis.Y) + (double)$$14.getY();


                boolean checkX = false;
                if (dir == Direction.UP)
                    checkX = newFlip.y < (double)this.mob.maxUpStep() && newFlip.x * newFlip.x + newFlip.z * newFlip.z < (double)Math.max(1.0F, this.mob.getBbWidth());
                if (dir == Direction.NORTH)
                    checkX = newFlip.z > (double)this.mob.maxUpStep() && newFlip.x * newFlip.x + newFlip.y * newFlip.y < (double)Math.max(1.0F, this.mob.getBbWidth());
                if (dir == Direction.SOUTH)
                    checkX = newFlip.z < (double)this.mob.maxUpStep() && newFlip.x * newFlip.x + newFlip.y * newFlip.y < (double)Math.max(1.0F, this.mob.getBbWidth());
                if (dir == Direction.WEST)
                    checkX = newFlip.x > (double)this.mob.maxUpStep() && newFlip.z * newFlip.z + newFlip.y * newFlip.y < (double)Math.max(1.0F, this.mob.getBbWidth());
                if (dir == Direction.EAST)
                    checkX = newFlip.x < (double)this.mob.maxUpStep() && newFlip.z * newFlip.z + newFlip.y * newFlip.y < (double)Math.max(1.0F, this.mob.getBbWidth());

                if (checkX
                        || !$$16.isEmpty()
                        && check
                        && !$$15.is(BlockTags.DOORS)
                        && !$$15.is(BlockTags.FENCES)) {
                    this.mob.getJumpControl().jump();
                    rdbt$operation = RDBT_JUMPING;
                }
            } else if (rdbt$operation == RDBT_JUMPING) {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.mob.onGround()) {
                    rdbt$operation = RDBT_WAIT;
                }
            } else {
                this.mob.setZza(0.0F);
            }
        }
    }

    @Inject(
            method = "isWalkable(FF)Z",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rdbt$isWalkable(float $$0, float $$1, CallbackInfoReturnable<Boolean> cir) {
        if (this.mob != null) {
            Direction dir = ((IGravityEntity) this.mob).roundabout$getGravityDirection();
            if (dir == Direction.DOWN)
                return;

            PathNavigation $$2 = this.mob.getNavigation();
            if ($$2 != null) {
                NodeEvaluator $$3 = $$2.getNodeEvaluator();

                if (dir == Direction.UP) {
                    if ($$3 != null
                            && $$3.getBlockPathType(this.mob.level(),
                            Mth.floor(this.mob.getX() + (double) $$0),
                            this.mob.getBlockY(),
                            Mth.floor(this.mob.getZ() + (double) $$1))
                            != BlockPathTypes.WALKABLE) {
                        cir.setReturnValue(false);
                        return;
                    }
                }
                if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                    if ($$3 != null
                            && $$3.getBlockPathType(this.mob.level(),
                            Mth.floor(this.mob.getX() + (double) $$0),
                            Mth.floor(this.mob.getY() + (double) $$1),
                            this.mob.getBlockZ())

                            != BlockPathTypes.WALKABLE) {
                        cir.setReturnValue(false);
                        return;
                    }
                }
                if (dir == Direction.EAST || dir == Direction.WEST) {
                    if ($$3 != null
                            && $$3.getBlockPathType(this.mob.level(),
                            this.mob.getBlockX(),
                            Mth.floor(this.mob.getY() + (double) $$0),
                            Mth.floor(this.mob.getZ() + (double) $$1))
                            != BlockPathTypes.WALKABLE) {
                        cir.setReturnValue(false);
                        return;
                    }
                }
            }

            cir.setReturnValue(true);

        }
    }
    @Unique
    protected byte
        RDBT_WAIT = 0,
            RDBT_MOVE_TO = 1,
            RDBT_STRAFE = 2,
            RDBT_JUMPING = 3;
}
