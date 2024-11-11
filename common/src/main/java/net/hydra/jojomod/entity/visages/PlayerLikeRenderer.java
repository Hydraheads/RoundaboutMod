package net.hydra.jojomod.entity.visages;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public class PlayerLikeRenderer<T extends JojoNPC> extends MobRenderer<T, PlayerLikeModel<T>> {
    public PlayerLikeRenderer(EntityRendererProvider.Context context, PlayerLikeModel<T> entityModel, float f) {
        super(context, entityModel, f);
        this.addLayer(new JojoNPCItemInHandLayer<>(this,context.getItemInHandRenderer()));
        this.addLayer(new PlayerLikeArrowLayer<>(context, this));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
        this.addLayer(new PlayerLikeParrotOnShoulderLayer<>(this, context.getModelSet()));
        this.addLayer(new PlayerLikeSpinAttackLayer<>(this, context.getModelSet()));
        this.addLayer(new PlayerLikeBeeStingLayer<>(this));
    }



    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }



    @Override
    public void render(T mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    public final int getTrueLight(Entity entity, float tickDelta) {
        BlockPos blockPos = BlockPos.containing(entity.getLightProbePosition(tickDelta));
        return LightTexture.pack(this.getTrueBlockLight(entity, blockPos), this.getTrueSkyLight(entity, blockPos));
    }

    protected int getTrueSkyLight(Entity entity, BlockPos pos) {
        return entity.level().getBrightness(LightLayer.SKY, pos);
    }

    protected int getTrueBlockLight(Entity entity, BlockPos pos) {
        if (entity.isOnFire()) {
            return 15;
        }
        return entity.level().getBrightness(LightLayer.BLOCK, pos);
    }

    protected void setupRotations(T $$0, PoseStack $$1, float $$2, float $$3, float $$4) {
        float $$5 = $$0.getSwimAmount($$4);
        if ($$0.isFallFlying()) {
            super.setupRotations($$0, $$1, $$2, $$3, $$4);
            float $$6 = (float)$$0.getFallFlyingTicks() + $$4;
            float $$7 = Mth.clamp($$6 * $$6 / 100.0F, 0.0F, 1.0F);
            if (!$$0.isAutoSpinAttack()) {
                $$1.mulPose(Axis.XP.rotationDegrees($$7 * (-90.0F - $$0.getXRot())));
            }

            Vec3 $$8 = $$0.getViewVector($$4);
            Vec3 $$9 = $$0.getDeltaMovementLerped($$4);
            double $$10 = $$9.horizontalDistanceSqr();
            double $$11 = $$8.horizontalDistanceSqr();
            if ($$10 > 0.0 && $$11 > 0.0) {
                double $$12 = ($$9.x * $$8.x + $$9.z * $$8.z) / Math.sqrt($$10 * $$11);
                double $$13 = $$9.x * $$8.z - $$9.z * $$8.x;
                $$1.mulPose(Axis.YP.rotation((float)(Math.signum($$13) * Math.acos($$12))));
            }
        } else if ($$5 > 0.0F) {
            super.setupRotations((T) $$0, $$1, $$2, $$3, $$4);
            float $$14 = $$0.isInWater() ? -90.0F - $$0.getXRot() : -90.0F;
            float $$15 = Mth.lerp($$5, 0.0F, $$14);
            $$1.mulPose(Axis.XP.rotationDegrees($$15));
            if ($$0.isVisuallySwimming()) {
                $$1.translate(0.0F, -1.0F, 0.3F);
            }
        } else {
            super.setupRotations($$0, $$1, $$2, $$3, $$4);
        }
    }

}

