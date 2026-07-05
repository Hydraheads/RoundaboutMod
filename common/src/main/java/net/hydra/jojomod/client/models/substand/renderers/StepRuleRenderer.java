package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.substand.StepRuleModel;
import net.hydra.jojomod.entity.StepRuleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class StepRuleRenderer extends EntityRenderer<StepRuleEntity> {
    private static final ResourceLocation STEP_RULE =
            new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/no_walk/no_walk.png");
    private static final ResourceLocation STEP_RULE_ACTIVE =
            new ResourceLocation(Roundabout.MOD_ID,"textures/stand/california_king_bed/no_walk/no_walk_active.png");

    protected StepRuleModel model;

    public StepRuleRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new StepRuleModel<>(context.bakeLayer(ModEntityRendererClient.STEP_RULE));
    }

    @Override
    public ResourceLocation getTextureLocation(StepRuleEntity stepRuleEntity) {
        if (stepRuleEntity.getTurnedBad())
            return STEP_RULE_ACTIVE;
        return STEP_RULE;
    }

    public void render(StepRuleEntity stepRuleEntity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        Player ClientPlayer = Minecraft.getInstance().player;
        //matrixStack.scale(0.95f, 0.95f, 0.95f);

        if (ClientUtil.canSeeStands(ClientPlayer)) {
            matrixStack.pushPose();

            VertexConsumer vertex = vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(getTextureLocation(stepRuleEntity)));

            matrixStack.mulPose(Axis.ZP.rotationDegrees(180f));
            matrixStack.translate(0,-0.5,0);
            if (stepRuleEntity.getTurnedBad()){
                matrixStack.scale(1.01f,1.01f,1.01f);
            }

            model.renderToBuffer(matrixStack, vertex, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                    Math.min((((float) stepRuleEntity.renderFadeIn) / 20) + (partialTicks * 0.05F),1f));
            matrixStack.popPose();
             super.render(stepRuleEntity, 0, partialTicks, matrixStack, vertexConsumerProvider, i);
        }
    }

    protected int getBlockLightLevel(StepRuleRenderer stepRuleEntity, BlockPos pos) {
        return 15;
    }

    protected int getSkyLightLevel(StepRuleRenderer stepRuleEntity, BlockPos pos) {
        return 15;
    }
}