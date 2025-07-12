package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBlockState;
import net.hydra.jojomod.access.IBlockStateBase;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class ZLevelChunk extends ChunkAccess {

    @Shadow @Final private Level level;

    public ZLevelChunk(ChunkPos $$0, UpgradeData $$1, LevelHeightAccessor $$2, Registry<Biome> $$3, long $$4, @Nullable LevelChunkSection[] $$5, @Nullable BlendingData $$6) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6);
    }

    /**
    Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
    private void roundabout$getBlockState(BlockPos $$0, CallbackInfoReturnable<BlockState> cir) {

        int $$1 = $$0.getX();
        int $$2 = $$0.getY();
        int $$3 = $$0.getZ();
        if (MainUtil.getHiddenBlocks().contains($$0)) {
            if (!this.level.isDebug()) {
                int $$5 = this.level.getSectionIndex($$2);

                if ($$5 >= 0 && $$5 < this.sections.length) {
                    LevelChunkSection $$6 = this.sections[$$5];
                    if (!$$6.hasOnlyAir()) {
                        BlockState state = $$6.getBlockState($$1 & 15, $$2 & 15, $$3 & 15);
                        ((IBlockState)state).roundabout$setInvis(true);
                        cir.setReturnValue(state);
                    }
                }
            }
        }
    }
     **/
}
