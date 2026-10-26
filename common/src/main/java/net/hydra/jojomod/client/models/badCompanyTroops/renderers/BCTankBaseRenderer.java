package net.hydra.jojomod.client.models.badCompanyTroops.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.entity.badCompanyTroops.BCSoldier;
import net.hydra.jojomod.entity.badCompanyTroops.BCTank;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBadCompany;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class BCTankBaseRenderer extends StandRenderer<BCTank> {

    private static final ResourceLocation BETA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/tank/beta.png");
    private static final ResourceLocation ANIME = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/tank/anime.png");
    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/tank/manga.png");
    private static final ResourceLocation MARINE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/tank/marine.png");
    private static final ResourceLocation SAND = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/bad_company/tank/sand.png");

    public BCTankBaseRenderer(EntityRendererProvider.Context context, StandModel<BCTank> entityModel, float f) {
        super(context, entityModel,f);
    }

    @Override
    public ResourceLocation getTextureLocation(BCTank entity) {
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
    public void render(BCTank mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumer, int i) {
        float factor = 1.2f;

        if(mobEntity.isBaby()){
            matrixStack.scale(0.60f * factor, 0.60f * factor, 0.60f * factor);
        } else {
            matrixStack.scale(0.80f * factor, 0.80f * factor, 0.80f * factor);
        }


        super.render(mobEntity, f, g, matrixStack, vertexConsumer, i);

    }

    @Nullable
    @Override
    protected RenderType getRenderType(BCTank entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation $$4 = this.getTextureLocation(entity);
        return RenderType.entityTranslucent($$4);
    }
}
