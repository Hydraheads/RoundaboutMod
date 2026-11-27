package net.hydra.jojomod.event.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.hydra.jojomod.event.powers.jumpingJackFlash.AirPressureSystem;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityAccess;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityState;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravitySync;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public final class JJFAirPressureCommand {

    private JJFAirPressureCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("RoundaboutAirPressure")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("Clear")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(context -> clear(
                                        context.getSource(),
                                        EntityArgument.getPlayers(context, "targets")))))
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(context -> apply(
                                context.getSource(),
                                EntityArgument.getPlayers(context, "targets"),
                                0))
                        .then(Commands.argument("percent", IntegerArgumentType.integer(0, 100))
                                .executes(context -> apply(
                                        context.getSource(),
                                        EntityArgument.getPlayers(context, "targets"),
                                        IntegerArgumentType.getInteger(context, "percent"))))));
    }

    private static int apply(CommandSourceStack source, Collection<ServerPlayer> targets, int percent) {
        int ticks = Math.round(percent * AirPressureSystem.FULL_PRESSURE_TICKS / 100.0F);
        int count = 0;

        for (ServerPlayer target : targets) {
            if (!(target instanceof ZeroGravityAccess access)) {
                continue;
            }

            ZeroGravityState state = access.jjf$getZeroGravityState();
            if (AirPressureSystem.isJJFUser(target)) {
                continue;
            }
            state.setCommandPressureActive(true);
            state.setAirPressureTicks(ticks);
            ZeroGravitySync.send(target, state);
            count++;
        }

        int affected = count;
        source.sendSuccess(() -> Component.literal(
                "Set JJF air pressure to " + percent + "% for " + affected + " player(s)."), false);
        return count;
    }

    private static int clear(CommandSourceStack source, Collection<ServerPlayer> targets) {
        int count = 0;

        for (ServerPlayer target : targets) {
            if (!(target instanceof ZeroGravityAccess access)) {
                continue;
            }

            ZeroGravityState state = access.jjf$getZeroGravityState();
            if (!state.isCommandPressureActive()) {
                continue;
            }

            state.setCommandPressureActive(false);
            if (!state.isSpread()) {
                state.resetAirPressureTicks();
            }
            ZeroGravitySync.send(target, state);
            count++;
        }

        int affected = count;
        source.sendSuccess(() -> Component.literal(
                "Cleared command-applied air pressure for " + affected + " player(s)."), false);
        return count;
    }
}
