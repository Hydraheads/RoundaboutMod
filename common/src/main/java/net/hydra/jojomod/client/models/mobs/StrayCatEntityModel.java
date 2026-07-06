package net.hydra.jojomod.client.models.mobs;

import net.hydra.jojomod.client.models.stand.StandModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.hydra.jojomod.Roundabout;

import net.hydra.jojomod.client.models.substand.renderers.animations.SheerHeartAttackAnimations;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class StrayCatEntityModel<T extends StrayCatEntity> extends HierarchicalModel<T> implements HeadedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "stray_cat_entity_model"), "main");

	private final ModelPart StrayCat;
	private final ModelPart body;
	private final ModelPart Stem;
	private final ModelPart Stemp1;
	private final ModelPart Stemp2;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart StemP3;
	private final ModelPart Head;
	private final ModelPart eyes;
	private final ModelPart right_eye;
	private final ModelPart left_eye;
	private final ModelPart mouth;
	private final ModelPart turret;
	private final ModelPart pot;

	@Override public ModelPart root() { return this.StrayCat; }
	@Override public ModelPart getHead() { return this.StemP3; }

	public StrayCatEntityModel(ModelPart root) {
		this.StrayCat = root.getChild("StrayCat");
		this.body = this.StrayCat.getChild("body");
		this.Stem = this.body.getChild("Stem");
		this.Stemp1 = this.Stem.getChild("Stemp1");
		this.Stemp2 = this.Stemp1.getChild("Stemp2");
		this.left_arm = this.Stemp2.getChild("left_arm");
		this.right_arm = this.Stemp2.getChild("right_arm");
		this.StemP3 = this.Stemp2.getChild("StemP3");
		this.Head = this.StemP3.getChild("Head");
		this.eyes = this.Head.getChild("eyes");
		this.right_eye = this.eyes.getChild("right_eye");
		this.left_eye = this.eyes.getChild("left_eye");
		this.mouth = this.Head.getChild("mouth");
		this.turret = this.StemP3.getChild("turret");
		this.pot = this.StrayCat.getChild("pot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition StrayCat = partdefinition.addOrReplaceChild("StrayCat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition body = StrayCat.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Stem = body.addOrReplaceChild("Stem", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.75F, -1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(-0.2F))
		.texOffs(0, 15).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Stemp1 = Stem.addOrReplaceChild("Stemp1", CubeListBuilder.create().texOffs(14, 16).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition cube_r1 = Stemp1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition Stemp2 = Stemp1.addOrReplaceChild("Stemp2", CubeListBuilder.create().texOffs(4, 18).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition cube_r2 = Stemp2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 17).addBox(-1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition left_arm = Stemp2.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(12, 12).addBox(0.0F, 1.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 6).addBox(0.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -1.0F, 0.0F));

		PartDefinition right_arm = Stemp2.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, 1.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 9).addBox(-2.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, 0.0F));

		PartDefinition StemP3 = Stemp2.addOrReplaceChild("StemP3", CubeListBuilder.create().texOffs(18, 15).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition cube_r3 = StemP3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 19).addBox(-1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition Head = StemP3.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, -3.75F, -0.0408F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(8, 16).addBox(-1.5F, -3.75F, 0.9592F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-2.0F, -3.5F, -1.5408F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.5F))
		.texOffs(8, 22).addBox(-2.5F, -4.5F, 1.9542F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-1.5F, -1.75F, -1.7908F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0408F));

		PartDefinition eyes = Head.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(0.0F, -2.25F, -1.0408F));

		PartDefinition right_eye = eyes.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(16, 3).addBox(-1.5F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

		PartDefinition left_eye = eyes.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(20, 3).addBox(-0.5F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

		PartDefinition mouth = Head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -0.75F, -1.8908F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition turret = StemP3.addOrReplaceChild("turret", CubeListBuilder.create().texOffs(0, 26).addBox(-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.5F))
		.texOffs(18, 30).addBox(-0.5F, -0.5F, -5.226F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.225F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition pot = StrayCat.addOrReplaceChild("pot", CubeListBuilder.create().texOffs(26, 20).addBox(-3.0F, -6.0F, -2.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(26, 32).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		StrayCat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}


	@Override
	public void setupAnim(T strayCat, float v, float v1, float v2, float v3, float v4) {

	}
}