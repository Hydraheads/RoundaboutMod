package net.hydra.jojomod.mixin.dworlds;

import com.google.common.collect.ImmutableList;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.world.DynamicWorld;
import net.hydra.jojomod.world.DynamicWorldAccessor;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class ServerAccessorMixin implements DynamicWorldAccessor {
    @Shadow @Final private Executor executor;
    @Shadow @Final protected LevelStorageSource.LevelStorageAccess storageSource;
    @Shadow @Final protected WorldData worldData;
    @Shadow @Final private LayeredRegistryAccess<RegistryLayer> registries;
    @Shadow @Final private Map<ResourceKey<Level>, ServerLevel> levels;

    @Unique private ChunkProgressListener progressListener;

    @Override public ServerLevelData roundabout$getLevelData() { return this.worldData.overworldData(); }
    @Override public WorldOptions roundabout$getWorldOptions() { return this.worldData.worldGenOptions(); }
    @Override public long roundabout$getObfuscatedSeed() { return BiomeManager.obfuscateSeed(this.worldData.worldGenOptions().seed()); }
    @Override public LevelStem roundabout$getLevelStem() { return this.registries.compositeAccess().registryOrThrow(Registries.LEVEL_STEM).get(LevelStem.OVERWORLD); }
    @Override public Executor roundabout$getExecutor() { return this.executor; }
    @Override public LevelStorageSource.LevelStorageAccess roundabout$getLevelStorageAccess() { return this.storageSource; }
    @Override public Map<ResourceKey<Level>, ServerLevel> roundabout$getLevels() { return this.levels; }
    @Override public ChunkProgressListener roundabout$getProgressListener() { return this.progressListener; }

    @Override
    public void roundabout$addWorld(ResourceKey<Level> key, ServerLevel level) {
        this.levels.put(key, level);
    }

    @Inject(method="createLevels", at=@At("TAIL"))
    private void createLevelsMixin(ChunkProgressListener progressListener, CallbackInfo ci)
    {
        this.progressListener = progressListener;
    }
}