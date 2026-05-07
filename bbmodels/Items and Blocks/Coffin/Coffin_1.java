// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Coffin_1<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "coffin_1"), "main");
	private final ModelPart coffin;
	private final ModelPart lid;
	private final ModelPart bottom;

	public Coffin_1(ModelPart root) {
		this.coffin = root.getChild("coffin");
		this.lid = this.coffin.getChild("lid");
		this.bottom = this.coffin.getChild("bottom");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition coffin = partdefinition.addOrReplaceChild("coffin", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition lid = coffin.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -16.0F, 12.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -9.0F, 8.0F));

		PartDefinition bottom = coffin.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 69).addBox(0.0F, 7.0F, -14.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(0.0F, 0.0F, -16.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 19).addBox(-2.0F, 0.0F, -16.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(12, 44).addBox(12.0F, 0.0F, -16.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -9.0F, 8.0F));

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