package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.client.models.layers.anubis.AnubisMobLayer;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CowRenderer.class)
public abstract class AnubisCowRendererMixin extends MobRenderer<Cow, CowModel<Cow>> {

    public AnubisCowRendererMixin(EntityRendererProvider.Context $$0, CowModel<Cow> $$1, float $$2) {super($$0, $$1, $$2);}

    @Inject(method = "<init>",at = @At(value = "RETURN"))
    private void roundabout$anubisCowLayer(EntityRendererProvider.Context $$0, CallbackInfo ci) {
        this.addLayer(new AnubisMobLayer<>(this));
    }
}
