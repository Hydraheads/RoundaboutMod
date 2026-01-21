package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class AchtungCapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public AchtungCapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> $$0) {
        super($$0);
    }

    /**Fade out cape*/
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At(value = "HEAD"),
            cancellable = true)
    private void roundabout$renderCape(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        if (ClientUtil.getThrowFadeToTheEther() != 1) {
            if ($$3.isCapeLoaded() && !$$3.isInvisible() && $$3.isModelPartShown(PlayerModelPart.CAPE)
                && $$3.getCloakTextureLocation() != null
            ) {
                ItemStack $$10 = $$3.getItemBySlot(EquipmentSlot.CHEST);
                if (!$$10.is(Items.ELYTRA)) {
                    $$0.pushPose();
                    $$0.translate(0.0F, 0.0F, 0.125F);
                    double $$11 = Mth.lerp((double) $$6, $$3.xCloakO, $$3.xCloak) - Mth.lerp((double) $$6, $$3.xo, $$3.getX());
                    double $$12 = Mth.lerp((double) $$6, $$3.yCloakO, $$3.yCloak) - Mth.lerp((double) $$6, $$3.yo, $$3.getY());
                    double $$13 = Mth.lerp((double) $$6, $$3.zCloakO, $$3.zCloak) - Mth.lerp((double) $$6, $$3.zo, $$3.getZ());
                    float $$14 = Mth.rotLerp($$6, $$3.yBodyRotO, $$3.yBodyRot);
                    double $$15 = (double) Mth.sin($$14 * (float) (Math.PI / 180.0));
                    double $$16 = (double) (-Mth.cos($$14 * (float) (Math.PI / 180.0)));
                    float $$17 = (float) $$12 * 10.0F;
                    $$17 = Mth.clamp($$17, -6.0F, 32.0F);
                    float $$18 = (float) ($$11 * $$15 + $$13 * $$16) * 100.0F;
                    $$18 = Mth.clamp($$18, 0.0F, 150.0F);
                    float $$19 = (float) ($$11 * $$16 - $$13 * $$15) * 100.0F;
                    $$19 = Mth.clamp($$19, -20.0F, 20.0F);
                    if ($$18 < 0.0F) {
                        $$18 = 0.0F;
                    }

                    float $$20 = Mth.lerp($$6, $$3.oBob, $$3.bob);
                    $$17 += Mth.sin(Mth.lerp($$6, $$3.walkDistO, $$3.walkDist) * 6.0F) * 32.0F * $$20;
                    if ($$3.isCrouching()) {
                        $$17 += 25.0F;
                    }

                    $$0.mulPose(Axis.XP.rotationDegrees(6.0F + $$18 / 2.0F + $$17));
                    $$0.mulPose(Axis.ZP.rotationDegrees($$19 / 2.0F));
                    $$0.mulPose(Axis.YP.rotationDegrees(180.0F - $$19 / 2.0F));
                    VertexConsumer $$21 = $$1.getBuffer(RenderType.entityTranslucent(
                            $$3.getCloakTextureLocation()
                    ));
                    this.getParentModel().renderCloak($$0, $$21, $$2, OverlayTexture.NO_OVERLAY);
                    $$0.popPose();
                }
                ci.cancel();
            }
        }
    }

}
