package net.hydra.jojomod.mixin.model_layers;

import net.hydra.jojomod.client.FacelessLayer;
import net.hydra.jojomod.client.models.layers.AnubisLayer;
import net.hydra.jojomod.client.models.layers.HeyYaLayer;
import net.hydra.jojomod.client.models.layers.MandomLayer;
import net.hydra.jojomod.client.models.layers.RattShoulderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public abstract class LayerHumanoidMobRendererMixin<T extends Mob, M extends HumanoidModel<T>> extends MobRenderer<T, M> {
    /**All humanoid model related layers*/
    @Inject(method= "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/HumanoidModel;FFFF)V", at = @At(value = "RETURN"))
    private void roundabout$renderHumanoidMobRenderer(EntityRendererProvider.Context $$0, HumanoidModel $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {
        this.addLayer(new FacelessLayer<>($$0, this));
        this.addLayer(new HeyYaLayer<>($$0, this));
        this.addLayer(new MandomLayer<>($$0, this));
        this.addLayer(new RattShoulderLayer<>($$0, this));
        this.addLayer(new AnubisLayer<>($$0, this));
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    public LayerHumanoidMobRendererMixin(EntityRendererProvider.Context $$0, M $$1, float $$2) {
        super($$0, $$1, $$2);
    }

}
