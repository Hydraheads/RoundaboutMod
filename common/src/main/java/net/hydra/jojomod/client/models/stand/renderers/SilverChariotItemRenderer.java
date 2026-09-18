package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;

public class SilverChariotItemRenderer extends StandHeldItemLayer<SilverChariotEntity, StandModel<SilverChariotEntity>> {
    public SilverChariotItemRenderer(RenderLayerParent<SilverChariotEntity, StandModel<SilverChariotEntity>> parent,
                                     ItemInHandRenderer itemRenderer) {
        super(parent, itemRenderer);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int p_117206_, SilverChariotEntity entity, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
        super.render(stack, bufferSource, p_117206_, entity, p_117208_, p_117209_, p_117210_, p_117211_, p_117212_, p_117213_);
    }
}
