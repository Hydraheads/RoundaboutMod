package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;

public class StandRenderer<T extends StandEntity> extends MobEntityRenderer<T, StandModel<T>> {
    public StandRenderer(EntityRendererFactory.Context context, StandModel<T> entityModel, float f) {
        super(context, entityModel, f);
    }



    @Override
    public Identifier getTexture(T entity) {
        return null;
    }



    @Override
    public void render(T mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
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
        BlockPos blockPos = BlockPos.ofFloored(entity.getClientCameraPosVec(tickDelta));
        return LightmapTextureManager.pack(this.getTrueBlockLight(entity, blockPos), this.getTrueSkyLight(entity, blockPos));
    }

    protected int getTrueSkyLight(Entity entity, BlockPos pos) {
        return entity.getWorld().getLightLevel(LightType.SKY, pos);
    }

    protected int getTrueBlockLight(Entity entity, BlockPos pos) {
        if (entity.isOnFire()) {
            return 15;
        }
        return entity.getWorld().getLightLevel(LightType.BLOCK, pos);
    }

    public float getStandOpacity(T entity){
        if (!entity.hasUser()) {
            return 1;
        } else {
            int vis = entity.getFadeOut();
            int max = entity.getMaxFade();
            float tot = (float) ((((float) vis / max) * 1.3) - 0.3);
            if (tot < 0) {
                tot = 0;
            }
            return tot;
        }
    }
}
