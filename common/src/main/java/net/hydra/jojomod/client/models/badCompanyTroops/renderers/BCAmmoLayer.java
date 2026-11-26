package net.hydra.jojomod.client.models.badCompanyTroops.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.entity.badCompanyTroops.BCTroop;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBadCompany;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BCAmmoLayer<T extends BCTroop, M extends StandModel<T>> extends RenderLayer<T, M> {

    public BCAmmoLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferSource, int i, T mobEntity, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        LivingEntity User = mobEntity.getUser();

        if (!ClientUtil.isPlayer(User) || !mobEntity.isRecharging()) {return; }

        if (((((IEntityAndData)User).roundabout$getTrueInvisibility() > -1) ||
                ((IEntityAndData)mobEntity).roundabout$getTrueInvisibility() > -1)
                || !(((StandUser)User).roundabout$getStandPowers() instanceof PowersBadCompany)){
            return;
        }

        if (mobEntity.getFadeOut() >= mobEntity.getMaxFade()){


            if (((IEntityAndData)User).roundabout$getTrueInvisibilityManhattan() < 1 && ClientUtil.checkIfClientCanSeeMobsForWindVision()){
                return;
            }
            EntityRenderDispatcher renderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

            matrixStack.pushPose();

            float height = (mobEntity.getNameTagOffsetY() + 0.275F);

            // Orient the texture
            matrixStack.scale(1, 1, 1);
            matrixStack.translate(0, height, 0);
            matrixStack.mulPose(renderDispatcher.cameraOrientation());
            matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));

            // Draw flat quad here
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(StandIcons.BAD_COMPANY_AMMUNITION)).color(1.0f, 1.0f, 1.0f, 0.6f);
            Matrix4f matrix = matrixStack.last().pose();
            Matrix3f normal = matrixStack.last().normal();

            vertexConsumer.vertex(matrix, -0.3f, -0.3f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
            vertexConsumer.vertex(matrix, 0.3f, -0.3f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
            vertexConsumer.vertex(matrix, 0.3f, 0.3f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();
            vertexConsumer.vertex(matrix, -0.3f, 0.3f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0, 0, -1).endVertex();


            RenderSystem.enableDepthTest();
            matrixStack.popPose();
        }
    }


}