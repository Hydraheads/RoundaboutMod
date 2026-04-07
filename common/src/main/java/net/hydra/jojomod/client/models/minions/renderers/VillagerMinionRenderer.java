package net.hydra.jojomod.client.models.minions.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.minions.VillagerMinionModel;
import net.hydra.jojomod.entity.zombie_minion.VillagerMinion;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vindicator;

public class VillagerMinionRenderer<T extends VillagerMinion> extends MobRenderer<VillagerMinion, VillagerMinionModel<VillagerMinion>> {
    private static final ResourceLocation VINDICATOR = new ResourceLocation("textures/entity/illager/vindicator.png");

    public VillagerMinionRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new VillagerMinionModel<>($$0.bakeLayer(ModEntityRendererClient.VILLAGER_MINION_LAYER)), 0.5F);
        this.addLayer(new ItemInHandLayer<VillagerMinion, VillagerMinionModel<VillagerMinion>>(this, $$0.getItemInHandRenderer()) {
            public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, VillagerMinion $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
                if ($$3.isAggressive()) {
                    super.render($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, $$9);
                }
            }
        });
    }


    public void scale(T $$0, PoseStack $$1, float $$2) {
        float $$3 = 0.9375F;
        $$1.scale(0.9375F, 0.9375F, 0.9375F);
    }
    public ResourceLocation getTextureLocation(VillagerMinion $$0) {
        return VINDICATOR;
    }
}