package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.item.BowlerHatItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class BowlerHatLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public BowlerHatLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {

        LivingEntity livent = entity;
        float heyFull = 1;

        ItemStack held = entity.getMainHandItem();
        ItemStack offHeld = entity.getOffhandItem();

        boolean isHoldingBowlerHat = (held.getItem() instanceof BowlerHatItem) || (offHeld.getItem() instanceof BowlerHatItem);

        if (!isHoldingBowlerHat || !ClientNetworking.getAppropriateConfig().bowlerHatSettings.enableHatRender) {
            return;
        }

        poseStack.pushPose();
        getParentModel().head.translateAndRotate(poseStack);
        // Apply additional transformations
        poseStack.translate(0.03F,-1.56F,-0.06F); //1 1
        // The first value goes to the right (negative) and left (positive)
        // The second value is correlated with up (negative) and down (positive)
        // the third is further and closer, positive to head towards bakc negative for away
        // Render your model here
        poseStack.scale(1.15F, 1.15F, 1.15F);
        boolean isHurt = livent.hurtTime > 0;
        float r = isHurt ? 1.0F : 1.0F;
        float g = isHurt ? 0.0F : 1.0F;
        float b = isHurt ? 0.0F : 1.0F;
        ModStrayModels.BOWLER_HAT.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, heyFull);
        poseStack.popPose();


    }
}

