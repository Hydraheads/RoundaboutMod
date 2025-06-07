package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.client.models.substand.LifeTrackerModel;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class LifeTrackerRenderer extends EntityRenderer<LifeTrackerEntity> {
    private static final ResourceLocation MAIN_TRACKER_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/life_detector.png");
    private static final ResourceLocation MAIN_TRACKER_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/life_detector_2.png");
    private static final ResourceLocation MAIN_TRACKER_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/life_detector_3.png");
    private static final ResourceLocation BLUE_TRACKER_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/blue_detector.png");
    private static final ResourceLocation BLUE_TRACKER_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/blue_detector_2.png");
    private static final ResourceLocation BLUE_TRACKER_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/blue_detector_3.png");
    private static final ResourceLocation PURPLE_TRACKER_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/purple_detector.png");
    private static final ResourceLocation PURPLE_TRACKER_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/purple_detector_2.png");
    private static final ResourceLocation PURPLE_TRACKER_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/purple_detector_3.png");
    private static final ResourceLocation GREEN_TRACKER_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/green_detector.png");
    private static final ResourceLocation GREEN_TRACKER_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/green_detector_2.png");
    private static final ResourceLocation GREEN_TRACKER_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/green_detector_3.png");
    private static final ResourceLocation DREAD_TRACKER_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/dread_detector.png");
    private static final ResourceLocation DREAD_TRACKER_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/dread_detector_2.png");
    private static final ResourceLocation DREAD_TRACKER_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/dread_detector_3.png");
    private static final ResourceLocation CREAM_TRACKER_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/cream_detector.png");
    private static final ResourceLocation CREAM_TRACKER_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/cream_detector_2.png");
    private static final ResourceLocation CREAM_TRACKER_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/cream_detector_3.png");
    private static final ResourceLocation MANGA_TRACKER_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/manga_detector.png");
    private static final ResourceLocation MANGA_TRACKER_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/manga_detector_2.png");
    private static final ResourceLocation MANGA_TRACKER_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/manga_detector_3.png");
    private final LifeTrackerModel<LifeTrackerEntity> model;

    public LifeTrackerRenderer(EntityRendererProvider.Context p_174100_) {
        super(p_174100_);
        this.model = new LifeTrackerModel<>(p_174100_.bakeLayer(ModEntityRendererClient.LIFE_DETECTOR));
    }

    public void render(LifeTrackerEntity p_114528_, float p_114529_, float p_114530_, PoseStack p_114531_, MultiBufferSource p_114532_, int p_114533_) {
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            float f = 1;

            p_114531_.pushPose();
            p_114531_.mulPose(Axis.YP.rotationDegrees(0));
            float f2 = 0.03125F;
            p_114531_.translate(0.0D, -0.4, 0.0D);
            //p_114531_.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(p_114528_, f, 0.0F, 0.0F, p_114528_.getYRot(), p_114528_.getXRot());
            VertexConsumer vertexconsumer = p_114532_.getBuffer(this.model.renderType(getTextureLocation(p_114528_)));
            this.model.renderToBuffer(p_114531_, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            p_114531_.popPose();
        }
    }


    public ResourceLocation getThird(LifeTrackerEntity var1){
        LivingEntity user = var1.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return BLUE_TRACKER_3;
            } else if (sft == StandFireType.PURPLE.id){
                return PURPLE_TRACKER_3;
            } else if (sft == StandFireType.GREEN.id){
                return GREEN_TRACKER_3;
            } else if (sft == StandFireType.DREAD.id){
                return DREAD_TRACKER_3;
            } else if (sft == StandFireType.CREAM.id){
                return CREAM_TRACKER_3;
            } else if (((StandUser) user).roundabout$getStandSkin() == MagiciansRedEntity.MANGA_SKIN){
                return MANGA_TRACKER_3;
            }
        }
        return MAIN_TRACKER_3;
    }
    public ResourceLocation getFirst(LifeTrackerEntity var1){
        LivingEntity user = var1.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return BLUE_TRACKER_1;
            } else if (sft == StandFireType.PURPLE.id){
                return PURPLE_TRACKER_1;
            } else if (sft == StandFireType.GREEN.id){
                return GREEN_TRACKER_1;
            } else if (sft == StandFireType.DREAD.id){
                return DREAD_TRACKER_1;
            } else if (sft == StandFireType.CREAM.id){
                return CREAM_TRACKER_1;
            } else if (((StandUser) user).roundabout$getStandSkin() == MagiciansRedEntity.MANGA_SKIN){
                return MANGA_TRACKER_1;
            }
        }
        return MAIN_TRACKER_1;
    }
    public ResourceLocation getSecond(LifeTrackerEntity var1){
        LivingEntity user = var1.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return BLUE_TRACKER_2;
            } else if (sft == StandFireType.PURPLE.id){
                return PURPLE_TRACKER_2;
            } else if (sft == StandFireType.GREEN.id){
                return GREEN_TRACKER_2;
            } else if (sft == StandFireType.DREAD.id){
                return DREAD_TRACKER_2;
            } else if (sft == StandFireType.CREAM.id){
                return CREAM_TRACKER_2;
            } else if (((StandUser) user).roundabout$getStandSkin() == MagiciansRedEntity.MANGA_SKIN){
                return MANGA_TRACKER_2;
            }
        }
        return MAIN_TRACKER_2;
    }
    @Override
    public ResourceLocation getTextureLocation(LifeTrackerEntity var1) {
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
