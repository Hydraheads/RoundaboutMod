package net.hydra.jojomod.client.models.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.entity.projectile.SilverChariotRapierPlatformEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class SilverChariotRapierPlatformModel<T extends SilverChariotRapierPlatformEntity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	// public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart sword;

	public SilverChariotRapierPlatformModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.sword = root.getChild("sword");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition sword = partdefinition.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, -2.0F, -11.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 12.75F, -4.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		sword.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}