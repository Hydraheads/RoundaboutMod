package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class IceTwisterModel<T extends Entity> extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "speedwagon_hat_converted"), "main");

    private final ModelPart root;

	public IceTwisterModel(ModelPart throot) {
        super(RenderType::entityTranslucent);

        throot = createBodyLayer().bakeRoot();
        root = throot;
	}

	public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, 0.0F));

        PartDefinition part1 = base.addOrReplaceChild("part1", CubeListBuilder.create().texOffs(0, 44).addBox(-16.0F, -6.0F, -16.0F, 32.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 1.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition part2 = base.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, 4.0F, -16.0F, 32.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-16.0F, -6.0F, -16.0F, 32.0F, 1.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
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

    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/stand/white_album/projectiles/ice_twister.png");
    }
    public void animate2(AnimationState $$0, AnimationDefinition $$1, float $$2, float $$3){
        this.animate($$0, $$1, $$2, $$3);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(getTextureLocation()));
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