package net.hydra.jojomod.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.RoundaboutMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import software.bernie.example.entity.BatEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

@Environment(value= EnvType.CLIENT)
public class StandEntityRenderer extends GeoEntityRenderer<StandEntity> implements GeoRenderer<StandEntity> {

    /** Ideally, all stands share 1 renderer. There is common code that they all use like fading and opacity.*/
    public StandEntityRenderer(EntityRendererFactory.Context renderManager) {

        super(renderManager, new StandModel());
    }
    @Override
    public RenderLayer getRenderType(StandEntity animatable, Identifier texture,
                                     @Nullable VertexConsumerProvider bufferSource,
                                     float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public Color getRenderColor(StandEntity animatable, float partialTick, int packedLight) {
        return Color.ofRGBA(1F,1F,1F,getStandOpacity());
    }

    public float getStandOpacity(){
        int vis = this.animatable.getFadeOut();
        int max = this.animatable.getMaxFade();
        float tot = (float) ((((float) vis/max)*1.3)-0.3);
        if (tot < 0) {
            tot=0;
        }
            return tot;
    }

    public float getStandSwimming(){
        int vis = this.animatable.getFadeOut();
        int max = this.animatable.getMaxFade();
        float tot = (float) ((((float) vis/max)*1.3)-0.3);
        if (tot < 0) {
            tot=0;
        }
        return tot;
    }

    @Override
    public void render(StandEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f,0.4f,0.4f);
        } else {
            poseStack.scale(0.87f, 0.87f, 0.87f);
        }
        //poseStack.translate();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, 255);
    }


    private final float maxRotX = 0.25F;
    private final float minRotX = 0.04F;
    private float swimRotCorrect = 0.0F;

    private int currentTick = -1;
    @Override
    public void renderFinal(MatrixStack poseStack, StandEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    //Note: To properly override, needs to copy most of the code from Geckolib.


    public static float controlledLerp(float delta, float start, float end, float multiplier) {
        return start + (delta * (end - start))*multiplier;
    }

}
