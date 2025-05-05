// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class D4C extends EntityModel<Entity> {
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart ears;
	private final ModelPart right_ear;
	private final ModelPart left_ear;
	private final ModelPart bone;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart upper_chest_only;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_left_arm;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;
	private final ModelPart lower_chest;
	private final ModelPart lower_torso;
	private final ModelPart legs;
	private final ModelPart left_leg;
	private final ModelPart upper_left_leg;
	private final ModelPart lower_left_leg;
	private final ModelPart right_leg;
	private final ModelPart upper_right_leg;
	private final ModelPart lower_right_leg;
	private final ModelPart BAM;
	private final ModelPart RightArmBAM;
	private final ModelPart RightArmBAM2;
	private final ModelPart RightArmBAM3;
	private final ModelPart LeftArmBAM;
	private final ModelPart LeftArmBAM4;
	private final ModelPart LeftArmBAM3;
	private final ModelPart LeftArmBAM2;
	private final ModelPart RightArmBAM4;
	private final ModelPart kick_barrage;
	private final ModelPart 1;
	private final ModelPart 2;
	private final ModelPart 3;
	private final ModelPart 4;
	private final ModelPart kick_barrage2;
	private final ModelPart 5;
	public D4C(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.ears = this.head2.getChild("ears");
		this.right_ear = this.ears.getChild("right_ear");
		this.left_ear = this.ears.getChild("left_ear");
		this.bone = this.head2.getChild("bone");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.upper_chest = this.torso.getChild("upper_chest");
		this.upper_chest_only = this.upper_chest.getChild("upper_chest_only");
		this.left_arm = this.upper_chest.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.lower_chest = this.torso.getChild("lower_chest");
		this.lower_torso = this.lower_chest.getChild("lower_torso");
		this.legs = this.body2.getChild("legs");
		this.left_leg = this.legs.getChild("left_leg");
		this.upper_left_leg = this.left_leg.getChild("upper_left_leg");
		this.lower_left_leg = this.left_leg.getChild("lower_left_leg");
		this.right_leg = this.legs.getChild("right_leg");
		this.upper_right_leg = this.right_leg.getChild("upper_right_leg");
		this.lower_right_leg = this.right_leg.getChild("lower_right_leg");
		this.BAM = this.stand2.getChild("BAM");
		this.RightArmBAM = this.BAM.getChild("RightArmBAM");
		this.RightArmBAM2 = this.BAM.getChild("RightArmBAM2");
		this.RightArmBAM3 = this.BAM.getChild("RightArmBAM3");
		this.LeftArmBAM = this.BAM.getChild("LeftArmBAM");
		this.LeftArmBAM4 = this.BAM.getChild("LeftArmBAM4");
		this.LeftArmBAM3 = this.BAM.getChild("LeftArmBAM3");
		this.LeftArmBAM2 = this.BAM.getChild("LeftArmBAM2");
		this.RightArmBAM4 = this.BAM.getChild("RightArmBAM4");
		this.kick_barrage = this.stand2.getChild("kick_barrage");
		this.1 = this.kick_barrage.getChild("1");
		this.2 = this.kick_barrage.getChild("2");
		this.3 = this.kick_barrage.getChild("3");
		this.4 = this.kick_barrage.getChild("4");
		this.kick_barrage2 = this.stand2.getChild("kick_barrage2");
		this.5 = this.kick_barrage2.getChild("5");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData stand = modelPartData.addChild("stand", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

		ModelPartData stand2 = stand.addChild("stand2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, 0.0F));

		ModelPartData head = stand2.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.15F, 0.0F));

		ModelPartData head2 = head.addChild("head2", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData ears = head2.addChild("ears", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right_ear = ears.addChild("right_ear", ModelPartBuilder.create().uv(64, 50).cuboid(-0.75F, -9.5F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F))
		.uv(32, 69).cuboid(-0.75F, -9.5F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.1F))
		.uv(48, 69).cuboid(-0.75F, -10.5F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.75F, -8.85F, 0.0F));

		ModelPartData left_ear = ears.addChild("left_ear", ModelPartBuilder.create().uv(64, 63).cuboid(-1.25F, -9.5F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.1F))
		.uv(54, 69).cuboid(-0.25F, -10.5F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(40, 69).cuboid(-1.25F, -9.5F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.75F, -8.85F, 0.0F));

		ModelPartData bone = head2.addChild("bone", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, -6.65F, -1.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.2F))
		.uv(32, 30).cuboid(-1.0F, -2.3F, -1.202F, 2.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(32, 30).cuboid(-1.0F, -1.0F, -1.202F, 2.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(32, 31).cuboid(-1.0F, 0.3F, -1.202F, 2.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.8F, -3.0F));

		ModelPartData body = stand2.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData body2 = body.addChild("body2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData torso = body2.addChild("torso", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData upper_chest = torso.addChild("upper_chest", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.0F, 0.0F));

		ModelPartData upper_chest_only = upper_chest.addChild("upper_chest_only", ModelPartBuilder.create().uv(0, 32).cuboid(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(32, 10).cuboid(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.201F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData left_arm = upper_chest.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.pivot(3.25F, -5.25F, 0.0F));

		ModelPartData upper_left_arm = left_arm.addChild("upper_left_arm", ModelPartBuilder.create().uv(0, 52).cuboid(0.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(56, 30).cuboid(0.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.2F))
		.uv(16, 52).cuboid(0.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_left_arm = left_arm.addChild("lower_left_arm", ModelPartBuilder.create().uv(24, 32).cuboid(-1.25F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(56, 10).cuboid(-1.25F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.1F))
		.uv(1, 76).cuboid(-1.25F, -0.25F, 2.25F, 4.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 5.5F, 0.0F));

		ModelPartData right_arm = upper_chest.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-3.25F, -5.25F, 0.0F));

		ModelPartData upper_right_arm = right_arm.addChild("upper_right_arm", ModelPartBuilder.create().uv(48, 50).cuboid(-4.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.1F))
		.uv(56, 0).cuboid(-4.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.2F))
		.uv(56, 40).cuboid(-4.75F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_right_arm = right_arm.addChild("lower_right_arm", ModelPartBuilder.create().uv(32, 50).cuboid(-2.75F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.1F))
		.uv(56, 20).cuboid(-2.75F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 0).mirrored().cuboid(-2.75F, -0.25F, 2.25F, 4.0F, 2.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.0F, 5.5F, 0.0F));

		ModelPartData lower_chest = torso.addChild("lower_chest", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.0F, 0.0F));

		ModelPartData lower_torso = lower_chest.addChild("lower_torso", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(32, 20).cuboid(-4.0F, -2.0F, -2.0F, 8.0F, 2.0F, 4.0F, new Dilation(0.2001F)), ModelTransform.pivot(0.0F, 6.0F, 0.0F));

		ModelPartData legs = body2.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData left_leg = legs.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, -1.0F, 0.0F));

		ModelPartData upper_left_leg = left_leg.addChild("upper_left_leg", ModelPartBuilder.create().uv(32, 60).cuboid(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new Dilation(0.199F))
		.uv(40, 30).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(32, 42).cuboid(-2.0F, 5.5F, -2.3F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_left_leg = left_leg.addChild("lower_left_leg", ModelPartBuilder.create().uv(40, 40).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(48, 60).cuboid(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new Dilation(0.197F)), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

		ModelPartData right_leg = legs.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, -1.0F, 0.0F));

		ModelPartData upper_right_leg = right_leg.addChild("upper_right_leg", ModelPartBuilder.create().uv(0, 62).cuboid(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new Dilation(0.198F))
		.uv(0, 42).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(32, 45).cuboid(-2.0F, 5.5F, -2.3F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_right_leg = right_leg.addChild("lower_right_leg", ModelPartBuilder.create().uv(16, 42).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(16, 62).cuboid(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new Dilation(0.196F)), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

		ModelPartData BAM = stand2.addChild("BAM", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -17.0F, -4.0F));

		ModelPartData RightArmBAM = BAM.addChild("RightArmBAM", ModelPartBuilder.create(), ModelTransform.pivot(-12.0F, -8.0F, 1.0F));

		ModelPartData cube_r1 = RightArmBAM.addChild("cube_r1", ModelPartBuilder.create().uv(0, 121).cuboid(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(16, 118).mirrored().cuboid(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		ModelPartData RightArm_r1 = RightArmBAM.addChild("RightArm_r1", ModelPartBuilder.create().uv(0, 112).cuboid(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.3F)), ModelTransform.of(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

		ModelPartData RightArmBAM2 = BAM.addChild("RightArmBAM2", ModelPartBuilder.create(), ModelTransform.pivot(-14.5F, -2.75F, 0.0F));

		ModelPartData cube_r2 = RightArmBAM2.addChild("cube_r2", ModelPartBuilder.create().uv(0, 121).cuboid(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(16, 118).mirrored().cuboid(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		ModelPartData RightArm_r2 = RightArmBAM2.addChild("RightArm_r2", ModelPartBuilder.create().uv(0, 112).cuboid(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.3F)), ModelTransform.of(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		ModelPartData RightArmBAM3 = BAM.addChild("RightArmBAM3", ModelPartBuilder.create(), ModelTransform.pivot(-13.625F, 0.75F, 0.0F));

		ModelPartData cube_r3 = RightArmBAM3.addChild("cube_r3", ModelPartBuilder.create().uv(0, 121).cuboid(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(16, 118).mirrored().cuboid(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		ModelPartData RightArm_r3 = RightArmBAM3.addChild("RightArm_r3", ModelPartBuilder.create().uv(0, 112).cuboid(0.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.3F)), ModelTransform.of(-0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		ModelPartData LeftArmBAM = BAM.addChild("LeftArmBAM", ModelPartBuilder.create(), ModelTransform.pivot(12.0F, -9.0F, 0.0F));

		ModelPartData cube_r4 = LeftArmBAM.addChild("cube_r4", ModelPartBuilder.create().uv(0, 121).mirrored().cuboid(-3.25F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
		.uv(16, 118).cuboid(-3.25F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		ModelPartData LeftArm_r1 = LeftArmBAM.addChild("LeftArm_r1", ModelPartBuilder.create().uv(0, 112).mirrored().cuboid(-3.25F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.3F)).mirrored(false), ModelTransform.of(-0.75F, -1.75F, -1.0F, -1.5708F, 0.0F, -1.5708F));

		ModelPartData LeftArmBAM4 = BAM.addChild("LeftArmBAM4", ModelPartBuilder.create(), ModelTransform.pivot(14.5F, -2.75F, 0.0F));

		ModelPartData cube_r5 = LeftArmBAM4.addChild("cube_r5", ModelPartBuilder.create().uv(0, 121).mirrored().cuboid(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
		.uv(16, 118).cuboid(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		ModelPartData LeftArm_r2 = LeftArmBAM4.addChild("LeftArm_r2", ModelPartBuilder.create().uv(0, 112).mirrored().cuboid(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.3F)).mirrored(false), ModelTransform.of(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

		ModelPartData LeftArmBAM3 = BAM.addChild("LeftArmBAM3", ModelPartBuilder.create(), ModelTransform.pivot(13.625F, 0.75F, 0.0F));

		ModelPartData cube_r6 = LeftArmBAM3.addChild("cube_r6", ModelPartBuilder.create().uv(0, 121).mirrored().cuboid(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
		.uv(16, 118).cuboid(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		ModelPartData LeftArm_r3 = LeftArmBAM3.addChild("LeftArm_r3", ModelPartBuilder.create().uv(0, 112).mirrored().cuboid(-4.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.3F)).mirrored(false), ModelTransform.of(0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, -1.5708F));

		ModelPartData LeftArmBAM2 = BAM.addChild("LeftArmBAM2", ModelPartBuilder.create(), ModelTransform.pivot(12.0F, -8.0F, 1.0F));

		ModelPartData cube_r7 = LeftArmBAM2.addChild("cube_r7", ModelPartBuilder.create().uv(0, 121).mirrored().cuboid(6.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
		.uv(16, 118).cuboid(6.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, -1.5708F));

		ModelPartData LeftArm_r4 = LeftArmBAM2.addChild("LeftArm_r4", ModelPartBuilder.create().uv(0, 112).mirrored().cuboid(6.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.3F)).mirrored(false), ModelTransform.of(2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, -1.5708F));

		ModelPartData RightArmBAM4 = BAM.addChild("RightArmBAM4", ModelPartBuilder.create(), ModelTransform.pivot(-12.0F, -9.0F, 0.0F));

		ModelPartData cube_r8 = RightArmBAM4.addChild("cube_r8", ModelPartBuilder.create().uv(0, 121).cuboid(-0.75F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(16, 118).mirrored().cuboid(-0.75F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		ModelPartData RightArm_r4 = RightArmBAM4.addChild("RightArm_r4", ModelPartBuilder.create().uv(0, 112).cuboid(-0.75F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.3F)), ModelTransform.of(0.75F, -1.75F, -1.0F, -1.5708F, 0.0F, 1.5708F));

		ModelPartData kick_barrage = stand2.addChild("kick_barrage", ModelPartBuilder.create(), ModelTransform.pivot(-11.0F, -12.0F, -2.0F));

		ModelPartData 1 = kick_barrage.addChild("1", ModelPartBuilder.create().uv(45, 118).mirrored().cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F)).mirrored(false)
		.uv(61, 119).mirrored().cuboid(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F)).mirrored(false), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

		ModelPartData upper_left_leg_r1 = 1.addChild("upper_left_leg_r1", ModelPartBuilder.create().uv(66, 116).cuboid(9.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

		ModelPartData base_r1 = 1.addChild("base_r1", ModelPartBuilder.create().uv(45, 110).mirrored().cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		ModelPartData 2 = kick_barrage.addChild("2", ModelPartBuilder.create().uv(45, 118).mirrored().cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F)).mirrored(false)
		.uv(61, 119).mirrored().cuboid(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F)).mirrored(false), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

		ModelPartData upper_left_leg_r2 = 2.addChild("upper_left_leg_r2", ModelPartBuilder.create().uv(66, 116).cuboid(9.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

		ModelPartData base_r2 = 2.addChild("base_r2", ModelPartBuilder.create().uv(45, 110).mirrored().cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		ModelPartData 3 = kick_barrage.addChild("3", ModelPartBuilder.create().uv(45, 118).mirrored().cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F)).mirrored(false)
		.uv(61, 119).mirrored().cuboid(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F)).mirrored(false), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

		ModelPartData upper_left_leg_r3 = 3.addChild("upper_left_leg_r3", ModelPartBuilder.create().uv(66, 116).cuboid(9.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

		ModelPartData base_r3 = 3.addChild("base_r3", ModelPartBuilder.create().uv(45, 110).mirrored().cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		ModelPartData 4 = kick_barrage.addChild("4", ModelPartBuilder.create().uv(45, 118).mirrored().cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F)).mirrored(false)
		.uv(61, 119).mirrored().cuboid(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F)).mirrored(false), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

		ModelPartData upper_left_leg_r4 = 4.addChild("upper_left_leg_r4", ModelPartBuilder.create().uv(66, 116).cuboid(9.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

		ModelPartData base_r4 = 4.addChild("base_r4", ModelPartBuilder.create().uv(45, 110).mirrored().cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		ModelPartData kick_barrage2 = stand2.addChild("kick_barrage2", ModelPartBuilder.create(), ModelTransform.pivot(11.0F, -12.0F, -2.0F));

		ModelPartData 5 = kick_barrage2.addChild("5", ModelPartBuilder.create().uv(45, 118).cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(61, 119).cuboid(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F))
		.uv(45, 118).cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(61, 119).cuboid(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F))
		.uv(45, 118).cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(61, 119).cuboid(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F))
		.uv(45, 118).cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(61, 119).cuboid(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

		ModelPartData upper_right_leg_r1 = 5.addChild("upper_right_leg_r1", ModelPartBuilder.create().uv(66, 116).mirrored().cuboid(-12.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
		.uv(66, 116).mirrored().cuboid(-12.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
		.uv(66, 116).mirrored().cuboid(-12.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
		.uv(66, 116).mirrored().cuboid(-12.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(11.0F, 4.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

		ModelPartData base_r5 = 5.addChild("base_r5", ModelPartBuilder.create().uv(45, 110).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(45, 110).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(45, 110).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(45, 110).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		stand.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}