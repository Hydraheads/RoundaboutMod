package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TheWorldRenderer extends StandRenderer<TheWorldEntity> {

    private static final ResourceLocation PART_3_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world.png");
    private static final ResourceLocation PART_3_MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world_manga.png");
    private static final ResourceLocation OVA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world_ova.png");
    private static final ResourceLocation HERITAGE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world_heritage.png");
    private static final ResourceLocation BLACK_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world_black.png");
    private static final ResourceLocation PART_7_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world_part_7.png");
    private static final ResourceLocation PART_7_BLUE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world_part_7_blue.png");
    private static final ResourceLocation OVER_HEAVEN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world_over_heaven.png");
    private static final ResourceLocation DARK_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/the_world_dark.png");
    public TheWorldRenderer(EntityRendererProvider.Context context) {
        super(context, new TheWorldModel<>(context.bakeLayer(ModEntityRendererClient.THE_WORLD_LAYER)),0f);
        this.addLayer(new TheWorldOverHeavenEyeLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TheWorldEntity entity) {
        byte BT = entity.getSkin();
        if (BT == TheWorldEntity.PART_3_SKIN){
            return PART_3_SKIN;
        } else if (BT == TheWorldEntity.MANGA_SKIN){
            return PART_3_MANGA_SKIN;
        } else if (BT == TheWorldEntity.OVA_SKIN){
            return OVA_SKIN;
        } else if (BT == TheWorldEntity.HERITAGE_SKIN){
            return HERITAGE_SKIN;
        } else if (BT == TheWorldEntity.BLACK_SKIN){
            return BLACK_SKIN;
        } else if (BT == TheWorldEntity.DARK_SKIN){
            return DARK_SKIN;
        } else if (BT == TheWorldEntity.PART_7_SKIN){
            return PART_7_SKIN;
        } else if (BT == TheWorldEntity.PART_7_BLUE){
            return PART_7_BLUE;
        } else if (BT == TheWorldEntity.OVER_HEAVEN){
            return OVER_HEAVEN;
        }
        return PART_3_SKIN;
    }

    @Override
    public void render(TheWorldEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (mobEntity.isBaby()){
            matrixStack.scale(0.5f,0.5f,0.5f);
        } else {
            matrixStack.scale(0.87f,0.87f,0.87f);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(TheWorldEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }


}
