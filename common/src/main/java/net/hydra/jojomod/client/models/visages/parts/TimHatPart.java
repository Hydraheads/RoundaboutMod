package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class TimHatPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart hat;
    private final ModelPart Root;

    public TimHatPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.hat = Root.getChild("hat");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(-8, 18).addBox(-4.0F, -4.9F, -5.0F, 8.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_cowboy_r1 = hat.addOrReplaceChild("right_cowboy_r1", CubeListBuilder.create().texOffs(7, 8).addBox(-3.0F, 0.0F, -5.0F, 3.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -4.9F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition right_cowboy_r2 = hat.addOrReplaceChild("right_cowboy_r2", CubeListBuilder.create().texOffs(10, 8).addBox(-3.0F, 0.0F, -5.0F, 3.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition left_cowboy_r1 = hat.addOrReplaceChild("left_cowboy_r1", CubeListBuilder.create().texOffs(1, 8).addBox(0.0F, 0.0F, -5.0F, 3.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -4.9F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition left_cowboy_r2 = hat.addOrReplaceChild("left_cowboy_r2", CubeListBuilder.create().texOffs(4, 8).addBox(0.0F, 0.0F, -5.0F, 3.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition right_hair_r1 = hat.addOrReplaceChild("right_hair_r1", CubeListBuilder.create().texOffs(1, 6).mirror().addBox(-2.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -2.0F, 4.09F, 0.0F, 0.6981F, 0.0F));

        PartDefinition right_hair_r2 = hat.addOrReplaceChild("right_hair_r2", CubeListBuilder.create().texOffs(4, 14).mirror().addBox(-2.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -2.0F, 3.99F, 0.0F, 0.6981F, 0.0F));

        PartDefinition left_hair_r1 = hat.addOrReplaceChild("left_hair_r1", CubeListBuilder.create().texOffs(1, 2).addBox(-1.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -2.0F, 4.09F, 0.0F, -0.6981F, 0.0F));

        PartDefinition left_hair_r2 = hat.addOrReplaceChild("left_hair_r2", CubeListBuilder.create().texOffs(4, 10).addBox(-1.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -2.0F, 3.99F, 0.0F, -0.6981F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }

    /**Idle 1 (byte 0) = head straight, idle 2 (byte 1) = head follow*/


    public ResourceLocation getTextureLocation(String path){
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_hats/"+path+".png");
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light, String path) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(path)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, String path) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(path)));
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

