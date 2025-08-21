package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ShulkerBoxBlockEntity.class, priority = 1001)
public abstract class GravityShulkerBoxBlockEntityMixin {
    @Shadow private float progressOld;

    @Shadow private float progress;

    @Inject(
            method = "moveCollidedEntities",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$moveCollidedEntities(Level $$0, BlockPos $$1, BlockState $$2, CallbackInfo ci) {
        ci.cancel();
        if ($$2.getBlock() instanceof ShulkerBoxBlock) {
            Direction $$3 = $$2.getValue(ShulkerBoxBlock.FACING);
            AABB $$4 = Shulker.getProgressDeltaAabb($$3, this.progressOld, this.progress).move($$1);
            List<Entity> $$5 = $$0.getEntities(null, $$4);
            if (!$$5.isEmpty()) {
                for (int $$6 = 0; $$6 < $$5.size(); $$6++) {
                    Entity $$7 = $$5.get($$6);
                    if ($$7.getPistonPushReaction() != PushReaction.IGNORE) {

                        Direction gravityDirection = GravityAPI.getGravityDirection($$7);
                        if (gravityDirection == Direction.DOWN) {
                            $$7.move(
                                    MoverType.SHULKER_BOX,
                                    new Vec3(
                                            ($$4.getXsize() + 0.01) * (double) $$3.getStepX(),
                                            ($$4.getYsize() + 0.01) * (double) $$3.getStepY(),
                                            ($$4.getZsize() + 0.01) * (double) $$3.getStepZ()
                                    )
                            );
                        } else {
                            $$7.move(
                                    MoverType.SHULKER_BOX,
                                    RotationUtil.vecWorldToPlayer(new Vec3(
                                            ($$4.getXsize() + 0.01) * (double) $$3.getStepX(),
                                            ($$4.getYsize() + 0.01) * (double) $$3.getStepY(),
                                            ($$4.getZsize() + 0.01) * (double) $$3.getStepZ()
                                    ), gravityDirection)
                            );
                        }
                    }
                }
            }
        }
    }
}