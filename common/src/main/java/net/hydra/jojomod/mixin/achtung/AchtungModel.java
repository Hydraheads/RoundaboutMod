package net.hydra.jojomod.mixin.achtung;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Model.class, priority = 100)
public class AchtungModel {
    /***
     * Code for Achtung Baby Model Rendering!
     * The rendertype is essentially what models in the game (be it entity models or some item models) use
     * to determine their transparency. This mixin makes all of them see through, with the caveat of
     * the fabulous rendering setting doing something weird and making this impossible.
     *
     */
    @Inject(method = "renderType", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$renderType(ResourceLocation $$0, CallbackInfoReturnable<RenderType> cir) {
        float throwfade = ClientUtil.getThrowFadeToTheEther();
        if (throwfade != 1 && !ClientUtil.isFabulous()) {
            cir.setReturnValue(RenderType.entityTranslucentCull($$0));
        }
    }

}
