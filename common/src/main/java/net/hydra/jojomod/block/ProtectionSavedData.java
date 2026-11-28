package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.*;

public class ProtectionSavedData extends SavedData {

    private final Map<ChunkPos, List<ProtectionEntry>> byChunk = new HashMap<>();
    private final List<ProtectionEntry> allEntries = new ArrayList<>();

    public static ProtectionSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                ProtectionSavedData::load,
                ProtectionSavedData::new,
                "protection_data"
        );
    }

    public void add(BlockPos be) {
        ProtectionEntry entry = new ProtectionEntry(
                be
        );

        allEntries.add(entry);
        index(entry);

        setDirty();
    }

    private void index(ProtectionEntry entry) {
        int minX = (entry.pos.getX() - 200) >> 4;
        int maxX = (entry.pos.getX() + 200) >> 4;
        int minZ = (entry.pos.getZ() - 200) >> 4;
        int maxZ = (entry.pos.getZ() + 200) >> 4;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                ChunkPos chunk = new ChunkPos(x, z);
                byChunk.computeIfAbsent(chunk, c -> new ArrayList<>()).add(entry);
            }
        }
    }

    public boolean isProtected(BlockPos pos) {
        ChunkPos chunk = new ChunkPos(pos);

        List<ProtectionEntry> list = byChunk.get(chunk);
        if (list == null) return false;

        for (ProtectionEntry entry : list) {
            if (entry.isInside(pos)) {
                return true;
            }
        }

        return false;
    }

    public boolean intersectsBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        Set<ProtectionEntry> checked = new HashSet<>();
        for (int x = minX >> 4; x <= maxX >> 4; x++) {
            for (int z = minZ >> 4; z <= maxZ >> 4; z++) {
                List<ProtectionEntry> entries = byChunk.get(new ChunkPos(x, z));
                if (entries == null) continue;
                for (ProtectionEntry entry : entries) {
                    if (!checked.add(entry)) continue;
                    long dx = Math.max(0L, Math.max((long)minX - entry.pos.getX(), (long)entry.pos.getX() - maxX));
                    long dy = Math.max(0L, Math.max((long)minY - entry.pos.getY(), (long)entry.pos.getY() - maxY));
                    long dz = Math.max(0L, Math.max((long)minZ - entry.pos.getZ(), (long)entry.pos.getZ() - maxZ));
                    if (dx >= 200 || dy >= 200 || dz >= 200) continue;
                    if (dx * dx + dy * dy + dz * dz < 200L * 200L) return true;
                }
            }
        }
        return false;
    }

    public void remove(BlockPos pos) {
        for (List<ProtectionEntry> list : byChunk.values()) {
            list.removeIf(e -> e.pos.equals(pos));
        }

        setDirty();
    }
    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();

        for (ProtectionEntry entry : allEntries) {
            CompoundTag e = new CompoundTag();
            e.putLong("pos", entry.pos.asLong());
            list.add(e);
        }

        tag.put("entries", list);
        return tag;
    }
    public static ProtectionSavedData load(CompoundTag tag) {
        ProtectionSavedData data = new ProtectionSavedData();

        ListTag list = tag.getList("entries", Tag.TAG_COMPOUND);

        for (Tag t : list) {
            CompoundTag e = (CompoundTag) t;

            BlockPos pos = BlockPos.of(e.getLong("pos"));

            ProtectionEntry entry = new ProtectionEntry(pos);

            data.allEntries.add(entry);
            data.index(entry); // rebuild chunk index
        }

        return data;
    }
}