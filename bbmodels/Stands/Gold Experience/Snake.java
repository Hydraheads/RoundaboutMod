// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Snake<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "snake"), "main");
	private final ModelPart Baby Snake;
	private final ModelPart Neck;
	private final ModelPart Head;
	private final ModelPart Tongue;
	private final ModelPart Tail1;
	private final ModelPart Tail2;
	private final ModelPart Rattle;

	public Snake(ModelPart root) {
		this.Baby Snake = root.getChild("Baby Snake");
		this.Neck = this.Baby Snake.getChild("Neck");
		this.Head = this.Neck.getChild("Head");
		this.Tongue = this.Head.getChild("Tongue");
		this.Tail1 = this.Baby Snake.getChild("Tail1");
		this.Tail2 = this.Tail1.getChild("Tail2");
		this.Rattle = this.Tail2.getChild("Rattle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Baby Snake = partdefinition.addOrReplaceChild("Baby Snake", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 5.0F));

		PartDefinition Neck = Baby Snake.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(1, 23).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -7.0F));

		PartDefinition Head = Neck.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -5.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(9, 13).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).mirror().addBox(-4.0F, -2.0F, 0.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(0, 16).addBox(0.0F, -2.0F, 0.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition Tongue = Head.addOrReplaceChild("Tongue", CubeListBuilder.create().texOffs(-3, 11).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition Tail1 = Baby Snake.addOrReplaceChild("Tail1", CubeListBuilder.create().texOffs(14, 23).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -1.0F, -6.0F));

		PartDefinition Tail2 = Tail1.addOrReplaceChild("Tail2", CubeListBuilder.create().texOffs(14, 11).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 7.0F));

		PartDefinition Rattle = Tail2.addOrReplaceChild("Rattle", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -4.2F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -1.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Baby Snake.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}