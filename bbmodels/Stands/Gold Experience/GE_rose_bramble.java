// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class GE_rose_bramble<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ge_rose_bramble"), "main");
	private final ModelPart bramble;
	private final ModelPart f1;
	private final ModelPart f2;
	private final ModelPart f3;
	private final ModelPart f4;

	public GE_rose_bramble(ModelPart root) {
		this.bramble = root.getChild("bramble");
		this.f1 = this.bramble.getChild("f1");
		this.f2 = this.bramble.getChild("f2");
		this.f3 = this.bramble.getChild("f3");
		this.f4 = this.bramble.getChild("f4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bramble = partdefinition.addOrReplaceChild("bramble", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition f1 = bramble.addOrReplaceChild("f1", CubeListBuilder.create().texOffs(0, -16).addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = f1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, -16).addBox(0.0F, -31.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition f2 = bramble.addOrReplaceChild("f2", CubeListBuilder.create().texOffs(32, -16).addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = f2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, -16).addBox(0.0F, -31.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition f3 = bramble.addOrReplaceChild("f3", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = f3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -31.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition f4 = bramble.addOrReplaceChild("f4", CubeListBuilder.create().texOffs(32, 16).addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = f4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 16).addBox(0.0F, -31.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bramble.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}