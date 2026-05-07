package net.hydra.jojomod.client.models.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.visages.parts.RipperEyesAnimation;
import net.hydra.jojomod.entity.projectile.RipperEyesProjectile;
import net.hydra.jojomod.entity.projectile.StandFireballEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.AnimationState;

public class RipperEyesModel<T extends RipperEyesProjectile> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart laser;
    private final ModelPart Root;

    public RipperEyesModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.Root = root;
        this.laser = root.getChild("laser");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition laser = partdefinition.addOrReplaceChild("laser", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0F));

        PartDefinition right_laser = laser.addOrReplaceChild("right_laser", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition left_laser = laser.addOrReplaceChild("left_laser", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.0F));

        PartDefinition right_laser2 = laser.addOrReplaceChild("right_laser2", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition left_laser2 = laser.addOrReplaceChild("left_laser2", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        entity.ripperEyes.startIfStopped(entity.tickCount);
    }

    public void animate2(AnimationState $$0, AnimationDefinition $$1, float $$2, float $$3){
        this.animate($$0, $$1, $$2, $$3);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

}
