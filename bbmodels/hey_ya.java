// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class hey_ya<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "hey_ya"), "main");
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart jaw;
	private final ModelPart upper_head;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart legs;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public hey_ya(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.jaw = this.head2.getChild("jaw");
		this.upper_head = this.head2.getChild("upper_head");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.left_arm = this.torso.getChild("left_arm");
		this.right_arm = this.torso.getChild("right_arm");
		this.legs = this.body2.getChild("legs");
		this.left_leg = this.legs.getChild("left_leg");
		this.right_leg = this.legs.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -20.0F, 1.5F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition jaw = head2.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.6F, 3.5F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r1 = jaw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-3.5F, 0.9914F, -5.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.2439F, -1.7218F, 0.0873F, 0.0F, 0.0F));

		PartDefinition upper_head = head2.addOrReplaceChild("upper_head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.3772F, -7.2F, -6.925F, 7.0F, 7.0F, 7.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 22).addBox(0.1228F, -9.2F, -2.925F, 0.0F, 8.0F, 8.0F, new CubeDeformation(-0.01F))
		.texOffs(29, 1).addBox(-0.8772F, -2.0F, -7.9F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1228F, -0.775F, 3.325F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r2 = upper_head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 0).addBox(0.5F, -6.0F, -0.5F, 0.0F, 8.0F, 8.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -3.2F, -2.2286F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r3 = upper_head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 22).addBox(0.5F, -6.0F, -0.5F, 0.0F, 8.0F, 8.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-0.5676F, -3.2F, -2.772F, 0.0F, -0.5672F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -20.0F, 1.5F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(32, 29).addBox(-1.6617F, -9.0804F, -0.5F, 4.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 23).addBox(-3.1617F, -0.3052F, -1.5F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(42, 54).addBox(-3.1617F, -6.1052F, -1.5F, 7.0F, 6.0F, 4.0F, new CubeDeformation(-0.1F))
		.texOffs(32, 16).addBox(-3.6617F, -8.0804F, -1.5F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.3383F, 9.0804F, -0.5F));

		PartDefinition left_arm = torso.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 38).addBox(-0.0656F, -1.0F, -0.9128F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4F, -7.0F, 0.5F, 0.0F, 0.0873F, 0.0F));

		PartDefinition right_arm = torso.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-3.6F, -7.0F, 0.45F));

		PartDefinition cube_r4 = right_arm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 38).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0617F, 4.0F, 0.05F, 0.0F, 3.1416F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, -1.5F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 41).addBox(-1.0317F, -0.5804F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.3617F, -9.9196F, 1.5F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.7F, -10.0F, 1.5F));

		PartDefinition cube_r5 = right_leg.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 41).addBox(-1.0F, -1.025F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.03F, 0.525F, 0.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}