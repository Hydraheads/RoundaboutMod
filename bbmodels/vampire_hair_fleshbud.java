// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class vampire_hair_fleshbud<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "vampire_hair_fleshbud"), "main");
	private final ModelPart hair;
	private final ModelPart right_flesh_bud;
	private final ModelPart left_flesh_bud;

	public vampire_hair_fleshbud(ModelPart root) {
		this.hair = root.getChild("hair");
		this.right_flesh_bud = this.hair.getChild("right_flesh_bud");
		this.left_flesh_bud = this.hair.getChild("left_flesh_bud");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition right_flesh_bud = hair.addOrReplaceChild("right_flesh_bud", CubeListBuilder.create().texOffs(-18, 0).addBox(-8.0F, 0.0F, -26.0F, 8.0F, 0.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5F, 0.0F));

		PartDefinition right_flesh_bud_r1 = right_flesh_bud.addOrReplaceChild("right_flesh_bud_r1", CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-2.5F, 0.0F, -23.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition right_incomplete_bud_r1 = right_flesh_bud.addOrReplaceChild("right_incomplete_bud_r1", CubeListBuilder.create().texOffs(30, 6).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-4.5F, 0.0F, -19.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition left_flesh_bud = hair.addOrReplaceChild("left_flesh_bud", CubeListBuilder.create().texOffs(-26, 0).mirror().addBox(0.0F, 0.0F, -26.0F, 8.0F, 0.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -5.5F, 0.0F));

		PartDefinition left_flesh_bud_r1 = left_flesh_bud.addOrReplaceChild("left_flesh_bud_r1", CubeListBuilder.create().texOffs(30, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(3.5F, 0.0F, -23.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition left_incomplete_bud_r1 = left_flesh_bud.addOrReplaceChild("left_incomplete_bud_r1", CubeListBuilder.create().texOffs(30, 6).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(4.5F, 0.0F, -19.0F, 0.0F, 0.0F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 48, 26);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hair.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}