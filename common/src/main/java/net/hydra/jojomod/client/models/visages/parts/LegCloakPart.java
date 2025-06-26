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

public class LegCloakPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart cloak;
    private final ModelPart Root;
    private final ModelPart rotate;

    public LegCloakPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.cloak = Root.getChild("cloak");
        this.rotate = Root.getChild("cloak").getChild("rotate");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition cloak = partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition rotate = cloak.addOrReplaceChild("rotate", CubeListBuilder.create(), PartPose.offset(0.0F, 0.6F, 2.0F));

        PartDefinition jacket_r1 = rotate.addOrReplaceChild("jacket_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, -0.5209F, -3.9544F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.55F, 0.05F, 2.9671F, 0.0F, 3.1416F));

        PartDefinition jacket_r2 = rotate.addOrReplaceChild("jacket_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -1.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(3.0F, 0.675F, -3.05F, 0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        cloak.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
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
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_leg_cloaks/"+path+".png");
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light, String path) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(path)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, String path, float xRot) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            cloak.xRot = xRot;
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(path)));
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

