package net.hydra.jojomod.client.models.layers;

import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.PlayerLikeModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class HumanoidLikeArmorModel<T extends JojoNPC> extends PlayerLikeModel<T> {
    public HumanoidLikeArmorModel(ModelPart $$0) {
        super();
    }

    public static MeshDefinition createBodyLayer(CubeDeformation $$0) {
        MeshDefinition $$1 = HumanoidModel.createMesh($$0, 0.0F);
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, $$0.extend(-0.1F)),
                PartPose.offset(-1.9F, 12.0F, 0.0F)
        );
        $$2.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, $$0.extend(-0.1F)),
                PartPose.offset(1.9F, 12.0F, 0.0F)
        );
        return $$1;
    }
}