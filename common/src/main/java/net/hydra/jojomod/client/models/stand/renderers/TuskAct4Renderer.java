package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.TuskAct3Model;
import net.hydra.jojomod.client.models.stand.TuskAct4Model;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TuskAct4Renderer extends StandRenderer<TuskEntity> {
    public TuskAct4Renderer(EntityRendererProvider.Context context) {
        super(context, new TuskAct4Model<>(context.bakeLayer(ModEntityRendererClient.TUSK_A4_LAYER)), 0f);
    }
    @Override
    public ResourceLocation getTextureLocation(TuskEntity entity) {return PowersTusk.getSkin(entity.getSkin(),4);}
}
