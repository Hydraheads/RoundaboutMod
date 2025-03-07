package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.shader.FogDataHolder;
import net.hydra.jojomod.client.shader.TSPostShader;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class ZLevelRenderer {

    @Shadow @Final private Minecraft minecraft;
    @Shadow
    @Nullable
    private ClientLevel level;
    @Shadow @Final private RenderBuffers renderBuffers;

    @Shadow protected abstract void renderHitOutline(PoseStack $$0, VertexConsumer $$1, Entity $$2, double $$3, double $$4, double $$5, BlockPos $$6, BlockState $$7);

    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V",ordinal = 1,shift = At.Shift.BEFORE),
            cancellable = true)
    private void roundabout$shouldOutline(PoseStack $$0, float $$1, long $$2, boolean $$3,
                                          Camera $$4, GameRenderer $$5, LightTexture $$6,
                                          Matrix4f $$7, CallbackInfo ci) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null) {
            StandUser sus = ((StandUser) player);
            StandPowers powers = sus.roundabout$getStandPowers();
            if (powers.isPiloting()) {
                StandEntity piloting = powers.getPilotingStand();
                if (piloting != null && piloting.isAlive() && !piloting.isRemoved()){
                    MultiBufferSource.BufferSource $$20 = this.renderBuffers.bufferSource();
                        if (this.minecraft.level != null) {
                            double d0 = 10;
                            HitResult $$47 = piloting.pick(d0, $$1, false);
                            if ($$47.getType() == HitResult.Type.BLOCK) {
                                BlockPos $$48 = ((BlockHitResult) $$47).getBlockPos();
                                BlockState $$49 = this.level.getBlockState($$48);
                                if (!$$49.isAir() && this.level.getWorldBorder().isWithinBounds($$48)) {
                                    Vec3 $$9 = $$4.getPosition();
                                    double $$10 = $$9.x();
                                    double $$11 = $$9.y();
                                    double $$12 = $$9.z();
                                    VertexConsumer $$50 = $$20.getBuffer(RenderType.lines());
                                    this.renderHitOutline($$0, $$50, $$4.getEntity(), $$10, $$11, $$12, $$48, $$49);
                                }
                            }
                        }

                }
            }
        }
    }

    @Inject(method="renderLevel", at=@At("TAIL"))
    private void roundabout$renderLevel(PoseStack $$0, float tickDelta, long $$2, boolean $$3, Camera $$4, GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci)
    {
        if (FogDataHolder.fogDensity > 0.25 && FogDataHolder.shouldRenderFog && minecraft.player != null && (minecraft.player.isSpectator())) {
            if (TSPostShader.FOG_SHADER != null && TSPostShader.FOG_SHADER_PASSES != null) {
                TSPostShader.setFloatUniform(TSPostShader.FOG_SHADER_PASSES, "FogDensity", FogDataHolder.fogDensity);
                TSPostShader.setFloatUniform(TSPostShader.FOG_SHADER_PASSES, "FogNear", FogDataHolder.fogNear);
                TSPostShader.setFloatUniform(TSPostShader.FOG_SHADER_PASSES, "FogFar", FogDataHolder.fogFar);
                TSPostShader.setVec3Uniform(TSPostShader.FOG_SHADER_PASSES, "FogColor", FogDataHolder.fogColor);

                TSPostShader.renderShader(TSPostShader.FOG_SHADER.get(), tickDelta);
            }
        }
    }
}
