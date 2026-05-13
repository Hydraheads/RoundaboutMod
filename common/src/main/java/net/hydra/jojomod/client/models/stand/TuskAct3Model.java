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

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -28.0F, -1.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.4357F, -7.1357F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 15).addBox(-4.0F, -5.4357F, -7.1357F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
                .texOffs(71, 0).addBox(-4.0F, -5.4357F, -7.1357F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
                .texOffs(30, 27).addBox(-0.5F, -8.1857F, -7.1857F, 1.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(62, 35).addBox(5.9F, -2.9357F, -2.6357F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(66, 35).addBox(4.9F, -3.9357F, -2.6357F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(70, 35).addBox(3.9F, -4.9357F, -2.6357F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 63).addBox(-6.9F, -2.9357F, -2.6357F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 63).addBox(-5.9F, -3.9357F, -2.6357F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 63).addBox(-4.9F, -4.9357F, -2.6357F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.1357F, -0.9643F));

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

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(6.2F, -27.0F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(20, 42).addBox(5.25F, -29.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(12, 48).addBox(6.3F, -23.8F, 0.05F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 51).addBox(5.8F, -22.8F, -0.45F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(48, 57).addBox(5.8F, -22.8F, -0.45F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 27.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(0, 48).addBox(-1.675F, -0.235F, -1.435F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(46, 32).addBox(-2.175F, 3.765F, -1.935F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(46, 36).addBox(-1.225F, -1.185F, 1.715F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 51).addBox(-1.675F, -0.235F, -1.435F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.475F, 7.435F, 0.985F));

        PartDefinition cube_r1 = lower_left_arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.475F, 5.065F, 1.265F, 2.9655F, -1.0814F, 2.5593F));

        PartDefinition cube_r2 = lower_left_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.375F, 5.565F, -1.235F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r3 = lower_left_arm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(14, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.125F, 5.565F, -1.235F, 0.0F, -0.3927F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-6.2F, -27.0F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(36, 42).addBox(-9.25F, -29.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(58, 19).addBox(-8.3F, -23.8F, 0.05F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 55).addBox(-8.8F, -22.8F, -0.45F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(52, 37).addBox(-8.8F, -22.8F, -0.45F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 27.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(46, 27).addBox(-1.825F, 3.765F, -1.935F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 51).addBox(-1.325F, -0.235F, -1.435F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(12, 51).addBox(-1.325F, -0.235F, -1.435F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(46, 36).mirror().addBox(-0.775F, -1.185F, 1.715F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.475F, 7.435F, 0.985F));

        PartDefinition cube_r4 = lower_right_arm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(10, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.475F, 5.065F, 1.265F, 2.9655F, 1.0814F, -2.5593F));

        PartDefinition cube_r5 = lower_right_arm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(14, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.375F, 5.565F, -1.235F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r6 = lower_right_arm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(14, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(10, 90).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.125F, 5.565F, -1.235F, 0.0F, 0.3927F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create().texOffs(0, 104).addBox(-6.0F, -21.8F, -0.25F, 12.0F, 7.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(0, 117).addBox(-6.0F, -21.8F, -0.25F, 12.0F, 7.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(0, 41).addBox(-5.0F, -21.9F, -1.35F, 10.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(91, 111).addBox(-5.1F, -17.6F, -1.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(91, 111).mirror().addBox(3.1F, -17.6F, -1.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = lower_chest.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(91, 111).mirror().addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.1123F, -17.4436F, -1.3F, 0.0F, 0.0F, -0.1571F));

        PartDefinition cube_r8 = lower_chest.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(91, 111).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1123F, -17.4436F, -1.3F, 0.0F, 0.0F, 0.1571F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-4.0F, -16.0F, -1.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(62, 33).addBox(-4.4F, -12.2F, -4.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 122).addBox(-4.9F, -15.85F, -3.35F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(60, 0).addBox(-4.9F, -15.85F, -3.35F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 16.0F, 1.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(52, 47).addBox(-1.9042F, 4.7958F, -1.9375F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(10, 90).addBox(-0.5542F, 5.7958F, 1.6125F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(46, 109).addBox(-1.0542F, -0.3042F, -1.0375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(58, 12).addBox(-1.0542F, -0.1042F, -1.0375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(43, 76).addBox(-0.5542F, 3.7958F, 1.0125F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(44, 61).addBox(-0.5542F, 3.7958F, 1.0125F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1542F, 4.2042F, -1.3125F));

        PartDefinition cube_r9 = lower_right_leg.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4958F, 6.7958F, -2.6875F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r10 = lower_right_leg.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5042F, 6.7958F, -2.6875F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r11 = lower_right_leg.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(67, 75).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(60, 60).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4042F, 5.7958F, -2.4375F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r12 = lower_right_leg.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(69, 42).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(62, 27).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5958F, 5.7958F, -2.4375F, 0.0F, -0.3927F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(4.0F, -16.0F, -1.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(46, 40).addBox(3.4F, -12.2F, -4.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 51).addBox(2.9F, -15.85F, -3.35F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(77, 119).addBox(2.9F, -15.85F, -3.35F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(-4.0F, 16.0F, 1.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(52, 43).addBox(-2.0958F, 4.7958F, -1.9375F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(10, 90).addBox(-0.4458F, 5.7958F, 1.6125F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(43, 73).addBox(-0.4458F, 3.7958F, 1.0125F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(44, 58).addBox(-0.4458F, 3.7958F, 1.0125F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 58).addBox(-0.9458F, -0.1042F, -1.0375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(71, 110).addBox(-0.9458F, -0.3042F, -1.0375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(-0.1542F, 4.2042F, -1.3125F));

        PartDefinition cube_r13 = lower_left_leg.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(7, 76).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(0, 61).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4042F, 5.7958F, -2.4375F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r14 = lower_left_leg.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(67, 72).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(60, 57).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5958F, 5.7958F, -2.4375F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r15 = lower_left_leg.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5042F, 6.7958F, -2.6875F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r16 = lower_left_leg.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(10, 90).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4958F, 6.7958F, -2.6875F, 0.0F, 0.3927F, 0.0F));

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