package net.hydra.jojomod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.level.ChunkPos;

import java.util.HashSet;

public class ChunkLoaderUtil {
    /**
     *  We can make a list of chunkLoader entities here,
     * where a ChunkMap.anyPlayerCloseEnoughForSpawning(ChunkPos pos) mixin could
     * check for that said entities and force the chunk to be loaded.

     *  SPECIAL NOTE: Forge seens to be a bit trickier (as Always), looks like there's something called
     * ForgeChunkManager, and it seens very confusing to understand, if anyone is willing to search up about
     * and implemented it, I would appreciate it.
     */

    public static HashSet<Entity> chunkLoadersEntities = new HashSet<>();

    /// should be called in the mixin
    public static boolean isAnyEntityInsideOf(ChunkPos pos) {
        safeInitList();
        for (Entity ent : chunkLoadersEntities) {
            ChunkPos check = new ChunkPos(ent.blockPosition());
            if (pos.equals(check)) {
                return true;
            }
        }

        return false;
    }

    public static void safeInitList() {
        if (chunkLoadersEntities == null) {
            chunkLoadersEntities = new HashSet<>();
        }
    }

}
