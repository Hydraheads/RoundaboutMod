package net.hydra.jojomod.mixin.gravity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class,priority = 1001)
public abstract class GravityEntityRendererMixin<T extends Entity>  {


    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;

    @Shadow public abstract Font getFont();

    @Inject(
            method = "renderNameTag",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$modifyExpressionValue_renderLabelIfPresent_getRotation_0(T $$0, Component $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        double $$5 = this.entityRenderDispatcher.distanceToSqr($$0);
        if (!($$5 > 4096.0)) {
            boolean $$6 = !$$0.isDiscrete();
            float $$7 = $$0.getNameTagOffsetY();
            int $$8 = "deadmau5".equals($$1.getString()) ? -10 : 0;
            $$2.pushPose();
            $$2.translate(0.0F, $$7, 0.0F);

            Quaternionf quaternion = new Quaternionf(RotationUtil.getCameraRotationQuaternion(gravityDirection));
            quaternion.conjugate();
            quaternion.mul(this.entityRenderDispatcher.cameraOrientation());

            $$2.mulPose(quaternion);
            $$2.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f $$9 = $$2.last().pose();
            float $$10 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int $$11 = (int)($$10 * 255.0F) << 24;
            Font $$12 = this.getFont();
            float $$13 = (float)(-$$12.width($$1) / 2);
            $$12.drawInBatch($$1, $$13, (float)$$8, 553648127, false, $$9, $$3, $$6 ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, $$11, $$4);
            if ($$6) {
                $$12.drawInBatch($$1, $$13, (float)$$8, -1, false, $$9, $$3, Font.DisplayMode.NORMAL, 0, $$4);
            }

            $$2.popPose();
        }
    }
}