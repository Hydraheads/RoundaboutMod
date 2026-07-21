// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class white_album_twister<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "white_album_twister"), "main");
	private final ModelPart base;
	private final ModelPart part1;
	private final ModelPart part2;

	public white_album_twister(ModelPart root) {
		this.base = root.getChild("base");
		this.part1 = this.base.getChild("part1");
		this.part2 = this.base.getChild("part2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, 0.0F));

		PartDefinition part1 = base.addOrReplaceChild("part1", CubeListBuilder.create().texOffs(0, 44).addBox(-16.0F, -6.0F, -16.0F, 32.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 1.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition part2 = base.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, 4.0F, -16.0F, 32.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -6.0F, -16.0F, 32.0F, 1.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}