// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class divadownimproved2 - Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "divadownimproved2_- converted"), "main");
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart top;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;
	private final ModelPart left_Arm;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_left_arm;
	private final ModelPart no_arms;
	private final ModelPart diving_gear;
	private final ModelPart lower_chest;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart upper_right_leg;
	private final ModelPart lower_right_leg;
	private final ModelPart left_leg;
	private final ModelPart upper_left_leg;
	private final ModelPart lower_left_leg;
	private final ModelPart BAM;
	private final ModelPart LeftArmBAM1;
	private final ModelPart LeftArmBAM2;
	private final ModelPart LeftArmBAM3;
	private final ModelPart RightArmBAM1;
	private final ModelPart RightArmBAM2;
	private final ModelPart RightArmBAM3;
	private final ModelPart kick_barrage;
	private final ModelPart One;
	private final ModelPart Two;
	private final ModelPart Three;
	private final ModelPart Four;

	public divadownimproved2 - Converted(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.top = this.head2.getChild("top");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.upper_chest = this.torso.getChild("upper_chest");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.left_Arm = this.upper_chest.getChild("left_Arm");
		this.upper_left_arm = this.left_Arm.getChild("upper_left_arm");
		this.lower_left_arm = this.left_Arm.getChild("lower_left_arm");
		this.no_arms = this.upper_chest.getChild("no_arms");
		this.diving_gear = this.no_arms.getChild("diving_gear");
		this.lower_chest = this.torso.getChild("lower_chest");
		this.legs = this.body2.getChild("legs");
		this.right_leg = this.legs.getChild("right_leg");
		this.upper_right_leg = this.right_leg.getChild("upper_right_leg");
		this.lower_right_leg = this.right_leg.getChild("lower_right_leg");
		this.left_leg = this.legs.getChild("left_leg");
		this.upper_left_leg = this.left_leg.getChild("upper_left_leg");
		this.lower_left_leg = this.left_leg.getChild("lower_left_leg");
		this.BAM = root.getChild("BAM");
		this.LeftArmBAM1 = this.BAM.getChild("LeftArmBAM1");
		this.LeftArmBAM2 = this.BAM.getChild("LeftArmBAM2");
		this.LeftArmBAM3 = this.BAM.getChild("LeftArmBAM3");
		this.RightArmBAM1 = this.BAM.getChild("RightArmBAM1");
		this.RightArmBAM2 = this.BAM.getChild("RightArmBAM2");
		this.RightArmBAM3 = this.BAM.getChild("RightArmBAM3");
		this.kick_barrage = root.getChild("kick_barrage");
		this.One = this.kick_barrage.getChild("One");
		this.Two = this.kick_barrage.getChild("Two");
		this.Three = this.kick_barrage.getChild("Three");
		this.Four = this.kick_barrage.getChild("Four");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(34, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition top = head2.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(2, 1).addBox(0.0F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -12.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, -22.0F, 19.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(70, 0).addBox(-7.5F, -24.0F, -21.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(91, 0).addBox(-7.5F, -24.0F, -21.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 22.0F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(70, 10).addBox(-7.5F, -17.5F, -21.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(91, 6).addBox(-7.5F, -18.0F, -21.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 22.0F, 0.0F));

		PartDefinition left_Arm = upper_chest.addOrReplaceChild("left_Arm", CubeListBuilder.create(), PartPose.offset(-3.0F, -22.0F, 16.0F));

		PartDefinition upper_left_arm = left_Arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(91, 0).addBox(-0.5F, -24.0F, -18.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 22.0F, 0.0F));

		PartDefinition upper_left_arm_r1 = upper_left_arm.addOrReplaceChild("upper_left_arm_r1", CubeListBuilder.create().texOffs(70, 27).addBox(-2.5F, -2.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(1.0F, -22.0F, -15.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition lower_left_arm = left_Arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(91, 6).addBox(-2.5F, -1.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, 5.0F, -17.0F));

		PartDefinition lower_left_arm_r1 = lower_left_arm.addOrReplaceChild("lower_left_arm_r1", CubeListBuilder.create().texOffs(70, 37).addBox(-1.5F, -1.5F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition no_arms = upper_chest.addOrReplaceChild("no_arms", CubeListBuilder.create().texOffs(0, 21).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition diving_gear = no_arms.addOrReplaceChild("diving_gear", CubeListBuilder.create().texOffs(153, 0).addBox(-1.5F, -24.0F, -5.0F, 3.0F, 1.5F, 1.5F, new CubeDeformation(0.0F))
		.texOffs(152, 4).addBox(1.5F, -24.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(166, 0).addBox(4.5F, -25.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(163, 0).addBox(-5.5F, -24.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(166, 0).addBox(-5.5F, -25.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(197, 0).addBox(0.0F, -24.0F, 3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(197, 0).addBox(-4.0F, -24.0F, 3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(157, 14).addBox(-4.5F, -25.0F, 4.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create().texOffs(30, 24).addBox(-4.0F, -18.0F, -2.5F, 8.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, -12.0F, 17.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(91, 0).addBox(-3.9F, -12.0F, -19.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(111, 16).addBox(-3.9F, -12.5F, -19.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(91, 7).addBox(-3.9F, -5.0F, -19.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(111, 27).addBox(-3.9F, -5.0F, -19.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, -12.0F, 9.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(91, 0).addBox(-0.1F, -12.0F, -11.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(131, 0).addBox(-0.1F, -12.5F, -11.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(91, 7).addBox(-0.1F, -5.0F, -11.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(131, 11).addBox(-0.1F, -5.0F, -11.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition BAM = partdefinition.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LeftArmBAM1 = BAM.addOrReplaceChild("LeftArmBAM1", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, -6.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(-4, 47).addBox(-1.0F, -5.0F, -3.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 50).addBox(-0.9F, -7.0F, -2.9F, 3.8F, 2.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, -28.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmBAM2 = BAM.addOrReplaceChild("LeftArmBAM2", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, -6.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(-4, 47).addBox(-1.0F, -5.0F, -3.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 50).addBox(-0.9F, -7.0F, -2.9F, 3.8F, 2.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, -20.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, -6.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(-4, 47).addBox(-1.0F, -5.0F, -3.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 50).addBox(-0.9F, -7.0F, -2.9F, 3.8F, 2.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -11.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightArmBAM1 = BAM.addOrReplaceChild("RightArmBAM1", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, -6.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(-4, 47).addBox(-1.0F, -5.0F, -3.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 50).addBox(-0.9F, -7.0F, -2.9F, 3.8F, 2.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -28.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, -6.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(-4, 47).addBox(-1.0F, -5.0F, -3.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 50).addBox(-0.9F, -7.0F, -2.9F, 3.8F, 2.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.0F, -20.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, -6.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(-4, 47).addBox(-1.0F, -5.0F, -3.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(14, 50).addBox(-0.9F, -7.0F, -2.9F, 3.8F, 2.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.0F, -11.0F, -5.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition kick_barrage = partdefinition.addOrReplaceChild("kick_barrage", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition One = kick_barrage.addOrReplaceChild("One", CubeListBuilder.create().texOffs(27, 54).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -5.0F, 0.0F));

		PartDefinition upperleg_r1 = One.addOrReplaceChild("upperleg_r1", CubeListBuilder.create().texOffs(45, 56).addBox(-1.9F, -3.7F, -1.9F, 3.8F, 4.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Two = kick_barrage.addOrReplaceChild("Two", CubeListBuilder.create().texOffs(27, 54).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -5.0F, 0.0F));

		PartDefinition upperleg_r2 = Two.addOrReplaceChild("upperleg_r2", CubeListBuilder.create().texOffs(45, 56).addBox(-1.9F, -3.7F, -1.9F, 3.8F, 4.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Three = kick_barrage.addOrReplaceChild("Three", CubeListBuilder.create().texOffs(27, 54).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -5.0F, 0.0F));

		PartDefinition upperleg_r3 = Three.addOrReplaceChild("upperleg_r3", CubeListBuilder.create().texOffs(45, 56).addBox(-1.9F, -3.7F, -1.9F, 3.8F, 4.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Four = kick_barrage.addOrReplaceChild("Four", CubeListBuilder.create().texOffs(27, 54).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -5.0F, 0.0F));

		PartDefinition upperleg_r4 = Four.addOrReplaceChild("upperleg_r4", CubeListBuilder.create().texOffs(45, 56).addBox(-1.9F, -3.7F, -1.9F, 3.8F, 4.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		BAM.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		kick_barrage.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}