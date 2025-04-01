package net.hydra.jojomod.entity.pathfinding;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.projectile.KnifeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class NoRenderer extends EntityRenderer<Entity> {


    public NoRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    public void render(KnifeEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
            super.render($$0, $$1, $$2, $$3, $$4, $$5);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity var1) {
        return null;
    }
}
