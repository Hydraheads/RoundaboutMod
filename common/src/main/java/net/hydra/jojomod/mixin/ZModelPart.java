package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ModelPart.class)
public class ZModelPart {
    @ModifyVariable(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V",
            at = @At("HEAD"),
            ordinal = 3,
            argsOnly = true)
    private float roundabout$modifyAlpha(float originalAlpha) {
        if (ClientUtil.getThrowFadeToTheEther() != 1f){
            return ClientUtil.getThrowFadeToTheEther();
        }
        return originalAlpha;
    }
}
