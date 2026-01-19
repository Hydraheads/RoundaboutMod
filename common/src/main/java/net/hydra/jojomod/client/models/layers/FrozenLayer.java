package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.access.AccessCreeperModel;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Zombie;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;

public class FrozenLayer<T extends LivingEntity, A extends EntityModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public FrozenLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/large_bubble.png");
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {

        boolean armsFrozen = (HeatUtil.isArmsFrozen(entity));
        if (armsFrozen){
            boolean bodyFrozen = (HeatUtil.isBodyFrozen(entity));
            boolean legsFrozen = (HeatUtil.isLegsFrozen(entity));
            //See  ClientUtil.hideArmor and ClientUtil.hideLegs
            if (!entity.isBaby()) {
                if (entity.getType() == EntityType.ZOMBIE ||
                        entity.getType() == EntityType.HUSK
                || entity.getType() == EntityType.DROWNED) {
                    if (getParentModel() instanceof ZombieModel<?> sm) {
                        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(StandIcons.ICICLE_LAYER));
                        renderPart(poseStack, sm.rightArm, consumer, packedLight);
                        renderPart(poseStack, sm.leftArm, consumer, packedLight);
                        if (legsFrozen) {
                            renderPart(poseStack, sm.leftLeg, consumer, packedLight);
                            renderPart(poseStack, sm.rightLeg, consumer, packedLight);
                        }
                        if (bodyFrozen) {
                            renderPart(poseStack, sm.body, consumer, packedLight);
                            renderPart(poseStack, sm.head, consumer, packedLight);
                        }
                    }
                } else if (entity.getType() == EntityType.SKELETON) {
                    if (getParentModel() instanceof SkeletonModel<?> sm) {
                        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(StandIcons.ICE_SKELETON_LAYER));
                        renderPart(poseStack, sm.rightArm, consumer, packedLight);
                        renderPart(poseStack, sm.leftArm, consumer, packedLight);
                        if (legsFrozen) {
                            renderPart(poseStack, sm.leftLeg, consumer, packedLight);
                            renderPart(poseStack, sm.rightLeg, consumer, packedLight);
                        }
                        if (bodyFrozen) {
                            renderPart(poseStack, sm.body, consumer, packedLight);
                            renderPart(poseStack, sm.head, consumer, packedLight);
                        }
                    }
                } else if (entity.getType() == EntityType.CREEPER) {
                    if (getParentModel() instanceof CreeperModel<?> sm) {
                        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(StandIcons.ICE_CREEPER_LAYER));
                        renderPartCreeper(poseStack, ((AccessCreeperModel)sm).roundabout$getRightFrontLeg(), consumer, packedLight);
                        renderPartCreeper(poseStack, ((AccessCreeperModel)sm).roundabout$getLeftFrontLeg(), consumer, packedLight);
                        renderPartCreeper(poseStack, ((AccessCreeperModel)sm).roundabout$getRightHindLeg(), consumer, packedLight);
                        renderPartCreeper(poseStack, ((AccessCreeperModel)sm).roundabout$getLeftHindLeg(), consumer, packedLight);
                        if (legsFrozen) {
                            renderPartCreeper(poseStack, ((AccessCreeperModel)sm).roundabout$getRoot().getChild("body"), consumer, packedLight);
                        }
                        if (bodyFrozen) {
                            renderPartCreeper(poseStack, ((AccessCreeperModel)sm).roundabout$getHead(), consumer, packedLight);
                        }
                    }
                }
            }
        }
    }


    public void renderPart(PoseStack stack, ModelPart part, VertexConsumer consumer, int packedLight){
        ClientUtil.pushPoseAndCooperate(stack, 46);
        part.visible = true;
        float xscale = part.xScale;
        float yscale = part.yScale;
        float zscale = part.zScale;

        part.xScale = xscale-0.01f;
        part.yScale = yscale-0.01f;
        part.zScale = zscale-0.01f;
        part.render(stack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1,
                1, 1, 1);
        part.xScale = xscale+0.01f;
        part.yScale = yscale+0.01f;
        part.zScale = zscale+0.01f;
        part.render(stack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1,
                1, 1, 1);
        part.xScale = xscale;
        part.yScale = yscale;
        part.zScale = zscale;
        ClientUtil.popPoseAndCooperate(stack, 46);
    }


    public void renderPartCreeper(PoseStack stack, ModelPart part, VertexConsumer consumer, int packedLight){
        ClientUtil.pushPoseAndCooperate(stack, 46);
        part.visible = true;
        float xscale = part.xScale;
        float yscale = part.yScale;
        float zscale = part.zScale;

        part.xScale = xscale-0.05f;
        part.yScale = yscale-0.05f;
        part.zScale = zscale-0.05f;
        part.y-=0.03f;
        part.render(stack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1,
                1, 1, 1);
        part.xScale = xscale+0.05f;
        part.yScale = yscale+0.05f;
        part.zScale = zscale+0.05f;
        part.render(stack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1,
                1, 1, 1);
        part.y+=0.03f;
        part.xScale = xscale;
        part.yScale = yscale;
        part.zScale = zscale;
        ClientUtil.popPoseAndCooperate(stack, 46);
    }
}
