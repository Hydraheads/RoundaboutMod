package net.hydra.jojomod.access;

import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;

public interface IViewArea {
    ChunkRenderDispatcher.RenderChunk roundabout$getRenderChunkAt(BlockPos $$0);
}
