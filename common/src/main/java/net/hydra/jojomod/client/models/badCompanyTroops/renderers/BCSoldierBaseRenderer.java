package net.hydra.jojomod.client.models.badCompanyTroops.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.entity.badCompanyTroops.BCSoldier;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBadCompany;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class BCSoldierBaseRenderer extends StandRenderer<BCSoldier> {

    private static final ResourceLocation BETA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/soldier/beta.png");
    private static final ResourceLocation ANIME = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/soldier/anime.png");
    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/soldier/manga.png");
    private static final ResourceLocation MARINE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/soldier/marine.png");
    private static final ResourceLocation SAND = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/soldier/sand.png");

    public BCSoldierBaseRenderer(EntityRendererProvider.Context context, StandModel<BCSoldier> entityModel, float f) {
        super(context, entityModel,f);
    }

    @Override
    public ResourceLocation getTextureLocation(BCSoldier entity) {
        LivingEntity user = entity.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersBadCompany) {
            byte skn = ((StandUser)user).roundabout$getStandSkin();

            if (skn == PowersBadCompany.ANIME_SKIN) {
                return ANIME;
            }else if (skn == PowersBadCompany.MANGA_SKIN) {
                return MANGA;
            }else if (skn == PowersBadCompany.BETA_SKIN) {
                return BETA;
            }else if (skn == PowersBadCompany.MARINE_SKIN) {
                return MARINE;
            }else if (skn == PowersBadCompany.SAND_SKIN) {
                return SAND;
            }
        }

        return ANIME;
    }

    @Override
    public void render(BCSoldier mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumer, int i) {
        float factor = 0.3f;
        if (mobEntity.isDisplay) {
            factor = 1.5f;
        }
        if(mobEntity.isBaby()){
            matrixStack.scale(0.60f * factor, 0.60f * factor, 0.60f * factor);
        } else {
            matrixStack.scale(0.80f * factor, 0.80f * factor, 0.80f * factor);
        }

        if (mobEntity.getBundle()) {
            float offCenter = 1.15f;

            // Line 1
            renderExtraSoldier(1.0f, 0.0f, -0.5f+offCenter, mobEntity, f, g, matrixStack, vertexConsumer, i);
            renderExtraSoldier(0.0f, 0.0f, 0.0f+offCenter, mobEntity, f, g, matrixStack, vertexConsumer, i);
            renderExtraSoldier(-1.0f, 0.0f, -0.5f+offCenter, mobEntity, f, g, matrixStack, vertexConsumer, i);

            // Line 2
            renderExtraSoldier(1.0f, 0.0f, -1.8f+offCenter, mobEntity, f, g, matrixStack, vertexConsumer, i);
            renderExtraSoldier(0.0f, 0.0f, -2.3f+offCenter, mobEntity, f, g, matrixStack, vertexConsumer, i);
            renderExtraSoldier(-1.0f, 0.0f, -1.8f+offCenter, mobEntity, f, g, matrixStack, vertexConsumer, i);
        }else {
            super.render(mobEntity, f, g, matrixStack, vertexConsumer, i);
        }
    }

    @Nullable
    @Override
    protected RenderType getRenderType(BCSoldier entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation $$4 = this.getTextureLocation(entity);
        return RenderType.entityTranslucent($$4);
    }

    private void renderExtraSoldier(float x, float y, float z, BCSoldier mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumer, int i) {
        matrixStack.pushPose();
        matrixStack.translate( x, y, z );

        super.render(mobEntity, f, g, matrixStack, vertexConsumer, i);
        matrixStack.popPose();
    }
}
