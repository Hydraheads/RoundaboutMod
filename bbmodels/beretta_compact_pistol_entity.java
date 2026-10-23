// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class beretta_compact_pistol_Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "beretta_compact_pistol_converted"), "main");
	private final ModelPart Root;
	private final ModelPart Beretta;

	public beretta_compact_pistol_Converted(ModelPart root) {
		this.Root = root.getChild("Root");
		this.Beretta = this.Root.getChild("Beretta");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Root = partdefinition.addOrReplaceChild("Root", CubeListBuilder.create(), PartPose.offset(1.5F, 24.0F, 3.0F));

		PartDefinition Beretta = Root.addOrReplaceChild("Beretta", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, -5.775F, -4.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.025F))
		.texOffs(10, 16).addBox(-2.0F, -4.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-2.0F, -5.75F, -4.75F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.001F))
		.texOffs(16, 16).addBox(-1.5F, -4.0F, -3.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 9).addBox(-2.0F, -5.0F, -4.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}