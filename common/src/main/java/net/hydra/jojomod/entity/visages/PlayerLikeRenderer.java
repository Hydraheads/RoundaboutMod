package net.hydra.jojomod.entity.visages;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
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
        this.setModelProperties(mobEntity);
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

    private void setModelProperties(T $$0) {
        PlayerLikeModel<T> $$1 = this.getModel();
        if ($$0.isSpectator()) {
            $$1.setAllVisible(false);
            $$1.head.visible = true;
            $$1.hat.visible = true;
        } else {
            $$1.setAllVisible(true);
            $$1.hat.visible = true;
            $$1.jacket.visible = true;
            $$1.leftPants.visible = true;
            $$1.rightPants.visible = true;
            $$1.leftSleeve.visible = true;
            $$1.rightSleeve.visible = true;
            $$1.crouching = $$0.isCrouching();
            HumanoidModel.ArmPose $$2 = getArmPose($$0, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose $$3 = getArmPose($$0, InteractionHand.OFF_HAND);
            if ($$2.isTwoHanded()) {
                $$3 = $$0.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }

            if ($$0.getMainArm() == HumanoidArm.RIGHT) {
                $$1.rightArmPose = $$2;
                $$1.leftArmPose = $$3;
            } else {
                $$1.rightArmPose = $$3;
                $$1.leftArmPose = $$2;
            }
        }
    }

    @Override
    public Vec3 getRenderOffset(T $$0, float $$1) {
        return $$0.isCrouching() ? new Vec3(0.0, -0.125, 0.0) : super.getRenderOffset($$0, $$1);
    }
    private static HumanoidModel.ArmPose getArmPose(JojoNPC $$0, InteractionHand $$1) {
        ItemStack $$2 = $$0.getItemInHand($$1);
        if ($$2.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if ($$0.getUsedItemHand() == $$1 && $$0.getUseItemRemainingTicks() > 0) {
                UseAnim $$3 = $$2.getUseAnimation();
                if ($$3 == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if ($$3 == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if ($$3 == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if ($$3 == UseAnim.CROSSBOW && $$1 == $$0.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if ($$3 == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if ($$3 == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if ($$3 == UseAnim.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }
            } else if (!$$0.swinging && $$2.is(Items.CROSSBOW) && CrossbowItem.isCharged($$2)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            return HumanoidModel.ArmPose.ITEM;
        }
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
            super.setupRotations($$0, $$1, $$2, $$3, $$4);
            float $$14 = $$0.isInWater() ? -90.0F - $$0.getXRot() : -90.0F;
            float $$15 = Mth.lerp($$5, 0.0F, $$14);
            $$1.mulPose(Axis.XP.rotationDegrees($$15));
            if ($$0.isVisuallySwimming()) {
                $$1.translate(0.0F, -1.0F, 0.3F);
            }
        } else {
            super.setupRotations($$0, $$1, $$2, $$3, $$4);
        }



        byte playerP = $$0.roundabout$GetPos();

        /*Dodge makes you lean forward visually*/
        if (playerP == PlayerPosIndex.DODGE_FORWARD || playerP == PlayerPosIndex.DODGE_BACKWARD) {
            int dodgeTime = $$0.roundabout$getDodgeTime();
            float FL;
            if (dodgeTime > -1) {
                if (dodgeTime > 5) {
                    FL = ((11 - ((float) dodgeTime + 1 + $$4 - 1.0F)) / 20.0F * 1.6F);
                } else {
                    FL = ((float) dodgeTime + 1 + $$4 - 1.0F) / 20.0F * 1.6F;
                }
                FL = Mth.sqrt(FL);
                if (FL > 1.0F) {
                    FL = 1.0F;
                }

                if (playerP == PlayerPosIndex.DODGE_FORWARD) {
                    FL *= -1;
                }

                $$1.mulPose(Axis.XP.rotationDegrees(FL * 45));
            }
        }

    }

}

