package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class CenturyBoyLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private final EntityRenderDispatcher dispatcher;
    public CenturyBoyLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())){
            if (entity != null){
                if (((IEntityAndData)entity).roundabout$getTrueInvisibility() > - 1 && !ClientUtil.checkIfClientCanSeeInvisAchtung())
                    return;


                StandUser user = ((StandUser) entity);
                boolean hasCB = (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy);
                boolean standOut = (PowerTypes.hasStandActive(entity) && hasCB);
                if (!entity.isInvisible()){
                    int CBticks = user.roundabout$getCBVanishTicks();

                    if (CBticks > 0 || standOut){
                        byte skin = user.roundabout$getStandSkin();
                        if (user.roundabout$getLastStandSkin() != skin){
                            user.roundabout$setLastStandSkin(skin);
                            CBticks = 0;
                            user.roundabout$setCBVanishTicks(0);
                        }

                        float heyfull = 0;
                        float fixedPartial = partialTicks - (int) partialTicks;

                        if (((TimeStop) entity.level()).CanTimeStopEntity(entity)){
                            fixedPartial = 0;
                        }

                        if (standOut){
                            heyfull = CBticks + fixedPartial;
                            heyfull = Math.min(heyfull / 10, 1f);
                        } else {
                            heyfull = CBticks - fixedPartial;
                            heyfull = Math.max(heyfull / 10, 0);
                        }

                        /// body
                        ClientUtil.pushPoseAndCooperate(poseStack, 26);

                        boolean isHurt = entity.hurtTime > 0;
                        float r = isHurt ? 1.0F : 1.0F;
                        float g = isHurt ? 0.4F : 1.0F;
                        float b = isHurt ? 0.4F : 1.0F;
                        if (entity.isBaby()){
                            poseStack.scale(0.6F, 0.6F, 0.6F);
                            poseStack.translate(0.3, 1, -0.3);
                        }

                        getParentModel().body.translateAndRotate(poseStack);

                        poseStack.translate(0.0, 0.75, 0.0);

                        ModStrayModels.CENTURY_BOY.renderBody(entity,partialTicks,poseStack,bufferSource,
                                packedLight, r, g, b, heyfull, skin);

                        ClientUtil.popPoseAndCooperate(poseStack, 26);
                        /// head
                        ClientUtil.pushPoseAndCooperate(poseStack, 26);
                        if (entity.isBaby()){
                            poseStack.scale(0.6F, 0.6F, 0.6F);
                            poseStack.translate(0.3, 1, -0.3);
                        }

                        getParentModel().head.translateAndRotate(poseStack);

                        poseStack.translate(0.0, 0.75, 0.0);

                        ModStrayModels.CENTURY_BOY.renderHead(entity,partialTicks,poseStack,bufferSource,
                                packedLight, r, g, b, heyfull, skin);

                        ClientUtil.popPoseAndCooperate(poseStack, 26);
                        /// left arm
                        ClientUtil.pushPoseAndCooperate(poseStack, 26);
                        if (entity.isBaby()){
                            poseStack.scale(0.6F, 0.6F, 0.6F);
                            poseStack.translate(0.3, 1, -0.3);
                        }

                        getParentModel().leftArm.translateAndRotate(poseStack);

                        poseStack.translate(-0.3, 0.63, 0.0);

                        ModStrayModels.CENTURY_BOY.renderLeftArm(entity,partialTicks,poseStack,bufferSource,
                                packedLight, r, g, b, heyfull, skin);

                        ClientUtil.popPoseAndCooperate(poseStack, 26);
                        /// left arm
                        ClientUtil.pushPoseAndCooperate(poseStack, 26);
                        if (entity.isBaby()){
                            poseStack.scale(0.6F, 0.6F, 0.6F);
                            poseStack.translate(0.3, 1, -0.3);
                        }

                        getParentModel().rightArm.translateAndRotate(poseStack);

                        poseStack.translate(0.3, 0.63, 0.0);

                        ModStrayModels.CENTURY_BOY.renderRightArm(entity,partialTicks,poseStack,bufferSource,
                                packedLight, r, g, b, heyfull, skin);

                        ClientUtil.popPoseAndCooperate(poseStack, 26);
                    }
                }
            }
        }
    }
}
