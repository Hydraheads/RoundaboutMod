// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
package net.hydra.jojomod.entity.stand;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;


public class TheWorldModel<T extends TheWorldEntity> extends StandModel<T> {
	public TheWorldModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.head = stand.getChild("stand2").getChild("head");
		this.body = stand.getChild("stand2").getChild("body");
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

		ModelPartData legs = body2.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData right_leg = legs.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, -13.0F, 0.0F));

		ModelPartData upper_right_leg = right_leg.addChild("upper_right_leg", ModelPartBuilder.create().uv(44, 20).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
				.uv(56, 49).cuboid(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F))
				.uv(0, 0).cuboid(-1.5F, 5.0F, -2.3F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_right_leg = right_leg.addChild("lower_right_leg", ModelPartBuilder.create().uv(32, 37).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
				.uv(40, 53).cuboid(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

		ModelPartData left_leg = legs.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, -13.0F, 0.0F));

		ModelPartData upper_left_leg = left_leg.addChild("upper_left_leg", ModelPartBuilder.create().uv(45, 8).cuboid(0.0F, -12.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.01F))
				.uv(57, 14).cuboid(0.0F, -12.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F))
				.uv(0, 3).cuboid(0.5F, -8.0F, -2.3F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 13.0F, 0.0F));

		ModelPartData lower_left_leg = left_leg.addChild("lower_left_leg", ModelPartBuilder.create().uv(44, 43).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
				.uv(57, 0).cuboid(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new Dilation(0.201F)), ModelTransform.pivot(0.0F, 7.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

	}

	@Override
	public ModelPart getPart() {
		return stand;
	}
}