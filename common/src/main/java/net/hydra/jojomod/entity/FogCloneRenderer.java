package net.hydra.jojomod.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class FogCloneRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    public FogCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Override
    public ResourceLocation getTextureLocation(T var1) {
        return null;
    }
}
