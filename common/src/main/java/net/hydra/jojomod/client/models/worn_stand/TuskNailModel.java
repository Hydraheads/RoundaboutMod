package net.hydra.jojomod.client.models.worn_stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.TuskAnimations;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;


public class TuskNailModel extends PsuedoHierarchicalModel {
    public static ResourceLocation uno = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/tusk/projectiles/nail.png");
    public ResourceLocation getTextureLocation(Entity context){
        return uno;
    }

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "tusk_drill_spin"), "main");
    private final ModelPart Root;

    public TuskNailModel() {
        super(RenderType::entityTranslucent);
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition nail = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-2, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        this.Root = LayerDefinition.create(meshdefinition, 32, 32).bakeRoot();
    }



    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float ageInTicks) {}

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float alpha,int i) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            StandUser user = ((StandUser) LE);
            user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount+i*3);
            this.animate(user.roundabout$getWornStandIdleAnimation(), TuskAnimations.NAIL_FLOAT,partialTicks+i*3,1.0F);

            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            //The number at the end is inversely proportional so 2 is half speed
            float scale = 0.05F;
            r = r*(1-scale) +  (float)Math.sin(partialTicks+i) * scale;
            g = g*(1-scale) +  (float)Math.sin(partialTicks+i*2) * scale;
            b = b*(1-scale) +  (float)Math.sin(partialTicks+i*3) * scale;
            root().render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

    public void tuskNail(LivingEntity livingEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int time, int nailCount) {
        StandUser SU = (StandUser) livingEntity;
        if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {
            if (PT.getAct() > 1 && ( (nailCount == 4 && PT.getMaxActiveNails() > 5) || (PT.getMaxActiveNails() == nailCount && PT.getMaxActiveNails() <= 5)  ) ) {
                poseStack.pushPose();
                ModStrayModels.TUSK_DRILL_NAIL.render(livingEntity, partialTicks, poseStack, bufferSource, packedLight, 1,1,1, 1.0F, time);
                poseStack.popPose();
            } else {
                this.render(livingEntity, PT.getAct() == 1 ? partialTicks : 0, poseStack, bufferSource, packedLight, 1,1,1, 1.0F, time);
            }
        }
    }

    public void firstPersonTuskNail(LivingEntity livingEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int time, int nailCount) {
        StandUser SU = (StandUser) livingEntity;
        if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {
            if (PT.getAct() > 1 && ( (nailCount == 4 && PT.getMaxActiveNails() > 5) || (PT.getMaxActiveNails() == nailCount && PT.getMaxActiveNails() <= 5)  ) ) {
                boolean isLeftHand = (livingEntity.getMainArm() == HumanoidArm.LEFT && nailCount <= 5) || (livingEntity.getMainArm() == HumanoidArm.RIGHT && nailCount > 5);

                poseStack.pushPose();
                poseStack.scale(0.5F,0.5F,0.5F);
                if (isLeftHand) {
                    poseStack.translate(0.3,0.1,0); // RIGHT BACKWARD, LEFT FORWARD, U/D, L/R
                } else {
                    poseStack.translate(-0.02,0.1,0); // RIGHT BACKWARD, LEFT FORWARD, U/D, L/R
                }
                poseStack.rotateAround(new Quaternionf(0, 1, 0, nailCount > 5 ? -1 : 1), 0, 0, 0);
                ModStrayModels.TUSK_DRILL_NAIL.render(livingEntity, partialTicks, poseStack, bufferSource, packedLight, 1,1,1, 1.0F, time);
                poseStack.popPose();
            } else {
                this.render(livingEntity, PT.getAct() == 1 ? partialTicks : 0, poseStack, bufferSource, packedLight, 1,1,1, 1.0F, time);
            }
        }
    }

}