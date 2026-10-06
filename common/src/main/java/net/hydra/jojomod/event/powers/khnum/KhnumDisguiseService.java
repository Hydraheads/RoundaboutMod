package net.hydra.jojomod.event.powers.khnum;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersKhnum;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public final class KhnumDisguiseService {
    private static final Pattern USERNAME = Pattern.compile("[A-Za-z0-9_]{3,16}");
    private static final Set<UUID> PENDING = ConcurrentHashMap.newKeySet();

    private KhnumDisguiseService() { }

    public static void request(ServerPlayer player, String requestedName) {
        String name = requestedName == null ? "" : requestedName.trim();
        if (!USERNAME.matcher(name).matches()) {
            player.sendSystemMessage(Component.translatable("roundabout.khnum.disguise.invalid"));
            return;
        }
        if (!isKhnum(player)) return;
        PowersKhnum powers = (PowersKhnum) ((StandUser) player).roundabout$getStandPowers();
        if (powers.onCooldown(PowerIndex.SKILL_1)) return;
        MinecraftServer server = player.getServer();
        if (server == null || !PENDING.add(player.getUUID())) return;
        CompletableFuture.supplyAsync(() -> findProfile(server, name), Util.backgroundExecutor())
                .exceptionally(error -> Optional.empty())
                .thenAcceptAsync(profile -> {
                    if (server.getPlayerList().getPlayer(player.getUUID()) == player && isKhnum(player)) {
                        if (profile.isPresent()) {
                            ((StandUser) player).roundabout$setDisguise(profile.get());
                            powers.setCooldown(PowerIndex.SKILL_1, 100);
                            player.level().playSound(null, player.blockPosition(), ModSounds.KHNUM_DISGUISE_EVENT,
                                    SoundSource.PLAYERS, 1.0F, 1.0F);
                        } else {
                            player.sendSystemMessage(Component.translatable("roundabout.khnum.disguise.not_found"));
                        }
                    }
                }, server)
                .whenComplete((profile, error) -> PENDING.remove(player.getUUID()));
    }

    private static boolean isKhnum(ServerPlayer player) {
        return ((StandUser) player).roundabout$getStandPowers() instanceof PowersKhnum;
    }

    private static Optional<GameProfile> findProfile(MinecraftServer server, String name) {
        AtomicReference<GameProfile> result = new AtomicReference<>();
        server.getProfileRepository().findProfilesByNames(new String[]{name}, Agent.MINECRAFT,
                new ProfileLookupCallback() {
                    @Override
                    public void onProfileLookupSucceeded(GameProfile profile) {
                        result.set(profile);
                    }

                    @Override
                    public void onProfileLookupFailed(GameProfile profile, Exception exception) { }
                });
        return Optional.ofNullable(result.get());
    }
}
