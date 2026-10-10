package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class CoffinModel extends Model {
    private final ModelPart root;

    public CoffinModel(ModelPart p_171016_) {
        super(RenderType::entitySolid);
        this.root = p_171016_;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition coffin = partdefinition.addOrReplaceChild("coffin", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition left = coffin.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -8.0F));

        PartDefinition lid = left.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -27.0F, 12.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -9.0F, 19.0F, 0.0F, 0.0F, -0.3054F));

        PartDefinition bottom = left.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 69).addBox(0.0F, 7.0F, -25.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(0.0F, 0.0F, -27.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 19).addBox(-2.0F, 0.0F, -27.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(12, 44).addBox(12.0F, 0.0F, -27.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -9.0F, 19.0F));

        PartDefinition right = coffin.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 8.0F));

        PartDefinition lid2 = right.addOrReplaceChild("lid2", CubeListBuilder.create().texOffs(96, 0).addBox(0.0F, -3.0F, 0.0F, 12.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -9.0F, -8.0F, 0.0F, 0.0F, -0.3054F));

        PartDefinition bottom2 = right.addOrReplaceChild("bottom2", CubeListBuilder.create().texOffs(96, 69).addBox(-6.0F, -1.0F, 0.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(108, 19).addBox(-8.0F, -8.0F, 0.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(108, 44).addBox(6.0F, -8.0F, 0.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 19).addBox(-6.0F, -8.0F, 14.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -8.0F));

        return LayerDefinition.create(meshdefinition, 160, 160);
    }

    public void renderToBuffer(PoseStack p_103919_, VertexConsumer p_103920_, int p_103921_, int p_103922_, float p_103923_, float p_103924_, float p_103925_, float p_103926_) {
        this.root.render(p_103919_, p_103920_, p_103921_, p_103922_, p_103923_, p_103924_, p_103925_, p_103926_);
    }
}
