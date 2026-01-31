package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.client.models.layers.anubis.AnubisHorseLayer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseRenderer.class)
public abstract class AnubisHorseRendererMixin<T extends AbstractHorse, M extends HorseModel<T>> extends MobRenderer<T, M> {

    public AnubisHorseRendererMixin(EntityRendererProvider.Context $$0, M $$1, float $$2) {super($$0, $$1, $$2);}

    @Inject(method = "<init>",at = @At(value = "RETURN"))
    private void roundabout$anubisHorseLayer(EntityRendererProvider.Context $$0, HorseModel $$1, float $$2, CallbackInfo ci) {
        this.addLayer(new AnubisHorseLayer<>(this));
    }
}
