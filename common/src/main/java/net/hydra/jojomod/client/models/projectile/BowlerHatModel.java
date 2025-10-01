package net.hydra.jojomod.client.models.projectile;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BowlerHatModel<T extends Entity> extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "speedwagon_hat_converted"), "main");
	private final ModelPart hat;
    private final ModelPart root;

	public BowlerHatModel() {
        super(RenderType::entityTranslucent);

        this.root = createBodyLayer().bakeRoot();
        this.hat = root.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 13).addBox(-13.5F, -6.5F, 4.5F, 10.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(32, 29).addBox(-12.5F, -11.5F, 5.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-12.5F, -11.5F, 5.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.1F))
		.texOffs(0, 0).addBox(-13.5F, -9.5F, 4.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	/* @Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	} */

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {
    }

    /**Idle 1 (byte 0) = head straight, idle 2 (byte 1) = head follow*/
    public void rotateHead(Entity context, float partialTicks, StandUser user){
        if (this.root().getChild("stand").getChild("stand2").hasChild("head")) {
            ModelPart head = this.root().getChild("stand").getChild("stand2").getChild("head");
            if (user.roundabout$getIdlePos() == 1) {
                head.setRotation((((context.getViewXRot(partialTicks)) * Mth.DEG_TO_RAD)), 0, 0);
            } else {
                head.setRotation(0, 0, 0);
            }

        }
    }
    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/item/bowler_hat_in_hand.png");
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