// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class gently_weeps<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "gently_weeps"), "main");
	private final ModelPart base;
	private final ModelPart part1;
	private final ModelPart part2;
	private final ModelPart part3;

	public gently_weeps(ModelPart root) {
		this.base = root.getChild("base");
		this.part1 = this.base.getChild("part1");
		this.part2 = this.base.getChild("part2");
		this.part3 = this.base.getChild("part3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 25.0F, 0.0F));

		PartDefinition part1 = base.addOrReplaceChild("part1", CubeListBuilder.create().texOffs(2, 3).addBox(-31.6F, -5.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(-1.6F, -5.0F, 29.6F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(-1.6F, -5.0F, -32.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(28.4F, -5.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition part2 = base.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(2, 3).addBox(-35.6F, 8.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(-1.6F, 8.0F, 33.6F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(-1.6F, 8.0F, -36.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(32.4F, 8.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -33.0F, 0.0F));

		PartDefinition part3 = base.addOrReplaceChild("part3", CubeListBuilder.create().texOffs(2, 3).addBox(-31.6F, -5.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(-1.6F, -5.0F, 29.6F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(-1.6F, -5.0F, -32.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(2, 3).addBox(28.4F, -5.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -33.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}