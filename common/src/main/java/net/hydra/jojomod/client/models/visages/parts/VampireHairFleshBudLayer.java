package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class VampireHairFleshBudLayer extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart hair;
    private final ModelPart Root;

    public VampireHairFleshBudLayer() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.hair = Root.getChild("hair");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offset(0.0F, -6.5F, 0.0F));

        PartDefinition right_flesh_bud = hair.addOrReplaceChild("right_flesh_bud", CubeListBuilder.create().texOffs(-17, 0).addBox(-8.0F, 0.0F, -26.0F, 8.0F, 0.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_flesh_bud_strand_r1 = right_flesh_bud.addOrReplaceChild("left_flesh_bud_strand_r1", CubeListBuilder.create().texOffs(-17, 0).mirror().addBox(-4.625F, -0.024F, -26.0F, 8.0F, 0.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.625F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition right_flesh_bud_r1 = right_flesh_bud.addOrReplaceChild("right_flesh_bud_r1", CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-2.5F, 0.0F, -23.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition right_incomplete_bud_r1 = right_flesh_bud.addOrReplaceChild("right_incomplete_bud_r1", CubeListBuilder.create().texOffs(30, 6).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-4.5F, 0.0F, -19.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition left_flesh_bud = hair.addOrReplaceChild("left_flesh_bud", CubeListBuilder.create().texOffs(-17, 0).mirror().addBox(0.0F, 0.0F, -26.0F, 8.0F, 0.0F, 26.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_flesh_bud_strand_r1 = left_flesh_bud.addOrReplaceChild("right_flesh_bud_strand_r1", CubeListBuilder.create().texOffs(-17, 0).addBox(-4.0F, 0.001F, -26.0F, 8.0F, 0.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.025F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition left_flesh_bud_r1 = left_flesh_bud.addOrReplaceChild("left_flesh_bud_r1", CubeListBuilder.create().texOffs(30, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(3.5F, 0.0F, -23.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition left_incomplete_bud_r1 = left_flesh_bud.addOrReplaceChild("left_incomplete_bud_r1", CubeListBuilder.create().texOffs(30, 6).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(4.5F, 0.0F, -19.0F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 48, 26);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        hair.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }

    /**Idle 1 (byte 0) = head straight, idle 2 (byte 1) = head follow*/


    public ResourceLocation getTextureLocation(Entity context, int poggers){
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/hair/vampire_3/vampire_hair_white_"+(poggers)+".png");
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (context instanceof Player PL && ((IFatePlayer) context).rdbt$getFatePowers() instanceof VampireFate vf) {
            int poggers = vf.getProgressIntoAnimation();
            if (poggers >= 16 && poggers <= 22) {
                poggers -= 16;
                hair.zScale = 2.5f;
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context,poggers)));
                root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
            }
        }
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha) {
        if (context instanceof LivingEntity LE) {

            if (context instanceof Player player && ((IFatePlayer) context).rdbt$getFatePowers() instanceof VampireFate vf) {
                int poggers = vf.getProgressIntoAnimation();
                if (poggers >= 16 && poggers <= 22) {
                    poggers -= 16;
                    this.root().getAllParts().forEach(ModelPart::resetPose);
                    if (((TimeStop) context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()) {
                        partialTicks = 0;
                    }
                    hair.zScale = 2.5f;
                    VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, poggers)));
                    //The number at the end is inversely proportional so 2 is half speed


                    Direction gravityDirection = GravityAPI.getGravityDirection(player);



                    root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
                }
            }
        }
    }

}

