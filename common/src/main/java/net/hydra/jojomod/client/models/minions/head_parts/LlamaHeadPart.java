package net.hydra.jojomod.client.models.minions.head_parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.entity.zombie_minion.*;
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

public class LlamaHeadPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart head;
    private final ModelPart Root;

    public LlamaHeadPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.head = Root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -18.0F, -4.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(17, 0).addBox(-4.0F, -21.0F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(17, 0).addBox(1.0F, -21.0F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -16.0F, -8.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }

    /**Idle 1 (byte 0) = head straight, idle 2 (byte 1) = head follow*/


    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/minions/llama.png");
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation()));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (context instanceof ParrotMinion pm){
                head.xScale = 0.55F;
                head.yScale = 0.55F;
                head.zScale = 0.55F;
                head.y += 1F;
            }
            if (context instanceof DogMinion pm){
                head.xScale = 0.97F;
                head.yScale = 0.97F;
                head.zScale = 0.97F;
                head.x += 1F;
                head.y += 2F;
            }
            if (context instanceof OcelotMinion pm){
                head.xScale = 0.83F;
                head.yScale = 0.83F;
                head.zScale = 0.83F;
                head.y += 2F;
            }
            if (context instanceof ChickenMinion pm){
                head.xScale = 0.63F;
                head.yScale = 0.63F;
                head.zScale = 0.63F;
            }

            if (context instanceof AxolotlMinion pm){
                head.xScale = 0.98F;
                head.yScale = 0.98F;
                head.zScale = 0.98F;
            }
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation()));
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

