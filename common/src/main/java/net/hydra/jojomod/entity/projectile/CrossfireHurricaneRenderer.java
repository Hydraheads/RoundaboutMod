package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class CrossfireHurricaneRenderer extends EntityRenderer<CrossfireHurricaneEntity> {

    private final CrossfireHurricaneModel model;
    private final CrossfireFirestormModel model2;

    public CrossfireHurricaneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new CrossfireHurricaneModel($$0.bakeLayer(ModEntityRendererClient.CROSSFIRE_LAYER));
        this.model2 = new CrossfireFirestormModel($$0.bakeLayer(ModEntityRendererClient.CROSSFIRE_FIRESTORM_LAYER));
    }



    public void render(CrossfireHurricaneEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            if (((TimeStop)$$0.level()).inTimeStopRange($$0)){
                $$2 = 0;
            }
            $$3.pushPose();
            float rsize = $$0.getMaxSize();
            if ($$0.getRenderSize() < rsize){
                if ($$0.getCrossNumber() == 6){
                    $$0.setRenderSize((Math.min((float) ($$0.getLastRenderSize() + ($$2 * (float)$$0.getAccrualRate())),rsize))*0.6F);
                } else {
                    $$0.setRenderSize(Math.min((float) ($$0.getLastRenderSize() + ($$2 * (float)$$0.getAccrualRate())),rsize));
                }
            }

            //$$3.mulPose(Axis.ZP.rotationDegrees(-180));
            //$$3.mulPose(Axis.YP.rotationDegrees((float) Mth.rotLerp($$2, (float) $$0.renderRotation, (float) $$0.lastRenderRotation)));
            $$3.mulPose(Axis.YP.rotationDegrees(-1*(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot()))));
            float fsize = $$0.getRenderSize() * 0.035f;
            $$3.scale(1.1f+fsize, 1.1f+fsize, 1.1f+fsize);
            VertexConsumer $$6 = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation($$0)));
            if ($$0.getCrossNumber() != 7) {
                if ($$0.getCrossNumber() == 6){
                    this.model2.renderToBuffer($$3, $$6, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.53f);
                } else {
                    this.model.renderToBuffer($$3, $$6, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.57f);
                }
            }
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, 15728880);
        }
    }



    public static final ResourceLocation CROSSFIRE_HURRICANE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_2.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_3.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_BLUE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_blue.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_BLUE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_2_blue.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_BLUE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_3_blue.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_PURPLE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_purple.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_PURPLE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_2_purple.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_PURPLE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_3_purple.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_GREEN_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_green.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_GREEN_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_2_green.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_GREEN_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_3_green.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_DREAD_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_dread.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_DREAD_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_2_dread.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_DREAD_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_3_dread.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_CREAM_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_cream.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_CREAM_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_2_cream.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_CREAM_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_3_cream.png");

    public static final ResourceLocation CROSSFIRE_HURRICANE_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_1.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_2.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_3.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_BLUE_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_1_blue.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_BLUE_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_2_blue.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_BLUE_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_3_blue.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_PURPLE_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_1_purple.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_PURPLE_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_2_purple.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_PURPLE_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_3_purple.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_GREEN_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_1_green.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_GREEN_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_2_green.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_GREEN_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_3_green.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_DREAD_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_1_dread.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_DREAD_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_2_dread.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_DREAD_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_3_dread.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_CREAM_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_1_cream.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_CREAM_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_2_cream.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_CREAM_TEXTURE_B = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/firestorm_3_cream.png");



    public ResourceLocation getThird(CrossfireHurricaneEntity var1){
        LivingEntity user = var1.getUser();
        int crossno = var1.getCrossNumber();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_3_BLUE_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_3_BLUE_TEXTURE;
                }
            } else if (sft == StandFireType.PURPLE.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_3_PURPLE_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_3_PURPLE_TEXTURE;
                }
            } else if (sft == StandFireType.GREEN.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_3_GREEN_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_3_GREEN_TEXTURE;
                }
            } else if (sft == StandFireType.DREAD.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_3_DREAD_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_3_DREAD_TEXTURE;
                }
            } else if (sft == StandFireType.CREAM.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_3_CREAM_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_3_CREAM_TEXTURE;
                }
            }
        }
        if (crossno == 6){
            return CROSSFIRE_HURRICANE_3_TEXTURE_B;
        }
        return CROSSFIRE_HURRICANE_3_TEXTURE;
    }
    public ResourceLocation getFirst(CrossfireHurricaneEntity var1){
        LivingEntity user = var1.getUser();
        int crossno = var1.getCrossNumber();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_BLUE_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_BLUE_TEXTURE;
                }
            } else if (sft == StandFireType.PURPLE.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_PURPLE_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_PURPLE_TEXTURE;
                }
            } else if (sft == StandFireType.GREEN.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_GREEN_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_GREEN_TEXTURE;
                }
            } else if (sft == StandFireType.DREAD.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_DREAD_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_DREAD_TEXTURE;
                }
            } else if (sft == StandFireType.CREAM.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_CREAM_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_CREAM_TEXTURE;
                }
            }
        }
        if (crossno == 6){
            return CROSSFIRE_HURRICANE_TEXTURE_B;
        }
        return CROSSFIRE_HURRICANE_TEXTURE;
    }
    public ResourceLocation getSecond(CrossfireHurricaneEntity var1){
        LivingEntity user = var1.getUser();
        int crossno = var1.getCrossNumber();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_2_BLUE_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_2_BLUE_TEXTURE;
                }
            } else if (sft == StandFireType.PURPLE.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_2_PURPLE_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_2_PURPLE_TEXTURE;
                }
            } else if (sft == StandFireType.GREEN.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_2_GREEN_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_2_GREEN_TEXTURE;
                }
            } else if (sft == StandFireType.DREAD.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_2_DREAD_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_2_DREAD_TEXTURE;
                }
            } else if (sft == StandFireType.CREAM.id){
                if (crossno == 6){
                    return CROSSFIRE_HURRICANE_2_CREAM_TEXTURE_B;
                } else {
                    return CROSSFIRE_HURRICANE_2_CREAM_TEXTURE;
                }
            }
        }
        if (crossno == 6){
            return CROSSFIRE_HURRICANE_2_TEXTURE_B;
        }
        return CROSSFIRE_HURRICANE_2_TEXTURE;
    }
    @Override
    public ResourceLocation getTextureLocation(CrossfireHurricaneEntity var1) {
        int tc = var1.tickCount % 9;
        if (tc > 5) {
            return getThird(var1);
        }
        if (tc > 2) {
            return getSecond(var1);
        }
        return getFirst(var1);
    }

}