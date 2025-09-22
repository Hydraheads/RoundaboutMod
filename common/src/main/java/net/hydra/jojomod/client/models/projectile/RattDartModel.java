package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class RattDartModel extends Model {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart knife;

	public RattDartModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.knife = root;
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition dart = partdefinition.addOrReplaceChild("dart", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -9.0F, 0.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition cube_r1 = dart.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 4).addBox(-0.0354F, -24.0F, -0.5404F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, 16.0F, 0.55F, 0.0F, -2.3562F, 0.0F));

		PartDefinition cube_r2 = dart.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 0).addBox(0.0F, -24.0F, -1.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.35F, 16.0F, 0.85F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r3 = dart.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(2, 0).addBox(0.0F, -25.0F, -0.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.5F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
    }


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		knife.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}