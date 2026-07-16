package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.KingCrimsonModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.TheWorldModel;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class KingCrimsonRenderer<M extends StandEntity> extends StandRenderer<KingCrimsonEntity>{

    private static final ResourceLocation PART_5_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/anime.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/manga.png");
    private static final ResourceLocation END_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/end.png");
    private static final ResourceLocation END_2_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/end_2.png");
    private static final ResourceLocation STARLESS_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/starless.png");
    private static final ResourceLocation AGOGO = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/agogo.png");
    private static final ResourceLocation DARK = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/dark.png");
    private static final ResourceLocation BLACK = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/black.png");
    private static final ResourceLocation SPINE_ART = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/spine_art.png");
    private static final ResourceLocation GREEN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/green.png");
    private static final ResourceLocation YELLOW = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/yellow.png");
    private static final ResourceLocation AQUA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/aqua.png");
    private static final ResourceLocation HEAVEN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/heaven.png");
    private static final ResourceLocation BETA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/beta.png");
    private static final ResourceLocation CONCEPT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/concept_art.png");
    private static final ResourceLocation RED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/red.png");
    private static final ResourceLocation BLUE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/king_crimson/blue.png");

    public KingCrimsonRenderer(EntityRendererProvider.Context context) {
        super(context, new KingCrimsonModel<>(context.bakeLayer(ModEntityRendererClient.KING_CRIMSON_LAYER)),0f);
    }


    @Override
    public ResourceLocation getTextureLocation(KingCrimsonEntity entity) {
        byte BT = entity.getSkin();
        if (BT == KingCrimsonEntity.MANGA_SKIN){
            return MANGA_SKIN;
        } else if (BT == KingCrimsonEntity.STARLESS){
            return STARLESS_SKIN;
        } else if (BT == KingCrimsonEntity.END){
            return END_SKIN;
        } else if (BT == KingCrimsonEntity.END_2){
            return END_2_SKIN;
        } else if (BT == KingCrimsonEntity.AGOGO){
            return AGOGO;
        } else if (BT == KingCrimsonEntity.HEAVEN){
            return HEAVEN;
        } else if (BT == KingCrimsonEntity.SPINE_ART){
            return SPINE_ART;
        } else if (BT == KingCrimsonEntity.GREEN){
            return GREEN;
        } else if (BT == KingCrimsonEntity.YELLOW){
            return YELLOW;
        } else if (BT == KingCrimsonEntity.AQUA){
            return AQUA;
        } else if (BT == KingCrimsonEntity.DARK){
            return DARK;
        } else if (BT == KingCrimsonEntity.BLACK){
            return BLACK;
        } else if (BT == KingCrimsonEntity.BETA){
            return BETA;
        } else if (BT == KingCrimsonEntity.CONCEPT){
            return CONCEPT;
        } else if (BT == KingCrimsonEntity.RED){
            return RED;
        } else if (BT == KingCrimsonEntity.BLUE){
            return BLUE;
        }
        return PART_5_SKIN;
    }

    @Override
    public void render(KingCrimsonEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(KingCrimsonEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
