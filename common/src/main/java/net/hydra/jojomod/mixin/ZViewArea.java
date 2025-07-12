package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IViewArea;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(ViewArea.class)
public abstract class ZViewArea implements IViewArea {

    @Shadow @Nullable protected abstract ChunkRenderDispatcher.RenderChunk getRenderChunkAt(BlockPos $$0);

    @Unique
    @Override
    public ChunkRenderDispatcher.RenderChunk roundabout$getRenderChunkAt(BlockPos $$0){
        return getRenderChunkAt($$0);
    }
}
