package net.hydra.jojomod.client.models.substand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SheerHeartAttackModel<T extends SheerHeartAttackEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "sheer_heart_attack_model"), "main");

	public SheerHeartAttackModel(ModelPart root) {
		this.stand = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -4.0F, -4.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 6).addBox(-1.5F, -5.0F, -3.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(-4.0F, -3.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(18, 10).addBox(3.0F, -3.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 4).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(24, 5).addBox(-2.0F, -5.0F, 2.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(8, 21).addBox(2.0F, -5.0F, -2.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 21).addBox(-2.0F, -5.0F, -2.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}