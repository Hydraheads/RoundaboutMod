package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.TuskAct1Model;
import net.hydra.jojomod.client.models.stand.TuskAct2Model;
import net.hydra.jojomod.client.models.stand.WalkingHeartModel;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.hydra.jojomod.entity.stand.WalkingHeartEntity;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TuskAct1Renderer extends StandRenderer<TuskEntity> {
    public TuskAct1Renderer(EntityRendererProvider.Context context) {
        super(context, new TuskAct1Model<>(context.bakeLayer(ModEntityRendererClient.TUSK_A1_LAYER)), 0f);
    }
    @Override
    public ResourceLocation getTextureLocation(TuskEntity entity) {return PowersTusk.getSkin(entity.getSkin(),1);}
}
