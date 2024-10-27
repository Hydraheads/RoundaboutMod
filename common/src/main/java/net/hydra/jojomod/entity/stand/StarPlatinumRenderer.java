package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class StarPlatinumRenderer<T extends StandEntity> extends StandRenderer<StarPlatinumEntity> {

    private static final ResourceLocation PART_3_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum.png");
    private static final ResourceLocation PART_3_MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_purple.png");
    private static final ResourceLocation OVA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_ova.png");
    private static final ResourceLocation GREEN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_green.png");
    private static final ResourceLocation BASEBALL_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_baseball.png");
    private static final ResourceLocation PART_4_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_part_4.png");
    private static final ResourceLocation PART_6_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_part_6.png");
    private static final ResourceLocation PART_6_ALT_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_part_6_alt.png");
    private static final ResourceLocation ATOMIC_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_atomic.png");
    public StarPlatinumRenderer(EntityRendererProvider.Context context) {
        super(context, new StarPlatinumModel<>(context.bakeLayer(ModEntityRendererClient.STAR_PLATINUM_LAYER)),0f);
        this.addLayer(new StarPlatinumEyeLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(StarPlatinumEntity entity) {
        byte BT = entity.getSkin();
        if (BT == StarPlatinumEntity.PART_3_SKIN){
            return PART_3_SKIN;
        } else if (BT == StarPlatinumEntity.PART_3_MANGA_SKIN){
            return PART_3_MANGA_SKIN;
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
        } else if (BT == StarPlatinumEntity.PART_6_ALT_SKIN){
            return PART_6_ALT_SKIN;
        } else if (BT == StarPlatinumEntity.ATOMIC_SKIN){
            return ATOMIC_SKIN;
        }
        return PART_3_SKIN;
    }

    @Override
    public void render(StarPlatinumEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (!(mobEntity.getUser() != null && Minecraft.getInstance().player != null && mobEntity.getScoping() &&
                mobEntity.getUser().is(Minecraft.getInstance().player) &&
                Minecraft.getInstance().options.getCameraType().isFirstPerson())) {
            if (mobEntity.isBaby()) {
                matrixStack.scale(0.5f, 0.5f, 0.5f);
            } else {
                matrixStack.scale(0.87f, 0.87f, 0.87f);
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
