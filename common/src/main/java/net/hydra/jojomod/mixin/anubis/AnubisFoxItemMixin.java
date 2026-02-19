package net.hydra.jojomod.mixin.anubis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxHeldItemLayer.class)
public abstract class AnubisFoxItemMixin extends RenderLayer<Fox, FoxModel<Fox>> {

    public AnubisFoxItemMixin(RenderLayerParent<Fox, FoxModel<Fox>> $$0) {
        super($$0);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Fox;FFFFFF)V",at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$anubisCancelHeldItem(PoseStack $$0, MultiBufferSource $$1, int $$2, Fox $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        StandUser SU = (StandUser) $$3;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis && PowerTypes.hasStandActive($$3) ) {
            ci.cancel();
        }
    }
}
