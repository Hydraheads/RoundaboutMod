package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.DiverDownAnimations;
import net.hydra.jojomod.client.models.stand.animations.GoldExperienceAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.client.models.stand.renderers.GoldExperienceBaseRenderer;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GoldExperienceModel <T extends GoldExperienceEntity> extends StandModel<T>{
    private GoldExperienceEntity currentEntity;
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
    private final ModelPart stand2;
    private final ModelPart head2;
    private final ModelPart bone;
    private final ModelPart body2;
    private final ModelPart torso;
    private final ModelPart upper_chest;
    private final ModelPart upper_chest_only;
    private final ModelPart right_arm;
    private final ModelPart upper_right_arm;
    private final ModelPart lower_right_arm;
    private final ModelPart vitalpunchvfx;
    private final ModelPart left_arm;
    private final ModelPart upper_left_arm;
    private final ModelPart lower_left_arm;
    private final ModelPart lower_chest;
    private final ModelPart lower_torso;
    private final ModelPart cloth;
    private final ModelPart legs;
    private final ModelPart right_leg;
    private final ModelPart upper_right_leg;
    private final ModelPart lower_right_leg;
    private final ModelPart left_leg;
    private final ModelPart upper_left_leg;
    private final ModelPart lower_left_leg;
    private final ModelPart kick_barrage;
    private final ModelPart One;
    private final ModelPart Two;
    private final ModelPart Three;
    private final ModelPart Four;
    private final ModelPart BAM;
    private final ModelPart RightArmBAM;
    private final ModelPart RightArmBAM2;
    private final ModelPart RightArmBAM3;
    private final ModelPart LeftArmBAM;
    private final ModelPart LeftArmBAM4;
    private final ModelPart LeftArmBAM3;

    public GoldExperienceModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.stand2 = this.stand.getChild("stand2");
        this.head = this.stand2.getChild("head");
        this.head2 = this.head.getChild("head2");
        this.bone = this.head2.getChild("bone");
        this.body = this.stand2.getChild("body");
        this.body2 = this.body.getChild("body2");
        this.torso = this.body2.getChild("torso");
        this.upper_chest = this.torso.getChild("upper_chest");
        this.upper_chest_only = this.upper_chest.getChild("upper_chest_only");
        this.right_arm = this.upper_chest.getChild("right_arm");
        this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
        this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
        this.vitalpunchvfx = this.lower_right_arm.getChild("vitalpunchvfx");
        this.left_arm = this.upper_chest.getChild("left_arm");
        this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
        this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
        this.lower_chest = this.torso.getChild("lower_chest");
        this.lower_torso = this.lower_chest.getChild("lower_torso");
        this.cloth = this.lower_torso.getChild("cloth");
        this.legs = this.body2.getChild("legs");
        this.right_leg = this.legs.getChild("right_leg");
        this.upper_right_leg = this.right_leg.getChild("upper_right_leg");
        this.lower_right_leg = this.right_leg.getChild("lower_right_leg");
        this.left_leg = this.legs.getChild("left_leg");
        this.upper_left_leg = this.left_leg.getChild("upper_left_leg");
        this.lower_left_leg = this.left_leg.getChild("lower_left_leg");
        this.kick_barrage = this.stand2.getChild("kick_barrage");
        this.One = this.kick_barrage.getChild("One");
        this.Two = this.kick_barrage.getChild("Two");
        this.Three = this.kick_barrage.getChild("Three");
        this.Four = this.kick_barrage.getChild("Four");
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

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 26).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 86).addBox(-1.0F, -0.6F, -4.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone = head2.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(32, 26).addBox(-2.0F, -27.975F, -4.0F, 5.0F, 3.0F, 11.0F, new CubeDeformation(0.145F))
                .texOffs(36, 20).addBox(-4.0F, -25.0F, 0.5F, 9.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -29.0F, -3.0F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.15F))
                .texOffs(0, 13).addBox(-4.0F, -29.0F, -3.0F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.075F))
                .texOffs(36, 0).addBox(-3.0F, -30.25F, -3.0F, 7.0F, 1.0F, 9.0F, new CubeDeformation(0.15F))
                .texOffs(36, 10).addBox(-3.0F, -30.25F, -3.0F, 7.0F, 1.0F, 9.0F, new CubeDeformation(0.075F)), PartPose.offset(-0.5F, 21.15F, -1.5F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(32, 40).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 51).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.15F))
                .texOffs(16, 52).addBox(2.0F, -2.0F, -2.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.005F))
                .texOffs(24, 47).addBox(-4.0F, -2.0F, -2.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(48, 55).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(64, 73).addBox(-5.0F, -0.85F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 54).addBox(-5.0F, -0.85F, 2.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(64, 30).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(60, 20).addBox(-1.0F, -1.25F, 2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(56, 40).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(68, 9).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition vitalpunchvfx = lower_right_arm.addOrReplaceChild("vitalpunchvfx", CubeListBuilder.create().texOffs(100, 8).addBox(-8.0F, -17.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.32F))
                .texOffs(88, 39).addBox(-8.1F, -17.9F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(6.1F, 17.65F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(16, 60).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(64, 55).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(16, 54).addBox(5.0F, -0.75F, 2.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 74).addBox(4.0F, -0.75F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(60, 20).mirror().addBox(-1.0F, -1.25F, 2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 60).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 70).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(0, 42).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 50).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(4, 77).addBox(-1.0F, -0.25F, -2.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition cloth = lower_torso.addOrReplaceChild("cloth", CubeListBuilder.create().texOffs(0, 92).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -0.25F, 2.25F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(64, 64).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F))
                .texOffs(24, 42).addBox(-3.0F, 1.0F, -0.9999F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(74, 73).addBox(-3.0F, 0.75F, -2.9999F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 20).addBox(-1.0F, -1.0F, -2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 70).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(64, 64).mirror().addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)).mirror(false)
                .texOffs(24, 42).mirror().addBox(2.0F, 1.0F, -0.9999F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(74, 73).mirror().addBox(0.0F, 0.75F, -2.9999F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 20).mirror().addBox(-1.0F, -1.0F, -2.25F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 70).mirror().addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition kick_barrage = stand2.addOrReplaceChild("kick_barrage", CubeListBuilder.create(), PartPose.offset(-11.0F, -12.0F, -2.0F));

        PartDefinition One = kick_barrage.addOrReplaceChild("One", CubeListBuilder.create().texOffs(80, 63).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false)
                .texOffs(96, 64).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition base_r1 = One.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(80, 55).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

        PartDefinition Two = kick_barrage.addOrReplaceChild("Two", CubeListBuilder.create().texOffs(80, 63).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false)
                .texOffs(96, 64).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition base_r2 = Two.addOrReplaceChild("base_r2", CubeListBuilder.create().texOffs(80, 55).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

        PartDefinition Three = kick_barrage.addOrReplaceChild("Three", CubeListBuilder.create().texOffs(80, 63).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false)
                .texOffs(96, 64).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition base_r3 = Three.addOrReplaceChild("base_r3", CubeListBuilder.create().texOffs(80, 55).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

        PartDefinition Four = kick_barrage.addOrReplaceChild("Four", CubeListBuilder.create().texOffs(80, 63).mirror().addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false)
                .texOffs(96, 64).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition base_r4 = Four.addOrReplaceChild("base_r4", CubeListBuilder.create().texOffs(80, 55).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

        PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

        PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

        PartDefinition cube_r1 = RightArmBAM.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(72, 39).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 63).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition Right_Arm_r1 = RightArmBAM.addOrReplaceChild("Right_Arm_r1", CubeListBuilder.create().texOffs(48, 65).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-15.0F, -2.0F, 1.0F));

        PartDefinition cube_r2 = RightArmBAM2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(72, 39).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 63).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition Right_Arm_r2 = RightArmBAM2.addOrReplaceChild("Right_Arm_r2", CubeListBuilder.create().texOffs(48, 65).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.0F, 4.0F, 1.0F));

        PartDefinition cube_r3 = RightArmBAM3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(72, 39).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 63).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition Right_Arm_r3 = RightArmBAM3.addOrReplaceChild("Right_Arm_r3", CubeListBuilder.create().texOffs(48, 65).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -8.0F, 1.0F));

        PartDefinition cube_r4 = LeftArmBAM.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(72, 39).mirror().addBox(6.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 63).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition Left_Arm_r1 = LeftArmBAM.addOrReplaceChild("Left_Arm_r1", CubeListBuilder.create().texOffs(48, 65).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(15.0F, -2.0F, 1.0F));

        PartDefinition cube_r5 = LeftArmBAM4.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(72, 39).mirror().addBox(6.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 63).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition Left_Arm_r2 = LeftArmBAM4.addOrReplaceChild("Left_Arm_r2", CubeListBuilder.create().texOffs(48, 65).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.0F, 4.0F, 1.0F));

        PartDefinition cube_r6 = LeftArmBAM3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(72, 39).mirror().addBox(6.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 63).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition Left_Arm_r3 = LeftArmBAM3.addOrReplaceChild("Left_Arm_r3", CubeListBuilder.create().texOffs(48, 65).mirror().addBox(6.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    StandPowers Power = new PowersGoldExperience(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.currentEntity = pEntity;
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
        this.animate(pEntity.lifePunch, GoldExperienceAnimations.LIFE_PUNCH, pAgeInTicks, 1f);
        this.animate(pEntity.hideLeg, StandAnimations.HIDE_LEG, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage, StandAnimations.KICK_BARRAGE, pAgeInTicks, 1.25f);
        this.animate(pEntity.kick_barrage_windup, StandAnimations.KICK_BARRAGE_CHARGE, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage_end, StandAnimations.KICK_BARRAGE_END, pAgeInTicks, 1f);
        this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
    private final org.joml.Vector3f animationVectorCache = new org.joml.Vector3f();

    private void animateWeighted(AnimationState state, AnimationDefinition animation,
                                 float animationProgress, float weight) {
        if (weight <= 0.0F) return;
        state.updateTime(animationProgress, 1.0F);
        state.ifStarted(animationState -> KeyframeAnimations.animate(this, animation,
                animationState.getAccumulatedTime(), weight, animationVectorCache));
    }

    // need to override this for the super cool glowy life punch arm
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if (this.currentEntity != null && this.currentEntity.getAnimation() == GoldExperienceEntity.LIFE_PUNCH) {
            int fullBright = 15728880;
            net.minecraft.client.renderer.MultiBufferSource.BufferSource globalBufferSource = net.minecraft.client.Minecraft.getInstance().renderBuffers().bufferSource();
            ResourceLocation texture = GoldExperienceBaseRenderer.getSkin(this.currentEntity.getSkin());
            RenderType additiveGlow = RenderType.beaconBeam(texture, true);
            VertexConsumer glowBuffer = globalBufferSource.getBuffer(additiveGlow);

            poseStack.pushPose();

            // we is currently making a glowy fist thing for this move specifically.
            // eventually, i'll have to update the tree to account for that.
            this.stand.translateAndRotate(poseStack);
            this.stand2.translateAndRotate(poseStack);
            this.body.translateAndRotate(poseStack);
            this.body2.translateAndRotate(poseStack);
            this.torso.translateAndRotate(poseStack);
            this.upper_chest.translateAndRotate(poseStack);
            this.right_arm.translateAndRotate(poseStack);

            this.lower_right_arm.render(poseStack, glowBuffer, fullBright, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

            poseStack.popPose();
        }
    }
}
