package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.ILivingEntityRenderer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.BigBubbleLayer;
import net.hydra.jojomod.client.models.stand.renderers.*;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.mobs.JosukePartEightNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerAlexNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerSteveNPC;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntityRenderer.class)
public abstract class ZLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M>, ILivingEntityRenderer {

    @Shadow protected abstract boolean addLayer(RenderLayer<T, M> $$0);
    @Shadow protected M model;
    @Shadow public abstract void render(T $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5);
    @Shadow public M getModel() { return null; }
    @Shadow @Nullable protected abstract RenderType getRenderType(T $$0, boolean $$1, boolean $$2, boolean $$3);

    protected ZLivingEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Unique
    private static int roundabout$PackRed(int $$0, int $$1) { return $$0 | $$1 << 16; }

    @Inject(method = "isBodyVisible", at = @At("HEAD"), cancellable = true)
    private void roundabout$forceBodyVisible(T entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof IEntityAndData data && data.roundabout$getTrueInvisibility() > 0) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$applyInvisibilityFade(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof IEntityAndData data && data.roundabout$getTrueInvisibility() > 0) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.cameraEntity != null) {
                double dist = entity.distanceTo(mc.cameraEntity);
                float alpha = PowersMetallica.getMetallicaInvisibilityAlpha(entity, dist);

                if (alpha <= 0.02f) {
                    ci.cancel();
                    return;
                }

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
                ClientUtil.setThrowFadeToTheEther(alpha);
            }
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void roundabout$forceTranslucent(T entity, boolean bodyVisible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
        if (entity instanceof IEntityAndData data && data.roundabout$getTrueInvisibility() > 0) {
            ResourceLocation texture = this.getTextureLocation(entity);
            cir.setReturnValue(RenderType.entityTranslucentCull(texture));
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "TAIL"))
    private void roundabout$renderTail(T entity, float $$1, float $$2, PoseStack matrixStack, MultiBufferSource buffer, int $$5, CallbackInfo ci) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtil.setThrowFadeToTheEther(1.0F);
        MetallicaClientRenderer.renderMetalMeterBar(entity, matrixStack, buffer);
    }

    @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z", at=@At("HEAD"), cancellable = true)
    private void roundabout$shouldShowName(T entity, CallbackInfoReturnable<Boolean> cir)
    {
        if (((StandUser)entity).roundabout$isParallelRunning()) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }

        if (entity instanceof IEntityAndData data && data.roundabout$getTrueInvisibility() > 0) {
            if (!ClientUtil.getInvisibilityVision()) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }


    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$renderX(T entity, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, CallbackInfo ci) {
//        if (roundabout$isRenderingYellowLines)
//            return;

        ClientUtil.savedPose = $$3.last().pose();
    }

    @Inject(method= "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/EntityModel;F)V", at = @At(value = "RETURN"))
    private void roundabout$init(EntityRendererProvider.Context $$0, EntityModel $$1, float $$2, CallbackInfo ci) {
        this.addLayer(new BigBubbleLayer<>($$0, ((LivingEntityRenderer)(Object)this)));
    }

    @Nullable
    public RenderType roundabout$getRenderType(LivingEntity $$0, boolean $$1, boolean $$2, boolean $$3) {
//        if (roundabout$isRenderingYellowLines)
//        {
//            return RenderType.translucent();
//        }

        ResourceLocation $$4 = this.getTextureLocation((T) $$0);
        if ($$2) {
            return RenderType.itemEntityTranslucentCull($$4);
        } else if ($$1) {
            return this.model.renderType($$4);
        } else {
            return $$3 ? RenderType.outline($$4) : null;
        }
    }

    @Unique

    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$rotations(T $$0, PoseStack $$1, float $$2, float $$3, float $$4, CallbackInfo ci) {
        if ($$0 instanceof Player P) {

            StandUser SU = (StandUser) P;
            if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
                int backflip = PA.getAttackTime();
                if (SU.roundabout$getStandAnimation() == PowerIndex.SNEAK_MOVEMENT) {
                    if (backflip < 16) {
                        $$1.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,360 * (backflip/15F)), 0, P.getEyeHeight()*0.6F, 0 );
                    }
                } else if (SU.roundabout$getStandAnimation() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                    if (true) {
                        float scale = Math.min((float)backflip/PowersAnubis.PogoDelay,1);
                        $$1.translate(0,0.5*scale,0.5*scale);
                        $$1.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,-100-P.getViewXRot(0F) * scale), 0, P.getEyeHeight()*0.4F, 0 );
                    }
                } /* else if (SU.roundabout$getStandAnimation() == PowerIndex.BARRAGE) {
                    int backflip = PA.getAttackTime();

                    $$1.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,360* 5 * (backflip/30F)), 0, P.getEyeHeight()*0.6F, 0 );

                } */
            }

            byte playerP = ((IPlayerEntity)$$0).roundabout$GetPos();

            /*Dodge makes you lean forward visually*/
            if (playerP == PlayerPosIndex.DODGE_FORWARD || playerP == PlayerPosIndex.DODGE_BACKWARD) {
                int dodgeTime = ((IPlayerEntity)$$0).roundabout$getDodgeTime();
                float $$5;
                if (dodgeTime > -1) {
                    if (dodgeTime > 5) {
                        $$5 = ((11 - ((float) dodgeTime + 1 + $$4 - 1.0F)) / 20.0F * 1.6F);
                    } else {
                        $$5 = ((float) dodgeTime + 1 + $$4 - 1.0F) / 20.0F * 1.6F;
                    }
                    $$5 = Mth.sqrt($$5);
                    if ($$5 > 1.0F) {
                        $$5 = 1.0F;
                    }

                    if (playerP == PlayerPosIndex.DODGE_FORWARD) {
                        $$5 *= -1;
                    }

                    $$1.mulPose(Axis.XP.rotationDegrees($$5 * 45));
                }
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER))
    private void roundabout$renderLivingEntity(T entity, float $$1, float $$2, PoseStack matrices, MultiBufferSource $$4, int $$5, CallbackInfo ci)
    {
        if (entity instanceof PlayerAlexNPC || entity instanceof PlayerSteveNPC)
        {
            Vector3f offset = ((JojoNPCPlayer) entity).getSizeOffset();
            matrices.scale(
                    1.0f+offset.x,
                    1.0f+offset.y,
                    1.0f+offset.z
            );
        }
        if (entity instanceof Player pl && pl.isSleeping()){
            ItemStack stack = ((IPlayerEntity) pl).roundabout$getMaskSlot();
            if (stack !=null && !stack.isEmpty() && stack.is(ModItems.JOSUKE_PART_EIGHT_MASK)){
                matrices.translate(0,-0.4,0);
            }
        } else if (entity instanceof JosukePartEightNPC jp && jp.isSleeping()){
            matrices.translate(0,-0.4,0);
        }
    }


    @Inject(method = "getOverlayCoords", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$GetOverlayCoords(LivingEntity $$0, float $$1, CallbackInfoReturnable<Integer> ci) {
        if (((StandUser)$$0).roundabout$getStoredDamageByte() > 0) {
            ci.setReturnValue(roundabout$PackRed(
                    ((StandUser)$$0).roundabout$getStoredDamageByte(),
                    10));
        }
    }
}