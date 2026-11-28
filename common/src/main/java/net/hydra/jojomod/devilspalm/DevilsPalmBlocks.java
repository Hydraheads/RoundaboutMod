package net.hydra.jojomod.devilspalm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public final class DevilsPalmBlocks {
    private static final String PREFIX = "devils_palm_";
    public static final List<String> IDS = List.of("air", "desert_main", "desert_light", "desert_dark",
            "badlands_main", "badlands_light", "badlands_dark", "grass_main", "grass_light", "grass_dark",
            "ice_main", "ice_light", "ice_dark");

    private DevilsPalmBlocks() {}

    public static String id(String name) { return PREFIX + name; }

    public static Block create(String name) {
        Block source = switch (name) {
            case "desert_main" -> Blocks.SANDSTONE;
            case "desert_light" -> Blocks.SMOOTH_SANDSTONE;
            case "desert_dark" -> Blocks.CUT_SANDSTONE;
            case "badlands_main" -> Blocks.RED_SANDSTONE;
            case "badlands_light" -> Blocks.SMOOTH_RED_SANDSTONE;
            case "badlands_dark" -> Blocks.TERRACOTTA;
            case "grass_main" -> Blocks.STONE;
            case "grass_light" -> Blocks.ANDESITE;
            case "grass_dark" -> Blocks.COBBLESTONE;
            case "ice_main" -> Blocks.PACKED_ICE;
            case "ice_light" -> Blocks.SNOW_BLOCK;
            case "ice_dark" -> Blocks.BLUE_ICE;
            case "air" -> null;
            default -> throw new IllegalArgumentException(name);
        };
        BlockBehaviour.Properties properties = source == null
                ? BlockBehaviour.Properties.of().noCollission().noOcclusion()
                : BlockBehaviour.Properties.copy(source);
        return new PalmBlock(properties.strength(source == null ? -1.0F : 1.5F, 3600000.0F).noLootTable().pushReaction(PushReaction.BLOCK), source);
    }

    public static BlockState state(String name) {
        Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation("roundabout", id(name)));
        if (!(block instanceof PalmBlock)) throw new IllegalStateException("Unregistered Palm block: " + name);
        return block.defaultBlockState();
    }

    public static BlockState material(DevilsPalmBiome biome, String material) {
        String palette = switch (biome) {
            case DESERT -> "desert";
            case BADLANDS -> "badlands";
            case MEADOW, PLAINS -> "grass";
            case ICE_SPIKES, SNOWY_PLAINS, FROZEN_PEAKS -> "ice";
            default -> throw new IllegalArgumentException("Unsupported biome");
        };
        return state(palette + "_" + material);
    }

    public static boolean isTemporary(BlockState state) { return state.getBlock() instanceof PalmBlock; }

    private static final class PalmBlock extends Block {
        private final boolean empty;
        private final Block source;

        private PalmBlock(Properties properties, Block source) {
            super(properties);
            this.source = source;
            this.empty = source == null;
        }

        @Override
        public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
            return empty ? 0.0F : source.defaultBlockState().getDestroyProgress(player, level, pos);
        }

        @Override
        public RenderShape getRenderShape(BlockState state) {
            return empty ? RenderShape.INVISIBLE : super.getRenderShape(state);
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return empty ? Shapes.empty() : super.getShape(state, level, pos, context);
        }

        @Override
        public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
            return empty || super.propagatesSkylightDown(state, level, pos);
        }
    }
}
