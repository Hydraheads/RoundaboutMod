package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Direction.class, priority = 1001)
public abstract class GravityDirectionMixin {
    @Shadow @Final public static Direction NORTH;
    @Shadow @Final public static Direction SOUTH;
    @Shadow @Final public static Direction UP;
    @Shadow @Final public static Direction DOWN;
    @Shadow @Final public static Direction WEST;
    @Shadow @Final public static Direction EAST;

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


    @Shadow
    private static Direction[] makeDirectionArray(Direction direction, Direction direction2, Direction direction3) {
        return null;
    }

}