// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class small_watch<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "small_watch"), "main");
	private final ModelPart watch;
	private final ModelPart scaleDown;
	private final ModelPart hand;

	public small_watch(ModelPart root) {
		this.watch = root.getChild("watch");
		this.scaleDown = this.watch.getChild("scaleDown");
		this.hand = this.scaleDown.getChild("hand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition watch = partdefinition.addOrReplaceChild("watch", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.0F));

		PartDefinition scaleDown = watch.addOrReplaceChild("scaleDown", CubeListBuilder.create().texOffs(1, 0).addBox(-4.175F, -0.9F, -3.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-5.025F, -2.425F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.5F)), PartPose.offset(1.5F, 5.5F, 0.0F));

		PartDefinition hand = scaleDown.addOrReplaceChild("hand", CubeListBuilder.create().texOffs(26, 0).addBox(-0.375F, -1.85F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offset(-4.525F, 0.075F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		watch.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}