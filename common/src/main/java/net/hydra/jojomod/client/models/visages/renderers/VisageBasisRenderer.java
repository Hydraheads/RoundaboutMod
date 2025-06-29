package net.hydra.jojomod.client.models.visages.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.FacelessLayer;
import net.hydra.jojomod.client.models.layers.HeyYaLayer;
import net.hydra.jojomod.client.models.layers.KnifeLayer;
import net.hydra.jojomod.client.models.layers.ShootingArmLayer;
import net.hydra.jojomod.client.models.layers.StoneLayer;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

public class VisageBasisRenderer extends LivingEntityRenderer<JojoNPC, PlayerModel<JojoNPC>> {
    public void changeTheModel(ItemStack visage){

        if (visage != null && !visage.isEmpty()) {
            if (visage.getItem() instanceof MaskItem MI) {
                if (MI.visageData.isCharacterVisage()) {
                    if (MI.visageData.isSlim()){
                        model = otherModel;
                        return;
                    }
                }
            }
        }
        model = mainModel;
    }
    @Unique
    protected PlayerModel otherModel;
    @Unique
    protected PlayerModel mainModel;

    public VisageBasisRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new PlayerModel<>($$0.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, $$0.getItemInHandRenderer()));
        this.addLayer(new ArrowLayer<>($$0, this));
        this.addLayer(new CustomHeadLayer<>(this, $$0.getModelSet(), $$0.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, $$0.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer<>(this, $$0.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this));
        //this.addLayer(new LocacacaBeamLayer<>($$0, this));
        this.addLayer(new StoneLayer<>($$0, this));
        this.addLayer(new FacelessLayer<>($$0, this));
        this.addLayer(new ShootingArmLayer<>($$0, this));
        this.addLayer(new HeyYaLayer<>($$0, this));
        this.addLayer(new VisagePartLayer<>($$0, this));
        otherModel = new PlayerModel<>($$0.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        mainModel = this.model;
    }

    public void render(JojoNPC $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        changeTheModel($$0.getBasis());
        this.setModelProperties($$0);
        super.render($$0, $$1, $$2, $$3, $$4, $$5);
    }

    private void setModelProperties(JojoNPC $$0) {
        PlayerModel<JojoNPC> $$1 = this.getModel();
        if ($$0.isSpectator()) {
            $$1.setAllVisible(false);
            $$1.head.visible = true;
            $$1.hat.visible = true;
        } else {
            $$1.setAllVisible(true);
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
    protected void scale(JojoNPC $$0, PoseStack $$1, float $$2) {
        ItemStack visage = $$0.getBasis();
        if (visage != null && !visage.isEmpty()) {
            if (visage.getItem() instanceof MaskItem MI) {
                if (MI instanceof ModificationMaskItem MD){
                    int height = visage.getOrCreateTagElement("modifications").getInt("height");
                    int width = visage.getOrCreateTagElement("modifications").getInt("width");
                    $$1.scale(0.798F + (((float) width)*0.001F), 0.7F+(((float) height)*0.001F), 0.798F+(((float) width)*0.001F));
                } else {
                    Vector3f scale = MI.visageData.scale();
                    $$1.scale(scale.x, scale.y, scale.z);
                }
                return;

            }
        }


        float $$3 = 0.9375F;
        $$1.scale(0.9375F, 0.9375F, 0.9375F);
    }
    private static HumanoidModel.ArmPose getArmPose(LivingEntity $$0, InteractionHand $$1) {
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
        @Override
        public ResourceLocation getTextureLocation(JojoNPC var1) {
            ItemStack visage = var1.getBasis();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    if (MI.visageData.isCharacterVisage()) {
                        return (new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_skins/"+MI.visageData.getSkinPath()+".png"));
                    }
                }
            }
            return null;
        }


    protected boolean shouldShowName(JojoNPC $$0) {
        return super.shouldShowName($$0) && ($$0.shouldShowName() || $$0.hasCustomName() && $$0 == this.entityRenderDispatcher.crosshairPickEntity);
    }
    protected void setupRotations(JojoNPC $$0, PoseStack $$1, float $$2, float $$3, float $$4) {
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
    }
}
