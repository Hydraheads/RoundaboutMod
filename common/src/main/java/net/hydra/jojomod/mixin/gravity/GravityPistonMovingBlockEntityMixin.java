package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonMovingBlockEntity.class)
public class GravityPistonMovingBlockEntityMixin {
    @Shadow @Final private static ThreadLocal<Direction> NOCLIP;

    @Inject(
            method = "moveEntityByPiston(Lnet/minecraft/core/Direction;Lnet/minecraft/world/entity/Entity;DLnet/minecraft/core/Direction;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private static void redirect_moveEntity_Vec3d_0(Direction $$0, Entity $$1, double $$2, Direction $$3, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$1);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        NOCLIP.set($$0);
        $$1.move(MoverType.PISTON, RotationUtil.vecWorldToPlayer($$2 * (double)$$3.getStepX(), $$2 * (double)$$3.getStepY(), $$2 * (double)$$3.getStepZ(),gravityDirection));
        NOCLIP.set(null);
    }
}
