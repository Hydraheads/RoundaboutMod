package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.TheGratefulDeadModel;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.hydra.jojomod.entity.stand.TheGratefulDeadEntity;

import javax.annotation.Nullable;

public class TheGratefulDeadRenderer extends StandRenderer<TheGratefulDeadEntity>{
    private static final ResourceLocation ANIME_THE_GRATEFUL_DEAD = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/the_grateful_dead/anime_tgd.png");
    private static final ResourceLocation MANGA_THE_GRATEFUL_DEAD = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/the_grateful_dead/manga_tgd.png");

    public TheGratefulDeadRenderer(EntityRendererProvider.Context context) {
        super(context, new TheGratefulDeadModel<>(context.bakeLayer(ModEntityRendererClient.THE_GRATEFUL_DEAD_LAYER)), 0f);
    }
    @Override
    public ResourceLocation getTextureLocation(TheGratefulDeadEntity entity){
        return switch (entity.getSkin()) {
            case (TheGratefulDeadEntity.ANIME_THE_GRATEFUL_DEAD) -> ANIME_THE_GRATEFUL_DEAD;
            case (TheGratefulDeadEntity.MANGA_THE_GRATEFUL_DEAD) -> MANGA_THE_GRATEFUL_DEAD;
            default -> ANIME_THE_GRATEFUL_DEAD;
        };
    }
    @Override
    public void render(TheGratefulDeadEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i){
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    @Nullable
    @Override
    protected RenderType getRenderType(TheGratefulDeadEntity entity, boolean showBody, boolean translucent, boolean showOutline){
        return super.getRenderType(entity, showBody, translucent, showOutline);
    }

    @Override
    public float getStandOpacity(TheGratefulDeadEntity entity){
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
