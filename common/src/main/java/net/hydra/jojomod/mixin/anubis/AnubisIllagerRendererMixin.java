package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.client.models.layers.anubis.AnubisIllagerLayer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.monster.AbstractIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerRenderer.class)
public abstract class AnubisIllagerRendererMixin<T extends AbstractIllager> extends MobRenderer<T, IllagerModel<T>> {

    public AnubisIllagerRendererMixin(EntityRendererProvider.Context $$0, IllagerModel<T> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method= "<init>", at = @At(value = "RETURN"))
    private void roundabout$addIllagerLayers(EntityRendererProvider.Context $$0, IllagerModel $$1, float $$2, CallbackInfo ci) {
        this.addLayer(new AnubisIllagerLayer<>($$0,((IllagerRenderer)(Object)this) ));
    }


}
