package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Projectile.class)
public abstract class GravityProjectileMixin {
    @ModifyVariable(
            method = "shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true)
    private float modify_setProperties_pitch(float value, Entity user, float yaw, float roll, float speed, float divergence) {
        Direction gravityDirection = GravityAPI.getGravityDirection(user);
        if (gravityDirection == Direction.DOWN) {
            return value;
        }

        return RotationUtil.rotPlayerToWorld(user.getYRot(), user.getXRot(), gravityDirection).y;
    }

    @ModifyVariable(
            method = "shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V",
            at = @At("HEAD"),
            ordinal = 1,
            argsOnly = true)
    private float modify_setProperties_yaw(float value, Entity user, float pitch, float roll, float speed, float divergence) {
        Direction gravityDirection = GravityAPI.getGravityDirection(user);
        if (gravityDirection == Direction.DOWN) {
            return value;
        }

        return RotationUtil.rotPlayerToWorld(user.getYRot(), user.getXRot(), gravityDirection).x;
    }
}