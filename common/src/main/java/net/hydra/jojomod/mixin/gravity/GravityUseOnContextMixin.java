package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UseOnContext.class)
public class GravityUseOnContextMixin {

    @Shadow @Final @Nullable private Player player;

    @Inject(
            method = "getRotation",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void wrapOperation_getPlayerYaw_getYaw_0(CallbackInfoReturnable<Float> cir) {
        if (this.player == null)
            return;
        Direction gravityDirection = GravityAPI.getGravityDirection(this.player);
        if (gravityDirection == Direction.DOWN)
            return;

        cir.setReturnValue(RotationUtil.rotPlayerToWorld(this.player.getYRot(), this.player.getXRot(), gravityDirection).x);
    }
}
