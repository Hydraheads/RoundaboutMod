package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CenturyBoyLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private final EntityRenderDispatcher dispatcher;

    public CenturyBoyLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    /// hello and welcome to the centuryboy rendering tutorial, take a seat and prepare, the lesson is about to start
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {

        /// basic checks to make sure that the player can see stands and the user of 20thcb isn't invisible
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            if (entity != null) {
                if (((IEntityAndData) entity).roundabout$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung())
                    return;


                /// check to see if 20thCB is activated
                StandUser user = ((StandUser) entity);
                boolean hasCB = (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy);
                boolean standOut = (PowerTypes.hasStandActive(entity) && hasCB);
                if (!entity.isInvisible()) {
                    int CBticks = user.roundabout$getCBVanishTicks();

                    /// skin definition
                    if (CBticks > 0 || standOut) {
                        byte skin = user.roundabout$getStandSkin();
                        if (user.roundabout$getLastStandSkin() != skin) {
                            user.roundabout$setLastStandSkin(skin);
                            CBticks = 0;
                            user.roundabout$setCBVanishTicks(0);
                        }

                        /// from my understanding these are the ticks that 20thcb are able to render basically
                        float heyfull = 0;
                        float fixedPartial = partialTicks - (int) partialTicks;

                        /// time stop check
                        if (((TimeStop) entity.level()).CanTimeStopEntity(entity)) {
                            fixedPartial = 0;
                        }

                        /// checks if the stand is currently active and defines if it should render in this tick
                        if (standOut) {
                            heyfull = CBticks + fixedPartial;
                            heyfull = Math.min(heyfull / 10, 1f);
                        } else {
                            heyfull = CBticks - fixedPartial;
                            heyfull = Math.max(heyfull / 10, 0);
                        }


                        /** this is part exclusive of CenturyBoyModel, basically i am rendering each part of 20thCB separately,
                        but they all use the same model, so i just made a common class for all the operations that happen in the model, you probably won't need this **/
                        ModStrayModels.CENTURY_BOY.renderPart(entity, heyfull, entity);


                        /** now this is the interesting part, i use the following clientUtil to render the part where i want,
                        set all the offsets and everything, and then use the model to render it properly**/
                        /// body
                        ClientUtil.pushPoseAndCooperate(poseStack, 26);

                        boolean isHurt = entity.hurtTime > 0;
                        float r = isHurt ? 1.0F : 1.0F;
                        float g = isHurt ? 0.4F : 1.0F;
                        float b = isHurt ? 0.4F : 1.0F;
                        if (entity.isBaby()) {
                            poseStack.scale(0.6F, 0.6F, 0.6F);
                            poseStack.translate(0.3, 1, -0.3);
                        }

                        /// ps. translate means moving it
                        getParentModel().body.translateAndRotate(poseStack);



                        /// for unlatch...
                        if (user.roundabout$getIdlePos() == 1) {
                            poseStack.translate(0.0, -0.2, 0.3);
                            ModStrayModels.CENTURY_BOY.renderAll(entity, partialTicks, poseStack, bufferSource,
                                    packedLight, r, g, b, heyfull, skin);
                            ClientUtil.popPoseAndCooperate(poseStack, 26);
                        } else {
                            poseStack.scale(1F,1F,1.1F);
                            poseStack.translate(0, -0.1, 0.54);
                            ModStrayModels.CENTURY_BOY.renderBody(entity, partialTicks, poseStack, bufferSource,
                                    packedLight, r, g, b, heyfull, skin);

                            ClientUtil.popPoseAndCooperate(poseStack, 26);
                            /// head
                            ClientUtil.pushPoseAndCooperate(poseStack, 26);
                            if (entity.isBaby()) {
                                poseStack.scale(0.6F, 0.6F, 0.6F);
                                poseStack.translate(0.3, 1, -0.3);
                            }

                            getParentModel().head.translateAndRotate(poseStack);

                            if (entity instanceof  IPlayerEntity IPL && IPL.roundabout$getMaskSlot().getItem() instanceof
                                    MaskItem MI) {
                                poseStack.scale(1F,1F,1.1F);
                            }

                            poseStack.translate(0, -0.1, 0.52);

                            ModStrayModels.CENTURY_BOY.renderHead(entity, partialTicks, poseStack, bufferSource,
                                    packedLight, r, g, b, heyfull, skin);

                            ClientUtil.popPoseAndCooperate(poseStack, 26);
                            /// left arm
                            ClientUtil.pushPoseAndCooperate(poseStack, 26);
                            if (entity.isBaby()) {
                                poseStack.scale(0.6F, 0.6F, 0.6F);
                                poseStack.translate(0.3, 1, -0.3);
                            }


                            getParentModel().leftArm.translateAndRotate(poseStack);


                            poseStack.translate(-.3, -0.2, 0.52);

                            if (getParentModel() instanceof PlayerModel<?> PM && ((IPlayerModel) PM).roundabout$getSlim()) {
                                ModStrayModels.CENTURY_BOY.renderLeftArmSlim(entity, partialTicks, poseStack, bufferSource,
                                        packedLight, r, g, b, heyfull, skin);
                            } else {
                                ModStrayModels.CENTURY_BOY.renderLeftArm(entity, partialTicks, poseStack, bufferSource,
                                        packedLight, r, g, b, heyfull, skin);
                            }

                            ClientUtil.popPoseAndCooperate(poseStack, 26);
                            /// right arm

                            ClientUtil.pushPoseAndCooperate(poseStack, 26);
                            if (entity.isBaby()) {
                                poseStack.scale(0.6F, 0.6F, 0.6F);
                                poseStack.translate(0.3, 1, -0.3);
                            }


                            getParentModel().rightArm.translateAndRotate(poseStack);

                            poseStack.translate(.3, -0.2, 0.52);

                            if (getParentModel() instanceof PlayerModel<?> PM && ((IPlayerModel) PM).roundabout$getSlim()) {
                            ModStrayModels.CENTURY_BOY.renderRightArmSlim(entity, partialTicks, poseStack, bufferSource,
                                    packedLight, r, g, b, heyfull, skin);
                            } else {
                                ModStrayModels.CENTURY_BOY.renderRightArm(entity, partialTicks, poseStack, bufferSource,
                                        packedLight, r, g, b, heyfull, skin);
                            }

                            ClientUtil.popPoseAndCooperate(poseStack, 26);

                            /// breast
                            if (entity instanceof  IPlayerEntity IPL && IPL.roundabout$getMaskSlot().getItem() instanceof
                                    MaskItem MI) {
                                if (MI.visageData.generateVisageData(entity).rendersBreast() || MI.visageData.generateVisageData(entity).rendersPlayerBreastPart() ||
                                        (MI.visageData.generateVisageData(entity).rendersSmallBreast() && user.roundabout$getIdlePos() != 0)){
                                    ClientUtil.pushPoseAndCooperate(poseStack, 26);
                                if (entity.isBaby()) {
                                    poseStack.scale(0.6F, 0.6F, 0.6F);
                                    poseStack.translate(0.3, 1, -0.3);
                                }
                                    poseStack.translate(0, 0.1, -0.01);
                                if (MI.visageData.generateVisageData(entity).rendersBreast()){
                                    poseStack.translate(0, -0.05, -0.05);
                                }
                                getParentModel().body.translateAndRotate(poseStack);


                                ModStrayModels.CENTURY_BOY.renderBreast(entity, partialTicks, poseStack, bufferSource,
                                        packedLight, r, g, b, heyfull, skin);

                                ClientUtil.popPoseAndCooperate(poseStack, 26);
                            }
                                }
                        }

                    }
                }
            }
        }
    }
}