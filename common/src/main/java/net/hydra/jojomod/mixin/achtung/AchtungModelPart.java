package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = ModelPart.class, priority = 100)
public abstract class AchtungModelPart {
    /***
     * Code for Achtung Baby Model Rendering!
     * The rendertype is essentially what models in the game (be it entity models or some item models) use
     * to determine their transparency. This mixin makes all of them see through, with the caveat of
     * the fabulous rendering setting doing something weird and making this impossible.
     *
     */


    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V",
            at = @At("HEAD"), cancellable = true)
    public void roundabout$modifyAlpha(PoseStack $$0, VertexConsumer $$1, int $$2, int $$3, float $$4, float $$5, float $$6, float originalAlpha, CallbackInfo ci) {
        float ether = ClientUtil.getThrowFadeToTheEther();
        if (ClientUtil.getThrowFadeToTheEther() != 1f){
            VertexConsumer newConsumer = $$1;
            ResourceLocation rl = ClientUtil.saveBufferTexture;
            if (rl != null) {
                MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                RenderType translucentType = RenderType.entityTranslucentCull(rl); // or any other transparent-compatible layer
                newConsumer = bufferSource.getBuffer(translucentType);
                ClientUtil.saveBufferTexture = null;
            }

            originalAlpha = ether;
            if (this.visible) {
                if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                    $$0.pushPose();
                    this.translateAndRotate($$0);
                    if (!this.skipDraw) {
                        this.compile($$0.last(), newConsumer, $$2, $$3, $$4, $$5, $$6, originalAlpha);
                    }

                    for (ModelPart $$8 : this.children.values()) {
                        $$8.render($$0, newConsumer, $$2, $$3, $$4, $$5, $$6, originalAlpha);
                    }

                    $$0.popPose();
                }
            }
            ci.cancel();
        }
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    public boolean visible;

    @Shadow @Final
    private List<ModelPart.Cube> cubes;

    @Shadow @Final private Map<String, ModelPart> children;

    @Shadow public abstract void translateAndRotate(PoseStack $$0);

    @Shadow public boolean skipDraw;

    @Shadow protected abstract void compile(PoseStack.Pose $$0, VertexConsumer $$1, int $$2, int $$3, float $$4, float $$5, float $$6, float $$7);
}
