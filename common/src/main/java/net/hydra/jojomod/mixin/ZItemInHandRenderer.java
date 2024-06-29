package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ZItemInHandRenderer {

    @Shadow
    private void applyItemArmTransform(PoseStack poseStack, HumanoidArm humanoidArm, float f) {
    }
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
    }
    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$renderArmWithItemAbstractClientPlayer(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float h, ItemStack itemStack, float i, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo ci) {
        if (abstractClientPlayer.isScoping()) {
            return;
        }
        if (!itemStack.isEmpty()) {
            if ((itemStack.is(ModItems.KNIFE) || itemStack.is(ModItems.KNIFE_BUNDLE)) && itemStack.getUseAnimation() == UseAnim.SPEAR) {
        boolean bl = interactionHand == InteractionHand.MAIN_HAND;
        HumanoidArm humanoidArm = bl ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();

                boolean bl2;
                boolean bl3 = bl2 = humanoidArm == HumanoidArm.RIGHT;
                if (abstractClientPlayer.isUsingItem() && abstractClientPlayer.getUseItemRemainingTicks() > 0 && abstractClientPlayer.getUsedItemHand() == interactionHand) {
                    int q = bl2 ? 1 : -1;
                    float knifeTime = 10f;
                    if (itemStack.is(ModItems.KNIFE)){knifeTime=5F;}
                    float kT2 = (float) (knifeTime*0.1);
                    float kT3 = (float) (knifeTime*0.01);

                    ci.cancel();
                    poseStack.pushPose();

                    this.applyItemArmTransform(poseStack, humanoidArm, i);
                    poseStack.translate((float)q * -0.3f, 0.25, 0.15731531f);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-55.0f));
                    poseStack.mulPose(Axis.YP.rotationDegrees((float)q * 35.3f));
                    poseStack.mulPose(Axis.ZP.rotationDegrees((float)q * -9.785f));
                    float r = (float)itemStack.getUseDuration() - ((float)this.minecraft.player.getUseItemRemainingTicks() - f + kT2);
                    float l = r / knifeTime;
                    if (l > kT2) {
                        l = kT2;
                    }
                    if (l > kT3) {
                        float m = Mth.sin((r - kT3) * 1.3f);
                        float n = l - kT3;
                        float o = m * n;
                        poseStack.translate(o * 0.0f, o * 0.004f, o * 0.0f);
                    }
                    poseStack.translate(0.0f, 0.0f, l * 0.2f);
                    poseStack.scale(1.0f, 1.0f, 1.0f + l * 0.2f);
                    poseStack.mulPose(Axis.YN.rotationDegrees((float)q * 45.0f));
                    this.renderItem(abstractClientPlayer, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !bl2, poseStack, multiBufferSource, j);
                    poseStack.popPose();
                }
            }
        }

    }
}