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

    private static final ResourceLocation PART_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/silver_chariot.png");
    private static final ResourceLocation PART_3_GREY = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/anime_silver_chariot.png");
    private static final ResourceLocation PART_3_MANGA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/manga_silver_chariot.png");
    private static final ResourceLocation PART_5 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/part_5_silver_chariot.png");
    private static final ResourceLocation AQUA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/aqua_silver_chariot.png");
    private static final ResourceLocation AZURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/azure_silver_chariot.png");
    private static final ResourceLocation BLUE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/blue_silver_chariot.png");
    private static final ResourceLocation CRYSTAL = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/crystal_silver_chariot.png");
    private static final ResourceLocation END_OF_THE_WORLD = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/end_of_the_world_silver_chariot.png");
    private static final ResourceLocation GENESIS_OF_THE_UNIVERSE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/genesis_of_the_universe_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_A = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_a_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_B = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_b_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_C = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_c_silver_chariot.png");
    private static final ResourceLocation NIGHTMARE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/nightmare_silver_chariot.png");
    private static final ResourceLocation ORANGE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/orange_silver_chariot.png");
    private static final ResourceLocation PASSIONE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/passione_silver_chariot.png");
    private static final ResourceLocation PURPLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/purple_silver_chariot.png");
    private static final ResourceLocation SANDY = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/sandy_silver_chariot.png");
    private static final ResourceLocation TURQUOISE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/turquoise_silver_chariot.png");
    private static final ResourceLocation YELLOW = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/yellow_silver_chariot.png");

    @Override
    public void render(SilverChariotRapierShotEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        // super.render($$0, $$1, $$2, $$3, $$4, $$5);
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            $$3.pushPose();
            // $$3.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot()) + 180.0F));
            // $$3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot()) - 90.0F));

            $$3.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot())));
            $$3.mulPose(Axis.XP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot())));

            $$3.scale(1.5f, 1.5f, 1.5f);

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
            case SilverChariotEntity.PART_3 -> {
                return PART_3;
            }
            case SilverChariotEntity.PART_3_GREY -> {
                return PART_3_GREY;
            }
            case SilverChariotEntity.PART_3_MANGA -> {
                return PART_3_MANGA;
            }
            case SilverChariotEntity.PART_5 -> {
                return PART_5;
            }
            case SilverChariotEntity.AQUA -> {
                return AQUA;
            }
            case SilverChariotEntity.AZURE -> {
                return AZURE;
            }
            case SilverChariotEntity.BLUE -> {
                return BLUE;
            }
            case SilverChariotEntity.CRYSTAL -> {
                return CRYSTAL;
            }
            case SilverChariotEntity.END_OF_THE_WORLD -> {
                return END_OF_THE_WORLD;
            }
            case SilverChariotEntity.GENESIS_OF_THE_UNIVERSE -> {
                return GENESIS_OF_THE_UNIVERSE;
            }
            case SilverChariotEntity.JOJONIUM_A -> {
                return JOJONIUM_A;
            }
            case SilverChariotEntity.JOJONIUM_B -> {
                return JOJONIUM_B;
            }
            case SilverChariotEntity.JOJONIUM_C -> {
                return JOJONIUM_C;
            }
            case SilverChariotEntity.NIGHTMARE -> {
                return NIGHTMARE;
            }
            case SilverChariotEntity.ORANGE -> {
                return ORANGE;
            }
            case SilverChariotEntity.PASSIONE -> {
                return PASSIONE;
            }
            case SilverChariotEntity.PURPLE -> {
                return PURPLE;
            }
            case SilverChariotEntity.SANDY -> {
                return SANDY;
            }
            case SilverChariotEntity.TURQUOISE -> {
                return TURQUOISE;
            }
            case SilverChariotEntity.YELLOW -> {
                return YELLOW;
            }
        }
        return PART_3;
    }

    @Override
    public ResourceLocation getTextureLocation(SilverChariotRapierShotEntity entity) {
        if (entity.silverChariot != null) {
            byte skin = entity.silverChariot.getSkin();
            return getSkin(skin);
        }
        return PART_3;
    }
}
