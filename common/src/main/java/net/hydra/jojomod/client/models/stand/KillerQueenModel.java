package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.KillerQueenAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.client.models.stand.animations.StarPlatinumAnimations;
import net.hydra.jojomod.client.models.stand.animations.TheWorldAnimations;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class KillerQueenModel<T extends KillerQueenEntity> extends StandModel<T> {

	private final ModelPart Stray_Cat;
	private final ModelPart bubble;

	public KillerQueenModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
        this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
		this.Stray_Cat = this.body.getChild("body2")
				.getChild("torso").getChild("lower_chest").getChild("lower_torso").getChild("Stray_Cat");
		this.bubble = this.body.getChild("body2").getChild("torso").getChild("lower_chest").getChild("bubble");
	}

	protected ModelPart getArm(HumanoidArm p_102852_) {
		return p_102852_ == HumanoidArm.LEFT ? this.rightHand : this.leftHand;
	}

	public static LayerDefinition getTexturedModelData() {
		/*MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(71, 0).addBox(-4.0F, -7.775F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.1F))
				.texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
				.texOffs(100, 114).mirror().addBox(-3.525F, -7.375F, -3.75F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.125F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition extras = head2.addOrReplaceChild("extras", CubeListBuilder.create(), PartPose.offset(0.0F, 24.15F, 0.0F));

		PartDefinition cube_r1 = extras.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 41).addBox(-0.5F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.1F))
				.texOffs(48, 31).addBox(-6.498F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(2.999F, -31.1465F, 0.2964F, -0.8552F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.25F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(24, 21).addBox(-3.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(32, 41).addBox(-3.75F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
				.texOffs(-2, -2).mirror().addBox(-4.0F, -0.75F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(67, 49).mirror().addBox(-2.75F, 3.25F, 2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(0, 26).addBox(-1.75F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.25F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(16, 31).addBox(-0.25F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(0, 46).addBox(-0.25F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
				.texOffs(-2, -2).addBox(4.0F, -0.75F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(67, 49).addBox(0.75F, 3.25F, 2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(-2, -2).addBox(-0.25F, -0.75F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(32, 0).addBox(-2.25F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.101F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = upper_chest_only.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(99, 42).mirror().addBox(1.0F, -2.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -3.5F, 2.75F, 0.0436F, 0.3054F, 0.0F));

		PartDefinition cube_r3 = upper_chest_only.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(98, 42).addBox(-1.0F, -2.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.5F, 2.75F, 0.1343F, 0.2607F, 0.0176F));

		PartDefinition cube_r4 = upper_chest_only.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(98, 42).addBox(-1.0F, -2.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.5F, 2.75F, 0.0436F, -0.3054F, 0.0F));

		PartDefinition cube_r5 = upper_chest_only.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(99, 42).mirror().addBox(1.0F, -2.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -3.5F, 2.75F, 0.1343F, -0.2607F, -0.0176F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(68, 28).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lid = lower_torso.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(82, 54).addBox(-2.0F, 0.0F, 0.05F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, -5.0F, -2.0F));

		PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(56, 20).addBox(-1.5F, -1.75F, -1.7F, 3.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F))
				.texOffs(32, 75).addBox(-4.5F, -1.65F, -1.45F, 9.0F, 2.0F, 5.0F, new CubeDeformation(-0.4F)), PartPose.offset(0.0F, 0.3F, -1.0F));

		PartDefinition hanger = belt.addOrReplaceChild("hanger", CubeListBuilder.create(), PartPose.offset(0.5F, 0.95F, -2.675F));

		PartDefinition cube_r6 = hanger.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(64, 79).addBox(-1.5F, -0.0868F, 0.025F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.775F, 1.175F, 0.096F, 0.0F, 0.0F));

		PartDefinition cube_r7 = hanger.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(64, 80).addBox(-1.5F, -0.0868F, 0.0667F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -0.775F, 1.175F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Stray_Cat = lower_torso.addOrReplaceChild("Stray_Cat", CubeListBuilder.create().texOffs(113, 98).addBox(-1.5F, -1.75F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(113, 93).addBox(-1.5F, -3.5F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.1F))
				.texOffs(112, 85).addBox(-2.0F, -0.55F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -2.25F, 0.0F));

		PartDefinition bubble = lower_chest.addOrReplaceChild("bubble", CubeListBuilder.create().texOffs(112, 57).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(40, 21).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(48, 0).addBox(-2.0F, 1.15F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.199F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(16, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
				.texOffs(17, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 75).mirror().addBox(-1.0F, -1.5F, -2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(32, 31).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(48, 0).mirror().addBox(-2.0F, 1.15F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.199F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(0, 64).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(2, 77).addBox(-1.0F, -1.5F, -2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(16, 41).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

		PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

		PartDefinition cube_r8 = RightArmBAM.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(14, 121).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(30, 118).mirror().addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

		PartDefinition cube_r9 = RightArmBAM2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(14, 121).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(30, 118).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

		PartDefinition cube_r10 = RightArmBAM3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(14, 121).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(30, 118).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -9.0F, 0.0F));

		PartDefinition cube_r11 = LeftArmBAM.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(14, 121).mirror().addBox(-3.25F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(30, 118).addBox(-3.25F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

		PartDefinition cube_r12 = LeftArmBAM4.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(14, 121).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(30, 118).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

		PartDefinition cube_r13 = LeftArmBAM3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(14, 121).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(30, 118).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);*/

		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(71, 0).addBox(-4.0F, -7.775F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.1F))
				.texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
				.texOffs(100, 114).mirror().addBox(-3.525F, -7.375F, -3.75F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.125F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = head2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 31).addBox(-0.5F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.1F))
				.texOffs(48, 41).addBox(5.498F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-2.999F, -6.9965F, 0.2964F, -0.8552F, 0.0F, 0.0F));

		PartDefinition extras = head2.addOrReplaceChild("extras", CubeListBuilder.create().texOffs(0, 79).addBox(-5.0F, -6.25F, -4.5F, 10.0F, 1.0F, 9.0F, new CubeDeformation(0.1F))
				.texOffs(1, 100).addBox(-5.75F, -5.05F, -4.5F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.1F))
				.texOffs(76, 122).addBox(-5.0F, -12.2F, -3.6F, 10.0F, 6.0F, 0.0F, new CubeDeformation(0.1F))
				.texOffs(48, 106).addBox(-5.4F, -5.05F, 3.7F, 11.0F, 5.0F, 1.0F, new CubeDeformation(0.1F))
				.texOffs(1, 100).addBox(4.0F, -5.05F, -4.5F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.1F))
				.texOffs(0, 74).addBox(-3.0F, -8.0F, -3.5F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -1.85F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.25F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(24, 21).addBox(-3.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(32, 41).addBox(-3.75F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
				.texOffs(-2, -2).mirror().addBox(-4.0F, -0.75F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(67, 49).mirror().addBox(-2.75F, 3.25F, 2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition extra2 = upper_right_arm.addOrReplaceChild("extra2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ShoulderPad_r1 = extra2.addOrReplaceChild("ShoulderPad_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-4.0F, 0.0F, -3.0F, 7.0F, 3.0F, 5.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-4.75F, 1.25F, 0.5F, 0.0F, 0.0F, -1.5708F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(0, 26).addBox(-1.75F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition sword = lower_right_arm.addOrReplaceChild("sword", CubeListBuilder.create(), PartPose.offsetAndRotation(0.25F, 3.75F, 0.0F, -1.6144F, 0.0F, 0.0F));

		PartDefinition Handle_r1 = sword.addOrReplaceChild("Handle_r1", CubeListBuilder.create().texOffs(98, 52).addBox(-1.0F, -1.0F, 3.75F, 2.0F, 2.0F, 6.0F, new CubeDeformation(-0.001F))
				.texOffs(44, 49).addBox(0.0F, -1.0F, -16.25F, 0.0F, 2.0F, 19.0F, new CubeDeformation(-0.001F))
				.texOffs(92, 56).addBox(-1.0F, -2.0F, 2.75F, 2.0F, 4.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 5.7471F, -0.0435F, 1.5708F, 0.0F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.25F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(16, 31).addBox(-0.25F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(0, 46).addBox(-0.25F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
				.texOffs(-2, -2).addBox(4.0F, -0.75F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(67, 49).addBox(0.75F, 3.25F, 2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition extra = upper_left_arm.addOrReplaceChild("extra", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftShoulderPad_r1 = extra.addOrReplaceChild("LeftShoulderPad_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-3.5F, -1.5F, -2.5F, 7.0F, 3.0F, 5.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(3.25F, 1.75F, 0.0F, 3.1416F, 0.0F, -1.5708F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(32, 0).addBox(-2.25F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.001F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.101F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = upper_chest_only.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(99, 42).mirror().addBox(1.0F, -2.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -3.5F, 2.75F, 0.0436F, 0.3054F, 0.0F));

		PartDefinition cube_r3 = upper_chest_only.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(98, 42).addBox(-1.0F, -2.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.5F, 2.75F, 0.1343F, 0.2607F, 0.0176F));

		PartDefinition cube_r4 = upper_chest_only.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(98, 42).addBox(-1.0F, -2.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.5F, 2.75F, 0.0436F, -0.3054F, 0.0F));

		PartDefinition cube_r5 = upper_chest_only.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(99, 42).mirror().addBox(1.0F, -2.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -3.5F, 2.75F, 0.1343F, -0.2607F, -0.0176F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(68, 28).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lid = lower_torso.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(82, 54).addBox(-2.0F, 0.0F, 0.05F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, -5.0F, -2.0F));

		PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(56, 20).addBox(-1.5F, -1.75F, -1.7F, 3.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F))
				.texOffs(32, 75).addBox(-4.5F, -1.65F, -1.45F, 9.0F, 2.0F, 5.0F, new CubeDeformation(-0.4F)), PartPose.offset(0.0F, 0.3F, -1.0F));

		PartDefinition hanger = belt.addOrReplaceChild("hanger", CubeListBuilder.create(), PartPose.offset(0.5F, 0.95F, -2.675F));

		PartDefinition cube_r6 = hanger.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(64, 79).addBox(-1.5F, -0.0868F, 0.025F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.775F, 1.175F, 0.096F, 0.0F, 0.0F));

		PartDefinition cube_r7 = hanger.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(64, 80).addBox(-1.5F, -0.0868F, 0.0667F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -0.775F, 1.175F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Stray_Cat = lower_torso.addOrReplaceChild("Stray_Cat", CubeListBuilder.create().texOffs(113, 98).addBox(-1.5F, -1.75F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(113, 93).addBox(-1.5F, -3.5F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.1F))
				.texOffs(112, 85).addBox(-2.0F, -0.55F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -2.25F, 0.0F));

		PartDefinition backGround = lower_torso.addOrReplaceChild("backGround", CubeListBuilder.create().texOffs(103, 10).addBox(-4.0F, -6.0F, 2.0F, 8.0F, 5.0F, 0.0F, new CubeDeformation(-0.05F)), PartPose.offset(0.0F, 0.0F, -0.1F));

		PartDefinition swordHolder = lower_chest.addOrReplaceChild("swordHolder", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r8 = swordHolder.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(23, 106).addBox(-1.5F, -2.0F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(5.0F, 5.65F, 0.05F, 0.7854F, 0.0F, 0.0F));

		PartDefinition sword2 = swordHolder.addOrReplaceChild("sword2", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, 4.0F, -5.0F, 1.0472F, 0.0F, 0.0F));

		PartDefinition Handle_r2 = sword2.addOrReplaceChild("Handle_r2", CubeListBuilder.create().texOffs(98, 52).addBox(-1.0F, -1.0F, 3.75F, 2.0F, 2.0F, 6.0F, new CubeDeformation(-0.001F))
				.texOffs(44, 49).addBox(0.0F, -1.0F, -16.25F, 0.0F, 2.0F, 19.0F, new CubeDeformation(-0.001F))
				.texOffs(92, 56).addBox(-1.0F, -2.0F, 2.75F, 2.0F, 4.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 5.7471F, -0.0435F, 1.5708F, 0.0F, 0.0F));

		PartDefinition bubble = lower_chest.addOrReplaceChild("bubble", CubeListBuilder.create().texOffs(112, 57).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(101, 73).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition cube_r9 = bubble.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(101, 73).addBox(-1.0F, -2.0F, -1.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -3.0F, -1.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(40, 21).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(48, 0).addBox(-2.0F, 1.15F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.199F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(16, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
				.texOffs(17, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 75).mirror().addBox(-1.0F, -1.5F, -2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(32, 31).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(48, 0).mirror().addBox(-2.0F, 1.15F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.199F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(0, 64).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(2, 77).addBox(-1.0F, -1.5F, -2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(16, 41).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

		PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

		PartDefinition cube_r10 = RightArmBAM.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(14, 121).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(30, 118).mirror().addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

		PartDefinition cube_r11 = RightArmBAM2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(14, 121).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(30, 118).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

		PartDefinition cube_r12 = RightArmBAM3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(14, 121).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(30, 118).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -9.0F, 0.0F));

		PartDefinition cube_r13 = LeftArmBAM.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(14, 121).mirror().addBox(-3.25F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(30, 118).addBox(-3.25F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

		PartDefinition cube_r14 = LeftArmBAM4.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(14, 121).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(30, 118).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

		PartDefinition cube_r15 = LeftArmBAM3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(14, 121).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(30, 118).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	PowersKillerQueen Power = new PowersKillerQueen(null);

	@Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
        this.animate(pEntity.finalPunch, StarPlatinumAnimations.FINAL_PUNCH, pAgeInTicks, 1.4f);
		this.animate(pEntity.lid_open, KillerQueenAnimations.bubbleShieldAnim, pAgeInTicks, 1f);
		this.animate(pEntity.bubbleSpin, KillerQueenAnimations.bubblespin, pAgeInTicks, 1f);
		this.animate(pEntity.hideBubble, KillerQueenAnimations.hideBubble, pAgeInTicks, 1f);
		this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1F);
		this.animate(pEntity.blockPlant, KillerQueenAnimations.BombPlant, pAgeInTicks, 1.4F);
		this.animate(pEntity.itemGrab, KillerQueenAnimations.Item_Grab, pAgeInTicks, 1F);
		this.animate(pEntity.itemThrow, StandAnimations.THROW_ITEM, pAgeInTicks, 1F);
		this.animate(pEntity.detonate, KillerQueenAnimations.detonate, pAgeInTicks, 1.5F);
		this.animate(pEntity.mobBombPlant, KillerQueenAnimations.FirstBombTouchMob,   pAgeInTicks, (1/(Math.max(0.1f, (float) (PowersKillerQueen.getMobPlantWindup()) / 60))));
		this.animate(pEntity.mobBombPlant2, KillerQueenAnimations.FirstBombTouchMob2, pAgeInTicks, (1/(Math.max(0.1f, (float) (PowersKillerQueen.getMobPlantWindup()) / 60))));
		this.animate(pEntity.bubbleLaunch, KillerQueenAnimations.bubble_launch, pAgeInTicks, 0.9F);
		this.animate(pEntity.bubbleRedirect, KillerQueenAnimations.bubble_redirection, pAgeInTicks, 1F);
		this.animate(pEntity.shaSend, KillerQueenAnimations.sha_deploy, pAgeInTicks, 2F);
		this.animate(pEntity.bitesTheDust, KillerQueenAnimations.TertiaryBomb, pAgeInTicks, 1.04F);
		this.animate(pEntity.itemThrowCharge, StarPlatinumAnimations.ItemGrab, pAgeInTicks, (1/((float) (Power.getArrowThrowChargeMax()) / 20.0f)) * 0.29f);
		this.animate(pEntity.arrowThrow, StarPlatinumAnimations.ItemThrow, pAgeInTicks, 1F);
		this.animate(pEntity.itemGrabAnimation, StandAnimations.GRAB_ITEM, pAgeInTicks, 1f);
		this.animate(pEntity.itemRetractAnimation, StandAnimations.RETRACT_ITEM, pAgeInTicks, 1.25f);
		this.animate(pEntity.hideExtra, KillerQueenAnimations.hideExtra, pAgeInTicks, 1f);
		this.animate(pEntity.hideSwordInHand, KillerQueenAnimations.hideSwordInHand, pAgeInTicks, 1f);

		boolean isSamurai = false;
		if (pEntity.getUser() != null) {

			isSamurai = pEntity.getSkin() == KillerQueenEntity.SAMURAI || pEntity.getSkin() == KillerQueenEntity.SPIRIT;
		}

		if (isSamurai) {
			this.animate(pEntity.finalKickWindup, KillerQueenAnimations.HeavyKickWindupSamurai, pAgeInTicks, 1f);
			this.animate(pEntity.finalKick, KillerQueenAnimations.HeavyKickSamurai, pAgeInTicks, 0.8f);
			this.animate(pEntity.impale, KillerQueenAnimations.ImpaleSamurai, pAgeInTicks, 1.04F);
			this.animate(pEntity.heavyStrike, KillerQueenAnimations.HeavyStrikeSamurai, pAgeInTicks, 1F);
		}else {
			this.animate(pEntity.finalKickWindup, KillerQueenAnimations.HeavyKickWindup, pAgeInTicks, 1f);
			this.animate(pEntity.finalKick, KillerQueenAnimations.HeavyKick, pAgeInTicks, 0.8f);
			this.animate(pEntity.impale, KillerQueenAnimations.Impale, pAgeInTicks, 1.04F);
			this.animate(pEntity.heavyStrike, KillerQueenAnimations.HeavyStrike, pAgeInTicks, 1F);
		}
	}


	@Override
    public ModelPart root() {
        return stand;
    }
}