package net.hydra.jojomod.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HumanoidMobRenderer.class)
public abstract class ZHumanoidMobRenderer<T extends Mob, M extends HumanoidModel<T>> extends MobRenderer<T, M> {
    public ZHumanoidMobRenderer(EntityRendererProvider.Context $$0, M $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    /**
    @Inject(method= "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/HumanoidModel;FFFF)V", at = @At(value = "RETURN"))
    private void roundabout$renderLocacacaCurse(EntityRendererProvider.Context $$0, HumanoidModel $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {
        this.addLayer(new StoneLayer<>($$0, this));
    }
    */

}