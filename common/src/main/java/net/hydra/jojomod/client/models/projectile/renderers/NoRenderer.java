package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NoRenderer extends EntityRenderer<Entity> {


    public NoRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    public void render(Entity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
    }

    @Override
    public ResourceLocation getTextureLocation(Entity var1) {
        return null;
    }
}
