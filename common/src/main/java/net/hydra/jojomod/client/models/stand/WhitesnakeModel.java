package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.KingCrimsonAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.client.models.stand.animations.StarPlatinumAnimations;
import net.hydra.jojomod.client.models.stand.animations.WhitesnakeAnimations;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import org.joml.Vector3f;

public class WhitesnakeModel extends StandModel<WhitesnakeEntity> {
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final StandPowers power = new PowersWhitesnake(null);
    private final Vector3f animationVectorCache = new Vector3f();
    private float controlHeadYaw;
    private float controlHeadPitch;

    public WhitesnakeModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        ModelPart stand2 = root.getChild("stand").getChild("stand2");
        ModelPart body2 = stand2.getChild("body").getChild("body2");
        ModelPart upperChest = body2.getChild("torso").getChild("upper_chest");
        ModelPart legs = body2.getChild("legs");
        this.rightArm = upperChest.getChild("right_arm");
        this.leftArm = upperChest.getChild("left_arm");
        this.rightHand = rightArm.getChild("lower_right_arm");
        this.leftHand = leftArm.getChild("lower_left_arm");
        this.rightLeg = legs.getChild("right_leg");
        this.leftLeg = legs.getChild("left_leg");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.005F))
                .texOffs(28, 31).addBox(-4.0F, -0.35F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.125F))
                .texOffs(0, 16).addBox(-4.0F, -10.1F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
                .texOffs(0, 32).addBox(-3.5F, -6.6F, -3.825F, 7.0F, 6.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 20).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create()
                .texOffs(32, 50).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 20).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create()
                .texOffs(48, 50).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(64, 59).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create()
                .texOffs(52, 40).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(60, 29).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create()
                .texOffs(0, 56).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(0, 66).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create()
                .texOffs(32, 10).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 40).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.009F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create()
                .texOffs(48, 60).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.05F))
                .texOffs(16, 70).mirror().addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create()
                .texOffs(0, 45).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(64, 68).mirror().addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create()
                .texOffs(32, 60).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.051F))
                .texOffs(16, 70).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.251F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create()
                .texOffs(0, 45).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(64, 68).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -12.0F));

        PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-11.5F, -9.0F, 0.0F));

        PartDefinition rotated_cube_1 = RightArmBAM.addOrReplaceChild("rotated_cube_1", CubeListBuilder.create()
                .texOffs(56, 0).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition rotated_cube_2 = RightArmBAM.addOrReplaceChild("rotated_cube_2", CubeListBuilder.create()
                .texOffs(16, 61).addBox(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition rotated_cube_3 = RightArmBAM.addOrReplaceChild("rotated_cube_3", CubeListBuilder.create()
                .texOffs(32, 69).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

        PartDefinition rotated_cube_4 = RightArmBAM2.addOrReplaceChild("rotated_cube_4", CubeListBuilder.create()
                .texOffs(56, 0).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition rotated_cube_5 = RightArmBAM2.addOrReplaceChild("rotated_cube_5", CubeListBuilder.create()
                .texOffs(16, 61).addBox(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition rotated_cube_6 = RightArmBAM2.addOrReplaceChild("rotated_cube_6", CubeListBuilder.create()
                .texOffs(32, 69).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

        PartDefinition rotated_cube_7 = RightArmBAM3.addOrReplaceChild("rotated_cube_7", CubeListBuilder.create()
                .texOffs(56, 0).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition rotated_cube_8 = RightArmBAM3.addOrReplaceChild("rotated_cube_8", CubeListBuilder.create()
                .texOffs(16, 61).addBox(0.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition rotated_cube_9 = RightArmBAM3.addOrReplaceChild("rotated_cube_9", CubeListBuilder.create()
                .texOffs(32, 69).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

        PartDefinition rotated_cube_10 = LeftArmBAM4.addOrReplaceChild("rotated_cube_10", CubeListBuilder.create()
                .texOffs(56, 0).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition rotated_cube_11 = LeftArmBAM4.addOrReplaceChild("rotated_cube_11", CubeListBuilder.create()
                .texOffs(16, 61).mirror().addBox(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition rotated_cube_12 = LeftArmBAM4.addOrReplaceChild("rotated_cube_12", CubeListBuilder.create()
                .texOffs(32, 69).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

        PartDefinition rotated_cube_13 = LeftArmBAM3.addOrReplaceChild("rotated_cube_13", CubeListBuilder.create()
                .texOffs(56, 0).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition rotated_cube_14 = LeftArmBAM3.addOrReplaceChild("rotated_cube_14", CubeListBuilder.create()
                .texOffs(16, 61).mirror().addBox(-4.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition rotated_cube_15 = LeftArmBAM3.addOrReplaceChild("rotated_cube_15", CubeListBuilder.create()
                .texOffs(32, 69).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(11.5F, -9.0F, 0.0F));

        PartDefinition rotated_cube_16 = LeftArmBAM.addOrReplaceChild("rotated_cube_16", CubeListBuilder.create()
                .texOffs(16, 61).mirror().addBox(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition rotated_cube_17 = LeftArmBAM.addOrReplaceChild("rotated_cube_17", CubeListBuilder.create()
                .texOffs(56, 0).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition rotated_cube_18 = LeftArmBAM.addOrReplaceChild("rotated_cube_18", CubeListBuilder.create()
                .texOffs(32, 69).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void defaultAnimations(WhitesnakeEntity entity, float animationProgress, float windupLength) {
        if (!entity.isRemoteControlled()) {
            this.animate(entity.idleAnimationState, StandAnimations.STAND_IDLE_FLOAT, animationProgress, 1.0F);
            this.animate(entity.idleAnimationState2, StandAnimations.IDLE_2, animationProgress, 1.0F);
            this.animate(entity.idleAnimationState3, StandAnimations.FLOATY_IDLE, animationProgress, 1.0F);
            this.animate(entity.idleAnimationState4, StandAnimations.STAR_PLATINUM_IDLE, animationProgress, 1.0F);
        }
        float partial = 1.4F;
        float full = 1.16666F;
        if (entity.getMeltLevel() > 0) {
            partial -= Math.min(0.5F, 0.9F * entity.getMeltLevel());
            full -= Math.min(0.5F, 0.07F * entity.getMeltLevel());
        }
        this.animate(entity.punchState1, StandAnimations.COMBO1, animationProgress, partial);
        this.animate(entity.punchState2, StandAnimations.COMBO2, animationProgress, full);
        this.animate(entity.punchState3, StandAnimations.COMBO3, animationProgress, full);
        this.animate(entity.blockAnimationState, StandAnimations.BLOCK, animationProgress, 1.0F);
        this.animate(entity.barrageChargeAnimationState, StandAnimations.BARRAGECHARGE, animationProgress, windupLength);
        this.animate(entity.barrageAnimationState, StandAnimations.BARRAGE, animationProgress, 1.0F);
        this.animate(entity.miningBarrageAnimationState, StandAnimations.MINING_BARRAGE, animationProgress, 1.65F);
        this.animate(entity.barrageEndAnimationState, StandAnimations.COMBO3, animationProgress, 2.2F);
        this.animate(entity.barrageHurtAnimationState, StandAnimations.BARRAGEDAMAGE, animationProgress, 2.5F);
        this.animate(entity.brokenBlockAnimationState, StandAnimations.BLOCKBREAK, animationProgress, 1.8F);
        this.animate(entity.standLeapAnimationState, StandAnimations.STAND_LEAP, animationProgress, 1.0F);
        this.animate(entity.standLeapEndAnimationState, StandAnimations.STAND_LEAP_END, animationProgress, 3.0F);
        this.animate(entity.discStealWindup, WhitesnakeAnimations.DISC_STEAL_WINDUP, animationProgress, 1.0F);
        this.animate(entity.discStealRelease, WhitesnakeAnimations.DISC_STEAL_RELEASE, animationProgress, 1.0F);
        if (entity.isMeltingModeActive()) {
            float partialTick = Mth.clamp(animationProgress - entity.tickCount, 0.0F, 1.0F);
            float acidTossBlend = entity.getMeltingAcidTossBlend(partialTick);
            float swimBlend = entity.getMeltingSwimBlend(partialTick) * (1.0F - acidTossBlend);
            float idleBlend = 1.0F - acidTossBlend - swimBlend;
            animateWeighted(entity.meltingIdle, WhitesnakeAnimations.MELTING_IDLE,
                    animationProgress, idleBlend);
            animateWeighted(entity.meltingSwim, WhitesnakeAnimations.MELTING_SWIM,
                    animationProgress, swimBlend);
            animateWeighted(entity.acidToss, WhitesnakeAnimations.MELTING_ACID_TOSS,
                    animationProgress, acidTossBlend);
        } else {
            this.animate(entity.acidToss, WhitesnakeAnimations.ACID_TOSS, animationProgress, 1.0F);
        }
    }

    private void animateWeighted(AnimationState state, AnimationDefinition animation,
                                 float animationProgress, float weight) {
        if (weight <= 0.0F) return;
        state.updateTime(animationProgress, 1.0F);
        state.ifStarted(animationState -> KeyframeAnimations.animate(this, animation,
                animationState.getAccumulatedTime(), weight, animationVectorCache));
    }

    @Override
    public void setupAnim(WhitesnakeEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        Minecraft minecraft = Minecraft.getInstance();
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        controlHeadYaw = netHeadYaw;
        controlHeadPitch = headPitch;
        defaultModifiers(entity);
        float windupLength = 1.0F / ((float) power.getBarrageWindup() / 20.0F);
        defaultAnimations(entity, ageInTicks, windupLength);
        this.animate(entity.hideFists, StandAnimations.HIDE_FISTS, ageInTicks, 1.0F);
        this.head.visible = !(!entity.isAutoModeActive() && entity.isRemoteControlled()
                && entity.getUser() == minecraft.player
                && minecraft.options.getCameraType().isFirstPerson());
        this.animate(entity.finalChopWindup, KingCrimsonAnimations.Chop_Start, ageInTicks, 0.8F);
        this.animate(entity.finalChop, StarPlatinumAnimations.FINAL_PUNCH, ageInTicks, 1.4F);
        this.animate(entity.finalChopHalf, KingCrimsonAnimations.Chop_Attack, ageInTicks, 1.0F);
        this.animate(entity.finalChopCharged, KingCrimsonAnimations.Chop_Charged, ageInTicks, 1.0F);
        this.animate(entity.itemGrab, StandAnimations.GRAB_ITEM, ageInTicks, 1.0F);
        this.animate(entity.itemThrow, StandAnimations.THROW_ITEM, ageInTicks, 1.25F);
        this.animate(entity.itemRetract, StandAnimations.RETRACT_ITEM, ageInTicks, 1.25F);
        this.animate(entity.impale, StandAnimations.IMPALE, ageInTicks, 1.04F);
        this.animate(entity.phaseGrab, StandAnimations.PHASE_GRAB, ageInTicks, 0.6F);
        if (entity.isRemoteControlled() && !entity.isMeltingModeActive()
                && entity.getAnimation() == 0 && limbSwingAmount > 0.01F) {
            float pace = entity.isSprinting() ? 0.9F : 0.6662F;
            float strength = Mth.clamp(limbSwingAmount, 0.0F, 1.0F) * (entity.isSprinting() ? 1.15F : 0.85F);
            float right = Mth.cos(limbSwing * pace) * strength;
            float left = Mth.cos(limbSwing * pace + Mth.PI) * strength;
            rightLeg.xRot += right;
            leftLeg.xRot += left;
            rightArm.xRot += left * 0.7F;
            leftArm.xRot += right * 0.7F;
        }
    }

    @Override
    public void rotateStand(WhitesnakeEntity entity, ModelPart stand, float tickDelta) {
        if (!entity.isRemoteControlled()) {
            super.rotateStand(entity, stand, tickDelta);
            return;
        }
        entity.setStandRotationX(0.0F);
        entity.setStandRotationY(0.0F);
        entity.setStandRotationZ(0.0F);
        setStandRotations(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void rotateHead(WhitesnakeEntity entity, ModelPart head, float tickDelta) {
        if (!entity.isRemoteControlled()) {
            super.rotateHead(entity, head, tickDelta);
            return;
        }
        float pitch = Mth.clamp(controlHeadPitch, -90.0F, 90.0F) * Mth.DEG_TO_RAD;
        float yaw = Mth.clamp(Mth.wrapDegrees(controlHeadYaw), -85.0F, 85.0F) * Mth.DEG_TO_RAD;
        setHeadRotations(pitch, yaw);
    }

    @Override
    public void rotateBody(WhitesnakeEntity entity, ModelPart body, float tickDelta) {
        if (!entity.isRemoteControlled()) {
            super.rotateBody(entity, body, tickDelta);
            return;
        }
        entity.setBodyRotationX(0.0F);
        entity.setBodyRotationY(0.0F);
        setBodyRotations(0.0F, 0.0F);
    }

}
