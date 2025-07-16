package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

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

		PartDefinition knife = partdefinition.addOrReplaceChild("knife", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 2.0F, -0.5F, 0.0F, 1.5708F, 1.5708F));

		PartDefinition handle = knife.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 9).addBox(-1.0F, -1.95F, 10.05F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.05F)), PartPose.offset(0.5F, -0.5F, -10.0F));

		PartDefinition blade = knife.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 3.75F, -2.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(5, 2).addBox(0.0F, 3.75F, -3.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.25F, -3.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		knife.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}