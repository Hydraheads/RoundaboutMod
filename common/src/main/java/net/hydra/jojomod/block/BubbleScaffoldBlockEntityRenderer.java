package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class BubbleScaffoldBlockEntityRenderer implements BlockEntityRenderer<BubbleScaffoldBlockEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/bubble_plunder.png");
    private static final ResourceLocation HEART_ATTACK_MINI = new ResourceLocation(Roundabout.MOD_ID, "textures/particle/heart_attack_mini.png");
    public BubbleScaffoldBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
        this.scale = 0.55f;
    }
    private final float scale;
    public int bubbleCount = 7;

    public void render(BubbleScaffoldBlockEntity bubbleScaffoldBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (bubbleScaffoldBlockEntity.bubbleList == null){
            bubbleScaffoldBlockEntity.bubbleList = new ArrayList<>();
        }
        if (bubbleScaffoldBlockEntity.bubbleList.isEmpty()){
            for (var i = 0; i< bubbleCount; i++) {
                bubbleScaffoldBlockEntity.bubbleList.add(new Vec3(
                        Math.random()*0.8+0.1F,
                        Math.random()*0.6+0.5F,
                        Math.random()*0.8+0.1F
                ));
            }
        }


        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {

            if (((TimeStop)bubbleScaffoldBlockEntity.getLevel()).inTimeStopRange(bubbleScaffoldBlockEntity.getBlockPos())){
                partialTick = 0;
            }
            for (Vec3 value : bubbleScaffoldBlockEntity.bubbleList) {
                ClientUtil.pushPoseAndCooperate(poseStack,6);


                poseStack.translate(value.x,value.y,value.z);
                // Orient the texture
                poseStack.scale(this.scale, this.scale, this.scale);
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack.translate(0, SoftAndWetPlunderBubbleEntity.eHeight / 2, 0);

                // Draw flat quad here
                VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(getBubbleTextureLocation(bubbleScaffoldBlockEntity)));
                Matrix4f matrix = poseStack.last().pose();
                Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
                normal.normalize();

                Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
                if (normal.y > 0) {
                    coursecorrect = new Vector3f(0.01f, 1f, 0.01f);
                    if (normal.y > 0.95) {
                        coursecorrect = new Vector3f(-0.577f, -0.577f, -0.577f);
                    }
                }

                float scaleIt = 0.23f;

                float size = (float) Math.min(scaleIt, (((float) bubbleScaffoldBlockEntity.getTickCount()) + partialTick) * (scaleIt * 0.1)); // Adjust to your needs
                vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
                vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();


                ClientUtil.popPoseAndCooperate(poseStack,6);
            }
        }
    }


    public ResourceLocation getBubbleTextureLocation(BubbleScaffoldBlockEntity bubbleScaffoldBlockEntity) {
        BlockState bs = bubbleScaffoldBlockEntity.getBlockState();
        if (bs.hasProperty(BlockStateProperties.TRIGGERED)){
            if (bs.getValue(BlockStateProperties.TRIGGERED)){
                return HEART_ATTACK_MINI;
            }
        }
       return TEXTURE;
    }
}
