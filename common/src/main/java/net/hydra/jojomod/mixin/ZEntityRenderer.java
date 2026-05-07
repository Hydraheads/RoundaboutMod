package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class ZEntityRenderer<T extends Entity> {

    /**Cancel hitbox rendering for stuff like go beyond*/
    @Inject(method = "shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z", at = @At(value = "HEAD"), cancellable = true)
    private <E extends Entity>  void rdbt$renderHitbox(T $$0, Frustum $$1, double $$2, double $$3, double $$4, CallbackInfoReturnable<Boolean> cir) {
        if (ClientUtil.forceEntityRendering($$0)){
            cir.setReturnValue(true);
        }
    }
}
