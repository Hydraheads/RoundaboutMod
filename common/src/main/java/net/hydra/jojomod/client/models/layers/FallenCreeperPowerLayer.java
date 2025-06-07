package net.hydra.jojomod.client.models.layers;

import net.hydra.jojomod.client.models.corpses.FallenCreeperModel;
import net.hydra.jojomod.entity.corpses.FallenCreeper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class FallenCreeperPowerLayer extends EnergySwirlLayer<FallenCreeper, FallenCreeperModel<FallenCreeper>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final FallenCreeperModel<FallenCreeper> model;

    public FallenCreeperPowerLayer(RenderLayerParent<FallenCreeper, FallenCreeperModel<FallenCreeper>> $$0, EntityModelSet $$1) {
        super($$0);
        this.model = new FallenCreeperModel<>($$1.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    @Override
    protected float xOffset(float $$0) {
        return $$0 * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected EntityModel<FallenCreeper> model() {
        return this.model;
    }
}
