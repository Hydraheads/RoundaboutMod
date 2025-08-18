package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.ILevelRenderer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.SavedSecond;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Pose;
import net.zetalasis.client.shader.RPostShaderRegistry;
import net.zetalasis.client.shader.callback.RenderCallbackRegistry;
import net.hydra.jojomod.client.models.layers.PreRenderEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class ZLevelRenderer implements ILevelRenderer {

    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Nullable
    private ClientLevel level;
    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Shadow
    protected abstract void renderHitOutline(PoseStack $$0, VertexConsumer $$1, Entity $$2, double $$3, double $$4, double $$5, BlockPos $$6, BlockState $$7);

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow
    @Nullable
    public abstract RenderTarget entityTarget();

    @Shadow protected abstract void renderEntity(Entity $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6);

    @Shadow private boolean captureFrustum;
    @Shadow @Nullable private Frustum capturedFrustum;
    @Shadow @Final private Vector3d frustumPos;
    @Shadow private Frustum cullingFrustum;

    @Shadow protected abstract boolean shouldShowEntityOutlines();

    @Shadow public abstract boolean isChunkCompiled(BlockPos $$0);

    @Shadow @Nullable private ViewArea viewArea;

    @Override
    @Unique
    public ViewArea roundabout$getViewArea(){
        return viewArea;
    }
    @Unique
    public boolean roundabout$recurse = false;
    @Inject(method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void roundabout$renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float partialTick, PoseStack stack, MultiBufferSource buffer, CallbackInfo ci) {

        if (entity != null){
            IEntityAndData entityAndData = ((IEntityAndData)entity);
            entityAndData.roundabout$setExclusiveLayers(true);
        }

        if (!roundabout$recurse) {
            if (entity instanceof PreRenderEntity pre) {
                if (pre.preRender(entity, cameraX, cameraY, cameraZ, partialTick, stack, buffer)) {
                    ci.cancel();
                    ((IEntityAndData) entity).roundabout$setExclusiveLayers(false);
                }
            }
        }
    }
    @Inject(method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "TAIL"))
    private void roundabout$renderEntityEnd(Entity $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6, CallbackInfo ci) {
        if ($$0 != null){
            ((IEntityAndData)$$0).roundabout$setExclusiveLayers(false);
        }
    }

    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V", ordinal = 1, shift = At.Shift.BEFORE),
            cancellable = true)
    private void roundabout$shouldOutline(PoseStack poseStack, float $$1, long $$2, boolean $$3,
                                          Camera camera, GameRenderer gameRenderer, LightTexture $$6,
                                          Matrix4f matrix4f, CallbackInfo ci) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null) {
            StandUser sus = ((StandUser) player);
            StandPowers powers = sus.roundabout$getStandPowers();
            if (powers.isPiloting()) {
                StandEntity piloting = powers.getPilotingStand();
                if (piloting != null && piloting.isAlive() && !piloting.isRemoved()) {
                    MultiBufferSource.BufferSource $$20 = this.renderBuffers.bufferSource();
                    if (this.minecraft.level != null) {
                        double d0 = 10;
                        HitResult $$47 = piloting.pick(d0, $$1, false);
                        if ($$47.getType() == HitResult.Type.BLOCK) {
                            BlockPos $$48 = ((BlockHitResult) $$47).getBlockPos();
                            BlockState $$49 = this.level.getBlockState($$48);
                            if (!$$49.isAir() && this.level.getWorldBorder().isWithinBounds($$48)) {
                                Vec3 $$9 = camera.getPosition();
                                double $$10 = $$9.x();
                                double $$11 = $$9.y();
                                double $$12 = $$9.z();
                                VertexConsumer $$50 = $$20.getBuffer(RenderType.lines());
                                this.renderHitOutline(poseStack, $$50, camera.getEntity(), $$10, $$11, $$12, $$48, $$49);
                            }
                        }
                    }
                }
            }
        }
    }



    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(value = "INVOKE",target="Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V",ordinal=0,shift = At.Shift.BEFORE))
    private void roundabout$renderLevelGhostEntities(PoseStack $$0, float $$1, long $$2, boolean $$3,
                                            Camera $$4, GameRenderer $$5, LightTexture $$6,
                                            Matrix4f $$7, CallbackInfo ci) {


        /**Render phantom mandom images
        if (ClientUtil.checkIfClientCanSeePastLocations()) {

            Vec3 $$9 = $$4.getPosition();
            double $$10 = $$9.x();
            double $$11 = $$9.y();
            double $$12 = $$9.z();


            boolean $$14 = this.capturedFrustum != null;
            Frustum $$15;
            if ($$14) {
                $$15 = this.capturedFrustum;
                $$15.prepare(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z);
            } else {
                $$15 = this.cullingFrustum;
            }
            boolean $$19 = false;
            MultiBufferSource.BufferSource $$20 = this.renderBuffers.bufferSource();
            for (Entity entity : this.level.entitiesForRendering()) {
                if (this.entityRenderDispatcher.shouldRender(entity, $$15, $$10, $$11, $$12) || entity.hasIndirectPassenger(this.minecraft.player)) {
                    BlockPos $$22 = entity.blockPosition();
                    if ((this.level.isOutsideBuildHeight($$22.getY()) || this.isChunkCompiled($$22))) {
                        if (entity.tickCount == 0) {
                            entity.xOld = entity.getX();
                            entity.yOld = entity.getY();
                            entity.zOld = entity.getZ();
                        }

                        MultiBufferSource $$24;
                            $$24 = $$20;

                        if (entity != null) {
                            ArrayDeque<SavedSecond> secondQue = ((IEntityAndData) entity).roundabout$getSecondQue();
                            if (secondQue != null && !secondQue.isEmpty()) {
                                SavedSecond sec = secondQue.getLast();

                                float xrot = entity.getXRot();
                                float yrot = entity.getYRot();
                                float headrot = entity.getYHeadRot();
                                double x = entity.getX();
                                double y = entity.getY();
                                double z = entity.getZ();
                                double xOld = entity.xOld;
                                double yOld = entity.yOld;
                                double zOld = entity.zOld;
                                Pose pose = entity.getPose();
                                Vec3 deltamov = entity.getDeltaMovement();
                                float walkdistance = entity.walkDist;

                                entity.setPosRaw(sec.position.x,sec.position.y,sec.position.z);
                                entity.xOld = sec.position.x;
                                entity.yOld = sec.position.y;
                                entity.zOld = sec.position.z;
                                entity.setXRot(sec.rotationVec.x);
                                entity.setYRot(sec.rotationVec.y);
                                entity.setYHeadRot(sec.headYRotation);
                                entity.setPose(Pose.STANDING);
                                ((IEntityAndData)entity).roundabout$setDeltaMovementRaw(Vec3.ZERO);
                                entity.walkDist = 0;

                                float bodyRot = 0;
                                if (entity instanceof LivingEntity LE){
                                    bodyRot = LE.yBodyRot;
                                    entity.setYBodyRot(sec.rotationVec.y);
                                }

                                ClientUtil.isMakingRenderingSeeThrough = true;
                                ClientUtil.lastRenderedMobTickCount = entity.tickCount;
                                renderEntity(entity, $$10,$$11,$$12, 1, $$0, $$24);
                                ClientUtil.isMakingRenderingSeeThrough = false;


                                entity.setPose(pose);
                                entity.setPosRaw(x, y, z);
                                entity.xOld = xOld;
                                entity.yOld = yOld;
                                entity.zOld = zOld;
                                entity.setYRot(yrot);
                                entity.setXRot(xrot);
                                entity.setYHeadRot(headrot);
                                ((IEntityAndData)entity).roundabout$setDeltaMovementRaw(deltamov);
                                entity.walkDist = walkdistance;
                                if (entity instanceof LivingEntity LE){
                                    entity.setYBodyRot(bodyRot);
                                }
                            }
                        }
                    }
                }
            }
        }
         */
    }

    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(value = "HEAD"))
    private void roundabout$renderLevelHead(PoseStack $$0, float $$1, long $$2, boolean $$3,
                                            Camera $$4, GameRenderer $$5, LightTexture $$6,
                                            Matrix4f $$7, CallbackInfo ci) {
        ClientUtil.mirrorCycles = 0;
    }


    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(value = "TAIL"))
    private void roundabout$renderLevel(PoseStack $$0, float partialTick, long $$2, boolean $$3,
                                        Camera $$4, GameRenderer $$5, LightTexture $$6,
                                        Matrix4f $$7, CallbackInfo ci) {
        RenderCallbackRegistry.roundabout$LEVEL_RENDER_FINISH(partialTick);

        Player player = Minecraft.getInstance().player;
        if (player != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            Vec3 $$9 = $$4.getPosition();
            double $$10 = $$9.x();
            double $$11 = $$9.y();
            double $$12 = $$9.z();
            MultiBufferSource.BufferSource $$20 = this.renderBuffers.bufferSource();
            this.roundabout$renderStringOnPlayer(player, $$10, $$11, $$12, partialTick, $$0, (MultiBufferSource) $$20);
        }
    }

    @Unique
    public void roundabout$renderStringOnPlayer(Player $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6) {

        Entity entity = ((StandUser) $$0).roundabout$getBoundTo();
        if (entity != null) {
            ClientUtil.roundabout$renderBound($$0, $$4, $$5, $$6, entity, 1.93F);
        }

    }
    /**This is also where red bind and other string-like moves will be rendered*/

    @Inject(method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;", at=@At("HEAD"), cancellable = true)
    private void roundabout$addParticle(ParticleOptions $$0, boolean $$1, boolean $$2, double $$3, double $$4, double $$5, double $$6, double $$7, double $$8, CallbackInfoReturnable<Particle> cir)
    {
        if (Minecraft.getInstance().player == null)
            return;

        if (((StandUser)Minecraft.getInstance().player).roundabout$isParallelRunning())
        {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }

    @Inject(method = "renderChunkLayer", at = @At("HEAD"))
    private void roundabout$renderChunkLayer(RenderType renderType, PoseStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix, CallbackInfo ci)
    {
        if (renderType == RenderType.solid())
        {
            RPostShaderRegistry.InverseProjectionMatrix.identity();
            RPostShaderRegistry.InverseProjectionMatrix.mul(positionMatrix);
            RPostShaderRegistry.InverseProjectionMatrix.mul(matrices.last().pose());
            RPostShaderRegistry.InverseProjectionMatrix.invert();
        }
    }
}
