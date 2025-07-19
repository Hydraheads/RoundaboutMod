package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowRenderer.class)
public abstract class AchtungArrowRenderer<T extends AbstractArrow> extends EntityRenderer<T> {
    /***
     * Code for rendering see through arrows with Achtung Baby!
     * If partially invisible, replace the renderer.
     * This is a copy of the arrow renderer, but with transparency support
     * like the stand arrow renderer now has*/


    @Inject(method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$renderArrow(T $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, CallbackInfo ci) {
        float throwfade = ClientUtil.getThrowFadeToTheEther();
        if (throwfade != 1){
            $$3.pushPose();
            $$3.mulPose(Axis.YP.rotationDegrees(Mth.lerp($$2, $$0.yRotO, $$0.getYRot()) - 90.0F));
            $$3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot())));
            int $$6 = 0;
            float $$7 = 0.0F;
            float $$8 = 0.5F;
            float $$9 = 0.0F;
            float $$10 = 0.15625F;
            float $$11 = 0.0F;
            float $$12 = 0.15625F;
            float $$13 = 0.15625F;
            float $$14 = 0.3125F;
            float $$15 = 0.05625F;
            float $$16 = (float)$$0.shakeTime - $$2;
            if ($$16 > 0.0F) {
                float $$17 = -Mth.sin($$16 * 3.0F) * $$16;
                $$3.mulPose(Axis.ZP.rotationDegrees($$17));
            }

            $$3.mulPose(Axis.XP.rotationDegrees(45.0F));
            $$3.scale(0.05625F, 0.05625F, 0.05625F);
            $$3.translate(-4.0F, 0.0F, 0.0F);
            VertexConsumer $$18 = $$4.getBuffer(RenderType.entityTranslucentCull(this.getTextureLocation($$0)));
            PoseStack.Pose $$19 = $$3.last();
            Matrix4f $$20 = $$19.pose();
            Matrix3f $$21 = $$19.normal();
            this.roundabout$vertex($$20, $$21, $$18, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, $$5);
            this.roundabout$vertex($$20, $$21, $$18, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, $$5);
            this.roundabout$vertex($$20, $$21, $$18, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, $$5);
            this.roundabout$vertex($$20, $$21, $$18, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, $$5);
            this.roundabout$vertex($$20, $$21, $$18, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, $$5);
            this.roundabout$vertex($$20, $$21, $$18, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, $$5);
            this.roundabout$vertex($$20, $$21, $$18, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, $$5);
            this.roundabout$vertex($$20, $$21, $$18, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, $$5);

            for (int $$22 = 0; $$22 < 4; $$22++) {
                $$3.mulPose(Axis.XP.rotationDegrees(90.0F));
                this.roundabout$vertex($$20, $$21, $$18, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, $$5);
                this.roundabout$vertex($$20, $$21, $$18, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, $$5);
                this.roundabout$vertex($$20, $$21, $$18, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, $$5);
                this.roundabout$vertex($$20, $$21, $$18, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, $$5);
            }

            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, $$5);
            ci.cancel();
        }
    }

    @Unique
    public void roundabout$vertex(Matrix4f $$0, Matrix3f $$1, VertexConsumer $$2, int $$3, int $$4, int $$5, float $$6, float $$7, int $$8, int $$9, int $$10, int $$11) {
        $$2.vertex($$0, (float)$$3, (float)$$4, (float)$$5)
                .color(255, 255, 255, ClientUtil.getThrowFadeToTheEtherInt())
                .uv($$6, $$7)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2($$11)
                .normal($$1, (float)$$8, (float)$$10, (float)$$9)
                .endVertex();
    }




    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    protected AchtungArrowRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Shadow
    public abstract void vertex(Matrix4f $$0, Matrix3f $$1, VertexConsumer $$2, int $$3, int $$4, int $$5, float $$6, float $$7, int $$8, int $$9, int $$10, int $$11);

}
