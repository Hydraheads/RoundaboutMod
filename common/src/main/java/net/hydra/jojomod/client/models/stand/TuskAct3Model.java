package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.4357F, -4.1357F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 15).addBox(-4.0F, -3.4357F, -4.1357F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(71, 0).addBox(-4.0F, -3.4357F, -4.1357F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
                .texOffs(30, 27).addBox(-0.5F, -6.1857F, -4.1857F, 1.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(31, 23).addBox(-7.0F, -2.9357F, 0.3643F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(62, 35).addBox(5.9F, -0.9357F, 0.3643F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 63).addBox(-6.9F, -0.9357F, 0.3643F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -29.8643F, -4.9643F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(83, 48).addBox(-6.0F, -29.0F, -1.25F, 12.0F, 7.0F, 5.0F, new CubeDeformation(0.2F))
                .texOffs(83, 60).addBox(-6.0F, -29.0F, -1.25F, 12.0F, 7.0F, 5.0F, new CubeDeformation(0.3F))
                .texOffs(-2, 30).addBox(-6.0F, -29.0F, -1.25F, 12.0F, 7.0F, 5.0F, new CubeDeformation(0.1F))
                .texOffs(58, 89).addBox(-0.1F, -29.0F, -2.9F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(86, 35).mirror().addBox(-4.95F, -32.2F, -1.3F, 10.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(98, 14).mirror().addBox(-4.95F, -32.2F, -1.3F, 10.0F, 3.0F, 5.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(58, 89).mirror().addBox(-0.9F, -29.0F, -2.9F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(1.2F, 0.0F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(20, 42).addBox(5.25F, -29.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(12, 48).addBox(6.3F, -23.8F, 0.05F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 51).addBox(5.8F, -22.8F, -0.45F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(48, 57).addBox(5.8F, -22.8F, -0.45F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(0, 48).addBox(-1.675F, -4.235F, -1.435F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(46, 32).addBox(-2.175F, -0.235F, -1.935F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(46, 36).addBox(-1.225F, -5.185F, 1.715F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 51).addBox(-1.675F, -4.235F, -1.435F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(7.475F, -15.565F, 0.985F));

        PartDefinition cube_r1 = lower_left_arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.475F, 1.065F, 1.265F, 3.0486F, 0.4812F, 2.3604F));

        PartDefinition cube_r2 = lower_left_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.475F, 1.065F, 1.265F, 2.9655F, -1.0814F, 2.5593F));

        PartDefinition cube_r3 = lower_left_arm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(14, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.575F, 1.065F, 1.265F, 3.0486F, 0.4812F, 2.3604F));

        PartDefinition cube_r4 = lower_left_arm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.575F, 1.065F, 1.265F, 2.9655F, -1.0814F, 2.5593F));

        PartDefinition cube_r5 = lower_left_arm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.375F, 1.565F, -1.235F, 0.0F, 1.1781F, 0.0F));

        PartDefinition cube_r6 = lower_left_arm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(14, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.375F, 1.565F, -1.235F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r7 = lower_left_arm.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(14, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 1.565F, -1.235F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r8 = lower_left_arm.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 1.565F, -1.235F, 0.0F, 1.1781F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-1.2F, 0.0F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(36, 42).addBox(-9.25F, -29.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(58, 19).addBox(-8.3F, -23.8F, 0.05F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 55).addBox(-8.8F, -22.8F, -0.45F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(52, 37).addBox(-8.8F, -22.8F, -0.45F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(46, 27).addBox(-1.825F, -0.235F, -1.935F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 51).addBox(-1.325F, -4.235F, -1.435F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(12, 51).addBox(-1.325F, -4.235F, -1.435F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(46, 36).mirror().addBox(-0.775F, -5.185F, 1.715F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.475F, -15.565F, 0.985F));

        PartDefinition cube_r9 = lower_right_arm.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(14, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.575F, 1.065F, 1.265F, 3.0486F, -0.4812F, -2.3604F));

        PartDefinition cube_r10 = lower_right_arm.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(10, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.575F, 1.065F, 1.265F, 2.9655F, 1.0814F, -2.5593F));

        PartDefinition cube_r11 = lower_right_arm.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(14, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.475F, 1.065F, 1.265F, 3.0486F, -0.4812F, -2.3604F));

        PartDefinition cube_r12 = lower_right_arm.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(10, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.475F, 1.065F, 1.265F, 2.9655F, 1.0814F, -2.5593F));

        PartDefinition cube_r13 = lower_right_arm.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(10, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(14, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.375F, 1.565F, -1.235F, 0.0F, -1.1781F, 0.0F));

        PartDefinition cube_r14 = lower_right_arm.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(14, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(10, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.125F, 1.565F, -1.235F, 0.0F, 0.3927F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create().texOffs(0, 104).addBox(-6.0F, -21.8F, -0.25F, 12.0F, 7.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(0, 117).addBox(-6.0F, -21.8F, -0.25F, 12.0F, 7.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(0, 41).addBox(-5.0F, -21.9F, -1.35F, 10.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(91, 111).addBox(-5.1F, -17.6F, -1.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(91, 111).mirror().addBox(3.1F, -17.6F, -1.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r15 = lower_chest.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(91, 111).mirror().addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.1123F, -17.4436F, -1.3F, 0.0F, 0.0F, -0.1571F));

        PartDefinition cube_r16 = lower_chest.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(91, 111).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1123F, -17.4436F, -1.3F, 0.0F, 0.0F, 0.1571F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(62, 33).addBox(-4.4F, -12.2F, -4.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 122).addBox(-4.9F, -15.85F, -3.35F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(60, 0).addBox(-4.9F, -15.85F, -3.35F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(52, 47).addBox(-1.9042F, -0.7042F, -0.9375F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(46, 109).addBox(-1.0542F, -5.8042F, -0.0375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(58, 12).addBox(-1.0542F, -5.6042F, -0.0375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(43, 76).addBox(-0.5542F, -1.7042F, 2.0125F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(44, 61).addBox(-0.5542F, -1.7042F, 2.0125F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.8458F, -6.2958F, -3.3125F));

        PartDefinition cube_r17 = lower_right_leg.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4958F, 1.2958F, -1.6875F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r18 = lower_right_leg.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5042F, 1.2958F, -1.6875F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r19 = lower_right_leg.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6042F, 1.2958F, -1.6875F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r20 = lower_right_leg.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(67, 75).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(60, 60).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4042F, 0.2958F, -1.4375F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r21 = lower_right_leg.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(69, 42).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(62, 27).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5958F, 0.2958F, -1.4375F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r22 = lower_right_leg.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0542F, 1.2958F, 2.6125F, 0.0F, 1.1781F, 0.0F));

        PartDefinition cube_r23 = lower_right_leg.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0542F, 1.2958F, 2.6125F, 0.0F, -0.3927F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(46, 40).addBox(3.4F, -12.2F, -4.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 51).addBox(2.9F, -15.85F, -3.35F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(77, 119).addBox(2.9F, -15.85F, -3.35F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(52, 43).addBox(-2.0958F, -0.7042F, -0.9375F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(43, 73).addBox(-0.4458F, -1.7042F, 2.0125F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(44, 58).addBox(-0.4458F, -1.7042F, 2.0125F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 58).addBox(-0.9458F, -5.6042F, -0.0375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(71, 110).addBox(-0.9458F, -5.8042F, -0.0375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(3.8458F, -6.2958F, -3.3125F));

        PartDefinition cube_r24 = lower_left_leg.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4958F, 1.2958F, -1.6875F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r25 = lower_left_leg.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(7, 76).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(0, 61).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4042F, 0.2958F, -1.4375F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r26 = lower_left_leg.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(67, 72).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(60, 57).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5958F, 0.2958F, -1.4375F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r27 = lower_left_leg.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0042F, 1.2958F, 2.6125F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r28 = lower_left_leg.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0042F, 1.2958F, 2.6125F, 0.0F, 1.1781F, 0.0F));

        PartDefinition cube_r29 = lower_left_leg.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5042F, 1.2958F, -1.6875F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r30 = lower_left_leg.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5042F, 1.2958F, -1.6875F, 0.0F, 1.1781F, 0.0F));

        PartDefinition cube_r31 = lower_left_leg.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4958F, 1.2958F, -1.6875F, 0.0F, -0.3927F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.scale(0.75F,0.75F,0.75F);
        matrices.translate(0,0.5,0);
        super.renderToBuffer(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    //    @Override
//    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//
//    }

}