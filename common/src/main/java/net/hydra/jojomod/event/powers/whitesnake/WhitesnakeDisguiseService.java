package net.hydra.jojomod.event.powers.whitesnake;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.Util;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public final class WhitesnakeDisguiseService {
    private static final int DISGUISE_COOLDOWN = 100;
    private static final Pattern USERNAME = Pattern.compile("[A-Za-z0-9_]{3,16}");

    private WhitesnakeDisguiseService() {
    }

    public static void request(ServerPlayer player, String requestedName) {
        String name = requestedName == null ? "" : requestedName.trim();
        if (!USERNAME.matcher(name).matches()) {
            player.sendSystemMessage(Component.translatable("roundabout.whitesnake.disguise.invalid"));
            return;
        }
        if (!canDisguise(player)) return;
        MinecraftServer server = player.getServer();
        if (server == null) return;
        CompletableFuture.supplyAsync(() -> findProfile(server, name), Util.backgroundExecutor())
                .thenAccept(result -> server.execute(() -> apply(player, result)));
    }

    private static boolean canDisguise(ServerPlayer player) {
        return ((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting() && powers.canExecuteMoveWithLevel(powers.getHallucinatoryDisguiseLevel())
                && !powers.onCooldown(PowerIndex.SKILL_4)
                && ((StandUser) player).roundabout$getStand() instanceof WhitesnakeEntity;
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
                    public void onProfileLookupFailed(GameProfile profile, Exception exception) {
                    }
                });
        return Optional.ofNullable(result.get());
    }

    private static void apply(ServerPlayer player, Optional<GameProfile> result) {
        if (!player.isAlive() || !canDisguise(player)) return;
        if (result.isEmpty()) {
            player.sendSystemMessage(Component.translatable("roundabout.whitesnake.disguise.not_found"));
            return;
        }
        StandEntity stand = ((StandUser) player).roundabout$getStand();
        if (!(stand instanceof WhitesnakeEntity whitesnake)) return;
        whitesnake.setDisguise(result.get());
        player.level().playSound(null, whitesnake.blockPosition(), ModSounds.WHITESNAKE_HALLUCINATION_DISGUISE_EVENT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        PowersWhitesnake powers = (PowersWhitesnake) ((StandUser) player).roundabout$getStandPowers();
        powers.setCooldown(PowerIndex.SKILL_4, DISGUISE_COOLDOWN);
        S2CPacketUtil.sendCooldownSyncPacket(player, PowerIndex.SKILL_4, DISGUISE_COOLDOWN);
    }
}
