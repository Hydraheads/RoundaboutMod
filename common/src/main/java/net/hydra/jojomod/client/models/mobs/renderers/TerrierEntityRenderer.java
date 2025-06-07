package net.hydra.jojomod.client.models.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;


public class TerrierEntityRenderer extends MobRenderer<Wolf, WolfModel<Wolf>> {
    private static final ResourceLocation WILD_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/terrier.png");
    private static final ResourceLocation TAMED_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/terrier_tame.png");
    private static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/terrier_angry.png");


    public TerrierEntityRenderer(EntityRendererProvider.Context context) {
        super(context,new WolfModel<>(context.bakeLayer(ModEntityRendererClient.WOLF_LAYER)),0.5F);
        this.addLayer(new WolfCollarLayer(this));
    }
    @Override
    public void render(Wolf wolfEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (wolfEntity.isWet()) {
            float h = wolfEntity.getWetShade(g);
            (this.model).setColor(h, h, h);
        }
        matrixStack.scale(0.8f,0.8f,0.8f);
        super.render(wolfEntity, f, g, matrixStack, vertexConsumerProvider, i);
        if (wolfEntity.isWet()) {
            (this.model).setColor(1.0f, 1.0f, 1.0f);
        }
    }
    @Override
    protected float getBob(Wolf wolfEntity, float f) {
        return wolfEntity.getTailAngle();
    }

    @Override
    public ResourceLocation getTextureLocation(Wolf terrierEntity) {
        if (terrierEntity.isTame()) {
            return TAMED_TEXTURE;
        }
        if (terrierEntity.isAngry()) {
            return ANGRY_TEXTURE;
        }
        return WILD_TEXTURE;
    }


}
