package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SoftAndWetBubbleRenderer extends EntityRenderer<SoftAndWetBubbleEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/bubble_plunder.png");
    private static final ResourceLocation BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/bubble.png");
    private static final ResourceLocation GAS_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/gasoline_bubble.png");
    private static final ResourceLocation WATER_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/water_bubble.png");
    private static final ResourceLocation LAVA_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/lava_bubble.png");

    private final float scale;
    private final ItemRenderer itemRenderer;

    public SoftAndWetBubbleRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = scale;
    }

    public SoftAndWetBubbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = 1f;
    }

    @Override
    public void render(SoftAndWetBubbleEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            poseStack.pushPose();

            // Orient the texture
            poseStack.scale(this.scale, this.scale, this.scale);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.translate(0, entity.getBbHeight() / 2, 0);

            // Draw flat quad here
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
            Matrix4f matrix = poseStack.last().pose();
            Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
            normal.normalize();

            Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
            if (normal.y > 0){
               coursecorrect = new Vector3f( 0.01f, 1f, 0.01f);
            }

            float scaleIt = 0.23f;
            if (entity instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.getHeldItem().isEmpty()){
                scaleIt = 0.33f;
            }

            float size = (float) Math.min(scaleIt, (((float)entity.tickCount)+partialTicks)*(scaleIt*0.1)); // Adjust to your needs
            vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();

            if (entity instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.getHeldItem().isEmpty()){
                poseStack.translate(0,-0.12,0);
                this.itemRenderer.renderStatic(plunder.getHeldItem(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, ((Entity)entity).level(), ((Entity)entity).getId());
            }

            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(SoftAndWetBubbleEntity entity) {
        if (entity instanceof SoftAndWetPlunderBubbleEntity sp){
            int ls = sp.getLiquidStolen();
            if (ls == 1){
                return GAS_BUBBLE;
            } else if (ls == 2){
                return WATER_BUBBLE;
            } else if (ls == 3){
                return LAVA_BUBBLE;
            }
            return TEXTURE;
        }
        return BUBBLE;
    }
}

