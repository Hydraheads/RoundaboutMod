package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GratefulDeadModel;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.hydra.jojomod.entity.stand.GratefulDeadEntity;

import javax.annotation.Nullable;

public class GratefulDeadRenderer extends StandRenderer<GratefulDeadEntity>{
    private static final ResourceLocation ANIME = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/grateful_dead/anime.png");
    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/grateful_dead/manga.png");
    private static final ResourceLocation ANGEL = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/grateful_dead/angel.png");

    public GratefulDeadRenderer(EntityRendererProvider.Context context) {
        super(context, new GratefulDeadModel<>(context.bakeLayer(ModEntityRendererClient.GRATEFUL_DEAD_LAYER)), 0f);
    }
    @Override
    public ResourceLocation getTextureLocation(GratefulDeadEntity entity){
        return switch (entity.getSkin()) {
            case (GratefulDeadEntity.ANGEL) -> ANGEL;
            case (GratefulDeadEntity.MANGA) -> MANGA;
            default -> ANIME;
        };
    }
    @Override
    public void render(GratefulDeadEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i){
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    @Nullable
    @Override
    protected RenderType getRenderType(GratefulDeadEntity entity, boolean showBody, boolean translucent, boolean showOutline){
        return super.getRenderType(entity, showBody, translucent, showOutline);
    }

    @Override
    public float getStandOpacity(GratefulDeadEntity entity){
        float base = super.getStandOpacity(entity);

        if (!entity.hasUser()) {
            return base;
        }
        if (((StandUser)entity.getUser()).roundabout$isParallelRunning()){
            return base/2;
        }
        else{
            return base;
        }
    }
}
