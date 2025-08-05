package net.hydra.jojomod.mixin.d4c.dworlds;

import net.hydra.jojomod.Roundabout;
import net.zetalasis.world.DynamicWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorD4CMixin {
    @Inject(method = "applyBiomeDecoration", at=@At("HEAD"), cancellable = true)
    /* this mixin is very volatile: it might (and probably will) break
    * unfortunately, it's the only way to do this without a custom chunk generator */
    private void roundabout$copyWorlds(WorldGenLevel level, ChunkAccess chunk, StructureManager structures, CallbackInfo ci)
    {
        if (DynamicWorld.isWorldDynamic(level.getLevel()))
        {
            try
            {
                MinecraftServer server = level.getServer();
                if (server == null)
                    return;

                ServerLevel overworld = server.overworld();

                overworld.setChunkForced(chunk.getPos().x, chunk.getPos().z, false);

                server.execute(()-> {
                    LevelChunk overworldChunk = overworld.getChunk(chunk.getPos().x, chunk.getPos().z);

                    ChunkPos chunkPos = chunk.getPos();

                    int startX = chunkPos.getMinBlockX();
                    int startZ = chunkPos.getMinBlockZ();

                    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

                    for (int x = startX; x < startX + 16; x++) {
                        for (int z = startZ; z < startZ + 16; z++) {
                            for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
                                pos.set(x, y, z);

                                BlockState state = overworldChunk.getBlockState(pos);
                                BlockState currentState = level.getBlockState(pos);

                                if (state.equals(currentState))
                                    continue;

                                level.setBlock(pos, state, Block.UPDATE_NONE);
                            }
                        }
                    };
                });
            }
            catch (Exception e)
            {
                Roundabout.LOGGER.error(e.toString());
                e.printStackTrace();
            }

            level.setCurrentlyGenerating(null);
            ci.cancel();
        }
    }
}
