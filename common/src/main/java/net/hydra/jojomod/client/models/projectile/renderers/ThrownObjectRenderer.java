package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class ThrownObjectRenderer<T extends Entity>
        extends EntityRenderer<T> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25f;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;

    public ThrownObjectRenderer(EntityRendererProvider.Context context, float f, boolean bl) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = f;
        this.fullBright = bl;
    }

    public ThrownObjectRenderer(EntityRendererProvider.Context context) {
        this(context, 1.0f, false);
    }

    @Override
    protected int getBlockLightLevel(T entity, BlockPos blockPos) {
        return this.fullBright ? 15 : super.getBlockLightLevel(entity, blockPos);
    }

    @Override
    public void render(T entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        ItemStack item = ((ItemSupplier)entity).getItem();
        if (((Entity)entity).tickCount < 2 && this.entityRenderDispatcher.camera.getEntity().distanceToSqr((Entity)entity) < 12.25) {
            return;
        }
        poseStack.pushPose();

        ThrownObjectEntity thrownObjectEntity = (ThrownObjectEntity)entity;
        if ( thrownObjectEntity.getItem().getItem() instanceof AnubisItem ) {

            if ( ((TimeStop)entity.level()).inTimeStopRange(entity)) {
                f = 0;
            }

            Vec3 dir = entity.getDeltaMovement().normalize();
            float rot =(float) (Math.atan2(dir.x,dir.z) * Mth.RAD_TO_DEG + 90);

            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,rot),0,0,0);

            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,(entity.tickCount+f)*-20),0,0,0);

            ItemStack stack = new ItemStack(ModItems.ANUBIS_ITEM);
            stack.getOrCreateTag().putFloat("CustomModelData", 1F);
            this.itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, entity.level(), entity.getId());
        } else {

            if (MainUtil.isThrownBlockItem(item.getItem()) &&
                    entity instanceof ThrownObjectEntity TOE && (TOE.getStyle() == ThrownObjectEntity.SPTHROW || TOE.getStyle() == ThrownObjectEntity.TWTHROW)){
                poseStack.scale((float) (this.scale*3), (float) (this.scale*3), (float) (this.scale*3));
            } else {
                poseStack.scale(this.scale, this.scale, this.scale);
                poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            }
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));

            this.itemRenderer.renderStatic(((ItemSupplier) entity).getItem(), ItemDisplayContext.GROUND, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, ((Entity) entity).level(), ((Entity) entity).getId());

        }
        poseStack.popPose();
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}


