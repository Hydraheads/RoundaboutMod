package net.hydra.jojomod.world;

import com.mojang.serialization.Lifecycle;
import net.hydra.jojomod.Roundabout;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.ArrayList;

public class DynamicWorld {
    private ServerLevel level;

    public DynamicWorld(MinecraftServer server, String name)
    {
        DynamicWorldAccessor accessor = DynamicWorldAccessor.getFrom(server);
        ResourceKey<Level> LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, Roundabout.location(name));

        RegistryAccess registry = server.registries().compositeAccess();
        MappedRegistry<LevelStem> manager = ((MappedRegistry<LevelStem>)registry.lookupOrThrow(Registries.DIMENSION));

        DynamicWorldAccessor.getFrom(manager).roundabout$setFrozen(false);

        var key = ResourceKey.create(Registries.LEVEL_STEM, LEVEL_KEY.location());
        if (!manager.containsKey(key))
        {
            manager.register(key, new LevelStem(server.overworld().getLevel().dimensionTypeRegistration(), server.overworld().getChunkSource().getGenerator()), Lifecycle.stable());
        }

        DynamicWorldAccessor.getFrom(manager).roundabout$setFrozen(true);

        level = new ServerLevel(
                server,
                accessor.roundabout$getExecutor(),
                accessor.roundabout$getLevelStorageAccess(),
                accessor.roundabout$getLevelData(),
                LEVEL_KEY,
                accessor.roundabout$getLevelStem(),
                accessor.roundabout$getProgressListener(),
                false, // isDebug
                accessor.roundabout$getObfuscatedSeed(),
                new ArrayList<>(),
                true, // shouldTickWorld
                null
        );

        accessor.roundabout$addWorld(LEVEL_KEY, level);
        level.tick(()->true);
    }

    public ServerLevel getLevel()
    {
        return this.level;
    }
}
