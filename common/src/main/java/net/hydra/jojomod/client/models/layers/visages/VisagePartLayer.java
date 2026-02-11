package net.hydra.jojomod.client.models.layers.visages;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.hydra.jojomod.item.BowlerHatItem;

import java.util.Map;

public class VisagePartLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {

    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public VisagePartLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    protected boolean getRenderT(boolean $$1, boolean $$2, boolean $$3) {
        if ($$2 || $$1) {
            return true;
        } else {
            return $$3 ? true : false;
        }
    }
    float scale = 1;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/large_bubble.png");
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, float var9, float var10) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null){
            if (entity instanceof CloneEntity fcg && fcg.player != null){
                entity = (T)fcg.player;
            }

            if (ClientUtil.getThrowFadePercent(entity,partialTicks) <= 0){
                return;
            }
            boolean $$18 = !entity.isInvisible();
            boolean $$19 = !$$18 && !entity.isInvisibleTo(minecraft.player);
            boolean $$20 = minecraft.shouldEntityAppearGlowing(entity);
            boolean $$21 = this.getRenderT($$18, $$19, $$20);
            if ($$21) {

                ItemStack visage = null;
                if (entity instanceof Player play) {
                    IPlayerEntity pl = ((IPlayerEntity) play);
                    visage = pl.roundabout$getMaskSlot();
                    ShapeShifts shift = ShapeShifts.getShiftFromByte(pl.roundabout$getShapeShift());
                    if (shift == ShapeShifts.OVA) {
                        visage = ModItems.ENYA_OVA_MASK.getDefaultInstance();
                    } else if (shift == ShapeShifts.EERIE) {
                        visage = null;
                    }
                } else if (entity instanceof JojoNPC jnpc) {
                    visage = jnpc.getBasis();
                } else if (entity instanceof ZombieAesthetician znpc) {
                    visage = znpc.getBasis();
                }

                boolean isHurt = entity.hurtTime > 0;
                float r = isHurt ? 1.0F : 1.0F;
                float g = isHurt ? 0.6F : 1.0F;
                float b = isHurt ? 0.6F : 1.0F;
                StandUser user = ((StandUser) entity);

                if (user.roundabout$getStandPowers() instanceof PowersWalkingHeart PW && (PW.inCombatMode() || PW.hasExtendedHeelsForWalking())){
                    if (user instanceof AbstractClientPlayer PE) {
                        renderRightHeelPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, PE.getSkinTextureLocation(),
                                r, g, b);
                        renderLeftHeelPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, PE.getSkinTextureLocation(),
                                r, g, b);
                    }
                }

                byte curse = user.roundabout$getLocacacaCurse();
                int muscle = user.roundabout$getZappedToID();
                //muscle = 100;
                if (muscle > -1) {
                    float scale = 1.055F;
                    float alpha = 0.6F;
                    float oscillation = Math.abs(((entity.tickCount % 10) + (partialTicks % 1)) - 5) * 0.04F;
                    alpha += oscillation;
                    if (entity.getMainArm() == HumanoidArm.RIGHT) {
                        if (curse != LocacacaCurseIndex.RIGHT_HAND && !HeatUtil.isArmsFrozen(entity)) {
                            if (getParentModel() instanceof PlayerModel<?> PM && ((IPlayerModel) PM).roundabout$getSlim()) {
                                renderRightArmSlim(poseStack, bufferSource, packedLight, entity, scale, scale, scale, partialTicks,
                                        r, g, b, StandIcons.MUSCLE_SLIM, 0.01F, 0, 0, alpha);
                            } else {
                                renderRightArm(poseStack, bufferSource, packedLight, entity, scale, scale, scale, partialTicks,
                                        r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                            }
                        }
                        if (curse != LocacacaCurseIndex.RIGHT_LEG && !HeatUtil.isLegsFrozen(entity)) {
                            renderRightLeg(poseStack, bufferSource, packedLight, entity, scale, scale, scale, partialTicks,
                                    r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                        }
                    } else {
                        if (curse != LocacacaCurseIndex.LEFT_HAND && !HeatUtil.isArmsFrozen(entity)) {
                            if (getParentModel() instanceof PlayerModel<?> PM && ((IPlayerModel) PM).roundabout$getSlim()) {
                                renderLeftArmSlim(poseStack, bufferSource, packedLight, entity, scale, scale, scale, partialTicks,
                                        r, g, b, StandIcons.MUSCLE_SLIM, -0.01F, 0, 0, alpha);
                            } else {
                                renderLeftArm(poseStack, bufferSource, packedLight, entity, scale, scale, scale, partialTicks,
                                        r, g, b, StandIcons.MUSCLE, -0.01F, 0, 0, alpha);
                            }
                        }
                        if (curse != LocacacaCurseIndex.LEFT_LEG && !HeatUtil.isLegsFrozen(entity)) {
                            renderLeftLeg(poseStack, bufferSource, packedLight, entity, scale, scale, scale, partialTicks,
                                    r, g, b, StandIcons.MUSCLE, -0.01F, 0, 0, alpha);
                        }
                    }
                }

                ItemStack hand = entity.getMainHandItem();
                ItemStack offHand = entity.getOffhandItem();

                if (visage != null && !visage.isEmpty()) {
                    boolean isBodyFrozen = HeatUtil.isBodyFrozen(entity);
                    if (visage.getItem() instanceof MaskItem MI) {
                        VisageData vd = MI.visageData.generateVisageData(entity);
                        String path = MI.visageData.getSkinPath();
                        if (vd.rendersBreast()) {
                            renderNormalBreast(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersSmallBreast()) {
                            renderSmallBreast(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersPonytail() && !isBodyFrozen) {
                            renderPonytail(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersBigHair() && !(hand.getItem() instanceof BowlerHatItem) && !(offHand.getItem() instanceof BowlerHatItem) && !isBodyFrozen) {
                            renderBigHair(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersKakyoinHair() && !(hand.getItem() instanceof BowlerHatItem) && !(offHand.getItem() instanceof BowlerHatItem) && !isBodyFrozen) {
                            renderKakyoinHair(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersDiegoHat() && !isBodyFrozen && !MainUtil.isWearingEitherStoneMask(entity) && !(hand.getItem() instanceof BowlerHatItem) && !(offHand.getItem() instanceof BowlerHatItem)) {
                            renderDiegoHat(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersSpeedwagonFoundationHat() && !isBodyFrozen && !MainUtil.isWearingEitherStoneMask(entity) && !(hand.getItem() instanceof BowlerHatItem) && !(offHand.getItem() instanceof BowlerHatItem)) {
                            renderSpeedwagonFoundationHat(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersBasicHat() && !isBodyFrozen && !MainUtil.isWearingEitherStoneMask(entity) && !(hand.getItem() instanceof BowlerHatItem) && !(offHand.getItem() instanceof BowlerHatItem)) {
                            renderBasicHat(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersSpikeyHair() && !isBodyFrozen && !MainUtil.isWearingEitherStoneMask(entity) && !(hand.getItem() instanceof BowlerHatItem) && !(offHand.getItem() instanceof BowlerHatItem)) {
                            renderSpikeyHair(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersJosukeDecals() && !isBodyFrozen) {
                            renderJosukeDecals(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersTasselHat() && !isBodyFrozen && !(hand.getItem() instanceof BowlerHatItem) && !(offHand.getItem() instanceof BowlerHatItem)) {
                            renderTasselHat(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersLegCloakPart() && !isBodyFrozen) {
                            renderLegCloakPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersAvdolHairPart() && !isBodyFrozen && !(hand.getItem() instanceof BowlerHatItem) && !(offHand.getItem() instanceof BowlerHatItem)) {
                            renderAvdolHair(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                    r, g, b);
                        }
                        if (vd.rendersPlayerBreastPart()) {
                            renderPlayerBreastPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                    r, g, b);
                        }
                        if (vd.rendersPlayerSmallBreastPart()) {
                            renderSmallPlayerBreastPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                    r, g, b);
                        }
                    }
                }

                if (entity instanceof Player play) {
                    ClientUtil.pushPoseAndCooperate(poseStack, 46);
                    IPlayerEntity pl = ((IPlayerEntity) play);
                    byte pos2 = pl.roundabout$GetPos2();
                    if (pos2 == PlayerPosIndex.BARRAGE) {
                        renderBarrageArmsPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                r, g, b);
                    } else if (pos2 == PlayerPosIndex.HAIR_EXTENSION || pos2 == PlayerPosIndex.HAIR_EXTENSION_2){
                        if (!isHurt){
                            r = pl.rdbt$getHairColorX();
                            g = pl.rdbt$getHairColorY();
                            b = pl.rdbt$getHairColorZ();

                            if (visage != null && !visage.isEmpty() && visage.getItem() instanceof MaskItem ME) {
                                VisageData vd = ME.visageData;
                                if (vd != null && vd.isCharacterVisage()) {
                                    r = ((float) vd.getHairColor().getX()) / 255;
                                    g = ((float) vd.getHairColor().getY()) / 255;
                                    b = ((float) vd.getHairColor().getZ()) / 255;
                                }
                            }
                        }
                        renderVampireHairOne(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                r, g, b);
                        renderVampireHairTwo(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                r, g, b);
                        renderVampireHairFleshBud(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                r, g, b);
                    } else if (pos2 == PlayerPosIndex.HAIR_SPIKE || pos2 == PlayerPosIndex.HAIR_SPIKE_2){
                        if (!isHurt){
                            r = pl.rdbt$getHairColorX();
                            g = pl.rdbt$getHairColorY();
                            b = pl.rdbt$getHairColorZ();

                            if (visage != null && !visage.isEmpty() && visage.getItem() instanceof MaskItem ME) {
                                VisageData vd = ME.visageData;
                                if (vd != null && vd.isCharacterVisage()) {
                                    r = ((float) vd.getHairColor().getX()) / 255;
                                    g = ((float) vd.getHairColor().getY()) / 255;
                                    b = ((float) vd.getHairColor().getZ()) / 255;
                                }
                            }
                        }

                        renderBodySpike(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                r, g, b);
                    }
                    ClientUtil.popPoseAndCooperate(poseStack, 46);
                }


                r = isHurt ? 1.0F : 1.0F;
                g = isHurt ? 0.6F : 1.0F;
                b = isHurt ? 0.6F : 1.0F;

                if (ClientUtil.hasChangedArms(entity)) {
                    if (getParentModel() instanceof PlayerModel<?> pm) {
                        pm.rightArm.visible = true;
                        pm.rightSleeve.visible = true;
                        pm.leftArm.visible = true;
                        pm.leftSleeve.visible = true;
                        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(ClientUtil.getChangedArmTexture(entity)));
                        ClientUtil.pushPoseAndCooperate(poseStack, 46);
                        pm.rightArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        pm.rightSleeve.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        ClientUtil.popPoseAndCooperate(poseStack, 46);

                        ClientUtil.pushPoseAndCooperate(poseStack, 46);
                        pm.leftArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        pm.leftSleeve.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        ClientUtil.popPoseAndCooperate(poseStack, 46);
                    }
                }


                if (ClientUtil.hasChangedLegs(entity)) {
                    if (getParentModel() instanceof PlayerModel<?> pm) {
                        pm.leftLeg.visible = true;
                        pm.leftPants.visible = true;
                        pm.rightPants.visible = true;
                        pm.rightLeg.visible = true;
                        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(ClientUtil.getChangedLegTexture(entity)));
                        ClientUtil.pushPoseAndCooperate(poseStack, 46);
                        pm.rightLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        pm.rightPants.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        ClientUtil.popPoseAndCooperate(poseStack, 46);
                        ClientUtil.pushPoseAndCooperate(poseStack, 46);
                        pm.leftLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        pm.leftPants.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        ClientUtil.popPoseAndCooperate(poseStack, 46);
                    }
                }
                if (ClientUtil.hasChangedBody(entity)) {
                    if (getParentModel() instanceof PlayerModel<?> pm) {
                        pm.body.visible = true;
                        pm.jacket.visible = true;
                        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(ClientUtil.getChangedBodyTexture(entity)));
                        ClientUtil.pushPoseAndCooperate(poseStack, 46);
                        pm.body.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        pm.jacket.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        ClientUtil.popPoseAndCooperate(poseStack, 46);
                    }
                }
                if (ClientUtil.hasChangedHead(entity)) {
                    if (getParentModel() instanceof PlayerModel<?> pm) {
                        pm.head.visible = true;
                        pm.hat.visible = true;
                        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(ClientUtil.getChangedHeadTexture(entity)));
                        ClientUtil.pushPoseAndCooperate(poseStack, 46);
                        pm.head.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        pm.hat.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r,
                                g, b, 1);
                        ClientUtil.popPoseAndCooperate(poseStack, 46);
                    }
                }
            }


            if (ClientUtil.rendersRipperEyes(entity)) {
                boolean isHurt = entity.hurtTime > 0;
                renderRipperEyes(poseStack, bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                        1, 1, 1);
            }

        }
    }

    public void renderRightLeg(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                               float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (getParentModel().rightLeg.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,46);
            getParentModel().rightLeg.translateAndRotate(poseStack);
            ModStrayModels.RightLeg.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,46);
        }
    }

    public void renderLeftLeg(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                               float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (getParentModel().leftLeg.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,45);
            getParentModel().leftLeg.translateAndRotate(poseStack);
            ModStrayModels.LeftLeg.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,45);
        }
    }
    public void renderRightArm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                           float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (getParentModel().rightArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,27);
            getParentModel().rightArm.translateAndRotate(poseStack);
            ModStrayModels.RightArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,28);
        }
    }
    public void renderRightArmSlim(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                               float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (getParentModel().rightArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,29);
            getParentModel().rightArm.translateAndRotate(poseStack);
            ModStrayModels.RightArmSlim.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,29);
        }
    }
    public void renderLeftArm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                               float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (getParentModel().leftArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,30);
            getParentModel().leftArm.translateAndRotate(poseStack);
            ModStrayModels.LeftArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,30);
        }
    }
    public void renderLeftArmSlim(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                   float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (getParentModel().leftArm.visible) {
            ClientUtil.pushPoseAndCooperate(poseStack,31);
            getParentModel().leftArm.translateAndRotate(poseStack);
            ModStrayModels.LeftArmSlim.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            ClientUtil.popPoseAndCooperate(poseStack,31);
        }
    }
    public void renderNormalBreast(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {
        ClientUtil.pushPoseAndCooperate(poseStack,32);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.ChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,32);
    }
    public void renderSmallBreast(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {
        ClientUtil.pushPoseAndCooperate(poseStack,33);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.SmallChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,33);
    }
    public void renderBodySpike(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                  float r, float g, float b) {
        ClientUtil.pushPoseAndCooperate(poseStack,33);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.bodySpikePart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1);
        ClientUtil.popPoseAndCooperate(poseStack,33);
    }
    public void renderPonytail(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,34);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.PonytailPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,34);
    }
    public void renderBigHair(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                               float r, float g, float b) {
        ClientUtil.pushPoseAndCooperate(poseStack,35);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.BigHairPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,35);
    }
    public void renderKakyoinHair(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                              float r, float g, float b) {
        ClientUtil.pushPoseAndCooperate(poseStack,35);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.KakyoinHairPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,35);
    }
    public void renderDiegoHat(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                              float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.DiegoHatPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderRipperEyes(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                               float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().head.translateAndRotate(poseStack);
        poseStack.translate(0,0,0.1);
        boolean yes = false;
        for (var i = 0; i < 80; i++) {
            ClientUtil.pushPoseAndCooperate(poseStack,36);
            if (yes) {
                poseStack.scale(1.01F, 1.01F, 1.01F);
            }
            ModStrayModels.ripperEyesPart.render(entity, partialTicks, poseStack, bufferSource, LightTexture.FULL_BRIGHT,
                    r, g, b, 0.8F);
            ClientUtil.popPoseAndCooperate(poseStack,36);
            poseStack.translate(0,0,-0.5);
            yes = !yes;
        }
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderSpeedwagonFoundationHat(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                               float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.SpeedwagonFoundationHatPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderBasicHat(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                               float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,37);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.BasicHatPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,37);
    }
    public void renderSpikeyHair(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                               float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,38);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.SpikeyHairPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,38);
    }
    public void renderVampireHairOne(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                 float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,38);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.VampireHairOne.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1);
        ClientUtil.popPoseAndCooperate(poseStack,38);
    }
    public void renderVampireHairTwo(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                     float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,38);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.VampireHairTwo.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1);
        ClientUtil.popPoseAndCooperate(poseStack,38);
    }
    public void renderVampireHairFleshBud(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                     float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,38);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.VampireHairFlesh.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1);
        ClientUtil.popPoseAndCooperate(poseStack,38);
    }
    public void renderAvdolHair(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                 float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,39);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.AvdolHairPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,39);
    }
    public void renderJosukeDecals(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                 float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,40);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.JosukeDecalsPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,40);
    }
    public void renderTasselHat(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                 float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,41);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.TasselHatPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,41);
    }
    public void renderLegCloakPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,42);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.LegCloakPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path,-1*(Math.min(getParentModel().leftLeg.xRot,getParentModel().rightLeg.xRot)));

        ClientUtil.popPoseAndCooperate(poseStack,42);
    }
    public void renderRightHeelPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, ResourceLocation path,
                                   float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,45);
        getParentModel().leftLeg.translateAndRotate(poseStack);
        ModStrayModels.LeftHeel.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);

        ClientUtil.popPoseAndCooperate(poseStack,45);
    }
    public void renderLeftHeelPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, ResourceLocation path,
                                    float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,45);
        getParentModel().rightLeg.translateAndRotate(poseStack);
        ModStrayModels.RightHeel.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        ClientUtil.popPoseAndCooperate(poseStack,45);
    }
    public void renderBarrageArmsPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                       float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,43);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.barrageArmsPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 0.8F);
        ClientUtil.popPoseAndCooperate(poseStack,43);
    }
    public void renderPlayerBreastPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                   float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,43);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.PlayerChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1);
        ClientUtil.popPoseAndCooperate(poseStack,43);
    }
    public void renderSmallPlayerBreastPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                       float r, float g, float b) {

        ClientUtil.pushPoseAndCooperate(poseStack,44);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.PlayerSmallChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1);
        ClientUtil.popPoseAndCooperate(poseStack,44);
    }
}
