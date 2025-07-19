package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Model.class)
public class ZModel {

    @Inject(method = "renderType", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$renderType(ResourceLocation $$0, CallbackInfoReturnable<RenderType> cir) {
        float throwfade = ClientUtil.getThrowFadeToTheEther();
        if (throwfade != 1 && !ClientUtil.isFabulous()) {
            cir.setReturnValue(RenderType.entityTranslucentCull($$0));
        }
    }
}
