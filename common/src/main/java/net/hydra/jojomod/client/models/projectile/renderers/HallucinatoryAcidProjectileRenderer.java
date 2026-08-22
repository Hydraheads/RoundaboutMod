package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.HallucinatoryAcidColors;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.HallucinatoryAcidProjectileModel;
import net.hydra.jojomod.entity.projectile.HallucinatoryAcidProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HallucinatoryAcidProjectileRenderer extends EntityRenderer<HallucinatoryAcidProjectile> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/projectile/acid_projectile.png");
    private final HallucinatoryAcidProjectileModel model;

    public HallucinatoryAcidProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new HallucinatoryAcidProjectileModel(
                context.bakeLayer(ModEntityRendererClient.HALLUCINATORY_ACID_PROJECTILE_LAYER));
    }

    @Override
    public void render(HallucinatoryAcidProjectile projectile, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float rotation = (projectile.tickCount + partialTick) * 12.0F;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotation * 0.65F));

        int color = HallucinatoryAcidColors.tint(
                HallucinatoryAcidProjectile.itemSkin(projectile.getItem()));
        float red = ((color >> 16) & 255) / 255.0F;
        float green = ((color >> 8) & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        VertexConsumer innerBuffer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.renderInner(poseStack, innerBuffer, packedLight, OverlayTexture.NO_OVERLAY,
                red, green, blue);
        VertexConsumer outerBuffer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
        model.renderOuter(poseStack, outerBuffer, packedLight, OverlayTexture.NO_OVERLAY,
                red, green, blue);
        poseStack.popPose();
        super.render(projectile, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(HallucinatoryAcidProjectile projectile) {
        return TEXTURE;
    }
}
