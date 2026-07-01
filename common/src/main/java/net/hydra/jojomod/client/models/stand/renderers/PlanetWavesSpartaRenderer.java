package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.stand.PlanetWavesSpartaModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.PlanetWavesSpartaEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.PlanetWavesEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PlanetWavesSpartaRenderer<T extends StandEntity> extends PlanetWavesBaseRenderer<PlanetWavesSpartaEntity> {
    public PlanetWavesSpartaRenderer(EntityRendererProvider.Context context) {
        super(context, new PlanetWavesSpartaModel<>(context.bakeLayer(ModEntityRendererClient.PLANET_WAVES_SPARTA_LAYER)), 0f);
    }
}