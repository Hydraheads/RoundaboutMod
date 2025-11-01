package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.client.models.stand.animations.TheGratefulDeadAnimations;
import net.hydra.jojomod.entity.stand.TheGratefulDeadEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersTheGratefulDead;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TheGratefulDeadModel<T extends TheGratefulDeadEntity> extends StandModel<T>{
    public TheGratefulDeadModel(ModelPart root){
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
        this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
    }

    public static LayerDefinition getTexturedModelData(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 13.5F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -18.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(1, 0).addBox(-3.5F, -8.85F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(46, 26).addBox(-3.5F, -8.85F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.21F))
                .texOffs(0, 0).addBox(-1.5F, -1.85F, -4.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(-0.5F, -2.85F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(2, 54).addBox(-4.5F, -0.0228F, -2.5229F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(4, 65).addBox(-4.5F, -0.0228F, -2.5229F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.5F, 1.0F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(29, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(61, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(29, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(61, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 7.0F, 0.0F));

        PartDefinition right_finger1 = lower_right_arm.addOrReplaceChild("right_finger1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 10.0F, -2.0F));

        PartDefinition right_finger2 = lower_right_arm.addOrReplaceChild("right_finger2", CubeListBuilder.create(), PartPose.offset(2.0F, 10.0F, 0.0F));

        PartDefinition cube_r1 = right_finger2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -0.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition right_finger3 = lower_right_arm.addOrReplaceChild("right_finger3", CubeListBuilder.create(), PartPose.offset(-2.0F, 10.0F, 0.0F));

        PartDefinition cube_r2 = right_finger3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -0.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.5F, 1.0F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(77, 0).addBox(0.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(45, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(77, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 7.0F, 0.0F));

        PartDefinition left_finger1 = lower_left_arm.addOrReplaceChild("left_finger1", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, -2.0F));

        PartDefinition left_finger2 = lower_left_arm.addOrReplaceChild("left_finger2", CubeListBuilder.create(), PartPose.offset(-2.0F, 10.0F, 0.0F));

        PartDefinition cube_r3 = left_finger2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -0.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition left_finger3 = lower_left_arm.addOrReplaceChild("left_finger3", CubeListBuilder.create(), PartPose.offset(2.0F, 10.0F, 0.0F));

        PartDefinition cube_r4 = left_finger3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -0.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create().texOffs(34, 57).addBox(-4.0F, 0.0F, -1.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 48).addBox(-4.0F, 0.0F, -1.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.21F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition cube_r5 = lower_chest.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(14, 16).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition cube_r6 = lower_chest.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(1.0F, -18.5F, -5.0F));

        PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-13.5F, -7.25F, 0.0F));

        PartDefinition cube_r7 = RightArmBAM.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r8 = RightArmBAM.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r9 = RightArmBAM.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r10 = RightArmBAM.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-17.5F, -0.25F, 0.0F));

        PartDefinition cube_r11 = RightArmBAM2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r12 = RightArmBAM2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r13 = RightArmBAM2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r14 = RightArmBAM2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.5F, 6.75F, 0.0F));

        PartDefinition cube_r15 = RightArmBAM3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r16 = RightArmBAM3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r17 = RightArmBAM3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r18 = RightArmBAM3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(31, 40).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 0.0F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition cube_r19 = LeftArmBAM.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, -7.25F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r20 = LeftArmBAM.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.5F, -7.25F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r21 = LeftArmBAM.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.5F, -11.25F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r22 = LeftArmBAM.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(29, 26).mirror().addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(13.5F, -7.25F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r23 = LeftArmBAM4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(29, 26).mirror().addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(15.5F, -0.25F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r24 = LeftArmBAM4.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.5F, -0.25F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r25 = LeftArmBAM4.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5F, -4.25F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r26 = LeftArmBAM4.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, -0.25F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r27 = LeftArmBAM3.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(29, 26).mirror().addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(11.5F, 6.75F, 0.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r28 = LeftArmBAM3.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.5F, 6.75F, -4.25F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition cube_r29 = LeftArmBAM3.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, 2.75F, -4.25F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r30 = LeftArmBAM3.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(31, 40).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, 6.75F, -4.25F, -1.5708F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
    StandPowers Power = new PowersTheGratefulDead(null);

    @Override
    public void defaultAnimations(T entity, float animationProgress, float windupLength){
        this.animate(entity.idleAnimationState, StandAnimations.IDLE_2, animationProgress, 1f);
        this.animate(entity.idleAnimationState2, StandAnimations.STAND_IDLE_FLOAT, animationProgress, 1f);
        this.animate(entity.punchState1, StandAnimations.COMBO1, animationProgress, 1.4f);
        this.animate(entity.punchState2, StandAnimations.COMBO2, animationProgress, 1.16666f); /*1.1666 for 6 ticks, 1.4 for 5*/
        this.animate(entity.punchState3, StandAnimations.COMBO3, animationProgress, 1.16666f);
        this.animate(entity.blockAnimationState, StandAnimations.BLOCK, animationProgress, 1f);
        this.animate(entity.barrageChargeAnimationState, StandAnimations.BARRAGECHARGE, animationProgress, windupLength);
        this.animate(entity.barrageAnimationState, StandAnimations.BARRAGE, animationProgress, 1f);
        this.animate(entity.miningBarrageAnimationState, StandAnimations.MINING_BARRAGE, animationProgress, 1.65f);
        this.animate(entity.barrageEndAnimationState, StandAnimations.COMBO3, animationProgress, 2.2f);
        this.animate(entity.barrageHurtAnimationState, StandAnimations.BARRAGEDAMAGE, animationProgress, 2.5f);
        this.animate(entity.brokenBlockAnimationState, StandAnimations.BLOCKBREAK, animationProgress, 1.8f);
        this.animate(entity.standLeapAnimationState, StandAnimations.STAND_LEAP, animationProgress, 1f);
        this.animate(entity.standLeapEndAnimationState, StandAnimations.STAND_LEAP_END, animationProgress, 3.0f);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
        defaultModifiers(pEntity);

        this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1F);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}
