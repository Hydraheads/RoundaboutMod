package net.hydra.jojomod.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.entity.passive.WolfEntity;

@Environment(value= EnvType.CLIENT)
public class TerrierEntityModel<T extends TerrierEntity> extends WolfEntityModel<T> {
    public TerrierEntityModel(ModelPart root) {
        super(root);
    }

    private static final String REAL_HEAD = "real_head";
    private static final String UPPER_BODY = "upper_body";
    private static final String REAL_TAIL = "real_tail";

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        float f = 13.5f;
        ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create(), ModelTransform.pivot(-1.0f, 13.5f, -7.0f));
        modelPartData2.addChild(REAL_HEAD, ModelPartBuilder.create().uv(0, 0)
                .cuboid(-2.0f, -3.0f, -2.0f, 6.0f, 6.0f, 4.0f, new Dilation(0.3F)).uv(42, 14)
                .cuboid(-2.5f, -6.0f, 0.0f, 3.0f, 3.0f, 1.0f, new Dilation(-0.3F)).uv(42, 14)
                .cuboid(1.5f, -6.0f, 0.0f, 3.0f, 3.0f, 1.0f, new Dilation(-0.3F)).uv(0, 10)
                .cuboid(-0.5f, -0.001f, -5.0f, 3.0f, 3.0f, 4.0f), ModelTransform.NONE);
        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(18, 14).cuboid(-3.0f, -2.0f, -3.0f, 6.0f, 9.0f, 6.0f), ModelTransform.of(0.0f, 14.0f, 2.0f, 1.5707964f, 0.0f, 0.0f));
        modelPartData.addChild(UPPER_BODY, ModelPartBuilder.create().uv(21, 0).cuboid(-3.0f, -3.0f, -3.0f, 8.0f, 6.0f, 7.0f, new Dilation(-0.3F,-0F,-0.3F)), ModelTransform.of(-1.0f, 14.0f, -3.0f, 1.5707964f, 0.0f, 0.0f));
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 18).cuboid(0.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f);
        modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, modelPartBuilder, ModelTransform.pivot(-2.5f, 16.0f, 7.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, modelPartBuilder, ModelTransform.pivot(0.5f, 16.0f, 7.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, modelPartBuilder, ModelTransform.pivot(-2.5f, 16.0f, -4.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, modelPartBuilder, ModelTransform.pivot(0.5f, 16.0f, -4.0f));
        ModelPartData modelPartData3 = modelPartData.addChild(EntityModelPartNames.TAIL, ModelPartBuilder.create(), ModelTransform.of(-1.0f, 12.0f, 8.0f, 0.62831855f, 0.0f, 0.0f));
        modelPartData3.addChild(REAL_TAIL, ModelPartBuilder.create().uv(9, 18).cuboid(0.0f, -1.4f, -1.0f, 2.0f, 8.0f, 2.0f,new Dilation(0F,-1.2F,0F)), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

}
