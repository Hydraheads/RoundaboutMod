package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class LocacacaBeamLayer<T extends LivingEntity, M extends PlayerModel<T>> extends RenderLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public LocacacaBeamLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(PoseStack ps, MultiBufferSource mb, int integ, T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, float $$6) {
        ModFirstPersonLayers.render(ps,mb,integ,$$0,$$2);
    }
}
