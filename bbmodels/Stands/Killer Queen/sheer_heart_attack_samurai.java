// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class sheer_heart_attack_samuraiV2<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "sheer_heart_attack_samuraiv2"), "main");
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart torch;
	private final ModelPart details;
	private final ModelPart spout;

	public sheer_heart_attack_samuraiV2(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.torch = this.head.getChild("torch");
		this.details = this.head.getChild("details");
		this.spout = this.head.getChild("spout");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(-4.0F, -3.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(18, 10).addBox(3.0F, -3.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(24, 4).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(24, 5).addBox(-2.0F, -5.0F, 2.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(8, 21).addBox(2.0F, -5.0F, -2.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 21).addBox(-2.0F, -5.0F, -2.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 29).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-1.5F, -4.0F, -4.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torch = head.addOrReplaceChild("torch", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition cube_r1 = torch.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(35, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 3.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition details = head.addOrReplaceChild("details", CubeListBuilder.create().texOffs(37, 6).addBox(-1.0F, -7.0F, -3.0F, 2.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition spout = head.addOrReplaceChild("spout", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.75F, -3.75F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r2 = spout.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(15, 28).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.25F, 0.75F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r3 = spout.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(7, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.75F, 0.75F, -1.5708F, 0.0F, 0.0F));

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