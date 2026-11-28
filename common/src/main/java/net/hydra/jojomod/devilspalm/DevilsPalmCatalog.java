package net.hydra.jojomod.devilspalm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DevilsPalmCatalog {
    private static final ResourceLocation ID = new ResourceLocation("roundabout", "devils_palm/formations.json");

    public record Formation(String name, int groundOffset, int arenaRadius, int weight) {}

    private DevilsPalmCatalog() {}

    public static List<Formation> read(ServerLevel level) {
        try (Reader reader = level.getServer().getResourceManager().getResource(ID)
                .orElseThrow(() -> new IllegalArgumentException("Missing Palm formation catalog")).openAsReader()) {
            JsonArray entries = JsonParser.parseReader(reader).getAsJsonObject().getAsJsonArray("formations");
            List<Formation> result = new ArrayList<>();
            Set<String> names = new HashSet<>();
            if (entries.size() < 1 || entries.size() > 64) throw new IllegalArgumentException("Invalid formation count");
            for (JsonElement element : entries) {
                JsonObject entry = element.getAsJsonObject();
                String name = entry.get("name").getAsString();
                int offset = entry.get("ground_offset").getAsInt();
                int radius = entry.get("arena_radius").getAsInt();
                int weight = entry.get("weight").getAsInt();
                if (!name.matches("[a-z0-9_-]+") || !names.add(name) || offset < 1 || offset > DevilsPalmTemplate.MAX_GROUND_OFFSET
                        || radius < 8 || radius > 40 || weight < 0 || weight > 1000)
                    throw new IllegalArgumentException("Invalid Palm formation entry: " + name);
                result.add(new Formation(name, offset, radius, weight));
            }
            return List.copyOf(result);
        } catch (IOException | RuntimeException exception) {
            throw new IllegalArgumentException("Cannot load Palm formation catalog: " + exception.getMessage(), exception);
        }
    }

    public static Formation find(ServerLevel level, String name) {
        String resolved = switch (name) {
            case "open_palm" -> "open_palm_large";
            case "curled_palm" -> "curled_palm_large";
            default -> name;
        };
        return read(level).stream().filter(f -> f.name().equals(resolved)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Palm formation: " + name));
    }

    public static Formation choose(ServerLevel level) {
        List<Formation> formations = read(level);
        int total = formations.stream().mapToInt(Formation::weight).sum();
        if (total <= 0) throw new IllegalArgumentException("No enabled automatic Palm formations");
        int pick = level.random.nextInt(total);
        for (Formation formation : formations) {
            pick -= formation.weight();
            if (pick < 0) return formation;
        }
        throw new IllegalStateException("Invalid Palm formation weights");
    }

    public static Rotation rotation(ServerLevel level, BlockPos center, Formation formation) {
        long seed = level.getSeed() ^ center.asLong() ^ ((long)formation.name().hashCode() << 32);
        seed ^= seed >>> 33;
        seed *= 0xff51afd7ed558ccdL;
        seed ^= seed >>> 33;
        return Rotation.values()[(int)(seed & 3L)];
    }
}
