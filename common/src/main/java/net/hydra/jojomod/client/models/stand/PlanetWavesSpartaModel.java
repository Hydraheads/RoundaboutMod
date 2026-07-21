package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.entity.stand.PlanetWavesEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PlanetWavesSpartaModel<T extends PlanetWavesEntity> extends PlanetWavesModel<T> {

    public PlanetWavesSpartaModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 60).addBox(-4.5F, -8.35F, -4.5F, 9.0F, 11.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition pipes = head2.addOrReplaceChild("pipes", CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 30).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(20, 30).addBox(-1.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -10.85F, 1.0F, -0.1745F, -0.1047F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create()
                        .texOffs(24, 16).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(28, 59).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.3F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cape_r1 = upper_chest_only.addOrReplaceChild("cape_r1", CubeListBuilder.create()
                        .texOffs(36, 69).addBox(-4.0F, -2.0F, -0.65F, 8.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -4.0F, 3.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create()
                        .texOffs(16, 36).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 46).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                        .texOffs(16, 30).addBox(-5.0F, -1.85F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create()
                        .texOffs(32, 36).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 27).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)),
                PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create()
                        .texOffs(0, 41).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                        .texOffs(48, 0).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                        .texOffs(16, 30).addBox(3.0F, -1.75F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create()
                        .texOffs(16, 46).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 36).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)),
                PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create()
                        .texOffs(24, 26).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create()
                        .texOffs(48, 18).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 45).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)),
                PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create()
                        .texOffs(48, 9).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create()
                        .texOffs(0, 30).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 51).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)),
                PartPose.offset(0.0F, 7.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }
}