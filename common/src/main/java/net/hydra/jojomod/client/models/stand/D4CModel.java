package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class D4CModel<T extends D4CEntity> extends StandModel<T> {
    public D4CModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
        this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition mask = head2.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.15F, -0.7F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(32, 30).addBox(-1.0F, -3.6F, -0.901F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 30).addBox(-1.0F, -2.3F, -0.901F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 31).addBox(-1.0F, -1.0F, -0.901F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.6F, -3.3F));

        PartDefinition ears = head2.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_ear = ears.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(64, 51).addBox(-1.75F, -9.5F, -1.0F, 3.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 70).addBox(-1.75F, -9.5F, -1.0F, 3.0F, 11.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(49, 70).addBox(-0.75F, -10.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, -8.85F, 0.4F));

        PartDefinition left_ear = ears.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(65, 64).addBox(-1.25F, -9.5F, -1.0F, 3.0F, 11.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(55, 70).addBox(-0.25F, -10.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(41, 70).addBox(-1.25F, -9.5F, -1.0F, 3.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.75F, -8.85F, 0.4F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 10).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(3.25F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(0, 52).addBox(0.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 30).addBox(0.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(16, 52).addBox(0.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(24, 32).addBox(-1.25F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(56, 10).addBox(-1.25F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.11F))
                .texOffs(1, 76).addBox(-1.25F, -0.25F, 2.25F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.011F)), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-3.25F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(48, 50).addBox(-4.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(56, 0).addBox(-4.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(56, 40).addBox(-4.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(32, 50).addBox(-2.75F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.11F))
                .texOffs(56, 20).addBox(-2.75F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(0, 0).mirror().addBox(-2.75F, -0.25F, 2.25F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.011F)).mirror(false), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 20).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.2001F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(32, 60).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.18F))
                .texOffs(40, 30).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(32, 42).addBox(-2.0F, 5.5F, -2.3F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(40, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0001F))
                .texOffs(48, 60).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.179F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(0, 62).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.181F))
                .texOffs(0, 42).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.011F))
                .texOffs(32, 45).addBox(-2.0F, 5.5F, -2.3F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(16, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 62).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.178F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -2.0F));

        PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

        PartDefinition cube_r1 = RightArmBAM.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 121).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r1 = RightArmBAM.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

        PartDefinition cube_r2 = RightArmBAM2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 121).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r2 = RightArmBAM2.addOrReplaceChild("RightArm_r2", CubeListBuilder.create().texOffs(0, 112).addBox(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

        PartDefinition cube_r3 = RightArmBAM3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 121).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r3 = RightArmBAM3.addOrReplaceChild("RightArm_r3", CubeListBuilder.create().texOffs(0, 112).addBox(0.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -9.0F, 0.0F));

        PartDefinition cube_r4 = LeftArmBAM.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-3.25F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(-3.25F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r1 = LeftArmBAM.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-3.25F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.75F, -1.75F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

        PartDefinition cube_r5 = LeftArmBAM4.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r2 = LeftArmBAM4.addOrReplaceChild("LeftArm_r2", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

        PartDefinition cube_r6 = LeftArmBAM3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r3 = LeftArmBAM3.addOrReplaceChild("LeftArm_r3", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-4.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition kick_barrage = stand2.addOrReplaceChild("kick_barrage", CubeListBuilder.create(), PartPose.offset(-11.0F, -12.0F, -2.0F));

        PartDefinition One = kick_barrage.addOrReplaceChild("One", CubeListBuilder.create().texOffs(45, 118).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(61, 119).mirror().addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition upper_left_leg_r1 = One.addOrReplaceChild("upper_left_leg_r1", CubeListBuilder.create().texOffs(66, 116).addBox(9.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

        PartDefinition base_r1 = One.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(45, 110).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

        PartDefinition Two = kick_barrage.addOrReplaceChild("Two", CubeListBuilder.create().texOffs(45, 118).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(61, 119).mirror().addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition upper_left_leg_r2 = Two.addOrReplaceChild("upper_left_leg_r2", CubeListBuilder.create().texOffs(66, 116).addBox(9.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

        PartDefinition base_r2 = Two.addOrReplaceChild("base_r2", CubeListBuilder.create().texOffs(45, 110).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

        PartDefinition Three = kick_barrage.addOrReplaceChild("Three", CubeListBuilder.create().texOffs(45, 118).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(61, 119).mirror().addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition upper_left_leg_r3 = Three.addOrReplaceChild("upper_left_leg_r3", CubeListBuilder.create().texOffs(66, 116).addBox(9.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

        PartDefinition base_r3 = Three.addOrReplaceChild("base_r3", CubeListBuilder.create().texOffs(45, 110).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

        PartDefinition Four = kick_barrage.addOrReplaceChild("Four", CubeListBuilder.create().texOffs(45, 118).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(61, 119).mirror().addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition upper_left_leg_r4 = Four.addOrReplaceChild("upper_left_leg_r4", CubeListBuilder.create().texOffs(66, 116).addBox(9.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

        PartDefinition base_r4 = Four.addOrReplaceChild("base_r4", CubeListBuilder.create().texOffs(45, 110).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    StandPowers Power = new PowersD4C(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
        this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1f);
        this.animate(pEntity.hideLeg, StandAnimations.HIDE_LEG, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage, StandAnimations.KICK_BARRAGE, pAgeInTicks, 1.25f);
        this.animate(pEntity.kick_barrage_windup, StandAnimations.KICK_BARRAGE_CHARGE, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage_end, StandAnimations.KICK_BARRAGE_END, pAgeInTicks, 1f);
        this.animate(pEntity.parallelWorldWindupAnimationState, StandAnimations.TIMESTOP, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}
