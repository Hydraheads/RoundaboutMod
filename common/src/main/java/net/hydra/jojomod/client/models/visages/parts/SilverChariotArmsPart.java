package net.hydra.jojomod.client.models.visages.parts;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.stand.animations.SilverChariotAnimations;
import net.hydra.jojomod.client.models.stand.renderers.SilverChariotRenderer;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SilverChariotArmsPart extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	// public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart root;
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart right_elbow;
	private final ModelPart right_elbow_ARMOR;
	private final ModelPart right_shoulder_ARMOR;
	private final ModelPart lower_right_arm;
	private final ModelPart right_sword;
	private final ModelPart right_blade;
	private final ModelPart right_tip;
	private final ModelPart right_wrist_ARMOR;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart left_elbow;
	private final ModelPart left_elbow_ARMOR;
	private final ModelPart left_shoulder_ARMOR;
	private final ModelPart lower_left_arm;
	private final ModelPart left_sword;
	private final ModelPart left_blade;
	private final ModelPart left_tip;
	private final ModelPart left_wrist_ARMOR;

	public SilverChariotArmsPart() {
		super(RenderType::entityTranslucent);

		this.root = createBodyLayer().bakeRoot();
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.upper_chest = this.torso.getChild("upper_chest");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.right_elbow = this.upper_right_arm.getChild("right_elbow");
		this.right_elbow_ARMOR = this.right_elbow.getChild("right_elbow_ARMOR");
		this.right_shoulder_ARMOR = this.upper_right_arm.getChild("right_shoulder_ARMOR");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.right_sword = this.lower_right_arm.getChild("right_sword");
		this.right_blade = this.right_sword.getChild("right_blade");
		this.right_tip = this.right_blade.getChild("right_tip");
		this.right_wrist_ARMOR = this.lower_right_arm.getChild("right_wrist_ARMOR");
		this.left_arm = this.upper_chest.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.left_elbow = this.upper_left_arm.getChild("left_elbow");
		this.left_elbow_ARMOR = this.left_elbow.getChild("left_elbow_ARMOR");
		this.left_shoulder_ARMOR = this.upper_left_arm.getChild("left_shoulder_ARMOR");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.left_sword = this.lower_left_arm.getChild("left_sword");
		this.left_blade = this.left_sword.getChild("left_blade");
		this.left_tip = this.left_blade.getChild("left_tip");
		this.left_wrist_ARMOR = this.lower_left_arm.getChild("left_wrist_ARMOR");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(34, 29).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(34, 17).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition right_elbow = upper_right_arm.addOrReplaceChild("right_elbow", CubeListBuilder.create().texOffs(56, 88).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 4.65F, 2.5F));

		PartDefinition right_elbow_ARMOR = right_elbow.addOrReplaceChild("right_elbow_ARMOR", CubeListBuilder.create().texOffs(26, 36).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition right_shoulder_ARMOR = upper_right_arm.addOrReplaceChild("right_shoulder_ARMOR", CubeListBuilder.create().texOffs(19, 51).mirror().addBox(-1.0F, -2.0F, -2.5F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.2F)).mirror(false)
				.texOffs(0, 44).mirror().addBox(0.0F, -5.2F, -4.5F, 0.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(21, 61).mirror().addBox(-1.0F, 1.4F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(-3.2F, 0.15F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(34, 40).addBox(-1.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(34, 51).addBox(-1.0F, 0.75F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.205F))
				.texOffs(114, 57).addBox(-1.0F, 1.75F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.15F))
				.texOffs(119, 64).addBox(0.0F, 1.75F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition right_sword = lower_right_arm.addOrReplaceChild("right_sword", CubeListBuilder.create().texOffs(112, 100).addBox(-1.5F, -1.5F, -2.7667F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F))
				.texOffs(112, 92).addBox(-0.5F, -0.5F, -4.0667F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.25F, 0.0667F));

		PartDefinition right_blade = right_sword.addOrReplaceChild("right_blade", CubeListBuilder.create().texOffs(92, 107).addBox(0.0F, -2.5F, -5.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0667F));

		PartDefinition right_tip = right_blade.addOrReplaceChild("right_tip", CubeListBuilder.create().texOffs(97, 110).addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.0F));

		PartDefinition right_wrist_ARMOR = lower_right_arm.addOrReplaceChild("right_wrist_ARMOR", CubeListBuilder.create().texOffs(0, 44).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.26F)), PartPose.offset(0.5F, 3.25F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(49, 29).addBox(0.0F, -0.75F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(49, 17).addBox(0.0F, -0.75F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_elbow = upper_left_arm.addOrReplaceChild("left_elbow", CubeListBuilder.create().texOffs(56, 88).mirror().addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, 4.75F, 2.5F));

		PartDefinition left_elbow_ARMOR = left_elbow.addOrReplaceChild("left_elbow_ARMOR", CubeListBuilder.create().texOffs(26, 36).mirror().addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition left_shoulder_ARMOR = upper_left_arm.addOrReplaceChild("left_shoulder_ARMOR", CubeListBuilder.create().texOffs(19, 51).addBox(-1.0F, -2.0F, -2.5F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.2F))
				.texOffs(0, 44).addBox(0.0F, -5.2F, -4.5F, 0.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(21, 61).addBox(-1.0F, 1.4F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(3.2F, 0.25F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(49, 40).addBox(-2.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(49, 51).addBox(-2.0F, 0.75F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.205F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition left_sword = lower_left_arm.addOrReplaceChild("left_sword", CubeListBuilder.create().texOffs(112, 100).mirror().addBox(-1.5F, -1.5F, -2.7667F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false)
				.texOffs(112, 92).mirror().addBox(-0.5F, -0.5F, -4.0667F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.5F, 5.25F, 0.0667F));

		PartDefinition left_blade = left_sword.addOrReplaceChild("left_blade", CubeListBuilder.create().texOffs(92, 107).mirror().addBox(0.0F, -2.5F, -5.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -4.0667F));

		PartDefinition left_tip = left_blade.addOrReplaceChild("left_tip", CubeListBuilder.create().texOffs(97, 110).mirror().addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -5.0F));

		PartDefinition left_wrist_ARMOR = lower_left_arm.addOrReplaceChild("left_wrist_ARMOR", CubeListBuilder.create().texOffs(14, 44).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.26F)), PartPose.offset(-0.5F, 3.25F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(Entity var1, float ageInTicks) {

	}

	public ResourceLocation getTextureLocation(Entity context) {
		byte bt = 0;
		if (context instanceof LivingEntity LE) {
			bt = ((StandUser) LE).roundabout$getStandSkin();
		}
		return SilverChariotRenderer.getSkin(bt);
	}

	public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
		if (!ClientUtil.canSeeStands(ClientUtil.getPlayer()))
			return;
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context)));
		root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
	}

	public void render(
			Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
			int light, float r, float g, float b, float alpha, float speed
	) {
		if (!ClientUtil.canSeeStands(ClientUtil.getPlayer()))
			return;
		if (context instanceof LivingEntity LE) {
			StandUser user = (StandUser) LE;
			if (user.roundabout$getStandPowers() instanceof PowersSilverChariot psc) {
				this.root().getAllParts().forEach(ModelPart::resetPose);
				if (((TimeStop) context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()) {
					partialTicks = 0;
				}
				int heyTicks = user.roundabout$getArmVanishTicks();
				boolean hasHeyYaOut = (PowerTypes.hasStandActive(LE) && PowerTypes.hasHandsActive(LE) &&
						(PowerTypes.hasHandsActiveRendering(LE) || ClientUtil.inPowerInventory));
				ClientUtil.skinTicker(ClientUtil.lastSkin,((StandUser)LE).roundabout$getStandSkin());


				float heyFull = 0;
				float fixedPartial = partialTicks % 1;
				if (ClientUtil.inPowerInventory && PowerTypes.hasStandActivelyEquipped(LE)
						&& ((StandUser)LE).roundabout$getStandPowers().hasHandsOut()){
					heyFull = ClientUtil.skinTicker + fixedPartial;
					heyFull = Math.min(heyFull / 10, 1f);
				} else {
					if (hasHeyYaOut) {
						heyFull = heyTicks + fixedPartial;
						heyFull = Math.min(heyFull / 10, 1f);
					} else {
						heyFull = heyTicks - fixedPartial;
						heyFull = Math.max(heyFull / 10, 0);
					}
					heyFull = Math.min(heyFull, alpha);
				}
				if (heyFull <= 0) {
					return;
				}
				byte animation = user.roundabout$getStandAnimation();


				VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(LE)));
				//The number at the end is inversely proportional so 2 is half speed
				if (!psc.isRightHanded()) {
					this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.HideRightArm(), context.tickCount+fixedPartial, 1f);
				} else {
					this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.HideLeftArm(), context.tickCount+fixedPartial, 1f);
				}
				user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount);
				if (animation == StandPowers.GUARD) {
					if (psc.isRightHanded()) {
						this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.RightGuardStart(), context.tickCount+fixedPartial, 1f);
					} else {
						this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.LeftGuardStart(), context.tickCount+fixedPartial, 1f);
					}
				} else {
					this.animate(user.roundabout$getWornStandIdleAnimation(), SilverChariotAnimations.Idle(), context.tickCount+fixedPartial, 1f);
					if (animation == StandPowers.PUNCH_RIGHT) {
						this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.RightHit1(), context.tickCount+fixedPartial, speed);
					} else if (animation == StandPowers.PUNCH_LEFT) {
						this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.LeftHit1(), context.tickCount+fixedPartial, speed);
					} else if (animation == StandPowers.VAULT) {
						if (psc.isRightHanded()) {
							this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.RightVault(), context.tickCount+fixedPartial, 1f);
						} else {
							this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.LeftVault(), context.tickCount+fixedPartial, 1f);
						}
					} else if (animation == StandPowers.MINING) {
						if (psc.isRightHanded()) {
							this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.LeftBarrage(), context.tickCount+fixedPartial, 1f);
						} else {
							this.animate(user.roundabout$getWornStandActiveAnimation(), SilverChariotAnimations.RightBarrage(), context.tickCount+fixedPartial, 1f);
						}
					}

					root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, heyFull);
				}
			}
		}
	}
}