package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.*;
import net.hydra.jojomod.client.gui.PowerInventoryMenu;
import net.hydra.jojomod.client.models.layers.*;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.mobs.*;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.stand.powers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerRenderer.class)
public abstract class ZPlayerRender<T extends LivingEntity, M extends EntityModel<T>> extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> implements IPlayerRenderer {


    @Shadow protected abstract void renderHand(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart modelPart, ModelPart modelPart2);

    public ZPlayerRender(EntityRendererProvider.Context $$0, PlayerModel<AbstractClientPlayer> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    public boolean originalArms;

    @Inject(method="<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At(value = "RETURN"))
    private void roundabout$initRend(EntityRendererProvider.Context $$0, boolean $$1, CallbackInfo ci) {
        /**Access to slim and not slim models simultaneously*/
        roundabout$otherModel = new PlayerModel<>($$0.bakeLayer($$1 ? ModelLayers.PLAYER : ModelLayers.PLAYER_SLIM), !$$1);
        roundabout$mainModel = this.model;
        originalArms = $$1;
    }
    @Unique
    protected PlayerModel roundabout$otherModel;
    @Unique
    protected PlayerModel roundabout$mainModel;

    private static AbstractClientPlayer ACP;
    private static InteractionHand IH;



        /**Stone Arms with locacaca first person*/
    @Inject(method = "renderRightHand", at = @At(value = "TAIL"))
    public void roundabout$renderRightHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, CallbackInfo ci) {

        byte curse = ((StandUser) player).roundabout$getLocacacaCurse();
        if (curse == LocacacaCurseIndex.RIGHT_HAND) {
            this.model.rightSleeve.xScale += 0.04F;
            this.model.rightSleeve.zScale += 0.04F;
            this.model.rightSleeve.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(StandIcons.STONE_RIGHT_ARM)), packedLight, OverlayTexture.NO_OVERLAY);
            this.model.rightSleeve.xScale -= 0.04F;
            this.model.rightSleeve.zScale -= 0.04F;
        } else {

            boolean isHurt = player.hurtTime > 0;
            float r = isHurt ? 1.0F : 1.0F;
            float g = isHurt ? 0.6F : 1.0F;
            float b = isHurt ? 0.6F : 1.0F;
            StandUser user = ((StandUser) player);
            int muscle = user.roundabout$getZappedToID();
            //muscle = 100;
            if (muscle > -1) {
                float scale = 1.055F;
                float alpha = 0.6F;
                float delta = ClientUtil.getDelta();
                if (((TimeStop) player.level()).CanTimeStopEntity(player)) {
                    delta = 0;
                }
                float oscillation = Math.abs(((player.tickCount % 10) + (delta % 1)) - 5) * 0.04F;
                alpha += oscillation;
                if (player.getMainArm() == HumanoidArm.RIGHT) {
                    if (((IPlayerModel) this.model).roundabout$getSlim()) {
                        roundabout$renderRightArmExtraModelSlim(poseStack, bufferSource, packedLight, (T) player, scale, scale, scale, delta,
                                1, 1, 1, StandIcons.MUSCLE_SLIM, 0.01F, 0, 0, alpha);
                    } else {
                        roundabout$renderRightArmExtraModel(poseStack, bufferSource, packedLight, (T) player, scale, scale, scale, delta,
                                r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                    }
                }
            }
        }
    }


    @Inject(method = "renderLeftHand", at = @At(value = "TAIL"))
    public void roundabout$renderLeftHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, CallbackInfo ci) {
        byte curse = ((StandUser) player).roundabout$getLocacacaCurse();
        if (curse == LocacacaCurseIndex.LEFT_HAND) {
            this.model.leftSleeve.xScale += 0.04F;
            this.model.leftSleeve.zScale += 0.04F;
            this.model.leftSleeve.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(StandIcons.STONE_LEFT_ARM)), packedLight, OverlayTexture.NO_OVERLAY);
            this.model.leftSleeve.xScale -= 0.04F;
            this.model.leftSleeve.zScale -= 0.04F;
        } else {
            boolean isHurt = player.hurtTime > 0;
            float r = isHurt ? 1.0F : 1.0F;
            float g = isHurt ? 0.6F : 1.0F;
            float b = isHurt ? 0.6F : 1.0F;
            StandUser user = ((StandUser) player);
            int muscle = user.roundabout$getZappedToID();
            //muscle = 100;
            if (muscle > -1) {
                float scale = 1.055F;
                float alpha = 0.6F;
                float delta = ClientUtil.getDelta();
                if (((TimeStop) player.level()).CanTimeStopEntity(player)) {
                    delta = 0;
                }
                float oscillation = Math.abs(((player.tickCount % 10) + (delta % 1)) - 5) * 0.04F;
                alpha += oscillation;
                if (player.getMainArm() == HumanoidArm.LEFT) {
                    if (((IPlayerModel) this.model).roundabout$getSlim()) {
                        roundabout$renderLeftArmExtraModelSlim(poseStack, bufferSource, packedLight, (T) player, scale, scale, scale, delta,
                                r, g, b, StandIcons.MUSCLE_SLIM, -0.01F, 0, 0, alpha);
                    } else {
                        roundabout$renderLeftArmExtraModel(poseStack, bufferSource, packedLight, (T) player, scale, scale, scale, delta,
                                r, g, b, StandIcons.MUSCLE, -0.01F, 0, 0, alpha);
                    }
                }
            }
        }
    }

    @Unique
    public void roundabout$renderRightArmExtraModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                                                    float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.rightArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,8);
            model.rightArm.translateAndRotate(poseStack);
            ModStrayModels.RightArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,8);
        }
    }
    @Unique
    public void roundabout$renderRightLegExtraModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                                                    float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.rightLeg.visible) {
            ModStrayModels.RightLeg.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
        }
    }
    @Unique
    public void roundabout$renderLeftLegExtraModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                                                    float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.leftLeg.visible) {
            ModStrayModels.LeftLeg.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
        }
    }

    public void roundabout$renderRightArmExtraModelSlim(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                                   float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.rightArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,13);
            model.rightArm.translateAndRotate(poseStack);
            ModStrayModels.RightArmSlim.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,13);
        }
    }
    public void roundabout$renderLeftArmExtraModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                              float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.leftArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,15);
            model.leftArm.translateAndRotate(poseStack);
            ModStrayModels.LeftArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,15);
        }
    }
    public void roundabout$renderLeftArmExtraModelSlim(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                  float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.leftArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,18);
            model.leftArm.translateAndRotate(poseStack);
            ModStrayModels.LeftArmSlim.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,18);
        }
    }



    @Shadow
    private void setModelProperties(AbstractClientPlayer $$0) {
    }

    @Override
    @Unique
    public void roundabout$setModelProperties(AbstractClientPlayer $$0){
        setModelProperties($$0);
    }

    @Inject(method = "getArmPose", at = @At(value = "HEAD"),cancellable = true)
    private static void roundabout$GetArmPose(AbstractClientPlayer $$0, InteractionHand $$1, CallbackInfoReturnable<HumanoidModel.ArmPose> ci) {
        ACP = $$0;
        IH = $$1;

        // ratt scope spyglass hand position
        StandUser SU = ((StandUser) $$0);
        if ($$1.equals(InteractionHand.MAIN_HAND)) {
            if (SU.roundabout$getStandPowers() instanceof PowersRatt) {
                if (SU.roundabout$getStandPowers().getStandUserSelf().roundabout$getCombatMode()) {
                    ci.setReturnValue(HumanoidModel.ArmPose.SPYGLASS);
                    return;
                }
            }

            if (((IPlayerEntity)$$0).roundabout$GetPos2() == PlayerPosIndex.BLOOD_SUCK){
                ci.setReturnValue(HumanoidModel.ArmPose.SPYGLASS);
                return;
            }
        }

    }


    @ModifyVariable(method = "getArmPose", at = @At(value = "STORE"),ordinal = 0)
    private static ItemStack roundabout$GetArmPose2(ItemStack $$0) {
        if (IH == InteractionHand.MAIN_HAND && ((StandUserClient)ACP).roundabout$getRoundaboutRenderMainHand() != null){
            $$0 = ((StandUserClient)ACP).roundabout$getRoundaboutRenderMainHand();
        } if (IH == InteractionHand.OFF_HAND && ((StandUserClient)ACP).roundabout$getRoundaboutRenderOffHand() != null){
            $$0 = ((StandUserClient)ACP).roundabout$getRoundaboutRenderOffHand();
        }
        return $$0;
    }


    ///  hides the arms if you're holding anubis
    @Inject(method = "setModelProperties", at = @At(value = "TAIL"))
    private void roundabout$setModelProperties(AbstractClientPlayer $$0, CallbackInfo ci) {
        if (ClientUtil.checkIfIsFirstPerson($$0))  {
            if (AnubisLayer.shouldRender($$0) != null) {
                PlayerModel<AbstractClientPlayer> playerModel = (PlayerModel)this.getModel();
                if (AnubisLayer.shouldRender($$0) == HumanoidArm.RIGHT) {
                    playerModel.rightArm.visible = false;
                    playerModel.rightSleeve.visible = false;
                } else {
                    playerModel.leftArm.visible = false;
                    playerModel.leftSleeve.visible = false;
                }
            }
        }
        if ($$0 instanceof StandUser standUser) {
            if (standUser.roundabout$getStandPowers() instanceof PowersGreenDay PGD) {
                      PlayerModel<AbstractClientPlayer> playerModel = this.getModel();

                if (PGD.legGoneTicks > 0) {
                    playerModel.leftLeg.visible = false;
                    playerModel.leftPants.visible = false;
                    playerModel.rightLeg.visible = false;
                    playerModel.rightPants.visible = false;
                }

            }

        }
    }

    @Inject(method = "setModelProperties", at = @At(value = "TAIL"))
    private void roundabout$setModelPropertiesCream(AbstractClientPlayer $$0, CallbackInfo ci) {
        if ($$0 instanceof StandUser standUser) {
            if (standUser.roundabout$getStandPowers() instanceof PowersCream PC) {
                PlayerModel<AbstractClientPlayer> playerModel = this.getModel();
                int transformTimer = PC.getTransformTimer();

                if (PC.getTransformDirection() == 1) {
                    if (transformTimer == 10) {
                        playerModel.head.visible = false;
                        playerModel.hat.visible = false;
                    } else if (transformTimer == 15) {
                        playerModel.leftArm.visible = false;
                        playerModel.rightArm.visible = false;
                        playerModel.body.visible = false;
                        playerModel.leftSleeve.visible = false;
                        playerModel.rightSleeve.visible = false;
                        playerModel.jacket.visible = false;
                    } else if (transformTimer == 20) {
                        playerModel.leftLeg.visible = false;
                        playerModel.rightLeg.visible = false;
                        playerModel.leftPants.visible = false;
                        playerModel.rightPants.visible = false;
                    }
                } else if (PC.getTransformDirection() == 2) { ///  Note to Chlope here: the limbs are visible by default so I'm not sure why you'd have to force this to be visible and it may lead to mod incompa
                    if (transformTimer == 10) {
                        playerModel.leftLeg.visible = true;
                        playerModel.rightLeg.visible = true;
                        playerModel.leftPants.visible = true;
                        playerModel.rightPants.visible = true;
                    } else if (transformTimer == 15) {
                        playerModel.leftArm.visible = true;
                        playerModel.rightArm.visible = true;
                        playerModel.body.visible = true;
                        playerModel.leftSleeve.visible = true;
                        playerModel.rightSleeve.visible = true;
                        playerModel.jacket.visible = true;
                    } else if (transformTimer == 20) {
                        playerModel.head.visible = true;
                        playerModel.hat.visible = true;
                    }
                }
            }
        }
    }



    /**Render external layers like soft and wet shooting mode out of context. This particular inject is for Achtung Baby*/
    @Inject(method = "renderHand", at = @At(value = "HEAD"), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderHandHEAD(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                CallbackInfo ci) {
        if (ClientUtil.getThrowFadeToTheEther() != 1){
            ci.cancel();
            PlayerModel<AbstractClientPlayer> $$6 = this.getModel();
            this.setModelProperties(acl);
            $$6.attackTime = 0.0F;
            $$6.crouching = false;
            $$6.swimAmount = 0.0F;
            $$6.setupAnim(acl, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            PlayerModel<AbstractClientPlayer> pbm = this.getModel();
            if (((IPlayerModel)pbm).roundabout$setupFirstPersonAnimations(acl, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,$$4,$$5,
                    buffer,integer,stack)){
                roundabout$renderHandLayers2(stack,buffer,integer,acl,$$4,$$5);
                return;
            }
            $$4.xRot = 0.0F;
            $$4.render(stack, buffer.getBuffer(RenderType.entityTranslucentCull(acl.getSkinTextureLocation())), integer, OverlayTexture.NO_OVERLAY);
            $$5.xRot = 0.0F;
            $$5.render(stack, buffer.getBuffer(RenderType.entityTranslucent(acl.getSkinTextureLocation())), integer, OverlayTexture.NO_OVERLAY);
            roundabout$renderHandLayers2(stack,buffer,integer,acl,$$4,$$5);
        }
    }
    

    /**Render external layers like soft and wet shooting mode out of context*/
    @Inject(method = "renderHand", at = @At(value = "TAIL"))
    private  <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderHandLayers(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                CallbackInfo ci) {
        //PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity,
        // float var5, float var6, float var7, float partialTicks, float var9, float var10)
        roundabout$renderHandLayers2(stack,buffer,integer,acl,$$4,$$5);
    }
    @Unique
    public void rdbt$copyTo(ModelPart $$0, ModelPart $$1) {
        $$1.xScale = $$0.xScale;
        $$1.yScale = $$0.yScale;
        $$1.zScale = $$0.zScale;
        $$1.xRot = $$0.xRot;
        $$1.yRot = $$0.yRot;
        $$1.zRot = $$0.zRot;
        $$1.x = $$0.x;
        $$1.y = $$0.y;
        $$1.z = $$0.z;
    }
    /**Apply hand animations to make the hand rotate*/
    @Inject(method = "renderHand", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/model/PlayerModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",shift = At.Shift.AFTER), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderHandAnimations(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                CallbackInfo ci) {
        PlayerModel<AbstractClientPlayer> $$6 = this.getModel();
        if (((IPlayerModel)$$6).roundabout$setupFirstPersonAnimations(acl, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,$$4,$$5,
                buffer,integer,stack)){
            ci.cancel();
            roundabout$renderHandLayers2(stack,buffer,integer,acl,$$4,$$5);
            return;
        }

        if (acl != null && ((StandUser)acl).roundabout$getStandPowers() instanceof PowersWalkingHeart PW && PW.inCombatMode()
        ) {

            $$6.rightLeg.copyFrom($$6.rightArm);
            $$6.rightLeg.zRot -= 0.8F;
            $$6.rightLeg.yRot += 2F;
            $$6.rightLeg.xRot = 0;

            $$6.rightPants.copyFrom($$6.rightLeg);
            $$6.leftLeg.copyFrom($$6.leftArm);
            $$6.leftLeg.zRot += 0.8F;
            $$6.leftLeg.yRot -= 2F;
            $$6.leftLeg.xRot = 0;
            $$6.leftPants.copyFrom($$6.leftLeg);

        }
    }

    @Inject(method = "renderHand", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderHandAnimationsRender(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                    AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                    CallbackInfo ci) {
        PlayerModel<AbstractClientPlayer> $$6 = this.getModel();
        if (acl != null && ((StandUser)acl).roundabout$getStandPowers() instanceof PowersWalkingHeart PW && PW.inCombatMode()){
            $$6.rightLeg.xRot = 0.2F;
            $$6.rightPants.copyFrom($$6.rightLeg);
            $$6.leftLeg.xRot = 0.2F;
            $$6.leftPants.copyFrom($$6.leftLeg);
        }
    }
    @Inject(method = "renderHand", at = @At(value = "TAIL"), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderHandAnimationsRenderTail(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                              AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                              CallbackInfo ci) {

        PlayerModel<AbstractClientPlayer> $$6 = this.getModel();
        if (acl != null && ((StandUser)acl).roundabout$getStandPowers() instanceof PowersWalkingHeart PW && PW.inCombatMode()) {

            boolean isHurt = acl.hurtTime > 0;
            float r = isHurt ? 1.0F : 1.0F;
            float g = isHurt ? 0.6F : 1.0F;
            float b = isHurt ? 0.6F : 1.0F;
            StandUser user = ((StandUser) acl);
            int muscle = user.roundabout$getZappedToID();
            //muscle = 100;
                float scale = 1.055F;
                float alpha = 0.6F;
                float delta = ClientUtil.getDelta();
                if (((TimeStop) acl.level()).CanTimeStopEntity(acl)) {
                    delta = 0;
                }
                float oscillation = Math.abs(((acl.tickCount % 10) + (delta % 1)) - 5) * 0.04F;
                alpha += oscillation;

            if ($$4 == $$6.leftLeg) {
                stack.pushPose();
                $$6.leftLeg.translateAndRotate(stack);
                rdbt$copyTo($$6.leftLeg, ModStrayModels.LeftHeel.root());
                ModStrayModels.LeftHeel.render(acl, ClientUtil.getDelta(), stack, buffer, integer,
                        1, 1, 1, 1, acl.getSkinTextureLocation());
                if (muscle > -1) {
                        roundabout$renderRightLegExtraModel(stack, buffer, integer, acl, scale, scale, scale, delta,
                                r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                }
                stack.popPose();
            } else {
                stack.pushPose();
                $$6.rightLeg.translateAndRotate(stack);
                ModStrayModels.RightHeel.render(acl, ClientUtil.getDelta(), stack, buffer, integer,
                        1, 1, 1, 1, acl.getSkinTextureLocation());
                if (muscle > -1) {
                    roundabout$renderLeftLegExtraModel(stack, buffer, integer, acl, scale, scale, scale, delta,
                            r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                }
                stack.popPose();
            }
        }
    }

    @Unique
    private  <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderHandLayers2(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                 AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5) {
        float yes = acl.tickCount;
        if (!ClientUtil.checkIfGamePaused() && !((TimeStop)acl.level()).CanTimeStopEntity(acl)){
            yes+=ClientUtil.getFrameTime();
        }
        ShootingArmLayer.renderOutOfContext(stack,buffer,getPackedLightCoords(acl,1F),acl,1,1,1,yes,
                0,0,$$4);
        AnubisLayer.renderOutOfContext(stack,buffer,getPackedLightCoords(acl,1F),acl,yes,$$4);
        if ($$4 != null && $$4.equals(this.model.rightArm)) {
            MandomLayer.renderWatchFirstPerson(stack, buffer, getPackedLightCoords(acl, 1F), acl, 1, 1, 1, yes,
                    0, 0, $$4, ((IPlayerModel) this.model).roundabout$getSlim()
            );
        }

    }

    @Inject(method = "renderRightHand", at = @At(value = "HEAD"), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderRightHandX(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {

        if ($$3 != null && ((StandUser)$$3).roundabout$getStandPowers() instanceof PowersWalkingHeart PW && PW.inCombatMode()){
            this.renderHand($$0, $$1, $$2, $$3, this.model.rightLeg, this.model.rightLeg);
            ci.cancel();
            return;
        }

        /**Access to slim and not slim models simultaneously*/
        IPlayerEntity ipe = ((IPlayerEntity) $$3);
        ItemStack visage = ipe.roundabout$getMaskSlot();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift());
        roundabout$changeTheModel($$3,visage,shift);

        if (roundabout$renderHandX($$0,$$1,$$2,$$3,true)){
            ci.cancel();
        }

        //render here
        //
    }


    @Inject(method = "renderLeftHand", at = @At(value = "HEAD"), cancellable = true)
    private <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderLeftHandX(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {
        if ($$3 != null && ((StandUser)$$3).roundabout$getStandPowers() instanceof PowersWalkingHeart PW && PW.inCombatMode()){
            this.renderHand($$0, $$1, $$2, $$3, this.model.leftLeg, this.model.leftLeg);
            ci.cancel();
            return;
        }

        /**Access to slim and not slim models simultaneously*/
        IPlayerEntity ipe = ((IPlayerEntity) $$3);
        ItemStack visage = ipe.roundabout$getMaskSlot();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift());
        roundabout$changeTheModel($$3,visage,shift);

        if (roundabout$renderHandX($$0,$$1,$$2,$$3,false)){
            ci.cancel();
        }
    }
    protected void roundabout$renderNameTagSpecial(AbstractClientPlayer $$0, Component $$1, PoseStack $$2, MultiBufferSource $$3, int $$4) {
        double $$5 = this.entityRenderDispatcher.distanceToSqr($$0);
        ClientUtil.pushPoseAndCooperate($$2,16);
        if ($$5 < 100.0) {
            Scoreboard $$6 = $$0.getScoreboard();
            Objective $$7 = $$6.getDisplayObjective(2);
            if ($$7 != null) {
                Score $$8 = $$6.getOrCreatePlayerScore($$0.getScoreboardName(), $$7);
                super.renderNameTag(
                        $$0, Component.literal(Integer.toString($$8.getScore())).append(CommonComponents.SPACE).append($$7.getDisplayName()), $$2, $$3, $$4
                );
                $$2.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
            }
        }

        super.renderNameTag($$0, $$1, $$2, $$3, $$4);
        ClientUtil.popPoseAndCooperate($$2,16);
    }
    @Inject(method = "renderNameTag(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$renderNameTag(AbstractClientPlayer $$0, Component $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, CallbackInfo ci) {
        IPlayerEntity ple = ((IPlayerEntity) $$0);
        byte shape = ple.roundabout$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift != ShapeShifts.PLAYER) {
            if (ClientNetworking.getAppropriateConfig() != null && ClientNetworking.getAppropriateConfig().nameTagSettings != null
                    && !ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagsWhenJusticeMorphed) {
                if (!(Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative() &&
                        ClientNetworking.getAppropriateConfig().nameTagSettings.bypassAllNametagHidesInCreativeMode)) {
                    ci.cancel();
                    return;
                }
            }
        }
        if (Minecraft.getInstance().player != null && !((StandUser) Minecraft.getInstance().player).roundabout$getStandPowers().canSeeThroughFog()
                && ((IPermaCasting) $$0.level()).roundabout$inPermaCastFogRange($$0)) {
            if (ClientNetworking.getAppropriateConfig() != null && ClientNetworking.getAppropriateConfig().nameTagSettings != null
                    && !ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagsInJusticeFog) {
                if (!(Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative() &&
                        ClientNetworking.getAppropriateConfig().nameTagSettings.bypassAllNametagHidesInCreativeMode)) {
                    ci.cancel();
                    return;
                }
            }
        }


        ItemStack visage = ple.roundabout$getMaskSlot();
        boolean characterType = true;
        if (visage != null && !visage.isEmpty() && visage.getItem() instanceof MaskItem ME) {
            characterType = ME.visageData.isCharacterVisage();

            if (ClientNetworking.getAppropriateConfig() != null  && ClientNetworking.getAppropriateConfig().nameTagSettings != null) {
                if (characterType) {
                    /**Do character visages hide nametags*/
                    if (!ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagOnCharacterVisages) {
                        ci.cancel();
                        return;
                    }

                    /**Setting for visage showing character name instead*/
                    if (ClientNetworking.getAppropriateConfig().nameTagSettings.renderActualCharactersNameUsingVisages) {
                        if (this.shouldShowName($$0)) {
                            Component comp = ME.getDisplayNameTag();
                            this.roundabout$renderNameTagSpecial($$0, comp, $$2, $$3, $$4);
                            ci.cancel();
                            return;
                        }
                    }
                } else {
                    /**Does glass visage hide nametags*/
                    if (!ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagOnPlayerVisages) {
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }

    @Unique
    public void roundabout$setShapeShift(Player pe, Mob me){
        if (pe instanceof AbstractClientPlayer lpe){
            ((IPlayerEntityAbstractClient)lpe).roundabout$setShapeShiftTemp(me);
        }
    }
    @Unique
    @Override
    public Mob roundabout$getShapeShift(Player pe){
        if (pe instanceof AbstractClientPlayer lpe){
            rdbt$loadModel(ShapeShifts.getShiftFromByte(((IPlayerEntity)pe).roundabout$getShapeShift()),
                    lpe,((IPlayerEntity)pe));
            return ((IPlayerEntityAbstractClient)lpe).roundabout$getShapeShiftTemp();
        }
        return null;
    }
    @Unique
    public void roundabout$setVisageData(Player pe, VisageData me){
        if (pe instanceof AbstractClientPlayer lpe){
            ((IPlayerEntityAbstractClient)lpe).roundabout$setVisageData(me);
        }
    }
    @Unique
    public VisageData roundabout$getVisageData(Player pe){
        if (pe instanceof AbstractClientPlayer lpe){
            return ((IPlayerEntityAbstractClient)lpe).roundabout$getVisageData();
        }
        return null;
    }
    @Unique
    public void roundabout$setLastVisage(Player pe, ItemStack me){
        if (pe instanceof AbstractClientPlayer lpe){
            ((IPlayerEntityAbstractClient)lpe).roundabout$setLastVisage(me);
        }
    }
    @Unique
    public ItemStack roundabout$getLastVisage(Player pe){
        if (pe instanceof AbstractClientPlayer lpe){
            return ((IPlayerEntityAbstractClient)lpe).roundabout$getLastVisage();
        }
        return null;
    }
    @Unique
    public void roundabout$setSwappedModel(Player pe, Mob me){
        if (pe instanceof AbstractClientPlayer lpe){
            ((IPlayerEntityAbstractClient)lpe).roundabout$setSwappedModel(me);
        }
    }
    @Unique
    public Mob roundabout$getSwappedModel(Player pe){
        if (pe instanceof AbstractClientPlayer lpe){
            return ((IPlayerEntityAbstractClient)lpe).roundabout$getSwappedModel();
        }
        return null;
    }

    public void rdbt$loadModel(ShapeShifts shift, AbstractClientPlayer acl, IPlayerEntity ipe){

        Mob shapeTemp = ((IPlayerEntityAbstractClient)ipe).roundabout$getShapeShiftTemp();
        if (shift != ShapeShifts.PLAYER && shift != ShapeShifts.EERIE && shift != ShapeShifts.OVA) {
            if (shift == ShapeShifts.ZOMBIE) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof Zombie))) {
                    roundabout$setShapeShift(acl, EntityType.ZOMBIE.create(Minecraft.getInstance().level));
                }
            } else if (shift == ShapeShifts.VILLAGER) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof Villager))) {
                    roundabout$setShapeShift(acl, roundabout$getVillager(Minecraft.getInstance().level, ipe));
                }
            } else if (shift == ShapeShifts.SKELETON) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof Skeleton))) {
                    roundabout$setShapeShift(acl, roundabout$getSkeleton(Minecraft.getInstance().level, ipe));
                }
            } else if (shift == ShapeShifts.WITHER_SKELETON) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof WitherSkeleton))) {
                    roundabout$setShapeShift(acl, roundabout$getWither(Minecraft.getInstance().level, ipe));
                }
            } else if (shift == ShapeShifts.STRAY) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof Stray))) {
                    roundabout$setShapeShift(acl, roundabout$getStray(Minecraft.getInstance().level, ipe));
                }
            }
        }
    }


    @Unique
    private <T extends LivingEntity, M extends EntityModel<T>>boolean roundabout$renderHandX(PoseStack stack,
                                                                                          MultiBufferSource buffer,
                                                                                          int packedLight,
                                                                                          AbstractClientPlayer acl,
                                                                                          boolean right) {
        IPlayerEntity ipe = ((IPlayerEntity) acl);
        StandUser standUser = ((StandUser) acl);
        StandPowers sp = standUser.roundabout$getStandPowers();


        byte shape = ipe.roundabout$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);


        if (shift != ShapeShifts.PLAYER && shift != ShapeShifts.EERIE && shift != ShapeShifts.OVA) {
            if (shift == ShapeShifts.ZOMBIE) {
                if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift(acl) instanceof Zombie))) {
                    roundabout$setShapeShift(acl,EntityType.ZOMBIE.create(Minecraft.getInstance().level));
                }
            } else if (shift == ShapeShifts.VILLAGER) {
                if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift(acl) instanceof Villager))) {
                    roundabout$setShapeShift(acl,roundabout$getVillager(Minecraft.getInstance().level,ipe));
                }
            } else if (shift == ShapeShifts.SKELETON) {
                if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift(acl) instanceof Skeleton))) {
                    roundabout$setShapeShift(acl,roundabout$getSkeleton(Minecraft.getInstance().level,ipe));
                }
            } else if (shift == ShapeShifts.WITHER_SKELETON) {
                if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift(acl) instanceof WitherSkeleton))) {
                    roundabout$setShapeShift(acl,roundabout$getWither(Minecraft.getInstance().level,ipe));
                }
            } else if (shift == ShapeShifts.STRAY) {
                if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift(acl) instanceof Stray))) {
                    roundabout$setShapeShift(acl,roundabout$getStray(Minecraft.getInstance().level,ipe));
                }
            }
            if (roundabout$getShapeShift(acl) != null) {
                EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                EntityRenderer<? super T> ER = $$7.getRenderer(roundabout$getShapeShift(acl));
                if (ER instanceof LivingEntityRenderer) {
                    Model ml = ((LivingEntityRenderer<?, ?>) ER).getModel();

                    if (shift == ShapeShifts.ZOMBIE) {
                        if (ml instanceof ZombieModel<?> zm) {
                            if (ER instanceof ZombieRenderer zr && roundabout$getShapeShift(acl) instanceof Zombie zmb) {
                                this.setModelProperties(acl);
                                zm.attackTime = 0.0F;
                                zm.crouching = false;
                                zm.swimAmount = 0.0F;
                                if (right) {
                                    roundabout$renderOtherHand(stack, buffer, packedLight, acl, zm.rightArm, null, ml, zr.getTextureLocation(zmb));
                                } else {
                                    roundabout$renderOtherHand(stack, buffer, packedLight, acl, zm.leftArm, null, ml, zr.getTextureLocation(zmb));
                                }
                            }
                        }
                    } else if (shift == ShapeShifts.VILLAGER) {
                        if (ml instanceof VillagerModel<?> zm) {
                            if (ER instanceof VillagerRenderer zr && roundabout$getShapeShift(acl) instanceof Villager zmb) {
                                this.setModelProperties(acl);
                                zm.attackTime = 0.0F;
                            }
                        }
                    } else if (shift == ShapeShifts.SKELETON) {
                        if (ml instanceof SkeletonModel<?> sm) {
                            if (ER instanceof SkeletonRenderer zr && roundabout$getShapeShift(acl) instanceof Skeleton skl) {
                                this.setModelProperties(acl);
                                sm.attackTime = 0.0F;
                                sm.crouching = false;
                                sm.swimAmount = 0.0F;
                                if (right) {
                                    roundabout$renderOtherHand(stack, buffer, packedLight, acl, sm.rightArm, null, ml, zr.getTextureLocation(skl));
                                } else {
                                    roundabout$renderOtherHand(stack, buffer, packedLight, acl, sm.leftArm, null, ml, zr.getTextureLocation(skl));
                                }
                            }
                        }
                    } else if (shift == ShapeShifts.WITHER_SKELETON) {
                        if (ml instanceof SkeletonModel<?> sm) {
                            if (ER instanceof WitherSkeletonRenderer zr && roundabout$getShapeShift(acl) instanceof WitherSkeleton skl) {
                                this.setModelProperties(acl);
                                sm.attackTime = 0.0F;
                                sm.crouching = false;
                                sm.swimAmount = 0.0F;
                                if (right) {
                                    roundabout$renderOtherHand(stack, buffer, packedLight, acl, sm.rightArm, null, ml, zr.getTextureLocation(skl));
                                } else {
                                    roundabout$renderOtherHand(stack, buffer, packedLight, acl, sm.leftArm, null, ml, zr.getTextureLocation(skl));
                                }
                            }
                        }
                    } else if (shift == ShapeShifts.STRAY) {
                        if (ml instanceof SkeletonModel<?> sm) {
                            if (ER instanceof StrayRenderer zr && roundabout$getShapeShift(acl) instanceof Stray skl) {
                                this.setModelProperties(acl);
                                sm.attackTime = 0.0F;
                                sm.crouching = false;
                                sm.swimAmount = 0.0F;
                                if (right) {
                                    roundabout$renderOtherHand(stack, buffer, packedLight, acl, sm.rightArm, null, ml, zr.getTextureLocation(skl));
                                } else {
                                    roundabout$renderOtherHand(stack, buffer, packedLight, acl, sm.leftArm, null, ml, zr.getTextureLocation(skl));
                                }
                            }
                        }
                    }


                    return true;
                }
            }
        } else {
            if (roundabout$getSwappedModel(acl) != null){
                EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                EntityRenderer<? super T> ER = $$7.getRenderer(roundabout$getSwappedModel(acl));
                if (ER instanceof LivingEntityRenderer) {
                }
            }
        }
        return false;
    }

        @Unique
    private void roundabout$renderOtherHand(PoseStack stack, MultiBufferSource buffer, int $$2, AbstractClientPlayer acl,
                                            ModelPart $$4, @Nullable ModelPart $$5, Model ML, ResourceLocation texture){

        if ($$4 != null && texture != null) {
            $$4.xRot = 0.0F;
            $$4.render(stack, buffer.getBuffer(RenderType.entitySolid(texture)), $$2, OverlayTexture.NO_OVERLAY);

        }
        if ($$5 != null && texture != null) {
            $$5.xRot = 0.0F;
            $$5.render(stack, buffer.getBuffer(RenderType.entityTranslucent(texture)), $$2, OverlayTexture.NO_OVERLAY);
        }


            float yes = acl.tickCount;
            if (!ClientUtil.checkIfGamePaused() && !((TimeStop)acl.level()).CanTimeStopEntity(acl)){
                yes+=ClientUtil.getFrameTime();
            }

            ShootingArmLayer.renderOutOfContext(stack,buffer,getPackedLightCoords(acl,1F),acl,1,1,1,yes,
                    0,0,$$5);
            AnubisLayer.renderOutOfContext(stack,buffer,getPackedLightCoords(acl,1F),acl,yes,$$4);
        }

    @Unique
    public void roundabout$corpseShowName(AbstractClientPlayer $$0, PoseStack $$3, MultiBufferSource $$4, int $$5){
        if (Minecraft.getInstance().player !=null && Minecraft.getInstance().player.isCreative() &&
                ClientNetworking.getAppropriateConfig() != null && ClientNetworking.getAppropriateConfig().nameTagSettings != null &&
                ClientNetworking.getAppropriateConfig().nameTagSettings.bypassAllNametagHidesInCreativeMode) {
            if (this.shouldShowName($$0)) {
                this.renderNameTag($$0, $$0.getDisplayName(), $$3, $$4, $$5);
            }
        } else {
            if (ClientNetworking.getAppropriateConfig() != null && ClientNetworking.getAppropriateConfig().nameTagSettings != null) {
                if (ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagsWhenJusticeMorphed) {
                    if (this.shouldShowName($$0)) {
                        this.renderNameTag($$0, $$0.getDisplayName(), $$3, $$4, $$5);
                    }
                }
            }
        }
    }
    boolean roundabout$switched = false;
    public void roundabout$changeTheModel(AbstractClientPlayer player, ItemStack visage, ShapeShifts shifts){

        IPlayerEntity pl = ((IPlayerEntity) player);
        visage = pl.roundabout$getMaskSlot();
        if (shifts == ShapeShifts.OVA) {
            visage = ModItems.ENYA_OVA_MASK.getDefaultInstance();
        } else if (shifts == ShapeShifts.EERIE) {
            visage = null;
        }

        if (visage != null && !visage.isEmpty()) {
            if (visage.getItem() instanceof MaskItem MI) {
                if (MI.visageData.isCharacterVisage()) {
                    if (((IPlayerModel)this.model).roundabout$getSlim() != MI.visageData.isSlim()){
                        if (MI.visageData.isSlim() != originalArms){
                            model = roundabout$otherModel;
                        } else {
                            model = roundabout$mainModel;
                        }
                    }
                    return;
                }
            }
        }
        if (((IPlayerModel)this.model).roundabout$getSlim() != originalArms) {
            model = roundabout$mainModel;
        }
    }
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD"), cancellable = true)
    public<T extends LivingEntity, M extends EntityModel<T>> void roundabout$render(AbstractClientPlayer $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, CallbackInfo ci) {

        /**Access to slim and not slim models simultaneously*/
        IPlayerEntity ipe = ((IPlayerEntity) $$0);
        ItemStack visage = ipe.roundabout$getMaskSlot();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift());
        roundabout$changeTheModel($$0,visage,shift);

            byte playerP = ((IPlayerEntity)$$0).roundabout$GetPos();

            /*Dodge makes you lean forward visually*/
            if (playerP == PlayerPosIndex.SUNLIGHT){
                ci.cancel();
                return;
            }


        if (!ClientUtil.checkIfIsFirstPerson($$0)) {
            Poses pose = Poses.getPosFromByte(ipe.roundabout$GetPoseEmote());
            if (shift != ShapeShifts.PLAYER && shift != ShapeShifts.EERIE && shift != ShapeShifts.OVA) {

                if (shift == ShapeShifts.ZOMBIE) {
                    if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift($$0) instanceof Zombie))) {
                        roundabout$setShapeShift($$0, EntityType.ZOMBIE.create(Minecraft.getInstance().level));
                    }
                    if (roundabout$getShapeShift($$0) != null) {
                        ItemStack tem = $$0.getMainHandItem();
                        roundabout$getShapeShift($$0).setAggressive(!tem.isEmpty() && tem.getMaxDamage() > 0);
                        roundabout$renderEntityForce1($$1, $$2, $$3, $$4, roundabout$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        roundabout$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.VILLAGER) {
                    if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift($$0) instanceof Villager))) {
                        roundabout$setShapeShift($$0, roundabout$getVillager(Minecraft.getInstance().level, ipe));
                    }
                    if (roundabout$getShapeShift($$0) != null) {
                        if (roundabout$getShapeShift($$0) instanceof Villager ve) {
                            if ($$0.isSleeping() && !ve.isSleeping()) {
                                Optional<BlockPos> blk = $$0.getSleepingPos();
                                blk.ifPresent(ve::startSleeping);
                            } else {
                                if (!$$0.isSleeping()) {
                                    ve.stopSleeping();
                                }
                            }
                        }
                        roundabout$renderEntityForce1($$1, $$2, $$3, $$4, roundabout$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        roundabout$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.SKELETON) {
                    if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift($$0) instanceof Skeleton))) {
                        roundabout$setShapeShift($$0, roundabout$getSkeleton(Minecraft.getInstance().level, ipe));
                    }
                    if (roundabout$getShapeShift($$0) != null) {
                        ItemStack tem = $$0.getMainHandItem();
                        roundabout$getShapeShift($$0).setAggressive(!tem.isEmpty() && tem.getMaxDamage() > 0);
                        roundabout$renderEntityForce1($$1, $$2, $$3, $$4, roundabout$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        roundabout$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.WITHER_SKELETON) {
                    if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift($$0) instanceof WitherSkeleton))) {
                        roundabout$setShapeShift($$0, roundabout$getWither(Minecraft.getInstance().level, ipe));
                    }
                    if (roundabout$getShapeShift($$0) != null) {
                        ItemStack tem = $$0.getMainHandItem();
                        roundabout$getShapeShift($$0).setAggressive(!tem.isEmpty() && tem.getMaxDamage() > 0);
                        roundabout$renderEntityForce1($$1, $$2, $$3, $$4, roundabout$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        roundabout$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.STRAY) {
                    if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift($$0) instanceof Stray))) {
                        roundabout$setShapeShift($$0, roundabout$getStray(Minecraft.getInstance().level, ipe));
                    }
                    if (roundabout$getShapeShift($$0) != null) {
                        ItemStack tem = $$0.getMainHandItem();
                        roundabout$getShapeShift($$0).setAggressive(!tem.isEmpty() && tem.getMaxDamage() > 0);
                        roundabout$renderEntityForce1($$1, $$2, $$3, $$4, roundabout$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        roundabout$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.OVA) {
                    if (Minecraft.getInstance().level != null && (!(roundabout$getShapeShift($$0) instanceof OVAEnyaNPC))) {
                        roundabout$setShapeShift($$0, ModEntities.OVA_ENYA.create(Minecraft.getInstance().level));
                    }
                    if (roundabout$getShapeShift($$0) != null) {
                        if (roundabout$getShapeShift($$0) instanceof OVAEnyaNPC ve) {
                            ve.standPos = pose;
                            ve.setupAnimationStates();
                            ve.host = $$0;
                            assertOnPlayerLike(ve, $$0, $$1, $$2, $$3, $$4, $$5,
                                    roundabout$getShapeShift($$0));
                            ci.cancel();
                            roundabout$corpseShowName($$0, $$3, $$4, $$5);
                        }
                    }
                }
            } else {

               roundabout$setSwappedModel($$0, null);

                /*** REPAIR THIS CODE ON THE NAMETAG SECTION
                        boolean characterType = true;
                        if (visage != null && !visage.isEmpty() && visage.getItem() instanceof MaskItem ME) {
                            characterType = ME.visageData.isCharacterVisage();
                        }
                        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative() &&
                                ClientNetworking.getAppropriateConfig() != null && ClientNetworking.getAppropriateConfig().nameTagSettings != null &&
                                ClientNetworking.getAppropriateConfig().nameTagSettings.bypassAllNametagHidesInCreativeMode) {
                            if (this.shouldShowName($$0)) {
                                this.renderNameTag($$0, $$0.getDisplayName(), $$3, $$4, $$5);
                            }
                        } else {
                            if (ClientNetworking.getAppropriateConfig() != null && ClientNetworking.getAppropriateConfig().nameTagSettings != null) {
                                if ((characterType && ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagOnCharacterVisages)
                                        || (!characterType && ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagOnPlayerVisages)) {
                                    if (this.shouldShowName($$0)) {

                                        Component comp = $$0.getDisplayName();
                                        if (ClientNetworking.getAppropriateConfig().nameTagSettings.renderActualCharactersNameUsingVisages
                                        && visage != null && !visage.isEmpty()) {
                                            comp = visage.getDisplayName();
                                        }

                                        this.renderNameTag($$0, comp, $$3, $$4, $$5);
                                    }
                                }
                            }
                 ***/
            }
        }

    }

    @Inject(method = "setModelProperties",
            at = @At(value = "TAIL"), cancellable = true)
    public<T extends LivingEntity, M extends EntityModel<T>> void roundabout$setModelProps(AbstractClientPlayer $$0, CallbackInfo ci) {
        /**Set visibility of limbs*/
        /**Generally sleeves and whatnot gotta go for modded layers*/
        StandUser user = ((StandUser) $$0);
        int muscle = user.roundabout$getZappedToID();
        //muscle = 100;
        byte curse = user.roundabout$getLocacacaCurse();
        PlayerModel<AbstractClientPlayer> $$1 = this.getModel();
        if (muscle > -1){
            if ($$0.getMainArm() == HumanoidArm.RIGHT){
                if (curse != LocacacaCurseIndex.RIGHT_HAND) {
                    $$1.rightSleeve.visible = false;
                }
                if (curse != LocacacaCurseIndex.RIGHT_LEG) {
                    $$1.rightPants.visible = false;
                }
            } else {
                if (curse != LocacacaCurseIndex.LEFT_HAND) {
                    $$1.leftSleeve.visible = false;
                }
                if (curse != LocacacaCurseIndex.LEFT_LEG) {
                    $$1.leftPants.visible = false;
                }
            }
        }

    }
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "TAIL"), cancellable = true)
    public<T extends LivingEntity, M extends EntityModel<T>> void roundabout$renderTail(AbstractClientPlayer entity, float $$1, float $$2, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {

    }

        @Unique
    public void roundabout$doJojoAnims(JojoNPC jj){
        jj.WRYYY.stop();
        jj.JOTARO.stop();
        jj.KOICHI.stop();
        jj.GIORNO.stop();
        jj.JONATHAN.stop();
        jj.JOSEPH.stop();
        jj.TORTURE_DANCE.stop();
        jj.OH_NO.stop();
        jj.WAMUU.stop();
            jj.VAMPIRE.stop();
    }

    @Unique
    boolean roundabout$wasJustCreated;

    public void assertOnPlayerLike(JojoNPC ve, Player $$0,float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4,
                                   int $$5, LivingEntity entityeah){
        IPlayerEntity ipe = ((IPlayerEntity) $$0);
        entityeah.setSwimming($$0.isSwimming());
        entityeah.setItemInHand(InteractionHand.MAIN_HAND,$$0.getMainHandItem());
        entityeah.setItemInHand(InteractionHand.OFF_HAND,$$0.getOffhandItem());
        entityeah.setPose($$0.getPose());
        ve.setLeftHanded($$0.getMainArm().equals(HumanoidArm.LEFT));
        ILivingEntityAccess ila = ((ILivingEntityAccess) $$0);
        ILivingEntityAccess ila2 = ((ILivingEntityAccess) entityeah);
        ila2.roundabout$setSwimAmount(ila.roundabout$getSwimAmount());
        ila2.roundabout$setSwimAmountO(ila.roundabout$getSwimAmountO());
        ila2.roundabout$setWasTouchingWater(ila.roundabout$getWasTouchingWater());
        ila2.roundabout$setFallFlyingTicks($$0.getFallFlyingTicks());
        ila2.roundabout$setSharedFlag(1, ila.roundabout$getSharedFlag(1));
        ila2.roundabout$setSharedFlag(2, ila.roundabout$getSharedFlag(2));
        ila2.roundabout$setSharedFlag(3, ila.roundabout$getSharedFlag(3));
        ila2.roundabout$setSharedFlag(4, ila.roundabout$getSharedFlag(4));
        ila2.roundabout$setSharedFlag(5, ila.roundabout$getSharedFlag(5));
        ila2.roundabout$setSharedFlag(6, ila.roundabout$getSharedFlag(6));
        entityeah.deathTime = $$0.deathTime;
        entityeah.setHealth($$0.getHealth());

        ItemStack stack = $$0.getItemBySlot(EquipmentSlot.CHEST);
        ve.setItemSlot(EquipmentSlot.CHEST, stack);
        ItemStack stackH = $$0.getItemBySlot(EquipmentSlot.HEAD);
        ve.setItemSlot(EquipmentSlot.HEAD, stackH);
        ItemStack stackL = $$0.getItemBySlot(EquipmentSlot.LEGS);
        ve.setItemSlot(EquipmentSlot.LEGS, stackL);
        ItemStack stackF = $$0.getItemBySlot(EquipmentSlot.FEET);
        ve.setItemSlot(EquipmentSlot.FEET, stackF);
        ve.roundabout$setDodgeTime(ipe.roundabout$getDodgeTime());
        ve.roundabout$setClientDodgeTime(ipe.roundabout$getClientDodgeTime());
        ve.roundabout$SetPos(ipe.roundabout$GetPos());
        ila2.roundabout$setUseItem($$0.getUseItem());
        ila2.roundabout$setUseItemTicks($$0.getUseItemRemainingTicks());
        ve.host = $$0;


        if ($$0.isFallFlying()){
            if (!ila2.roundabout$getSharedFlag(7)) {
                ila2.roundabout$setSharedFlag(7, true);
            }
        } else {
            if (ila2.roundabout$getSharedFlag(7)){
                ila2.roundabout$setSharedFlag(7,false);
            }
        }

        if ($$0.isSleeping() && !ve.isSleeping()) {
            Optional<BlockPos> blk = $$0.getSleepingPos();
            blk.ifPresent(ve::startSleeping);
        } else {
            if (!$$0.isSleeping() && ve.isSleeping()){
                ve.stopSleeping();
            }
        }
        roundabout$renderEntityForce1($$1, $$2, $$3, $$4, entityeah, $$0, $$5);
    }

    @Unique
    public Villager roundabout$getVillager(ClientLevel lev, IPlayerEntity ipe){
        Villager vil = EntityType.VILLAGER.create(lev);
        if (vil != null) {
            byte BT = ipe.roundabout$getShapeShiftExtraData();
            vil.setVillagerData(vil.getVillagerData().setType(ShapeShifts.getTypeFromByte(BT)));
            vil.setVillagerData(vil.getVillagerData().setProfession(ShapeShifts.getProfessionFromByte(BT)));
        }
        return vil;
    }
    @Unique
    public Skeleton roundabout$getSkeleton(ClientLevel lev, IPlayerEntity ipe){
        Skeleton vil = EntityType.SKELETON.create(lev);
        if (vil != null) {
            vil.setItemInHand(InteractionHand.MAIN_HAND, Items.BOW.getDefaultInstance());
        }
        return vil;
    }
    @Unique
    public Stray roundabout$getStray(ClientLevel lev, IPlayerEntity ipe){
        Stray vil = EntityType.STRAY.create(lev);
        if (vil != null) {
            vil.setItemInHand(InteractionHand.MAIN_HAND, Items.BOW.getDefaultInstance());
        }
        return vil;
    }
    @Unique
    public WitherSkeleton roundabout$getWither(ClientLevel lev, IPlayerEntity ipe){
        WitherSkeleton vil = EntityType.WITHER_SKELETON.create(lev);
        if (vil != null) {
            vil.setItemInHand(InteractionHand.MAIN_HAND, Items.STONE_SWORD.getDefaultInstance());
        }
        return vil;
    }


    @Unique
    public void roundabout$renderEntityForce1(float f1, float f2, PoseStack $$3, MultiBufferSource $$4, LivingEntity $$6, Player user, int light) {

        ClientUtil.pushPoseAndCooperate($$3,23);
        $$6.deathTime = user.deathTime;
        $$6.setHealth(user.getHealth());
        $$6.xOld = user.xOld;
        $$6.yOld = user.yOld;
        $$6.zOld = user.zOld;
        $$6.xo = user.xo;
        $$6.yo = user.yo;
        $$6.zo = user.zo;
        $$6.setPos(user.getPosition(0F));
        $$6.yBodyRotO = user.yBodyRotO;
        $$6.yHeadRotO = user.getYRot();
        $$6.yBodyRot = user.yBodyRot;
        $$6.yHeadRot = user.getYRot();
        $$6.setYRot(user.getYRot());
        $$6.setXRot(user.getXRot());
        $$6.xRotO = user.xRotO;
        $$6.yRotO = user.yRotO;
        $$6.tickCount = user.tickCount;
        $$6.attackAnim = user.attackAnim;
        $$6.oAttackAnim = user.oAttackAnim;
        $$6.hurtDuration = user.hurtDuration;
        $$6.hurtTime = user.hurtTime;
        ((StandUser) $$6).roundabout$setBubbleEncased(((StandUser)user).roundabout$getBubbleEncased());
        ((IEntityAndData)$$6).roundabout$setExclusiveLayers(((IEntityAndData)user).roundabout$getExclusiveLayers());
        ((StandUser) $$6).roundabout$setEmulator(user);
        IEntityAndData entd = ((IEntityAndData) user);
        IEntityAndData entd2 = ((IEntityAndData) $$6);
        entd2.roundabout$setVehicle(entd.roundabout$getVehicle());

        $$6.walkAnimation.setSpeed(user.walkAnimation.speed());
        IWalkAnimationState iwalk = ((IWalkAnimationState) $$6.walkAnimation);
        IWalkAnimationState uwalk = ((IWalkAnimationState) user.walkAnimation);
        iwalk.roundabout$setPosition(uwalk.roundabout$getPosition());
        iwalk.roundabout$setSpeedOld(uwalk.roundabout$getSpeedOld());

        ILivingEntityAccess ilive = ((ILivingEntityAccess)$$6);
        ILivingEntityAccess ulive = ((ILivingEntityAccess)user);
        ilive.roundabout$setAnimStep(ulive.roundabout$getAnimStep());
        ilive.roundabout$setAnimStepO(ulive.roundabout$getAnimStepO());
        $$6.setSpeed(user.getSpeed());
        ((IEntityAndData)$$6).roundabout$setNoAAB();

        roundabout$renderEntityForce2(f1,f2,$$3,$$4,$$6, light, user);
        ClientUtil.popPoseAndCooperate($$3,23);
    }

    @Unique
    public void roundabout$renderEntityForce2(float f1, float f2, PoseStack $$3, MultiBufferSource $$4,LivingEntity $$6, int light, LivingEntity user) {
        EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
        Vec3 renderoffset = $$7.getRenderer(user).getRenderOffset(user,0);

        if (!renderoffset.equals(Vec3.ZERO)){
            $$3.translate(-1*renderoffset.x,-1*renderoffset.y,-1*renderoffset.z);
        }

        if (light == 15728880) {
            ((IEntityAndData) $$6).roundabout$setShadow(false);
            ((IEntityAndData) user).roundabout$setShadow(false);
        }
        $$7.setRenderShadow(false);
        boolean hb = $$7.shouldRenderHitBoxes();
        $$7.setRenderHitBoxes(false);

        /**
        OptionInstance<GraphicsStatus> gm = Minecraft.getInstance().options.graphicsMode();
        GraphicsStatus gs = (GraphicsStatus)gm.get();
        gm.set(GraphicsStatus.FANCY);
         **/
        $$7.render($$6, 0.0, 0.0, 0.0, f1, f2, $$3,$$4, light);
        /**
        gm.set(gs);
         **/
        $$7.setRenderShadow(true);
        $$7.setRenderHitBoxes(hb);

    }
    @Inject(method = "scale(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$scale(AbstractClientPlayer $$0, PoseStack $$1, float $$2, CallbackInfo ci) {
        IPlayerEntity ple = ((IPlayerEntity) $$0);
        ItemStack visage = ple.roundabout$getMaskSlot();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(ple.roundabout$getShapeShift());
        if (shift == ShapeShifts.OVA) {
            visage = ModItems.ENYA_OVA_MASK.getDefaultInstance();
        } else if (shift == ShapeShifts.EERIE) {
            visage = null;
        }


        if (visage != null && !visage.isEmpty()) {
            if (visage.getItem() instanceof MaskItem MI) {
                if (MI instanceof ModificationMaskItem MD){
                   int height = visage.getOrCreateTagElement("modifications").getInt("height");
                    int width = visage.getOrCreateTagElement("modifications").getInt("width");
                    $$1.scale(0.798F + (((float) width)*0.001F), 0.7F+(((float) height)*0.001F), 0.798F+(((float) width)*0.001F));
                } else {
                    Vector3f scale = MI.visageData.scale();
                    $$1.scale(scale.x, scale.y, scale.z);
                }
                ci.cancel();

            }
        }
    }
    @Inject(method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getTextureLocation(AbstractClientPlayer $$0, CallbackInfoReturnable<ResourceLocation> cir) {
    }
    @Shadow
    public ResourceLocation getTextureLocation(AbstractClientPlayer var1) {
        return null;
    }
}
