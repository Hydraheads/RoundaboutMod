package net.hydra.jojomod.client.models.badCompanyTroops;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.badCompanyTroops.BCTank;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BCTankModel<T extends BCTank> extends StandModel<T> implements HeadedModel {
    private final ModelPart stand2;
    private final ModelPart wheels;
    private final ModelPart cannon;

    public BCTankModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.stand2 = this.stand.getChild("stand2");
        this.wheels = this.stand2.getChild("wheels");
        this.head = this.stand2.getChild("head");
        this.cannon = this.head.getChild("cannon");
    }

    public ModelPart getHead(){
        return this.head;
    }

    public ModelPart getCannon(){
        return this.cannon;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -3.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -1.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition wheels = stand2.addOrReplaceChild("wheels", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -2.0F, -1.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(0, 20).addBox(2.0F, -2.0F, -1.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.3F))
                .texOffs(14, 22).addBox(2.25F, -2.0F, -1.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(14, 22).addBox(-3.25F, -2.0F, -1.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, -1.75F, 4.0F));

        PartDefinition cannon = head.addOrReplaceChild("cannon", CubeListBuilder.create().texOffs(10, 22).addBox(-0.5F, -0.5F, -4.75F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing,limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        //if (!entity.isDisplay) {


            this.getHead().yRot = netHeadYaw * ((float) Math.PI / 180F);
            this.getCannon().xRot = headPitch * ((float) Math.PI / 180F);
        //}

        defaultModifiers(entity);

    }

    @Override
    public ModelPart root() {
        return stand;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
