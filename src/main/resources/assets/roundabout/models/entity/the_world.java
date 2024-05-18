// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package com.example.mod;
   
public class unknown extends EntityModel<Entity> {
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart extra_details;
	private final ModelPart top;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart upper_straps;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_left_arm;
	private final ModelPart tanks;
	private final ModelPart lower_chest;
	private final ModelPart lower_torso;
	private final ModelPart lower_straps;
	private final ModelPart belt;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart upper_right_leg;
	private final ModelPart lower_right_leg;
	private final ModelPart left_leg;
	private final ModelPart upper_left_leg;
	private final ModelPart lower_left_leg;
	public unknown(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = root.getChild("stand2");
		this.head = root.getChild("head");
		this.head2 = root.getChild("head2");
		this.extra_details = root.getChild("extra_details");
		this.top = root.getChild("top");
		this.body = root.getChild("body");
		this.body2 = root.getChild("body2");
		this.torso = root.getChild("torso");
		this.upper_chest = root.getChild("upper_chest");
		this.upper_straps = root.getChild("upper_straps");
		this.right_arm = root.getChild("right_arm");
		this.upper_right_arm = root.getChild("upper_right_arm");
		this.lower_right_arm = root.getChild("lower_right_arm");
		this.left_arm = root.getChild("left_arm");
		this.upper_left_arm = root.getChild("upper_left_arm");
		this.lower_left_arm = root.getChild("lower_left_arm");
		this.tanks = root.getChild("tanks");
		this.lower_chest = root.getChild("lower_chest");
		this.lower_torso = root.getChild("lower_torso");
		this.lower_straps = root.getChild("lower_straps");
		this.belt = root.getChild("belt");
		this.legs = root.getChild("legs");
		this.right_leg = root.getChild("right_leg");
		this.upper_right_leg = root.getChild("upper_right_leg");
		this.lower_right_leg = root.getChild("lower_right_leg");
		this.left_leg = root.getChild("left_leg");
		this.upper_left_leg = root.getChild("upper_left_leg");
		this.lower_left_leg = root.getChild("lower_left_leg");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData stand = modelPartData.addChild("stand", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

		ModelPartData stand2 = stand.addChild("stand2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, 0.0F));

		ModelPartData head = stand2.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.15F, 0.0F));

		ModelPartData head2 = head.addChild("head2", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -5.85F, -4.0F, 8.0F, 6.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 6).cuboid(-1.0F, -0.65F, -4.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData extra_details = head2.addChild("extra_details", ModelPartBuilder.create().uv(0, 14).cuboid(-4.0F, 1.7886F, -5.0302F, 8.0F, 5.0F, 8.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -8.8761F, 1.0302F));

		ModelPartData top = extra_details.addChild("top", ModelPartBuilder.create().uv(60, 45).cuboid(-4.0F, -0.6114F, 0.9698F, 8.0F, 2.0F, 2.0F, new Dilation(0.246F))
		.uv(0, 73).cuboid(-4.0F, -0.6F, -5.0302F, 8.0F, 2.0F, 8.0F, new Dilation(0.2499F)), ModelTransform.pivot(0.0F, -0.1F, 0.0F));

		ModelPartData top_r1 = top.addChild("top_r1", ModelPartBuilder.create().uv(24, 0).cuboid(-4.0F, -0.2671F, -5.2528F, 8.0F, 2.0F, 6.0F, new Dilation(0.2497F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3944F, 0.0F, 0.0F));

		ModelPartData body = stand2.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

		ModelPartData body2 = body.addChild("body2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData torso = body2.addChild("torso", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData upper_chest = torso.addChild("upper_chest", ModelPartBuilder.create().uv(24, 27).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData upper_straps = upper_chest.addChild("upper_straps", ModelPartBuilder.create().uv(16, 37).cuboid(-3.9F, -24.001F, -3.0F, 2.0F, 6.0F, 6.0F, new Dilation(0.05F))
		.uv(0, 37).cuboid(1.9F, -24.001F, -3.0F, 2.0F, 6.0F, 6.0F, new Dilation(0.05F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData right_arm = upper_chest.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-4.0F, 0.75F, 0.0F));

		ModelPartData upper_right_arm = right_arm.addChild("upper_right_arm", ModelPartBuilder.create().uv(16, 53).cuboid(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(60, 23).cuboid(-4.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_right_arm = right_arm.addChild("lower_right_arm", ModelPartBuilder.create().uv(0, 49).cuboid(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(28, 59).cuboid(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(-2.0F, 5.5F, 0.0F));

		ModelPartData left_arm = upper_chest.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, 0.75F, 0.0F));

		ModelPartData upper_left_arm = left_arm.addChild("upper_left_arm", ModelPartBuilder.create().uv(48, 30).cuboid(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(0, 59).cuboid(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_left_arm = left_arm.addChild("lower_left_arm", ModelPartBuilder.create().uv(28, 47).cuboid(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(52, 58).cuboid(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(2.0F, 5.5F, 0.0F));

		ModelPartData tanks = upper_chest.addChild("tanks", ModelPartBuilder.create().uv(65, 64).cuboid(-3.5F, -23.0F, 2.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F))
		.uv(41, 65).cuboid(0.5F, -23.0F, 2.0F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData lower_chest = torso.addChild("lower_chest", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.0F, 0.0F));

		ModelPartData lower_torso = lower_chest.addChild("lower_torso", ModelPartBuilder.create().uv(0, 27).cuboid(-4.0F, -18.0F, -2.0F, 8.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 18.0F, 0.0F));

		ModelPartData lower_straps = lower_torso.addChild("lower_straps", ModelPartBuilder.create().uv(11, 63).cuboid(-3.9F, -18.0F, -2.75F, 2.0F, 5.0F, 5.0F, new Dilation(0.05F))
		.uv(59, 35).cuboid(1.9F, -18.0F, -2.75F, 2.0F, 5.0F, 5.0F, new Dilation(0.05F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData belt = lower_torso.addChild("belt", ModelPartBuilder.create().uv(24, 14).cuboid(-4.05F, -13.0F, -2.5F, 8.0F, 1.0F, 5.0F, new Dilation(0.1F))
		.uv(0, 14).cuboid(-1.5F, -14.0F, -2.7F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData legs = body2.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData right_leg = legs.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, -1.0F, 0.0F));

		ModelPartData upper_right_leg = right_leg.addChild("upper_right_leg", ModelPartBuilder.create().uv(44, 20).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(56, 49).cuboid(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F))
		.uv(0, 0).cuboid(-1.5F, 5.0F, -2.3F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_right_leg = right_leg.addChild("lower_right_leg", ModelPartBuilder.create().uv(32, 37).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(40, 53).cuboid(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

		ModelPartData left_leg = legs.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, -1.0F, 0.0F));

		ModelPartData upper_left_leg = left_leg.addChild("upper_left_leg", ModelPartBuilder.create().uv(45, 8).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
		.uv(57, 14).cuboid(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F))
		.uv(0, 3).cuboid(-1.5F, 5.0F, -2.3F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_left_leg = left_leg.addChild("lower_left_leg", ModelPartBuilder.create().uv(44, 43).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
		.uv(57, 0).cuboid(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F)), ModelTransform.pivot(0.0F, 7.0F, 0.0F));
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