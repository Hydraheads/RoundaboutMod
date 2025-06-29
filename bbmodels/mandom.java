// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class mandom<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "mandom"), "main");
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart tentacles;

	public mandom(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.tentacles = this.body2.getChild("tentacles");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 12.275F, 0.05F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.3223F, -12.5897F, 0.2F, 0.0F, 0.0F, -0.7505F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, -1.275F, -1.275F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5179F, -7.5351F, -4.5365F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.55F, 1.675F, 1.9F, 0.1809F, -0.435F, 0.2015F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(3.0F, 1.0F, -1.5F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.6F, -0.8F, 1.1F, 0.0F, -0.5236F, 0.0F));

		PartDefinition cube_r1 = body2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 15).addBox(-7.0F, -2.0F, 0.0F, 13.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.75F, 0.825F, 0.5236F, 0.0F, 0.0F));

		PartDefinition tentacles = body2.addOrReplaceChild("tentacles", CubeListBuilder.create(), PartPose.offset(-3.0F, 0.0F, 2.0F));

		PartDefinition cube_r2 = tentacles.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 0.375F, -1.175F, -0.3647F, -0.1586F, -0.6236F));

		PartDefinition cube_r3 = tentacles.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.425F, -1.975F, 1.75F, -0.3515F, 0.0071F, -0.3266F));

		PartDefinition cube_r4 = tentacles.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.425F, -0.8F, 0.4F, -0.2921F, -0.0775F, -0.4284F));

		PartDefinition cube_r5 = tentacles.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.475F, 2.425F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r6 = tentacles.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(12, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.525F, -0.975F, 0.086F, -0.0701F, 0.0042F));

		PartDefinition cube_r7 = tentacles.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -1.475F, 1.025F, -0.0548F, -0.0807F, 0.135F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}