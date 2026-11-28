package net.hydra.jojomod.devilspalm;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class DevilsPalmSavedData extends SavedData {
    private final Set<Long> modified = new HashSet<>();
    private final Map<Long, Set<Long>> modifiedPositions = new HashMap<>();
    private final Map<Long, Long> locations = new HashMap<>();
    private final Map<Long, Long> lastVisit = new HashMap<>();
    private BlockPos selected;
    private long selectedAt;
    private long sequence;
    private long nextAttempt;
    private boolean automatic = true;
    private String selectedTemplate;
    private boolean selectedManifested;
    private boolean relocating;
    private int relocationCursor;

    public static DevilsPalmSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(DevilsPalmSavedData::load, DevilsPalmSavedData::new, "roundabout_devils_palm");
    }

    public void recordModification(BlockPos pos) {
        if (modifiedPositions.computeIfAbsent(new ChunkPos(pos).toLong(), key -> new HashSet<>()).add(pos.asLong())) setDirty();
    }

    public boolean isModified(int x, int z) { return modified.contains(ChunkPos.asLong(x, z)); }
    public Set<Long> modifiedPositions(int x, int z) { return modifiedPositions.getOrDefault(ChunkPos.asLong(x, z), Set.of()); }
    public int modifiedCount() {
        Set<Long> chunks = new HashSet<>(modified);
        chunks.addAll(modifiedPositions.keySet());
        return chunks.size();
    }
    public int locationCount() { return locations.size(); }
    public BlockPos selected() { return selected; }
    public String selectedTemplate() { return selectedTemplate; }
    public long selectedAt() { return selectedAt; }
    public long nextAttempt() { return nextAttempt; }
    public boolean automatic() { return automatic; }
    public boolean selectedManifested() { return selectedManifested; }
    public boolean relocating() { return relocating; }

    public void beginRelocation() {
        if (selected == null || selectedManifested || relocating) return;
        relocating = true;
        relocationCursor = 0;
        setDirty();
    }

    public int nextRelocationSample(int count) {
        int index = Math.floorMod(relocationCursor, count);
        relocationCursor = (index + 1) % count;
        setDirty();
        return index;
    }
    public void setAutomatic(boolean value) { automatic = value; setDirty(); }
    public void markManifested() { selectedManifested = true; setDirty(); }

    public void setNextAttempt(long tick) { nextAttempt = tick; setDirty(); }

    public boolean eligible(BlockPos pos, long now) {
        for (Map.Entry<Long, Long> entry : locations.entrySet()) {
            BlockPos known = BlockPos.of(entry.getKey());
            long dx = (long) known.getX() - pos.getX(), dz = (long) known.getZ() - pos.getZ();
            if (dx * dx + dz * dz > 144L * 144L) continue;
            if (now < entry.getValue() || lastVisit.containsKey(entry.getKey()) && sequence - lastVisit.get(entry.getKey()) < 3) return false;
        }
        return true;
    }

    public Set<Long> candidates() { return Set.copyOf(locations.keySet()); }

    public void select(BlockPos pos, long now) { select(pos, now, null); }

    public void select(BlockPos pos, long now, String template) {
        if (relocating && selected != null) {
            long old = selected.asLong();
            if (!lastVisit.containsKey(old) && locations.getOrDefault(old, 0L) == 0L) locations.remove(old);
        }
        relocating = false;
        relocationCursor = 0;
        selected = pos.immutable();
        selectedAt = now;
        selectedTemplate = template;
        selectedManifested = false;
        locations.putIfAbsent(pos.asLong(), 0L);
        setDirty();
    }

    public void retireFailed(long now, long cooldown) {
        selectedManifested = false;
        retire(now, cooldown);
    }

    public void retire(long now, long cooldown) {
        if (selected == null) return;
        long key = selected.asLong();
        locations.put(key, now + cooldown);
        if (selectedManifested) lastVisit.put(key, ++sequence);
        selected = null;
        selectedAt = 0;
        selectedTemplate = null;
        selectedManifested = false;
        relocating = false;
        relocationCursor = 0;
        setDirty();
    }

    public static DevilsPalmSavedData load(CompoundTag tag) {
        DevilsPalmSavedData data = new DevilsPalmSavedData();
        for (long key : tag.getLongArray("modified")) data.modified.add(key);
        for (long key : tag.getLongArray("modifiedPositions")) {
            BlockPos pos = BlockPos.of(key);
            data.modifiedPositions.computeIfAbsent(new ChunkPos(pos).toLong(), ignored -> new HashSet<>()).add(key);
        }
        for (Tag entry : tag.getList("locations", Tag.TAG_COMPOUND)) {
            CompoundTag item = (CompoundTag) entry;
            long key = item.getLong("pos");
            data.locations.put(key, item.getLong("until"));
            if (item.contains("visit")) data.lastVisit.put(key, item.getLong("visit"));
        }
        if (tag.contains("selected")) data.selected = BlockPos.of(tag.getLong("selected"));
        data.selectedAt = tag.getLong("selectedAt");
        data.sequence = tag.getLong("sequence");
        data.nextAttempt = tag.getLong("nextAttempt");
        data.automatic = !tag.contains("automatic") || tag.getBoolean("automatic");
        if (tag.contains("selectedTemplate")) data.selectedTemplate = tag.getString("selectedTemplate");
        data.selectedManifested = tag.getBoolean("selectedManifested");
        data.relocating = data.selected != null && !data.selectedManifested && tag.getBoolean("relocating");
        data.relocationCursor = data.relocating ? tag.getInt("relocationCursor") : 0;
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("schema", 4);
        if (selectedTemplate != null) tag.putString("selectedTemplate", selectedTemplate);
        tag.putLongArray("modified", modified.stream().mapToLong(Long::longValue).toArray());
        tag.putLongArray("modifiedPositions", modifiedPositions.values().stream().flatMap(Set::stream).mapToLong(Long::longValue).toArray());
        ListTag entries = new ListTag();
        locations.forEach((key, until) -> {
            CompoundTag item = new CompoundTag();
            item.putLong("pos", key);
            item.putLong("until", until);
            if (lastVisit.containsKey(key)) item.putLong("visit", lastVisit.get(key));
            entries.add(item);
        });
        tag.put("locations", entries);
        if (selected != null) tag.putLong("selected", selected.asLong());
        else tag.remove("selected");
        tag.putLong("selectedAt", selectedAt);
        tag.putLong("sequence", sequence);
        tag.putLong("nextAttempt", nextAttempt);
        tag.putBoolean("automatic", automatic);
        tag.putBoolean("selectedManifested", selectedManifested);
        tag.putBoolean("relocating", relocating);
        tag.putInt("relocationCursor", relocationCursor);
        return tag;
    }
}
