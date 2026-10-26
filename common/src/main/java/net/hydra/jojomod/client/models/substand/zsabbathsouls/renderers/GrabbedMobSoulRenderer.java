package net.hydra.jojomod.client.models.substand.zsabbathsouls.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.substand.zsabbathsouls.GrabbedMobSoulModel;
import net.hydra.jojomod.entity.objects.GrabbedMobSoul;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GrabbedMobSoulRenderer<T extends GrabbedMobSoul>  extends GrabbedMobSoulBaseRenderer<T, PlayerModel<T>> {

    public GrabbedMobSoulRenderer(EntityRendererProvider.Context context) {
        super(context, new GrabbedMobSoulModel(context.bakeLayer(ModEntityRendererClient.GRABBED_MOB_SOUL_LAYER)), 0f);
    }
}
