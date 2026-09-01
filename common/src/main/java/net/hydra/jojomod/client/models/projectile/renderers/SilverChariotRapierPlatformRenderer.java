package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.SilverChariotRapierModel;
import net.hydra.jojomod.client.models.projectile.SilverChariotRapierPlatformModel;
import net.hydra.jojomod.entity.projectile.SilverChariotRapierPlatformEntity;
import net.hydra.jojomod.entity.projectile.SilverChariotRapierShotEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SilverChariotRapierPlatformRenderer extends EntityRenderer<SilverChariotRapierPlatformEntity> {

    private final SilverChariotRapierPlatformModel model;

    public SilverChariotRapierPlatformRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new SilverChariotRapierPlatformModel<>($$0.bakeLayer(ModEntityRendererClient.SILVER_CHARIOT_RAPIER_PLATFORM_LAYER));
    }

    private static final ResourceLocation ANIME_PART_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/anime_part_3_rapier.png");

    @Override
    public ResourceLocation getTextureLocation(SilverChariotRapierPlatformEntity entity) {
        // Yes, I am aware that it is not proper to have only one case in a switch statement
        // return switch (entity.getSkin()) {
        //    default -> ANIME_PART_3;
        // };
        return ANIME_PART_3;
    }

    @Override
    public void render(SilverChariotRapierPlatformEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        // super.render($$0, $$1, $$2, $$3, $$4, $$5);
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            $$3.pushPose();
            // $$3.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot()) + 180.0F));
            // $$3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot()) - 90.0F));

            $$3.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot())));
            $$3.mulPose(Axis.XP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot())));

            $$3.scale(1.1f, 1.1f, 1.1f);
            VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect($$4, this.model.renderType(this.getTextureLocation($$0)), false, false);// $$0.isFoil());
            this.model.renderToBuffer($$3, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            $$3.popPose();
            // super.render($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }
}
