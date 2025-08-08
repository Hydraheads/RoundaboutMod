package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Direction.class, priority = 1001)
public abstract class GravityDirectionMixin {
    /**Adjusts direction finders with current gravity*/

    @ModifyVariable(method = "orderedByNearest", at = @At(value = "STORE"), ordinal = 1
            )
    private static float roundabout$orderByNearest1(float f1, Entity entity) {
        Direction gravityDirection = GravityAPI.getGravityDirection(entity);
        if (gravityDirection != Direction.DOWN){
            return RotationUtil.rotPlayerToWorld(f1,entity.getViewXRot(1F),gravityDirection).x;
        }
        return f1;
    }

    @ModifyVariable(method = "orderedByNearest", at = @At(value = "STORE"), ordinal = 0
    )
    private static float roundabout$orderByNearest2(float f1, Entity entity) {
        Direction gravityDirection = GravityAPI.getGravityDirection(entity);
        if (gravityDirection != Direction.DOWN){
            return RotationUtil.rotPlayerToWorld(f1,entity.getViewYRot(1F),gravityDirection).y;
        }
        return f1;
    }


    @Inject(method = "getFacingAxis", at = @At(value = "HEAD"), cancellable = true
    )
    private static void roundabout$getFacingAxis(Entity entity, Direction.Axis $$1, CallbackInfoReturnable<Direction> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection(entity);
        if (gravityDirection != Direction.DOWN){
            switch ($$1) {
                case X -> {
                    if (Direction.EAST.isFacingAngle(
                            RotationUtil.rotPlayerToWorld(entity.getViewYRot(1.0F), entity.getViewXRot(1f), gravityDirection).x
                    ))
                        cir.setReturnValue(Direction.EAST);
                    else
                        cir.setReturnValue(Direction.WEST);
                }
                case Z -> {
                    if (Direction.SOUTH.isFacingAngle(
                            RotationUtil.rotPlayerToWorld(entity.getViewYRot(1.0F), entity.getViewXRot(1f), gravityDirection).x
                    ))
                        cir.setReturnValue(Direction.SOUTH);
                    else
                        cir.setReturnValue(Direction.NORTH);
                }
                case Y -> {
                    if (
                            RotationUtil.rotPlayerToWorld(entity.getViewYRot(1f), entity.getViewXRot(1.0F), gravityDirection).y
                                    < 0.0F)
                        cir.setReturnValue(Direction.UP);
                    else
                        cir.setReturnValue(Direction.DOWN);
                }
            };

        }
    }

}