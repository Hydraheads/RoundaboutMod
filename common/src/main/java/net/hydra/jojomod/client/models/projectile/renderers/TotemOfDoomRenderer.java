package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.TotemOfDoomEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

public class TotemOfDoomRenderer extends EntityRenderer<TotemOfDoomEntity> {
    private final ItemRenderer itemRenderer;
    private final float scale;

    public TotemOfDoomRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = 1.0F;
    }

    @Override
    public void render(TotemOfDoomEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        float age = (float) entity.tickCount + partialTicks;

        poseStack.pushPose();

        // bobs up and down for the SPOOKY
        float bob = Mth.sin(age * 0.1F) * 0.08F;
        poseStack.translate(0.0D, 0.25D + bob, 0.0D);

        // wobbles at the start before losing the wobble
        float wobbleDamp = (float) Math.exp(-age / 12.0F);
        float wobblePitch = Mth.sin(age * 0.45F) * 15.0F * wobbleDamp;
        float wobbleRoll  = Mth.cos(age * 0.45F) * 15.0F * wobbleDamp;
        poseStack.mulPose(Axis.XP.rotationDegrees(wobblePitch));
        poseStack.mulPose(Axis.ZP.rotationDegrees(wobbleRoll));

        // rotation code
        // exponent is here to get the spin to slow down after a while
        float spinAngle = (4.0F * age) + (360.0F * (1.0F - (float) Math.exp(-age / 15.0F)));
        poseStack.mulPose(Axis.YP.rotationDegrees(spinAngle));

        // renderer like ThrownItemRenderer
        poseStack.scale(this.scale, this.scale, this.scale);
        this.itemRenderer.renderStatic(
                entity.getItem(),
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                entity.level(),
                entity.getId()
        );

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TotemOfDoomEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}