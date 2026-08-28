package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.util.config.Config;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public final class HallucinationEffect extends MobEffect {
    public static final int MAX_LEVEL = 3;
    private static final int CONFUSION_PULSE_TICKS = 7;
    private static final UUID SLOWNESS_ID = UUID.fromString("BBA1462A-85ED-4AD4-A6A0-08BEEA9C9177");

    public HallucinationEffect() {
        super(MobEffectCategory.HARMFUL, 0xEEEAF7);
    }

    public static MobEffectInstance createInstance(int duration, int amplifier) {
        Config.WhitesnakeSettings config = ClientNetworking.getAppropriateConfig().whitesnakeSettings;
        amplifier = Math.min(MAX_LEVEL - 1, Math.max(0, amplifier));
        return new MobEffectInstance(
                ModEffects.HALLUCINATION,
                duration, amplifier, false, !config.hideHallucinationParticles,
                !config.hallucinationHidesEffects);
    }

    public static boolean hasDistortion(MobEffectInstance effect) {
        return effect != null && effect.getAmplifier() < MAX_LEVEL - 1;
    }

    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap attributes, int amplifier) {
        if (amplifier != 1) return;
        AttributeInstance movement = attributes.getInstance(Attributes.MOVEMENT_SPEED);
        if (movement != null && movement.getModifier(SLOWNESS_ID) == null) {
            movement.addTransientModifier(new AttributeModifier(SLOWNESS_ID,
                    "Hallucination movement slowdown", -0.06D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap attributes, int amplifier) {
        AttributeInstance movement = attributes.getInstance(Attributes.MOVEMENT_SPEED);
        if (movement != null) movement.removeModifier(SLOWNESS_ID);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        MobEffectInstance hallucination = living.getEffect(this);
        if (hallucination == null) return;
        int duration = hallucination.getDuration();
        if (!living.level().isClientSide() && living instanceof Mob mob && amplifier < 2) {
            int interval = amplifier == 1 ? 60 : 120;
            if (living.tickCount % interval == 0) {
                IMob mobData = (IMob) mob;
                mobData.roundabout$setConfusionTicks(Math.max(
                        mobData.roundabout$getConfusionTicks(), CONFUSION_PULSE_TICKS));
            }
        }
        if (amplifier >= 2) {
            MobEffectInstance dreaming = living.getEffect(
                    ModEffects.DREAMING);
            if (dreaming == null || dreaming.getDuration() < duration - 1) {
                living.addEffect(DreamingEffect.createInstance(duration));
            }
        }
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinationAppliesMelting) {
            MobEffectInstance melting = living.getEffect(ModEffects.MELTING);
            if (melting == null || melting.getAmplifier() < amplifier
                    || melting.getAmplifier() == amplifier && melting.getDuration() < duration - 1) {
                living.addEffect(new MobEffectInstance(ModEffects.MELTING, duration, amplifier));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
