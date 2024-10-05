// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ModItemModels;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import javax.swing.text.html.parser.Entity;

public class HarpoonModel extends Model {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "harpoon"), "main");
	private final ModelPart harpoon;

	public HarpoonModel(ModelPart root) {

		super(RenderType::entityCutout);
		this.harpoon = root;
		ModItemModels.HARPOON_MODEL = this;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition harpoon = partdefinition.addOrReplaceChild("harpoon", CubeListBuilder.create().texOffs(13, 13).addBox(0.575F, -2.0F, -1.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, -7).addBox(1.075F, -9.0F, -4.0F, 0.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.425F, -9.0F, -0.5F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.575F, 3.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		harpoon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}