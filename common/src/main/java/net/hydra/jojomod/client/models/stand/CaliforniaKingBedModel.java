package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.layers.animations.KingBedAnimations;
import net.hydra.jojomod.client.models.stand.animations.DiverDownAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class CaliforniaKingBedModel<T extends CaliforniaKingBedEntity> extends StandModel<T>{
    public CaliforniaKingBedModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, 2.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -0.5F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso_string = body2.addOrReplaceChild("torso_string", CubeListBuilder.create().texOffs(44, 2).mirror().addBox(1.5F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(41, 2).addBox(-5.5F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = torso_string.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(45, 55).addBox(-4.0F, 0.0F, -2.75F, 8.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(23, 53).addBox(-1.55F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition left_leg_string = torso.addOrReplaceChild("left_leg_string", CubeListBuilder.create().texOffs(44, 45).addBox(-4.5F, 0.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 8.0F, 0.0F));

        PartDefinition left_leg_substring = left_leg_string.addOrReplaceChild("left_leg_substring", CubeListBuilder.create().texOffs(44, 50).addBox(-3.0F, 0.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 5.0F, 0.0F));

        PartDefinition left_leg = left_leg_substring.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(34, 59).addBox(-2.0F, 0.0F, -2.75F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(38, 27).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(23, 53).addBox(-1.5F, 1.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 4.0F, 0.0F));

        PartDefinition right_leg_string = torso.addOrReplaceChild("right_leg_string", CubeListBuilder.create().texOffs(34, 45).addBox(-0.5F, 0.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 8.0F, 0.0F));

        PartDefinition right_leg_substring = right_leg_string.addOrReplaceChild("right_leg_substring", CubeListBuilder.create().texOffs(34, 50).addBox(-2.0F, 0.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 5.0F, 0.0F));

        PartDefinition right_leg = right_leg_substring.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(34, 59).addBox(-2.0F, 0.0F, -2.75F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(24, 5).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(23, 53).addBox(-1.5F, 1.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 4.0F, 0.0F));

        PartDefinition right_arm_string = stand2.addOrReplaceChild("right_arm_string", CubeListBuilder.create().texOffs(42, 6).addBox(-0.5F, 0.0F, 0.0F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 6.0F, -0.5F));

        PartDefinition right_arm_substring = right_arm_string.addOrReplaceChild("right_arm_substring", CubeListBuilder.create().texOffs(44, 11).addBox(-1.0F, 0.0F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 5.0F, 0.0F));

        PartDefinition right_arm = right_arm_substring.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(34, 59).addBox(-2.0F, 0.0F, -2.75F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(38, 36).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(23, 53).addBox(-1.5F, 1.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 3.0F, 0.0F));

        PartDefinition left_arm_string = stand2.addOrReplaceChild("left_arm_string", CubeListBuilder.create().texOffs(5, 41).addBox(-1.5F, 0.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 6.0F, -0.5F));

        PartDefinition left_arm_substring = left_arm_string.addOrReplaceChild("left_arm_substring", CubeListBuilder.create().texOffs(0, 45).addBox(-5.0F, 0.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 4.0F, 0.0F));

        PartDefinition left_arm = left_arm_substring.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(34, 59).addBox(-2.0F, 0.0F, -2.75F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(16, 40).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(23, 53).addBox(-1.5F, 1.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 4.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(23, 27).addBox(4.0F, -10.0F, -1.0F, 4.0F, 10.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(23, 50).addBox(-7.5F, -4.0F, -1.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(23, 53).addBox(-1.5F, -8.0F, 4.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(55, 14).addBox(-7.5F, -4.0F, 2.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(55, 14).addBox(4.5F, -4.0F, 2.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(23, 50).addBox(4.5F, -4.0F, -1.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(39, 14).addBox(-6.0F, -12.0F, -2.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(-4.0F, -10.0F, -3.0F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-8.0F, -12.0F, -1.0F, 16.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(-8.0F, -10.0F, -1.0F, 4.0F, 10.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);

        this.animate(pEntity.barrageHurtAnimationState, StandAnimations.BARRAGEDAMAGE, pAgeInTicks, 2.5f);
        this.animate(pEntity.fall_brace, KingBedAnimations.Fall_Brace, pAgeInTicks, 1f);
        this.animate(pEntity.idleAnimationState2, KingBedAnimations.Normal, pAgeInTicks, 1f);
        this.animate(pEntity.idleAnimationState, KingBedAnimations.Whimsical, pAgeInTicks, 0.9F);
    }

    @Override
    public float rotationStrength(){
        return 3f;
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}
