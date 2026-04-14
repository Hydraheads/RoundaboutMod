package net.hydra.jojomod.mixin.manhattan;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorLayer.class)
public class HorseArmorWindVision {

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V", at = @At(value = "HEAD"),
            cancellable = true)
    private void rdbt$renderHorseArmor(PoseStack $$0, MultiBufferSource $$1, int $$2, Horse $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci)
    {
        if ($$3 != null) {
            IEntityAndData entityAndData = ((IEntityAndData) $$3);
            if(entityAndData.roundabout$getTrueInvisibilityManhattan() < 1 && ClientUtil.checkIfClientCanSeeMobsForWindVision()) {
                ci.cancel();
            }
        }
    }
}
