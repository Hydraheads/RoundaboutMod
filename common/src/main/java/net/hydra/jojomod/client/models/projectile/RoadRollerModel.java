package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.hydra.jojomod.client.models.projectile.animations.road_roller_wheels;
import net.hydra.jojomod.client.models.projectile.animations.road_roller_explosion;

public class RoadRollerModel <T extends LivingEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("roundabout", "road_roller_layer"), "main");
    private final ModelPart front_wheel;
    private final ModelPart back_wheel;
    private final ModelPart bb_main;
    private final ModelPart road_roller;
    private final ModelPart root;

    public RoadRollerModel(ModelPart root) {
        super(RenderType::entityTranslucent);

        this.root = createBodyLayer().bakeRoot();
        this.road_roller = root.getChild("road_roller");
        this.bb_main = this.road_roller.getChild("bb_main");
        this.front_wheel = this.bb_main.getChild("front_wheel");
        this.back_wheel = this.bb_main.getChild("back_wheel");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition road_roller = partdefinition.addOrReplaceChild("road_roller", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bb_main = road_roller.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(44, 16).addBox(-5.0F, -10.0F, -9.0F, 10.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 58).addBox(-5.0F, -10.0F, -9.0F, 10.0F, 7.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(72, 58).addBox(-5.0F, -8.0F, -1.0F, 10.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.5F, -13.0F, 6.0F, 11.0F, 10.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-5.5F, -13.0F, 6.0F, 11.0F, 10.0F, 11.0F, new CubeDeformation(0.1F))
                .texOffs(36, 70).addBox(-7.0F, -11.0F, 9.0F, 14.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(72, 70).addBox(-3.0F, -8.5F, 2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(47, 3).addBox(-5.6F, -15.85F, 6.4F, 11.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 73).addBox(-3.0F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 5.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(44, 31).addBox(-6.0F, -3.5F, -2.0F, 12.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, -3.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition front_wheel = bb_main.addOrReplaceChild("front_wheel", CubeListBuilder.create().texOffs(36, 58).addBox(-6.0F, -2.8F, -3.0F, 12.0F, 6.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -3.0F, -5.0F));

        PartDefinition back_wheel = bb_main.addOrReplaceChild("back_wheel", CubeListBuilder.create().texOffs(0, 42).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(40, 42).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -4.0F, 11.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        road_roller.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return this.road_roller;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof RoadRollerEntity roadRoller) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            this.animate(roadRoller.wheels, road_roller_wheels.animation, ageInTicks, 1f);
            this.animate(roadRoller.explode, road_roller_explosion.animation, ageInTicks, 1f);
        }
    }
}