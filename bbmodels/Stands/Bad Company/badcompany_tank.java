// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class badcompany_tank<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "badcompany_tank"), "main");
	private final ModelPart Cannon;
	private final ModelPart bb_main;

	public badcompany_tank(ModelPart root) {
		this.Cannon = root.getChild("Cannon");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Cannon = partdefinition.addOrReplaceChild("Cannon", CubeListBuilder.create().texOffs(0, 12).addBox(-0.5F, -3.0F, -5.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -1.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(14, 12).addBox(-3.0F, -2.0F, -1.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.25F))
		.texOffs(0, 19).addBox(2.0F, -2.0F, -1.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.3F))
		.texOffs(0, 7).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(12, 19).addBox(2.25F, -2.0F, -1.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.2F))
		.texOffs(20, 0).addBox(-3.25F, -2.0F, -1.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Cannon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}