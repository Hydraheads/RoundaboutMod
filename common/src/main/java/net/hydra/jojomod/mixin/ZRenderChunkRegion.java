package net.hydra.jojomod.mixin;

import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderChunkRegion.class)
public abstract class ZRenderChunkRegion implements BlockAndTintGetter {
}
