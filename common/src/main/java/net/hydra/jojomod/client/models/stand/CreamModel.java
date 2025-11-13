package net.hydra.jojomod.client.models.stand;// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.hydra.jojomod.client.models.stand.animations.CreamAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.entity.stand.CreamEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersCream;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CreamModel<T extends CreamEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");

    public final ModelPart voidBone;
    public final ModelPart void1;
    public final ModelPart void2;

	public CreamModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
        this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
        this.voidBone = stand.getChild("stand2").getChild("voidBone");
        this.void1 = voidBone.getChild("void1");
        this.void2 = voidBone.getChild("void2");
	}

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 3.75F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_head = head2.addOrReplaceChild("upper_head", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -7.65F, -4.0F, 8.0F, 5.75F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(94, 95).addBox(-4.0F, -7.75F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.01F))
                .texOffs(64, 114).addBox(-3.4F, -7.2F, -3.8F, 7.0F, 5.65F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = upper_head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(2, 2).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.5F, -7.1F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition cube_r2 = upper_head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(2, 2).mirror().addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.5F, -7.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r3 = upper_head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.5F, -7.1F, 0.0F, 0.0F, 1.5708F, -1.1781F));

        PartDefinition cube_r4 = upper_head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, -7.1F, 0.0F, 0.0F, -1.5708F, 1.1781F));

        PartDefinition cube_r5 = upper_head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, -7.1F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition cube_r6 = upper_head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -7.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition head4_r1 = upper_head.addOrReplaceChild("head4_r1", CubeListBuilder.create().texOffs(18, 117).mirror().addBox(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, -6.1F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r7 = upper_head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(100, 79).mirror().addBox(-3.5F, -2.0F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.7977F, 0.0626F, 1.458F, 0.0F, 1.5272F, -0.3927F));

        PartDefinition cube_r8 = upper_head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(100, 79).addBox(-3.5F, -2.0F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.7977F, 0.0626F, 1.458F, 0.0F, -1.5272F, 0.3927F));

        PartDefinition cube_r9 = upper_head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(97, 73).addBox(-5.0F, -2.5F, 0.0F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.01F, -0.44F, 4.31F, 0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r10 = upper_head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(46, 107).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.85F, -3.85F, 0.0F, 0.0F, -3.1416F));

        PartDefinition cube_r11 = upper_head.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(50, 104).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4696F, -3.0527F, -3.95F, 0.0F, 0.0F, -3.1416F));

        PartDefinition cube_r12 = upper_head.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(50, 104).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4304F, -3.0527F, -3.95F, 0.0F, 0.0F, -3.1416F));

        PartDefinition head4_r2 = upper_head.addOrReplaceChild("head4_r2", CubeListBuilder.create().texOffs(20, 119).addBox(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -6.1F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition lower_head = head2.addOrReplaceChild("lower_head", CubeListBuilder.create().texOffs(66, 83).addBox(-4.0F, -1.9F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(64, 114).addBox(-3.4F, -1.55F, -3.8F, 7.0F, 1.35F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(50, 104).addBox(1.9696F, -1.7527F, -3.95F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(50, 104).addBox(-2.9304F, -1.7527F, -3.95F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(46, 107).addBox(-2.5F, -1.75F, -3.85F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(0, 34).addBox(-4.5F, -6.0F, -2.0F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.75F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(0, 44).addBox(0.75F, -7.75F, -3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(0, 98).addBox(0.75F, -7.75F, -3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(64, 55).addBox(0.75F, -5.75F, -3.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(16, 45).addBox(0.5F, -8.75F, -3.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.2F))
                .texOffs(38, 79).addBox(2.75F, -0.25F, 1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.75F, 7.0F, 1.0F));

        PartDefinition cube_r13 = upper_left_arm.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(38, 79).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.75F, 0.25F, 2.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(64, 45).addBox(-2.75F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.21F))
                .texOffs(32, 64).addBox(-2.75F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, 9.25F, 0.0F));

        PartDefinition lower_left_arm_r1 = lower_left_arm.addOrReplaceChild("lower_left_arm_r1", CubeListBuilder.create().texOffs(42, 13).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 3.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.75F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-4.75F, -7.75F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(0, 98).mirror().addBox(-4.75F, -7.75F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(64, 55).mirror().addBox(-4.75F, -5.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false)
                .texOffs(16, 45).mirror().addBox(-5.5F, -8.75F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.2F)).mirror(false)
                .texOffs(38, 79).mirror().addBox(-2.75F, -0.25F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.75F, 7.0F, 0.0F));

        PartDefinition cube_r14 = upper_right_arm.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(38, 79).mirror().addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.75F, 0.25F, 3.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(64, 45).mirror().addBox(-1.25F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.21F)).mirror(false)
                .texOffs(32, 64).mirror().addBox(-1.25F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.75F, 9.25F, 0.0F));

        PartDefinition lower_right_arm_r1 = lower_right_arm.addOrReplaceChild("lower_right_arm_r1", CubeListBuilder.create().texOffs(42, 13).mirror().addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 3.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(26, 35).addBox(-4.5F, -6.0F, -2.0F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(52, 0).addBox(-2.0F, -1.0F, -1.9999F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(37, 117).mirror().addBox(-2.0F, -1.0F, -1.9998F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_right_leg_r1 = upper_right_leg.addOrReplaceChild("upper_right_leg_r1", CubeListBuilder.create().texOffs(42, 13).addBox(-3.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 7.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(16, 53).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(52, 35).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.201F))
                .texOffs(16, 76).mirror().addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false)
                .texOffs(46, 81).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(32, 53).addBox(-2.0F, -1.0F, -1.9998F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.201F))
                .texOffs(37, 117).addBox(-2.0F, -1.0F, -1.9998F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_left_leg_r1 = upper_left_leg.addOrReplaceChild("upper_left_leg_r1", CubeListBuilder.create().texOffs(36, 13).addBox(0.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 7.0F, -2.5F, -0.0873F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(48, 53).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(46, 81).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(16, 64).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(16, 76).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition voidBone = stand2.addOrReplaceChild("voidBone", CubeListBuilder.create(), PartPose.offset(0.0F, -20.0F, -1.0F));

        PartDefinition void1 = voidBone.addOrReplaceChild("void1", CubeListBuilder.create().texOffs(120, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition void2 = voidBone.addOrReplaceChild("void2", CubeListBuilder.create().texOffs(120, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    StandPowers Power = new PowersCream(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
        this.animate(pEntity.blockGrabAnimation, StandAnimations.GRAB_BLOCK, pAgeInTicks, 1f);
        this.animate(pEntity.blockThrowAnimation, StandAnimations.THROW_BLOCK, pAgeInTicks, 1.7f);
        this.animate(pEntity.itemGrabAnimation, StandAnimations.GRAB_ITEM, pAgeInTicks, 1f);
        this.animate(pEntity.itemThrowAnimation, StandAnimations.THROW_ITEM, pAgeInTicks, 1.25f);
        this.animate(pEntity.blockRetractAnimation, StandAnimations.RETRACT_BLOCK, pAgeInTicks, 1.25f);
        this.animate(pEntity.itemRetractAnimation, StandAnimations.RETRACT_ITEM, pAgeInTicks, 1.25f);
        this.animate(pEntity.entityGrabAnimation, StandAnimations.GRAB_BLOCK, pAgeInTicks, 3f);
        this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1f);
        this.animate(pEntity.hideLeg, StandAnimations.HIDE_LEG, pAgeInTicks, 1f);
        this.animate(pEntity.phaseGrab, StandAnimations.PHASE_GRAB, pAgeInTicks, 0.6f);
        this.animate(pEntity.creamVoidEat, CreamAnimations.CREAM_EAT_VOID, pAgeInTicks, 1f);
        this.animate(pEntity.creamUnEat, CreamAnimations.CREAM_UN_EAT, pAgeInTicks, 1f);
        this.animate(pEntity.creamVoidAttackBall, CreamAnimations.CREAM_VOID_ATTACK_BALL, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}