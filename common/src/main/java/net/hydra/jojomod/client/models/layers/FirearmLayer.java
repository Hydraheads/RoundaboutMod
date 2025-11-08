package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.SnubnoseRevolverItem;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class FirearmLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public FirearmLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (entity instanceof Player player) {
            boolean $$18 = !entity.isInvisible();
            boolean $$19 = !$$18 && !entity.isInvisibleTo(player);
            if (!$$18) {
                return;
            }
        }
        LivingEntity livent = entity;
        float heyFull = 1;

        ItemStack held = entity.getMainHandItem();
        ItemStack offHeld = entity.getOffhandItem();

        boolean isHoldingFirearmRight = (held.getItem() instanceof FirearmItem);
        boolean isHoldingFirearmLeft = (offHeld.getItem() instanceof FirearmItem);

        if (!isHoldingFirearmRight && !isHoldingFirearmLeft || !(ConfigManager.getClientConfig() != null && ConfigManager.getClientConfig().enableFirearmRender)) {
            return;
        }

        poseStack.pushPose();
        if (isHoldingFirearmRight && held.getItem() instanceof SnubnoseRevolverItem) {
            getParentModel().rightArm.translateAndRotate(poseStack);
            poseStack.translate(-0.032F,0.55F,-0.66F);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(1.0F, 1.0F, 1.0F);
            boolean isHurt = livent.hurtTime > 0;
            float r = isHurt ? 1.0F : 1.0F;
            float g = isHurt ? 0.0F : 1.0F;
            float b = isHurt ? 0.0F : 1.0F;
            ModStrayModels.SNUBNOSE_REVOLVER_MODEL.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, heyFull);
            poseStack.popPose();
        } else if (isHoldingFirearmLeft && offHeld.getItem() instanceof SnubnoseRevolverItem) {
            getParentModel().leftArm.translateAndRotate(poseStack);
            poseStack.translate(0.032F,0.55F,-0.66F);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(1.0F, 1.0F, 1.0F);
            boolean isHurt = livent.hurtTime > 0;
            float r = isHurt ? 1.0F : 1.0F;
            float g = isHurt ? 0.0F : 1.0F;
            float b = isHurt ? 0.0F : 1.0F;
            ModStrayModels.SNUBNOSE_REVOLVER_MODEL.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, heyFull);
            poseStack.popPose();
        }
    }
}

