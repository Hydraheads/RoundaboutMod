// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.client.models.stand.animations.StarPlatinumAnimations;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class StarPlatinumModel<T extends StarPlatinumEntity> extends StandModel<T> {
	ModelPart finger;
	public StarPlatinumModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.head = stand.getChild("stand2").getChild("head");
		this.body = stand.getChild("stand2").getChild("body");
		this.finger = stand.getChild("stand2").getChild("body").getChild("body2")
				.getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm")
				.getChild("finger");
		this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
				.getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
		this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
				.getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
	}

	@Override
	public void defaultModifiers(T entity) {
		Minecraft mc = Minecraft.getInstance();
		if (entity.getUser() != null) {
			LivingEntity User = entity.getUser();
			if (!mc.isPaused() && !(((TimeStop) entity.level()).CanTimeStopEntity(User))) {
				float tickDelta = mc.getDeltaFrameTime();
				entity.fingerInterpolation = MainUtil.controlledLerp(tickDelta, entity.fingerInterpolation, entity.getFingerLength(), 1f);
				finger.yScale = entity.fingerInterpolation;
			}
		}
		super.defaultModifiers(entity);
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(45, 29).addBox(-5.0F, -4.4F, -1.5F, 10.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F))
				.texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 6).addBox(-1.0F, -0.36F, -4.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(96, 56).addBox(-0.5F, -6.2F, -4.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(100, 55).addBox(-4.218F, -5.3824F, -4.25F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.01F))
				.texOffs(101, 51).addBox(-3.2006F, -6.1831F, -4.25F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(100, 55).mirror().addBox(1.218F, -5.3824F, -4.25F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.01F)).mirror(false)
				.texOffs(101, 51).mirror().addBox(1.2006F, -6.1831F, -4.25F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hair = head2.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0121F, -7.2155F, -3.6373F, 3.0036F, 0.0097F, 3.1368F));

		PartDefinition middle_tuff = hair.addOrReplaceChild("middle_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0532F, 0.0616F, -1.1443F, -0.0873F, 0.0F, 0.0F));

		PartDefinition middle_tuff_r1 = middle_tuff.addOrReplaceChild("middle_tuff_r1", CubeListBuilder.create().texOffs(101, 88).addBox(0.0F, -1.5F, -2.05F, 2.0F, 2.0F, 2.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition middle_tuft2 = middle_tuff.addOrReplaceChild("middle_tuft2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0024F, -3.4145F, -0.9799F, 0.6109F, 0.0F, 0.0F));

		PartDefinition middle_tuft3_r1 = middle_tuft2.addOrReplaceChild("middle_tuft3_r1", CubeListBuilder.create().texOffs(101, 97).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9876F, -1.6968F, -4.8218F, 0.8727F, 0.0F, 0.0F));

		PartDefinition middle_tuft2_r1 = middle_tuft2.addOrReplaceChild("middle_tuft2_r1", CubeListBuilder.create().texOffs(101, 92).addBox(0.0F, -5.4F, -0.6F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(-0.0024F, 2.5756F, 0.458F, 0.9599F, 0.0F, 0.0F));

		PartDefinition middle_tuff2 = hair.addOrReplaceChild("middle_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.081F, 1.2514F, -4.2602F, 0.2182F, 0.0F, 0.0F));

		PartDefinition middle_tuff_r2 = middle_tuff2.addOrReplaceChild("middle_tuff_r2", CubeListBuilder.create().texOffs(101, 88).addBox(0.0F, -1.5F, -2.05F, 2.0F, 2.0F, 2.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition middle_tuft3 = middle_tuff2.addOrReplaceChild("middle_tuft3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0024F, -3.4145F, -0.9799F, 0.2618F, 0.0F, 0.0F));

		PartDefinition middle_tuft4_r1 = middle_tuft3.addOrReplaceChild("middle_tuft4_r1", CubeListBuilder.create().texOffs(101, 97).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(1.0076F, -1.1129F, -3.9246F, 0.829F, 0.0F, 0.0F));

		PartDefinition middle_tuft3_r2 = middle_tuft3.addOrReplaceChild("middle_tuft3_r2", CubeListBuilder.create().texOffs(101, 92).addBox(0.0F, -4.4F, -0.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(-0.0024F, 2.5756F, 0.458F, 0.9599F, 0.0F, 0.0F));

		PartDefinition middle_tuff3 = hair.addOrReplaceChild("middle_tuff3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.08F, 4.1256F, -5.1237F, 0.6109F, 0.0F, 0.0F));

		PartDefinition middle_tuff_r3 = middle_tuff3.addOrReplaceChild("middle_tuff_r3", CubeListBuilder.create().texOffs(101, 88).addBox(0.0F, -1.5F, -2.05F, 2.0F, 2.0F, 2.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition middle_tuft4 = middle_tuff3.addOrReplaceChild("middle_tuft4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0024F, -3.4145F, -0.9799F, 0.2618F, 0.0F, 0.0F));

		PartDefinition middle_tuft5_r1 = middle_tuft4.addOrReplaceChild("middle_tuft5_r1", CubeListBuilder.create().texOffs(101, 97).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(1.0076F, -1.1129F, -3.9246F, 0.829F, 0.0F, 0.0F));

		PartDefinition middle_tuft4_r2 = middle_tuft4.addOrReplaceChild("middle_tuft4_r2", CubeListBuilder.create().texOffs(101, 92).addBox(0.0F, -4.4F, -0.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(-0.0024F, 2.5756F, 0.4579F, 0.9599F, 0.0F, 0.0F));

		PartDefinition right_tuff = hair.addOrReplaceChild("right_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(2.1678F, 0.3166F, -1.1818F, -0.0206F, 0.173F, -0.0853F));

		PartDefinition right_tuff_r1 = right_tuff.addOrReplaceChild("right_tuff_r1", CubeListBuilder.create().texOffs(92, 88).addBox(0.0F, 0.5F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.5996F, -0.1245F, 0.1796F));

		PartDefinition right_tuft2 = right_tuff.addOrReplaceChild("right_tuft2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.9469F, -3.067F, -0.8842F, 0.2583F, 0.0036F, 0.0433F));

		PartDefinition right_tuft3_r1 = right_tuft2.addOrReplaceChild("right_tuft3_r1", CubeListBuilder.create().texOffs(91, 98).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5746F, -0.4757F, -3.8234F, -0.4718F, -0.1245F, 0.1796F));

		PartDefinition right_tuft2_r1 = right_tuft2.addOrReplaceChild("right_tuft2_r1", CubeListBuilder.create().texOffs(91, 93).addBox(-1.0F, -1.6F, -0.4F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4292F, 0.8293F, -1.8203F, 1.2541F, -0.1245F, 0.1796F));

		PartDefinition right_tuff2 = hair.addOrReplaceChild("right_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.9407F, 1.1226F, -4.1063F, 0.0721F, 0.208F, 0.1049F));

		PartDefinition right_tuff_r2 = right_tuff2.addOrReplaceChild("right_tuff_r2", CubeListBuilder.create().texOffs(92, 102).addBox(0.0F, 0.5F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.5996F, -0.1245F, 0.1796F));

		PartDefinition right_tuft3 = right_tuff2.addOrReplaceChild("right_tuft3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.9469F, -3.067F, -0.8842F, 0.2583F, 0.0036F, 0.0433F));

		PartDefinition right_tuft4_r1 = right_tuft3.addOrReplaceChild("right_tuft4_r1", CubeListBuilder.create().texOffs(91, 112).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5746F, -0.4757F, -3.8234F, -0.4718F, -0.1245F, 0.1796F));

		PartDefinition right_tuft3_r2 = right_tuft3.addOrReplaceChild("right_tuft3_r2", CubeListBuilder.create().texOffs(91, 107).addBox(-1.0F, -1.6F, -0.4F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4292F, 0.8293F, -1.8203F, 1.2541F, -0.1245F, 0.1796F));

		PartDefinition right_tuff3 = hair.addOrReplaceChild("right_tuff3", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9678F, 2.2469F, -6.7173F, 0.7976F, 0.2203F, 0.0873F));

		PartDefinition right_tuff_r3 = right_tuff3.addOrReplaceChild("right_tuff_r3", CubeListBuilder.create().texOffs(92, 116).addBox(0.0F, 0.5F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.5996F, -0.1245F, 0.1796F));

		PartDefinition right_tuff4 = right_tuff3.addOrReplaceChild("right_tuff4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.8476F, -1.9564F, -2.5033F, -0.1745F, 0.0F, 0.0F));

		PartDefinition right_tuff_r4 = right_tuff4.addOrReplaceChild("right_tuff_r4", CubeListBuilder.create().texOffs(93, 121).addBox(0.0F, 1.5F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0225F, -3.1633F, -2.6195F, 0.5996F, -0.1245F, 0.1796F));

		PartDefinition farright_tuff = hair.addOrReplaceChild("farright_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(4.2659F, 2.6394F, -3.3368F, -0.0315F, 0.0778F, -0.307F));

		PartDefinition farright_tuff_r1 = farright_tuff.addOrReplaceChild("farright_tuff_r1", CubeListBuilder.create().texOffs(82, 88).addBox(-1.0F, -0.2777F, -0.7076F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -1.0F, -2.5F, 1.0198F, -0.0819F, 0.7922F));

		PartDefinition farright_tufftip = farright_tuff.addOrReplaceChild("farright_tufftip", CubeListBuilder.create(), PartPose.offset(1.6789F, -1.8535F, -2.2002F));

		PartDefinition farright_tuff2_r1 = farright_tufftip.addOrReplaceChild("farright_tuff2_r1", CubeListBuilder.create().texOffs(81, 93).addBox(-2.0F, -2.5F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-0.6116F, 0.6395F, -1.4626F, 1.587F, -0.0819F, 0.7922F));

		PartDefinition farright_tuff2 = hair.addOrReplaceChild("farright_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.7562F, 4.8971F, -5.0377F, 0.1257F, 0.0675F, -0.5195F));

		PartDefinition farright_tuff_r2 = farright_tuff2.addOrReplaceChild("farright_tuff_r2", CubeListBuilder.create().texOffs(82, 99).addBox(-1.0F, -0.2777F, -0.7076F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -1.0F, -2.5F, 1.0198F, -0.0819F, 0.7922F));

		PartDefinition farright_tuff4 = farright_tuff2.addOrReplaceChild("farright_tuff4", CubeListBuilder.create(), PartPose.offset(1.6789F, -1.8535F, -2.2002F));

		PartDefinition farright_tuff3_r1 = farright_tuff4.addOrReplaceChild("farright_tuff3_r1", CubeListBuilder.create().texOffs(81, 104).addBox(-2.0F, -2.5F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(-0.6116F, 0.6395F, -1.4626F, 1.587F, -0.0819F, 0.7922F));

		PartDefinition left_tuff = hair.addOrReplaceChild("left_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.1921F, 0.3166F, -1.1818F, -0.0206F, -0.173F, 0.0853F));

		PartDefinition left_tuff_r1 = left_tuff.addOrReplaceChild("left_tuff_r1", CubeListBuilder.create().texOffs(92, 88).mirror().addBox(-2.0F, 0.5F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.51F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.5996F, 0.1245F, -0.1796F));

		PartDefinition left_tuft2 = left_tuff.addOrReplaceChild("left_tuft2", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.9469F, -3.067F, -0.8842F, 0.2583F, -0.0036F, -0.0433F));

		PartDefinition left_tuft4_r1 = left_tuft2.addOrReplaceChild("left_tuft4_r1", CubeListBuilder.create().texOffs(91, 98).mirror().addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5746F, -0.4757F, -3.8234F, -0.4718F, 0.1245F, -0.1796F));

		PartDefinition left_tuft3_r1 = left_tuft2.addOrReplaceChild("left_tuft3_r1", CubeListBuilder.create().texOffs(91, 93).mirror().addBox(-2.0F, -1.6F, -0.4F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.4292F, 0.8293F, -1.8203F, 1.2541F, 0.1245F, -0.1796F));

		PartDefinition left_tuff2 = hair.addOrReplaceChild("left_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.965F, 1.1226F, -4.1063F, 0.0721F, -0.208F, -0.1049F));

		PartDefinition left_tuff_r2 = left_tuff2.addOrReplaceChild("left_tuff_r2", CubeListBuilder.create().texOffs(92, 102).mirror().addBox(-2.0F, 0.5F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.51F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.5996F, 0.1245F, -0.1796F));

		PartDefinition left_tuft3 = left_tuff2.addOrReplaceChild("left_tuft3", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.9469F, -3.067F, -0.8842F, 0.2583F, -0.0036F, -0.0433F));

		PartDefinition left_tuft5_r1 = left_tuft3.addOrReplaceChild("left_tuft5_r1", CubeListBuilder.create().texOffs(91, 112).mirror().addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5746F, -0.4757F, -3.8234F, -0.4718F, 0.1245F, -0.1796F));

		PartDefinition left_tuft4_r2 = left_tuft3.addOrReplaceChild("left_tuft4_r2", CubeListBuilder.create().texOffs(91, 107).mirror().addBox(-2.0F, -1.6F, -0.4F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.4292F, 0.8293F, -1.8203F, 1.2541F, 0.1245F, -0.1796F));

		PartDefinition left_tuff3 = hair.addOrReplaceChild("left_tuff3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9921F, 2.2469F, -6.7173F, 0.7976F, -0.2203F, -0.0873F));

		PartDefinition left_tuff_r3 = left_tuff3.addOrReplaceChild("left_tuff_r3", CubeListBuilder.create().texOffs(92, 116).mirror().addBox(-2.0F, 0.5F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.51F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.5996F, 0.1245F, -0.1796F));

		PartDefinition left_tuff4 = left_tuff3.addOrReplaceChild("left_tuff4", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.8476F, -1.9564F, -2.5033F, -0.1745F, 0.0F, 0.0F));

		PartDefinition left_tuff_r4 = left_tuff4.addOrReplaceChild("left_tuff_r4", CubeListBuilder.create().texOffs(93, 121).mirror().addBox(-2.0F, 1.5F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.51F)).mirror(false), PartPose.offsetAndRotation(-0.0225F, -3.1633F, -2.6195F, 0.5996F, 0.1245F, -0.1796F));

		PartDefinition farleft_tuff = hair.addOrReplaceChild("farleft_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.2902F, 2.6394F, -3.3368F, -0.0315F, -0.0778F, 0.307F));

		PartDefinition farleft_tuff_r1 = farleft_tuff.addOrReplaceChild("farleft_tuff_r1", CubeListBuilder.create().texOffs(82, 88).mirror().addBox(-1.0F, -0.2777F, -0.7076F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0F, -2.5F, 1.0198F, 0.0819F, -0.7922F));

		PartDefinition farleft_tufftip = farleft_tuff.addOrReplaceChild("farleft_tufftip", CubeListBuilder.create(), PartPose.offset(-1.6789F, -1.8535F, -2.2002F));

		PartDefinition farleft_tuff3_r1 = farleft_tufftip.addOrReplaceChild("farleft_tuff3_r1", CubeListBuilder.create().texOffs(81, 93).mirror().addBox(-1.0F, -2.5F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offsetAndRotation(0.6116F, 0.6395F, -1.4626F, 1.587F, 0.0819F, -0.7922F));

		PartDefinition farleft_tuff2 = hair.addOrReplaceChild("farleft_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.7805F, 4.8971F, -5.0377F, 0.1257F, -0.0675F, 0.5195F));

		PartDefinition farleft_tuff_r2 = farleft_tuff2.addOrReplaceChild("farleft_tuff_r2", CubeListBuilder.create().texOffs(82, 99).mirror().addBox(-1.0F, -0.2777F, -0.7076F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0F, -2.5F, 1.0198F, 0.0819F, -0.7922F));

		PartDefinition farleft_tuff4 = farleft_tuff2.addOrReplaceChild("farleft_tuff4", CubeListBuilder.create(), PartPose.offset(-1.6789F, -1.8535F, -2.2002F));

		PartDefinition farleft_tuff4_r1 = farleft_tuff4.addOrReplaceChild("farleft_tuff4_r1", CubeListBuilder.create().texOffs(81, 104).mirror().addBox(-1.0F, -2.5F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offsetAndRotation(0.6116F, 0.6395F, -1.4626F, 1.587F, 0.0819F, -0.7922F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(30, 18).addBox(-4.5F, -7.0F, -2.5F, 9.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 43).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(28, 69).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(76, 78).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition lower_left_leg_r1 = upper_right_arm.addOrReplaceChild("lower_left_leg_r1", CubeListBuilder.create().texOffs(5, 59).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 4.3F, 2.45F, 3.1416F, 0.0F, -3.1416F));

		PartDefinition right_shoulder_pad = upper_right_arm.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = right_shoulder_pad.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(99, 66).addBox(-5.0F, -2.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(69, 16).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 75).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(12, 69).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(60, 74).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg_r1 = upper_left_arm.addOrReplaceChild("lower_right_leg_r1", CubeListBuilder.create().texOffs(5, 59).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.4F, 2.45F, 3.1416F, 0.0F, 3.1416F));

		PartDefinition left_shoulder_pad = upper_left_arm.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition cube_r2 = left_shoulder_pad.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(99, 66).mirror().addBox(-1.0F, -2.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(68, 6).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(74, 36).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition finger = lower_left_arm.addOrReplaceChild("finger", CubeListBuilder.create().texOffs(33, 124).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.375F, -0.925F, 0.0F, 0.0F, -3.1416F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(34, 39).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_straps = lower_torso.addOrReplaceChild("lower_straps", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(47, 12).addBox(-4.05F, 0.0F, -2.5F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition belt_r1 = belt.addOrReplaceChild("belt_r1", CubeListBuilder.create().texOffs(113, 54).mirror().addBox(-0.5F, -1.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.45F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition belt_r2 = belt.addOrReplaceChild("belt_r2", CubeListBuilder.create().texOffs(113, 54).addBox(-0.5F, -1.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.45F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition back_belt = belt.addOrReplaceChild("back_belt", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.1F, 0.15F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.55F));

		PartDefinition front_belt = belt.addOrReplaceChild("front_belt", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -0.125F, -0.1F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.6F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(72, 57).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(5, 59).addBox(-1.0F, -1.1F, -2.3F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(71, 26).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(44, 73).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(60, 62).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(5, 59).mirror().addBox(-1.0F, -1.1F, -2.3F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(72, 69).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

		PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

		PartDefinition cube_r3 = RightArmBAM.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 121).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 118).mirror().addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArm_r1 = RightArmBAM.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

		PartDefinition cube_r4 = RightArmBAM2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 121).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 118).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArm_r2 = RightArmBAM2.addOrReplaceChild("RightArm_r2", CubeListBuilder.create().texOffs(0, 112).addBox(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

		PartDefinition cube_r5 = RightArmBAM3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 121).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 118).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition RightArm_r3 = RightArmBAM3.addOrReplaceChild("RightArm_r3", CubeListBuilder.create().texOffs(0, 112).addBox(0.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -9.0F, 0.0F));

		PartDefinition cube_r6 = LeftArmBAM.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-3.25F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(16, 118).addBox(-3.25F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArm_r1 = LeftArmBAM.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-3.25F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.75F, -1.75F, -1.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

		PartDefinition cube_r7 = LeftArmBAM4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(16, 118).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArm_r2 = LeftArmBAM4.addOrReplaceChild("LeftArm_r2", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

		PartDefinition cube_r8 = LeftArmBAM3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(16, 118).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition LeftArm_r3 = LeftArmBAM3.addOrReplaceChild("LeftArm_r3", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-4.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition kick_barrage = stand2.addOrReplaceChild("kick_barrage", CubeListBuilder.create(), PartPose.offset(-11.0F, -12.0F, -2.0F));

		PartDefinition One = kick_barrage.addOrReplaceChild("One", CubeListBuilder.create().texOffs(45, 118).mirror().addBox(-2.0F, 3.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false)
				.texOffs(61, 119).mirror().addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition upper_left_leg_r1 = One.addOrReplaceChild("upper_left_leg_r1", CubeListBuilder.create().texOffs(67, 117).addBox(10.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.475F, 3.475F, -2.4F, -0.0873F, 0.0F, 0.0F));

		PartDefinition base_r1 = One.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(45, 110).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		PartDefinition Two = kick_barrage.addOrReplaceChild("Two", CubeListBuilder.create().texOffs(45, 118).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
				.texOffs(61, 119).mirror().addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition upper_left_leg_r2 = Two.addOrReplaceChild("upper_left_leg_r2", CubeListBuilder.create().texOffs(67, 117).addBox(10.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.475F, 3.475F, -2.4F, -0.0873F, 0.0F, 0.0F));

		PartDefinition base_r2 = Two.addOrReplaceChild("base_r2", CubeListBuilder.create().texOffs(45, 110).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		PartDefinition Three = kick_barrage.addOrReplaceChild("Three", CubeListBuilder.create().texOffs(45, 118).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
				.texOffs(61, 119).mirror().addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition upper_left_leg_r3 = Three.addOrReplaceChild("upper_left_leg_r3", CubeListBuilder.create().texOffs(67, 117).addBox(10.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.475F, 3.475F, -2.4F, -0.0873F, 0.0F, 0.0F));

		PartDefinition base_r3 = Three.addOrReplaceChild("base_r3", CubeListBuilder.create().texOffs(45, 110).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		PartDefinition Four = kick_barrage.addOrReplaceChild("Four", CubeListBuilder.create().texOffs(45, 118).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
				.texOffs(61, 119).mirror().addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition upper_left_leg_r4 = Four.addOrReplaceChild("upper_left_leg_r4", CubeListBuilder.create().texOffs(67, 117).addBox(10.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.475F, 3.475F, -2.4F, -0.0873F, 0.0F, 0.0F));

		PartDefinition base_r4 = Four.addOrReplaceChild("base_r4", CubeListBuilder.create().texOffs(45, 110).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	StandPowers Power = new PowersStarPlatinum(null);

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		defaultModifiers(pEntity);
		defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
		this.animate(pEntity.timeStopAnimationState, StandAnimations.TIMESTOP, pAgeInTicks, 1f);
		this.animate(pEntity.timeStopReleaseAnimation, StandAnimations.BLOCKBREAK, pAgeInTicks, 1f);
		this.animate(pEntity.blockGrabAnimation, StandAnimations.GRAB_BLOCK, pAgeInTicks, 1f);
		this.animate(pEntity.blockThrowAnimation, StandAnimations.THROW_BLOCK, pAgeInTicks, 1.7f);
		this.animate(pEntity.blockLoinAnimationState, StarPlatinumAnimations.BLOCK_LOIN, pAgeInTicks, 1f);
		this.animate(pEntity.itemGrabAnimation, StarPlatinumAnimations.ItemGrab, pAgeInTicks, 1f);
		this.animate(pEntity.itemThrowAnimation, StarPlatinumAnimations.ItemThrow, pAgeInTicks, 1f);
		this.animate(pEntity.blockRetractAnimation, StandAnimations.RETRACT_BLOCK, pAgeInTicks, 1.25f);
		this.animate(pEntity.itemRetractAnimation, StandAnimations.RETRACT_ITEM, pAgeInTicks, 1.25f);
		this.animate(pEntity.entityGrabAnimation, StandAnimations.GRAB_BLOCK, pAgeInTicks, 3f);
		this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1f);
		this.animate(pEntity.hideLeg, StandAnimations.HIDE_LEG, pAgeInTicks, 1f);
		this.animate(pEntity.kick_barrage, StandAnimations.KICK_BARRAGE, pAgeInTicks, 1.25f);
		this.animate(pEntity.kick_barrage_windup, StandAnimations.KICK_BARRAGE_CHARGE, pAgeInTicks, 1f);
		this.animate(pEntity.kick_barrage_end, StandAnimations.KICK_BARRAGE_END, pAgeInTicks, 1f);
		this.animate(pEntity.impale, StandAnimations.IMPALE, pAgeInTicks, 1.04f);
		this.animate(pEntity.starFinger, StarPlatinumAnimations.STAR_FINGER, pAgeInTicks, 0.55f);
		this.animate(pEntity.starFinger2, StarPlatinumAnimations.STAR_FINGER, pAgeInTicks, 7f);
		this.animate(pEntity.finalPunchWindup, StarPlatinumAnimations.FINAL_PUNCH_WINDUP, pAgeInTicks, 1f);
		this.animate(pEntity.finalPunch, StarPlatinumAnimations.FINAL_PUNCH, pAgeInTicks, 1.4f);
		this.animate(pEntity.phaseGrab, StandAnimations.PHASE_GRAB, pAgeInTicks, 0.6f);
	}

	@Override
	public ModelPart root() {
		return stand;
	}
}