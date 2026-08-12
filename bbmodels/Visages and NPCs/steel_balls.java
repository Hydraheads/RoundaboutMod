// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class steel_balls<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "steel_balls"), "main");
	private final ModelPart body;
	private final ModelPart left_SB;
	private final ModelPart right_SB;

	public steel_balls(ModelPart root) {
		this.body = root.getChild("body");
		this.left_SB = this.body.getChild("left_SB");
		this.right_SB = this.body.getChild("right_SB");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(4, 53).mirror().addBox(3.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(4, 53).addBox(-6.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 11.0F, 0.0F));

		PartDefinition left_SB = body.addOrReplaceChild("left_SB", CubeListBuilder.create().texOffs(25, 48).mirror().addBox(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(25, 54).mirror().addBox(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.15F)).mirror(false), PartPose.offsetAndRotation(5.0F, 3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition right_SB = body.addOrReplaceChild("right_SB", CubeListBuilder.create().texOffs(25, 48).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 54).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(-5.0F, 3.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}