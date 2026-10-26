// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Coffin_item<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "coffin_item"), "main");
	private final ModelPart coffin;
	private final ModelPart left;
	private final ModelPart lid;
	private final ModelPart bottom;
	private final ModelPart right;
	private final ModelPart lid2;
	private final ModelPart bottom2;

	public Coffin_item(ModelPart root) {
		this.coffin = root.getChild("coffin");
		this.left = this.coffin.getChild("left");
		this.lid = this.left.getChild("lid");
		this.bottom = this.left.getChild("bottom");
		this.right = this.coffin.getChild("right");
		this.lid2 = this.right.getChild("lid2");
		this.bottom2 = this.right.getChild("bottom2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition coffin = partdefinition.addOrReplaceChild("coffin", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left = coffin.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -8.0F));

		PartDefinition lid = left.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -27.0F, 12.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -9.0F, 19.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition bottom = left.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 69).addBox(0.0F, 7.0F, -25.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(0.0F, 0.0F, -27.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 19).addBox(-2.0F, 0.0F, -27.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(12, 44).addBox(12.0F, 0.0F, -27.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -9.0F, 19.0F));

		PartDefinition right = coffin.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition lid2 = right.addOrReplaceChild("lid2", CubeListBuilder.create().texOffs(96, 0).addBox(0.0F, -3.0F, 0.0F, 12.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -9.0F, -8.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition bottom2 = right.addOrReplaceChild("bottom2", CubeListBuilder.create().texOffs(96, 69).addBox(-6.0F, -1.0F, 0.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(108, 19).addBox(-8.0F, -8.0F, 0.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(108, 44).addBox(6.0F, -8.0F, 0.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(96, 19).addBox(-6.0F, -8.0F, 14.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 160, 160);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		coffin.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}