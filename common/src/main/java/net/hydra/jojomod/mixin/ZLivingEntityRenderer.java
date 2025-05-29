package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.access.ILivingEntityRenderer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.FacelessLayer;
import net.hydra.jojomod.client.IAlphaModel;
import net.hydra.jojomod.entity.client.BigBubbleLayer;
import net.hydra.jojomod.entity.client.StoneLayer;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.corpses.FallenSpider;
import net.hydra.jojomod.entity.projectile.KnifeLayer;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.mobs.PlayerAlexNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerSteveNPC;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
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
public abstract class ZLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M>,
        ILivingEntityRenderer {


    @Shadow protected abstract boolean addLayer(RenderLayer<T, M> $$0);

    @Shadow protected M model;

    @Shadow public abstract void render(T $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5);

    protected ZLivingEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Unique
    private static int roundabout$PackRed(int $$0, int $$1) {
        return $$0 | $$1 << 16;
    }
    @Unique
    private static int roundabout$PackGreen(int $$0, int $$1) {
        return $$0 | $$1 << 8;
    }
    @Unique
    private static int roundabout$PackBlue(int $$0, int $$1) {
        return $$0 | $$1;
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$render(T entity, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, CallbackInfo ci) {
        ClientUtil.savedPose = $$3.last().pose();

        if (((StandUser)entity).roundabout$isParallelRunning())
        {
            if (entity instanceof Player player)
            {
                if (player != Minecraft.getInstance().player)
                    ci.cancel();
                else
                {
                    if (model instanceof AgeableListModel<?> ageableListModel)
                        ((IAlphaModel)ageableListModel).roundabout$setAlpha(0.5f);
                }
            }
            else
                ci.cancel();
        }
        else
        {
            if (entity instanceof Player)
                if (model instanceof AgeableListModel<?> ageableListModel)
                    ((IAlphaModel)ageableListModel).roundabout$setAlpha(1.0f);
        }
    }

    @Inject(method= "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/EntityModel;F)V", at = @At(value = "RETURN"))
    private void roundabout$init(EntityRendererProvider.Context $$0, EntityModel $$1, float $$2, CallbackInfo ci) {
        this.addLayer(new BigBubbleLayer<>($$0, ((LivingEntityRenderer)(Object)this)));
    }

    @Nullable
    public RenderType roundabout$getRenderType(LivingEntity $$0, boolean $$1, boolean $$2, boolean $$3) {
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
        if ($$0 instanceof Player) {
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
        } else if ($$0 instanceof FallenMob FM){
            /*fog corpse / corpse control*/
            int tickTock = FM.ticksThroughPhases;
            if (FM.getPhasesFull()){
                tickTock = 10;
                FM.ticksThroughPhases = 10;
            }
            float yes = Math.min(10, tickTock + $$4);
            if (FM.getActivated()) {
                yes = Math.max(0,tickTock- $$4);
            }
            float $$5 = (yes /10);
            if ($$0 instanceof FallenSpider FS){
                $$1.mulPose(Axis.XP.rotationDegrees($$5 * 180));
                $$1.translate(0,-$$5*(1*FM.getBbHeight()),0);
            } else {
                $$1.mulPose(Axis.XP.rotationDegrees($$5 * 90));
                $$1.translate(0,-$$5*(0.5*FM.getBbHeight()),-($$5*0.15));
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
    }

    @Inject(method = "getOverlayCoords", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$GetOverlayCoords(LivingEntity $$0, float $$1, CallbackInfoReturnable<Integer> ci) {
        if (((StandUser)$$0).roundabout$getStoredDamageByte() > 0) {
            ci.setReturnValue(roundabout$PackRed(
                    ((StandUser)$$0).roundabout$getStoredDamageByte(),
                    10));
        }
    }
    @Shadow
    public M getModel() {
        return null;
    }

    @Shadow
    protected float getFlipDegrees(T $$0) {
        return 90.0F;
    }

}
