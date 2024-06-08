package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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

    @Shadow
    private static void renderBlockShadow(PoseStack.Pose pose, VertexConsumer vertexConsumer, ChunkAccess chunkAccess, LevelReader levelReader, BlockPos blockPos, double d, double e, double f, float g, float h) {
    }

    @Inject(
            method = "renderShadow",
            at = @At(value = "HEAD"), cancellable = true)
    private static void renderShadow(PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity, float f, float g, LevelReader levelReader, float h, CallbackInfo ci) {
        if (((TimeStop) entity.level()).CanTimeStopEntity(entity)) {
            //h = ((IEntityDataSaver) entity).getPreTSTick();
            Mob mob;
            float i = h;
             g = Minecraft.getInstance().getDeltaFrameTime();
            if (entity instanceof Mob && (mob = (Mob) entity).isBaby()) {
                i *= 0.5f;
            }
            double d = Mth.lerp((double) g, ((IEntityDataSaver) entity).getPreTSX(), entity.getX());
            double e = Mth.lerp((double) g, ((IEntityDataSaver) entity).getPreTSY(), entity.getY());
            double j = Mth.lerp((double) g, ((IEntityDataSaver) entity).getPreTSZ(), entity.getZ());
            ((IEntityDataSaver) entity).setPreTSX(d);
            ((IEntityDataSaver) entity).setPreTSY(e);
            ((IEntityDataSaver) entity).setPreTSZ(j);
            float k = Math.min(f / 0.5f, i);
            int l = Mth.floor(d - (double) i);
            int m = Mth.floor(d + (double) i);
            int n = Mth.floor(e - (double) k);
            int o = Mth.floor(e);
            int p = Mth.floor(j - (double) i);
            int q = Mth.floor(j + (double) i);
            PoseStack.Pose pose = poseStack.last();
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(SHADOW_RENDER_TYPE);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (int r = p; r <= q; ++r) {
                for (int s = l; s <= m; ++s) {
                    mutableBlockPos.set(s, 0, r);
                    ChunkAccess chunkAccess = levelReader.getChunk(mutableBlockPos);
                    for (int t = n; t <= o; ++t) {
                        mutableBlockPos.setY(t);
                        float u = f - (float) (e - (double) mutableBlockPos.getY()) * 0.5f;
                        renderBlockShadow(pose, vertexConsumer, chunkAccess, levelReader, mutableBlockPos, d, e, j, i, u);
                    }
                }
            }
            ci.cancel();
        }
    }
}
