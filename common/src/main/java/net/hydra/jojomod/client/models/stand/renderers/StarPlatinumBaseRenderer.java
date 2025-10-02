package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class StarPlatinumBaseRenderer<T extends StandEntity> extends StandRenderer<StarPlatinumEntity>  {

    public static final ResourceLocation PART_3_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/anime.png");
    public static final ResourceLocation PART_3_MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/manga.png");
    public static final ResourceLocation PART_3_MANGA_PURPLE_SKIN= new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/purple.png");

    public static final ResourceLocation OVA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/ova.png");
    public static final ResourceLocation GREEN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/green.png");
    public static final ResourceLocation GREEN_2 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/green_2.png");
    public static final ResourceLocation BASEBALL_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/baseball.png");
    public static final ResourceLocation PART_4_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/part_4.png");
    public static final ResourceLocation PART_6_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/part_6.png");
    public static final ResourceLocation ATOMIC_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/atomic.png");
    public static final ResourceLocation MANGA_FIRST_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/first_summon.png");
    public static final ResourceLocation JOJONIUM_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/jojonium.png");
    public static final ResourceLocation BETA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/beta.png");
    public static final ResourceLocation ARCADE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/arcade.png");
    public static final ResourceLocation ARCADE_SKIN_2 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/arcade_2.png");
    public static final ResourceLocation FOUR_DEE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/4d.png");
    public static final ResourceLocation JOJOVELLER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/jojoveller.png");
    public static final ResourceLocation CROP = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/crop.png");
    public static final ResourceLocation VOLUME_39 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/volume_39.png");
    public static final ResourceLocation JUMP_13 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/jump_13.png");
    public static final ResourceLocation TREE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/tree.png");
    public static final ResourceLocation NETHER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum/nether.png");
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
        } else if (BT == StarPlatinumEntity.GREEN_2){
            return GREEN_2;
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
        } else if (BT == StarPlatinumEntity.JOJONIUM_SKIN){
            return JOJONIUM_SKIN;
        } else if (BT == StarPlatinumEntity.BETA){
            return BETA_SKIN;
        } else if (BT == StarPlatinumEntity.ARCADE){
            return ARCADE_SKIN;
        } else if (BT == StarPlatinumEntity.FOUR_DEE){
            return FOUR_DEE_SKIN;
        } else if (BT == StarPlatinumEntity.JOJOVELLER){
            return JOJOVELLER;
        } else if (BT == StarPlatinumEntity.CROP){
            return CROP;
        } else if (BT == StarPlatinumEntity.VOLUME_39){
            return VOLUME_39;
        } else if (BT == StarPlatinumEntity.JUMP_13){
            return JUMP_13;
        } else if (BT == StarPlatinumEntity.ARCADE_2){
            return ARCADE_SKIN_2;
        } else if (BT == StarPlatinumEntity.TREE){
            return TREE;
        } else if (BT == StarPlatinumEntity.NETHER){
            return NETHER;
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
