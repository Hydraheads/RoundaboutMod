package net.hydra.jojomod.client.models.mobs.renderers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.layers.anubis.AnubisMobLayer;
import net.hydra.jojomod.client.models.mobs.AnubisGuardianModel;
import net.hydra.jojomod.client.models.mobs.StrayCatEntityModel;
import net.hydra.jojomod.client.models.mobs.layers.AnubisGuardianLayer;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class StrayCatEntityRenderer<T extends StrayCatEntity>
        extends MobRenderer<T, StrayCatEntityModel<T>> {

    private static final ResourceLocation ANIME =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/stray_cat/entity/anime.png");

    public StrayCatEntityRenderer(EntityRendererProvider.Context context) {
        super(context,new StrayCatEntityModel<>(context.bakeLayer(ModEntityRendererClient.STRAY_CAT_ENTITY_LAYER)),0.4F);
        
    }

    @Override
    public ResourceLocation getTextureLocation(StrayCatEntity strayCatEntity) {
        return ANIME;
    }
}
