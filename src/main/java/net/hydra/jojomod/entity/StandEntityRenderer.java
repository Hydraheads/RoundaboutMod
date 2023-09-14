package net.hydra.jojomod.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(value= EnvType.CLIENT)
public class StandEntityRenderer extends GeoEntityRenderer<StandEntity> implements GeoRenderer<StandEntity> {

    public StandEntityRenderer(EntityRendererFactory.Context renderManager) {

        super(renderManager, new StandModel());
    }
    @Override
    public RenderLayer getRenderType(StandEntity animatable, Identifier texture,
                                     @Nullable VertexConsumerProvider bufferSource,
                                     float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public Color getRenderColor(StandEntity animatable, float partialTick, int packedLight) {
        return Color.ofRGBA(1F,1F,1F,getStandOpacity());
    }

    public float getStandOpacity(){
        int vis = this.animatable.getFadeOut();
        int max = this.animatable.getMaxFade();
        float tot = (float) ((((float) vis/max)*1.3)-0.3);
        if (tot < 0) {
            tot=0;
        }
            return tot;
    }

    @Override
    public void render(StandEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f,0.4f,0.4f);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, 255);
    }
}
