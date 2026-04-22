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
import net.minecraft.world.phys.Vec3;

public class Tusk1NailModel extends PsuedoHierarchicalModel {
	private final ModelPart model;
    private final ModelPart nail1;
    private final ModelPart nail2;

    private final ModelPart act_2;
   // private final ModelPart nail2_1;
    private final ModelPart nail2_2;
    private final ModelPart nail2_3;


    public Tusk1NailModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.model = root;
        this.nail1 = root.getChild("nail1");
        this.nail2 = root.getChild("nail2");

        this.act_2 = root.getChild("act_2");
     //   this.nail2_1 = act_2.getChild("nail2_1");
        this.nail2_2 = act_2.getChild("nail2_2");
        this.nail2_3 = act_2.getChild("nail2_3");


    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition nail1 = partdefinition.addOrReplaceChild("nail1", CubeListBuilder.create().texOffs(-2, 2).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition nail2 = partdefinition.addOrReplaceChild("nail2", CubeListBuilder.create().texOffs(-2, 0).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition act_2 = partdefinition.addOrReplaceChild("act_2", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

     //   PartDefinition nail2_1 = act_2.addOrReplaceChild("nail2_1", CubeListBuilder.create(), PartPose.offset(1.5F, -0.5F, 0.0F));

       // PartDefinition cube_r1 = nail2_1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-3, 7).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, -1.5F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition nail2_2 = act_2.addOrReplaceChild("nail2_2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r2 = nail2_2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-3, 4).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition nail2_3 = act_2.addOrReplaceChild("nail2_3", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 1.5F));

        PartDefinition cube_r3 = nail2_3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-3, 10).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

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


        Vec3 d = var1.getDeltaMovement();
        act_2.yRot = (float) Math.atan(d.x/d.z);
     //   nail2_1.zRot = ageInTicks*0.1F;
        nail2_2.zRot = -ageInTicks;
        nail2_3.zRot = ageInTicks;

    }


}