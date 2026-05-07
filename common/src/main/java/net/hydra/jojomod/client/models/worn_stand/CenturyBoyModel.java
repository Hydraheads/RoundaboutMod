package net.hydra.jojomod.client.models.worn_stand;// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
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

public class CenturyBoyModel extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "20cb"), "main");
	private final ModelPart Waist;
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart Root;

	public CenturyBoyModel() {
		this.Root = createBodyLayer().bakeRoot();
		this.Waist = Root.getChild("Waist");
		this.Head = this.Waist.getChild("Head");
		this.Body = this.Waist.getChild("Body");
		this.RightArm = this.Waist.getChild("RightArm");
		this.LeftArm = this.Waist.getChild("LeftArm");
	}



	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Waist = partdefinition.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition Head = Waist.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(32, 31).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.55F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition RightAntenna_r1 = Head.addOrReplaceChild("RightAntenna_r1", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -10.0F, -7.0F, 0.0F, 17.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -8.0F, -2.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition leftAntenna_r1 = Head.addOrReplaceChild("leftAntenna_r1", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -10.0F, -7.0F, 0.0F, 17.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -8.0F, -2.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition Body = Waist.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 47).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.3F))
		.texOffs(56, 0).addBox(-9.0F, -3.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(78, 0).addBox(4.0F, -3.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(64, 40).addBox(9.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(64, 40).addBox(-11.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(64, 59).addBox(-11.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(64, 59).addBox(11.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition RightArm = Waist.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 80).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(-5.0F, -10.0F, 0.0F));

		PartDefinition LeftArm = Waist.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 63).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(5.0F, -10.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Waist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return Root;
	}

	@Override
	public void setupAnim(Entity var1, float ageInTicks) {

	}

	public static ResourceLocation manga = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/manga.png");
	public static ResourceLocation bedrock = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/bedrock.png");
	public static ResourceLocation bee = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/bee.png");
	public static ResourceLocation biker = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/biker.png");
	public static ResourceLocation retro = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/retro.png");
	public static ResourceLocation gold = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/gold.png");
	public static ResourceLocation old_century_boy = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/19th_century_boy.png");
	public static ResourceLocation shulker = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/shulker.png");
	public static ResourceLocation sulfur = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/sulfur.png");
	public static ResourceLocation lemon = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/lemon.png");
	public static ResourceLocation blue = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/blue.png");
	public static ResourceLocation grape = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/grape.png");
	public static ResourceLocation strawberry = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/strawberry.png");


	public ResourceLocation getTextureLocation(Entity context, byte skin){
		switch (skin){
			case Powers20thCenturyBoy.BEDROCK -> {return bedrock;}
			case Powers20thCenturyBoy.BEE -> {return bee;}
			case Powers20thCenturyBoy.BIKER -> {return biker;}
			case Powers20thCenturyBoy.RETRO -> {return retro;}
			case Powers20thCenturyBoy.GOLD -> {return gold;}
			case Powers20thCenturyBoy.OLD_CENTURY_BOY -> {return old_century_boy;}
			case Powers20thCenturyBoy.SHULKER -> {return shulker;}
			case Powers20thCenturyBoy.SULFUR -> {return sulfur;}
			case Powers20thCenturyBoy.LEMON -> {return lemon;}
			case Powers20thCenturyBoy.BLUE -> {return blue;}
			case Powers20thCenturyBoy.GRAPE -> {return grape;}
			case Powers20thCenturyBoy.STRAWBERRY -> {return strawberry;}
			default -> {return manga;}
		}
	}



	public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context, (byte)0)));
		root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
	}

	public void renderBody(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
						   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
				partialTicks = 0;
			}
			StandUser user = ((StandUser) LE);
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			root().getChild("Waist").getChild("Body").render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderHead(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
						   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
				partialTicks = 0;
			}
			StandUser user = ((StandUser) LE);
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			root().getChild("Waist").getChild("Head").render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderRightArm(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
							  int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
				partialTicks = 0;
			}
			StandUser user = ((StandUser) LE);
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			root().getChild("Waist").getChild("RightArm").render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderLeftArm(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
						   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
				partialTicks = 0;
			}
			StandUser user = ((StandUser) LE);
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			root().getChild("Waist").getChild("LeftArm").render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}

	public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
					   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
				partialTicks = 0;
			}
			StandUser user = ((StandUser) LE);
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
			/** put animation here later **/


			root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
		}
	}

}