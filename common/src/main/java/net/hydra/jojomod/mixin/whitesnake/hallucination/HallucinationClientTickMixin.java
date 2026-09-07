package net.hydra.jojomod.mixin.whitesnake.hallucination;

import net.hydra.jojomod.block.HallucinatoryAcidBlockEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.whitesnake.HallucinationEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.core.SectionPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class HallucinationClientTickMixin {
    @Unique private boolean roundaboutWhitesnake$acidHidden;

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundaboutWhitesnake$refreshAcidVisibility(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        MobEffectInstance effect = minecraft.player == null ? null
                : minecraft.player.getEffect(ModEffects.HALLUCINATION);
        boolean hidden = HallucinationEffect.hasDistortion(effect);
        if (hidden == roundaboutWhitesnake$acidHidden) return;
        roundaboutWhitesnake$acidHidden = hidden;
        if (minecraft.level == null || minecraft.player == null) return;

        int renderDistance = minecraft.options.getEffectiveRenderDistance();
        int centerChunkX = minecraft.player.chunkPosition().x;
        int centerChunkZ = minecraft.player.chunkPosition().z;
        for (int chunkX = centerChunkX - renderDistance; chunkX <= centerChunkX + renderDistance; chunkX++) {
            for (int chunkZ = centerChunkZ - renderDistance; chunkZ <= centerChunkZ + renderDistance; chunkZ++) {
                if (!minecraft.level.getChunkSource().hasChunk(chunkX, chunkZ)) continue;
                LevelChunk chunk = minecraft.level.getChunkSource().getChunk(chunkX, chunkZ, false);
                if (chunk == null) continue;
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    if (blockEntity instanceof HallucinatoryAcidBlockEntity) {
                        minecraft.levelRenderer.setSectionDirty(
                                SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getX()),
                                SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getY()),
                                SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getZ()));
                    }
                }
            }
        }
    }
}
