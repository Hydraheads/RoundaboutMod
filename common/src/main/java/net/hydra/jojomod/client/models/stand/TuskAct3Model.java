package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.TuskAnimations;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class TuskAct3Model<T extends TuskEntity> extends StandModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor


    public TuskAct3Model(ModelPart root) {
        this.stand = root.getChild("stand");
        ModelPart stand2 = this.stand.getChild("stand2");
        this.head = stand2.getChild("head");
        this.body = stand2.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.5F, -19.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, -6.0F, -2.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(30, 0).addBox(-2.5F, -3.0F, -4.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(30, 10).addBox(-2.5F, -3.0F, -4.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(32, 37).addBox(0.0F, -5.0F, -4.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ears = head2.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(1.0F, -3.0F, -1.0F));

        PartDefinition left_ear = ears.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(48, 30).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(40, 49).addBox(2.0F, 2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.0F, -1.0F));

        PartDefinition right_ear = ears.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(48, 32).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 34).addBox(-3.0F, 2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 1.0F, -1.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 26).addBox(-4.0F, -20.0F, -1.0F, 7.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 26).addBox(-4.0F, -20.0F, -1.0F, 7.0F, 6.0F, 5.0F, new CubeDeformation(0.1F))
                .texOffs(0, 0).addBox(-5.0F, -27.0F, -2.0F, 9.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).addBox(-5.0F, -27.0F, -2.0F, 9.0F, 7.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition arms = torso.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition left_arm = arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 37).addBox(0.0F, -2.5F, -2.5F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -25.0F, 1.0F));

        PartDefinition upper_arm = left_arm.addOrReplaceChild("upper_arm", CubeListBuilder.create().texOffs(42, 37).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 16).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(1.5F, 2.5F, 0.0F));

        PartDefinition lower_arm = upper_arm.addOrReplaceChild("lower_arm", CubeListBuilder.create().texOffs(42, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 21).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(48, 16).addBox(-1.0F, -1.0F, 1.2F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition hand = lower_arm.addOrReplaceChild("hand", CubeListBuilder.create().texOffs(30, 20).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 12).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition cube_r1 = hand.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 36).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 1.5F, -1.0036F, 0.0F, 0.0F));

        PartDefinition right_arm = arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 37).addBox(-3.0F, -2.5F, -2.5F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -25.0F, 1.0F));

        PartDefinition upper_arm2 = right_arm.addOrReplaceChild("upper_arm2", CubeListBuilder.create().texOffs(32, 45).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 16).addBox(-1.0F, 0.6F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.5F, 2.5F, 0.0F));

        PartDefinition lower_arm2 = upper_arm2.addOrReplaceChild("lower_arm2", CubeListBuilder.create().texOffs(0, 47).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 18).addBox(-1.0F, -1.0F, 1.2F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(54, 21).addBox(-1.0F, 0.6F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition hand2 = lower_arm2.addOrReplaceChild("hand2", CubeListBuilder.create().texOffs(30, 23).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition cube_r2 = hand2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 48).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 1.5F, -1.0036F, 0.0F, 0.0F));

        PartDefinition legs = torso.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-3.0F, -14.0F, 0.0F));

        PartDefinition upper_leg = right_leg.addOrReplaceChild("upper_leg", CubeListBuilder.create().texOffs(44, 52).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 16).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_leg = upper_leg.addOrReplaceChild("lower_leg", CubeListBuilder.create().texOffs(48, 16).addBox(-1.0F, -1.0F, -1.2F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(44, 58).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(35, 61).addBox(-1.5F, 4.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 6).addBox(-1.5F, 4.0F, -1.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(40, 45).addBox(-0.5F, 4.0F, 1.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(54, 21).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -14.0F, 0.0F));

        PartDefinition upper_leg2 = left_leg.addOrReplaceChild("upper_leg2", CubeListBuilder.create().texOffs(52, 52).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 16).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_leg2 = upper_leg2.addOrReplaceChild("lower_leg2", CubeListBuilder.create().texOffs(48, 18).addBox(-1.0F, -1.0F, -1.2F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(52, 58).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(35, 61).addBox(-1.5F, 4.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 9).addBox(-1.5F, 4.0F, -1.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(44, 49).addBox(-0.5F, 4.0F, 1.5F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(54, 21).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.translate(0,0.1F,0);
        super.renderToBuffer(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public float rotationStrength() {
        return 0.5F;
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        this.defaultModifiers(pEntity);
        this.animate(pEntity.idleAnimationState, TuskAnimations.ACT_3_IDLE,pAgeInTicks,1F);
    }

}