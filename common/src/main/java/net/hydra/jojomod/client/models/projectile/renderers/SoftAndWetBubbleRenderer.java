package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetExplosiveBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetItemLaunchingBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.powers.TimeStop;
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
    private static final ResourceLocation SHOOTING_1 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_1.png");
    private static final ResourceLocation SHOOTING_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_2.png");
    private static final ResourceLocation SHOOTING_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_3.png");
    private static final ResourceLocation SHOOTING_4 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_4.png");
    private static final ResourceLocation ITEM_1 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/explosive_item_bubble_1.png");
    private static final ResourceLocation ITEM_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/explosive_item_bubble_2.png");
    private static final ResourceLocation ITEM_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/explosive_item_bubble_3.png");
    private static final ResourceLocation ITEM_4 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/explosive_item_bubble_4.png");
    private static final ResourceLocation MOB = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/bubble_plunder_mob.png");
    private static final ResourceLocation BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/bubble.png");
    private static final ResourceLocation GAS_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/gasoline_bubble.png");
    private static final ResourceLocation WATER_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/water_bubble.png");
    private static final ResourceLocation LAVA_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/lava_bubble.png");
    private static final ResourceLocation FIRE_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/fire_bubble.png");
    private static final ResourceLocation BLOOD_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/blood_bubble.png");
    private static final ResourceLocation BLUE_BLOOD_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/hemolymph_bubble.png");
    private static final ResourceLocation ENDER_BLOOD_BUBBLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/ender_blood_bubble.png");

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
            if (((TimeStop)entity.level()).inTimeStopRange(entity)){
                partialTicks = 0;
            }
            if (!(entity instanceof SoftAndWetPlunderBubbleEntity sp && sp.isPopPlunderBubbble())) {
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

                /**This ome is good*/
                Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
                if (normal.y > 0) {
                    /**This ome needs serial fixing*/
                    coursecorrect = new Vector3f(0.01f, 1f, 0.01f);
                    if (normal.y > 0.95) {
                        coursecorrect = new Vector3f(-0.577f, -0.577f, -0.577f);
                    }
                }

                float scaleIt = 0.23f;

                if (entity instanceof SoftAndWetPlunderBubbleEntity sp) {
                    int ls = sp.getLiquidStolen();
                    if (ls == 3) {
                        packedLight = 15728880;
                    } else if (sp.getActivated() && sp.getPlunderType() == PlunderTypes.OXYGEN.id){
                        packedLight = 15728880;
                    }
                    if (!sp.getHeldItem().isEmpty()) {
                        scaleIt = 0.33f;
                    }
                    if (sp.getEntityStolen() > 0 && sp.getPlunderType() == PlunderTypes.MOBS.id) {
                        scaleIt = 1.0f;
                    }
                } else if (entity instanceof SoftAndWetItemLaunchingBubbleEntity itemn){
                    scaleIt = 0.33f;
                }


                float size = (float) Math.min(scaleIt, (((float) entity.tickCount) + partialTicks) * (scaleIt * 0.1)); // Adjust to your needs
                if (entity instanceof SoftAndWetExplosiveBubbleEntity seb || entity instanceof SoftAndWetItemLaunchingBubbleEntity itemn){
                    size = scaleIt;
                }
                vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();

                if (entity instanceof SoftAndWetPlunderBubbleEntity plunder && !plunder.getHeldItem().isEmpty()) {
                    poseStack.translate(0, -0.12, 0);
                    this.itemRenderer.renderStatic(plunder.getHeldItem(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, ((Entity) entity).level(), ((Entity) entity).getId());
                } if (entity instanceof SoftAndWetItemLaunchingBubbleEntity launch && !launch.getHeldItem().isEmpty()) {
                    poseStack.translate(0, -0.19, 0);
                    this.itemRenderer.renderStatic(launch.getHeldItem(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, ((Entity) entity).level(), ((Entity) entity).getId());
                }

                poseStack.popPose();
                super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
            }
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
            } else if (ls == 4){
                return BLOOD_BUBBLE;
            } else if (ls == 5){
                return BLUE_BLOOD_BUBBLE;
            } else if (ls == 6){
                return ENDER_BLOOD_BUBBLE;
            }

            if (sp.getActivated() && sp.getPlunderType() == PlunderTypes.OXYGEN.id){
                return FIRE_BUBBLE;
            }
            if (sp.getActivated() && sp.getPlunderType() == PlunderTypes.MOBS.id){
                return MOB;
            }
            return TEXTURE;
        } else if (entity instanceof SoftAndWetItemLaunchingBubbleEntity seb){
            int div = seb.tickCount % 4;
            return switch (div) {
                case 1 -> ITEM_1;
                case 2 -> ITEM_2;
                case 3 -> ITEM_3;
                default -> ITEM_4;
            };
        } else if (entity instanceof SoftAndWetExplosiveBubbleEntity seb){
            int div = seb.tickCount % 4;
            return switch (div) {
                case 1 -> SHOOTING_2;
                case 2 -> SHOOTING_3;
                case 3 -> SHOOTING_4;
                default -> SHOOTING_1;
            };
        }
        return BUBBLE;
    }
}

