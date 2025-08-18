package net.hydra.jojomod.mixin;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.JusticeModel;
import net.hydra.jojomod.client.models.stand.renderers.JusticeBaseRenderer;
import net.hydra.jojomod.client.models.stand.renderers.JusticeRenderer;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.GlaiveItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.stand.powers.PowersAchtungBaby;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ZItemInHandRenderer {

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

    @Shadow protected abstract void renderPlayerArm(PoseStack $$0, MultiBufferSource $$1, int $$2, float $$3, float $$4, HumanoidArm $$5);

    @Inject(method = "renderHandsWithItems", at = @At(value = "HEAD"), cancellable = true)
    public<T extends LivingEntity, M extends EntityModel<T>>
    void roundabout$renderHandsWithItems(float partialTick, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                         LocalPlayer localPlayer, int light, CallbackInfo ci) {
        if (localPlayer != null){
            boolean fp = Minecraft.getInstance().options.getCameraType().isFirstPerson();
            StandUser user = ((StandUser)localPlayer);
            StandPowers powers = user.roundabout$getStandPowers();
            if (user.roundabout$getStand() instanceof StarPlatinumEntity SE){
                if (fp && SE.getScoping()){
                    ci.cancel();
                    return;
                }
            }
            if (user.roundabout$getStandPowers() instanceof PowersRatt) {
                if (user.roundabout$getStandPowers().scopeLevel != 0) {
                    ci.cancel();
                    return;
                }
            }


            if (powers.isPiloting()){
                StandEntity stand = powers.getPilotingStand();
                float aan = localPlayer.getAttackAnim(partialTick);
                InteractionHand hando = MoreObjects.firstNonNull(localPlayer.swingingArm, InteractionHand.MAIN_HAND);
                float sev = Mth.lerp(partialTick, localPlayer.xRotO, localPlayer.getXRot());
                float nine = Mth.lerp(partialTick, localPlayer.xBobO, localPlayer.xBob);
                float ten = Mth.lerp(partialTick, localPlayer.yBobO, localPlayer.yBob);
                poseStack.mulPose(Axis.XP.rotationDegrees((localPlayer.getViewXRot(partialTick) - nine) * 0.1F));
                poseStack.mulPose(Axis.YP.rotationDegrees((localPlayer.getViewYRot(partialTick) - ten) * 0.1F));
                if (stand != null) {
                    EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                    EntityRenderer<? super T> ER = $$7.getRenderer(stand);
                    EntityRenderer<? super T> P = $$7.getRenderer(localPlayer);
                    if (ER instanceof StandRenderer<?>) {
                        Model ml = ((StandRenderer<?>) ER).getModel();
                        if (ml instanceof JusticeModel<?> sm) {
                            if (ER instanceof JusticeRenderer zr && stand instanceof JusticeEntity skl && P instanceof PlayerRenderer PR) {


                                /** I can't for the life of me get decipher these values enough to put the hands onscreen
                                 * Wasted 6 hours, if anyone else wants to try getting justice
                                 * to render its hands in first person pilot mode be my guest
                                 **/

                                /**
                                float $$11 = hando == InteractionHand.MAIN_HAND ? aan : 0.0F;
                                float $$12 = 1.0F - Mth.lerp($$0, oMainHandHeight, this.mainHandHeight);
                                this.roundabout$renderJusticeArmWithItem(
                                        $$3, $$0, sev, InteractionHand.MAIN_HAND, $$11, $$12, $$1, $$2, $$4,skl);

                                float $$13 = hando == InteractionHand.OFF_HAND ? aan : 0.0F;
                                float $$14 = 1.0F - Mth.lerp($$0, this.oOffHandHeight, this.offHandHeight);
                                this.roundabout$renderJusticeArmWithItem(
                                        $$3, $$0, sev, InteractionHand.OFF_HAND, $$13, $$14, $$1, $$2, $$4,skl);
                                 **/

                            }
                        }
                    }
                }
                ci.cancel();
            }
        }
    }

    @Unique
    private<T extends StandEntity>  void roundabout$renderJusticeArm(PoseStack $$0, MultiBufferSource $$1, int $$2,
                                                                     float $$3, float $$4, HumanoidArm $$5, JusticeEntity JE) {
        boolean $$6 = $$5 != HumanoidArm.LEFT;
        float $$7 = $$6 ? 1.0F : -1.0F;
        float $$8 = Mth.sqrt($$4);
        float $$9 = -0.3F * Mth.sin($$8 * (float) Math.PI);
        float $$10 = 0.4F * Mth.sin($$8 * (float) (Math.PI * 2));
        float $$11 = -0.4F * Mth.sin($$4 * (float) Math.PI);
        $$0.translate($$7 * ($$9 + 0.64000005F), $$10 + -0.6F + $$3 * -0.6F, $$11 + -0.71999997F);
        $$0.mulPose(Axis.YP.rotationDegrees($$7 * 45.0F));
        float $$12 = Mth.sin($$4 * $$4 * (float) Math.PI);
        float $$13 = Mth.sin($$8 * (float) Math.PI);
        $$0.mulPose(Axis.YP.rotationDegrees($$7 * $$13 * 70.0F));
        $$0.mulPose(Axis.ZP.rotationDegrees($$7 * $$12 * -20.0F));
        AbstractClientPlayer $$14 = this.minecraft.player;
        RenderSystem.setShaderTexture(0, $$14.getSkinTextureLocation());
        $$0.translate($$7 * -1.0F, 3.6F, 3.5F);
        $$0.mulPose(Axis.ZP.rotationDegrees($$7 * 120.0F));
        $$0.mulPose(Axis.XP.rotationDegrees(200.0F));
        $$0.mulPose(Axis.YP.rotationDegrees($$7 * -135.0F));
        $$0.translate($$7 * 5.6F, 0.0F, 0.0F);
        JusticeBaseRenderer $$15 = (JusticeBaseRenderer)this.entityRenderDispatcher.<JusticeEntity>getRenderer(JE);
        if ($$6) {
            $$15.renderRightHand($$0, $$1, $$2, JE);
        } else {
            $$15.renderLeftHand($$0, $$1, $$2, JE);
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$tick(CallbackInfo ci) {
    }
    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$renderArmWithItemAbstractClientPlayer(AbstractClientPlayer abstractClientPlayer, float ff, float g, InteractionHand interactionHand, float h, ItemStack itemStack, float i, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo ci) {
        if (abstractClientPlayer.isScoping()) {
            return;
        }

        if (abstractClientPlayer != null && ((StandUser)abstractClientPlayer).roundabout$getEffectiveCombatMode() && !abstractClientPlayer.isUsingItem()){
            ClientUtil.pushPoseAndCooperate(poseStack,4);
            boolean $$10 = interactionHand == InteractionHand.MAIN_HAND;
            HumanoidArm humarm = $$10 ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
            if ($$10 && !abstractClientPlayer.isInvisible()) {
                this.renderPlayerArm(poseStack, multiBufferSource, j, i, h, humarm);
            }
            ClientUtil.popPoseAndCooperate(poseStack,4);
            ci.cancel();
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
                    ClientUtil.pushPoseAndCooperate(poseStack,10);

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
                    ClientUtil.popPoseAndCooperate(poseStack,10);
                } else if (itemStack.getUseAnimation() == UseAnim.BLOCK && itemStack.getItem() instanceof StandArrowItem) {
                    ci.cancel();
                    ClientUtil.pushPoseAndCooperate(poseStack,11);
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
                    ClientUtil.popPoseAndCooperate(poseStack,11);
                }
            } else {
                if (itemStack.getItem() instanceof StandArrowItem SI){
                    LivingEntity ME =  MainUtil.homeOnWorthy(abstractClientPlayer.level(),abstractClientPlayer.position(),5);
                    if (ME != null && SI.isWorthinessType(itemStack,ME)) {
                        float homingMod = (5-Math.min(5,ME.distanceTo(abstractClientPlayer)))/5;
                        boolean bl = interactionHand == InteractionHand.MAIN_HAND;
                        HumanoidArm humanoidArm = bl ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
                        boolean bl2;
                        boolean bl3 = bl2 = humanoidArm == HumanoidArm.RIGHT;
                        int q = bl2 ? 1 : -1;
                        ci.cancel();
                        ClientUtil.pushPoseAndCooperate(poseStack,12);

                        this.applyItemArmTransform(poseStack, humanoidArm, i);
                        poseStack.translate((float) q * -0.28f, 0.15, 0.1);
                        float knifeTime = 5f;
                        float kT2 = (float) (knifeTime * 0.1);
                        float r = (-ff + kT2);
                        poseStack.scale(1.0f, 1.0f, 1.0f);
                        poseStack.mulPose(Axis.XP.rotationDegrees(-30.0f-Math.abs(20F*(r*homingMod))-(14F*homingMod)));
                        this.renderItem(abstractClientPlayer, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND :
                                ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !bl2, poseStack, multiBufferSource, j);
                        ClientUtil.popPoseAndCooperate(poseStack,12);
                    }
                }
            }
        }
    }
}