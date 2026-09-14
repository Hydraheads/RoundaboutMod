package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.SilverChariotRapierModel;
import net.hydra.jojomod.entity.projectile.SilverChariotRapierShotEntity;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SilverChariotRapierRenderer extends EntityRenderer<SilverChariotRapierShotEntity> {

    private final SilverChariotRapierModel model;

    public SilverChariotRapierRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new SilverChariotRapierModel<>($$0.bakeLayer(ModEntityRendererClient.SILVER_CHARIOT_RAPIER_LAYER));
    }

    //  static final ResourceLocation ANIME_PART_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/anime_part_3_rapier.png");

    private static final ResourceLocation DEFAULT = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/silver_chariot.png");
    private static final ResourceLocation ANIME_PART_3_GREY = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/anime_silver_chariot.png");
    private static final ResourceLocation MANGA_PART_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/manga_silver_chariot.png");
    private static final ResourceLocation PART_5 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/part_5_silver_chariot.png");

    @Override
    public void render(SilverChariotRapierShotEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        // super.render($$0, $$1, $$2, $$3, $$4, $$5);
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            $$3.pushPose();
            // $$3.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot()) + 180.0F));
            // $$3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot()) - 90.0F));

            $$3.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot())));
            $$3.mulPose(Axis.XP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot())));

            $$3.scale(1.1f, 1.1f, 1.1f);

            RenderType $$6 = RenderType.entityTranslucent(this.getTextureLocation($$0));
            // VertexConsumer $$7 = ItemRenderer.getFoilBufferDirect($$4, this.model.renderType(this.getTextureLocation($$0)), false, false);// $$0.isFoil());
            VertexConsumer $$7 = ItemRenderer.getFoilBufferDirect($$4, $$6, false, false);// $$0.isFoil());
            this.model.renderToBuffer($$3, $$7, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            $$3.popPose();
            // super.render($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }

    public static ResourceLocation getSkin(byte bt) {
        switch (bt) {
            case SilverChariotEntity.ANIME_PART_3_SILVER_CHARIOT -> {
                return DEFAULT;
            }
            case SilverChariotEntity.ANIME_PART_3_SILVER_CHARIOT_GREY -> {
                return ANIME_PART_3_GREY;
            }
            case SilverChariotEntity.MANGA_PART_3_SILVER_CHARIOT -> {
                return MANGA_PART_3;
            }
            case SilverChariotEntity.PART_5_SILVER_CHARIOT -> {
                return PART_5;
            }
        }
        return DEFAULT;
    }

    @Override
    public ResourceLocation getTextureLocation(SilverChariotRapierShotEntity entity) {
        // Yes, I am aware that it is not proper to have only one case in a switch statement
        // return switch (entity.getSkin()) {
        //    default -> ANIME_PART_3;
        // };
        return DEFAULT;
    }
}
