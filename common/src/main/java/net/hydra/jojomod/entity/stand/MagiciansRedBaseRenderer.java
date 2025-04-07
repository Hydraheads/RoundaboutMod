package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.MagiciansRedSpinEffectLayer;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class MagiciansRedBaseRenderer<M extends StandEntity> extends StandRenderer<MagiciansRedEntity> {

    private static final ResourceLocation PART_3_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red.png");
    private static final ResourceLocation BLUE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_blue.png");
    private static final ResourceLocation PURPLE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_purple.png");
    private static final ResourceLocation GREEN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_green.png");
    private static final ResourceLocation DREAD_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_dread.png");
    private static final ResourceLocation DREAD_BEAST_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_chagaroth.png");
    private static final ResourceLocation BLUE_ACE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_blue_ace.png");
    private static final ResourceLocation MAGMA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_magma.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_manga.png");
    private static final ResourceLocation LIGHTER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_lighter.png");
    private static final ResourceLocation OVA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_red_ova.png");
    private static final ResourceLocation SIDEKICK_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/magicians_sidekicks.png");
    public MagiciansRedBaseRenderer(EntityRendererProvider.Context context, StandModel root) {
        super(context, root,0f);
        this.addLayer(new MagiciansRedSpinEffectLayer<>(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(MagiciansRedEntity entity) {
        byte BT = entity.getSkin();
        if (BT == MagiciansRedEntity.BLUE_SKIN|| BT == MagiciansRedEntity.BLUE_ABLAZE){
            return BLUE_SKIN;
        } else if (BT == MagiciansRedEntity.PURPLE_SKIN|| BT == MagiciansRedEntity.PURPLE_ABLAZE){
            return PURPLE_SKIN;
        } else if (BT == MagiciansRedEntity.GREEN_SKIN|| BT == MagiciansRedEntity.GREEN_ABLAZE){
            return GREEN_SKIN;
        } else if (BT == MagiciansRedEntity.DREAD_SKIN|| BT == MagiciansRedEntity.DREAD_ABLAZE){
            return DREAD_SKIN;
        } else if (BT == MagiciansRedEntity.DREAD_BEAST_SKIN){
            return DREAD_BEAST_SKIN;
        } else if (BT == MagiciansRedEntity.BLUE_ACE_SKIN){
            return BLUE_ACE_SKIN;
        } else if (BT == MagiciansRedEntity.MAGMA_SKIN){
            return MAGMA_SKIN;
        } else if (BT == MagiciansRedEntity.MANGA_SKIN){
            return MANGA_SKIN;
        } else if (BT == MagiciansRedEntity.LIGHTER_SKIN || BT == MagiciansRedEntity.LIGHTER_ABLAZE){
            return LIGHTER_SKIN;
        } else if (BT == MagiciansRedEntity.OVA_SKIN){
            return OVA_SKIN;
        } else if (BT == MagiciansRedEntity.SIDEKICK){
            return SIDEKICK_SKIN;
        }
            return PART_3_SKIN;
    }

    @Override
    public void render(MagiciansRedEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public boolean skipLighting(MagiciansRedEntity mr){
        return mr.emitsLight();
    }

    @Nullable
    @Override
    protected RenderType getRenderType(MagiciansRedEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }


    @Override
    protected void setupRotations(MagiciansRedEntity $$0, PoseStack $$1, float $$2, float $$3, float $$4) {
        super.setupRotations($$0,$$1,$$2,$$3,$$4);
        if ($$0.getOffsetType() == OffsetIndex.LOOSE  && !$$0.getDisplay()) {
            $$1.mulPose(Axis.XP.rotationDegrees(-90.0F - $$0.getXRot()));
        }
    }
}
