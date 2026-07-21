package net.hydra.jojomod.client.models.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.PWBigMeteorEntity;
import net.hydra.jojomod.entity.projectile.PWMeteorEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class PWBigMeteorModel <T extends PWBigMeteorEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "pw_big_meteor"), "main");
    private final ModelPart Fireball;
    private final ModelPart Root;

    public PWBigMeteorModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.Root = root;
        this.Fireball = root.getChild("Fireball");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Fireball = partdefinition.addOrReplaceChild("Fireball", CubeListBuilder.create()
                        .texOffs(0, 18).addBox(-4.0F, -7.0F, -4.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.5F, 3.5F, 0.5F));
        PartDefinition layer = Fireball.addOrReplaceChild("layer", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5.0F, -8.0F, -5.0F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
                        .texOffs(28, 18).addBox(-3.0F, -5F, -11.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Fireball.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

}



