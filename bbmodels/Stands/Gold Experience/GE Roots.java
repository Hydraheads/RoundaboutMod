// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class GE Roots<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ge_roots"), "main");
	private final ModelPart GE ROOTS;
	private final ModelPart root1;
	private final ModelPart root2;
	private final ModelPart root3;
	private final ModelPart root4;

	public GE Roots(ModelPart root) {
		this.GE ROOTS = root.getChild("GE ROOTS");
		this.root1 = this.GE ROOTS.getChild("root1");
		this.root2 = this.GE ROOTS.getChild("root2");
		this.root3 = this.GE ROOTS.getChild("root3");
		this.root4 = this.GE ROOTS.getChild("root4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition GE ROOTS = partdefinition.addOrReplaceChild("GE ROOTS", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition root1 = GE ROOTS.addOrReplaceChild("root1", CubeListBuilder.create().texOffs(0, 25).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition base_r1 = root1.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(1, 0).mirror().addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 0.0F, -2.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition base_r2 = root1.addOrReplaceChild("base_r2", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 0.0F, -2.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition base_r3 = root1.addOrReplaceChild("base_r3", CubeListBuilder.create().texOffs(1, 0).mirror().addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 0.0F, 2.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition base_r4 = root1.addOrReplaceChild("base_r4", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 0.0F, 2.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition root2 = GE ROOTS.addOrReplaceChild("root2", CubeListBuilder.create().texOffs(0, 18).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition root3 = GE ROOTS.addOrReplaceChild("root3", CubeListBuilder.create().texOffs(0, 11).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition root4 = GE ROOTS.addOrReplaceChild("root4", CubeListBuilder.create().texOffs(0, 4).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		GE ROOTS.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}