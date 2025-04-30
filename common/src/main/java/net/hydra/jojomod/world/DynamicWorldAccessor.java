package net.hydra.jojomod.world;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.Executor;

public interface DynamicWorldAccessor {
    ServerLevelData roundabout$getLevelData();
    WorldOptions roundabout$getWorldOptions();
    long roundabout$getObfuscatedSeed();
    LevelStem roundabout$getLevelStem();
    Executor roundabout$getExecutor();
    LevelStorageSource.LevelStorageAccess roundabout$getLevelStorageAccess();
    Map<ResourceKey<Level>, ServerLevel> roundabout$getLevels();
    @Nullable ChunkProgressListener roundabout$getProgressListener();

    void roundabout$addWorld(ResourceKey<Level> key, ServerLevel level);
    void roundabout$removeWorld(ResourceKey<Level> key);

    static DynamicWorldAccessor getFrom(MinecraftServer server)
    { return ((DynamicWorldAccessor) server); }

    static DynamicWorldAccessor getFrom(MappedRegistry<LevelStem> registry)
    { return ((DynamicWorldAccessor) registry); }

    void roundabout$setFrozen(boolean frozen);
    boolean roundabout$isFrozen();
}
