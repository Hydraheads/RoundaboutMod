package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = ModelPart.class, priority = 100)
public abstract class ZModelPart {
    @Shadow public boolean visible;

    @Shadow @Final private List<ModelPart.Cube> cubes;

    @Shadow @Final private Map<String, ModelPart> children;

    @Shadow public abstract void translateAndRotate(PoseStack $$0);

    @Shadow public boolean skipDraw;

    @Shadow protected abstract void compile(PoseStack.Pose $$0, VertexConsumer $$1, int $$2, int $$3, float $$4, float $$5, float $$6, float $$7);

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V",
            at = @At("HEAD"), cancellable = true)
    public void roundabout$modifyAlpha(PoseStack $$0, VertexConsumer $$1, int $$2, int $$3, float $$4, float $$5, float $$6, float originalAlpha, CallbackInfo ci) {
        float ether = ClientUtil.getThrowFadeToTheEther();
        if (ClientUtil.getThrowFadeToTheEther() != 1f){
            originalAlpha = ether;
            if (this.visible) {
                if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                    $$0.pushPose();
                    this.translateAndRotate($$0);
                    if (!this.skipDraw) {
                        this.compile($$0.last(), $$1, $$2, $$3, $$4, $$5, $$6, originalAlpha);
                    }

                    for (ModelPart $$8 : this.children.values()) {
                        $$8.render($$0, $$1, $$2, $$3, $$4, $$5, $$6, originalAlpha);
                    }

                    $$0.popPose();
                }
            }
            ci.cancel();
        }
    }
}
