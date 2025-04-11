package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.client.PreRenderEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
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

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

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

    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(value = "TAIL"))
    private void roundabout$renderLevel(PoseStack $$0, float $$1, long $$2, boolean $$3,
                                          Camera $$4, GameRenderer $$5, LightTexture $$6,
                                          Matrix4f $$7, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            Vec3 $$9 = $$4.getPosition();
            double $$10 = $$9.x();
            double $$11 = $$9.y();
            double $$12 = $$9.z();
            MultiBufferSource.BufferSource $$20 = this.renderBuffers.bufferSource();
            this.roundabout$renderStringOnPlayer(player, $$10, $$11, $$12, $$1, $$0, (MultiBufferSource)$$20);
        }
    }

    @Unique
    public void roundabout$renderStringOnPlayer(Player $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6){

        Entity entity = ((StandUser)$$0).roundabout$getBoundTo();
        if (entity != null) {
            roundabout$renderBound($$0, $$4, $$5, $$6,entity);
        }

    }
    private void roundabout$renderBound(Player victim, float p_115463_, PoseStack p_115464_, MultiBufferSource mb, Entity binder) {
        p_115464_.pushPose();
        Vec3 vec3 = binder.getRopeHoldPosition(p_115463_);
        if (binder instanceof LivingEntity lv){
            StandUser su = ((StandUser)lv);
            StandEntity stand = su.roundabout$getStand();
            if (stand != null){
                vec3 = stand.getRopeHoldPosition(p_115463_);
            }
        }
        double d0 = (double)(Mth.lerp(p_115463_, victim.yBodyRotO, victim.yBodyRot) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = victim.getLeashOffset(p_115463_);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp((double)p_115463_, victim.xo, victim.getX()) + d1;
        double d4 = Mth.lerp((double)p_115463_, victim.yo, victim.getY()) + vec31.y; //+3
        double d5 = Mth.lerp((double)p_115463_, victim.zo, victim.getZ()) + d2;
        p_115464_.translate(d1, vec31.y, d2);
        float f = (float)(vec3.x - d3);
        float f1 = (float)(vec3.y - d4);
        float f2 = (float)(vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = mb.getBuffer(RenderType.leash());
        Matrix4f matrix4f = p_115464_.last().pose();
        float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = BlockPos.containing(victim.getEyePosition(p_115463_));
        BlockPos blockpos1 = BlockPos.containing(binder.getEyePosition(p_115463_));
        int i = this.roundabout$getBlockLightLevel(victim, blockpos);
        int j = roundabout$getBlockLightLevel(binder, blockpos1);
        int k = victim.level().getBrightness(LightLayer.SKY, blockpos);
        int l = victim.level().getBrightness(LightLayer.SKY, blockpos1);

        for(int i1 = 0; i1 <= 24; ++i1) {
            roundabout$addVertexPair(binder, vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            roundabout$addVertexPair(binder, vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
        }

        p_115464_.popPose();
    }
    @Unique
    protected int roundabout$getBlockLightLevel(Entity $$0, BlockPos $$1) {
        return $$0.isOnFire() ? 15 : $$0.level().getBrightness(LightLayer.BLOCK, $$1);
    }

    private static void roundabout$addVertexPair(Entity binder, VertexConsumer p_174308_, Matrix4f p_254405_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
        float f = (float)p_174321_ / 24.0F;
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        int tc = binder.tickCount % 9;
        float f1 = 0.7F + (float)(Math.random()*0.3);
        if (tc > 5) {
            f1*=0.92F;
        }
        if (tc > 2) {
            f1*=0.84F;
        }
        Vec3 color = roundabout$getBindColor(binder);
        float f2 = (float) (color.x() * f1);
        float f3 = (float) (color.y() * f1);
        float f4 = (float) (color.z() * f1);
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        float width = 0.05F;
        p_174308_.vertex(p_254405_, f5 - p_174319_ - width, f6 + p_174318_ + width - 1.93F, f7 + p_174320_ + width).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        p_174308_.vertex(p_254405_, f5 + p_174319_ + width, f6 + p_174317_ - p_174318_ - width - 1.93F, f7 - p_174320_ - width).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    @Unique
    private static Vec3 roundabout$getBindColor(Entity binder) {
        if (binder instanceof LivingEntity LE && ((StandUser)binder).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return new Vec3(0,0.8F,0.949F);
            } else if (sft == StandFireType.PURPLE.id){
                return new Vec3(0.839F,0.122F,0.949F);
            } else if (sft == StandFireType.GREEN.id){
                return new Vec3(0.345F,1F,0.2F);
            } else if (sft == StandFireType.DREAD.id){
                return new Vec3(0.788F,0,0);
            } else if (sft == StandFireType.CREAM.id){
                return new Vec3(0.949F,0.945F,0.718F);
            }
        }
        return new Vec3(0.969F,0.569F,0.102F);
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
