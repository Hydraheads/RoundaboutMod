package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.client.models.layers.anubis.AnubisWolfLayer;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfRenderer.class)
public abstract class AnubisWolfRendererMixin extends MobRenderer<Wolf, WolfModel<Wolf>> {

    public AnubisWolfRendererMixin(EntityRendererProvider.Context $$0, WolfModel<Wolf> $$1, float $$2) {super($$0, $$1, $$2);}

    @Inject(method = "<init>",at = @At(value = "RETURN"))
    private void roundabout$anubisWolfLayer(EntityRendererProvider.Context $$0, CallbackInfo ci) {
        this.addLayer(new AnubisWolfLayer(this));
    }
}
