package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class StandModel<T extends StandEntity> extends SinglePartEntityModel<T> {
    private float alpha;
    ModelPart stand;
    ModelPart head;
    ModelPart body;

    public void setHeadRotations(float pitch,float yaw){
        this.head.pitch = pitch;
        this.head.yaw = yaw;
    }public void setBodyRotations(float pitch,float yaw){
        this.body.pitch = pitch;
        this.body.yaw = yaw;
    } public void setStandRotations(float pitch,float yaw){
        this.stand.pitch = pitch;
        this.stand.yaw = yaw;
    }

    @Override
    public ModelPart getPart() {
        return stand;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        stand.render(matrices, vertexConsumer, light, overlay, red, green, blue, this.alpha);
    }

    public void setAlpha(float alpha){
        this.alpha = alpha;
    }
}
