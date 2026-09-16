package net.hydra.jojomod.event.powers;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.zetalasis.networking.message.api.ModMessageEvents;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public final class DiverDownDisguiseService {
    private static final Pattern USERNAME = Pattern.compile("[A-Za-z0-9_]{3,16}");

    private DiverDownDisguiseService() {
    }

    /**
     * Called on the server when the DiverDownDisguise packet is received.
     */
    public static void request(ServerPlayer player, String requestedName) {
        String name = requestedName == null ? "" : requestedName.trim();
        if (!USERNAME.matcher(name).matches()) {
            player.sendSystemMessage(Component.translatable("roundabout.whitesnake.disguise.invalid"));
            return;
        }
        if (!canDisguise(player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        // Asynchronously lookup the player's GameProfile from Mojang
        CompletableFuture.supplyAsync(() -> findProfile(server, name), Util.backgroundExecutor())
                .thenAccept(profile -> server.execute(() -> apply(player, profile)));
    }

    private static boolean canDisguise(ServerPlayer player) {
        return ((StandUser) player).roundabout$getStandPowers() instanceof PowersDiverDown powers
                && powers.isDiveActive() && powers.submergedTarget != null;
    }

    private static GameProfile findProfile(MinecraftServer server, String name) {
        AtomicReference<GameProfile> result = new AtomicReference<>();
        server.getProfileRepository().findProfilesByNames(new String[]{name}, Agent.MINECRAFT,
                new ProfileLookupCallback() {
                    @Override
                    public void onProfileLookupSucceeded(GameProfile profile) {
                        result.set(profile);
                    }

                    @Override
                    public void onProfileLookupFailed(GameProfile profile, Exception exception) {
                    }
                });
        return result.get();
    }

    private static void apply(ServerPlayer player, GameProfile profile) {
        if (!player.isAlive() || !canDisguise(player)) return;
        if (profile == null) {
            player.sendSystemMessage(Component.translatable("roundabout.whitesnake.disguise.not_found"));
            return;
        }

        PowersDiverDown powers = (PowersDiverDown) ((StandUser) player).roundabout$getStandPowers();
        Entity target = powers.submergedTarget;
        if (target == null || !target.isAlive()) return;

        // it's disguising time
        ((StandUser) target).roundabout$setDisguise(profile);
        powers.applyDisguiseToTarget(target, profile);
    }
}