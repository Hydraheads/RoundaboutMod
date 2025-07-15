package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.IAlphaModel;
import net.minecraft.client.model.AgeableListModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableListModel.class)
/* Mixin for player transparency */
public abstract class ZAgeableListModel implements IAlphaModel {
    @Shadow public abstract void renderToBuffer(PoseStack $$0, VertexConsumer $$1, int $$2, int $$3, float $$4, float $$5, float $$6, float $$7);

    @Unique private float roundabout$alpha = 1.0f;

    @Override
    public void roundabout$setAlpha(float alpha) {
        roundabout$alpha = alpha;
    }

    @Override
    public float roundabout$getAlpha() {
        return roundabout$alpha;
    }

    @ModifyVariable(
            method = "renderToBuffer",
            at = @At("HEAD"),
            ordinal = 3
    )
    private float roundabout$modifyAlpha(float originalAlpha) {
        if (roundabout$alpha != 1){
            return originalAlpha*roundabout$alpha;
        }
        return originalAlpha;
    }
}
