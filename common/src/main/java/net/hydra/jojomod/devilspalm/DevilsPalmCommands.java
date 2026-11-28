package net.hydra.jojomod.devilspalm;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.io.IOException;

public final class DevilsPalmCommands {
    private DevilsPalmCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("locate").requires(source -> source.hasPermission(2))
                .then(Commands.literal("structure")
                        .then(Commands.literal("#roundabout:devils_palm").executes(context -> locate(context.getSource(), false)))
                        .then(Commands.literal("#roundabout:dormant_devils_palm").executes(context -> locate(context.getSource(), true)))
                        .then(Commands.literal("devilspalm").executes(context -> locate(context.getSource(), false)))
                        .then(Commands.literal("dormantdevilspalm").executes(context -> locate(context.getSource(), true)))
                        .then(Commands.literal("dormant_devils_palm").executes(context -> locate(context.getSource(), true)))
                        .then(Commands.literal("dormant")
                                .then(Commands.literal("devils")
                                        .then(Commands.literal("palm").executes(context -> locate(context.getSource(), true)))))));
        dispatcher.register(Commands.literal("roundaboutDevilsPalm")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("info").executes(context -> info(context.getSource())))
                .then(Commands.literal("templates").executes(context -> {
                    CommandSourceStack source = context.getSource();
                    try {
                        String names = String.join(", ", DevilsPalmCatalog.read(source.getLevel()).stream()
                                .map(DevilsPalmCatalog.Formation::name).toList());
                        source.sendSuccess(() -> Component.literal("Palm templates: " + names), false);
                        return 1;
                    } catch (IllegalArgumentException exception) {
                        source.sendFailure(Component.literal(exception.getMessage()));
                        return 0;
                    }
                }))
                .then(Commands.literal("raidspots").executes(context -> {
                    CommandSourceStack source = context.getSource();
                    int count = DevilsPalmTerrain.raidSpawnPositions(source.getLevel()).size();
                    source.sendSuccess(() -> Component.literal("Available humanoid raid spawn positions: " + count), false);
                    return count;
                }))
                .then(Commands.literal("automatic").then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                    CommandSourceStack source = context.getSource();
                    if (!source.getLevel().dimension().equals(Level.OVERWORLD)) {
                        source.sendFailure(Component.literal("Overworld only"));
                        return 0;
                    }
                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                    DevilsPalmSavedData data = DevilsPalmSavedData.get(source.getLevel());
                    data.setAutomatic(enabled);
                    if (enabled) data.setNextAttempt(source.getLevel().getGameTime());
                    source.sendSuccess(() -> Component.literal("Automatic Palm manifestation: " + enabled), true);
                    return 1;
                })))
                .then(terrainCommand("plan", false))
                .then(terrainCommand("manifest", true))
                .then(Commands.literal("search").executes(context -> search(context.getSource())))
                .then(Commands.literal("awaken").executes(context -> awaken(context.getSource())))
                .then(Commands.literal("recover").executes(context -> {
                    CommandSourceStack source = context.getSource();
                    try {
                        DevilsPalmTerrain.retryRecovery(source.getLevel());
                        source.sendSuccess(() -> Component.literal(DevilsPalmTerrain.status(source.getLevel())), false);
                        return 1;
                    } catch (IllegalArgumentException exception) {
                        source.sendFailure(Component.literal(exception.getMessage()));
                        return 0;
                    }
                }))
                .then(Commands.literal("restore").executes(context -> {
                    CommandSourceStack source = context.getSource();
                    DevilsPalmTerrain.restore(source.getLevel());
                    if (!DevilsPalmTerrain.isBusy(source.getLevel())) {
                        source.sendFailure(Component.literal("No manifested Palm to restore"));
                        return 0;
                    }
                    source.sendSuccess(() -> Component.literal(DevilsPalmTerrain.status(source.getLevel())), false);
                    return 1;
                }))
                .then(Commands.literal("check")
                        .executes(context -> check(context.getSource(), BlockPos.containing(context.getSource().getPosition())))
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(context -> check(context.getSource(), BlockPosArgument.getBlockPos(context, "pos"))))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> terrainCommand(String name, boolean start) {
        return Commands.literal(name)
                .executes(context -> terrain(context.getSource(), "open_palm_large", forward(context.getSource()), start))
                .then(Commands.argument("template", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            try {
                                return SharedSuggestionProvider.suggest(DevilsPalmCatalog.read(context.getSource().getLevel())
                                        .stream().map(DevilsPalmCatalog.Formation::name), builder);
                            } catch (IllegalArgumentException exception) {
                                return builder.buildFuture();
                            }
                        })
                        .executes(context -> terrain(context.getSource(), StringArgumentType.getString(context, "template"), forward(context.getSource()), start))
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(context -> terrain(context.getSource(), StringArgumentType.getString(context, "template"), BlockPosArgument.getBlockPos(context, "pos"), start))));
    }

    private static BlockPos forward(CommandSourceStack source) {
        return BlockPos.containing(source.getPosition()).relative(Direction.fromYRot(source.getRotation().y), 80);
    }

    private static int search(CommandSourceStack source) {
        try {
            boolean found = DevilsPalmManager.search(source.getLevel(), BlockPos.containing(source.getPosition()));
            if (!found) {
                source.sendFailure(Component.literal(DevilsPalmManager.lastSearch(source.getLevel())));
                return 0;
            }
            return locate(source, true);
        } catch (IllegalArgumentException | IllegalStateException | IOException exception) {
            source.sendFailure(Component.literal(String.valueOf(exception.getMessage())));
            return 0;
        }
    }

    private static int awaken(CommandSourceStack source) {
        if (!source.getLevel().dimension().equals(Level.OVERWORLD)) {
            source.sendFailure(Component.literal("Overworld only"));
            return 0;
        }
        var level = source.getLevel();
        DevilsPalmSavedData data = DevilsPalmSavedData.get(level);
        BlockPos origin = BlockPos.containing(source.getPosition());
        try {
            BlockPos pos = DevilsPalmManager.dormantCenter(level);
            if (pos == null && data.selected() != null && !data.selectedManifested()) {
                if (!DevilsPalmManager.search(level, origin)) {
                    source.sendFailure(Component.literal(DevilsPalmManager.lastSearch(level)));
                    return 0;
                }
                pos = DevilsPalmManager.dormantCenter(level);
            }
            if (pos == null) {
                source.sendFailure(Component.literal("No dormant Palm. Run /roundaboutDevilsPalm search first."));
                return 0;
            }
            try {
                DevilsPalmTerrain.planOrStart(level, pos, DevilsPalmManager.dormantTemplate(level), true);
            } catch (IllegalArgumentException exception) {
                DevilsPalmManager.requestRelocation(level, String.valueOf(exception.getMessage()));
                if (DevilsPalmManager.search(level, origin)) {
                    source.sendSuccess(() -> Component.literal("Original site rejected. Dormant Palm relocated; run awaken again or approach the new location."), false);
                    return locate(source, true);
                }
                source.sendFailure(Component.literal(DevilsPalmManager.lastSearch(level)));
                return 0;
            }
            data.markManifested();
            source.sendSuccess(() -> Component.literal("Manifestation started. Use /roundaboutDevilsPalm info to monitor growth."), false);
            return locate(source, false);
        } catch (IllegalArgumentException | IllegalStateException | IOException exception) {
            source.sendFailure(Component.literal(String.valueOf(exception.getMessage())));
            return 0;
        }
    }

    private static int locate(CommandSourceStack source, boolean dormantOnly) {
        if (!source.getLevel().dimension().equals(Level.OVERWORLD)) {
            source.sendFailure(Component.literal("Devil's Palms only occur in the Overworld"));
            return 0;
        }
        BlockPos visible = dormantOnly ? null : DevilsPalmTerrain.visibleCenter(source.getLevel()).orElse(null);
        BlockPos pos = visible == null ? DevilsPalmManager.dormantCenter(source.getLevel()) : visible;
        if (pos == null) {
            if (DevilsPalmSavedData.get(source.getLevel()).relocating()) {
                source.sendFailure(Component.literal("Dormant Devil's Palm is relocating. Use /roundaboutDevilsPalm search to retry now."));
            } else {
                source.sendFailure(Component.literal("No " + (dormantOnly ? "dormant " : "active or dormant ") + "Devil's Palm available. Use /roundaboutDevilsPalm info or search."));
            }
            return 0;
        }
        int distance = (int)Math.min(Integer.MAX_VALUE, Math.floor(Math.hypot(source.getPosition().x - pos.getX(), source.getPosition().z - pos.getZ())));
        BlockPos arrival = arrivalPoint(source, pos);
        Component coordinates = Component.literal("[" + pos.getX() + ", ~, " + pos.getZ() + "]")
                .withStyle(ChatFormatting.GREEN);
        String kind = visible == null ? "Dormant Devil's Palm" : "Devil's Palm";
        source.sendSuccess(() -> Component.literal(kind + " is at ").append(coordinates).append(" (" + distance + " blocks away)"), false);
        if (arrival != null) {
            Component approach = Component.literal("[" + arrival.toShortString() + "]")
                    .withStyle(style -> style.withColor(ChatFormatting.GREEN).withUnderlined(true)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                    "/tp @s " + (arrival.getX() + 0.5) + " " + arrival.getY() + " " + (arrival.getZ() + 0.5))));
            source.sendSuccess(() -> Component.literal("Safe approach: ").append(approach), false);
        } else {
            source.sendSuccess(() -> Component.literal("No loaded safe arrival point. Approach from 65-90 blocks away."), false);
        }
        return Math.max(1, distance);
    }

    private static BlockPos arrivalPoint(CommandSourceStack source, BlockPos center) {
        ServerLevel level = source.getLevel();
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;
        for (int dx = -88; dx <= 88; dx += 8) {
            for (int dz = -88; dz <= 88; dz += 8) {
                int radiusSquared = dx * dx + dz * dz;
                if (radiusSquared < 64 * 64 || radiusSquared > 88 * 88
                        || Math.abs(dx) <= DevilsPalmManager.SAFE_DISTANCE && Math.abs(dz) <= DevilsPalmManager.SAFE_DISTANCE) continue;
                int x = center.getX() + dx, z = center.getZ() + dz;
                if (level.getChunkSource().getChunkNow(x >> 4, z >> 4) == null) continue;
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                if (y <= level.getMinBuildHeight() || y + 1 >= level.getMaxBuildHeight()) continue;
                BlockPos feet = new BlockPos(x, y, z);
                if (!level.getWorldBorder().isWithinBounds(feet)) continue;
                BlockPos floor = feet.below();
                BlockState ground = level.getBlockState(floor);
                if (!ground.getFluidState().isEmpty() || !ground.isFaceSturdy(level, floor, Direction.UP)
                        || ground.is(Blocks.CACTUS) || ground.is(Blocks.MAGMA_BLOCK)
                        || ground.is(Blocks.CAMPFIRE) || ground.is(Blocks.SOUL_CAMPFIRE)
                        || !level.isEmptyBlock(feet) || !level.isEmptyBlock(feet.above())) continue;
                double distance = source.getPosition().distanceToSqr(x + 0.5, y, z + 0.5);
                if (distance < bestDistance) {
                    best = feet;
                    bestDistance = distance;
                }
            }
        }
        return best;
    }

    private static int terrain(CommandSourceStack source, String template, BlockPos pos, boolean start) {
        try {
            String result = DevilsPalmTerrain.planOrStart(source.getLevel(), pos, template, start);
            if (start) {
                BlockPos dormant = DevilsPalmManager.dormantCenter(source.getLevel());
                BlockPos visible = DevilsPalmTerrain.visibleCenter(source.getLevel()).orElse(null);
                if (dormant != null && visible != null && dormant.getX() == visible.getX() && dormant.getZ() == visible.getZ())
                    DevilsPalmSavedData.get(source.getLevel()).markManifested();
                source.sendSuccess(() -> Component.literal("Manifestation started. Use /roundaboutDevilsPalm info to monitor growth."), false);
                locate(source, false);
            } else {
                source.sendSuccess(() -> Component.literal("Plan passed: " + result), false);
            }
            return 1;
        } catch (IllegalArgumentException | IllegalStateException | IOException exception) {
            source.sendFailure(Component.literal(exception.getMessage() == null ? "Palm terrain operation failed" : exception.getMessage()));
            return 0;
        }
    }

    private static int info(CommandSourceStack source) {
        DevilsPalmSavedData data = DevilsPalmSavedData.get(source.getLevel());
        BlockPos selected = DevilsPalmManager.dormantCenter(source.getLevel());
        source.sendSuccess(() -> Component.literal("Dormant target: " + (selected == null ? (data.relocating() ? "relocating" : "none") : selected.toShortString()) + "; known locations: " + data.locationCount() + "; recorded chunks: " + data.modifiedCount()), false);
        source.sendSuccess(() -> Component.literal("Automatic: " + data.automatic() + "; " + DevilsPalmTerrain.status(source.getLevel())), false);
        source.sendSuccess(() -> Component.literal("Last search: " + DevilsPalmManager.lastSearch(source.getLevel())), false);
        return 1;
    }

    private static int check(CommandSourceStack source, BlockPos pos) {
        long start = System.nanoTime();
        DevilsPalmLocationFinder.Result result = DevilsPalmLocationFinder.preliminary(source.getLevel(), pos);
        long elapsed = (System.nanoTime() - start) / 1_000_000L;
        String message = (result.accepted() ? "Preliminary pass: " : "Rejected: ") + result.reason()
                + " at " + result.position().toShortString() + " (" + elapsed + " ms)";
        if (result.accepted()) source.sendSuccess(() -> Component.literal(message), false);
        else source.sendFailure(Component.literal(message));
        return result.accepted() ? 1 : 0;
    }
}
