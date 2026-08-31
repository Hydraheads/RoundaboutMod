package net.hydra.jojomod.event.powers.whitesnake;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.WeakHashMap;

public final class AcidExposureTracker {
    private static final Map<LivingEntity, Exposure> EXPOSURES = new WeakHashMap<>();

    private AcidExposureTracker() {
    }

    public static void touch(LivingEntity living, long gameTime) {
        Exposure exposure = EXPOSURES.computeIfAbsent(living, ignored -> new Exposure());
        if (exposure.lastSeen == gameTime) return;
        if (exposure.lastSeen < gameTime - 1L) {
            exposure.ticks = 0;
            MobEffectInstance current = living.getEffect(ModEffects.HALLUCINATION);
            if (current == null) {
                living.addEffect(HallucinationEffect.createInstance(200, 0));
            }
        }
        exposure.lastSeen = gameTime;
        exposure.ticks++;
        if (exposure.ticks < ClientNetworking.getAppropriateConfig().whitesnakeSettings
                .hallucinatoryAcidStageUpTime) return;

        exposure.ticks = 0;
        MobEffectInstance current = living.getEffect(ModEffects.HALLUCINATION);
        int amplifier = current == null ? 0 : Math.min(
                HallucinationEffect.MAX_LEVEL - 1, current.getAmplifier() + 1);
        int duration = (current == null ? 0 : current.getDuration()) + 200;
        living.addEffect(HallucinationEffect.createInstance(Math.max(200, duration), amplifier));
    }

    private static final class Exposure {
        private long lastSeen = Long.MIN_VALUE;
        private int ticks;
    }
}
