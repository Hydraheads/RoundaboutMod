package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GentlyWeepsModel<T extends Entity> extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

    private final ModelPart root;

	public GentlyWeepsModel(ModelPart throot) {
        super(RenderType::entityTranslucent);

        throot = createBodyLayer().bakeRoot();
        root = throot;
	}

	public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 25.0F, 0.0F));

        PartDefinition part1 = base.addOrReplaceChild("part1", CubeListBuilder.create().texOffs(2, 3).addBox(-20.6F, -5.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(-1.6F, -5.0F, 18.6F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(-1.6F, -5.0F, -21.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(17.4F, -5.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition part2 = base.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(2, 3).addBox(-20.6F, 8.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(-1.6F, 8.0F, 18.6F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(-1.6F, 8.0F, -21.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(17.4F, 8.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -33.0F, 0.0F));

        PartDefinition part3 = base.addOrReplaceChild("part3", CubeListBuilder.create().texOffs(2, 3).addBox(-20.6F, -5.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(-1.6F, -5.0F, 18.6F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(-1.6F, -5.0F, -21.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(17.4F, -5.0F, -1.4F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -33.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {
    }

    public ResourceLocation getTextureLocation(Entity context){
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/stand/white_album/projectiles/gently_weeps.png");
    }
    public void animate2(AnimationState $$0, AnimationDefinition $$1, float $$2, float $$3){
        this.animate($$0, $$1, $$2, $$3);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(context)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            //r = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }
}