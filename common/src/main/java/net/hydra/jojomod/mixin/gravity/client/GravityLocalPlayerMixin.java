package net.hydra.jojomod.mixin.gravity.client;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.stream.StreamSupport;

@Mixin(LocalPlayer.class)
public abstract class GravityLocalPlayerMixin extends AbstractClientPlayer {
    public GravityLocalPlayerMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    protected abstract boolean suffocatesAt(BlockPos pos);

    @Shadow protected abstract void updateAutoJump(float f, float g);

    @Shadow private int autoJumpTime;

    @Shadow public Input input;

    @Shadow protected abstract boolean canAutoJump();

    @Inject(
            method = "suffocatesAt(Lnet/minecraft/core/BlockPos;)Z",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$collision(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN)
            return;

        AABB $$1 = this.getBoundingBox();
        AABB playerBox = this.getBoundingBox();
        Vec3 playerMask = RotationUtil.maskPlayerToWorld(0.0D, 1.0D, 0.0D, gravityDirection);
        AABB posBox = new AABB(pos);
        Vec3 posMask = RotationUtil.maskPlayerToWorld(1.0D, 0.0D, 1.0D, gravityDirection);

        AABB $$2 = new AABB(
                playerMask.multiply(playerBox.minX, playerBox.minY, playerBox.minZ).add(posMask.multiply(posBox.minX, posBox.minY, posBox.minZ)),
                playerMask.multiply(playerBox.maxX, playerBox.maxY, playerBox.maxZ).add(posMask.multiply(posBox.maxX, posBox.maxY, posBox.maxZ))
        ).deflate(1.0E-7);
        cir.setReturnValue(this.level().collidesWithSuffocatingBlock(this, $$2));
    }

    @Inject(
            method = "move",
            at = @At("HEAD"),
            cancellable = true
    )
    private void rdbt$updateAutoJumpmove(MoverType $$0, Vec3 $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN) return;
        ci.cancel();

        if (gravityDirection == Direction.NORTH || gravityDirection == Direction.SOUTH) {
            double $$2 = this.getX();
            double $$3 = this.getY();
            super.move($$0, $$1);
            this.updateAutoJump((float) (this.getX() - $$2), (float) (this.getY() - $$3));
        } else if (gravityDirection == Direction.EAST || gravityDirection == Direction.WEST) {
            double $$2 = this.getY();
            double $$3 = this.getZ();
            super.move($$0, $$1);
            this.updateAutoJump((float) (this.getY() - $$2), (float) (this.getZ() - $$3));
        } else {
            double $$2 = this.getX();
            double $$3 = this.getZ();
            super.move($$0, $$1);
            this.updateAutoJump((float) (this.getX() - $$2), (float) (this.getZ() - $$3));
        }
    }

    @Inject(
            method = "updateAutoJump(FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void rdbt$updateAutoJump(float $$0, float $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN) return;
        ci.cancel();

        if (true == true)
            return;
        //we can maybe look into auto jump more one day

        if (gravityDirection == Direction.NORTH || gravityDirection == Direction.SOUTH) {


            if (this.canAutoJump()) {
                Vec3 $$2 = this.position();
                Vec3 $$3 = $$2.add((double)$$0, (double)$$1, 0);
                Vec3 $$4 = new Vec3((double)$$0, (double)$$1,0);
                float $$5 = this.getSpeed();
                float $$6 = (float)$$4.lengthSqr();
                if ($$6 <= 0.001F) {
                    Vec2 $$7 = this.input.getMoveVector();
                    float $$8 = $$5 * $$7.x;
                    float $$9 = $$5 * $$7.y;
                    float $$10 = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0));
                    float $$11 = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0));
                    $$4 = new Vec3((double)($$8 * $$11 - $$9 * $$10), $$4.y, (double)($$9 * $$11 + $$8 * $$10));
                    $$6 = (float)$$4.lengthSqr();
                    if ($$6 <= 0.001F) {
                        return;
                    }
                }

                float $$12 = Mth.invSqrt($$6);
                Vec3 $$13 = $$4.scale((double)$$12);
                Vec3 $$14 = this.getForward();
                float $$15 = (float)($$14.x * $$13.x + $$14.y * $$13.y);
                if (!($$15 < -0.15F)) {
                    CollisionContext $$16 = CollisionContext.of(this);
                    BlockPos $$17 = BlockPos.containing(this.getX(), this.getY(), this.getBoundingBox().maxZ);
                    BlockState $$18 = this.level().getBlockState($$17);
                    if ($$18.getCollisionShape(this.level(), $$17, $$16).isEmpty()) {
                        $$17 = $$17.relative(gravityDirection.getOpposite());
                        BlockState $$19 = this.level().getBlockState($$17);
                        if ($$19.getCollisionShape(this.level(), $$17, $$16).isEmpty()) {
                            float $$20 = 7.0F;
                            float $$21 = 1.2F;
                            if (this.hasEffect(MobEffects.JUMP)) {
                                $$21 += (float)(this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.75F;
                            }

                            float $$22 = Math.max($$5 * 7.0F, 1.0F / $$12);
                            Vec3 $$24 = $$3.add($$13.scale((double)$$22));
                            float $$25 = this.getBbWidth();
                            float $$26 = this.getBbHeight();
                            AABB $$27 = new AABB($$2, $$24.add(0.0, (double)$$26, 0.0)).inflate((double)$$25, 0.0, (double)$$25);
                            Vec3 $$23 = $$2.add(0.0, 0.51F, 0.0);
                            $$24 = $$24.add(0.0, 0.51F, 0.0);
                            Vec3 $$28 = $$13.cross(new Vec3(0.0, 1.0, 0.0));
                            Vec3 $$29 = $$28.scale((double)($$25 * 0.5F));
                            Vec3 $$30 = $$23.subtract($$29);
                            Vec3 $$31 = $$24.subtract($$29);
                            Vec3 $$32 = $$23.add($$29);
                            Vec3 $$33 = $$24.add($$29);
                            Iterable<VoxelShape> $$34 = this.level().getCollisions(this, $$27);
                            Iterator<AABB> $$35 = StreamSupport.stream($$34.spliterator(), false).flatMap($$0x -> $$0x.toAabbs().stream()).iterator();
                            float $$36 = Float.MIN_VALUE;

                            while ($$35.hasNext()) {
                                AABB $$37 = $$35.next();
                                if ($$37.intersects($$30, $$31) || $$37.intersects($$32, $$33)) {
                                    $$36 = (float)$$37.maxY;
                                    Vec3 $$38 = $$37.getCenter();
                                    BlockPos $$39 = BlockPos.containing($$38);

                                    for (int $$40 = 1; (float)$$40 < $$21; $$40++) {
                                        BlockPos $$41 = $$39.relative(gravityDirection.getOpposite());
                                        BlockState $$42 = this.level().getBlockState($$41);
                                        VoxelShape $$43;
                                        if (!($$43 = $$42.getCollisionShape(this.level(), $$41, $$16)).isEmpty()) {
                                            $$36 = (float)$$43.max(Direction.Axis.Y) + (float)$$41.getY();
                                            if ((double)$$36 - this.getY() > (double)$$21) {
                                                return;
                                            }
                                        }

                                        if ($$40 > 1) {
                                            $$17 = $$17.relative(gravityDirection.getOpposite());
                                            BlockState $$44 = this.level().getBlockState($$17);
                                            if (!$$44.getCollisionShape(this.level(), $$17, $$16).isEmpty()) {
                                                return;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }

                            if ($$36 != Float.MIN_VALUE) {
                                float $$45 = (float)((double)$$36 - this.getY());
                                if (!($$45 <= 0.5F) && !($$45 > $$21)) {
                                    this.autoJumpTime = 1;
                                }
                            }
                        }
                    }
                }
            }






        } else if (gravityDirection == Direction.EAST || gravityDirection == Direction.WEST) {

            if (this.canAutoJump()) {
                Vec3 $$2 = this.position();
                Vec3 $$3 = $$2.add((double)0.0, $$0, (double)$$1);
                Vec3 $$4 = new Vec3((double)0.0, $$0, (double)$$1);
                float $$5 = this.getSpeed();
                float $$6 = (float)$$4.lengthSqr();
                if ($$6 <= 0.001F) {
                    Vec2 $$7 = this.input.getMoveVector();
                    float $$8 = $$5 * $$7.x;
                    float $$9 = $$5 * $$7.y;
                    float $$10 = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0));
                    float $$11 = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0));
                    $$4 = new Vec3((double)($$8 * $$11 - $$9 * $$10), $$4.y, (double)($$9 * $$11 + $$8 * $$10));
                    $$6 = (float)$$4.lengthSqr();
                    if ($$6 <= 0.001F) {
                        return;
                    }
                }

                float $$12 = Mth.invSqrt($$6);
                Vec3 $$13 = $$4.scale((double)$$12);
                Vec3 $$14 = this.getForward();
                float $$15 = (float)($$14.y * $$13.y + $$14.z * $$13.z);
                if (!($$15 < -0.15F)) {
                    CollisionContext $$16 = CollisionContext.of(this);
                    BlockPos $$17 = BlockPos.containing(this.getBoundingBox().maxX, this.getY(), this.getZ());
                    BlockState $$18 = this.level().getBlockState($$17);
                    if ($$18.getCollisionShape(this.level(), $$17, $$16).isEmpty()) {
                        $$17 = $$17.relative(gravityDirection.getOpposite());
                        BlockState $$19 = this.level().getBlockState($$17);
                        if ($$19.getCollisionShape(this.level(), $$17, $$16).isEmpty()) {
                            float $$20 = 7.0F;
                            float $$21 = 1.2F;
                            if (this.hasEffect(MobEffects.JUMP)) {
                                $$21 += (float)(this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.75F;
                            }

                            float $$22 = Math.max($$5 * 7.0F, 1.0F / $$12);
                            Vec3 $$24 = $$3.add($$13.scale((double)$$22));
                            float $$25 = this.getBbWidth();
                            float $$26 = this.getBbHeight();
                            AABB $$27 = new AABB($$2, $$24.add(0.0, (double)$$26, 0.0)).inflate((double)$$25, 0.0, (double)$$25);
                            Vec3 $$23 = $$2.add(0.0, 0.51F, 0.0);
                            $$24 = $$24.add(0.0, 0.51F, 0.0);
                            Vec3 $$28 = $$13.cross(new Vec3(0.0, 1.0, 0.0));
                            Vec3 $$29 = $$28.scale((double)($$25 * 0.5F));
                            Vec3 $$30 = $$23.subtract($$29);
                            Vec3 $$31 = $$24.subtract($$29);
                            Vec3 $$32 = $$23.add($$29);
                            Vec3 $$33 = $$24.add($$29);
                            Iterable<VoxelShape> $$34 = this.level().getCollisions(this, $$27);
                            Iterator<AABB> $$35 = StreamSupport.stream($$34.spliterator(), false).flatMap($$0x -> $$0x.toAabbs().stream()).iterator();
                            float $$36 = Float.MIN_VALUE;

                            while ($$35.hasNext()) {
                                AABB $$37 = $$35.next();
                                if ($$37.intersects($$30, $$31) || $$37.intersects($$32, $$33)) {
                                    $$36 = (float)$$37.maxY;
                                    Vec3 $$38 = $$37.getCenter();
                                    BlockPos $$39 = BlockPos.containing($$38);

                                    for (int $$40 = 1; (float)$$40 < $$21; $$40++) {
                                        BlockPos $$41 = $$39.relative(gravityDirection.getOpposite());;
                                        BlockState $$42 = this.level().getBlockState($$41);
                                        VoxelShape $$43;
                                        if (!($$43 = $$42.getCollisionShape(this.level(), $$41, $$16)).isEmpty()) {
                                            $$36 = (float)$$43.max(Direction.Axis.Y) + (float)$$41.getY();
                                            if ((double)$$36 - this.getY() > (double)$$21) {
                                                return;
                                            }
                                        }

                                        if ($$40 > 1) {
                                            $$17 = $$17.relative(gravityDirection.getOpposite());
                                            BlockState $$44 = this.level().getBlockState($$17);
                                            if (!$$44.getCollisionShape(this.level(), $$17, $$16).isEmpty()) {
                                                return;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }

                            if ($$36 != Float.MIN_VALUE) {
                                float $$45 = (float)((double)$$36 - this.getY());
                                if (!($$45 <= 0.5F) && !($$45 > $$21)) {
                                    this.autoJumpTime = 1;
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    @Inject(
            method = "moveTowardsClosestSpace(DD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void rdbt$inject_pushOutOfBlocks(double x, double z, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN) return;

        ci.cancel();

        Vec3 pos = RotationUtil.vecPlayerToWorld(x - this.getX(), 0.0D, z - this.getZ(), gravityDirection).add(this.position());
        BlockPos blockPos = BlockPos.containing(pos);
        if (this.suffocatesAt(blockPos)) {
            double dx = pos.x - (double) blockPos.getX();
            double dy = pos.y - (double) blockPos.getY();
            double dz = pos.z - (double) blockPos.getZ();
            Direction direction = null;
            double minDistToEdge = Double.MAX_VALUE;

            Direction[] directions = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
            for (Direction playerDirection : directions) {
                Direction worldDirection = RotationUtil.dirPlayerToWorld(playerDirection, gravityDirection);

                double g = worldDirection.getAxis().choose(dx, dy, dz);
                double distToEdge = worldDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0D - g : g;
                if (distToEdge < minDistToEdge && !this.suffocatesAt(blockPos.relative(worldDirection))) {
                    minDistToEdge = distToEdge;
                    direction = playerDirection;
                }
            }

            if (direction != null) {
                Vec3 velocity = this.getDeltaMovement();
                if (direction.getAxis() == Direction.Axis.X) {
                    this.setDeltaMovement(0.1D * (double) direction.getStepX(), velocity.y, velocity.z);
                }
                else if (direction.getAxis() == Direction.Axis.Z) {
                    this.setDeltaMovement(velocity.x, velocity.y, 0.1D * (double) direction.getStepZ());
                }
            }
        }
    }
}