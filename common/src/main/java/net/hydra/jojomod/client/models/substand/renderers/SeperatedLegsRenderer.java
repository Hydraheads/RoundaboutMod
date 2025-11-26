package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GreenDayModel;
import net.hydra.jojomod.client.models.substand.LifeTrackerModel;
import net.hydra.jojomod.client.models.substand.SeperatedLegsModel;
import net.hydra.jojomod.entity.stand.GreenDayEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.substand.SeperatedLegsEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SeperatedLegsRenderer extends EntityRenderer<SeperatedLegsEntity> {
    private static final ResourceLocation PART_FIVE_GREEN_DAY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/part_four_green_day.png");
    private final SeperatedLegsModel<SeperatedLegsEntity> model;

    public SeperatedLegsRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SeperatedLegsModel<>(context.bakeLayer(ModEntityRendererClient.SEPERATED_LEGS_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(SeperatedLegsEntity seperatedLegsEntity) {
        return PART_FIVE_GREEN_DAY;
    }

    @Override
    public void render(SeperatedLegsEntity p_114528_, float p_114529_, float p_114530_, PoseStack p_114531_, MultiBufferSource p_114532_, int p_114533_) {
        float f = 1;

        p_114531_.pushPose();
        p_114531_.mulPose(Axis.YP.rotationDegrees(0));
        float f2 = 1F;
        p_114531_.translate(0.0D, -0.4D, 0.0D);
        p_114531_.scale(1F,1F,1F);
        //p_114531_.scale(0.5F, 0.5F, 0.5F);
        this.model.setupAnim(p_114528_, f, 0.0F, 0.0F, p_114528_.getYRot(), p_114528_.getXRot());
        VertexConsumer vertexconsumer = p_114532_.getBuffer(RenderType.entityCutout(getTextureLocation(p_114528_)));
        this.model.renderToBuffer(p_114531_, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.6F);
        p_114531_.popPose();
    }

}
