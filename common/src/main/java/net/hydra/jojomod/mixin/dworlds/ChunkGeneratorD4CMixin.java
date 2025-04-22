package net.hydra.jojomod.mixin.dworlds;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorD4CMixin {
    @Unique private final TicketType<String> FORCE_LOAD_CHUNK = TicketType.create("roundabout_force_load_chunk", String::compareToIgnoreCase);

    @Inject(method = "applyBiomeDecoration", at=@At("HEAD"), cancellable = true)
    /* this mixin is very volatile: it might (and probably will) break
    * unfortunately, it's the only way to do this without a custom chunk generator */
    private void roundabout$copyWorlds(WorldGenLevel level, ChunkAccess chunk, StructureManager structures, CallbackInfo ci)
    {
        // TODO: figure out how to use the chunk ticketing system to force the overworld chunk to be loaded so we can copy it over

        //if (level.getLevel().dimension().location().getNamespace().equals("roundabout"))
//        if (!level.getLevel().equals(level.getLevel().getServer().overworld()))
//        {
//            try
//            {
//                ServerLevel overworld = level.getLevel().getServer().overworld();
//
//                ChunkPos chunkPos = chunk.getPos();
//
//                overworld.getChunkSource().addRegionTicket(FORCE_LOAD_CHUNK, chunkPos, 1, "roundabout");
//
//                int startX = chunkPos.getMinBlockX();
//                int startZ = chunkPos.getMinBlockZ();
//
//                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
//
//                for (int x = startX; x < startX + 16; x++) {
//                    for (int z = startZ; z < startZ + 16; z++) {
//                        for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
//                            pos.set(x, y, z);
//
//                            BlockState state = overworld.getBlockState(pos);
//                            BlockState currentState = level.getBlockState(pos);
//
//                            if (state.equals(currentState))
//                                continue;
//
//                            level.setBlock(pos, state, Block.UPDATE_NONE);
//                        }
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//                Roundabout.LOGGER.error(e.toString());
//                e.printStackTrace();
//            }
//
//            level.setCurrentlyGenerating(null);
//            ci.cancel();
//        }
    }
}
