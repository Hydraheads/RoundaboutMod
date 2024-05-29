package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;

public class StandRenderer<T extends StandEntity> extends MobRenderer<T, StandModel<T>> {
    /**Stand renderers should all extend this, because it is used
     * to make sure stand lighting doesn't mess up when they clip through blocks.*/
    public StandRenderer(EntityRendererProvider.Context context, StandModel<T> entityModel, float f) {
        super(context, entityModel, f);
    }



    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }



    @Override
    public void render(T mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        (this.model).setAlpha(getStandOpacity(mobEntity));



        int plight = i;
        var owner = mobEntity.getUser();
        if (owner != null) {
            int tlight = getTrueLight(owner,g);
            if (plight < tlight && plight < 1){
                plight = tlight;
            }
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, plight);
    }
    public final int getTrueLight(Entity entity, float tickDelta) {
        BlockPos blockPos = BlockPos.containing(entity.getLightProbePosition(tickDelta));
        return LightTexture.pack(this.getTrueBlockLight(entity, blockPos), this.getTrueSkyLight(entity, blockPos));
    }

    protected int getTrueSkyLight(Entity entity, BlockPos pos) {
        return entity.level().getBrightness(LightLayer.SKY, pos);
    }

    protected int getTrueBlockLight(Entity entity, BlockPos pos) {
        if (entity.isOnFire()) {
            return 15;
        }
        return entity.level().getBrightness(LightLayer.BLOCK, pos);
    }

    public float getStandOpacity(T entity){
            int vis = entity.getFadeOut();
            int max = entity.getMaxFade();
            float tot = (float) ((((float) vis / max) * 1.3) - 0.3);
            if (tot < 0) {
                tot = 0;
            }
            return tot;
    }
}
