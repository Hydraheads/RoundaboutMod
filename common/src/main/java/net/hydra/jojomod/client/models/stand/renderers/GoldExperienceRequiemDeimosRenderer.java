package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GoldExperienceRequiemDeimosModel;
import net.hydra.jojomod.entity.stand.GoldExperienceRequiemEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class GoldExperienceRequiemDeimosRenderer extends GoldExperienceRequiemBaseRenderer {
    public GoldExperienceRequiemDeimosRenderer(EntityRendererProvider.Context context) {
        super(context, new GoldExperienceRequiemDeimosModel<>(context.bakeLayer(ModEntityRendererClient.GOLD_EXPERIENCE_REQUIEM_DEIMOS_LAYER)), 0f);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(GoldExperienceRequiemEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation texture = this.getTextureLocation(entity);
        if (translucent || showBody) {
            return RenderType.entityTranslucent(texture);
        }
        return showOutline ? RenderType.outline(texture) : null;
    }

    @Override
    public ResourceLocation getTextureLocation(GoldExperienceRequiemEntity entity) {
        return DEIMOS;
    }
}