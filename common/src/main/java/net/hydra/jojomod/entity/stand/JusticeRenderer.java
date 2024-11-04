package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class JusticeRenderer<T extends StandEntity> extends StandRenderer<JusticeEntity> {
    public JusticeRenderer(EntityRendererProvider.Context context) {
        super(context, new JusticeModel<>(context.bakeLayer(ModEntityRendererClient.JUSTICE_LAYER)),0f);
    }




    private static final ResourceLocation PART_3_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice.png");
    private static final ResourceLocation PART_3_MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_manga.png");
    private static final ResourceLocation OVA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_ova.png");
    private static final ResourceLocation BOGGED_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_bogged.png");
    private static final ResourceLocation SKELETON = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_skeleton.png");
    private static final ResourceLocation STRAY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_stray.png");
    private static final ResourceLocation WITHER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_wither.png");
    private static final ResourceLocation TAROT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_tarot.png");
    private static final ResourceLocation FLAMED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_flamed.png");


    @Override
    public ResourceLocation getTextureLocation(JusticeEntity entity) {
        byte BT = entity.getSkin();
        if (BT == JusticeEntity.PART_3_SKIN){
            return PART_3_SKIN;
        } else if (BT == JusticeEntity.MANGA_SKIN){
            return PART_3_MANGA_SKIN;
        } else if (BT == JusticeEntity.OVA_SKIN){
            return OVA_SKIN;
        } else if (BT == JusticeEntity.BOGGED){
            return BOGGED_SKIN;
        } else if (BT == JusticeEntity.SKELETON_SKIN){
            return SKELETON;
        } else if (BT == JusticeEntity.STRAY_SKIN){
            return STRAY;
        } else if (BT == JusticeEntity.FLAMED){
            return FLAMED;
        } else if (BT == JusticeEntity.WITHER){
            return WITHER;
        } else if (BT == JusticeEntity.TAROT){
            return TAROT;
        }
        return PART_3_SKIN;
    }
    @Override
    public void render(JusticeEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(2.0f,2.0f,2.0f);
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(JusticeEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}

