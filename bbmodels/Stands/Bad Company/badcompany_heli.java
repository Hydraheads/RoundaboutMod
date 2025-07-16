// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class badcompany_heli<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "badcompany_heli"), "main");
	private final ModelPart heli_blade;
	private final ModelPart bb_main;

	public badcompany_heli(ModelPart root) {
		this.heli_blade = root.getChild("heli_blade");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition heli_blade = partdefinition.addOrReplaceChild("heli_blade", CubeListBuilder.create(), PartPose.offset(-0.8689F, 14.0F, 1.6185F));

		PartDefinition blade_r1 = heli_blade.addOrReplaceChild("blade_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, -6.5F, 1.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3378F, 0.0F, -0.3686F, 0.0F, -0.7418F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(20, 27).addBox(-2.0F, -9.0F, -0.5F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(0.5F, -9.0F, -0.5F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(30, 0).addBox(-3.5F, -9.0F, -0.5F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-8.0F, -8.0F, -0.5F, 14.0F, 1.0F, 4.0F, new CubeDeformation(-0.25F))
		.texOffs(0, 19).addBox(-1.5F, -8.0F, -1.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(20, 20).addBox(-2.5F, -8.0F, -1.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(10, 30).addBox(-1.5F, -10.0F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 30).addBox(0.5F, -6.0F, 1.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 30).addBox(-2.5F, -6.0F, 1.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 11).addBox(-3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
		.texOffs(30, 11).addBox(0.0F, -5.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
		.texOffs(20, 19).addBox(-8.0F, -7.25F, 1.5F, 14.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 6).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.0F, -7.5F, -1.0F, -0.6981F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		heli_blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}