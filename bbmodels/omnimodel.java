// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class omnimodel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "omnimodel"), "main");
	private final ModelPart playerlike;
	private final ModelPart full_body;
	private final ModelPart head_part;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart body_part;
	private final ModelPart upper_body;
	private final ModelPart right_arms;
	private final ModelPart right_arm;
	private final ModelPart right_arm_one;
	private final ModelPart right_arm_two;
	private final ModelPart right_sleeve;
	private final ModelPart right_sleeve_one;
	private final ModelPart right_sleeve_two;
	private final ModelPart left_arms;
	private final ModelPart left_arm;
	private final ModelPart left_arm_one;
	private final ModelPart left_arm_two;
	private final ModelPart left_sleeve;
	private final ModelPart left_sleeve_one;
	private final ModelPart left_sleeve_two;
	private final ModelPart body;
	private final ModelPart chest;
	private final ModelPart chest_two;
	private final ModelPart jacket;
	private final ModelPart chest_three;
	private final ModelPart cloak;
	private final ModelPart legs;
	private final ModelPart right_legs;
	private final ModelPart right_pants;
	private final ModelPart right_leg;
	private final ModelPart left_legs;
	private final ModelPart left_leg;
	private final ModelPart left_pants;

	public omnimodel(ModelPart root) {
		this.playerlike = root.getChild("playerlike");
		this.full_body = this.playerlike.getChild("full_body");
		this.head_part = this.full_body.getChild("head_part");
		this.head = this.head_part.getChild("head");
		this.hat = this.head_part.getChild("hat");
		this.body_part = this.full_body.getChild("body_part");
		this.upper_body = this.body_part.getChild("upper_body");
		this.right_arms = this.upper_body.getChild("right_arms");
		this.right_arm = this.right_arms.getChild("right_arm");
		this.right_arm_one = this.right_arm.getChild("right_arm_one");
		this.right_arm_two = this.right_arm.getChild("right_arm_two");
		this.right_sleeve = this.right_arms.getChild("right_sleeve");
		this.right_sleeve_one = this.right_sleeve.getChild("right_sleeve_one");
		this.right_sleeve_two = this.right_sleeve.getChild("right_sleeve_two");
		this.left_arms = this.upper_body.getChild("left_arms");
		this.left_arm = this.left_arms.getChild("left_arm");
		this.left_arm_one = this.left_arm.getChild("left_arm_one");
		this.left_arm_two = this.left_arm.getChild("left_arm_two");
		this.left_sleeve = this.left_arms.getChild("left_sleeve");
		this.left_sleeve_one = this.left_sleeve.getChild("left_sleeve_one");
		this.left_sleeve_two = this.left_sleeve.getChild("left_sleeve_two");
		this.body = this.upper_body.getChild("body");
		this.chest = this.body.getChild("chest");
		this.chest_two = this.body.getChild("chest_two");
		this.jacket = this.upper_body.getChild("jacket");
		this.chest_three = this.jacket.getChild("chest_three");
		this.cloak = this.upper_body.getChild("cloak");
		this.legs = this.body_part.getChild("legs");
		this.right_legs = this.legs.getChild("right_legs");
		this.right_pants = this.right_legs.getChild("right_pants");
		this.right_leg = this.right_legs.getChild("right_leg");
		this.left_legs = this.legs.getChild("left_legs");
		this.left_leg = this.left_legs.getChild("left_leg");
		this.left_pants = this.left_legs.getChild("left_pants");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition playerlike = partdefinition.addOrReplaceChild("playerlike", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition full_body = playerlike.addOrReplaceChild("full_body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head_part = full_body.addOrReplaceChild("head_part", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition head = head_part.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head_part.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_part = full_body.addOrReplaceChild("body_part", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_body = body_part.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition right_arms = upper_body.addOrReplaceChild("right_arms", CubeListBuilder.create(), PartPose.offset(-3.5F, -10.0F, 0.0F));

		PartDefinition right_arm = right_arms.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-1.0F, 2.0F, 0.0F));

		PartDefinition right_arm_one = right_arm.addOrReplaceChild("right_arm_one", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm_two = right_arm.addOrReplaceChild("right_arm_two", CubeListBuilder.create().texOffs(40, 16).addBox(-2.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_sleeve = right_arms.addOrReplaceChild("right_sleeve", CubeListBuilder.create(), PartPose.offset(-1.0F, 2.0F, 0.0F));

		PartDefinition right_sleeve_one = right_sleeve.addOrReplaceChild("right_sleeve_one", CubeListBuilder.create().texOffs(40, 32).addBox(-3.5F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_sleeve_two = right_sleeve.addOrReplaceChild("right_sleeve_two", CubeListBuilder.create().texOffs(40, 32).addBox(-2.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arms = upper_body.addOrReplaceChild("left_arms", CubeListBuilder.create(), PartPose.offset(3.5F, -10.0F, 0.0F));

		PartDefinition left_arm = left_arms.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0F, 0.0F));

		PartDefinition left_arm_one = left_arm.addOrReplaceChild("left_arm_one", CubeListBuilder.create().texOffs(32, 48).addBox(-0.5F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm_two = left_arm.addOrReplaceChild("left_arm_two", CubeListBuilder.create().texOffs(32, 48).addBox(-0.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_sleeve = left_arms.addOrReplaceChild("left_sleeve", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0F, 0.0F));

		PartDefinition left_sleeve_one = left_sleeve.addOrReplaceChild("left_sleeve_one", CubeListBuilder.create().texOffs(48, 48).addBox(-0.5F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_sleeve_two = left_sleeve.addOrReplaceChild("left_sleeve_two", CubeListBuilder.create().texOffs(46, 48).addBox(-0.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = upper_body.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(-5.0F, -0.4F, 0.0F));

		PartDefinition chest_r1 = chest.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(24, 20).addBox(-4.0F, 0.0F, 0.005F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 4.65F, -3.775F, 1.5502F, 0.4577F, 1.5222F));

		PartDefinition chest_r2 = chest.addOrReplaceChild("chest_r2", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, 0.005F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 4.65F, -3.775F, 1.5735F, 0.4581F, 1.5757F));

		PartDefinition chest_r3 = chest.addOrReplaceChild("chest_r3", CubeListBuilder.create().texOffs(20, 24).addBox(-4.0F, 0.0F, 0.005F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 4.65F, -3.775F, 1.117F, 0.0F, 0.0F));

		PartDefinition chest_r4 = chest.addOrReplaceChild("chest_r4", CubeListBuilder.create().texOffs(20, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 1.05F, -2.0F, -0.4581F, 0.0F, 0.0F));

		PartDefinition chest_two = body.addOrReplaceChild("chest_two", CubeListBuilder.create().texOffs(19, 20).addBox(-4.0F, 1.0F, -2.725F, 8.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, -0.175F, 0.425F));

		PartDefinition jacket = upper_body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.255F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition chest_three = jacket.addOrReplaceChild("chest_three", CubeListBuilder.create().texOffs(19, 36).addBox(-4.0F, 1.0F, -2.725F, 8.0F, 4.0F, 1.0F, new CubeDeformation(-0.08F)), PartPose.offset(0.0F, -0.175F, 0.425F));

		PartDefinition cloak = upper_body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(78, 15).addBox(-5.0F, -11.0F, 2.5F, 10.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition legs = body_part.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-5.0F, -24.0F, 0.0F));

		PartDefinition right_legs = legs.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(2.9F, 12.0F, 0.0F));

		PartDefinition right_pants = right_legs.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.1F, 0.0F, 0.0F));

		PartDefinition right_leg = right_legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 0.0F, 0.0F));

		PartDefinition left_legs = legs.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(7.0F, 12.0F, 0.0F));

		PartDefinition left_leg = left_legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_pants = left_legs.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.249F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		playerlike.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}