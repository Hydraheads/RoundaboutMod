package net.hydra.jojomod.devilspalm;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DevilsPalmTemplate {
    public static final int MAX_WIDTH = 96;
    public static final int MAX_HEIGHT = 48;
    public static final int MAX_CHANGES = 120000;
    public static final int MAX_GROUND_OFFSET = 12;

    public record Placement(BlockPos pos, BlockState state) {}

    private DevilsPalmTemplate() {}

    public static List<Placement> read(ServerLevel level, DevilsPalmCatalog.Formation formation, BlockPos center, DevilsPalmBiome biome, Rotation rotation, DevilsPalmSurface surface) {
        String name = formation.name();
        int groundOffset = formation.groundOffset();
        if (!name.matches("[a-z0-9_-]+")) throw new IllegalArgumentException("Invalid template name");
        ResourceLocation id = new ResourceLocation("roundabout", "devils_palm/" + name);
        StructureTemplate template = level.getStructureManager().get(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));
        CompoundTag tag = template.save(new CompoundTag());
        ListTag size = tag.getList("size", Tag.TAG_INT);
        if (size.size() != 3) throw new IllegalArgumentException("Invalid template size");
        int width = size.getInt(0), height = size.getInt(1), depth = size.getInt(2);
        if (width < 1 || depth < 1 || height <= groundOffset || width > MAX_WIDTH || depth > MAX_WIDTH || height > MAX_HEIGHT)
            throw new IllegalArgumentException("Template must fit 96 x 48 x 96 and include its configured ground offset");
        if (!tag.getList("entities", Tag.TAG_COMPOUND).isEmpty() || tag.contains("palettes"))
            throw new IllegalArgumentException("Entities and multiple palettes are not supported");
        ListTag palette = tag.getList("palette", Tag.TAG_COMPOUND);
        BlockState[] replacements = new BlockState[palette.size()];
        ListTag blocks = tag.getList("blocks", Tag.TAG_COMPOUND);
        List<Placement> result = new ArrayList<>();
        Set<Long> seen = new HashSet<>();
        for (int i = 0; i < blocks.size(); i++) {
            CompoundTag block = blocks.getCompound(i);
            if (block.contains("nbt")) throw new IllegalArgumentException("Block entities are not allowed in Palm templates");
            int index = block.getInt("state");
            if (index < 0 || index >= palette.size()) throw new IllegalArgumentException("Invalid palette index");
            CompoundTag state = palette.getCompound(index);
            String material = state.getString("Name");
            if (material.equals("minecraft:air") || material.equals("minecraft:cave_air") || material.equals("minecraft:void_air") || material.equals("minecraft:structure_void")) continue;
            ListTag coordinates = block.getList("pos", Tag.TAG_INT);
            if (coordinates.size() != 3) throw new IllegalArgumentException("Invalid block position");
            int x = coordinates.getInt(0), y = coordinates.getInt(1), z = coordinates.getInt(2);
            if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth)
                throw new IllegalArgumentException("Block lies outside template bounds");
            BlockState replacement = replacements[index];
            if (replacement == null) {
                replacement = switch (material) {
                    case "minecraft:magenta_wool" -> DevilsPalmBlocks.state("air");
                    case "minecraft:sandstone" -> DevilsPalmBlocks.material(biome, "main");
                    case "minecraft:smooth_sandstone" -> DevilsPalmBlocks.material(biome, "light");
                    case "minecraft:cut_sandstone" -> DevilsPalmBlocks.material(biome, "dark");
                    default -> throw new IllegalArgumentException("Unsupported template material: " + material);
                };
                replacements[index] = replacement;
            }
            int dx = x - width / 2, dz = z - depth / 2;
            BlockPos pos = switch (rotation) {
                case NONE -> center.offset(dx, y - groundOffset, dz);
                case CLOCKWISE_90 -> center.offset(-dz, y - groundOffset, dx);
                case CLOCKWISE_180 -> center.offset(-dx, y - groundOffset, -dz);
                case COUNTERCLOCKWISE_90 -> center.offset(dz, y - groundOffset, -dx);
            };
            pos = pos.offset(0, surface.formationAt(pos.getX(), pos.getZ()) - center.getY(), 0);
            if (!seen.add(pos.asLong())) throw new IllegalArgumentException("Duplicate position in template");
            result.add(new Placement(pos, replacement));
            if (result.size() > MAX_CHANGES) throw new IllegalArgumentException("Template exceeds block budget");
        }
        if (result.isEmpty()) throw new IllegalArgumentException("Empty Palm template");
        return result;
    }
}
