package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;

public class StandRenderer<T extends StandEntity> extends MobEntityRenderer<T, StandModel<T>> {
    public StandRenderer(EntityRendererFactory.Context context, StandModel<T> entityModel, float f) {
        super(context, entityModel, f);
    }


    private final float maxRotX = 0.25F;
    private final float minRotX = 0.04F;
    private float swimRotCorrect = 0.0F;

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }

    public void rotateHead(T mobEntity,  ModelPart head, float tickDelta){

        var animationNumber = mobEntity.getOffsetType();
        var animationStyle = OffsetIndex.OffsetStyle(animationNumber);
            float rotX = mobEntity.getHeadRotationX();
            float rotY = mobEntity.getHeadRotationY();
            if (animationStyle == OffsetIndex.FOLLOW_STYLE){
                /*This code makes the head of the model turn towards swim rotation while swimming*/
                if (mobEntity.isSwimming() || mobEntity.isCrawling() || mobEntity.isFallFlying()) {
                    if (swimRotCorrect > -45) {
                        swimRotCorrect -= 2;
                        swimRotCorrect = Math.min(swimRotCorrect, -45);
                    }
                } else {
                    if (swimRotCorrect < 0) {
                        swimRotCorrect += 2;
                        swimRotCorrect = Math.max(swimRotCorrect, 0);
                    }
                }
                rotX = ((mobEntity.getMaster().getPitch(tickDelta)%360) - swimRotCorrect) * MathHelper.RADIANS_PER_DEGREE;
                rotY = ((MathHelper.lerpAngleDegrees(tickDelta, mobEntity.getMaster().prevHeadYaw, mobEntity.getMaster().headYaw)%360)
                                - (MathHelper.lerpAngleDegrees(tickDelta, mobEntity.getMaster().prevBodyYaw, mobEntity.getMaster().bodyYaw)%360))
                        * MathHelper.RADIANS_PER_DEGREE;

            } else if (animationStyle == OffsetIndex.FIXED_STYLE){
                rotX = 0;
                rotY = (float) -(mobEntity.getPunchYaw(mobEntity.getAnchorPlace(),
                        0.36) * MathHelper.RADIANS_PER_DEGREE);
            }
            mobEntity.setHeadRotationX(rotX);
            mobEntity.setHeadRotationY(rotY);
            this.model.setHeadRotations(rotX,rotY);
    } public void rotateStand(T mobEntity,  ModelPart stand, float tickDelta){
        var animationNumber = mobEntity.getOffsetType();
        var animationStyle = OffsetIndex.OffsetStyle(animationNumber);

        float rotX = mobEntity.getStandRotationX();
        float rotY = mobEntity.getStandRotationY();
        float cRX = 0;
        float cRY = 0;
        if (animationStyle == OffsetIndex.FIXED_STYLE){
            cRX = (mobEntity.getMaster().getPitch(tickDelta)%360) * MathHelper.RADIANS_PER_DEGREE;
        }
        rotX = MainUtil.controlledLerp(tickDelta, rotX, cRX, 0.8f);
        rotY = MainUtil.controlledLerp(tickDelta, rotY, cRY, 0.8f);
        mobEntity.setStandRotationX(rotX);
        mobEntity.setStandRotationY(rotY);
        this.model.setStandRotations(rotX,rotY);

    } public void rotateBody(T mobEntity,  ModelPart body, float tickDelta){
        var animationNumber = mobEntity.getOffsetType();
        var animationStyle = OffsetIndex.OffsetStyle(animationNumber);

        float rotX = mobEntity.getBodyRotationX();
        float rotY = mobEntity.getBodyRotationY();
        if (animationStyle == OffsetIndex.FOLLOW_STYLE){
            float cRot = maxRotX;

            if (mobEntity.isSwimming() || mobEntity.isFallFlying()) {
                cRot = ((mobEntity.getMaster().getPitch(tickDelta)%360) + 90) * MathHelper.RADIANS_PER_DEGREE;
            } else if (mobEntity.isCrawling()) {
                cRot = 90 * MathHelper.RADIANS_PER_DEGREE;
            } else {
                int moveForward = mobEntity.getMoveForward();
                if (moveForward < 0) {
                    cRot *= -moveForward;
                } else if (moveForward > 0) {
                    cRot *= -moveForward;
                } else {
                    cRot = 0;
                }
                cRot *= -0.6F;
            }
            rotX = MainUtil.controlledLerp(tickDelta, rotX, cRot, 0.15f);
            rotY = MainUtil.controlledLerp(tickDelta, rotY, 0, 0.8f);
        } else if (animationStyle == OffsetIndex.FIXED_STYLE) {
            rotX = MainUtil.controlledLerp(tickDelta, rotX, 0, 0.8f);
            rotY = MainUtil.controlledLerp(tickDelta, rotY, (float) -(mobEntity.getPunchYaw(mobEntity.getAnchorPlace(),
                    0.36) * MathHelper.RADIANS_PER_DEGREE), 0.8f);
        }
        mobEntity.setBodyRotationX(rotX);
        mobEntity.setBodyRotationY(rotY);
        this.model.setBodyRotations(rotX,rotY);
    }

    @Override
    public void render(T mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        (this.model).setAlpha(getStandOpacity(mobEntity));

        if (mobEntity.getMaster() != null) {
            if ((this.model).getPart() != null) {
                rotateStand(mobEntity, (this.model).getPart(), g);
                if ((this.model).getPart().hasChild("stand2")) {

                    if ((this.model).getPart().getChild("stand2").hasChild("head")) {
                        rotateHead(mobEntity, (this.model).getPart().getChild("stand2").getChild("head"), g);
                    }

                    if ((this.model).getPart().getChild("stand2").hasChild("body")) {
                        rotateBody(mobEntity, (this.model).getPart().getChild("stand2").getChild("body"), g);
                    }

                }
            }
        }

        int plight = i;
        var owner = mobEntity.getMaster();
        if (owner != null) {
            int tlight = getTrueLight(owner,g);
            if (plight < tlight && plight < 1){
                plight = tlight;
            }
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, plight);
    }
    public final int getTrueLight(Entity entity, float tickDelta) {
        BlockPos blockPos = BlockPos.ofFloored(entity.getClientCameraPosVec(tickDelta));
        return LightmapTextureManager.pack(this.getTrueBlockLight(entity, blockPos), this.getTrueSkyLight(entity, blockPos));
    }

    protected int getTrueSkyLight(Entity entity, BlockPos pos) {
        return entity.getWorld().getLightLevel(LightType.SKY, pos);
    }

    protected int getTrueBlockLight(Entity entity, BlockPos pos) {
        if (entity.isOnFire()) {
            return 15;
        }
        return entity.getWorld().getLightLevel(LightType.BLOCK, pos);
    }

    public float getStandOpacity(T entity){
        if (!entity.hasMaster()) {
            return 1;
        } else {
            int vis = entity.getFadeOut();
            int max = entity.getMaxFade();
            float tot = (float) ((((float) vis / max) * 1.3) - 0.3);
            if (tot < 0) {
                tot = 0;
            }
            return tot;
        }
    }
}
