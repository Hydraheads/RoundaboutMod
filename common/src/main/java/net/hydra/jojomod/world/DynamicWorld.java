package net.hydra.jojomod.world;

import com.mojang.serialization.Lifecycle;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.dimension.LevelStem;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DynamicWorld {
    private ServerLevel level;
    private final String name;

    public static HashMap<String, ServerLevel> levels = new HashMap<>();

    public ResourceKey<Level> LEVEL_KEY;

    /**
     * @param server The server to register the world to.
     * @param name The name of the dimension (roundabout:name)
     * @param broadcastPacket Should broadcast to everyone that the world is made automatically?
     */
    public DynamicWorld(MinecraftServer server, String name, boolean broadcastPacket)
    {
        this.name = name;

        DynamicWorldAccessor accessor = DynamicWorldAccessor.getFrom(server);
        LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, Roundabout.location(name));

        var manager = getLevelStemRegistry(server);

        DynamicWorldAccessor managerAccessor = DynamicWorldAccessor.getFrom(manager);
        boolean isFrozen = managerAccessor.roundabout$isFrozen();

        managerAccessor.roundabout$setFrozen(false);

        var key = ResourceKey.create(Registries.LEVEL_STEM, LEVEL_KEY.location());
        if (!manager.containsKey(key))
        {
            manager.register(key, new LevelStem(server.overworld().getLevel().dimensionTypeRegistration(), server.overworld().getChunkSource().getGenerator()), Lifecycle.stable());
        }

        managerAccessor.roundabout$setFrozen(isFrozen);

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
                false, // shouldTickWorld
                null
        );

        server.overworld().getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(level.getWorldBorder()));
        accessor.roundabout$addWorld(LEVEL_KEY, level);

        level.tick(()->true);

        levels.put(name, level);

        if (broadcastPacket)
            broadcastPacketsToPlayers(server);
    }

    public void broadcastPacketsToPlayers(MinecraftServer server)
    {
        for (ServerPlayer sp : server.getPlayerList().getPlayers())
        {
            ModPacketHandler.PACKET_ACCESS.sendNewDynamicWorld(sp, name, level, null);
        }
    }

    public ServerLevel getLevel()
    {
        return this.level;
    }

    public String getName()
    {
        return this.name;
    }

    private static MappedRegistry<LevelStem> getLevelStemRegistry(MinecraftServer server) {
        RegistryAccess registryManager = server.registryAccess();
        var temp = registryManager.registryOrThrow(Registries.LEVEL_STEM);

        return (MappedRegistry<LevelStem>) temp;
    }

    public static void deregisterWorld(MinecraftServer server, String name)
    {
        DynamicWorldAccessor accessor = DynamicWorldAccessor.getFrom(server);

        accessor.roundabout$removeWorld(ResourceKey.create(Registries.DIMENSION, Roundabout.location(name)));

        for (ServerPlayer sp : server.getPlayerList().getPlayers())
        {
            ModPacketHandler.PACKET_ACCESS.deregisterDynamicWorld(sp, name);
        }
    }

    private static String generateRandomStringByWords(int numWords) {
        String[] words = {
                "boat",
                "ship",
                "airplane",
                "sword",
                "car",
                "train",
                "bike",
                "rocket",
                "submarine",
                "zeppelin",
                "spiral-staircase",
                "desolation-row",
                "fig-tart",
                "rhinoceros-beetle",
                "singularity-point",
                "giotto",
                "angel",
                "hydrangea",
                "secret-emperor"
        };

        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < numWords; i++) {
            if (i > 0) result.append("-");
            result.append(words[random.nextInt(words.length)]);
        }

        return result.toString();
    }

    public static DynamicWorld generateD4CWorld(MinecraftServer server)
    {
        return new DynamicWorld(server, "d4c-"+generateRandomStringByWords(7)+"-"+server.overworld().getRandom().nextIntBetweenInclusive(0, 999999), false);
    }
}
