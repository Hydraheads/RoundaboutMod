// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ripper_eyes<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ripper_eyes"), "main");
	private final ModelPart laser;
	private final ModelPart right_laser;
	private final ModelPart left_laser;
	private final ModelPart right_laser2;
	private final ModelPart left_laser2;

	public ripper_eyes(ModelPart root) {
		this.laser = root.getChild("laser");
		this.right_laser = this.laser.getChild("right_laser");
		this.left_laser = this.laser.getChild("left_laser");
		this.right_laser2 = this.laser.getChild("right_laser2");
		this.left_laser2 = this.laser.getChild("left_laser2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition laser = partdefinition.addOrReplaceChild("laser", CubeListBuilder.create(), PartPose.offset(0.0F, -23.0F, -1.0F));

		PartDefinition right_laser = laser.addOrReplaceChild("right_laser", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition left_laser = laser.addOrReplaceChild("left_laser", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.0F));

		PartDefinition right_laser2 = laser.addOrReplaceChild("right_laser2", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition left_laser2 = laser.addOrReplaceChild("left_laser2", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		laser.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}