package net.hydra.jojomod.event.powers.disc;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.util.config.Config;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public final class DiscSealController {
    private DiscSealController() {
    }

    public static boolean seal(LivingEntity target, byte type) {
        if (!WhitesnakeDiscUtil.isBodyDiscEnabled(type)) return false;
        Config.WhitesnakeSettings config = ClientNetworking.getAppropriateConfig().whitesnakeSettings;
        if (!config.discSealing || (config.discSealingPlayersOnly && !(target instanceof Player))) return false;
        if (config.discSealRequiresHallucination
                && !target.hasEffect(ModEffects.HALLUCINATION)) return false;
        int duration = duration(target, type, config);
        if (duration <= 0) return false;
        if (type == WhitesnakeDiscUtil.STAND) {
            StandUser standUser = (StandUser) target;
            if (standUser.roundabout$getStandDisc().isEmpty()) return false;
            standUser.roundabout$setSealedTicks(duration);
            if (!((DiscBearer) target).roundabout$hasMemoryDisc()) {
                if (target instanceof Mob mob) {
                    mob.setTarget(null);
                    mob.getNavigation().stop();
                    if (mob.isNoAi()) mob.setNoAi(false);
                } else if (target instanceof ServerPlayer player) {
                    MemoryAiController.clearPlayerState(player);
                }
            }
            return true;
        }

        DiscBearer bearer = (DiscBearer) target;
        boolean ownsDisc = switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> WhitesnakeDiscUtil.canCarrySightDisc(target)
                    && bearer.roundabout$ownsSightDisc();
            case WhitesnakeDiscUtil.MEMORY -> bearer.roundabout$ownsMemoryDisc();
            case WhitesnakeDiscUtil.HEARING -> bearer.roundabout$ownsHearingDisc();
            default -> false;
        };
        if (!ownsDisc) return false;
        bearer.roundabout$setDiscSeal(type, duration, duration);
        if (type == WhitesnakeDiscUtil.SIGHT) {
            ((StandUser) target).roundabout$deeplyRemoveAttackTarget();
        } else if (type == WhitesnakeDiscUtil.MEMORY) {
            ((StandUser) target).roundabout$deeplyRemoveAttackTarget();
            if (target instanceof Mob mob) {
                mob.setTarget(null);
                mob.getNavigation().stop();
                if (mob.isNoAi()) mob.setNoAi(false);
            } else if (target instanceof ServerPlayer player) {
                MemoryAiController.clearPlayerState(player);
            }
        }
        return true;
    }

    public static int duration(byte type, Config.WhitesnakeSettings config) {
        return switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> config.sightDiscSealTime;
            case WhitesnakeDiscUtil.MEMORY -> config.memoryDiscSealTime;
            case WhitesnakeDiscUtil.HEARING -> config.hearingDiscSealTime;
            default -> config.standDiscSealTime;
        };
    }

    private static int duration(LivingEntity target, byte type, Config.WhitesnakeSettings config) {
        int duration = duration(type, config);
        MobEffectInstance hallucination = target.getEffect(ModEffects.HALLUCINATION);
        if (hallucination == null || duration <= 0) return duration;
        int level = Math.min(5, hallucination.getAmplifier() + 1);
        int extraPercent = Math.min(100, level * config.discSealHallucinationMultiplierPerLevel);
        return Math.round(duration * (1.0F + extraPercent * 0.01F));
    }
}
