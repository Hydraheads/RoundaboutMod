package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class StarPlatinumBaseRenderer<T extends StandEntity> extends StandRenderer<StarPlatinumEntity>  {

    public static final ResourceLocation PART_3_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum.png");
    public static final ResourceLocation PART_3_MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_manga.png");
    public static final ResourceLocation PART_3_MANGA_PURPLE_SKIN= new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_purple.png");
    public static final ResourceLocation OVA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_ova.png");
    public static final ResourceLocation GREEN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_green.png");
    public static final ResourceLocation BASEBALL_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_baseball.png");
    public static final ResourceLocation PART_4_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_part_4.png");
    public static final ResourceLocation PART_6_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_part_6.png");
    public static final ResourceLocation ATOMIC_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_atomic.png");
    public static final ResourceLocation MANGA_FIRST_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_first_summon.png");
    public StarPlatinumBaseRenderer(EntityRendererProvider.Context context, StandModel<StarPlatinumEntity> entityModel, float f) {
        super(context, entityModel,f);
        this.addLayer(new StarPlatinumEyeLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(StarPlatinumEntity entity) {
        byte BT = entity.getSkin();
        if (BT == StarPlatinumEntity.PART_3_SKIN){
            return PART_3_SKIN;
        } else if (BT == StarPlatinumEntity.MANGA_SKIN){
            return PART_3_MANGA_SKIN;
        } else if (BT == StarPlatinumEntity.MANGA_PURPLE_SKIN){
            return PART_3_MANGA_PURPLE_SKIN;
        } else if (BT == StarPlatinumEntity.OVA_SKIN){
            return OVA_SKIN;
        } else if (BT == StarPlatinumEntity.GREEN_SKIN){
            return GREEN_SKIN;
        } else if (BT == StarPlatinumEntity.BASEBALL_SKIN){
            return BASEBALL_SKIN;
        } else if (BT == StarPlatinumEntity.PART_4_SKIN){
            return PART_4_SKIN;
        } else if (BT == StarPlatinumEntity.PART_6_SKIN){
            return PART_6_SKIN;
        } else if (BT == StarPlatinumEntity.ATOMIC_SKIN){
            return ATOMIC_SKIN;
        } else if (BT == StarPlatinumEntity.FIRST_SKIN){
            return MANGA_FIRST_SKIN;
        }
        return PART_3_SKIN;
    }

    @Override
    public void render(StarPlatinumEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (!(mobEntity.getUser() != null && Minecraft.getInstance().player != null && mobEntity.getScoping() &&
                mobEntity.getUser().is(Minecraft.getInstance().player) &&
                Minecraft.getInstance().options.getCameraType().isFirstPerson())) {
            float factor = 0.5F + (mobEntity.getSizePercent()/2);
            if (mobEntity.isBaby()) {
                matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
            } else {
                matrixStack.scale(0.87f*factor, 0.87f*factor, 0.87f*factor);
            }
            super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        }
    }

    @Nullable
    @Override
    protected RenderType getRenderType(StarPlatinumEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
