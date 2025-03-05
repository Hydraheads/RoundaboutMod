package net.hydra.jojomod.entity.visages.mobs;

import com.google.common.collect.ImmutableList;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.PlayerLikeModel;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PlayerNPCModel<T extends JojoNPCPlayer> extends PlayerLikeModel<T> {
    public PlayerNPCModel(ModelPart root) {
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

        PartDefinition hat = head_part.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body_part = full_body.addOrReplaceChild("body_part", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_body = body_part.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition right_arms = upper_body.addOrReplaceChild("right_arms", CubeListBuilder.create(), PartPose.offset(-3.5F, -10.0F, 0.0F));

        PartDefinition right_arm = right_arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition right_sleeve = right_arms.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-4.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-0.5F, 2.0F, 0.0F));

        PartDefinition left_arms = upper_body.addOrReplaceChild("left_arms", CubeListBuilder.create(), PartPose.offset(3.5F, -10.0F, 0.0F));

        PartDefinition left_arm = left_arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(0.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.0F, 0.0F));

        PartDefinition left_sleeve = left_arms.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(0.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(0.5F, 2.0F, 0.0F));

        PartDefinition body = upper_body.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition jacket = upper_body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.255F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition cloak = upper_body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(78, 15).addBox(-5.0F, -11.0F, 2.5F, 10.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legs = body_part.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-5.0F, -24.0F, 0.0F));

        PartDefinition right_legs = legs.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(2.9F, 12.0F, 0.0F));

        PartDefinition right_pants = right_legs.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.1F, 0.0F, 0.0F));

        PartDefinition right_leg = right_legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 0.0F, 0.0F));

        PartDefinition left_legs = legs.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(7.0F, 12.0F, 0.0F));

        PartDefinition left_leg = left_legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_pants = left_legs.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.249F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
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
