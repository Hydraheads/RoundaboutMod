package net.hydra.jojomod.client.models.projectile.renderers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.SilverChariotRapierModel;
import net.hydra.jojomod.client.models.projectile.SilverChariotRapierPlatformModel;
import net.hydra.jojomod.entity.projectile.SilverChariotRapierPlatformEntity;
import net.hydra.jojomod.entity.projectile.SilverChariotRapierShotEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SilverChariotRapierPlatformRenderer extends EntityRenderer<SilverChariotRapierPlatformEntity> {

    private final SilverChariotRapierPlatformModel model;

    public SilverChariotRapierPlatformRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new SilverChariotRapierPlatformModel<>($$0.bakeLayer(ModEntityRendererClient.SILVER_CHARIOT_RAPIER_PLATFORM_LAYER));
    }

    private static final ResourceLocation ANIME_PART_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/anime_part_3.png");

    @Override
    public ResourceLocation getTextureLocation(SilverChariotRapierPlatformEntity entity) {
        // Yes, I am aware that it is not proper to have only one case in a switch statement
        return switch (entity.getSkin()) {
            default -> ANIME_PART_3;
        };
    }
}
