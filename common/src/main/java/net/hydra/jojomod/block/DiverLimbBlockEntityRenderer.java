package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.renderers.DiverDownBaseRenderer;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;

public class DiverLimbBlockEntityRenderer implements BlockEntityRenderer<DiverLimbBlockEntity> {

    private record DiverLimbs(ModelPart rightArm, ModelPart leftArm, ModelPart rightLeg, ModelPart leftLeg) {
        public ModelPart getLimb(int index) {
            return switch (index) {
                case 0 -> rightArm;
                case 1 -> leftArm;
                case 2 -> rightLeg;
                case 3 -> leftLeg;
                default -> rightArm;
            };
        }
    }

    private final Map<Byte, DiverLimbs> skinLimbs = new HashMap<>();
    private final DiverLimbs defaultLimbs;

    public DiverLimbBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
        this.defaultLimbs = bakeLimbs(context, ModEntityRendererClient.DIVER_DOWN_LAYER);
        this.skinLimbs.put((byte) 0, this.defaultLimbs);
        this.skinLimbs.put(DiverDownEntity.BETA_DIVER, bakeLimbs(context, ModEntityRendererClient.DIVER_DOWN_BETA_LAYER));
        this.skinLimbs.put(DiverDownEntity.WORLD_DIVER, bakeLimbs(context, ModEntityRendererClient.DIVER_DOWN_WORLD_LAYER));
        // for the future, when adding more models, follow this template:
        // this.skinLimbs.put(DiverDownEntity.MODEL_NAME, bakeLimbs(context, ModEntityRendererClient.MODEL_NAME_LAYER));
    }

    private static DiverLimbs bakeLimbs(BlockEntityRendererProvider.Context context, ModelLayerLocation layer) {
        //gets the default diver down model
        ModelPart root = context.bakeLayer(layer);
        //gets the chest for the arms
        ModelPart chest = root.getChild("stand").getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest");
        //arms
        ModelPart rArm = chest.getChild("right_arm");
        ModelPart lArm = chest.getChild("left_arm");
        //gets the legs portion for left and right leg
        ModelPart legs = root.getChild("stand").getChild("stand2").getChild("body").getChild("body2").getChild("legs");
        //left and right legs
        ModelPart rLeg = legs.getChild("right_leg");
        ModelPart lLeg = legs.getChild("left_leg");
        // center each limb
        rArm.setPos(2.0F, -7.0F, 0.0F);
        lArm.setPos(-2.0F, -7.0F, 0.0F);
        rLeg.setPos(0.0F, -9.0F, 0.0F);
        lLeg.setPos(0.0F, -9.0F, 0.0F);

        return new DiverLimbs(rArm, lArm, rLeg, lLeg);
    }

    public int limbCount = 4;

    public void render(DiverLimbBlockEntity DiverLimbBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        //only renders the limbs if the client can see stands
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            //omg push and pop queues hiiii!!!!
            //needed to revert all poses back to normal later since the limbs are gonna be rendred in a bunch of different directions
            //roundabout has its own push and pop queue for posing and debugging, so i'm using that instead of poseStack.pushPose();
            ClientUtil.pushPoseAndCooperate(poseStack,7);

            // Duration in ticks (lower = faster)
            float emergeTicks = 3.0F;
            float progress = Mth.clamp((DiverLimbBlockEntity.clientAge + partialTick) / emergeTicks, 0.0F, 1.0F);

            // Change this variable if the limb start too deep into a wall which causes it to bleed into other walls
            float startDepth = 0.3F;
            float depth = (1.0F - progress) * startDepth;
            Direction wallDir = DiverLimbBlockEntity.facing != null ? DiverLimbBlockEntity.facing : Direction.NORTH;
            double offsetX = wallDir.getStepX() * depth;
            double offsetY = wallDir.getStepY() * depth;
            double offsetZ = wallDir.getStepZ() * depth;

            // Position at block center + emergence offset
            poseStack.translate(0.5D + offsetX, 0.5D + offsetY, 0.5D + offsetZ);

            // Orient the texture
            // Diver Down specific: need to have the limb face the right way based on the directions found in DiverLimbBlockEntity
            if (DiverLimbBlockEntity.facing != null) {
                switch (DiverLimbBlockEntity.facing) {
                    case NORTH -> {
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    }
                    case EAST -> {
                        poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    }
                    case SOUTH -> {
                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    }
                    case WEST -> {
                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    }
                    case DOWN -> {
                        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                    }
                    default -> {} // UP is 0. Ultimately useless since the move can't be placed on the ceiling.
                }
            }

            // Flip Y and Z because notch doesn't know basic math
            poseStack.scale(1.0F, -1.0F, -1.0F);
            // Get the stand skin
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(DiverDownBaseRenderer.getSkin(DiverLimbBlockEntity.standSkin)));

            // Select the limb model according to limbIndex
            DiverLimbs limbs = this.skinLimbs.getOrDefault(DiverLimbBlockEntity.standSkin, this.defaultLimbs);
            ModelPart limb = limbs.getLimb(DiverLimbBlockEntity.limbIndex);

            // Render the limbs
            limb.render(poseStack, vertexConsumer, packedLight, packedOverlay);

            //returns every pose change back to normal
            ClientUtil.popPoseAndCooperate(poseStack,6);
        }
    }
}