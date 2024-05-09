package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.Nullable;

public class TheWorldRenderer extends StandRenderer<TheWorldEntity> {

    private static final Identifier SKIN_1 = new Identifier(RoundaboutMod.MOD_ID,"textures/stand/the_world_s.png");
    public TheWorldRenderer(EntityRendererFactory.Context context) {
        super(context, new TheWorldModel<>(context.getPart(ModEntityRendererClient.THE_WORLD_LAYER)),0f);
    }

    @Override
    public Identifier getTexture(TheWorldEntity entity) {
        return SKIN_1;
    }

    @Override
    public void render(TheWorldEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (mobEntity.isBaby()){
            matrixStack.scale(0.5f,0.5f,0.5f);
        } else {
            matrixStack.scale(0.87f,0.87f,0.87f);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(TheWorldEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderLayer(entity, showBody, true, showOutline);
    }


}
