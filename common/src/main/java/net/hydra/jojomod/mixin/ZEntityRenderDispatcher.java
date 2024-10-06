package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class ZEntityRenderDispatcher {

    @Shadow
    @Final
    private static RenderType SHADOW_RENDER_TYPE;

    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"), cancellable = true)
    protected <E extends Entity>  void roundabout$render(E $$0, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int $$8, CallbackInfo ci) {
        if ($$0 instanceof LivingEntity){
            ((StandUser)$$0).roundabout$tryBlip();
        }
    }
    @Shadow
    private static void renderBlockShadow(
            PoseStack.Pose $$0, VertexConsumer $$1, ChunkAccess $$2, LevelReader $$3, BlockPos $$4, double $$5, double $$6, double $$7, float $$8, float $$9
    ) {}

    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
    private static void roundabout$RenderShadow(PoseStack $$0, MultiBufferSource $$1, Entity $$2, float renderDistance, float $$4, LevelReader $$5, float shadowRadius, CallbackInfo ci) {
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
