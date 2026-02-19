package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.client.models.layers.anubis.AnubisMobLayer;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerRenderer.class)
public abstract class AnubisVillagerRendererMixin extends MobRenderer<Villager, VillagerModel<Villager>> {

    public AnubisVillagerRendererMixin(EntityRendererProvider.Context $$0, VillagerModel<Villager> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "<init>",at = @At(value = "RETURN"))
    private void roundabout$anubisVillagerLayer(EntityRendererProvider.Context $$0, CallbackInfo ci) {
        this.addLayer(new AnubisMobLayer<>(this));
    }
}
