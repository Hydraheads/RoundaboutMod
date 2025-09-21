package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.entity.stand.TheGratefulDeadEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TheGratefulDeadModel<T extends TheGratefulDeadEntity> extends StandModel<T>{
    public TheGratefulDeadModel(ModelPart root){
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("chest").getChild("left_arm").getChild("left_lower");
        this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("chest").getChild("right_arm").getChild("right_lower");
    }

    public static LayerDefinition getTexturedModelData(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 13.5F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -18.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(1, 0).addBox(-3.5F, -8.85F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, -1.85F, -4.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition chest = torso.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = chest.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(0, 52).addBox(-4.5F, -6.0F, -3.0F, 9.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_chest = chest.addOrReplaceChild("lower_chest", CubeListBuilder.create().texOffs(31, 55).addBox(-3.5F, 0.0F, -3.0F, 7.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-3.5F, 3.0F, -1.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(14, 16).addBox(-3.5F, 3.0F, 1.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition right_arm = chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.5F, 1.0F, 0.0F));

        PartDefinition right_upper = right_arm.addOrReplaceChild("right_upper", CubeListBuilder.create().texOffs(29, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, -2).mirror().addBox(-4.25F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_lower = right_arm.addOrReplaceChild("right_lower", CubeListBuilder.create().texOffs(29, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 7.0F, 0.0F));

        PartDefinition right_finger1 = right_lower.addOrReplaceChild("right_finger1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 10.0F, -2.0F));

        PartDefinition right_finger2 = right_lower.addOrReplaceChild("right_finger2", CubeListBuilder.create(), PartPose.offset(2.0F, 10.0F, 0.0F));

        PartDefinition cube_r1 = right_finger2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -0.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition right_finger3 = right_lower.addOrReplaceChild("right_finger3", CubeListBuilder.create(), PartPose.offset(-2.0F, 10.0F, 0.0F));

        PartDefinition cube_r2 = right_finger3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -0.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition left_arm = chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.5F, 1.0F, 0.0F));

        PartDefinition left_upper = left_arm.addOrReplaceChild("left_upper", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, -2).addBox(4.25F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_lower = left_arm.addOrReplaceChild("left_lower", CubeListBuilder.create().texOffs(45, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 7.0F, 0.0F));

        PartDefinition left_finger1 = left_lower.addOrReplaceChild("left_finger1", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, -2.0F));

        PartDefinition left_finger2 = left_lower.addOrReplaceChild("left_finger2", CubeListBuilder.create(), PartPose.offset(-2.0F, 10.0F, 0.0F));

        PartDefinition cube_r3 = left_finger2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -0.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition left_finger3 = left_lower.addOrReplaceChild("left_finger3", CubeListBuilder.create(), PartPose.offset(2.0F, 10.0F, 0.0F));

        PartDefinition cube_r4 = left_finger3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -0.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -13.5F, 0.0F));

        PartDefinition RightArmBAM1 = BAM.addOrReplaceChild("RightArmBAM1", CubeListBuilder.create(), PartPose.offset(-13.5F, -7.25F, 0.0F));

        PartDefinition cube_r5 = RightArmBAM1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r6 = RightArmBAM1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r7 = RightArmBAM1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r8 = RightArmBAM1.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-17.5F, -0.25F, 0.0F));

        PartDefinition cube_r9 = RightArmBAM2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r10 = RightArmBAM2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r11 = RightArmBAM2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r12 = RightArmBAM2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.5F, 6.75F, 0.0F));

        PartDefinition cube_r13 = RightArmBAM3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r14 = RightArmBAM3.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r15 = RightArmBAM3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r16 = RightArmBAM3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}
