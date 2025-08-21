package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class GravityItemMixin {
    @Inject(
            method = "getPlayerPOVHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/ClipContext$Fluid;)Lnet/minecraft/world/phys/BlockHitResult;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void rdbt$getPlayerPOVHitResult(Level $$0, Player $$1, ClipContext.Fluid $$2, CallbackInfoReturnable<BlockHitResult> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$1);
        if (gravityDirection == Direction.DOWN)
            return;

        float $$3 = RotationUtil.rotPlayerToWorld($$1.getYRot(), $$1.getXRot(), gravityDirection).y;
        float $$4 = RotationUtil.rotPlayerToWorld($$1.getYRot(), $$1.getXRot(), gravityDirection).x;
        Vec3 $$5 = $$1.getEyePosition();
        float $$6 = Mth.cos(-$$4 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float $$7 = Mth.sin(-$$4 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float $$8 = -Mth.cos(-$$3 * (float) (Math.PI / 180.0));
        float $$9 = Mth.sin(-$$3 * (float) (Math.PI / 180.0));
        float $$10 = $$7 * $$8;
        float $$12 = $$6 * $$8;
        double $$13 = 5.0;
        Vec3 $$14 = $$5.add((double)$$10 * 5.0, (double)$$9 * 5.0, (double)$$12 * 5.0);
        cir.setReturnValue($$0.clip(new ClipContext($$5, $$14, ClipContext.Block.OUTLINE, $$2, $$1)));
    }
}
