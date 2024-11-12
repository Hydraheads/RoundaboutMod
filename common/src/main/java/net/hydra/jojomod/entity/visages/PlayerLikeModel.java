package net.hydra.jojomod.entity.visages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;

import java.util.List;
import java.util.function.Function;

public class PlayerLikeModel<T extends JojoNPC> extends HierarchicalModel<T> implements HeadedModel, ArmedModel {
    /**Override this for every stand model.*/
    private float alpha;
    private static final String EAR = "ear";
    private static final String CLOAK = "cloak";
    private static final String LEFT_SLEEVE = "left_sleeve";
    private static final String RIGHT_SLEEVE = "right_sleeve";
    private static final String LEFT_PANTS = "left_pants";
    private static final String RIGHT_PANTS = "right_pants";
    public ModelPart playerlike;
    public ModelPart head;
    public ModelPart hat;

    public ModelPart body;
    public ModelPart leftArm;
    public ModelPart rightArm;
    public ModelPart rightLeg;
    public ModelPart leftLeg;
    private List<ModelPart> parts;
    public ModelPart leftSleeve;
    public ModelPart rightSleeve;
    public ModelPart leftPants;
    public ModelPart rightPants;
    public ModelPart jacket;
    public ModelPart cloak;
    public boolean crouching;
    public float swimAmount;
    public ModelPart getRandomModelPart(RandomSource $$0) {
        return this.parts.get($$0.nextInt(this.parts.size()));
    }
    protected ModelPart getArm(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.rightArm : this.leftArm;
    }

    public ModelPart getHead(){
        return head;
    }
    @Override
    public ModelPart root() {
        return playerlike;
    }

    /**Most humanoid stands share these animations.*/
    public void defaultAnimations(T entity, float animationProgress, float windupLength){
    }

    /**If a stand has a head or a body, it pretty much could benefit from having
     * defaultmodifiers, because that makes its head and body rotate along
     * with the stand user.*/
    public void defaultModifiers(T entity){
    }

    private HumanoidArm getAttackArm(T $$0) {
        HumanoidArm $$1 = $$0.getMainArm();
        return $$0.swingingArm == InteractionHand.MAIN_HAND ? $$1 : $$1.getOpposite();
    }


    public void setAllVisible(boolean $$0) {
        this.head.visible = $$0;
        this.hat.visible = $$0;
        this.body.visible = $$0;
        this.rightArm.visible = $$0;
        this.leftArm.visible = $$0;
        this.rightLeg.visible = $$0;
        this.leftLeg.visible = $$0;
        this.leftSleeve.visible = $$0;
        this.rightSleeve.visible = $$0;
        this.leftPants.visible = $$0;
        this.rightPants.visible = $$0;
        this.jacket.visible = $$0;
        this.cloak.visible = $$0;
    }


    private void poseRightArm(T $$0) {
        switch (this.rightArmPose) {
            case EMPTY:
                this.rightArm.yRot = 0.0F;
                break;
            case BLOCK:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
                this.rightArm.yRot = (float) (-Math.PI / 6);
                break;
            case ITEM:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) (Math.PI / 10);
                this.rightArm.yRot = 0.0F;
                break;
            case THROW_SPEAR:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
                this.rightArm.yRot = 0.0F;
                break;
            case BOW_AND_ARROW:
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
                this.rightArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                this.leftArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                break;
            case CROSSBOW_CHARGE:
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, $$0, true);
                break;
            case CROSSBOW_HOLD:
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
                break;
            case BRUSH:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) (Math.PI / 5);
                this.rightArm.yRot = 0.0F;
                break;
            case SPYGLASS:
                this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - ($$0.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                this.rightArm.yRot = this.head.yRot - (float) (Math.PI / 12);
                break;
            case TOOT_HORN:
                this.rightArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
                this.rightArm.yRot = this.head.yRot - (float) (Math.PI / 6);
        }
    }

    protected void setupAttackAnimation(T $$0, float $$1) {
        if (!(this.attackTime <= 0.0F)) {
            HumanoidArm $$2 = this.getAttackArm($$0);
            ModelPart $$3 = this.getArm($$2);
            float $$4 = this.attackTime;
            this.body.yRot = Mth.sin(Mth.sqrt($$4) * (float) (Math.PI * 2)) * 0.2F;
            if ($$2 == HumanoidArm.LEFT) {
                this.body.yRot *= -1.0F;
            }
            float negativeRot = this.body.yRot*=-1;

            this.rightArm.z = Mth.sin(negativeRot) * 5.0F;
            this.leftArm.z = -Mth.sin(negativeRot) * 5.0F;
            this.rightArm.yRot = this.rightArm.yRot + negativeRot;
            this.leftArm.yRot = this.leftArm.yRot + negativeRot;
            this.leftArm.xRot = this.leftArm.xRot + negativeRot;
            $$4 = 1.0F - this.attackTime;
            $$4 *= $$4;
            $$4 *= $$4;
            $$4 = 1.0F - $$4;
            float $$5 = Mth.sin($$4 * (float) Math.PI);
            float $$6 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            $$3.xRot -= $$5 * 1.2F + $$6;
            $$3.yRot = $$3.yRot + this.body.yRot * 2.0F;
            $$3.zRot = $$3.zRot + Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }
    }
    @Override
    public void setupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {

        boolean $$6 = $$0.getFallFlyingTicks() > 4;
        boolean $$7 = $$0.isVisuallySwimming();
        this.head.yRot = $$4 * (float) (Math.PI / 180.0);
        this.swimAmount = $$0.getSwimAmount(ClientUtil.getFrameTime());
        if ($$6) {
            this.head.xRot = (float) (-Math.PI / 4);
        } else if (this.swimAmount > 0.0F) {
            if ($$7) {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (float) (-Math.PI / 4));
            } else {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, $$5 * (float) (Math.PI / 180.0));
            }
        } else {
            this.head.xRot = $$5 * (float) (Math.PI / 180.0);
        }

        this.body.yRot = 0.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.x = 0.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 0.0F;
        float $$8 = 1.0F;
        if ($$6) {
            $$8 = (float)$$0.getDeltaMovement().lengthSqr();
            $$8 /= 0.2F;
            $$8 *= $$8 * $$8;
        }

        if ($$8 < 1.0F) {
            $$8 = 1.0F;
        }

        this.rightArm.xRot = Mth.cos($$1 * 0.6662F + (float) Math.PI) * 2.0F * $$2 * 0.5F / $$8;
        this.leftArm.xRot = Mth.cos($$1 * 0.6662F) * 2.0F * $$2 * 0.5F / $$8;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos($$1 * 0.6662F) * 1.4F * $$2 / $$8;
        this.leftLeg.xRot = Mth.cos($$1 * 0.6662F + (float) Math.PI) * 1.4F * $$2 / $$8;
        this.rightLeg.yRot = 0.005F;
        this.leftLeg.yRot = -0.005F;
        this.rightLeg.zRot = 0.005F;
        this.leftLeg.zRot = -0.005F;
        if (this.riding) {
            this.rightArm.xRot += (float) (-Math.PI / 5);
            this.leftArm.xRot += (float) (-Math.PI / 5);
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = (float) (Math.PI / 10);
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = (float) (-Math.PI / 10);
            this.leftLeg.zRot = -0.07853982F;
        }

        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        boolean $$9 = $$0.getMainArm() == HumanoidArm.RIGHT;
        if ($$0.isUsingItem()) {
            boolean $$10 = $$0.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if ($$10 == $$9) {
                this.poseRightArm($$0);
            } else {
                this.poseLeftArm($$0);
            }
        } else {
            boolean $$11 = $$9 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
            if ($$9 != $$11) {
                this.poseLeftArm($$0);
                this.poseRightArm($$0);
            } else {
                this.poseRightArm($$0);
                this.poseLeftArm($$0);
            }
        }

        this.setupAttackAnimation($$0, $$3);
        if (this.crouching) {
            this.body.xRot = 0.5F;
            this.body.z = 5.2F;
            this.rightArm.xRot += 0.4F;
            this.leftArm.xRot += 0.4F;
            this.rightLeg.z = 4.0F;
            this.leftLeg.z = 4.0F;
            this.rightLeg.y = 0.2F;
            this.leftLeg.y = 0.2F;
            this.head.y = 4.2F;
            this.body.y = 2.0F;
            this.leftArm.y = 3.2F;
            this.rightArm.y = 3.2F;
        } else {
            this.body.xRot = 0.0F;
            this.rightLeg.z = 0.0F;
            this.leftLeg.z = 0.0F;
            this.rightLeg.y = 0.0F;
            this.leftLeg.y = 0.0F;
            this.head.y = 0.0F;
            this.body.y = 0.0F;
            this.leftArm.y = 0.0F;
            this.rightArm.y = 0.0F;
        }

        if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.rightArm, $$3, 1.0F);
        }

        if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.leftArm, $$3, -1.0F);
        }

        if (this.swimAmount > 0.0F) {
            float $$12 = $$1 % 26.0F;
            HumanoidArm $$13 = this.getAttackArm($$0).getOpposite();
            float $$14 = $$13 == HumanoidArm.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            float $$15 = $$13 == HumanoidArm.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            if (!$$0.isUsingItem()) {
                if ($$12 < 14.0F) {
                    this.leftArm.xRot = this.rotlerpRad($$15, this.leftArm.xRot, 0.0F);
                    this.rightArm.xRot = Mth.lerp($$14, this.rightArm.xRot, 0.0F);
                    this.leftArm.yRot = this.rotlerpRad($$15, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp($$14, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(
                            $$15, this.leftArm.zRot, (float) Math.PI + 1.8707964F * this.quadraticArmUpdate($$12) / this.quadraticArmUpdate(14.0F)
                    );
                    this.rightArm.zRot = Mth.lerp(
                            $$14, this.rightArm.zRot, (float) Math.PI - 1.8707964F * this.quadraticArmUpdate($$12) / this.quadraticArmUpdate(14.0F)
                    );
                } else if ($$12 >= 14.0F && $$12 < 22.0F) {
                    float $$16 = ($$12 - 14.0F) / 8.0F;
                    this.leftArm.xRot = this.rotlerpRad($$15, this.leftArm.xRot, (float) (Math.PI / 2) * $$16);
                    this.rightArm.xRot = Mth.lerp($$14, this.rightArm.xRot, (float) (Math.PI / 2) * $$16);
                    this.leftArm.yRot = this.rotlerpRad($$15, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp($$14, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad($$15, this.leftArm.zRot, 5.012389F - 1.8707964F * $$16);
                    this.rightArm.zRot = Mth.lerp($$14, this.rightArm.zRot, 1.2707963F + 1.8707964F * $$16);
                } else if ($$12 >= 22.0F && $$12 < 26.0F) {
                    float $$17 = ($$12 - 22.0F) / 4.0F;
                    this.leftArm.xRot = this.rotlerpRad($$15, this.leftArm.xRot, (float) (Math.PI / 2) - (float) (Math.PI / 2) * $$17);
                    this.rightArm.xRot = Mth.lerp($$14, this.rightArm.xRot, (float) (Math.PI / 2) - (float) (Math.PI / 2) * $$17);
                    this.leftArm.yRot = this.rotlerpRad($$15, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp($$14, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad($$15, this.leftArm.zRot, (float) Math.PI);
                    this.rightArm.zRot = Mth.lerp($$14, this.rightArm.zRot, (float) Math.PI);
                }
            }

            float $$18 = 0.3F;
            float $$19 = 0.33333334F;
            this.leftLeg.xRot = Mth.lerp(this.swimAmount, this.leftLeg.xRot, 0.3F * Mth.cos($$1 * 0.33333334F + (float) Math.PI));
            this.rightLeg.xRot = Mth.lerp(this.swimAmount, this.rightLeg.xRot, 0.3F * Mth.cos($$1 * 0.33333334F));
        }
        byte pos = $$0.roundabout$GetPos();
        if (pos == PlayerPosIndex.DODGE_FORWARD || pos == PlayerPosIndex.DODGE_BACKWARD){
            //this.body.xRot = -20.0F;
            int dodgeTime = $$0.roundabout$getDodgeTime();
            float fl;
            if (dodgeTime > -1) {
                if (dodgeTime > 5) {
                    fl = ((11 - ((float) dodgeTime + 1 + $$2 - 1.0F)) / 20.0F * 1.6F);
                } else {
                    fl = ((float) dodgeTime + 1 + $$2 - 1.0F) / 20.0F * 1.6F;
                }
                if (fl != 0) {
                    fl = Mth.sqrt(fl);
                }
                if (fl > 1.0F) {
                    fl = 1.0F;
                }

                if (pos == PlayerPosIndex.DODGE_BACKWARD){
                    fl*=-1;
                    this.leftLeg.xRot = (float) (-fl*1.2);
                } else {
                    this.leftLeg.xRot = -fl;
                }
                this.rightLeg.xRot = fl;
                this.leftArm.xRot = -fl;
                this.rightArm.xRot = fl;
                this.head.xRot += (float) (-fl*0.3);
                this.setupAttackAnimation($$0, $$3);


                boolean JJ = $$0.getMainArm() == HumanoidArm.RIGHT;
                if ($$0.isUsingItem()) {
                    boolean $$10 = $$0.getUsedItemHand() == InteractionHand.MAIN_HAND;
                    if ($$10 == JJ) {
                        this.poseRightArm($$0);
                    } else {
                        this.poseLeftArm($$0);
                    }
                } else {
                    boolean $$11 = JJ ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
                    if (JJ != $$11) {
                        this.poseLeftArm($$0);
                        this.poseRightArm($$0);
                    } else {
                        this.poseRightArm($$0);
                        this.poseLeftArm($$0);
                    }
                }

            }
        }
        this.hat.copyFrom(this.head);


        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        if ($$0.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            if ($$0.isCrouching()) {
                this.cloak.z = 1.4F;
                this.cloak.y = 1.85F;
            } else {
                this.cloak.z = 0.0F;
                this.cloak.y = 0.0F;
            }
        } else if ($$0.isCrouching()) {
            this.cloak.z = 0.3F;
            this.cloak.y = 0.8F;
        } else {
            this.cloak.z = -1.1F;
            this.cloak.y = -0.85F;
        }
    }

    private float quadraticArmUpdate(float $$0) {
        return -65.0F * $$0 + $$0 * $$0;
    }
    private void poseLeftArm(T $$0) {
        switch (this.leftArmPose) {
            case EMPTY:
                this.leftArm.yRot = 0.0F;
                break;
            case BLOCK:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
                this.leftArm.yRot = (float) (Math.PI / 6);
                break;
            case ITEM:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) (Math.PI / 10);
                this.leftArm.yRot = 0.0F;
                break;
            case THROW_SPEAR:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
                this.leftArm.yRot = 0.0F;
                break;
            case BOW_AND_ARROW:
                this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
                this.leftArm.yRot = 0.1F + this.head.yRot;
                this.rightArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                this.leftArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                break;
            case CROSSBOW_CHARGE:
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, $$0, false);
                break;
            case CROSSBOW_HOLD:
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
                break;
            case BRUSH:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) (Math.PI / 5);
                this.leftArm.yRot = 0.0F;
                break;
            case SPYGLASS:
                this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - ($$0.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                this.leftArm.yRot = this.head.yRot + (float) (Math.PI / 12);
                break;
            case TOOT_HORN:
                this.leftArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
                this.leftArm.yRot = this.head.yRot + (float) (Math.PI / 6);
        }
    }
    public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
    public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;
    public void copyPropertiesTo(HumanoidModel<T> $$0) {
        super.copyPropertiesTo($$0);
        $$0.leftArmPose = this.leftArmPose;
        $$0.rightArmPose = this.rightArmPose;
        $$0.crouching = this.crouching;
        $$0.head.copyFrom(this.head);
        $$0.hat.copyFrom(this.hat);
        $$0.body.copyFrom(this.body);
        $$0.rightArm.copyFrom(this.rightArm);
        $$0.leftArm.copyFrom(this.leftArm);
        $$0.rightLeg.copyFrom(this.rightLeg);
        $$0.leftLeg.copyFrom(this.leftLeg);
    }

    protected float rotlerpRad(float $$0, float $$1, float $$2) {
        float $$3 = ($$2 - $$1) % (float) (Math.PI * 2);
        if ($$3 < (float) -Math.PI) {
            $$3 += (float) (Math.PI * 2);
        }

        if ($$3 >= (float) Math.PI) {
            $$3 -= (float) (Math.PI * 2);
        }

        return $$1 + $$0 * $$3;
    }
    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        playerlike.render(matrices, vertexConsumer, light, overlay, red, green, blue, this.alpha);
    }

    public void setAlpha(float alpha){
        this.alpha = alpha;
    }

    public boolean getSlim(){
        return false;
    }

    @Override
    public void translateToHand(HumanoidArm var1, PoseStack var2) {
        ModelPart $$2 = this.getArm(var1.getOpposite());
        if (this.getSlim()) {
            float $$3 = (4.5F * (float)(var1 == HumanoidArm.RIGHT ? 1 : -1))*-1;
            $$2.x += $$3;
            $$2.y += 2F;
            $$2.translateAndRotate(var2);
            $$2.x -= $$3;
            $$2.y -= 2F;
        } else {
            float $$3 = (4F * (float)(var1 == HumanoidArm.RIGHT ? 1 : -1))*-1;
            $$2.x += $$3;
            $$2.y += 2F;
            $$2.translateAndRotate(var2);
            $$2.x -= $$3;
            $$2.y -= 2F;
        }
    }

    public PlayerLikeModel() {
        super();
        this.scaleHead = true;
        this.babyYHeadOffset = 16.0F;
        this.babyZHeadOffset = 0.0F;
        this.babyHeadScale = 2.0F;
        this.babyBodyScale = 2.0F;
        this.bodyYOffset = 24.0F;
    }
    private final boolean scaleHead;
    private final float babyYHeadOffset;
    private final float babyZHeadOffset;
    private final float babyHeadScale;
    private final float babyBodyScale;
    private final float bodyYOffset;


}
