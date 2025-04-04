package net.hydra.jojomod.mixin;

import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalShape.class)
public class ZPortalShape {
    @Inject(method = "isEmpty(Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$isEmpty(BlockState $$0, CallbackInfoReturnable<Boolean> cir) {
        if ($$0.is(ModBlocks.STAND_FIRE)){
            cir.setReturnValue(true);
        }
    }
}
