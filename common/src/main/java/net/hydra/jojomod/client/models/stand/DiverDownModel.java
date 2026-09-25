package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.DiverDownAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class DiverDownModel<T extends DiverDownEntity> extends StandModel<T>{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "diver_down"), "main");
    private final ModelPart stand2;
    private final ModelPart head2;
    private final ModelPart divingequip_left;
    private final ModelPart divingequip_right;
    private final ModelPart eyebrows;
    private final ModelPart body2;
    private final ModelPart torso;
    private final ModelPart upper_chest;
    private final ModelPart upper_chest_only;
    private final ModelPart tanks;
    private final ModelPart right_arm;
    private final ModelPart upper_right_arm;
    private final ModelPart lower_right_arm;
    private final ModelPart left_arm;
    private final ModelPart upper_left_arm;
    private final ModelPart lower_left_arm;
    private final ModelPart lower_chest;
    private final ModelPart lower_torso;
    private final ModelPart legs;
    private final ModelPart left_leg;
    private final ModelPart upper_left_leg;
    private final ModelPart lower_left_leg;
    private final ModelPart right_leg;
    private final ModelPart upper_right_leg;
    private final ModelPart lower_right_leg;
    private final ModelPart BAM;
    private final ModelPart RightArmBAM;
    private final ModelPart RightArmBAM2;
    private final ModelPart RightArmBAM3;
    private final ModelPart LeftArmBAM;
    private final ModelPart LeftArmBAM4;
    private final ModelPart LeftArmBAM3;

    public DiverDownModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.stand2 = this.stand.getChild("stand2");
        this.head = this.stand2.getChild("head");
        this.head2 = this.head.getChild("head2");
        this.divingequip_left = this.head2.getChild("divingequip_left");
        this.divingequip_right = this.head2.getChild("divingequip_right");
        this.eyebrows = this.head2.getChild("eyebrows");
        this.body = this.stand2.getChild("body");
        this.body2 = this.body.getChild("body2");
        this.torso = this.body2.getChild("torso");
        this.upper_chest = this.torso.getChild("upper_chest");
        this.upper_chest_only = this.upper_chest.getChild("upper_chest_only");
        this.tanks = this.upper_chest_only.getChild("tanks");
        this.right_arm = this.upper_chest.getChild("right_arm");
        this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
        this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
        this.left_arm = this.upper_chest.getChild("left_arm");
        this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
        this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
        this.lower_chest = this.torso.getChild("lower_chest");
        this.lower_torso = this.lower_chest.getChild("lower_torso");
        this.legs = this.body2.getChild("legs");
        this.left_leg = this.legs.getChild("left_leg");
        this.upper_left_leg = this.left_leg.getChild("upper_left_leg");
        this.lower_left_leg = this.left_leg.getChild("lower_left_leg");
        this.right_leg = this.legs.getChild("right_leg");
        this.upper_right_leg = this.right_leg.getChild("upper_right_leg");
        this.lower_right_leg = this.right_leg.getChild("lower_right_leg");
        this.BAM = this.stand2.getChild("BAM");
        this.RightArmBAM = this.BAM.getChild("RightArmBAM");
        this.RightArmBAM2 = this.BAM.getChild("RightArmBAM2");
        this.RightArmBAM3 = this.BAM.getChild("RightArmBAM3");
        this.LeftArmBAM = this.BAM.getChild("LeftArmBAM");
        this.LeftArmBAM4 = this.BAM.getChild("LeftArmBAM4");
        this.LeftArmBAM3 = this.BAM.getChild("LeftArmBAM3");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, 53).addBox(4.0F, -6.85F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(56, 10).addBox(4.0F, -10.85F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(56, 0).addBox(-2.0F, 0.15F, -4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition divingequip_left = head2.addOrReplaceChild("divingequip_left", CubeListBuilder.create().texOffs(32, 20).addBox(2.0F, -1.5F, -0.5F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(56, 15).addBox(2.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 36).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 6).addBox(0.0F, -1.5F, 7.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.65F, -3.5F));

        PartDefinition divingequip_right = head2.addOrReplaceChild("divingequip_right", CubeListBuilder.create().texOffs(50, 36).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 8).addBox(-3.0F, -1.5F, 7.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 29).addBox(-3.0F, -1.5F, -0.5F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(48, 56).addBox(-3.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.65F, -3.5F));

        PartDefinition eyebrows = head2.addOrReplaceChild("eyebrows", CubeListBuilder.create().texOffs(8, 11).addBox(-4.0F, -29.0F, -3.2F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.15F, -1.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tanks = upper_chest_only.addOrReplaceChild("tanks", CubeListBuilder.create().texOffs(12, 53).addBox(-4.0F, -25.0F, -2.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).addBox(1.0F, -25.0F, -2.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(56, 18).addBox(2.0F, -26.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 56).addBox(-3.0F, -26.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 4.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(16, 33).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(32, 38).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 38).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(0.0F, -0.85F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false)
                .texOffs(16, 33).mirror().addBox(0.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(32, 38).mirror().addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 38).mirror().addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)).mirror(false), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(32, 10).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(48, 47).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(50, 20).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F))
                .texOffs(56, 2).mirror().addBox(-1.75F, -2.0F, -2.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(48, 47).mirror().addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(0, 33).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 2).mirror().addBox(-2.25F, -2.0F, -2.5F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(50, 20).mirror().addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

        PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

        PartDefinition cube_r1 = RightArmBAM.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(50, 29).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 43).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition Right_Arm_r1 = RightArmBAM.addOrReplaceChild("Right_Arm_r1", CubeListBuilder.create().texOffs(32, 48).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-15.0F, -2.0F, 1.0F));

        PartDefinition cube_r2 = RightArmBAM2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(50, 29).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 43).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition Right_Arm_r2 = RightArmBAM2.addOrReplaceChild("Right_Arm_r2", CubeListBuilder.create().texOffs(32, 48).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.0F, 4.0F, 1.0F));

        PartDefinition cube_r3 = RightArmBAM3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(50, 29).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 43).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition Right_Arm_r3 = RightArmBAM3.addOrReplaceChild("Right_Arm_r3", CubeListBuilder.create().texOffs(32, 48).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -8.0F, 1.0F));

        PartDefinition cube_r4 = LeftArmBAM.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(50, 29).mirror().addBox(6.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 43).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition Left_Arm_r1 = LeftArmBAM.addOrReplaceChild("Left_Arm_r1", CubeListBuilder.create().texOffs(32, 48).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(15.0F, -2.0F, 1.0F));

        PartDefinition cube_r5 = LeftArmBAM4.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(50, 29).mirror().addBox(6.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 43).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition Left_Arm_r2 = LeftArmBAM4.addOrReplaceChild("Left_Arm_r2", CubeListBuilder.create().texOffs(32, 48).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.0F, 4.0F, 1.0F));

        PartDefinition cube_r6 = LeftArmBAM3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(50, 29).mirror().addBox(6.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 43).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition Left_Arm_r3 = LeftArmBAM3.addOrReplaceChild("Left_Arm_r3", CubeListBuilder.create().texOffs(32, 48).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    StandPowers Power = new PowersDiverDown(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));

        this.animate(pEntity.diverzip, DiverDownAnimations.DIVER_ZIP, pAgeInTicks, 1F);
        this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1F);
        this.animate(pEntity.miningBarrageAnimationState, DiverDownAnimations.Barrage, pAgeInTicks, 1f);
        this.animate(pEntity.barrageHurtAnimationState, DiverDownAnimations.BarrageDamage, pAgeInTicks, 2.5f);
        this.animate(pEntity.brokenBlockAnimationState, StandAnimations.BLOCKBREAK, pAgeInTicks, 1.8f);
        this.animate(pEntity.idleAnimationState, DiverDownAnimations.Idle, pAgeInTicks, 1f);
        this.animate(pEntity.idleAnimationState2, StandAnimations.FLOATY_IDLE, pAgeInTicks, 1f);
        this.animate(pEntity.blockAnimationState, DiverDownAnimations.BLOCK, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage_windup, StandAnimations.KICK_BARRAGE_CHARGE, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage, StandAnimations.KICK_BARRAGE, pAgeInTicks, 1.25f);
        this.animate(pEntity.kick_barrage_end, StandAnimations.KICK_BARRAGE_END, pAgeInTicks, 1f);
        this.animate(pEntity.hideLegEntirely, StandAnimations.HIDE_LEGS_ENTIRELY, pAgeInTicks, 1.25f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
    }