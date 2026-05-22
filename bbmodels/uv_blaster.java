// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class uv<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "uv"), "main");
	private final ModelPart backpack;
	private final ModelPart bone;
	private final ModelPart bone2;

	public uv(ModelPart root) {
		this.backpack = root.getChild("backpack");
		this.bone = this.backpack.getChild("bone");
		this.bone2 = this.backpack.getChild("bone2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition backpack = partdefinition.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -1.0F, 8.0F, 9.0F, 3.0F, new CubeDeformation(0.22F)), PartPose.offset(0.0F, 6.0F, 3.0F));

		PartDefinition bone = backpack.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, -3.0F, -5.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.04F))
		.texOffs(16, 12).addBox(0.0F, -3.0F, -10.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.23F)), PartPose.offsetAndRotation(4.0F, -6.0F, 2.0F, -0.1767F, -0.1264F, 0.0341F));

		PartDefinition bone2 = backpack.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -3.0F, -5.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.04F))
		.texOffs(16, 20).addBox(-3.0F, -3.0F, -10.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.23F)), PartPose.offsetAndRotation(-4.0F, -6.0F, 2.0F, -0.1767F, 0.1264F, -0.0341F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		backpack.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}