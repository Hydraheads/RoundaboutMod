package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.projectile.CinderellaVisageDisplayEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CinderellaVisageDisplayRenderer<T extends Entity>
        extends EntityRenderer<T> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25f;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;

    public CinderellaVisageDisplayRenderer(EntityRendererProvider.Context context, float f, boolean bl) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = f;
        this.fullBright = bl;
    }

    public CinderellaVisageDisplayRenderer(EntityRendererProvider.Context context) {
        this(context, 1.0f, false);
    }

    @Override
    protected int getBlockLightLevel(T entity, BlockPos blockPos) {
        return this.fullBright ? 15 : super.getBlockLightLevel(entity, blockPos);
    }

    public boolean shouldRender(T $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        return true;
    }
    @Override
    public void render(T entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (((TimeStop)entity.level()).inTimeStopRange(entity)){
            g = 0;
        }

        if (entity instanceof CinderellaVisageDisplayEntity cde) {
            float rsize = cde.getMaxSize();
            if (cde.getRenderSize() < rsize) {
                cde.setRenderSize(Math.min((float) (cde.getLastRenderSize() + (g * (float) cde.getAccrualRate())), rsize));
            }
        }

        ItemStack item = ((ItemSupplier)entity).getItem();
        poseStack.pushPose();
            poseStack.scale(this.scale, this.scale, this.scale);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        this.itemRenderer.renderStatic(((ItemSupplier)entity).getItem(), ItemDisplayContext.GROUND, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, ((Entity)entity).level(), ((Entity)entity).getId());
        poseStack.popPose();
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
