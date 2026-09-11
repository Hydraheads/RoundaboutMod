package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class DiverLimbBlockEntityRenderer implements BlockEntityRenderer<DiverLimbBlockEntity> {
    private static final ResourceLocation PART_6 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/base.png");
    private static final ResourceLocation LAVA_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/lavadiver.png");
    private static final ResourceLocation RED_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/reddiver.png");
    private static final ResourceLocation ORANGE_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/orangediver.png");
    private static final ResourceLocation TREASURE_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/treasurediver.png");
    private static final ResourceLocation BIRTHDAY_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/birthdaydiver.png");
    private static final ResourceLocation FIRE_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/firedown.png");

    //for all the limbs
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    //TEMPORARILY rendering soft and wet bubbles to make sure that the move works. can update with limbs later.
    public DiverLimbBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
        //gets the diver down model
        ModelPart root = context.bakeLayer(ModEntityRendererClient.DIVER_DOWN_LAYER);
        //gets the chest for the arms
        ModelPart chest = root.getChild("stand").getChild("stand2").getChild("body").getChild("body2").getChild("torso").getChild("upper_chest");
        //arms
        this.rightArm = chest.getChild("right_arm");
        this.leftArm = chest.getChild("left_arm");
        //gets the legs portion for left and right leg
        ModelPart legs = root.getChild("stand").getChild("stand2").getChild("body").getChild("body2").getChild("legs");
        //left and right legs
        this.rightLeg = legs.getChild("right_leg");
        this.leftLeg = legs.getChild("left_leg");
        // center each limb
        this.rightArm.setPos(0.0F, -7.0F, 0.0F);
        this.leftArm.setPos(0.0F, -7.0F, 0.0F);
        this.rightLeg.setPos(0.0F, -9.0F, 0.0F);
        this.leftLeg.setPos(0.0F, -9.0F, 0.0F);
    }
    public int limbCount = 4;

    public void render(DiverLimbBlockEntity DiverLimbBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        //only renders the limbs if the client can see stands
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            //can repurpose tihs code later for when diving ripple effect is added
            /*if (((TimeStop)DiverLimbBlockEntity.getLevel()).inTimeStopRange(DiverLimbBlockEntity.getBlockPos())){
                partialTick = 0;
            }*/

            //omg push and pop queues hiiii!!!!
            //needed to revert all poses back to normal later since the limbs are gonna be rendred in a bunch of different directions
            //roundabout has it's own push and pop queue for posing and debugging, so i'm using that instead of poseStack.pushPose();
            ClientUtil.pushPoseAndCooperate(poseStack,7);

            //puts the limbs at the center of the block
            poseStack.translate(0.5D,0.5D,0.5D);

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
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getSkinLocation(DiverLimbBlockEntity.standSkin)));

            // Select the limb model according to limbIndex
            ModelPart limb = switch (DiverLimbBlockEntity.limbIndex) {
                case 0 -> this.rightArm;
                case 1 -> this.leftArm;
                case 2 -> this.rightLeg;
                case 3 -> this.leftLeg;
                default -> this.rightArm;
            };

            // Render the limbs
            limb.render(poseStack, vertexConsumer, packedLight, packedOverlay);
            
            //returns every pose change back to normal
            ClientUtil.popPoseAndCooperate(poseStack,6);
        }
    }


    public ResourceLocation getSkinLocation(byte skin) {
        return switch (skin) {
            case DiverDownEntity.LAVA_DIVER -> LAVA_DIVER;
            case DiverDownEntity.RED_DIVER -> RED_DIVER;
            case DiverDownEntity.ORANGE_DIVER -> ORANGE_DIVER;
            case DiverDownEntity.TREASURE_DIVER -> TREASURE_DIVER;
            case DiverDownEntity.BIRTHDAY_DIVER -> BIRTHDAY_DIVER;
            case DiverDownEntity.FIRE_DIVER -> FIRE_DIVER;
            default -> PART_6;
        };
    }
}