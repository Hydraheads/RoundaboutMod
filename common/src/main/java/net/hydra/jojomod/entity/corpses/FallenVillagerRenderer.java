package net.hydra.jojomod.entity.corpses;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;

public class FallenVillagerRenderer extends MobRenderer<FallenVillager, FallenVillagerModel<FallenVillager>> {
    private static final ResourceLocation FALLEN_ZOMBIE_LOCATION = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/justice_corpses/justice_villager.png");

    public FallenVillagerRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new FallenVillagerModel<>($$0.bakeLayer(ModelLayers.VILLAGER)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, $$0.getModelSet(), $$0.getItemInHandRenderer()));
        this.addLayer(new CrossedArmsItemLayer<>(this, $$0.getItemInHandRenderer()));
    }

    private static final ResourceLocation VILLAGER_BASE_SKIN = new ResourceLocation("textures/entity/villager/villager.png");

    public ResourceLocation getTextureLocation(FallenVillager $$0) {
        return FALLEN_ZOMBIE_LOCATION;
    }

    protected void scale(FallenVillager $$0, PoseStack $$1, float $$2) {
        float $$3 = 0.9375F;
        if ($$0.isBaby()) {
            $$3 *= 0.5F;
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }

        $$1.scale($$3, $$3, $$3);
    }
}
