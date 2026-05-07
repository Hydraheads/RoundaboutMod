package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class VampireHairOnePart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart hair;
    private final ModelPart Root;

    public VampireHairOnePart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.hair = Root.getChild("hair");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition hair_left_r1 = hair.addOrReplaceChild("hair_left_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -10.0F, 0.0F, 9.0F, 10.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition hair_left_r2 = hair.addOrReplaceChild("hair_left_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -13.0F, 0.0F, 9.0F, 13.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition hair_right_r1 = hair.addOrReplaceChild("hair_right_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, 0.0F, 9.0F, 10.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition hair_right_r2 = hair.addOrReplaceChild("hair_right_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -13.0F, 0.0F, 9.0F, 13.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition hair_back_r1 = hair.addOrReplaceChild("hair_back_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -12.0F, 0.0F, 9.0F, 13.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 18, 13);
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


    public ResourceLocation getTextureLocation(Entity context){
        int poggers = 0;
        //if (context instanceof Player PL && ((IFatePlayer)context).rdbt$getFatePowers() instanceof VampireFate vf){
        //    poggers = vf.getProgressIntoAnimation();
        //} else {
            poggers = context.tickCount;
        //}
        int finale = Mth.floor((((float)poggers)/2))%7;
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/hair/vampire_2/vampire_hair_white_"+(finale)+".png");
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

