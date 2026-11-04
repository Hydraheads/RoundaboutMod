package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.GoldExperienceModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GoldExperienceLifePunchLayer extends RenderLayer<GoldExperienceEntity, StandModel<GoldExperienceEntity>> {

    private static final ResourceLocation CORE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/life_punch_core.png");
    private static final ResourceLocation SOFT_AURA_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/life_punch_aura.png");

    public GoldExperienceLifePunchLayer(RenderLayerParent<GoldExperienceEntity, StandModel<GoldExperienceEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, GoldExperienceEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.lifePunch.isStarted()) {

            GoldExperienceModel<GoldExperienceEntity> model = (GoldExperienceModel<GoldExperienceEntity>) this.getParentModel();

            // hide left arm so it only renders on the right hand
            model.left_arm.visible = false;

            VertexConsumer coreConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(CORE_TEXTURE));
            model.renderToBuffer(
                    poseStack,
                    coreConsumer,
                    15728880,
                    OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 0.55F
            );

            VertexConsumer auraConsumer = bufferSource.getBuffer(RenderType.energySwirl(SOFT_AURA_TEXTURE, 0.0F, 0.0F));
            model.renderToBuffer(
                    poseStack,
                    auraConsumer,
                    15728880,
                    OverlayTexture.NO_OVERLAY,
                    0.4F, 0.4F, 0.2F, 0.3F
            );

            model.left_arm.visible = true;
        }
    }
}