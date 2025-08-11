package net.hydra.jojomod.mixin.gravity.client;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class GravityClientPlayerEntityMixin extends AbstractClientPlayer {
    public GravityClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    protected abstract boolean suffocatesAt(BlockPos pos);

    @Inject(
            method = "suffocatesAt(Lnet/minecraft/core/BlockPos;)Z",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$redirect_wouldCollideAt_new_0(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN) {
            return;
        }
        AABB playerBox = this.getBoundingBox();
        Vec3 playerMask = RotationUtil.maskPlayerToWorld(0.0D, 1.0D, 0.0D, gravityDirection);
        AABB posBox = new AABB(pos);
        Vec3 posMask = RotationUtil.maskPlayerToWorld(1.0D, 0.0D, 1.0D, gravityDirection);

        AABB collision = new AABB(
                playerMask.multiply(playerBox.minX, playerBox.minY, playerBox.minZ).add(posMask.multiply(posBox.minX, posBox.minY, posBox.minZ)),
                playerMask.multiply(playerBox.maxX, playerBox.maxY, playerBox.maxZ).add(posMask.multiply(posBox.maxX, posBox.maxY, posBox.maxZ))
        );

        cir.setReturnValue(this.level().collidesWithSuffocatingBlock(this, collision));
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