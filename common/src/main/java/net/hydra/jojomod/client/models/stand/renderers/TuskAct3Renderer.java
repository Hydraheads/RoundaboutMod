package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.TuskAct3Model;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TuskAct3Renderer extends StandRenderer<TuskEntity> {
    public TuskAct3Renderer(EntityRendererProvider.Context context) {
        super(context, new TuskAct3Model<>(context.bakeLayer(ModEntityRendererClient.TUSK_A3_LAYER)), 0f);
    }
    @Override
    public ResourceLocation getTextureLocation(TuskEntity entity) {return PowersTusk.getSkin(entity.getSkin(),3);}

}
