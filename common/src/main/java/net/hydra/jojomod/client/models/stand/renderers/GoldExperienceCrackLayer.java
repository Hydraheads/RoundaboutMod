package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.GoldExperienceModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GoldExperienceCrackLayer extends RenderLayer<GoldExperienceEntity, StandModel<GoldExperienceEntity>> {

    private static final ResourceLocation CRACK_1 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/transformation/ge_crack_1.png");
    private static final ResourceLocation CRACK_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/transformation/ge_crack_2.png");
    private static final ResourceLocation CRACK_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/transformation/ge_crack_3.png");


    public GoldExperienceCrackLayer(RenderLayerParent<GoldExperienceEntity, StandModel<GoldExperienceEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, GoldExperienceEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.thisIsRequiemAnimation.isStarted() && entity.getUser() != null && ((StandUser)entity.getUser()).roundabout$getStandPowers() instanceof PowersGoldExperience GE) {

            ResourceLocation crack = getCrackByTime(GE.attackTimeDuring / 20.0f);
            if (crack != null) {
                GoldExperienceModel<GoldExperienceEntity> model = (GoldExperienceModel<GoldExperienceEntity>) this.getParentModel();

                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(crack));

                model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, entity.getFadePercent()*0.01F);
            }
        }
    }

    private ResourceLocation getCrackByTime(float time) {
        if (time >= 8.54f) {
            return CRACK_3;
        }
        if (time >= 4.71f) {
            return CRACK_2;
        }
        if (time >= 0.96f) {
            return CRACK_1;
        }

        return null;
    }
}