package net.hydra.jojomod.client.models.worn_stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SoftAndWetShootingArmModel extends PsuedoHierarchicalModel {
// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "soft_and_wet_shooting_arm"), "main");
    private final ModelPart armAddon;
    private final ModelPart Root;

    public SoftAndWetShootingArmModel() {
        super(RenderType::entityTranslucent);
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition partdefinition = mesh.getRoot();
        PartDefinition arm_addon = partdefinition.addOrReplaceChild("arm_addon", CubeListBuilder.create().texOffs(0, 0).addBox(3.0F, 8.525F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offset(-6.0F, 9.475F, 0.0F));
        this.Root = LayerDefinition.create(mesh, 32, 32).bakeRoot();
        this.armAddon = Root.getChild("arm_addon");
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        armAddon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1,  float pAgeInTicks) {

    }

}
