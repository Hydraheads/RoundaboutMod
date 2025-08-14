package net.hydra.jojomod.mixin.gravity.client;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemPickupParticle.class)
public abstract class GravityItemParticlePickupMixin {

    @Shadow
    @Final
    private Entity target;

    @ModifyVariable(
            method = "render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getX()D",
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0
    )
    private double modify_buildGeometry_double_0(double value) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.target);
        if (gravityDirection == Direction.DOWN) {
            return value;
        }

        return value + RotationUtil.vecPlayerToWorld(0.0D, 0.5D, 0.0D, gravityDirection).x;
    }

    @ModifyVariable(
            method = "render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getX()D",
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            ordinal = 1
    )
    private double modify_buildGeometry_double_1(double value) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.target);
        if (gravityDirection == Direction.DOWN) {
            return value;
        }

        return value - 0.5D + RotationUtil.vecPlayerToWorld(0.0D, 0.5D, 0.0D, gravityDirection).y;
    }

    @ModifyVariable(
            method = "render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getX()D",
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            ordinal = 2
    )
    private double modify_buildGeometry_double_2(double value) {
        Direction gravityDirection = GravityAPI.getGravityDirection(this.target);
        if (gravityDirection == Direction.DOWN) {
            return value;
        }

        return value + RotationUtil.vecPlayerToWorld(0.0D, 0.5D, 0.0D, gravityDirection).z;
    }
    //@Shadow
    //@Final
    //private Entity target;

    //@Shadow
    //private double targetX;

    //@Shadow
    //private double targetY;

    //@Shadow
    //private double targetZ;

    /**
     * Make item absorption destination correct.
     * @author qouteall
     * @reason simpler than multiple injections
     */
    //@Overwrite
    //private void updatePosition() {
    //    Vec3 entityPos = target.position();
    //    Vec3 eyePos = target.getEyePosition();
    //    Vec3 mid = eyePos.add(entityPos).scale(0.5);

    //    this.targetX = mid.x();
     //   this.targetY = mid.y();
     //   this.targetZ = mid.z();
    //}
}