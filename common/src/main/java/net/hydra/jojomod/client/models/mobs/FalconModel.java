package net.hydra.jojomod.client.models.mobs;

import net.hydra.jojomod.client.models.stand.StandModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.mobs.animations.StrayCatEntityAnimations;
import net.hydra.jojomod.Roundabout;

import net.hydra.jojomod.client.models.substand.renderers.animations.SheerHeartAttackAnimations;
import net.hydra.jojomod.entity.mobs.FalconEntity;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class FalconModel<T extends FalconEntity> extends HierarchicalModel<T> implements HeadedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart falcon;
	private final ModelPart head;
	private final ModelPart body;
	//private final ModelPart Scarf;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart tail;
	private final ModelPart left_wing;
	private final ModelPart left_wing_rotation;
	private final ModelPart right_wing;
	private final ModelPart right_wing_rotation;

	public FalconModel(ModelPart root) {
		this.falcon = root.getChild("falcon");
		this.head = this.falcon.getChild("head");
		this.body = this.falcon.getChild("body");
		//this.Scarf = this.body.getChild("Scarf");
		this.left_leg = this.body.getChild("left_leg");
		this.right_leg = this.body.getChild("right_leg");
		this.tail = this.body.getChild("tail");
		this.left_wing = this.falcon.getChild("left_wing");
		this.left_wing_rotation = this.left_wing.getChild("left_wing_rotation");
		this.right_wing = this.falcon.getChild("right_wing");
		this.right_wing_rotation = this.right_wing.getChild("right_wing_rotation");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition falcon = partdefinition.addOrReplaceChild("falcon", CubeListBuilder.create(), PartPose.offset(0.0F, 14.5F, -0.5F));

		PartDefinition head = falcon.addOrReplaceChild("head", CubeListBuilder.create().texOffs(8, 18).addBox(-1.0F, -1.5F, -2.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(-1.0F, -2.5F, -3.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(20, 4).addBox(-0.5F, -1.5F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 7).addBox(-0.5F, -1.75F, -3.95F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(0, 25).addBox(-1.0F, -2.5F, -3.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(13, 21).addBox(0.0F, -6.975F, -3.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.025F, 0.3F));

		PartDefinition body = falcon.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -0.45F, 0.2182F, 0.0F, 0.0F));

		//PartDefinition Scarf = body.addOrReplaceChild("Scarf", CubeListBuilder.create().texOffs(18, 27).addBox(-1.5F, -0.3F, -1.9F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.425F, 0.25F));

		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 0).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 0).addBox(-0.5F, 2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 6.6598F, 0.0088F, -0.2182F, 0.0F, 0.0F));

		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(8, 14).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 0).mirror().addBox(-0.5F, 2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 6.6598F, 0.0088F, -0.2182F, 0.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(18, 18).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(-0.02F)), PartPose.offsetAndRotation(0.0F, 4.2857F, 1.618F, 0.48F, 0.0F, 0.0F));

		PartDefinition left_wing = falcon.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5F, 0.9F, 0.2F, 0.2182F, 0.0F, 0.0F));

		PartDefinition left_wing_rotation = left_wing.addOrReplaceChild("left_wing_rotation", CubeListBuilder.create().texOffs(12, 9).addBox(-0.5F, -2.5F, -1.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition right_wing = falcon.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, 0.9F, 0.2F, 0.2182F, 0.0F, 0.0F));

		PartDefinition right_wing_rotation = right_wing.addOrReplaceChild("right_wing_rotation", CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, -2.5F, -1.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		falcon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return falcon;
	}

	@Override
	public ModelPart getHead() {
		return head;
	}

	@Override
	public void setupAnim(T falcon, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.getHead().yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		this.getHead().xRot = pHeadPitch * ((float) Math.PI / 180F);
	}

}