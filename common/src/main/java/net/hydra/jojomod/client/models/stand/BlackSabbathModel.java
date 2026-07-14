package net.hydra.jojomod.client.models.stand;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.BlackSabbathAnimations;
import net.hydra.jojomod.client.models.stand.animations.ManhattanTransferAnimations;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.hydra.jojomod.stand.powers.PowersPlanetWaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BlackSabbathModel<T extends BlackSabbathEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart stand2;
	private final ModelPart head2;
	private final ModelPart hat;
	private final ModelPart jaw;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart upper_chest_only;
	private final ModelPart decor;
	private final ModelPart coat_open;
	private final ModelPart coat_closed;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_left_arm;
	private final ModelPart lower_chest;
	private final ModelPart lower_torso;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart upper_right_leg;
	private final ModelPart lower_right_leg;
	private final ModelPart left_leg;
	private final ModelPart upper_left_leg;
	private final ModelPart lower_left_leg;

    @Override
    public void defaultModifiers(T entity) {
        Minecraft mc = Minecraft.getInstance();
        if (entity.getUser() != null) {
            LivingEntity User = entity.getUser();
            if (!mc.isPaused() && !(((TimeStop) entity.level()).CanTimeStopEntity(User))) {
                float tickDelta = mc.getDeltaFrameTime();
            }
        }
        super.defaultModifiers(entity);
    }

	public BlackSabbathModel(ModelPart root) {
		this.stand = root.getChild("stand");

        this.stand2 = stand.getChild("stand2");

        this.head = stand.getChild("stand2").getChild("head");

		this.head2 = stand.getChild("stand2").getChild("head").getChild("head2");

		this.hat = stand.getChild("stand2").getChild("head").getChild("head2").getChild("hat");
		this.jaw = stand.getChild("stand2").getChild("head").getChild("head2").getChild("jaw");
		this.body = stand.getChild("stand2").getChild("body");
		this.body2 = stand.getChild("stand2").getChild("body").getChild("body2");
		this.torso = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso");
		this.upper_chest = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest");
		this.upper_chest_only = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("upper_chest_only");
		this.decor = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("upper_chest_only").getChild("decor");
		this.coat_open = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("upper_chest_only").getChild("coat_open");
		this.coat_closed = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("upper_chest_only").getChild("coat_closed");
		this.right_arm = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("right_arm");
		this.upper_right_arm = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("upper_right_arm");
		this.lower_right_arm = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
		this.left_arm = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("left_arm");
		this.upper_left_arm = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("upper_left_arm");
		this.lower_left_arm = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
		this.lower_chest = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("lower_chest");
		this.lower_torso = stand.getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("lower_chest").getChild("lower_torso");
		this.legs = stand.getChild("stand2").getChild("body").getChild("body2").getChild("legs");
		this.right_leg = stand.getChild("stand2").getChild("body").getChild("body2").getChild("legs").getChild("right_leg");
		this.upper_right_leg = stand.getChild("stand2").getChild("body").getChild("body2").getChild("legs").getChild("right_leg").getChild("upper_right_leg");
		this.lower_right_leg = stand.getChild("stand2").getChild("body").getChild("body2").getChild("legs").getChild("right_leg").getChild("lower_right_leg");
		this.left_leg = stand.getChild("stand2").getChild("body").getChild("body2").getChild("legs").getChild("left_leg");
		this.upper_left_leg = stand.getChild("stand2").getChild("body").getChild("body2").getChild("legs").getChild("left_leg").getChild("upper_left_leg");
		this.lower_left_leg = stand.getChild("stand2").getChild("body").getChild("body2").getChild("legs").getChild("left_leg").getChild("lower_left_leg");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(32, 32).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(93, 33).addBox(-0.5F, -7.8F, 3.275F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(96, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_horn_r1 = head2.addOrReplaceChild("left_horn_r1", CubeListBuilder.create().texOffs(48, 13).addBox(0.0F, 0.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -5.85F, -1.0F, 0.0F, 0.0F, 0.48F));

		PartDefinition right_horn_r1 = head2.addOrReplaceChild("right_horn_r1", CubeListBuilder.create().texOffs(44, 27).addBox(-3.0F, 0.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -5.85F, -1.0F, 0.0F, 0.0F, -0.48F));

		PartDefinition hat = head2.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, -9.35F, 0.2929F));

		PartDefinition hat_r1 = hat.addOrReplaceChild("hat_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -2.5F, -6.5F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(80, 39).addBox(-5.5F, -2.5F, -6.5F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1841F, 0.7769F, 0.1298F));

		PartDefinition jaw = head2.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(70, 47).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.85F, -4.2F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(44, 17).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition arrows_r1 = upper_chest_only.addOrReplaceChild("arrows_r1", CubeListBuilder.create().texOffs(86, 16).addBox(-5.5F, -2.025F, -5.5F, 11.0F, 4.0F, 10.0F, new CubeDeformation(0.25F))
		.texOffs(1, 18).addBox(-5.5F, -2.025F, -5.5F, 11.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.5F, 0.5F, 0.3491F, 0.0F, 0.0F));

		PartDefinition decor = upper_chest_only.addOrReplaceChild("decor", CubeListBuilder.create().texOffs(54, 27).addBox(-4.25F, -0.25F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 18).addBox(2.25F, -2.25F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 68).addBox(-4.25F, -2.25F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 13).addBox(1.25F, -0.25F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.025F, -3.5F));

		PartDefinition coat_open = upper_chest_only.addOrReplaceChild("coat_open", CubeListBuilder.create().texOffs(80, 72).addBox(-9.0F, -2.0F, -3.0F, 18.0F, 21.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(80, 101).addBox(-9.0F, -2.0F, -3.0F, 18.0F, 21.0F, 6.0F, new CubeDeformation(-0.05F)), PartPose.offset(0.0F, -5.025F, 0.0F));

		PartDefinition coat_closed = upper_chest_only.addOrReplaceChild("coat_closed", CubeListBuilder.create().texOffs(0, 32).addBox(-5.0F, -1.25F, -3.0F, 10.0F, 20.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(41, 77).addBox(-6.0F, -1.25F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(57, 77).addBox(5.0F, -1.25F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(40, 102).addBox(-5.0F, -1.25F, -3.0F, 10.0F, 20.0F, 6.0F, new CubeDeformation(-0.05F)), PartPose.offset(0.0F, -4.775F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(33, 57).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(65, 0).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(1, 58).addBox(-1.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(33, 67).addBox(-1.0F, 0.75F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(16, 58).addBox(0.0F, -0.75F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(64, 27).addBox(0.0F, -0.75F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(48, 58).addBox(-2.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(64, 67).addBox(-2.0F, 0.75F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(32, 47).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(64, 58).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(56, 47).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 68).addBox(-2.0F, 0.7999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F))
		.texOffs(23, 82).addBox(-2.0F, 4.7999F, -3.3998F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.201F))
		.texOffs(25, 80).addBox(-0.1F, 4.7999F, 2.3502F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(64, 36).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(68, 9).addBox(-2.0F, 0.8F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F))
		.texOffs(31, 82).addBox(-1.0F, 4.7999F, -3.3998F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.201F))
		.texOffs(33, 80).addBox(-0.9F, 4.7999F, 2.3502F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

    StandPowers Power = new PowersBlackSabbath(null);

    @Override
    public ModelPart root() {
        return stand;
    }

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        defaultModifiers(pEntity);
        super.setupAnim(pEntity,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
        this.animate(pEntity.coat_open, BlackSabbathAnimations.CoatOpen, pAgeInTicks, 1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}