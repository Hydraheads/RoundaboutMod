package net.hydra.jojomod.client.models.worn_stand;// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.CenturyBoyAnimations;
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
	private final ModelPart RightArmSlim;
	private final ModelPart LeftArmSlim;
	private final ModelPart Root;
	private final ModelPart Breast;

	public CenturyBoyModel() {
		this.Root = createBodyLayer().bakeRoot();
		this.Waist = Root.getChild("stand");
		this.Head = this.Waist.getChild("head");
		this.Body = this.Waist.getChild("body");
		this.RightArm = this.Waist.getChild("rightArm");
		this.RightArmSlim = this.Waist.getChild("rightArmSlim");
		this.LeftArm = this.Waist.getChild("leftArm");
		this.LeftArmSlim = this.Waist.getChild("leftArmSlim");
		this.Breast = this.Waist.getChild("breast");
	}



	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 8.4F));

		PartDefinition head = stand.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 31).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.55F)), PartPose.offset(0.0F, 0.0F, -8.4F));

		PartDefinition rightAntennaR1 = head.addOrReplaceChild("RightAntenna_r1", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -10.0F, -7.0F, 0.0F, 17.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -7.0F, -2.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition leftAntennaR1 = head.addOrReplaceChild("leftAntenna_r1", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -10.0F, -7.0F, 0.0F, 17.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -7.0F, -2.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition body = stand.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 47).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.3F))
				.texOffs(64, 59).addBox(11.0F, -4.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(64, 59).addBox(-11.0F, -4.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(78, 0).addBox(4.0F, -4.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(64, 40).addBox(9.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(64, 40).addBox(-11.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(56, 0).addBox(-9.0F, -4.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -8.4F));

		PartDefinition breast = stand.addOrReplaceChild("breast", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chestR1 = breast.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(48, 75).addBox(-4.2F, 0.0F, -0.1F, 8.4F, 4.1F, 4.1F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, -0.4F, -2.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition rightArm = stand.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(16, 80).addBox(0.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(-8.0F, 4.0F, -8.4F));

		PartDefinition rightArmSlim = stand.addOrReplaceChild("rightArmSlim", CubeListBuilder.create().texOffs(112, 17).addBox(1.0F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(-8.0F, 4.0F, -8.4F));

		PartDefinition leftArm = stand.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(16, 63).addBox(-4.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(8.0F, 4.0F, -8.4F));

		PartDefinition leftArmSlim = stand.addOrReplaceChild("leftArmSlim", CubeListBuilder.create().texOffs(112, 0).addBox(-4.0F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(8.0F, 4.0F, -8.4F));

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
	public static ResourceLocation salmonberry = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/salmonberry.png");
	public static ResourceLocation chicken = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/chicken.png");
	public static ResourceLocation older_century_boy = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/10th_century_boy.png");
	public static ResourceLocation oldest_century_boy = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/11th_century_boy.png");
	public static ResourceLocation beta = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/20_centuryboy/beta.png");


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
			case Powers20thCenturyBoy.SALMONBERRY -> {return salmonberry;}
			case Powers20thCenturyBoy.CHICKEN -> {return chicken;}
			case Powers20thCenturyBoy.OLDER_CENTURY_BOY -> {return older_century_boy;}
			case Powers20thCenturyBoy.OLDEST_CENTURY_BOY -> {return oldest_century_boy;}
			case Powers20thCenturyBoy.BETA ->  {return  beta;}
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
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			Body.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderHead(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
						   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			Head.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderRightArm(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
							  int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			RightArm.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderLeftArm(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
						   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity) {
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			LeftArm.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderRightArmSlim(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
							   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			RightArmSlim.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderLeftArmSlim(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
							  int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity) {
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			LeftArmSlim.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
	public void renderBreast(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
							  int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity) {
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			if (ClientUtil.hasChangedBody(context)) {
				consumer = bufferSource.getBuffer(RenderType.entityTranslucent(ClientUtil.getChangedBodyBreastTexture(context)));
			}
			Breast.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);

		}
	}
	public void renderAll(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
					   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity) {
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));

			Body.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
			Head.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
			RightArm.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
			LeftArm.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
		}
	}

	public void renderPart(Entity context, float partialTicks, LivingEntity LE){
		this.root().getAllParts().forEach(ModelPart::resetPose);

		StandUser user = ((StandUser) LE);
		user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount);
		if (user.roundabout$getIdlePos() == 2){
			this.animate(user.roundabout$getWornStandIdleAnimation(), CenturyBoyAnimations.standanim.hat,partialTicks, 1f);
		}
		if (user.roundabout$getIdlePos() == 1){
			this.animate(user.roundabout$getWornStandIdleAnimation(), CenturyBoyAnimations.standanim.unlatch,partialTicks, 1f);
		}

	}

}