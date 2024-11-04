// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package com.example.mod;
   
public class unknown extends EntityModel<Entity> {
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart extra_details;
	private final ModelPart crown;
	private final ModelPart jaw;
	private final ModelPart right_hand_1;
	private final ModelPart right_finger;
	private final ModelPart right_fingertip2;
	private final ModelPart left_finger;
	private final ModelPart left_fingertip;
	private final ModelPart middle_finger;
	private final ModelPart middle_fingertip;
	private final ModelPart thumb;
	private final ModelPart right_hand_2;
	private final ModelPart right_finger2;
	private final ModelPart right_fingertip3;
	private final ModelPart left_finger2;
	private final ModelPart left_fingertip2;
	private final ModelPart middle_finger2;
	private final ModelPart middle_fingertip2;
	private final ModelPart thumb2;
	public unknown(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = root.getChild("stand2");
		this.head = root.getChild("head");
		this.head2 = root.getChild("head2");
		this.extra_details = root.getChild("extra_details");
		this.crown = root.getChild("crown");
		this.jaw = root.getChild("jaw");
		this.right_hand_1 = root.getChild("right_hand_1");
		this.right_finger = root.getChild("right_finger");
		this.right_fingertip2 = root.getChild("right_fingertip2");
		this.left_finger = root.getChild("left_finger");
		this.left_fingertip = root.getChild("left_fingertip");
		this.middle_finger = root.getChild("middle_finger");
		this.middle_fingertip = root.getChild("middle_fingertip");
		this.thumb = root.getChild("thumb");
		this.right_hand_2 = root.getChild("right_hand_2");
		this.right_finger2 = root.getChild("right_finger2");
		this.right_fingertip3 = root.getChild("right_fingertip3");
		this.left_finger2 = root.getChild("left_finger2");
		this.left_fingertip2 = root.getChild("left_fingertip2");
		this.middle_finger2 = root.getChild("middle_finger2");
		this.middle_fingertip2 = root.getChild("middle_fingertip2");
		this.thumb2 = root.getChild("thumb2");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData stand = modelPartData.addChild("stand", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

		ModelPartData stand2 = stand.addChild("stand2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, 0.0F));

		ModelPartData head = stand2.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.15F, 0.0F));

		ModelPartData head2 = head.addChild("head2", ModelPartBuilder.create().uv(24, 8).cuboid(-4.0F, -7.85F, -4.0F, 8.0F, 6.0F, 8.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = head2.addChild("cube_r1", ModelPartBuilder.create().uv(37, 43).cuboid(-6.0F, -7.0F, 0.0F, 12.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.5F, 3.375F, -0.2182F, 0.0F, 0.0F));

		ModelPartData extra_details = head2.addChild("extra_details", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.2114F, -5.0302F, 8.0F, 8.0F, 8.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -8.8761F, 1.0302F));

		ModelPartData crown = extra_details.addChild("crown", ModelPartBuilder.create().uv(56, 8).mirrored().cuboid(2.625F, -2.9739F, -2.0302F, 2.0F, 5.0F, 2.0F, new Dilation(-0.2F)).mirrored(false)
		.uv(1, 1).cuboid(-0.5F, -6.0739F, -1.5302F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(28, 4).cuboid(-0.5F, -8.0739F, -1.5302F, 1.0F, 3.0F, 1.0F, new Dilation(-0.2F))
		.uv(0, 16).cuboid(-4.0F, -3.9739F, -5.0302F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(55, 2).cuboid(-1.0F, -2.9739F, -5.5302F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(47, 2).cuboid(-1.0F, 0.0261F, -5.5302F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData crown_r1 = crown.addChild("crown_r1", ModelPartBuilder.create().uv(47, 2).cuboid(0.025F, -25.975F, -1.2F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(55, 2).cuboid(0.025F, -28.975F, -1.2F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.975F, 26.0011F, 2.3698F, 0.0F, 3.1416F, 0.0F));

		ModelPartData crown_r2 = crown.addChild("crown_r2", ModelPartBuilder.create().uv(56, 8).mirrored().cuboid(-1.0F, -26.6F, 0.5F, 2.0F, 5.0F, 2.0F, new Dilation(-0.2F)).mirrored(false), ModelTransform.of(-3.375F, 23.6261F, 0.4698F, 0.0F, 3.1416F, 0.0F));

		ModelPartData jaw = head2.addChild("jaw", ModelPartBuilder.create(), ModelTransform.of(0.0F, -2.075F, 3.725F, 0.3491F, 0.0F, 0.0F));

		ModelPartData jaw_r1 = jaw.addChild("jaw_r1", ModelPartBuilder.create().uv(24, 24).cuboid(-4.0F, -1.0F, -8.0F, 8.0F, 2.0F, 8.0F, new Dilation(-0.251F)), ModelTransform.of(0.0F, 0.7466F, 0.2578F, -0.1745F, 0.0F, 0.0F));

		ModelPartData right_hand_1 = modelPartData.addChild("right_hand_1", ModelPartBuilder.create().uv(40, 53).cuboid(-2.0F, -7.0F, -4.0F, 4.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(9.4603F, 3.119F, -5.7865F, -1.747F, -0.4488F, -1.1814F));

		ModelPartData right_finger = right_hand_1.addChild("right_finger", ModelPartBuilder.create().uv(6, 40).cuboid(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.18F)), ModelTransform.of(-0.1909F, -4.6349F, -1.8531F, 0.0F, 0.0F, 0.1309F));

		ModelPartData right_fingertip2 = right_finger.addChild("right_fingertip2", ModelPartBuilder.create().uv(14, 40).cuboid(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.15F))
		.uv(17, 50).cuboid(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 53).cuboid(0.0408F, 7.406F, -0.5354F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.325F, 6.2F, 0.0F, -0.1309F, 0.0F, 0.3054F));

		ModelPartData left_finger = right_hand_1.addChild("left_finger", ModelPartBuilder.create().uv(10, 46).cuboid(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.18F)), ModelTransform.of(-0.1909F, -4.6349F, 2.0719F, 0.0F, 0.0F, 0.1309F));

		ModelPartData left_fingertip = left_finger.addChild("left_fingertip", ModelPartBuilder.create().uv(0, 54).cuboid(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.15F))
		.uv(17, 58).cuboid(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 61).cuboid(0.0408F, 7.406F, -0.5454F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.325F, 6.2F, 0.0F, 0.1309F, 0.0F, 0.3054F));

		ModelPartData middle_finger = right_hand_1.addChild("middle_finger", ModelPartBuilder.create().uv(4, 48).cuboid(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.18F)), ModelTransform.pivot(-0.1909F, -4.6349F, 0.1219F));

		ModelPartData middle_fingertip = middle_finger.addChild("middle_fingertip", ModelPartBuilder.create().uv(0, 40).cuboid(-1.1467F, 0.1531F, -1.05F, 1.0F, 7.0F, 2.0F, new Dilation(0.15F))
		.uv(17, 54).cuboid(0.0159F, 6.3599F, -1.0469F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 57).cuboid(0.0158F, 8.356F, -0.5604F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.35F, 6.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

		ModelPartData thumb = right_hand_1.addChild("thumb", ModelPartBuilder.create().uv(8, 54).cuboid(0.0747F, 0.1958F, -0.9206F, 1.0F, 5.0F, 2.0F, new Dilation(0.2F))
		.uv(17, 46).cuboid(1.3F, 3.7F, -0.925F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 49).cuboid(1.3F, 5.701F, -0.3965F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.275F, -4.3F, 1.9F, 0.3578F, 0.0F, 0.5672F));

		ModelPartData right_hand_2 = modelPartData.addChild("right_hand_2", ModelPartBuilder.create().uv(40, 53).cuboid(-2.0F, -7.0F, -4.0F, 4.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-9.5397F, 3.119F, -5.7865F, -1.3742F, -0.4407F, -2.0077F));

		ModelPartData right_finger2 = right_hand_2.addChild("right_finger2", ModelPartBuilder.create().uv(6, 40).cuboid(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.18F)), ModelTransform.of(-0.1909F, -4.6349F, -2.2781F, 0.0F, 0.0F, 0.1309F));

		ModelPartData right_fingertip3 = right_finger2.addChild("right_fingertip3", ModelPartBuilder.create().uv(14, 40).cuboid(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.15F))
		.uv(17, 50).cuboid(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 53).cuboid(0.0408F, 7.406F, -0.5354F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.325F, 6.2F, 0.0F, -0.1309F, 0.0F, 0.3054F));

		ModelPartData left_finger2 = right_hand_2.addChild("left_finger2", ModelPartBuilder.create().uv(10, 46).cuboid(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.18F)), ModelTransform.of(-0.1909F, -4.6349F, 2.3469F, 0.0F, 0.0F, 0.1309F));

		ModelPartData left_fingertip2 = left_finger2.addChild("left_fingertip2", ModelPartBuilder.create().uv(0, 54).cuboid(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.15F))
		.uv(17, 58).cuboid(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 61).cuboid(0.0408F, 7.406F, -0.5454F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.325F, 6.2F, 0.0F, 0.1309F, 0.0F, 0.3054F));

		ModelPartData middle_finger2 = right_hand_2.addChild("middle_finger2", ModelPartBuilder.create().uv(4, 48).cuboid(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new Dilation(0.18F)), ModelTransform.pivot(-0.1909F, -4.6349F, 0.0469F));

		ModelPartData middle_fingertip2 = middle_finger2.addChild("middle_fingertip2", ModelPartBuilder.create().uv(0, 40).cuboid(-1.1467F, 0.1531F, -1.05F, 1.0F, 7.0F, 2.0F, new Dilation(0.15F))
		.uv(17, 54).cuboid(0.0159F, 6.3599F, -1.0469F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 57).cuboid(0.0158F, 8.356F, -0.5604F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.35F, 6.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

		ModelPartData thumb2 = right_hand_2.addChild("thumb2", ModelPartBuilder.create().uv(8, 54).cuboid(0.0747F, 0.1958F, -0.9206F, 1.0F, 5.0F, 2.0F, new Dilation(0.2F))
		.uv(17, 46).cuboid(1.3F, 3.7F, -0.925F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 49).cuboid(1.3F, 5.701F, -0.3965F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.175F, -4.3F, 2.175F, 0.3491F, 0.0F, 0.5672F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		stand.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_hand_1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_hand_2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}