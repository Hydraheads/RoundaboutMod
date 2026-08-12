package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.stand.PlanetWavesCosmicModel;
import net.hydra.jojomod.client.models.stand.PlanetWavesSpartaModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.PlanetWavesCosmicEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.PlanetWavesEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PlanetWavesCosmicRenderer<T extends StandEntity> extends PlanetWavesBaseRenderer<PlanetWavesCosmicEntity> {
    public PlanetWavesCosmicRenderer(EntityRendererProvider.Context context) {
        super(context, new PlanetWavesCosmicModel<>(context.bakeLayer(ModEntityRendererClient.PLANET_WAVES_COSMIC_LAYER)), 0f);
    }
}