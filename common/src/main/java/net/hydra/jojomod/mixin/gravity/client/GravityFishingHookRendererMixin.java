package net.hydra.jojomod.mixin.gravity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHookRenderer.class)
public abstract class GravityFishingHookRendererMixin extends EntityRenderer<FishingHook> {
    @Shadow
    @Final
    private static RenderType RENDER_TYPE;

    protected GravityFishingHookRendererMixin(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Shadow
    private static void vertex(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int light, float x, int y, int u, int v) {}

    @Shadow
    private static void stringVertex(float x, float y, float z, VertexConsumer buffer, PoseStack.Pose normal, float f, float g) {}

    @Shadow
    private static float fraction(int value, int max) {return 0.0F;}


    // TODO mixin fishing hook rendering in a better way
    @Inject(
            method = "Lnet/minecraft/client/renderer/entity/FishingHookRenderer;render(Lnet/minecraft/world/entity/projectile/FishingHook;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void inject_render(FishingHook fishingBobberEntity, float yaw, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, CallbackInfo ci) {
        Player playerEntity = fishingBobberEntity.getPlayerOwner();
        if (playerEntity == null) return;

        Direction gravityDirection = GravityAPI.getGravityDirection(playerEntity);
        if (gravityDirection == Direction.DOWN) return;

        ci.cancel();

        matrixStack.pushPose();
        matrixStack.pushPose();
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose entry = matrixStack.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RENDER_TYPE);
        vertex(vertexConsumer, matrix4f, matrix3f, light, 0.0F, 0, 0, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, light, 1.0F, 0, 1, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, light, 1.0F, 1, 1, 0);
        vertex(vertexConsumer, matrix4f, matrix3f, light, 0.0F, 1, 0, 0);
        matrixStack.popPose();
        int armOffset = playerEntity.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        ItemStack itemStack = playerEntity.getMainHandItem();
        if (!itemStack.is(Items.FISHING_ROD)) {
            armOffset = -armOffset;
        }

        float handSwingProgress = playerEntity.getAttackAnim(tickDelta);
        float sinHandSwingProgress = Mth.sin(Mth.sqrt(handSwingProgress) * 3.1415927F);
        float radBodyYaw = Mth.lerp(tickDelta, playerEntity.yBodyRotO, playerEntity.yBodyRot) * 0.017453292F;
        double sinBodyYaw = Mth.sin(radBodyYaw);
        double cosBodyYaw = Mth.cos(radBodyYaw);
        double scaledArmOffset = (double) armOffset * 0.35D;
        Vec3 lineStart;
        if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && playerEntity == Minecraft.getInstance().player) {
            Vec3 lineOffset = RotationUtil.vecWorldToPlayer(this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) armOffset * 0.525F, -0.1F), gravityDirection);
            lineOffset = lineOffset.scale(960.0D / this.entityRenderDispatcher.options.fov().get());
            lineOffset = lineOffset.yRot(sinHandSwingProgress * 0.5F);
            lineOffset = lineOffset.xRot(-sinHandSwingProgress * 0.7F);
            lineStart = new Vec3(
                    Mth.lerp(tickDelta, playerEntity.xo, playerEntity.getX()),
                    Mth.lerp(tickDelta, playerEntity.yo, playerEntity.getY()),
                    Mth.lerp(tickDelta, playerEntity.zo, playerEntity.getZ())
            ).add(RotationUtil.vecPlayerToWorld(lineOffset.add(0.0D, playerEntity.getEyeHeight(), 0.0D), gravityDirection));
        }
        else {
            lineStart = new Vec3(
                    Mth.lerp(tickDelta, playerEntity.xo, playerEntity.getX()),
                    playerEntity.yo + (playerEntity.getY() - playerEntity.yo) * tickDelta,
                    Mth.lerp(tickDelta, playerEntity.zo, playerEntity.getZ())
            ).add(RotationUtil.vecPlayerToWorld(
                    -cosBodyYaw * scaledArmOffset - sinBodyYaw * 0.8D,
                    playerEntity.getEyeHeight() + (playerEntity.isCrouching() ? -0.1875D : 0.0D) - 0.45D,
                    -sinBodyYaw * scaledArmOffset + cosBodyYaw * 0.8D,
                    gravityDirection
            ));
        }

        double bobberX = Mth.lerp(tickDelta, fishingBobberEntity.xo, fishingBobberEntity.getX());
        double bobberY = Mth.lerp(tickDelta, fishingBobberEntity.yo, fishingBobberEntity.getY()) + 0.25D;
        double bobberZ = Mth.lerp(tickDelta, fishingBobberEntity.zo, fishingBobberEntity.getZ());
        float relX = (float) (lineStart.x - bobberX);
        float relY = (float) (lineStart.y - bobberY);
        float relZ = (float) (lineStart.z - bobberZ);
        VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderType.lineStrip());
        PoseStack.Pose entry2 = matrixStack.last();

        for (int i = 0; i <= 16; ++i) {
            stringVertex(relX, relY, relZ, vertexConsumer2, entry2, fraction(i, 16), fraction(i + 1, 16));
        }

        matrixStack.popPose();
        super.render(fishingBobberEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }
}