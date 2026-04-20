package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.TuskAnimations;
import net.hydra.jojomod.entity.projectile.TuskNailEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class Tusk1NailModel extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart model;
    private final ModelPart nail1;
    private final ModelPart nail2;

    public Tusk1NailModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.model = root;
        this.nail1 = root.getChild("nail1");
        this.nail2 = root.getChild("nail2");

    }

	public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition nail1 = partdefinition.addOrReplaceChild("nail1", CubeListBuilder.create().texOffs(-2, 2).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition nail2 = partdefinition.addOrReplaceChild("nail2", CubeListBuilder.create().texOffs(-2, 0).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        model.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return model;
    }

    @Override
    public void setupAnim(Entity var1, float ageInTicks) {
        nail1.yRot = ageInTicks;
        nail2.yRot = -ageInTicks;
    }


}