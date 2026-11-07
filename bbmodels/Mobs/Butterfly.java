// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Butterfly<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "butterfly"), "main");
	private final ModelPart butterfly;
	private final ModelPart right_wing;
	private final ModelPart left_wing;

	public Butterfly(ModelPart root) {
		this.butterfly = root.getChild("butterfly");
		this.right_wing = this.butterfly.getChild("right_wing");
		this.left_wing = this.butterfly.getChild("left_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition butterfly = partdefinition.addOrReplaceChild("butterfly", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, -3.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 23.0F, 1.0F));

		PartDefinition left_antennae_r1 = butterfly.addOrReplaceChild("left_antennae_r1", CubeListBuilder.create().texOffs(10, 0).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.5F, -3.5F, -0.7854F, -0.7854F, 0.0F));

		PartDefinition right_antennae_r1 = butterfly.addOrReplaceChild("right_antennae_r1", CubeListBuilder.create().texOffs(10, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -3.5F, -0.7854F, 0.7854F, 0.0F));

		PartDefinition right_wing = butterfly.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(-7, 0).addBox(-6.0F, 0.0F, -3.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_wing = butterfly.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(-7, 0).mirror().addBox(0.0F, 0.0F, -3.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 14, 14);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		butterfly.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}