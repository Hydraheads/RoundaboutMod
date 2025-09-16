package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=EntityCollisionContext.class,priority = 104)
public abstract class GravityEntityCollisionContextMixin {

    @Inject(
            method = "isAbove(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/core/BlockPos;Z)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void roundabout$isAbove(VoxelShape shape, BlockPos pos, boolean defaultValue, CallbackInfoReturnable<Boolean> cir) {
        if (this.entity == null) return;

        Direction gravityDirection = GravityAPI.getGravityDirection(this.entity);
        if (gravityDirection == Direction.DOWN) return;

        double realBottom = RotationUtil.boxWorldToPlayer(entity.getBoundingBox(), gravityDirection).minY;

        if (shape.isEmpty()) {
            cir.setReturnValue(true);
            return;
        }

        AABB shapeBox = RotationUtil.boxWorldToPlayer(
                shape.bounds().inflate(-9.999999747378752E-6D), gravityDirection
        );
        AABB posBox = RotationUtil.boxWorldToPlayer(new AABB(pos), gravityDirection);
        cir.setReturnValue(
                realBottom > posBox.minY + shapeBox.maxX
        );
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow
    @Final
    private Entity entity;

    @Shadow
    @Final
    private double entityBottom;
}
