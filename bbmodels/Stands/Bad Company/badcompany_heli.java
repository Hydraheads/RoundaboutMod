// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class helicopter2<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "helicopter2"), "main");
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart body;
	private final ModelPart propeller;
	private final ModelPart propellerSmol;
	private final ModelPart wings;

	public helicopter2(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.body = this.stand2.getChild("body");
		this.propeller = this.body.getChild("propeller");
		this.propellerSmol = this.body.getChild("propellerSmol");
		this.wings = this.body.getChild("wings");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(-1.0F, 20.5F, -2.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(20, 20).addBox(-1.0F, -0.5F, -8.5F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(14, 30).addBox(2.0F, 1.5F, -6.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 30).addBox(-1.0F, 1.5F, -6.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 11).addBox(-1.5F, 2.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
		.texOffs(30, 11).addBox(1.5F, 2.5F, -6.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
		.texOffs(0, 19).addBox(0.0F, -0.5F, -8.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(20, 27).addBox(-0.5F, -1.5F, -8.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(10, 30).addBox(0.0F, -2.5F, -6.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5015F, 0.0F, 6.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 6).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.5F, 0.0F, -8.5F, -0.6981F, 0.0F, 0.0F));

		PartDefinition propeller = body.addOrReplaceChild("propeller", CubeListBuilder.create().texOffs(28, 30).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -2.5F, -6.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition propellerSmol = body.addOrReplaceChild("propellerSmol", CubeListBuilder.create().texOffs(0, -3).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wings = body.addOrReplaceChild("wings", CubeListBuilder.create().texOffs(0, 30).addBox(2.0F, -1.5F, -8.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(30, 0).addBox(-2.0F, -1.5F, -8.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-6.5F, -0.5F, -8.0F, 14.0F, 1.0F, 4.0F, new CubeDeformation(-0.25F))
		.texOffs(20, 19).addBox(-6.5F, 0.25F, -6.0F, 14.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

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