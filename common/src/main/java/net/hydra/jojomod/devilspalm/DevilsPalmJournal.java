package net.hydra.jojomod.devilspalm;

import net.hydra.jojomod.devilspalm.DevilsPalmTerrain.Change;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class DevilsPalmJournal {
    private DevilsPalmJournal() {}

    record Recovered(BlockPos center, List<Change> changes) {}

    static Path path(ServerLevel level) {
        return level.getServer().getWorldPath(LevelResource.ROOT).resolve("roundabout-devils-palm-journal.nbt");
    }

    static void write(ServerLevel level, BlockPos center, List<Change> changes) throws IOException {
        CompoundTag root = new CompoundTag();
        root.putInt("version", 2);
        root.putLong("center", center.asLong());
        ListTag palette = new ListTag();
        Map<BlockState, Integer> indices = new HashMap<>();
        long[] positions = new long[changes.size()];
        int[] originals = new int[positions.length];
        int[] temporaries = new int[positions.length];
        for (int i = 0; i < positions.length; i++) {
            Change change = changes.get(i);
            positions[i] = change.pos().asLong();
            originals[i] = paletteIndex(change.original(), palette, indices);
            temporaries[i] = paletteIndex(change.temporary(), palette, indices);
        }
        root.put("palette", palette);
        root.putLongArray("positions", positions);
        root.putIntArray("originals", originals);
        root.putIntArray("temporaries", temporaries);
        Path path = path(level);
        if (Files.exists(path)) throw new IOException("An unfinished Palm journal already exists");
        Path temporary = path.resolveSibling(path.getFileName() + ".tmp");
        NbtIo.writeCompressed(root, temporary.toFile());
        try (FileChannel channel = FileChannel.open(temporary, StandardOpenOption.WRITE)) { channel.force(true); }
        Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE);
    }

    private static int paletteIndex(BlockState state, ListTag palette, Map<BlockState, Integer> indices) {
        return indices.computeIfAbsent(state, key -> {
            int index = palette.size();
            palette.add(NbtUtils.writeBlockState(key));
            return index;
        });
    }

    private static BlockState readState(CompoundTag tag) throws IOException {
        ResourceLocation id = ResourceLocation.tryParse(tag.getString("Name"));
        if (id == null || !BuiltInRegistries.BLOCK.containsKey(id)) throw new IOException("Unknown journal block state");
        return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag);
    }

    private static void appendRecovered(ServerLevel level, BlockPos center, List<Change> changes, Set<Long> positions,
                                        BlockPos pos, BlockState original, BlockState temporary) throws IOException {
        if (!positions.add(pos.asLong()) || !DevilsPalmBlocks.isTemporary(temporary) || !DevilsPalmLocationFinder.canReplace(original)
                || level.isOutsideBuildHeight(pos) || Math.abs((long)pos.getX() - center.getX()) > DevilsPalmLocationFinder.RADIUS
                || Math.abs((long)pos.getZ() - center.getZ()) > DevilsPalmLocationFinder.RADIUS || pos.getY() < center.getY() - 12 - DevilsPalmSurface.MAX_RELIEF
                || pos.getY() > center.getY() + 48 + DevilsPalmSurface.MAX_RELIEF) throw new IOException("Invalid journal entry");
        changes.add(new Change(pos, original, temporary));
    }

    static Recovered read(ServerLevel level) throws IOException {
        CompoundTag root = NbtIo.readCompressed(path(level).toFile());
        if (!root.contains("center", Tag.TAG_LONG)) throw new IOException("Missing journal center");
        BlockPos center = BlockPos.of(root.getLong("center"));
        List<Change> changes = new ArrayList<>();
        Set<Long> positions = new HashSet<>();
        if (root.getInt("version") == 1) {
            ListTag entries = root.getList("changes", Tag.TAG_COMPOUND);
            if (entries.isEmpty() || entries.size() > 30000) throw new IOException("Invalid legacy journal size");
            for (int i = 0; i < entries.size(); i++) {
                CompoundTag entry = entries.getCompound(i);
                if (!entry.contains("pos", Tag.TAG_LONG) || !entry.contains("original", Tag.TAG_COMPOUND)
                        || !entry.contains("temporary", Tag.TAG_COMPOUND)) throw new IOException("Invalid legacy journal entry");
                appendRecovered(level, center, changes, positions, BlockPos.of(entry.getLong("pos")),
                        readState(entry.getCompound("original")), readState(entry.getCompound("temporary")));
            }
        } else if (root.getInt("version") == 2) {
            long[] packed = root.getLongArray("positions");
            int[] originals = root.getIntArray("originals"), temporaries = root.getIntArray("temporaries");
            ListTag entries = root.getList("palette", Tag.TAG_COMPOUND);
            if (packed.length == 0 || packed.length > DevilsPalmTemplate.MAX_CHANGES || originals.length != packed.length
                    || temporaries.length != packed.length || entries.isEmpty() || entries.size() > 4096)
                throw new IOException("Invalid journal arrays");
            List<BlockState> palette = new ArrayList<>();
            for (int i = 0; i < entries.size(); i++) palette.add(readState(entries.getCompound(i)));
            for (int i = 0; i < packed.length; i++) {
                if (originals[i] < 0 || originals[i] >= palette.size() || temporaries[i] < 0 || temporaries[i] >= palette.size())
                    throw new IOException("Invalid journal palette index");
                appendRecovered(level, center, changes, positions, BlockPos.of(packed[i]), palette.get(originals[i]), palette.get(temporaries[i]));
            }
        } else throw new IOException("Unknown Palm journal version");
        return new Recovered(center, changes);
    }

    static void archive(ServerLevel level) throws IOException {
        Path path = path(level);
        Path archive = path.resolveSibling("roundabout-devils-palm-last-completed.nbt");
        try {
            Files.move(path, archive, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(path, archive, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
