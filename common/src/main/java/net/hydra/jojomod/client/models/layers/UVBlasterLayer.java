package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.item.UltravioletBlasterItem;
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

public class UVBlasterLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public UVBlasterLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
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
        if (((IEntityAndData)entity).roundabout$getTrueInvisibility() > - 1 && !ClientUtil.checkIfClientCanSeeInvisAchtung() || ((IEntityAndData)entity).roundabout$getTrueInvisibilityManhattan() < 1 && ClientUtil.checkIfClientCanSeeMobsForWindVision())
            return;
        LivingEntity livent = entity;
        float heyFull = 1;

        ItemStack held = entity.getMainHandItem();
        ItemStack offHeld = entity.getOffhandItem();

        boolean isHoldingBowlerHat = (held.getItem() instanceof UltravioletBlasterItem) || (offHeld.getItem() instanceof UltravioletBlasterItem);

        if (!isHoldingBowlerHat || !(ConfigManager.getClientConfig() != null &&
                ConfigManager.getClientConfig().enableBowlerHatRender)) {
            return;
        }

        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        poseStack.translate(0.03F,-1.56F,-0.06F);
        poseStack.scale(1.15F, 1.15F, 1.15F);
        boolean isHurt = livent.hurtTime > 0;
        float r = isHurt ? 1.0F : 1.0F;
        float g = isHurt ? 0.0F : 1.0F;
        float b = isHurt ? 0.0F : 1.0F;
        ModStrayModels.UV_BLASTER.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, heyFull);
        ModStrayModels.UV_BLASTER.render2(livent, partialTicks, poseStack, bufferSource, 15728880,
                r, g, b, heyFull);
        poseStack.popPose();
    }
}

