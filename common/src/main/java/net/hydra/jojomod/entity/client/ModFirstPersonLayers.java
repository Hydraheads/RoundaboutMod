package net.hydra.jojomod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ModFirstPersonLayers {
    /**Mojang decided to make literally zero layer applications on first person, so to render beams like guardian
     * ones without arbitrarily making them entities, we have to use our own custom coded renderer*/

    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/locacaca_layers/locacaca_beam.png");
    public static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(BEAM_LOCATION);
    private static void vertex(
            VertexConsumer $$0, Matrix4f $$1, Matrix3f $$2, float $$3, float $$4, float $$5, int $$6, int $$7, int $$8, float $$9, float $$10
    ) {
        $$0.vertex($$1, $$3, $$4, $$5)
                .color($$6, $$7, $$8, 255)
                .uv($$9, $$10)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal($$2, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }


    private static Vec3 getPosition(LivingEntity $$0, double $$1, float $$2) {
        double $$3 = Mth.lerp((double)$$2, $$0.xOld, $$0.getX());
        double $$4 = Mth.lerp((double)$$2, $$0.yOld, $$0.getY()) + $$1;
        double $$5 = Mth.lerp((double)$$2, $$0.zOld, $$0.getZ());
        return new Vec3($$3, $$4, $$5);
    }


    public static float getAttackAnimationScale(float $$0, LivingEntity ent, ItemStack item) {
        return ((float)( ent.getUseItem().getUseDuration() - ent.getUseItemRemainingTicks()) + $$0) / (float)item.getUseDuration();
    }

    public static void render(PoseStack ps, MultiBufferSource mb, int packedLight, LivingEntity player, float deltaTime){
        /**
        if (player.isUsingItem() && player.getUseItem().is(ModItems.NEW_LOCACACA)) {
            LivingEntity ent = MainUtil.getStoneTarget(player.level(), player);
            if (ent != null) {
                float $$7 = getAttackAnimationScale(deltaTime,player,player.getUseItem());
                float $$8 = ( player.getUseItem().getUseDuration() - player.getUseItemRemainingTicks()) + deltaTime;
                float f2 = $$8 * 0.5F % 1.0F;
                float f3 = (float) (player.getBbHeight() * 0.5D);
                ps.pushPose();
                ps.translate(0.0F, f3, 0.0F);
                Vec3 vec3 = getPosition(ent, (double)ent.getBbHeight() * 0.5D, deltaTime);
                Vec3 vec31 = getPosition(player, (double)f3, deltaTime);
                Vec3 vec32 = vec3.subtract(vec31);
                float f4 = (float)(vec32.length() + 1.0D);
                vec32 = vec32.normalize();
                float f5 = (float)Math.acos(vec32.y);
                float f6 = (float)Math.atan2(vec32.z, vec32.x);
                ps.mulPose(Axis.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
                ps.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
                int i = 1;
                float f7 = $$8 * 0.05F * -1.5F;
                float f8 = $$7 * $$7;
                int j = 64 + (int)(f8 * 191.0F);
                int k = 32 + (int)(f8 * 191.0F);
                int l = 128 - (int)(f8 * 64.0F);
                float f9 = 0.2F;
                float f10 = 0.282F;
                float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
                float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
                float f13 = Mth.cos(f7 + ((float)Math.PI / 4F)) * 0.282F;
                float f14 = Mth.sin(f7 + ((float)Math.PI / 4F)) * 0.282F;
                float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
                float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
                float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
                float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
                float f19 = Mth.cos(f7 + (float)Math.PI) * 0.2F;
                float f20 = Mth.sin(f7 + (float)Math.PI) * 0.2F;
                float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
                float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
                float f23 = Mth.cos(f7 + ((float)Math.PI / 2F)) * 0.2F;
                float f24 = Mth.sin(f7 + ((float)Math.PI / 2F)) * 0.2F;
                float f25 = Mth.cos(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
                float f26 = Mth.sin(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
                float f27 = 0.0F;
                float f28 = 0.4999F;
                float f29 = -1.0F + f2;
                float f30 = f4 * 2.5F + f29;
                VertexConsumer vertexconsumer = mb.getBuffer(BEAM_RENDER_TYPE);
                PoseStack.Pose posestack$pose = ps.last();
                Matrix4f matrix4f = posestack$pose.pose();
                Matrix3f matrix3f = posestack$pose.normal();


                vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
                vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
                vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
                vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
                vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
                vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
                vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
                vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
                float f31 = 0.0F;
                if (player.tickCount % 2 == 0) {
                    f31 = 0.5F;
                }

                vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
                vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
                vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
                vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
                ps.popPose();
            }
        }
         */
    }

}
