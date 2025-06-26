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
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PlayerChestPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart breast;
    private final ModelPart Root;

    public PlayerChestPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.breast = Root.getChild("breast");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition chest2 = partdefinition.addOrReplaceChild("breast", CubeListBuilder.create(), PartPose.offset(-0.5F, 12.0F, 0.0F));

        PartDefinition chest_r1 = chest2.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, 0.005F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-3.5F, -7.75F, -3.775F, 1.5735F, 0.4581F, 1.5757F));

        PartDefinition chest_r2 = chest2.addOrReplaceChild("chest_r2", CubeListBuilder.create().texOffs(24, 20).addBox(-4.0F, 0.0F, 0.005F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(4.5F, -7.75F, -3.775F, 1.5502F, 0.4577F, 1.5222F));

        PartDefinition chest_r3 = chest2.addOrReplaceChild("chest_r3", CubeListBuilder.create().texOffs(20, 24).addBox(-4.0F, 0.0F, 0.005F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.5F, -7.75F, -3.775F, 1.117F, 0.0F, 0.0F));

        PartDefinition chest_r4 = chest2.addOrReplaceChild("chest_r4", CubeListBuilder.create().texOffs(20, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.5F, -11.35F, -2.0F, -0.4581F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
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

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha) {
        Roundabout.LOGGER.info("3");
        if (context instanceof AbstractClientPlayer LE) {
            Roundabout.LOGGER.info("4");
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(LE.getSkinTextureLocation()));
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

