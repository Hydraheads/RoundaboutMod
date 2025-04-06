package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CrossfireFirestormModel<T extends CrossfireHurricaneEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "crossfire_hurricane"), "main");
    private final ModelPart Fire_Ankh;
    private final ModelPart Square;
    private final ModelPart Root;

    public CrossfireFirestormModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.Root = root;
        this.Fire_Ankh = root.getChild("Fire_Ankh");
        this.Square = this.Fire_Ankh.getChild("Square");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Fire_Ankh = partdefinition.addOrReplaceChild("Fire_Ankh", CubeListBuilder.create().texOffs(0, 4).addBox(-0.5F, -10.0F, -0.5F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(1, 0).addBox(-6.5F, -12.0F, -0.5F, 14.0F, 2.0F, 2.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition cube_r1 = Fire_Ankh.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 10).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(6.075F, -9.45F, 0.5F, 0.0F, 0.0F, 2.0508F));

        PartDefinition cube_r2 = Fire_Ankh.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 10).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-5.975F, -11.25F, 0.5F, 0.0F, 0.0F, -2.0508F));

        PartDefinition Square = Fire_Ankh.addOrReplaceChild("Square", CubeListBuilder.create(), PartPose.offset(1.2F, -6.0F, 0.0F));

        PartDefinition cube_r3 = Square.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 7).addBox(-3.5F, -3.5F, -0.5F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.02F))
                .texOffs(8, 14).addBox(-3.5F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.002F))
                .texOffs(14, 14).addBox(0.5F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.01F))
                .texOffs(8, 4).addBox(-3.5F, 0.5F, -0.5F, 6.0F, 2.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, -9.7F, 0.0F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Fire_Ankh.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

}