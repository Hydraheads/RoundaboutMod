package net.hydra.jojomod.client.models.worn_stand;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MoldRightArm<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "mold_right_arm"), "main");
	private final ModelPart root;
	private final ModelPart Body;

	public ModelPart root() {
		return root;
	}

	public MoldRightArm() {
		super(RenderType::entityCutout);
		this.root = createBodyLayer().bakeRoot();
		this.Body = this.root.getChild("Body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, -0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
	public ResourceLocation getTextureLocation(){
		return new ResourceLocation(Roundabout.MOD_ID,
				"textures/stand/green_day/mold_main_arm_texture.png");
	}

	public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation()));
		root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
					   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation()));
			//r = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
			root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
		}
	}

}