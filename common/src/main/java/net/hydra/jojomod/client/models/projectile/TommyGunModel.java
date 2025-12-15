package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.HierarchicalModel;
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

public class TommyGunModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "snubnosed_revolver"), "main");
    private final ModelPart TommyGun;
    private final ModelPart root;

	public TommyGunModel() {
        this.root = createBodyLayer().bakeRoot();
        this.TommyGun = root.getChild("TommyGun");
	}

	public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition TommyGun = partdefinition.addOrReplaceChild("TommyGun", CubeListBuilder.create().texOffs(22, 19).addBox(-0.5F, -4.0F, -17.501F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -4.0F, -17.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.25F))
                .texOffs(15, 21).addBox(-0.5F, -4.0F, -14.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-1.0F, -3.0F, -11.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.001F))
                .texOffs(0, 0).addBox(-1.5F, -4.0F, -6.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(15, 19).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(22, -1).addBox(0.0F, -1.0F, -3.25F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.7349F, 5.8871F));

        PartDefinition buttstock_r1 = TommyGun.addOrReplaceChild("buttstock_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-1.25F, -1.5F, -3.0F, 2.5F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 5.25F, -0.3054F, 0.0F, 0.0F));

        PartDefinition back_grip_r1 = TommyGun.addOrReplaceChild("back_grip_r1", CubeListBuilder.create().texOffs(15, 1).addBox(-1.0F, -3.0F, -1.5F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition drum_r1 = TommyGun.addOrReplaceChild("drum_r1", CubeListBuilder.create().texOffs(11, 12).addBox(-2.5F, -2.5F, -1.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, -4.25F, 0.0F, 0.0F, 0.7854F));

        PartDefinition front_grip_r1 = TommyGun.addOrReplaceChild("front_grip_r1", CubeListBuilder.create().texOffs(14, 23).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -11.0F, 0.48F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 33, 33);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        TommyGun.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(Roundabout.MOD_ID, "textures/item/tommy_gun.png");
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