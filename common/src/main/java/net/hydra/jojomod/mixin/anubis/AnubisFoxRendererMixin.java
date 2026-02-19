package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.client.models.layers.anubis.AnubisMobLayer;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxRenderer.class)
public abstract class AnubisFoxRendererMixin extends MobRenderer<Fox, FoxModel<Fox>> {

    public AnubisFoxRendererMixin(EntityRendererProvider.Context $$0, FoxModel<Fox> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "<init>",at = @At(value = "RETURN"))
    private void roundabout$anubisFoxLayer(EntityRendererProvider.Context $$0, CallbackInfo ci) {
        this.addLayer(new AnubisMobLayer<>(this));
    }
}
