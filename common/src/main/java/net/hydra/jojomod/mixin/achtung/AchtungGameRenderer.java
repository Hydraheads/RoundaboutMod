package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class AchtungGameRenderer {
    /***
     * Just being extra careful that the transparency achtung baby creates is getting reset in potential
     * spill-over areas in case mods are canceling things. This is the tail end of the renderItemInHand
     * function.
     */
    @Inject(method = "renderItemInHand", at = @At(value = "TAIL"))
    private void roundabout$renderItemInHand(PoseStack $$0, Camera $$1, float $$2, CallbackInfo ci)
    {
        ClientUtil.setThrowFadeToTheEther(1);
        ClientUtil.hideInvis = false;
    }
}
