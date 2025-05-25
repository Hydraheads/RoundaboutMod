package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class D4CRenderer extends StandRenderer<D4CEntity> {

    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/d4c/manga.png");
    private static final ResourceLocation WONDER_FESTIVAL = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/d4c/wonder_festival.png");
    private static final ResourceLocation PROMO = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/d4c/promo.png");
    private static final ResourceLocation PROMO_L = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/d4c/promo_l.png");
    private static final ResourceLocation SPECIAL = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/d4c/special.png");
    private static final ResourceLocation GRAND = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/d4c/grand.png");

    public D4CRenderer(EntityRendererProvider.Context context) {
        super(context, new D4CModel<>(context.bakeLayer(ModEntityRendererClient.D4C_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(D4CEntity entity) {
        return switch (entity.getSkin()) {
            case (D4CEntity.MANGA_SKIN) -> MANGA_SKIN;
            case (D4CEntity.WONDER_FESTIVAL) -> WONDER_FESTIVAL;
            case (D4CEntity.PROMO) -> PROMO;
            case (D4CEntity.PROMO_L) -> PROMO_L;
            case (D4CEntity.SPECIAL) -> SPECIAL;
            case (D4CEntity.GRAND) -> GRAND;
            default -> MANGA_SKIN;
        };
    }

    @Override
    public void render(D4CEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
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
    protected RenderType getRenderType(D4CEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
