package net.hydra.jojomod.entity;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.client.ModEntityRendererClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
public class TerrierEntityRenderer extends MobEntityRenderer<WolfEntity, WolfEntityModel<WolfEntity>> {
    private static final Identifier WILD_TEXTURE = new Identifier(RoundaboutMod.MOD_ID, "textures/entity/terrier.png");
    private static final Identifier TAMED_TEXTURE = new Identifier(RoundaboutMod.MOD_ID,"textures/entity/terrier_tame.png");
    private static final Identifier ANGRY_TEXTURE = new Identifier(RoundaboutMod.MOD_ID,"textures/entity/terrier_angry.png");

    public TerrierEntityRenderer(EntityRendererFactory.Context context) {
        //super(context, new WolfEntityModel(context.getPart(EntityModelLayers.WOLF)), 0.5f);
        //this.addFeature(new WolfCollarFeatureRenderer(this));
        super(context,new WolfEntityModel<>(context.getPart(ModEntityRendererClient.WOLF_LAYER)),0.5F);
        this.addFeature(new WolfCollarFeatureRenderer(this));
    }

    @Override
    protected float getAnimationProgress(WolfEntity wolfEntity, float f) {
        return wolfEntity.getTailAngle();
    }

    @Override
    public Identifier getTexture(WolfEntity terrierEntity) {
        if (terrierEntity.isTamed()) {
            return TAMED_TEXTURE;
        }
        if (terrierEntity.hasAngerTime()) {
            return ANGRY_TEXTURE;
        }
        return WILD_TEXTURE;
    }


}
