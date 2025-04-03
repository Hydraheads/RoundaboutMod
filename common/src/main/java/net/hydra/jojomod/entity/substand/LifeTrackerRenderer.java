package net.hydra.jojomod.entity.substand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;

public class LifeTrackerRenderer extends EntityRenderer<LifeTrackerEntity> {
    private static final ResourceLocation MAIN_TRACKER_1 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/life_detector.png");
    private static final ResourceLocation MAIN_TRACKER_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/life_detector_2.png");
    private static final ResourceLocation MAIN_TRACKER_3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/life_detector/life_detector_3.png");
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
            } else if (sft == StandFireType.PURPLE.id){
            } else if (sft == StandFireType.GREEN.id){
            } else if (sft == StandFireType.DREAD.id){
            }
        }
        return MAIN_TRACKER_3;
    }
    public ResourceLocation getFirst(LifeTrackerEntity var1){
        LivingEntity user = var1.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
            } else if (sft == StandFireType.PURPLE.id){
            } else if (sft == StandFireType.GREEN.id){
            } else if (sft == StandFireType.DREAD.id){
            }
        }
        return MAIN_TRACKER_1;
    }
    public ResourceLocation getSecond(LifeTrackerEntity var1){
        LivingEntity user = var1.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
            } else if (sft == StandFireType.PURPLE.id){
            } else if (sft == StandFireType.GREEN.id){
            } else if (sft == StandFireType.DREAD.id){
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
