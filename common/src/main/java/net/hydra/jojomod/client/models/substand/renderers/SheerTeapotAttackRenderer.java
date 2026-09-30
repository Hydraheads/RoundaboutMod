package net.hydra.jojomod.client.models.substand.renderers;

import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.substand.SheerTeapotAttackModel;
import net.hydra.jojomod.entity.substand.SheerTeapotAttackEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;


public class SheerTeapotAttackRenderer extends SheerHeartAttackBaseRenderer<SheerTeapotAttackEntity> {

    public SheerTeapotAttackRenderer(EntityRendererProvider.Context context) {
        super(context, new SheerTeapotAttackModel<>(context.bakeLayer(ModEntityRendererClient.SHEER_TEAPOT_ATTACK_LAYER)));
    }
    
}