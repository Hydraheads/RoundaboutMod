package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.TuskAct2Model;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TuskAct2Renderer extends StandRenderer<TuskEntity> {
    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/tusk/act_2/manga.png");
    public TuskAct2Renderer(EntityRendererProvider.Context context) {
        super(context, new TuskAct2Model<>(context.bakeLayer(ModEntityRendererClient.TUSK_A2_LAYER)), 0f);
    }
    @Override
    public ResourceLocation getTextureLocation(TuskEntity entity) {return PowersTusk.getSkin(entity.getSkin(),2);}
}
