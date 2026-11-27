package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.*;
import net.hydra.jojomod.client.models.layers.*;
import net.hydra.jojomod.client.models.layers.anubis.AnubisLayer;
import net.hydra.jojomod.client.models.visages.parts.WhiteAlbumColdPart;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.mobs.*;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.IronBallCrossbowItem;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.stand.powers.*;
import net.hydra.jojomod.util.SkinUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
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


    @Inject(method = "renderRightHand", at = @At("TAIL"))
    private void jjf$renderRightHand(PoseStack poseStack, MultiBufferSource bufferSource,
                                     int light, AbstractClientPlayer player, CallbackInfo ci) {
        if (net.hydra.jojomod.client.models.layers.JJFPlayerArmsOverlay.shouldRender(player)) {
            net.hydra.jojomod.client.models.layers.JJFPlayerArmsOverlay.renderArm(
                    poseStack, bufferSource, light, this.model.rightArm, true, player);
        }
    }

    @Inject(method = "renderLeftHand", at = @At("TAIL"))
    private void jjf$renderLeftHand(PoseStack poseStack, MultiBufferSource bufferSource,
                                    int light, AbstractClientPlayer player, CallbackInfo ci) {
        if (net.hydra.jojomod.client.models.layers.JJFPlayerArmsOverlay.shouldRender(player)) {
            net.hydra.jojomod.client.models.layers.JJFPlayerArmsOverlay.renderArm(
                    poseStack, bufferSource, light, this.model.leftArm, false, player);
        }
    }
    @Shadow protected abstract void renderHand(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart modelPart, ModelPart modelPart2);

    public ZPlayerRender(EntityRendererProvider.Context $$0, PlayerModel<AbstractClientPlayer> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    public boolean originalArms;

    @Inject(method="<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At(value = "RETURN"))
    private void sba$initRend(EntityRendererProvider.Context $$0, boolean $$1, CallbackInfo ci) {
        /**Access to slim and not slim models simultaneously*/
        sba$otherModel = new PlayerModel<>($$0.bakeLayer($$1 ? ModelLayers.PLAYER : ModelLayers.PLAYER_SLIM), !$$1);
        sba$mainModel = this.model;
        originalArms = $$1;
    }
    @Unique
    protected PlayerModel sba$otherModel;
    @Unique
    protected PlayerModel sba$mainModel;

    private static AbstractClientPlayer ACP;
    private static InteractionHand IH;


    @Unique
    @Override
    public void rdbt$scale(AbstractClientPlayer $$0, PoseStack $$1, float $$2) {
        scale($$0,$$1,$$2);
    }

        /**Stone Arms with locacaca first person*/
    @Inject(method = "renderRightHand", at = @At(value = "TAIL"))
    public void sba$renderRightHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, CallbackInfo ci) {

        if (AnubisLayer.shouldRender(player) != null){
            return;
        }
        if (((StandUser)player).sba$getStandPowers() instanceof Powers20thCenturyBoy CB && CB.invincibleState && PowerTypes.isUsingStand(player)) {
            return;
        }

        byte curse = ((StandUser) player).sba$getLocacacaCurse();
        float delta = ClientUtil.getDelta();
        if (((TimeStop) player.level()).CanTimeStopEntity(player)) {
            delta = 0;
        }
        float whiteAmt = PowersWhiteAlbum.getWhiteAlbumAmt(player, delta);
        float oasisAmt = PowersOasis.getOasisAmt(player, delta);
        if (whiteAmt > 0) {
            boolean isHurt = player.hurtTime > 0;
            float r = 1;
            float g = 1;
            float b = 1;
            byte skin = ((StandUser) player).sba$getStandSkin();
            String path = PowersWhiteAlbum.getSkinString(skin);
            if (!ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
                path = "ice";
            }
            if (((StandUser) player).sba$getStandPowers() instanceof PowersWhiteAlbum PW
                    && PW.cracked) {
                path = "cracked/" + path;
            }
            ClientUtil.pushPoseAndCooperate(poseStack, 8);

            model.rightArm.translateAndRotate(poseStack);
            if (((IPlayerModel) this.model).sba$getSlim()) {
                ModStrayModels.WhiteAlbumSlimRightArm.render(
                        player, delta, poseStack, bufferSource, packedLight,
                        r, g, b, whiteAmt, path);
            } else {
                ModStrayModels.WhiteAlbumRightArm.render(
                        player, delta, poseStack, bufferSource, packedLight,
                        r, g, b, whiteAmt, path);
            }
            ClientUtil.popPoseAndCooperate(poseStack, 8);

        } else if (oasisAmt > 0) {
            float r = 1;
            float g = 1;
            float b = 1;
            byte skin = ((StandUser) player).sba$getStandSkin();
            String path = PowersOasis.getSkinString(skin);

            ClientUtil.pushPoseAndCooperate(poseStack,8);

            model.rightArm.translateAndRotate(poseStack);
            if (((IPlayerModel) this.model).sba$getSlim()) {
                ModStrayModels.OasisSlimRightArm.render(
                        player, delta, poseStack, bufferSource, packedLight,
                        r, g, b, oasisAmt, path);
            } else {
                ModStrayModels.OasisRightArm.render(
                        player, delta, poseStack, bufferSource, packedLight,
                        r, g, b, oasisAmt, path);
            }
            ClientUtil.popPoseAndCooperate(poseStack,8);

        } else if (((StandUser) player).sba$hasDiverArms()) {
            // had my own diver arms thing that i made before realizing it should also appear in first person, so all the code for that goes there.
            DiverArmsLayer.renderFirstPersonArm(poseStack, bufferSource, packedLight, player, this.model.rightArm, true, 1.0F);
        } else if (((StandUser) player).sba$getStandPowers() instanceof PowersSilverChariot PSC && PowerTypes.hasStandActive(player) && PSC.hasHandsOut() && (!SilverChariotEntity.isPart5Skin(((StandUser) player).sba$getStandSkin()) && player.getMainArm() == HumanoidArm.RIGHT)) {
            SilverChariotArmsLayer.renderFirstPersonArm(poseStack, bufferSource, packedLight, player, this.model.rightArm, true, 1.0F);
        } else if (curse == LocacacaCurseIndex.RIGHT_HAND) {
            this.model.rightSleeve.xScale += 0.04F;
            this.model.rightSleeve.zScale += 0.04F;
            this.model.rightSleeve.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(StandIcons.STONE_RIGHT_ARM)), packedLight, OverlayTexture.NO_OVERLAY);
            this.model.rightSleeve.xScale -= 0.04F;
            this.model.rightSleeve.zScale -= 0.04F;
        } else {

            boolean isHurt = player.hurtTime > 0;
            float r = 1F;
            float g = 1;
            float b = 1;
            StandUser user = ((StandUser) player);
            int muscle = user.sba$getZappedToID();
            //muscle = 100;
            if (muscle > -1) {
                float scale = 1.055F;
                float alpha = 0.6F;
                if (((TimeStop) player.level()).CanTimeStopEntity(player)) {
                    delta = 0;
                }
                float oscillation = Math.abs(((player.tickCount % 10) + (delta % 1)) - 5) * 0.04F;
                alpha += oscillation;
                if (player.getMainArm() == HumanoidArm.RIGHT) {
                    if (((IPlayerModel) this.model).sba$getSlim()) {
                        sba$renderRightArmExtraModelSlim(poseStack, bufferSource, packedLight, (T) player, scale, scale, scale, delta,
                                1, 1, 1, StandIcons.MUSCLE_SLIM, 0.01F, 0, 0, alpha);
                    } else {
                        sba$renderRightArmExtraModel(poseStack, bufferSource, packedLight, (T) player, scale, scale, scale, delta,
                                r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                    }
                }
            }
        }
    }


    @Inject(method = "renderLeftHand", at = @At(value = "TAIL"))
    public void sba$renderLeftHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, CallbackInfo ci) {
        if (((StandUser)player).sba$getStandPowers() instanceof Powers20thCenturyBoy CB && CB.invincibleState && PowerTypes.isUsingStand(player)) {
            return;
        }

        byte curse = ((StandUser) player).sba$getLocacacaCurse();
        float delta = ClientUtil.getDelta();
        if (((TimeStop) player.level()).CanTimeStopEntity(player)) {
            delta = 0;
        }
        float whiteAmt = PowersWhiteAlbum.getWhiteAlbumAmt(player, delta);
        float oasisAmt = PowersOasis.getOasisAmt(player, delta);
        if (whiteAmt > 0) {
            boolean isHurt = player.hurtTime > 0;
            float r = 1;
            float g = 1;
            float b = 1;
            byte skin = ((StandUser) player).sba$getStandSkin();
            String path = PowersWhiteAlbum.getSkinString(skin);
            if (!ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
                path = "ice";
            }
            if (((StandUser)player).sba$getStandPowers() instanceof PowersWhiteAlbum PW
                    && PW.cracked){
                path = "cracked/"+path;
            }
            ClientUtil.pushPoseAndCooperate(poseStack,8);
            model.leftArm.translateAndRotate(poseStack);
            if (((IPlayerModel) this.model).sba$getSlim()) {
                ModStrayModels.WhiteAlbumSlimLeftArm.render(
                        player, delta, poseStack, bufferSource, packedLight,
                        r, g, b, whiteAmt, path);
            } else {
                ModStrayModels.WhiteAlbumLeftArm.render(
                        player, delta, poseStack, bufferSource, packedLight,
                        r, g, b, whiteAmt, path);
            }
            ClientUtil.popPoseAndCooperate(poseStack,8);

        } else if (oasisAmt > 0) {
            float r = 1;
            float g = 1;
            float b = 1;
            byte skin = ((StandUser) player).sba$getStandSkin();
            String path = PowersOasis.getSkinString(skin);

            ClientUtil.pushPoseAndCooperate(poseStack,8);
            model.leftArm.translateAndRotate(poseStack);
            if (((IPlayerModel) this.model).sba$getSlim()) {
                ModStrayModels.OasisSlimLeftArm.render(
                        player, delta, poseStack, bufferSource, packedLight,
                        r, g, b, oasisAmt, path);
            } else {
                ModStrayModels.OasisLeftArm.render(
                        player, delta, poseStack, bufferSource, packedLight,
                        r, g, b, oasisAmt, path);
            }
            ClientUtil.popPoseAndCooperate(poseStack,8);

        } else if (((StandUser) player).sba$hasDiverArms()) {
            // had my own diver arms thing that i made before realizing it should also appear in first person, so all the code for that goes there.
            DiverArmsLayer.renderFirstPersonArm(poseStack, bufferSource, packedLight, player, this.model.leftArm, false, 1.0F);
        } else if (((StandUser) player).sba$getStandPowers() instanceof PowersSilverChariot PSC && PowerTypes.hasStandActive(player) && PSC.hasHandsOut() && (SilverChariotEntity.isPart5Skin(((StandUser) player).sba$getStandSkin()) || player.getMainArm() == HumanoidArm.LEFT)) {
            SilverChariotArmsLayer.renderFirstPersonArm(poseStack, bufferSource, packedLight, player, this.model.leftArm, false, 1.0F);
        } else if (curse == LocacacaCurseIndex.LEFT_HAND) {
            this.model.leftSleeve.xScale += 0.04F;
            this.model.leftSleeve.zScale += 0.04F;
            this.model.leftSleeve.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(StandIcons.STONE_LEFT_ARM)), packedLight, OverlayTexture.NO_OVERLAY);
            this.model.leftSleeve.xScale -= 0.04F;
            this.model.leftSleeve.zScale -= 0.04F;
        } else {
            boolean isHurt = player.hurtTime > 0;
            float r = 1;
            float g = 1;
            float b = 1;
            StandUser user = ((StandUser) player);
            int muscle = user.sba$getZappedToID();
            //muscle = 100;
            if (muscle > -1) {
                float scale = 1.055F;
                float alpha = 0.6F;
                float oscillation = Math.abs(((player.tickCount % 10) + (delta % 1)) - 5) * 0.04F;
                alpha += oscillation;
                if (player.getMainArm() == HumanoidArm.LEFT) {
                    if (((IPlayerModel) this.model).sba$getSlim()) {
                        sba$renderLeftArmExtraModelSlim(poseStack, bufferSource, packedLight, (T) player, scale, scale, scale, delta,
                                r, g, b, StandIcons.MUSCLE_SLIM, -0.01F, 0, 0, alpha);
                    } else {
                        sba$renderLeftArmExtraModel(poseStack, bufferSource, packedLight, (T) player, scale, scale, scale, delta,
                                r, g, b, StandIcons.MUSCLE, -0.01F, 0, 0, alpha);
                    }
                }
            }
        }
    }

    @Unique
    public void sba$renderRightArmExtraModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
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
    public void sba$renderRightLegExtraModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                                                    float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.rightLeg.visible) {
            ModStrayModels.RightLeg.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
        }
    }
    @Unique
    public void sba$renderLeftLegExtraModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                                                    float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.leftLeg.visible) {
            ModStrayModels.LeftLeg.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
        }
    }

    public void sba$renderRightArmExtraModelSlim(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                                   float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.rightArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,13);
            model.rightArm.translateAndRotate(poseStack);
            ModStrayModels.RightArmSlim.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,13);
        }
    }
    public void sba$renderLeftArmExtraModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float xx, float yy, float zz, float partialTicks,
                              float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (model.leftArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,15);
            model.leftArm.translateAndRotate(poseStack);
            ModStrayModels.LeftArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,15);
        }
    }
    public void sba$renderLeftArmExtraModelSlim(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
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

    @Inject(method = "getArmPose", at = @At(value = "HEAD"),cancellable = true)
    private static void sba$GetArmPose(AbstractClientPlayer $$0, InteractionHand $$1, CallbackInfoReturnable<HumanoidModel.ArmPose> ci) {
        ACP = $$0;
        IH = $$1;

        // ratt scope spyglass hand position
        StandUser SU = ((StandUser) $$0);
        if ($$1.equals(InteractionHand.MAIN_HAND)) {
            if (SU.sba$getStandPowers() instanceof PowersRatt && PowerTypes.hasStandActive($$0)) {
                if (SU.sba$getCombatMode()) {
                    ci.setReturnValue(HumanoidModel.ArmPose.SPYGLASS);
                    return;
                }
            }

            if (((IPlayerEntity)$$0).sba$GetPos2() == PlayerPosIndex.BLOOD_SUCK){
                ci.setReturnValue(HumanoidModel.ArmPose.SPYGLASS);
                return;
            }
        }

        ItemStack $$2 = $$0.getItemInHand($$1);
        if (!$$2.isEmpty()) {

            if (!($$0.getUsedItemHand() == $$1 && $$0.getUseItemRemainingTicks() > 0)) {
                if (!$$0.swinging && $$2.is(ModItems.IRON_BALL_CROSSBOW) && IronBallCrossbowItem.isCharged($$2)) {
                    ci.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
                }
            }
        }

    }


    @ModifyVariable(method = "getArmPose", at = @At(value = "STORE"),ordinal = 0)
    private static ItemStack sba$GetArmPose2(ItemStack $$0) {
        if (IH == InteractionHand.MAIN_HAND && ((StandUserClient)ACP).sba$getSBARenderMainHand() != null){
            $$0 = ((StandUserClient)ACP).sba$getSBARenderMainHand();
        } if (IH == InteractionHand.OFF_HAND && ((StandUserClient)ACP).sba$getSBARenderOffHand() != null){
            $$0 = ((StandUserClient)ACP).sba$getSBARenderOffHand();
        }
        return $$0;
    }


   @Inject(method = "renderHand",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;setModelProperties(Lnet/minecraft/client/player/AbstractClientPlayer;)V",shift = At.Shift.AFTER))
    private void sba$hideAnubisArm(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, ModelPart $$4, ModelPart $$5, CallbackInfo ci) {
        HumanoidArm arm = null;
        if (AnubisLayer.shouldRender($$3) != null) {
            arm = AnubisLayer.shouldRender($$3);
        }
        StandUser SU = (StandUser) $$3;
        if (SU.sba$getStandPowers() instanceof PowersTusk && PowerTypes.isUsingStand($$3)) {
            arm = $$3.getMainArm();
        }
       if (SU.sba$getStandPowers() instanceof Powers20thCenturyBoy PCB && PCB.invincibleState) {
           arm = $$3.getMainArm();
       }

        if (arm == HumanoidArm.RIGHT) {
            this.getModel().rightArm.visible = false;
            this.getModel().rightSleeve.visible = false;
        } else if (arm == HumanoidArm.LEFT)  {
            this.getModel().leftArm.visible = false;
            this.getModel().leftSleeve.visible = false;
        }
    }

    @Inject(method = "setModelProperties", at = @At(value = "TAIL"))
    private void sba$setModelProperties(AbstractClientPlayer $$0, CallbackInfo ci) {
        if ($$0 instanceof StandUser standUser) {
            PlayerModel<AbstractClientPlayer> playerModel = this.getModel();
            if (standUser.sba$getStandPowers() instanceof PowersGreenDay PGD) {

                if (PGD.legGoneTicks > 0) {
                    playerModel.leftLeg.visible = false;
                    playerModel.leftPants.visible = false;
                    playerModel.rightLeg.visible = false;
                    playerModel.rightPants.visible = false;
                }

                if (ClientUtil.hasChangedLegs($$0)){
                    playerModel.rightArm.visible = false;
                    playerModel.rightSleeve.visible = false;
                    playerModel.leftArm.visible = false;
                    playerModel.leftSleeve.visible = false;
                }

                if(!PGD.HasMainArm && (PGD.self.getMainArm() ==HumanoidArm.RIGHT)){
                    playerModel.rightArm.visible=false;
                    playerModel.rightSleeve.visible=false;
                }
                if(!PGD.HasOffHand && (PGD.self.getMainArm() ==HumanoidArm.RIGHT)){
                    playerModel.leftArm.visible=false;
                    playerModel.leftSleeve.visible=false;
                }
                if(!PGD.HasMainArm && (PGD.self.getMainArm() ==HumanoidArm.LEFT)){
                    playerModel.leftArm.visible=false;
                    playerModel.leftSleeve.visible=false;
                }
                if(!PGD.HasOffHand && (PGD.self.getMainArm() ==HumanoidArm.LEFT)){
                    playerModel.rightArm.visible=false;
                    playerModel.rightSleeve.visible=false;
                }
            }

            if (standUser.sba$getStandPowers() instanceof PowersCatchTheRainbow PCTR){

                if(!PCTR.HasOffHand && (PCTR.self.getMainArm() ==HumanoidArm.RIGHT)){
                    playerModel.leftArm.visible=false;
                    playerModel.leftSleeve.visible=false;
                }
                if(!PCTR.HasOffHand && (PCTR.self.getMainArm() ==HumanoidArm.LEFT)){
                    playerModel.rightArm.visible=false;
                    playerModel.rightSleeve.visible=false;
                }
            }


            if (!(ClientUtil.checkIfFirstPerson() && $$0.is(ClientUtil.getPlayer()))
            || !((IEntityAndData)$$0).sba$getExclusiveLayers()){
                if (ClientUtil.hasChangedArms($$0)){
                    playerModel.rightArm.visible = false;
                    playerModel.rightSleeve.visible = false;
                    playerModel.leftArm.visible = false;
                    playerModel.leftSleeve.visible = false;
                }
                if (ClientUtil.hasChangedLegs($$0)){
                    playerModel.rightLeg.visible = false;
                    playerModel.rightPants.visible = false;
                    playerModel.leftLeg.visible = false;
                    playerModel.leftPants.visible = false;
                }
                if (ClientUtil.hasChangedBody($$0)){
                    playerModel.body.visible = false;
                    playerModel.jacket.visible = false;
                }
                if (ClientUtil.hasChangedHead($$0)){
                    playerModel.head.visible = false;
                    playerModel.hat.visible = false;
                }
                if (ClientUtil.hideCapeAndEars($$0)){
                    IPlayerModel iPlayerModel = ((IPlayerModel) playerModel);
                    iPlayerModel.sba$getEar().visible = false;
                    iPlayerModel.sba$getCloak().visible = false;
                }
            }

        }
    }




    /**Render external layers like soft and wet shooting mode out of context. This particular inject is for Achtung Baby*/
    @Inject(method = "renderHand", at = @At(value = "HEAD"), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void sba$renderHandHEAD(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                CallbackInfo ci) {
        ((IEntityAndData)acl).sba$setExclusiveLayers(true);
        boolean shouldRenderArms = true; // make this an AbilityScapeBasis thing at some point idk
        StandUser standUser = (StandUser) acl;
        if (AnubisLayer.shouldRender(acl) != null) {shouldRenderArms = false;}
        if (standUser.sba$getStandPowers() instanceof Powers20thCenturyBoy PCB && PCB.invincibleState){shouldRenderArms = false;}
        if (standUser.sba$getStandPowers() instanceof PowersTusk && PowerTypes.isUsingStand(acl)) {shouldRenderArms = false;}

        if ( (ClientUtil.getThrowFadeToTheEther() != 1 || ClientUtil.hasChangedArms(acl)) && shouldRenderArms ){
            ci.cancel();
            PlayerModel<AbstractClientPlayer> $$6 = this.getModel();
            this.setModelProperties(acl);
            $$6.attackTime = 0.0F;
            $$6.crouching = false;
            $$6.swimAmount = 0.0F;
            $$6.setupAnim(acl, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            PlayerModel<AbstractClientPlayer> pbm = this.getModel();
            if (((IPlayerModel)pbm).sba$setupFirstPersonAnimations(acl, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,$$4,$$5,
                    buffer,integer,stack)){
                sba$renderHandLayers2(stack,buffer,integer,acl,$$4,$$5);
                return;
            }
            $$4.xRot = 0.0F;
            RenderType tl = RenderType.entityTranslucentCull(acl.getSkinTextureLocation());
            if (ClientUtil.hasChangedArms(acl)){
                tl = RenderType.entityTranslucent(ClientUtil.getChangedArmTexture(acl));
            }
            $$4.render(stack, buffer.getBuffer(tl), integer, OverlayTexture.NO_OVERLAY);
            $$5.xRot = 0.0F;
            $$5.render(stack, buffer.getBuffer(tl), integer, OverlayTexture.NO_OVERLAY);
            sba$renderHandLayers2(stack,buffer,integer,acl,$$4,$$5);
            ((IEntityAndData)acl).sba$setExclusiveLayers(false);
        }
    }
    

    /**Render external layers like soft and wet shooting mode out of context*/
    @Inject(method = "renderHand", at = @At(value = "TAIL"))
    private  <T extends LivingEntity, M extends EntityModel<T>>void sba$renderHandLayers(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                CallbackInfo ci) {
        //PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity,
        // float var5, float var6, float var7, float partialTicks, float var9, float var10)
        sba$renderHandLayers2(stack,buffer,integer,acl,$$4,$$5);
        ((IEntityAndData)acl).sba$setExclusiveLayers(false);
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
    private  <T extends LivingEntity, M extends EntityModel<T>>void sba$renderHandAnimations(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                CallbackInfo ci) {

        PlayerModel<AbstractClientPlayer> $$6 = this.getModel();
        if (((IPlayerModel)$$6).sba$setupFirstPersonAnimations(acl, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,$$4,$$5,
                buffer,integer,stack)){
            ci.cancel();
            sba$renderHandLayers2(stack,buffer,integer,acl,$$4,$$5);
            return;
        }

        if (acl != null && ((StandUser)acl).sba$getStandPowers() instanceof PowersWalkingHeart PW && PW.rendersInCombatMode()
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
    private  <T extends LivingEntity, M extends EntityModel<T>>void sba$renderHandAnimationsRender(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                    AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                    CallbackInfo ci) {
        PlayerModel<AbstractClientPlayer> $$6 = this.getModel();
        if (acl != null && ((StandUser)acl).sba$getStandPowers() instanceof PowersWalkingHeart PW && PW.rendersInCombatMode()){
            $$6.rightLeg.xRot = 0.2F;
            $$6.rightPants.copyFrom($$6.rightLeg);
            $$6.leftLeg.xRot = 0.2F;
            $$6.leftPants.copyFrom($$6.leftLeg);
        }
    }
    @Inject(method = "renderHand", at = @At(value = "TAIL"), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void sba$renderHandAnimationsRenderTail(PoseStack stack, MultiBufferSource buffer, int integer,
                                                                                                              AbstractClientPlayer acl, ModelPart $$4, ModelPart $$5,
                                                                                                              CallbackInfo ci) {

        PlayerModel<AbstractClientPlayer> $$6 = this.getModel();
        if (acl != null && ((StandUser)acl).sba$getStandPowers() instanceof PowersWalkingHeart PW && PW.rendersInCombatMode()) {

            boolean isHurt = acl.hurtTime > 0;
            float r = 1;
            float g = 1;
            float b = 1;
            StandUser user = ((StandUser) acl);
            int muscle = user.sba$getZappedToID();
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
                        sba$renderRightLegExtraModel(stack, buffer, integer, acl, scale, scale, scale, delta,
                                r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                }
                stack.popPose();
            } else {
                stack.pushPose();
                $$6.rightLeg.translateAndRotate(stack);
                ModStrayModels.RightHeel.render(acl, ClientUtil.getDelta(), stack, buffer, integer,
                        1, 1, 1, 1, acl.getSkinTextureLocation());
                if (muscle > -1) {
                    sba$renderLeftLegExtraModel(stack, buffer, integer, acl, scale, scale, scale, delta,
                            r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                }
                stack.popPose();
            }
        }
    }

    @Unique
    private  <T extends LivingEntity, M extends EntityModel<T>>void sba$renderHandLayers2(PoseStack stack, MultiBufferSource buffer, int integer,
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
                    0, 0, $$4, ((IPlayerModel) this.model).sba$getSlim()
            );

            if (((StandUser)acl).sba$getStandPowers() instanceof PowersWhiteAlbum pwa &&
                    ((IPlayerEntity)acl).sba$GetPos2() == PlayerPosIndex.CHARGE_SHOT){

                ClientUtil.pushPoseAndCooperate(stack,33);
                $$4.translateAndRotate(stack);
                stack.translate(0.11F,0.07F,0.1F);
                ModStrayModels.WhiteAlbumCold.render(acl, acl.tickCount+ ClientUtil.getFrameTime() %1, stack, buffer, getPackedLightCoords(acl,1F),
                        1,1,1, 1, "test");
                ClientUtil.popPoseAndCooperate(stack,33);
            }
        }

    }

    @Inject(method = "renderRightHand", at = @At(value = "HEAD"), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void sba$renderRightHandX(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {

        if ($$3 != null && ((StandUser)$$3).sba$getStandPowers() instanceof PowersWalkingHeart PW && PW.rendersInCombatMode()){
            this.renderHand($$0, $$1, $$2, $$3, this.model.rightLeg, this.model.rightLeg);
            ci.cancel();
            return;
        }

        /**Access to slim and not slim models simultaneously*/
        IPlayerEntity ipe = ((IPlayerEntity) $$3);
        ItemStack visage = ipe.sba$getMaskSlot();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.sba$getShapeShift());
        sba$changeTheModel($$3,visage,shift);

        if (sba$renderHandX($$0,$$1,$$2,$$3,true)){
            ci.cancel();
        }

        //render here
        //
    }


    @Inject(method = "renderLeftHand", at = @At(value = "HEAD"), cancellable = true)
    private <T extends LivingEntity, M extends EntityModel<T>>void sba$renderLeftHandX(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {
        if ($$3 != null && ((StandUser)$$3).sba$getStandPowers() instanceof PowersWalkingHeart PW && PW.rendersInCombatMode()){
            this.renderHand($$0, $$1, $$2, $$3, this.model.leftLeg, this.model.leftLeg);
            ci.cancel();
            return;
        }

        /**Access to slim and not slim models simultaneously*/
        IPlayerEntity ipe = ((IPlayerEntity) $$3);
        ItemStack visage = ipe.sba$getMaskSlot();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.sba$getShapeShift());
        sba$changeTheModel($$3,visage,shift);

        if (sba$renderHandX($$0,$$1,$$2,$$3,false)){
            ci.cancel();
        }
    }
    protected void sba$renderNameTagSpecial(AbstractClientPlayer $$0, Component $$1, PoseStack $$2, MultiBufferSource $$3, int $$4) {
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
    private void sba$renderNameTag(AbstractClientPlayer $$0, Component $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, CallbackInfo ci) {
        if ($$0 instanceof StandUser su && su.sba$isDisguised()) {
            GameProfile profile = su.sba$getDisguiseProfile();
            if (profile != null && profile.getName() != null && !profile.getName().isEmpty()) {
                if ($$0 != Minecraft.getInstance().player && !$$0.isInvisible() && !Minecraft.getInstance().options.hideGui) {
                    this.sba$renderNameTagSpecial($$0, Component.literal(profile.getName()), $$2, $$3, $$4);
                }
                ci.cancel();
                return;
            }
        }
        //deletes original nametag
        if ($$0 instanceof StandUser su && su.sba$isDisguised()) {
            com.mojang.authlib.GameProfile profile = su.sba$getDisguiseProfile();
            if (profile != null && profile.getName() != null) {
                if (!$$1.getString().equals(profile.getName())) {
                    ci.cancel();
                    return;
                }
            }
        }
        IPlayerEntity ple = ((IPlayerEntity) $$0);
        byte shape = ple.sba$getShapeShift();
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
        if (Minecraft.getInstance().player != null && !((StandUser) Minecraft.getInstance().player).sba$getStandPowers().canSeeThroughFog()
                && ((IPermaCasting) $$0.level()).sba$inPermaCastFogRange($$0)) {
            if (ClientNetworking.getAppropriateConfig() != null && ClientNetworking.getAppropriateConfig().nameTagSettings != null
                    && !ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagsInJusticeFog) {
                if (!(Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative() &&
                        ClientNetworking.getAppropriateConfig().nameTagSettings.bypassAllNametagHidesInCreativeMode)) {
                    ci.cancel();
                    return;
                }
            }
        }


        ItemStack visage = ple.sba$getMaskSlot();
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
                            this.sba$renderNameTagSpecial($$0, comp, $$2, $$3, $$4);
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
    public void sba$setShapeShift(Player pe, Mob me){
        if (pe instanceof AbstractClientPlayer lpe){
            ((IPlayerEntityAbstractClient)lpe).sba$setShapeShiftTemp(me);
        }
    }
    @Unique
    @Override
    public Mob sba$getShapeShift(Player pe){
        if (pe instanceof AbstractClientPlayer lpe){
            rdbt$loadModel(ShapeShifts.getShiftFromByte(((IPlayerEntity)pe).sba$getShapeShift()),
                    lpe,((IPlayerEntity)pe));
            return ((IPlayerEntityAbstractClient)lpe).sba$getShapeShiftTemp();
        }
        return null;
    }
    @Unique
    public void sba$setVisageData(Player pe, VisageData me){
        if (pe instanceof AbstractClientPlayer lpe){
            ((IPlayerEntityAbstractClient)lpe).sba$setVisageData(me);
        }
    }
    @Unique
    public VisageData sba$getVisageData(Player pe){
        if (pe instanceof AbstractClientPlayer lpe){
            return ((IPlayerEntityAbstractClient)lpe).sba$getVisageData();
        }
        return null;
    }
    @Unique
    public void sba$setLastVisage(Player pe, ItemStack me){
        if (pe instanceof AbstractClientPlayer lpe){
            ((IPlayerEntityAbstractClient)lpe).sba$setLastVisage(me);
        }
    }
    @Unique
    public ItemStack sba$getLastVisage(Player pe){
        if (pe instanceof AbstractClientPlayer lpe){
            return ((IPlayerEntityAbstractClient)lpe).sba$getLastVisage();
        }
        return null;
    }
    @Unique
    public void sba$setSwappedModel(Player pe, Mob me){
        if (pe instanceof AbstractClientPlayer lpe){
            ((IPlayerEntityAbstractClient)lpe).sba$setSwappedModel(me);
        }
    }
    @Unique
    public Mob sba$getSwappedModel(Player pe){
        if (pe instanceof AbstractClientPlayer lpe){
            return ((IPlayerEntityAbstractClient)lpe).sba$getSwappedModel();
        }
        return null;
    }

    public void rdbt$loadModel(ShapeShifts shift, AbstractClientPlayer acl, IPlayerEntity ipe){

        Mob shapeTemp = ((IPlayerEntityAbstractClient)ipe).sba$getShapeShiftTemp();
        if (shift != ShapeShifts.PLAYER && shift != ShapeShifts.EERIE && shift != ShapeShifts.OVA) {
            if (shift == ShapeShifts.ZOMBIE) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof Zombie))) {
                    sba$setShapeShift(acl, EntityType.ZOMBIE.create(Minecraft.getInstance().level));
                }
            } else if (shift == ShapeShifts.VILLAGER) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof Villager))) {
                    sba$setShapeShift(acl, sba$getVillager(Minecraft.getInstance().level, ipe));
                }
            } else if (shift == ShapeShifts.SKELETON) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof Skeleton))) {
                    sba$setShapeShift(acl, sba$getSkeleton(Minecraft.getInstance().level, ipe));
                }
            } else if (shift == ShapeShifts.WITHER_SKELETON) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof WitherSkeleton))) {
                    sba$setShapeShift(acl, sba$getWither(Minecraft.getInstance().level, ipe));
                }
            } else if (shift == ShapeShifts.STRAY) {
                if (Minecraft.getInstance().level != null && (!(shapeTemp instanceof Stray))) {
                    sba$setShapeShift(acl, sba$getStray(Minecraft.getInstance().level, ipe));
                }
            }
        }
    }


    @Unique
    private <T extends LivingEntity, M extends EntityModel<T>>boolean sba$renderHandX(PoseStack stack,
                                                                                          MultiBufferSource buffer,
                                                                                          int packedLight,
                                                                                          AbstractClientPlayer acl,
                                                                                          boolean right) {
        IPlayerEntity ipe = ((IPlayerEntity) acl);
        StandUser standUser = ((StandUser) acl);
        StandPowers sp = standUser.sba$getStandPowers();


        byte shape = ipe.sba$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);


        if (shift != ShapeShifts.PLAYER && shift != ShapeShifts.EERIE && shift != ShapeShifts.OVA) {
            if (shift == ShapeShifts.ZOMBIE) {
                if (Minecraft.getInstance().level != null && (!(sba$getShapeShift(acl) instanceof Zombie))) {
                    sba$setShapeShift(acl,EntityType.ZOMBIE.create(Minecraft.getInstance().level));
                }
            } else if (shift == ShapeShifts.VILLAGER) {
                if (Minecraft.getInstance().level != null && (!(sba$getShapeShift(acl) instanceof Villager))) {
                    sba$setShapeShift(acl,sba$getVillager(Minecraft.getInstance().level,ipe));
                }
            } else if (shift == ShapeShifts.SKELETON) {
                if (Minecraft.getInstance().level != null && (!(sba$getShapeShift(acl) instanceof Skeleton))) {
                    sba$setShapeShift(acl,sba$getSkeleton(Minecraft.getInstance().level,ipe));
                }
            } else if (shift == ShapeShifts.WITHER_SKELETON) {
                if (Minecraft.getInstance().level != null && (!(sba$getShapeShift(acl) instanceof WitherSkeleton))) {
                    sba$setShapeShift(acl,sba$getWither(Minecraft.getInstance().level,ipe));
                }
            } else if (shift == ShapeShifts.STRAY) {
                if (Minecraft.getInstance().level != null && (!(sba$getShapeShift(acl) instanceof Stray))) {
                    sba$setShapeShift(acl,sba$getStray(Minecraft.getInstance().level,ipe));
                }
            }
            if (sba$getShapeShift(acl) != null) {
                EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                EntityRenderer<? super T> ER = $$7.getRenderer(sba$getShapeShift(acl));
                if (ER instanceof LivingEntityRenderer) {
                    Model ml = ((LivingEntityRenderer<?, ?>) ER).getModel();

                    if (shift == ShapeShifts.ZOMBIE) {
                        if (ml instanceof ZombieModel<?> zm) {
                            if (ER instanceof ZombieRenderer zr && sba$getShapeShift(acl) instanceof Zombie zmb) {
                                this.setModelProperties(acl);
                                zm.attackTime = 0.0F;
                                zm.crouching = false;
                                zm.swimAmount = 0.0F;
                                if (right) {
                                    sba$renderOtherHand(stack, buffer, packedLight, acl, zm.rightArm, null, ml, zr.getTextureLocation(zmb));
                                } else {
                                    sba$renderOtherHand(stack, buffer, packedLight, acl, zm.leftArm, null, ml, zr.getTextureLocation(zmb));
                                }
                            }
                        }
                    } else if (shift == ShapeShifts.VILLAGER) {
                        if (ml instanceof VillagerModel<?> zm) {
                            if (ER instanceof VillagerRenderer zr && sba$getShapeShift(acl) instanceof Villager zmb) {
                                this.setModelProperties(acl);
                                zm.attackTime = 0.0F;
                            }
                        }
                    } else if (shift == ShapeShifts.SKELETON) {
                        if (ml instanceof SkeletonModel<?> sm) {
                            if (ER instanceof SkeletonRenderer zr && sba$getShapeShift(acl) instanceof Skeleton skl) {
                                this.setModelProperties(acl);
                                sm.attackTime = 0.0F;
                                sm.crouching = false;
                                sm.swimAmount = 0.0F;
                                if (right) {
                                    sba$renderOtherHand(stack, buffer, packedLight, acl, sm.rightArm, null, ml, zr.getTextureLocation(skl));
                                } else {
                                    sba$renderOtherHand(stack, buffer, packedLight, acl, sm.leftArm, null, ml, zr.getTextureLocation(skl));
                                }
                            }
                        }
                    } else if (shift == ShapeShifts.WITHER_SKELETON) {
                        if (ml instanceof SkeletonModel<?> sm) {
                            if (ER instanceof WitherSkeletonRenderer zr && sba$getShapeShift(acl) instanceof WitherSkeleton skl) {
                                this.setModelProperties(acl);
                                sm.attackTime = 0.0F;
                                sm.crouching = false;
                                sm.swimAmount = 0.0F;
                                if (right) {
                                    sba$renderOtherHand(stack, buffer, packedLight, acl, sm.rightArm, null, ml, zr.getTextureLocation(skl));
                                } else {
                                    sba$renderOtherHand(stack, buffer, packedLight, acl, sm.leftArm, null, ml, zr.getTextureLocation(skl));
                                }
                            }
                        }
                    } else if (shift == ShapeShifts.STRAY) {
                        if (ml instanceof SkeletonModel<?> sm) {
                            if (ER instanceof StrayRenderer zr && sba$getShapeShift(acl) instanceof Stray skl) {
                                this.setModelProperties(acl);
                                sm.attackTime = 0.0F;
                                sm.crouching = false;
                                sm.swimAmount = 0.0F;
                                if (right) {
                                    sba$renderOtherHand(stack, buffer, packedLight, acl, sm.rightArm, null, ml, zr.getTextureLocation(skl));
                                } else {
                                    sba$renderOtherHand(stack, buffer, packedLight, acl, sm.leftArm, null, ml, zr.getTextureLocation(skl));
                                }
                            }
                        }
                    }


                    return true;
                }
            }
        } else {
            if (sba$getSwappedModel(acl) != null){
                EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                EntityRenderer<? super T> ER = $$7.getRenderer(sba$getSwappedModel(acl));
                if (ER instanceof LivingEntityRenderer) {
                }
            }
        }
        return false;
    }

        @Unique
    private void sba$renderOtherHand(PoseStack stack, MultiBufferSource buffer, int $$2, AbstractClientPlayer acl,
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
    public void sba$corpseShowName(AbstractClientPlayer $$0, PoseStack $$3, MultiBufferSource $$4, int $$5){
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
    boolean sba$switched = false;
    public void sba$changeTheModel(AbstractClientPlayer player, ItemStack visage, ShapeShifts shifts){
        if (player instanceof StandUser su && su.sba$isDisguised()) {
            GameProfile profile = su.sba$getDisguiseProfile();
            if (profile != null) {
                SkinUtil.SkinData skin = SkinUtil.getSkin(profile);
                if (skin.slim() != originalArms) {
                    model = sba$otherModel;
                } else {
                    model = sba$mainModel;
                }
                return;
            }
        }
        IPlayerEntity pl = ((IPlayerEntity) player);
        visage = pl.sba$getMaskSlot();
        if (shifts == ShapeShifts.OVA) {
            visage = ModItems.ENYA_OVA_MASK.getDefaultInstance();
        } else if (shifts == ShapeShifts.EERIE) {
            visage = null;
        }

        if (visage != null && !visage.isEmpty()) {
            if (visage.getItem() instanceof MaskItem MI) {
                VisageData vd = MI.visageData.generateVisageData(player);
                if (vd.isCharacterVisage()) {
                    if (((IPlayerModel)this.model).sba$getSlim() != vd.isSlim()){
                        if (vd.isSlim() != originalArms){
                            model = sba$otherModel;
                        } else {
                            model = sba$mainModel;
                        }
                    }
                    return;
                } else if (visage.is(ModItems.RAT_MASK)){
                    if (((IPlayerModel) this.model).sba$getSlim()){
                        if (originalArms){
                            model = sba$otherModel;
                        } else {
                            model = sba$mainModel;
                        }
                    }
                }
            }
        }
        if (((IPlayerModel)this.model).sba$getSlim() != originalArms) {
            model = sba$mainModel;
        }
    }
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD"), cancellable = true)
    public<T extends LivingEntity, M extends EntityModel<T>> void sba$render(AbstractClientPlayer $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, CallbackInfo ci) {

        /**Access to slim and not slim models simultaneously*/
        IPlayerEntity ipe = ((IPlayerEntity) $$0);
        ItemStack visage = ipe.sba$getMaskSlot();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.sba$getShapeShift());
        sba$changeTheModel($$0,visage,shift);

            byte playerP = ((IPlayerEntity)$$0).sba$GetPos();

            /*When you die to the sun you don't appear*/
            if (playerP == PlayerPosIndex.SUNLIGHT){
                ci.cancel();
                return;
            }


        if (!ClientUtil.checkIfIsFirstPerson($$0)) {
            Poses pose = Poses.getPosFromByte(ipe.sba$GetPoseEmote());
            if (shift != ShapeShifts.PLAYER && shift != ShapeShifts.EERIE && shift != ShapeShifts.OVA) {

                if (shift == ShapeShifts.ZOMBIE) {
                    if (Minecraft.getInstance().level != null && (!(sba$getShapeShift($$0) instanceof Zombie))) {
                        sba$setShapeShift($$0, EntityType.ZOMBIE.create(Minecraft.getInstance().level));
                    }
                    if (sba$getShapeShift($$0) != null) {
                        ItemStack tem = $$0.getMainHandItem();
                        sba$getShapeShift($$0).setAggressive(!tem.isEmpty() && tem.getMaxDamage() > 0);
                        sba$renderEntityForce1($$1, $$2, $$3, $$4, sba$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        sba$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.VILLAGER) {
                    if (Minecraft.getInstance().level != null && (!(sba$getShapeShift($$0) instanceof Villager))) {
                        sba$setShapeShift($$0, sba$getVillager(Minecraft.getInstance().level, ipe));
                    }
                    if (sba$getShapeShift($$0) != null) {
                        if (sba$getShapeShift($$0) instanceof Villager ve) {
                            if ($$0.isSleeping() && !ve.isSleeping()) {
                                Optional<BlockPos> blk = $$0.getSleepingPos();
                                blk.ifPresent(ve::startSleeping);
                            } else {
                                if (!$$0.isSleeping()) {
                                    ve.stopSleeping();
                                }
                            }
                        }
                        sba$renderEntityForce1($$1, $$2, $$3, $$4, sba$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        sba$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.SKELETON) {
                    if (Minecraft.getInstance().level != null && (!(sba$getShapeShift($$0) instanceof Skeleton))) {
                        sba$setShapeShift($$0, sba$getSkeleton(Minecraft.getInstance().level, ipe));
                    }
                    if (sba$getShapeShift($$0) != null) {
                        ItemStack tem = $$0.getMainHandItem();
                        sba$getShapeShift($$0).setAggressive(!tem.isEmpty() && tem.getMaxDamage() > 0);
                        sba$renderEntityForce1($$1, $$2, $$3, $$4, sba$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        sba$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.WITHER_SKELETON) {
                    if (Minecraft.getInstance().level != null && (!(sba$getShapeShift($$0) instanceof WitherSkeleton))) {
                        sba$setShapeShift($$0, sba$getWither(Minecraft.getInstance().level, ipe));
                    }
                    if (sba$getShapeShift($$0) != null) {
                        ItemStack tem = $$0.getMainHandItem();
                        sba$getShapeShift($$0).setAggressive(!tem.isEmpty() && tem.getMaxDamage() > 0);
                        sba$renderEntityForce1($$1, $$2, $$3, $$4, sba$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        sba$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.STRAY) {
                    if (Minecraft.getInstance().level != null && (!(sba$getShapeShift($$0) instanceof Stray))) {
                        sba$setShapeShift($$0, sba$getStray(Minecraft.getInstance().level, ipe));
                    }
                    if (sba$getShapeShift($$0) != null) {
                        ItemStack tem = $$0.getMainHandItem();
                        sba$getShapeShift($$0).setAggressive(!tem.isEmpty() && tem.getMaxDamage() > 0);
                        sba$renderEntityForce1($$1, $$2, $$3, $$4, sba$getShapeShift($$0), $$0, $$5);
                        ci.cancel();
                        sba$corpseShowName($$0, $$3, $$4, $$5);
                    }
                } else if (shift == ShapeShifts.OVA) {
                    if (Minecraft.getInstance().level != null && (!(sba$getShapeShift($$0) instanceof OVAEnyaNPC))) {
                        sba$setShapeShift($$0, ModEntities.OVA_ENYA.create(Minecraft.getInstance().level));
                    }
                    if (sba$getShapeShift($$0) != null) {
                        if (sba$getShapeShift($$0) instanceof OVAEnyaNPC ve) {
                            ve.standPos = pose;
                            ve.setupAnimationStates();
                            ve.host = $$0;
                            assertOnPlayerLike(ve, $$0, $$1, $$2, $$3, $$4, $$5,
                                    sba$getShapeShift($$0));
                            ci.cancel();
                            sba$corpseShowName($$0, $$3, $$4, $$5);
                        }
                    }
                }
            } else {

               sba$setSwappedModel($$0, null);

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
    public<T extends LivingEntity, M extends EntityModel<T>> void sba$setModelProps(AbstractClientPlayer $$0, CallbackInfo ci) {
        /**Set visibility of limbs*/
        /**Generally sleeves and whatnot gotta go for modded layers*/
        StandUser user = ((StandUser) $$0);
        int muscle = user.sba$getZappedToID();
        //muscle = 100;
        byte curse = user.sba$getLocacacaCurse();
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
    public<T extends LivingEntity, M extends EntityModel<T>> void sba$renderTail(AbstractClientPlayer entity, float $$1, float $$2, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {

    }

        @Unique
    public void sba$doJojoAnims(JojoNPC jj){
        jj.WRYYY.stop();
        jj.JOTARO.stop();
        jj.KOICHI.stop();
        jj.GIORNO.stop();
        jj.JONATHAN.stop();
        jj.JOSEPH.stop();
        jj.TORTURE_DANCE.stop();
        jj.OH_NO.stop();
        jj.WAMUU.stop();
            jj.ANASUI.stop();
            jj.VAMPIRE.stop();
    }

    @Unique
    boolean sba$wasJustCreated;

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
        ila2.sba$setSwimAmount(ila.sba$getSwimAmount());
        ila2.sba$setSwimAmountO(ila.sba$getSwimAmountO());
        ila2.sba$setWasTouchingWater(ila.sba$getWasTouchingWater());
        ila2.sba$setFallFlyingTicks($$0.getFallFlyingTicks());
        ila2.sba$setSharedFlag(1, ila.sba$getSharedFlag(1));
        ila2.sba$setSharedFlag(2, ila.sba$getSharedFlag(2));
        ila2.sba$setSharedFlag(3, ila.sba$getSharedFlag(3));
        ila2.sba$setSharedFlag(4, ila.sba$getSharedFlag(4));
        ila2.sba$setSharedFlag(5, ila.sba$getSharedFlag(5));
        ila2.sba$setSharedFlag(6, ila.sba$getSharedFlag(6));
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
        ve.sba$setDodgeTime(ipe.sba$getDodgeTime());
        ve.sba$setClientDodgeTime(ipe.sba$getClientDodgeTime());
        ve.sba$SetPos(ipe.sba$GetPos());
        ila2.sba$setUseItem($$0.getUseItem());
        ila2.sba$setUseItemTicks($$0.getUseItemRemainingTicks());
        ve.host = $$0;


        if ($$0.isFallFlying()){
            if (!ila2.sba$getSharedFlag(7)) {
                ila2.sba$setSharedFlag(7, true);
            }
        } else {
            if (ila2.sba$getSharedFlag(7)){
                ila2.sba$setSharedFlag(7,false);
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
        sba$renderEntityForce1($$1, $$2, $$3, $$4, entityeah, $$0, $$5);
    }

    @Unique
    public Villager sba$getVillager(ClientLevel lev, IPlayerEntity ipe){
        Villager vil = EntityType.VILLAGER.create(lev);
        if (vil != null) {
            byte BT = ipe.sba$getShapeShiftExtraData();
            vil.setVillagerData(vil.getVillagerData().setType(ShapeShifts.getTypeFromByte(BT)));
            vil.setVillagerData(vil.getVillagerData().setProfession(ShapeShifts.getProfessionFromByte(BT)));
        }
        return vil;
    }
    @Unique
    public Skeleton sba$getSkeleton(ClientLevel lev, IPlayerEntity ipe){
        Skeleton vil = EntityType.SKELETON.create(lev);
        if (vil != null) {
            vil.setItemInHand(InteractionHand.MAIN_HAND, Items.BOW.getDefaultInstance());
        }
        return vil;
    }
    @Unique
    public Stray sba$getStray(ClientLevel lev, IPlayerEntity ipe){
        Stray vil = EntityType.STRAY.create(lev);
        if (vil != null) {
            vil.setItemInHand(InteractionHand.MAIN_HAND, Items.BOW.getDefaultInstance());
        }
        return vil;
    }
    @Unique
    public WitherSkeleton sba$getWither(ClientLevel lev, IPlayerEntity ipe){
        WitherSkeleton vil = EntityType.WITHER_SKELETON.create(lev);
        if (vil != null) {
            vil.setItemInHand(InteractionHand.MAIN_HAND, Items.STONE_SWORD.getDefaultInstance());
        }
        return vil;
    }


    @Unique
    public void sba$renderEntityForce1(float f1, float f2, PoseStack $$3, MultiBufferSource $$4, LivingEntity $$6, Player user, int light) {

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
        ((StandUser) $$6).sba$setBubbleEncased(((StandUser)user).sba$getBubbleEncased());
        ((IEntityAndData)$$6).sba$setExclusiveLayers(((IEntityAndData)user).sba$getExclusiveLayers());
        ((StandUser) $$6).sba$setEmulator(user);
        IEntityAndData entd = ((IEntityAndData) user);
        IEntityAndData entd2 = ((IEntityAndData) $$6);
        entd2.sba$setVehicle(entd.sba$getVehicle());

        $$6.walkAnimation.setSpeed(user.walkAnimation.speed());
        IWalkAnimationState iwalk = ((IWalkAnimationState) $$6.walkAnimation);
        IWalkAnimationState uwalk = ((IWalkAnimationState) user.walkAnimation);
        iwalk.sba$setPosition(uwalk.sba$getPosition());
        iwalk.sba$setSpeedOld(uwalk.sba$getSpeedOld());

        ILivingEntityAccess ilive = ((ILivingEntityAccess)$$6);
        ILivingEntityAccess ulive = ((ILivingEntityAccess)user);
        ilive.sba$setAnimStep(ulive.sba$getAnimStep());
        ilive.sba$setAnimStepO(ulive.sba$getAnimStepO());
        $$6.setSpeed(user.getSpeed());
        ((IEntityAndData)$$6).sba$setNoAAB();

        sba$renderEntityForce2(f1,f2,$$3,$$4,$$6, light, user);
        ClientUtil.popPoseAndCooperate($$3,23);
    }

    @Unique
    public void sba$renderEntityForce2(float f1, float f2, PoseStack $$3, MultiBufferSource $$4,LivingEntity $$6, int light, LivingEntity user) {
        EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
        Vec3 renderoffset = $$7.getRenderer(user).getRenderOffset(user,0);

        if (!renderoffset.equals(Vec3.ZERO)){
            $$3.translate(-1*renderoffset.x,-1*renderoffset.y,-1*renderoffset.z);
        }

        if (light == 15728880) {
            ((IEntityAndData) $$6).sba$setShadow(false);
            ((IEntityAndData) user).sba$setShadow(false);
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
    public void sba$scale(AbstractClientPlayer $$0, PoseStack $$1, float $$2, CallbackInfo ci) {
        IPlayerEntity ple = ((IPlayerEntity) $$0);
        ItemStack visage = ple.sba$getMaskSlot();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(ple.sba$getShapeShift());
        if (shift == ShapeShifts.OVA) {
            visage = ModItems.ENYA_OVA_MASK.getDefaultInstance();
        } else if (shift == ShapeShifts.EERIE) {
            visage = null;
        }


        if (visage != null && !visage.isEmpty()) {
            if (visage.getItem() instanceof MaskItem MI) {
                if (MI instanceof ModificationMaskItem MD){
                    if (visage.getTag() != null) {
                        CompoundTag tag = visage.getOrCreateTagElement("modifications");
                        if (tag.contains("height") || tag.contains("width")) {
                            int height = visage.getOrCreateTagElement("modifications").getInt("height");
                            int width = visage.getOrCreateTagElement("modifications").getInt("width");
                            $$1.scale(0.798F + (((float) width) * 0.001F), 0.7F + (((float) height) * 0.001F), 0.798F + (((float) width) * 0.001F));
                        }
                    }
                } else {
                    VisageData vd = MI.visageData.generateVisageData($$0);
                    Vector3f scale =vd.scale();
                    $$1.scale(scale.x, scale.y, scale.z);
                }

                ci.cancel();
            }
        }
        byte widthMod = ((IPlayerEntity)$$0).sba$getWidthFactor();
        if (widthMod > 0){
            if (widthMod < 5){
                float factor = 1f-(0.03F*widthMod);
                Vector3f scale = new Vector3f(factor,1,factor);
                $$1.scale(scale.x, scale.y, scale.z);
            } else {
                float factor = 1f+(0.07F*(widthMod-4));
                Vector3f scale = new Vector3f(factor,1,factor);
                $$1.scale(scale.x, scale.y, scale.z);
            }
        }
    }
    @Inject(method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "HEAD"), cancellable = true)
    public void sba$getTextureLocation(AbstractClientPlayer $$0, CallbackInfoReturnable<ResourceLocation> cir) {
    }
    @Shadow
    public ResourceLocation getTextureLocation(AbstractClientPlayer var1) {
        return null;
    }
}
