package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowLayer.class)
public class AchtungArrowLayer {
    /***
     * things stuck in you shouldn't be rendering while you are invisible*/
    @Inject(method = "renderStuckItem", at = @At(value = "HEAD"), cancellable = true)
    protected <E extends Entity>  void roundabout$render(PoseStack $$0, MultiBufferSource $$1, int $$2, Entity $$3, float $$4, float $$5, float $$6, float $$7, CallbackInfo ci) {
        if (((IEntityAndData)$$3).roundabout$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung())
            ci.cancel();
    }


}
