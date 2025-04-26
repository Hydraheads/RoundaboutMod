package net.hydra.jojomod.entity.visages.mobs;

import net.hydra.jojomod.entity.visages.PlayerLikeModel;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class JosukePartEightModel<T extends JosukePartEightNPC> extends PlayerLikeModel<T> {
    public JosukePartEightModel(ModelPart root) {
        initParts(root);
    }

    @Override
    public boolean getSlim(){
        return false;
    }

    @Override
    public void defaultModifiers(T entity) {
        super.defaultModifiers(entity);
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition playerlike = partdefinition.addOrReplaceChild("playerlike", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition full_body = playerlike.addOrReplaceChild("full_body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_part = full_body.addOrReplaceChild("head_part", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition head = head_part.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-1.5F, -2.0F, -4.0F, 0.0F, 0.0349F, 0.0F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(1.525F, -2.0F, -4.0F, 0.0F, 0.0349F, 0.0F));

        PartDefinition hat = head_part.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(11, 67).addBox(-5.0F, -7.65F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = hat.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(68, 2).addBox(-1.0026F, -0.682F, -0.2933F, 6.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.1F, -8.5F, -0.2F, 0.0F, 0.0033F, -2.8798F));

        PartDefinition body_part = full_body.addOrReplaceChild("body_part", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legs = body_part.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-5.0F, -24.0F, 0.0F));

        PartDefinition right_legs = legs.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(3.0F, 12.0F, 0.0F));

        PartDefinition right_pants = right_legs.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = right_legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_legs = legs.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(7.0F, 12.0F, 0.0F));

        PartDefinition left_leg = left_legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_pants = left_legs.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.249F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_body = body_part.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition right_arms = upper_body.addOrReplaceChild("right_arms", CubeListBuilder.create(), PartPose.offset(-3.5F, -10.0F, 0.0F));

        PartDefinition right_arm = right_arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.8F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.7F, 2.0F, 0.0F));

        PartDefinition right_sleeve = right_arms.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.8F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-0.7F, 2.0F, 0.0F));

        PartDefinition left_arms = upper_body.addOrReplaceChild("left_arms", CubeListBuilder.create(), PartPose.offset(3.5F, -10.0F, 0.0F));

        PartDefinition left_arm = left_arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-0.2F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.7F, 2.0F, 0.0F));

        PartDefinition left_sleeve = left_arms.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(49, 48).addBox(-0.2F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(0.7F, 2.0F, 0.0F));

        PartDefinition body = upper_body.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.001F))
                .texOffs(66, 26).addBox(0.15F, 1.925F, -2.425F, 5.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(66, 18).addBox(-3.425F, -2.65F, -3.325F, 5.0F, 5.0F, 2.0F, new CubeDeformation(-0.6F)), PartPose.offsetAndRotation(3.475F, 4.95F, 0.35F, 0.0F, 0.0F, 0.1658F));

        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(66, 10).addBox(-1.0F, -5.0F, -1.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(-0.7F)), PartPose.offsetAndRotation(-3.425F, 7.2F, -2.325F, -0.0999F, -0.01F, -0.0999F));

        PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(-5.0F, -0.4F, 0.0F));

        PartDefinition cloak = upper_body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(78, 15).addBox(-5.0F, -11.0F, 2.5F, 10.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition jacket = upper_body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.255F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
    StandPowers Power = new PowersStarPlatinum(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
    }

    @Override
    public ModelPart root() {
        return playerlike;
    }
}
