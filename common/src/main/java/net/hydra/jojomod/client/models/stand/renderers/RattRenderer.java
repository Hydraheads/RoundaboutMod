package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.layers.RattShoulderLayer;
import net.hydra.jojomod.client.models.stand.CinderellaModel;
import net.hydra.jojomod.client.models.stand.RattModel;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.util.gravity.GCUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RattRenderer extends StandRenderer<RattEntity> {

    private static final ResourceLocation ANIME_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/anime.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/manga.png");
    private static final ResourceLocation MELON_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/melon.png");
    private static final ResourceLocation SAND_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/sand.png");
    private static final ResourceLocation AZTEC_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/aztec.png");
    private static final ResourceLocation TOWER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/tower.png");
    private static final ResourceLocation SNOWY_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/snowy.png");
    private static final ResourceLocation GUARDIAN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/ratt/guardian.png");



    public RattRenderer(EntityRendererProvider.Context context) {
        super(context, new RattModel<>(context.bakeLayer(ModEntityRendererClient.RATT_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(RattEntity entity) {
        byte BT = entity.getSkin();
        switch (BT) {
            case RattEntity.MANGA_SKIN -> {return MANGA_SKIN;}
            case RattEntity.MELON_SKIN -> {return MELON_SKIN;}
            case RattEntity.SAND_SKIN -> {return SAND_SKIN;}
            case RattEntity.AZTEC_SKIN -> {return AZTEC_SKIN;}
            case RattEntity.TOWER_SKIN -> {return TOWER_SKIN;}
            case RattEntity.SNOWY_SKIN -> {return SNOWY_SKIN;}
            case RattEntity.GUARDIAN_SKIN -> {return GUARDIAN_SKIN;}
            default -> {return ANIME_SKIN;}
        }
    }


    @Override
    public void render(RattEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(0.75F,0.75F,0.75F);
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(RattEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
