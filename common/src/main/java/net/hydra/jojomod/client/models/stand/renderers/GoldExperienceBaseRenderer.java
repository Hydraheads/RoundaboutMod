package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class GoldExperienceBaseRenderer extends StandRenderer<GoldExperienceEntity> {
    public static final ResourceLocation PART_5 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/base.png");
    public static final ResourceLocation BETA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/beta.png");
    public static final ResourceLocation DIAMOND = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/diamond.png");
    public static final ResourceLocation GUARD = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/guard.png");
    public static final ResourceLocation PS2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/gold_experience/ps2.png");

    public GoldExperienceBaseRenderer(EntityRendererProvider.Context context, StandModel<GoldExperienceEntity> entityModel, float f) {
        super(context, entityModel, f);
        this.addLayer(new GoldExperienceLifePunchLayer(this));
        this.addLayer(new GoldExperienceCrackLayer(this));
        this.addLayer(new GoldExperienceEyeLayer<>(this, this));
        this.addLayer(new StandArrowHeldItemLayer<>(this, context.getItemInHandRenderer()));
    }

    public static ResourceLocation getSkin(byte bt) {
        if (bt == GoldExperienceEntity.BETA) {
            return BETA;
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
        return PART_5;
    }

    @Override
    public void render(GoldExperienceEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(GoldExperienceEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public ResourceLocation getTextureLocation(GoldExperienceEntity entity) {
        return getSkin(entity.getSkin());
    }
}