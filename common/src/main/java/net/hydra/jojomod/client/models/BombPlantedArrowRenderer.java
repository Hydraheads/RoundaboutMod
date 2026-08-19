package net.hydra.jojomod.client.models;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;

public class BombPlantedArrowRenderer extends ArrowRenderer<AbstractArrow> {
    public static final ResourceLocation NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");
    public static final ResourceLocation TIPPED_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/tipped_arrow.png");
    public static final ResourceLocation SPECTRAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/spectral_arrow.png");

    public BombPlantedArrowRenderer(EntityRendererProvider.Context p_174422_) {
        super(p_174422_);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractArrow abstractArrow) {
        if (abstractArrow instanceof Arrow AR) {
            return AR.getColor() > 0 ? TIPPED_ARROW_LOCATION : NORMAL_ARROW_LOCATION;
        }else {
            return SPECTRAL_ARROW_LOCATION;
        }
    }
}

