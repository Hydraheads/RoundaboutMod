package net.hydra.jojomod.client.models;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class RoadRollerStrayModel<T extends Entity> extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "road_roller_layer"), "main");
    private final ModelPart front_wheel;
    private final ModelPart back_wheel;
    private final ModelPart bb_main;
    private final ModelPart root;

	public RoadRollerStrayModel() {
        super(RenderType::entityTranslucent);

        this.root = createBodyLayer().bakeRoot();
        this.front_wheel = root.getChild("front_wheel");
        this.back_wheel = root.getChild("back_wheel");
        this.bb_main = root.getChild("bb_main");
	}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition front_wheel = partdefinition.addOrReplaceChild("front_wheel", CubeListBuilder.create().texOffs(36, 58).addBox(-6.0F, -2.8F, -3.0F, 12.0F, 6.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 21.0F, -5.0F));

        PartDefinition back_wheel = partdefinition.addOrReplaceChild("back_wheel", CubeListBuilder.create().texOffs(0, 42).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(40, 42).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 20.0F, 11.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(44, 16).addBox(-5.0F, -10.0F, -9.0F, 10.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(72, 58).addBox(-5.0F, -8.0F, -1.0F, 10.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.5F, -13.0F, 6.0F, 11.0F, 10.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(36, 70).addBox(-7.0F, -11.0F, 9.0F, 14.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(72, 70).addBox(-3.0F, -8.5F, 2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-5.5F, -13.0F, 6.0F, 11.0F, 10.0F, 11.0F, new CubeDeformation(0.1F))
                .texOffs(47, 3).addBox(-5.6F, -15.85F, 6.4F, 11.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 58).addBox(-5.0F, -10.0F, -9.0F, 10.0F, 7.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 73).addBox(-3.0F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 5.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(44, 31).addBox(-6.0F, -3.5F, -2.0F, 12.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, -3.5F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

	/* @Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	} */

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        front_wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        back_wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {
    }
    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/entity/road_roller.png");
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation()));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation()));
            //r = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }
}