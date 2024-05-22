package net.hydra.jojomod.entity.Terrier;


import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class TerrierEntityModel<T extends TerrierEntity> extends WolfModel<T> {
    public TerrierEntityModel(ModelPart root) {
        super(root);
    }

    private static final String REAL_HEAD = "real_head";
    private static final String UPPER_BODY = "upper_body";
    private static final String REAL_TAIL = "real_tail";

    public static LayerDefinition createBodyLayerTerrier() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        float f = 13.5f;
        PartDefinition modelPartData2 = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create(), PartPose.offset(-1.0f, 13.5f, -7.0f));
        modelPartData2.addOrReplaceChild(REAL_HEAD, CubeListBuilder.create().texOffs(0, 0)
                .addBox(-2.0f, -3.0f, -2.0f, 6.0f, 6.0f, 4.0f, new CubeDeformation(0.3F)).texOffs(42, 14)
                .addBox(-2.5f, -6.0f, 0.0f, 3.0f, 3.0f, 1.0f, new CubeDeformation(-0.3F)).texOffs(42, 14)
                .addBox(1.5f, -6.0f, 0.0f, 3.0f, 3.0f, 1.0f, new CubeDeformation(-0.3F)).texOffs(0, 10)
                .addBox(-0.5f, -0.001f, -5.0f, 3.0f, 3.0f, 4.0f), PartPose.ZERO);
        modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(18, 14).addBox(-3.0f, -2.0f, -3.0f, 6.0f, 9.0f, 6.0f), PartPose.offsetAndRotation(0.0f, 14.0f, 2.0f, 1.5707964f, 0.0f, 0.0f));
        modelPartData.addOrReplaceChild(UPPER_BODY, CubeListBuilder.create().texOffs(21, 0).addBox(-3.0f, -3.0f, -3.0f, 8.0f, 6.0f, 7.0f, new CubeDeformation(-0.3F,-0F,-0.3F)), PartPose.offsetAndRotation(-1.0f, 14.0f, -3.0f, 1.5707964f, 0.0f, 0.0f));
        CubeListBuilder modelPartBuilder = CubeListBuilder.create().texOffs(0, 18).addBox(0.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f);
        modelPartData.addOrReplaceChild(PartNames.RIGHT_HIND_LEG, modelPartBuilder, PartPose.offset(-2.5f, 16.0f, 7.0f));
        modelPartData.addOrReplaceChild(PartNames.LEFT_HIND_LEG, modelPartBuilder, PartPose.offset(0.5f, 16.0f, 7.0f));
        modelPartData.addOrReplaceChild(PartNames.RIGHT_FRONT_LEG, modelPartBuilder, PartPose.offset(-2.5f, 16.0f, -4.0f));
        modelPartData.addOrReplaceChild(PartNames.LEFT_FRONT_LEG, modelPartBuilder, PartPose.offset(0.5f, 16.0f, -4.0f));
        PartDefinition modelPartData3 = modelPartData.addOrReplaceChild(PartNames.TAIL, CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0f, 12.0f, 8.0f, 0.62831855f, 0.0f, 0.0f));
        modelPartData3.addOrReplaceChild(REAL_TAIL, CubeListBuilder.create().texOffs(9, 18).addBox(0.0f, -1.4f, -1.0f, 2.0f, 8.0f, 2.0f,new CubeDeformation(0F,-1.2F,0F)), PartPose.ZERO);
        return LayerDefinition.create(modelData, 64, 32);
    }

}
