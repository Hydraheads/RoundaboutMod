package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;


public class AnubisLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private final EntityRenderDispatcher dispatcher;
    public AnubisLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }


    public static HumanoidArm shouldRender(LivingEntity entity) {
        StandUser user = ((StandUser)entity);
        if (entity.isUsingItem() && !entity.getUseItem().is(ModItems.ANUBIS_ITEM)) {return null;}
        if (entity.getMainHandItem().getItem() instanceof AnubisItem
                || entity.getOffhandItem().getItem() instanceof AnubisItem
                || user.roundabout$isPossessed()
                || (user.roundabout$getStandPowers() instanceof PowersAnubis && PowerTypes.hasStandActive(entity))
                || user.roundabout$getAnubisVanishTicks() > 0 ) {
            return entity.getMainArm();
        }
        return null;
    }

    public static boolean isSheathed(LivingEntity entity) {
        StandUser user = ((StandUser)entity);
        if (entity.getMainHandItem().getItem() instanceof AnubisItem) {
            if (!user.roundabout$isPossessed()) {
                return (user.roundabout$getStandPowers() instanceof PowersAnubis && !PowerTypes.hasStandActive(entity))
                        || !(user.roundabout$getStandPowers() instanceof PowersAnubis);
            }
        }
        return false;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (((IEntityAndData) entity).roundabout$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung())
            return;
        if (!entity.isInvisible()) {
            if (entity != null && AnubisLayer.shouldRender(entity) != null) {

                ClientUtil.pushPoseAndCooperate(poseStack,25);

                StandUser SU = (StandUser) entity;

                if (AnubisLayer.shouldRender(entity) == HumanoidArm.RIGHT ) {
                    getParentModel().rightArm.translateAndRotate(poseStack);
                    poseStack.translate(-0.075,0.9,0);
                    if (!isSheathed(entity)) {poseStack.translate(0,0.2,0);}
                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1,0,0,isSheathed(entity) ? 90 : -90),0,0,0);
                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0, -90),0,0,0);
                    if (isSheathed(entity)) {poseStack.translate(1,-0.6,-0.025);}
                } else {
                    getParentModel().leftArm.translateAndRotate(poseStack);
                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,isSheathed(entity) ? -90 : 90),0,0,0);
                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,-90),0,0,0);
                    if (isSheathed(entity)) {poseStack.translate(0.1,-0.7,-0.025);}
                }
                poseStack.translate(-0.25,0.5,0.05);




                renderAnubis(poseStack, bufferSource, packedLight, entity, partialTicks);
                ClientUtil.popPoseAndCooperate(poseStack,25);

                ClientUtil.pushPoseAndCooperate(poseStack,47);
                if (SU.roundabout$isPossessed()) {
                    renderHumanoidAnubis(poseStack,bufferSource,packedLight,entity,partialTicks,0.6F);
                } else if (SU.roundabout$getStandPowers()  instanceof PowersAnubis AP && PowerTypes.hasStandActive(entity) && SU.roundabout$getIdlePos() == 1) {
                    renderHumanoidAnubis(poseStack,bufferSource,packedLight,entity,partialTicks,SU.roundabout$getAnubisVanishTicks()/10F*0.7F );
                }
                ClientUtil.popPoseAndCooperate(poseStack,47);

            }
        }
    }

    public static void renderOutOfContext(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float partialTicks, ModelPart handarm) {
        if (!entity.isInvisible()) {
            if (entity != null && AnubisLayer.shouldRender(entity) != null) {
                StandUser user = ((StandUser) entity);


                ClientUtil.pushPoseAndCooperate(poseStack, 48);

                handarm.translateAndRotate(poseStack);

                poseStack.translate(0, 0.9, 0);
                if (AnubisLayer.shouldRender(entity) == HumanoidArm.RIGHT) {
                    poseStack.translate(0,0,0);
                    if (isSheathed(entity)) {
                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0F, 0F, 1F, 200), 0, 0, 0);
                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 0, 30), 0, 0, 0);
                        poseStack.translate(0.5,0.35,0);
                    } else {
                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1,  20), 0, 0, 0);
                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 0, -20), 0, 0, 0);
                    }

               //     if (isSheathed(entity)) {poseStack.translate(0,0.5,0.8);}
                } else {
                    poseStack.translate(0.2,0,0);
                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 1, 0, 180), 0, 0, 0);
                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0, 0, 1, 45), 0, 0, 0);
                    poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(1, 0, 0, isSheathed(entity) ? 225 :45), 0, 0, 0);
                    if (isSheathed(entity)) {poseStack.translate(0,0.1,0);} else {poseStack.translate(0.3, -0.4, -0.05);}
                }

                renderAnubis(poseStack,bufferSource,packedLight,entity,partialTicks);
                ClientUtil.popPoseAndCooperate(poseStack, 48);

            }
        }
    }

    public static void renderAnubis(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float partialTicks) {

        StandUser user = ((StandUser)entity );
        boolean hasHeyYaOut = (PowerTypes.hasStandActive(entity) && user.roundabout$getStandPowers() instanceof PowersAnubis);
        int heyTicks = user.roundabout$getAnubisVanishTicks();
        float heyFull = 0;
        float fixedPartial = partialTicks - (int) partialTicks;
        if (((TimeStop)entity.level()).CanTimeStopEntity(entity)){
            fixedPartial = 0;
        }
        if (hasHeyYaOut){
            heyFull = heyTicks+fixedPartial;
            heyFull = Math.min(heyFull/10,1f);
        } else {
            heyFull = heyTicks-fixedPartial;
            heyFull = Math.max(heyFull/10,0);
        }


        byte skin = 0;
        if ( user.roundabout$isPossessed() ) {
            ModStrayModels.ANUBIS.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    1, 1, 1, 1F, (byte) 1);
        } else if ( (user.roundabout$getStandPowers() instanceof PowersAnubis PA && PowerTypes.hasStandActive(entity) ) || (heyTicks != 0 && !entity.getMainHandItem().is(ModItems.ANUBIS_ITEM)   ) ) {
            ModStrayModels.ANUBIS.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    1, 1, 1, heyFull, user.roundabout$getStandSkin() );
        } else if (entity.getMainHandItem().getItem() instanceof AnubisItem && !user.roundabout$getEffectiveCombatMode()) {
            ModStrayModels.ANUBIS.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    1, 1, 1, 1F, (byte) 0);
        }


    }

    public void renderHumanoidAnubis(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float partialTicks, float alpha) {
        if (true) {return;}
        /// I'm going to come back to this goober later

        this.getParentModel().body.translateAndRotate(poseStack);
        poseStack.translate(-0.55,-0.1,0.5);

        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,-15),0,0,0);

        ModStrayModels.ANUBIS_HUMAN.render(entity,partialTicks,poseStack,bufferSource,packedLight,1.0F,1.0F,1.0F,alpha  );

    }


}

