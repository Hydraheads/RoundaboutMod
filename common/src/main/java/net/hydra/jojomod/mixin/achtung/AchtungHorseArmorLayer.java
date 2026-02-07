package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorLayer.class)
public class AchtungHorseArmorLayer {

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V", at = @At(value = "HEAD"),
    cancellable = true)
    private void rdbt$renderHorseArmor(PoseStack $$0, MultiBufferSource $$1, int $$2, Horse $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci)
    {
        if ($$3 != null) {
            if (MainUtil.getEntityIsTrulyInvisible($$3) && !ClientUtil.checkIfClientCanSeeInvisAchtung()){
                ci.cancel();
            }
        }
    }
}
