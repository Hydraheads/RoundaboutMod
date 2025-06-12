package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.NoHitboxRendering;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.lwjgl.opengl.GL20C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class ZEntityRenderDispatcher {

    @Shadow
    @Final
    private static RenderType SHADOW_RENDER_TYPE;


    @Unique
    public boolean roundabout$recurse = false;
    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"), cancellable = true)
    protected <E extends Entity>  void roundabout$render(E $$0, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int light, CallbackInfo ci) {
        if ($$0 instanceof LivingEntity){
            ((StandUser)$$0).roundabout$tryBlip();
        }
        if ($$0 instanceof Projectile && ClientUtil.getScreenFreeze()){
            ci.cancel();
            return;
        }
        if ($$0 instanceof LivingEntity LE && !roundabout$recurse){
            byte bt =  ((StandUser)LE).roundabout$getGlow();
            if (bt > 0){
                int light2 = light;
                if (bt ==1){
                    if ($$0 instanceof Zombie || $$0 instanceof Player) {
                        light2 = Math.min(light2, 11010048);
                    } else {
                        light2 = (int)(((float)light2)/2);
                    }
                    /**
                     * Unfortunately, the light value decrease never seems to never work with ANY amount of variance, just /2
                     *
                    Roundabout.LOGGER.info("1:"+light2);
                    Roundabout.LOGGER.info("2:"+((float)light2));
                    Roundabout.LOGGER.info("3:"+(((float)light2)*0.7F));
                    Roundabout.LOGGER.info("4:"+Mth.floor(((float)light2)*0.7F));
                     **/
                } else if (bt == 2){
                    light2 = 15728880;
                }
                roundabout$recurse = true;
                render($$0,$$1,$$2,$$3,$$4,$$5,$$6,$$7,light2);
                roundabout$recurse = false;
                ci.cancel();
            }
        }
    }

    /**Cancel hitbox rendering for stuff like go beyond*/
    @Inject(method = "renderHitbox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;F)V", at = @At(value = "HEAD"), cancellable = true)
    protected static <E extends Entity>  void roundabout$renderHitbox(PoseStack $$0, VertexConsumer $$1, Entity $$2, float $$3, CallbackInfo ci) {
        if ($$2 instanceof NoHitboxRendering){
            ci.cancel();
        }
    }

        /**This is where red bind and other string-like moves will be rendered*/
    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",shift = At.Shift.BEFORE))
    protected <E extends Entity>  void roundabout$preRender(E $$0, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int $$8, CallbackInfo ci) {

        if ($$0 instanceof LivingEntity LE) {
            Entity entity = ((StandUser)$$0).roundabout$getBoundTo();
            if (entity != null) {
                ClientUtil.roundabout$renderBound(LE, $$5, $$6, $$7, entity, 0);
            }
        }
    }
    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE",
    target="Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
    shift = At.Shift.AFTER), cancellable = true)
    protected <E extends Entity>  void roundabout$renderFlame(E $$0, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int $$8, CallbackInfo ci) {
        if ($$0 instanceof LivingEntity LE){
            if (roundabout$displayFireAnimation(LE)){
                roundabout$renderFlame($$6, $$7, LE);
            }
        }
    }


    private void roundabout$renderFlame(PoseStack $$0, MultiBufferSource $$1, LivingEntity $$2) {
        byte bt = ((StandUser) $$2).roundabout$getOnStandFire();
        if (bt > 0) {
            $$0.pushPose();
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();

            $$0.mulPose(Axis.YP.rotationDegrees(-this.camera.getYRot()));

            // Rotates every 2 ticks through the 32 frames
            int frame = 0;
            if (Minecraft.getInstance().level != null ){
                frame =(int) (Minecraft.getInstance().level.getGameTime() / 2) % 32;
            }
            float $$5 = $$2.getBbWidth() * 1.4F;
            $$0.scale($$5, $$5, $$5);
            float frameHeight = 1.0F / 32;
            float $$6 = 0.5F;
            float $$7 = 0.0F;
            float $$8 = $$2.getBbHeight() / $$5;
            float $$9 = 0.0F;

            float u0 = 0.0F;
            float u1 = 1.0F;
            float v0 = frame * frameHeight;
            float v1 = (frame + 1) * frameHeight;
            float $$10 = 0.0F;
            int $$11 = 0;
            VertexConsumer vertexConsumer;

            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            if (bt == StandFireType.BLUE.id){
                vertexConsumer = $$1.getBuffer(RenderType.entityTranslucent(Roundabout.location("textures/block/stand_fire_blue_0.png")));
            } else if (bt == StandFireType.PURPLE.id){
                vertexConsumer = $$1.getBuffer(RenderType.entityTranslucent(Roundabout.location("textures/block/stand_fire_purple_0.png")));
            } else if (bt == StandFireType.GREEN.id){
                vertexConsumer = $$1.getBuffer(RenderType.entityTranslucent(Roundabout.location("textures/block/stand_fire_green_0.png")));
            } else if (bt == StandFireType.DREAD.id){
                vertexConsumer = $$1.getBuffer(RenderType.entityTranslucent(Roundabout.location("textures/block/stand_fire_dread_0.png")));
            } else if (bt == StandFireType.CREAM.id){
                vertexConsumer = $$1.getBuffer(RenderType.entityTranslucent(Roundabout.location("textures/block/stand_fire_cream_0.png")));
            } else {
                vertexConsumer = $$1.getBuffer(RenderType.entityTranslucent(Roundabout.location("textures/block/stand_fire_0.png")));
            }

            for (PoseStack.Pose matrices = $$0.last(); $$8 > 0.0F; $$11++) {

                RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
                if (bt == StandFireType.BLUE.id){
                    RenderSystem.setShaderTexture(0, Roundabout.location("textures/block/stand_fire_blue_0.png"));
                } else if (bt == StandFireType.PURPLE.id){
                    RenderSystem.setShaderTexture(0, Roundabout.location("textures/block/stand_fire_purple_0.png"));
                } else if (bt == StandFireType.GREEN.id){
                    RenderSystem.setShaderTexture(0, Roundabout.location("textures/block/stand_fire_green_0.png"));
                } else if (bt == StandFireType.DREAD.id){
                    RenderSystem.setShaderTexture(0, Roundabout.location("textures/block/stand_fire_dread_0.png"));
                } else if (bt == StandFireType.CREAM.id){
                    RenderSystem.setShaderTexture(0, Roundabout.location("textures/block/stand_fire_cream_0.png"));
                } else {
                    RenderSystem.setShaderTexture(0, Roundabout.location("textures/block/stand_fire_0.png"));
                }

                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
                fireVertex(vertexConsumer, matrices, $$6 - 0.0F, 0.0F - $$9, $$10, u0, v1);
                fireVertex(vertexConsumer, matrices,-$$6 - 0.0F, 0.0F - $$9, $$10, u1, v1);
                fireVertex(vertexConsumer, matrices, -$$6 - 0.0F, 1.4F - $$9, $$10, u1, v0);
                fireVertex(vertexConsumer, matrices, $$6 - 0.0F, 1.4F - $$9, $$10, u0, v0);

                BufferUploader.drawWithShader(bufferbuilder.end());

                $$8 -= 0.45F;
                $$9 -= 0.45F;
                $$6 *= 0.9F;
                $$10 += 0.03F;
            }

            $$0.popPose();
        }
    }

    @Unique
    public boolean roundabout$displayFireAnimation(LivingEntity LE) {
        return ((StandUser)LE).roundabout$isOnStandFire() && !LE.isSpectator();
    }
    @Shadow
    private static void renderBlockShadow(
            PoseStack.Pose $$0, VertexConsumer $$1, ChunkAccess $$2, LevelReader $$3, BlockPos $$4, double $$5, double $$6, double $$7, float $$8, float $$9
    ) {}

    @Shadow public abstract <T extends Entity> EntityRenderer<? super T> getRenderer(T $$0);


    @Shadow public Camera camera;

    @Shadow public abstract <E extends Entity> void render(E $$0, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int $$8);

    private static void fireVertex(VertexConsumer vertexConsumer, PoseStack.Pose matrices, float x, float y, float z, float u, float v) {
        vertexConsumer.vertex(matrices.pose(), x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(matrices.normal(), 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
    private static void roundabout$RenderShadow(PoseStack $$0, MultiBufferSource $$1, Entity $$2, float renderDistance, float $$4, LevelReader $$5, float shadowRadius, CallbackInfo ci) {
        if (!((IEntityAndData)$$2).roundabout$getShadow()){
            ((IEntityAndData)$$2).roundabout$setShadow(true);
            ci.cancel();
            return;
        }

        if (((TimeStop)$$2.level()).CanTimeStopEntity($$2) && $$2 instanceof LivingEntity) {
            $$4 = Minecraft.getInstance().getFrameTime();
            float $$7 = shadowRadius;
            if ($$2 instanceof Mob $$8 && $$8.isBaby()) {
                $$7 = shadowRadius * 0.5F;
            }

            double $$9 = Mth.lerp((double)$$4, ((IEntityAndData)$$2).roundabout$getRoundaboutPrevX(), $$2.getX());
            double $$10 = Mth.lerp((double)$$4, ((IEntityAndData)$$2).roundabout$getRoundaboutPrevY(), $$2.getY());
            double $$11 = Mth.lerp((double)$$4, ((IEntityAndData)$$2).roundabout$getRoundaboutPrevZ(), $$2.getZ());
            float $$12 = Math.min(renderDistance / 0.5F, $$7);
            int $$13 = Mth.floor($$9 - (double) $$7);
            int $$14 = Mth.floor($$9 + (double) $$7);
            int $$15 = Mth.floor($$10 - (double) $$12);

            int $$16 = Mth.floor($$10);
            int $$17 = Mth.floor($$11 - (double) $$7);
            int $$18 = Mth.floor($$11 + (double) $$7);
            PoseStack.Pose $$19 = $$0.last();
            VertexConsumer $$20 = $$1.getBuffer(SHADOW_RENDER_TYPE);
            BlockPos.MutableBlockPos $$21 = new BlockPos.MutableBlockPos();

            for (int $$22 = $$17; $$22 <= $$18; $$22++) {
                for (int $$23 = $$13; $$23 <= $$14; $$23++) {
                    $$21.set($$23, 0, $$22);
                    ChunkAccess $$24 = $$5.getChunk($$21);

                    for (int $$25 = $$15; $$25 <= $$16; $$25++) {
                        $$21.setY($$25);
                        float $$26 = renderDistance - (float) ($$10 - (double) $$2.getY()) * 0.5F;
                        renderBlockShadow($$19, $$20, $$24, $$5, $$21, $$9, $$10, $$11, $$7, $$26);
                    }
                }
            }
            ci.cancel();
        }
    }

    /**

    BlockHitResult HX = $$2.level().clip(new ClipContext($$2.position(), $$2.position().add(new Vec3(0,-10,0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, $$2));
            if (HX.getType() != HitResult.Type.BLOCK){
        $$10 = HX.getLocation().y;
    }
   */
}
