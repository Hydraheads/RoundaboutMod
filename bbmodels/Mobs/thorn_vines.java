// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class thorn_vines<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "thorn_vines"), "main");
	private final ModelPart vine;
	private final ModelPart f1;
	private final ModelPart f2;
	private final ModelPart f3;

	public thorn_vines(ModelPart root) {
		this.vine = root.getChild("vine");
		this.f1 = this.vine.getChild("f1");
		this.f2 = this.vine.getChild("f2");
		this.f3 = this.vine.getChild("f3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition vine = partdefinition.addOrReplaceChild("vine", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition f1 = vine.addOrReplaceChild("f1", CubeListBuilder.create().texOffs(-16, 0).addBox(-8.0F, -0.25F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(32, -12).addBox(0.0F, -6.25F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = f1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, -12).addBox(0.0F, -4.5F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition f2 = vine.addOrReplaceChild("f2", CubeListBuilder.create().texOffs(-16, 16).addBox(-8.0F, -0.25F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(32, -6).addBox(0.0F, -6.25F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = f2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, -6).addBox(0.0F, -4.5F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition f3 = vine.addOrReplaceChild("f3", CubeListBuilder.create().texOffs(-16, 32).addBox(-8.0F, -0.25F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(0.0F, -6.25F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = f3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 0).addBox(0.0F, -4.5F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		vine.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}