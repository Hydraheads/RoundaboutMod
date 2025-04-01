package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.entity.client.PreRenderEntity;
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
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void roundabout$renderEntity(Entity $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6, CallbackInfo ci){
        if ($$0 instanceof PreRenderEntity pre){
            if (pre.preRender($$0,$$1,$$2,$$3,$$4,$$5,$$6)){
                ci.cancel();
            }
        }
    }
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

    // test the shader
//    @Inject(method = "renderLevel", at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Lighting;setupLevel(Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER, ordinal = 0))
//    private void roundabout$renderLevel(PoseStack $$0, float $$1, long $$2, boolean $$3, Camera $$4, GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci)
//    {
//        if (RCoreShader.roundabout$timestopProgram == null)
//            return;
//
//        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
//        RenderSystem.setShader(()-> RCoreShader.roundabout$timestopProgram);
//        RenderSystem.depthFunc(GL20C.GL_ALWAYS);
//        RenderSystem.depthMask(false);
//        RenderSystem.enableBlend();
//
//        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
//        bufferbuilder.vertex(-0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0, 0).endVertex();
//        bufferbuilder.vertex(0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0, 1).endVertex();
//        bufferbuilder.vertex(0.5F,  0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1, 1).endVertex();
//        bufferbuilder.vertex(-0.5F,  0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1, 0).endVertex();
//        BufferUploader.drawWithShader(bufferbuilder.end());
//
//        RenderSystem.disableBlend();
//        RenderSystem.depthMask(true);
//        RenderSystem.depthFunc(GL20C.GL_LEQUAL);
//    }
}
