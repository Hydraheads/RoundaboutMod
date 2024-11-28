package net.hydra.jojomod.mixin;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.GlaiveItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;
    @Shadow
    private float mainHandHeight;
    @Shadow
    private float oMainHandHeight;
    @Shadow
    private float offHandHeight;
    @Shadow
    private float oOffHandHeight;

    @Inject(method = "renderHandsWithItems", at = @At(value = "HEAD"), cancellable = true)
    public<T extends LivingEntity, M extends EntityModel<T>>
    void roundabout$renderHandsWithItems(float $$0, PoseStack $$1, MultiBufferSource.BufferSource $$2,
                                         LocalPlayer $$3, int $$4, CallbackInfo ci) {
        if ($$3 != null){
            boolean fp = Minecraft.getInstance().options.getCameraType().isFirstPerson();
            StandUser user = ((StandUser)$$3);
            StandPowers powers = user.roundabout$getStandPowers();
            if (user.roundabout$getStand() instanceof StarPlatinumEntity SE){
                if (fp && SE.getScoping()){
                    ci.cancel();
                    return;
                }
            }
            if (powers.isPiloting()){
                StandEntity stand = powers.getPilotingStand();
                float aan = $$3.getAttackAnim($$0);
                InteractionHand hando = MoreObjects.firstNonNull($$3.swingingArm, InteractionHand.MAIN_HAND);
                float sev = Mth.lerp($$0, $$3.xRotO, $$3.getXRot());
                float nine = Mth.lerp($$0, $$3.xBobO, $$3.xBob);
                float ten = Mth.lerp($$0, $$3.yBobO, $$3.yBob);
                $$1.mulPose(Axis.XP.rotationDegrees(($$3.getViewXRot($$0) - nine) * 0.1F));
                $$1.mulPose(Axis.YP.rotationDegrees(($$3.getViewYRot($$0) - ten) * 0.1F));
                if (stand != null) {
                    EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                    EntityRenderer<? super T> ER = $$7.getRenderer(stand);
                    if (ER instanceof StandRenderer<?>) {
                        Model ml = ((StandRenderer<?>) ER).getModel();
                        if (ml instanceof JusticeModel<?> sm) {
                            if (ER instanceof JusticeRenderer<?> zr && stand instanceof JusticeEntity skl) {


                                /** I can't for the life of me get decipher these values enough to put the hands onscreen
                                 * Wasted 6 hours, if anyone else wants to try getting justice
                                 * to render its hands in first person pilot mode be my guest
                                float $$11 = hando == InteractionHand.MAIN_HAND ? aan : 0.0F;
                                float $$12 = 1.0F - Mth.lerp($$0, oMainHandHeight, this.mainHandHeight);
                                this.roundabout$renderArmWithItem(
                                        $$3, $$0, sev, InteractionHand.MAIN_HAND, $$11, $$12, $$1, $$2, $$4,
                                        sm.rightHand, null, ml, zr.getTextureLocation(skl),
                                        1,-0.3F,0.4F,-0.4F,0.64000005F,-0.6F,-0.6F,
                                        -0.71999997F, 45F,70F,-20F, -1, 3.6F, 3.5F,
                                        120F,200F,-135F,5.6F,0,0, 1F, 1F, 1F);

                                float $$13 = hando == InteractionHand.OFF_HAND ? aan : 0.0F;
                                float $$14 = 1.0F - Mth.lerp($$0, this.oOffHandHeight, this.offHandHeight);
                                this.roundabout$renderArmWithItem(
                                        $$3, $$0, sev, InteractionHand.OFF_HAND, $$13, $$14, $$1, $$2, $$4,
                                        sm.leftHand, null, ml, zr.getTextureLocation(skl),
                                        1,-0.3F,0.4F,-0.4F,0.64000005F,-0.6F,-0.6F,
                                        -0.71999997F, 45F,70F,-20F, -1, 3.6F, -3.5F,
                                        120F,200F,-135F,5.6F,0,0, 1F, 1F, 1F);
                                 **/

                            }
                        }
                    }
                }
                ci.cancel();
            }
        }
    }
    /*
    $$3, $$0, sev, InteractionHand.OFF_HAND, $$13, $$14, $$1, $$2, $$4,
    sm.leftHand, null, ml, zr.getTextureLocation(skl),
    1,-0.3F,0.4F,-0.4F,0.64000005F,-0.6F,-0.6F,
    -0.71999997F, 45F,70F,-20F, -1, 3.6F, 3.5F,
    120F,200F,-135F,5.6F,0,0,1,1,1);
    * */

    @Unique
    private void roundabout$renderArmWithItem(
            AbstractClientPlayer $$0, float $$1, float $$2, InteractionHand $$3, float $$4, float $$6, PoseStack $$7,
            MultiBufferSource $$8, int $$9, @Nullable ModelPart mp,  @Nullable ModelPart mp2, Model ml,
            ResourceLocation texture, float d1, float d2, float d3, float d4, float d5, float d6, float d7,
            float d8, float d9, float d10, float d11, float d12, float d13, float d14,
            float d15, float d16, float d17, float d18, float d19, float d20,
            float d21, float d22, float d23
    ) {
        boolean $$10 = $$3 == InteractionHand.MAIN_HAND;
        HumanoidArm $$11 = $$10 ? $$0.getMainArm() : $$0.getMainArm().getOpposite();
        if ($$10 && !$$0.isInvisible()) {
            this.roundabout$renderPlayerArm($$7, $$8, $$9, $$6, $$4, $$11, mp, mp2, ml, texture, d1, d2, d3, d4,
                    d5,d6,d7,d8,d9,d10,d11,d12,d13,d14,d15,d16,d17,d18,d19,d20,d21,d22,d23);
        }
    }
    @Unique
    private void roundabout$renderPlayerArm(PoseStack $$0, MultiBufferSource $$1, int $$2, float $$3, float $$4, HumanoidArm $$5,
                                            @Nullable ModelPart mp, @Nullable ModelPart mp2, Model ml, ResourceLocation texture,
                                            float d1, float d2, float d3, float d4, float d5, float d6, float d7,
                                            float d8, float d9, float d10, float d11, float d12, float d13, float d14,
                                            float d15, float d16, float d17, float d18, float d19, float d20,
                                            float d21, float d22, float d23) {
        boolean $$6 = $$5 != HumanoidArm.LEFT;
        float $$7 = $$6 ? d1 : -d1;
        float $$8 = Mth.sqrt($$4);
        float $$9 = d2 * Mth.sin($$8 * (float) Math.PI);
        float $$10 = d3 * Mth.sin($$8 * (float) (Math.PI * 2));
        float $$11 = d4 * Mth.sin($$4 * (float) Math.PI);
        $$0.translate($$7 * ($$9 + d5), $$10 + d6 + $$3 * d7, $$11 + d8);
        $$0.mulPose(Axis.YP.rotationDegrees($$7 * d9));
        float $$12 = Mth.sin($$4 * $$4 * (float) Math.PI);
        float $$13 = Mth.sin($$8 * (float) Math.PI);
        $$0.mulPose(Axis.YP.rotationDegrees($$7 * $$13 * d10));
        $$0.mulPose(Axis.ZP.rotationDegrees($$7 * $$12 * d11));
        AbstractClientPlayer $$14 = this.minecraft.player;
        RenderSystem.setShaderTexture(0, $$14.getSkinTextureLocation());
        $$0.translate($$7 * d12, d13, d14);
        $$0.mulPose(Axis.ZP.rotationDegrees($$7 * d15));
        $$0.mulPose(Axis.XP.rotationDegrees(d16));
        $$0.mulPose(Axis.YP.rotationDegrees($$7 * -d17));
        $$0.translate($$7 * d18, d19, d20);
        $$0.scale(d21, d22, d23);
        PlayerRenderer $$15 = (PlayerRenderer)this.entityRenderDispatcher.<AbstractClientPlayer>getRenderer($$14);
        if ($$6) {
            roundabout$renderOtherHand($$0, $$1, $$2, $$14, mp, mp2, ml, texture, $$15);
        } else {
            roundabout$renderOtherHand($$0, $$1, $$2, $$14, mp, mp2, ml, texture, $$15);
        }
    }

    @Unique
    private void roundabout$renderOtherHand(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3,
                                            @Nullable ModelPart $$4, @Nullable ModelPart $$5, Model ML, ResourceLocation texture,
                                            PlayerRenderer OG){

        if ($$4 != null) {
            $$4.xRot = 0.0F;
            $$4.render($$0, $$1.getBuffer(RenderType.entitySolid(texture)), $$2, OverlayTexture.NO_OVERLAY);
        }
        if ($$5 != null) {
            $$5.xRot = 0.0F;
            $$5.render($$0, $$1.getBuffer(RenderType.entityTranslucent(texture)), $$2, OverlayTexture.NO_OVERLAY);
        }
    }
    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$renderArmWithItemAbstractClientPlayer(AbstractClientPlayer abstractClientPlayer, float ff, float g, InteractionHand interactionHand, float h, ItemStack itemStack, float i, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo ci) {
        if (abstractClientPlayer.isScoping()) {
            return;
        }
        if (!itemStack.isEmpty()) {
            if (abstractClientPlayer.isUsingItem() && abstractClientPlayer.getUseItemRemainingTicks() > 0 && abstractClientPlayer.getUsedItemHand() == interactionHand) {

                boolean bl = interactionHand == InteractionHand.MAIN_HAND;
                HumanoidArm humanoidArm = bl ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
                boolean bl2;
                boolean bl3 = bl2 = humanoidArm == HumanoidArm.RIGHT;
                int q = bl2 ? 1 : -1;
                if ((itemStack.getUseAnimation() == UseAnim.SPEAR &&
                        (itemStack.is(ModItems.KNIFE) || itemStack.is(ModItems.KNIFE_BUNDLE)
                                || itemStack.is(ModItems.MATCH) || itemStack.is(ModItems.MATCH_BUNDLE))) || itemStack.getItem() instanceof GlaiveItem) {

                    float knifeTime = 10f;
                    if (itemStack.is(ModItems.KNIFE) || itemStack.is(ModItems.MATCH)) {
                        knifeTime = 5F;
                    } else if (itemStack.getItem() instanceof GlaiveItem) {
                        knifeTime = 14F;
                    }
                    float kT2 = (float) (knifeTime * 0.1);
                    float kT3 = (float) (knifeTime * 0.01);

                    ci.cancel();
                    poseStack.pushPose();

                    this.applyItemArmTransform(poseStack, humanoidArm, i);
                    poseStack.translate((float) q * -0.3f, 0.25, 0.15731531f);
                    if (itemStack.is(ModItems.KNIFE) || itemStack.is(ModItems.KNIFE_BUNDLE)) {
                        poseStack.mulPose(Axis.XP.rotationDegrees(-55.0f));
                    } else if (itemStack.getItem() instanceof GlaiveItem) {
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
                    } else {
                        poseStack.mulPose(Axis.XP.rotationDegrees(-10.0f));
                    }
                    poseStack.mulPose(Axis.YP.rotationDegrees((float) q * 35.3f));
                    poseStack.mulPose(Axis.ZP.rotationDegrees((float) q * -9.785f));
                    float r = (float) itemStack.getUseDuration() - ((float) this.minecraft.player.getUseItemRemainingTicks() - ff + kT2);
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
                    if (itemStack.is(ModItems.KNIFE_BUNDLE) || itemStack.is(ModItems.MATCH_BUNDLE) || itemStack.getItem() instanceof GlaiveItem) {
                        l /= 2;
                    }
                    if (itemStack.getItem() instanceof GlaiveItem) {
                        poseStack.translate(0.0f, l * -0.4, l * 0.1f);
                    } else {
                        poseStack.translate(0.0f, 0.0f, l * 0.2f);
                    }
                    poseStack.scale(1.0f, 1.0f, 1.0f + l * 0.2f);
                    poseStack.mulPose(Axis.YN.rotationDegrees((float) q * 45.0f));
                    this.renderItem(abstractClientPlayer, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND :
                            ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !bl2, poseStack, multiBufferSource, j);
                    poseStack.popPose();
                } else if (itemStack.getUseAnimation() == UseAnim.BLOCK && itemStack.getItem() instanceof StandArrowItem) {
                    ci.cancel();
                    poseStack.pushPose();
                    this.applyItemArmTransform(poseStack, humanoidArm, i);
                    poseStack.translate((float) q * -0.3f, 0.25, 0.15731531f);
                    float knifeTime = 5f;
                    float kT2 = (float) (knifeTime * 0.1);
                    float kT3 = (float) (knifeTime * 0.01);
                    poseStack.mulPose(Axis.XP.rotationDegrees(30.0f));
                    poseStack.mulPose(Axis.YP.rotationDegrees((float) q * -35.3f));
                    poseStack.mulPose(Axis.ZP.rotationDegrees((float) q * -9.785f));
                    float r = (float) itemStack.getUseDuration() - ((float) this.minecraft.player.getUseItemRemainingTicks() - ff + kT2);
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
                    poseStack.translate(0.0f, l * -0.6, l * -0.1f);
                    poseStack.scale(1.0f, 1.0f, 1.0f);
                    poseStack.mulPose(Axis.YN.rotationDegrees((float) q * 45.0f));
                    this.renderItem(abstractClientPlayer, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND :
                            ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !bl2, poseStack, multiBufferSource, j);
                    poseStack.popPose();
                }
            } else {
                if (itemStack.getItem() instanceof StandArrowItem){
                    Mob ME =  MainUtil.homeOnWorthy(abstractClientPlayer.level(),abstractClientPlayer.position(),5);
                    if (ME != null) {
                        float homingMod = (5-Math.min(5,ME.distanceTo(abstractClientPlayer)))/5;
                        boolean bl = interactionHand == InteractionHand.MAIN_HAND;
                        HumanoidArm humanoidArm = bl ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
                        boolean bl2;
                        boolean bl3 = bl2 = humanoidArm == HumanoidArm.RIGHT;
                        int q = bl2 ? 1 : -1;
                        ci.cancel();
                        poseStack.pushPose();

                        this.applyItemArmTransform(poseStack, humanoidArm, i);
                        poseStack.translate((float) q * -0.28f, 0.15, 0.1);
                        float knifeTime = 5f;
                        float kT2 = (float) (knifeTime * 0.1);
                        float r = (-ff + kT2);
                        poseStack.scale(1.0f, 1.0f, 1.0f);
                        poseStack.mulPose(Axis.XP.rotationDegrees(-30.0f-Math.abs(20F*(r*homingMod))-(14F*homingMod)));
                        this.renderItem(abstractClientPlayer, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND :
                                ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !bl2, poseStack, multiBufferSource, j);
                        poseStack.popPose();
                    }
                }
            }
        }
    }
}