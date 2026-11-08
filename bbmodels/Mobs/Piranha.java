// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Piranha<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "piranha"), "main");
	private final ModelPart root;
	private final ModelPart Piranha;
	private final ModelPart body;
	private final ModelPart glowing_eyes;
	private final ModelPart jaw;
	private final ModelPart right_fin;
	private final ModelPart left_fin;
	private final ModelPart fin_top;
	private final ModelPart tail;

	public Piranha(ModelPart root) {
		this.root = root.getChild("root");
		this.Piranha = this.root.getChild("Piranha");
		this.body = this.Piranha.getChild("body");
		this.glowing_eyes = this.body.getChild("glowing_eyes");
		this.jaw = this.body.getChild("jaw");
		this.right_fin = this.body.getChild("right_fin");
		this.left_fin = this.body.getChild("left_fin");
		this.fin_top = this.Piranha.getChild("fin_top");
		this.tail = this.Piranha.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Piranha = root.addOrReplaceChild("Piranha", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = Piranha.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.5F, -4.0F, 2.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition glowing_eyes = body.addOrReplaceChild("glowing_eyes", CubeListBuilder.create().texOffs(21, 17).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 1.5F, 0.0F));

		PartDefinition jaw = body.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(20, 10).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.005F))
		.texOffs(12, 14).addBox(-1.0F, -0.98F, -1.99F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 0.5F, -3.0F));

		PartDefinition right_fin = body.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(2, 17).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition left_fin = body.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(2, 14).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition fin_top = Piranha.addOrReplaceChild("fin_top", CubeListBuilder.create().texOffs(11, -6).addBox(0.0F, -6.0F, 0.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -3.0F));

		PartDefinition tail = Piranha.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, -4).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 3.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}