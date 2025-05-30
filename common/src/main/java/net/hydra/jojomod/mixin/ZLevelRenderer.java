package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.shader.callback.RenderCallbackRegistry;
import net.hydra.jojomod.entity.client.PreRenderEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20C;
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
public abstract class ZLevelRenderer {

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

    @Inject(method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void roundabout$renderEntity(Entity $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6, CallbackInfo ci) {
        if ($$0 != null){
            ((IEntityAndData)$$0).roundabout$setExclusiveLayers(true);
        }

        if ($$0 instanceof PreRenderEntity pre) {
            if (pre.preRender($$0, $$1, $$2, $$3, $$4, $$5, $$6)) {
                ci.cancel();
                ((IEntityAndData)$$0).roundabout$setExclusiveLayers(false);
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
}
