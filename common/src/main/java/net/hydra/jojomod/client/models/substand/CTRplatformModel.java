package net.hydra.jojomod.client.models.substand;// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.substand.CTRplatformEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CTRplatformModel<T extends CTRplatformEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ctrplatformmodel"), "main");
	private final ModelPart platform;

	public CTRplatformModel(ModelPart root) {
		this.platform = root.getChild("platform");
	}

	@Override
	public ModelPart root() {
		return platform;
	}

	public ModelPart getStand() {
		return platform;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition platform = partdefinition.addOrReplaceChild("platform", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -8.0F, -10.0F, 20.0F, 8.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		platform.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}