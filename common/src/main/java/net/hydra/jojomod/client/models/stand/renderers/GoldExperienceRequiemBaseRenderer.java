package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GoldExperienceRequiemModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.GoldExperienceRequiemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class GoldExperienceRequiemBaseRenderer extends StandRenderer<GoldExperienceRequiemEntity> {
    public static final ResourceLocation PART_5 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience_requiem/base.png");
    public static final ResourceLocation DIAMOND = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience_requiem/diamond.png");
    public static final ResourceLocation GUARD = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience_requiem/guard.png");
    public static final ResourceLocation PS2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience_requiem/ps2.png");
    public static final ResourceLocation DEIMOS = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience_requiem/deimos.png");

    public GoldExperienceRequiemBaseRenderer(EntityRendererProvider.Context context, StandModel<GoldExperienceRequiemEntity> entityModel, float f) {
        super(context, entityModel, f);
        //this.addLayer(new GoldExperienceLifePunchLayer(this));
    }

    public static ResourceLocation getSkin(byte bt) {
        if (bt == GoldExperienceEntity.BETA) {
            return PART_5;
        }
        if (bt == GoldExperienceEntity.DIAMOND) {
            return DIAMOND;
        }
        if (bt == GoldExperienceEntity.GUARD) {
            return GUARD;
        }
        if (bt == GoldExperienceEntity.PS2) {
            return PS2;
        }
        if (bt == GoldExperienceEntity.DEIMOS) {
            return DEIMOS;
        }
        return PART_5;
    }

    @Override
    public void render(GoldExperienceRequiemEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if(mobEntity.isBaby()){
            matrixStack.scale(0.50f * factor, 0.50f * factor, 0.50f * factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(GoldExperienceRequiemEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public ResourceLocation getTextureLocation(GoldExperienceRequiemEntity entity) {
        return getSkin(entity.getSkin());
    }
}
