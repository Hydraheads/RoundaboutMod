package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.hydra.jojomod.stand.powers.PowersCatchTheRainbow;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public class CatchTheRainbowLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public CatchTheRainbowLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            if (((IEntityAndData)entity).roundabout$getTrueInvisibility() > - 1 && !ClientUtil.checkIfClientCanSeeInvisAchtung())
                return;
            LivingEntity livent = entity;
            if (!entity.isInvisible()) {
                if (entity != null) {
                    StandUser user = ((StandUser) livent);
                    boolean hasCatchTheRainbowOut = (PowerTypes.hasStandActive(livent) && user.roundabout$getStandPowers() instanceof PowersCatchTheRainbow);

                    //int heyTicks = user.roundabout$getCatchTheRainbowVanishTicks();
                    int rainbowTicks = MainUtil.getWornStandVanishticksSafe(user, PowersCatchTheRainbow.class);/*0;

                    if (user.roundabout$getStandPowers() instanceof PowersCatchTheRainbow) {
                        rainbowTicks = user.roundabout$getCurrentStandVanishTicks();
                    }else if (user.roundabout$getPreviousStandPowers() instanceof PowersCatchTheRainbow) {
                        rainbowTicks = user.roundabout$getPreviousStandVanishTicks();
                    }*/

                    if (rainbowTicks > 0 || hasCatchTheRainbowOut) {
                        byte skin = user.roundabout$getStandSkin();
                        if (user.roundabout$getStandPowers() instanceof PowersCatchTheRainbow) {
                            if (user.roundabout$getLastStandSkin() != skin) {
                                user.roundabout$setLastStandSkin(skin);
                                rainbowTicks = 0;

                                user.roundabout$setCurrentStandVanishTicks(0);
                            }
                        }else {
                            skin = user.roundabout$getPreviousSkin();
                        }

                        float heyFull = 0;
                        float fixedPartial = partialTicks - (int) partialTicks;
                        if (((TimeStop)entity.level()).CanTimeStopEntity(entity)){
                            fixedPartial = 0;
                        }
                        if (hasCatchTheRainbowOut){
                            heyFull = rainbowTicks+fixedPartial;
                            heyFull = Math.min(heyFull/10,1f);
                        } else {
                            heyFull = rainbowTicks-fixedPartial;
                            heyFull = Math.max(heyFull/10,0);
                        }
                        ClientUtil.pushPoseAndCooperate(poseStack,24);

                        // Translate to the body
                            getParentModel().head.translateAndRotate(poseStack);
                            // Apply additional transformations
                            poseStack.translate(-0.0325F, -1.245, -0.5F); //1 1
                            // The first value goes to the right (negative) and left (positive)
                            // The second value is correlated with up (negative) and down (positive)
                        // the third is further and closer, positive to head towards the mob's facing direction negative for away
                        // Render your model here
                        poseStack.scale(1.026F, 1.026F, 1.026F);
                        if (entity.isBaby()){
                            poseStack.scale(0.6F, 0.6F, 0.6F);
                            poseStack.translate(0.3, 3, -0.3);
                            }
                        if (skin != 10) {
                            ModStrayModels.CATCH_THE_RAINBOW.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                                    1, 1, 1, heyFull, skin);
                        }else {
                            ModStrayModels.CATCH_THE_RAINBOW_UMVUTHI.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                                    1, 1, 1, heyFull, skin);
                        }
                        ClientUtil.popPoseAndCooperate(poseStack,24);
                    }
                }
            }
        }
    }
}