package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.WhitesnakeModel;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class WhitesnakeRenderer extends StandRenderer<WhitesnakeEntity> {
    private static final ResourceLocation[] SKINS = {
            texture("anime"),
            texture("manga"),
            texture("anime_purple"),
            texture("anime_green"),
            texture("anime_yellow"),
            texture("anime_aqua"),
            texture("manga_purple"),
            texture("manga_red"),
            texture("gold"),
            texture("silver"),
            texture("cotton_candy"),
            texture("asbr"),
            texture("agogo"),
            texture("dark"),
            texture("sour_candy"),
            texture("edgy_gold"),
            texture("gold_trimmed"),
            texture("sandsnake"),
            texture("eoh"),
            texture("fleshsnake")
    };
    private final WhitesnakeDisguiseRenderer disguiseRenderer;

    public WhitesnakeRenderer(EntityRendererProvider.Context context) {
        super(context, new WhitesnakeModel(context.bakeLayer(ModEntityRendererClient.WHITESNAKE_LAYER)), 0.0F);
        disguiseRenderer = new WhitesnakeDisguiseRenderer(context);
        addLayer(new WhitesnakeDiscHeldItemLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(WhitesnakeEntity entity) {
        int skin = entity.getSkin();
        return skin >= 0 && skin < SKINS.length ? SKINS[skin] : SKINS[0];
    }

    private static ResourceLocation texture(String name) {
        return new ResourceLocation(Roundabout.MOD_ID, "textures/stand/whitesnake/" + name + ".png");
    }

    @Override
    public void render(WhitesnakeEntity entity, float yaw, float tickDelta, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight) {
        if (entity.isDisguised() && (!entity.isDisguiseFlickering(tickDelta)
                || entity.shouldRenderDisguiseDuringFlicker(tickDelta))) {
            disguiseRenderer.render(entity, yaw, tickDelta, poseStack, buffers, packedLight);
            return;
        }
        float factor = 0.5F + entity.getSizePercent() / 2.0F;
        float scale = entity.isBaby() ? 0.5F * factor : 0.87F * factor;
        poseStack.scale(scale, scale, scale);
        super.render(entity, yaw, tickDelta, poseStack, buffers, packedLight);
    }

    @Override
    public float getStandOpacity(WhitesnakeEntity entity) {
        if (entity.isRemoteControlled()) return 1.0F;
        return super.getStandOpacity(entity);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(WhitesnakeEntity entity, boolean showBody,
                                       boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
