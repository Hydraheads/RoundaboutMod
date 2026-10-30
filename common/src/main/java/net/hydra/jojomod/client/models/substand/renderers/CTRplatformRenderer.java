package net.hydra.jojomod.client.models.substand.renderers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.CTRplatformModel;
import net.hydra.jojomod.entity.StepRuleEntity;
import net.hydra.jojomod.entity.substand.CTRplatformEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CTRplatformRenderer extends StandRenderer<CTRplatformEntity> {

    public CTRplatformRenderer(EntityRendererProvider.Context context) {
        super(context, new CTRplatformModel<>(context.bakeLayer(ModEntityRendererClient.CTR_PLATFORM_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(CTRplatformEntity ctrplatformEntity) {
        return new ResourceLocation(Roundabout.MOD_ID,"textures/stand/achtung_baby/very_badass_texture.png");
    }
}
