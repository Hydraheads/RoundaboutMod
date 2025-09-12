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

public class SmallChestPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart breast;
    private final ModelPart Root;

    public SmallChestPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.breast = Root.getChild("breast");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition breast = partdefinition.addOrReplaceChild("breast", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 12.2F, 0.0F, 0.3578F, 0.0F, 0.0F));

        PartDefinition shirt_chest_r1 = breast.addOrReplaceChild("shirt_chest_r1", CubeListBuilder.create().texOffs(1, 10).addBox(-4.0F, 0.0F, 1.0F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -11.702F, 1.1477F, -0.6109F, 0.0F, 0.0F));

        PartDefinition chest_r1 = breast.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(1, 1).addBox(-4.0F, 0.0F, 1.0F, 8.0F, 4.0F, 3.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -11.702F, 1.1477F, -0.4363F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        breast.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
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
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_breasts/"+path+".png");
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

