package net.hydra.jojomod.client.models.layers.visages;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityAbstractClient;
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
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
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

public class VisagePartLayer2<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {

    private final EntityRenderDispatcher dispatcher;
    public VisagePartLayer2(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
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
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, float var9, float var10) {
        if (MainUtil.isHumanoid2(entity)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft != null) {
                if (ClientUtil.getThrowFadePercent(entity, partialTicks) <= 0) {
                    return;
                }
                boolean $$18 = !entity.isInvisible();
                boolean $$19 = !$$18 && !entity.isInvisibleTo(minecraft.player);
                boolean $$20 = minecraft.shouldEntityAppearGlowing(entity);
                boolean $$21 = this.getRenderT($$18, $$19, $$20);
                if ($$21) {


                    boolean isHurt = entity.hurtTime > 0;
                    float r = isHurt ? 1.0F : 1.0F;
                    float g = isHurt ? 0.6F : 1.0F;
                    float b = isHurt ? 0.6F : 1.0F;
                    StandUser user = ((StandUser) entity);


                    boolean hasWhiteAlbumOut = user.roundabout$getStandPowers() instanceof PowersWhiteAlbum pw && pw.renderHelmet();
                    int whiteAlbumTicks = user.roundabout$getWhiteAlbumVanishTicks();
                    boolean hideExtraPartsWithSuit = false;
                    float heyFull = 0;
                    byte skin = user.roundabout$getStandSkin();
                    if (hasWhiteAlbumOut || whiteAlbumTicks > 0) {
                        if (user.roundabout$getLastStandSkin() != skin) {
                            user.roundabout$setLastStandSkin(skin);
                            whiteAlbumTicks = 0;
                            user.roundabout$setWhiteAlbumVanishTicks(0);
                        }

                        float partialTicks2 = partialTicks % 1;
                        if (hasWhiteAlbumOut) {
                            heyFull = whiteAlbumTicks + partialTicks2;
                            heyFull = Math.min(heyFull / 10, 1f);
                        } else {
                            heyFull = whiteAlbumTicks - partialTicks2;
                            heyFull = Math.max(heyFull / 10, 0);
                        }

                        if (heyFull > 0) {
                            hideExtraPartsWithSuit = true;
                        }
                    }

                    if (hasWhiteAlbumOut || whiteAlbumTicks > 0) {

                        if (user.roundabout$getStandPowers() instanceof PowersWhiteAlbum pw) {

                            String path = PowersWhiteAlbum.getSkinString(skin);
                            String path2 = path;
                            if (!ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
                                path = "ice";
                                path2 = null;
                            }
                            if (pw.cracked) {
                                path = "cracked/" + path;
                            }
                            poseStack.pushPose();
                            renderWhiteAlbumHeadPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                    partialTicks, path, r, g, b, heyFull);
                            renderWhiteAlbumBodyPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                    partialTicks, path, r, g, b, heyFull);
                            if (skin == PowersWhiteAlbum.YUKI && path2 != null && !pw.cracked) {
                                renderWhiteAlbumHeadPart(poseStack, bufferSource, 15728880, entity, xx, yy, zz,
                                        partialTicks, path + "_glowing", r, g, b, heyFull);
                                renderWhiteAlbumBodyPart(poseStack, bufferSource, 15728880, entity, xx, yy, zz,
                                        partialTicks, path + "_glowing", r, g, b, heyFull);
                            }
                            renderWhiteAlbumRightLegPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                    partialTicks, path, r, g, b, heyFull);
                            renderWhiteAlbumLeftLegPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                    partialTicks, path, r, g, b, heyFull);

                            if (getParentModel() instanceof PlayerModel<?> PM && ((IPlayerModel) PM).roundabout$getSlim()) {
                                renderWhiteAlbumSlimRightArmPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                        partialTicks, path, r, g, b, heyFull);
                                renderWhiteAlbumSlimLeftArmPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                        partialTicks, path, r, g, b, heyFull);
                            } else {
                                renderWhiteAlbumRightArmPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                        partialTicks, path, r, g, b, heyFull);
                                renderWhiteAlbumLeftArmPart(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                        partialTicks, path, r, g, b, heyFull);
                            }

                            if (pw.hasSkatesActivated()) {
                                renderWhiteAlbumSkates(poseStack, bufferSource, packedLight, entity, xx, yy, zz,
                                        partialTicks, path, r, g, b, heyFull);
                            }

                            poseStack.popPose();
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
    public void renderWhiteAlbumBodyPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                          float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumBody.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderWhiteAlbumSkates(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                             float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().rightLeg.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumSkate.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().leftLeg.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumSkate.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderWhiteAlbumRightLegPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                         float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().rightLeg.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumRightLeg.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderWhiteAlbumLeftLegPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                             float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().leftLeg.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumLeftLeg.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderWhiteAlbumLeftArmPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                            float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().leftArm.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumLeftArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderWhiteAlbumSlimLeftArmPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                            float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().leftArm.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumSlimLeftArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderWhiteAlbumRightArmPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                            float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().rightArm.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumRightArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderWhiteAlbumSlimRightArmPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                                float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().rightArm.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumSlimRightArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
    public void renderWhiteAlbumHeadPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                              float r, float g, float b, float alpha) {

        ClientUtil.pushPoseAndCooperate(poseStack,36);
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.WhiteAlbumHead.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, alpha, path);
        ClientUtil.popPoseAndCooperate(poseStack,36);
    }
}
