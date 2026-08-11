package net.hydra.jojomod.client;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public final class HallucinationRenderOffset {
    private HallucinationRenderOffset() {
    }

    public static Vec3 forEntity(Entity rendered) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null
                || rendered == minecraft.getCameraEntity() || !(rendered instanceof LivingEntity)) {
            return Vec3.ZERO;
        }
        if (((StandUser) minecraft.player).roundabout$getStand() == rendered) return Vec3.ZERO;
        MobEffectInstance effect = minecraft.player.getEffect(ModEffects.HALLUCINATION);
        if (effect == null || effect.getAmplifier() < 1) return Vec3.ZERO;

        int level = Math.min(5, effect.getAmplifier() + 1);
        double maximumDistance = level >= 4 ? 2.0D : 1.0D;
        long interval = minecraft.level.getGameTime() / 40L;
        long seed = interval * 341873128712L ^ (long) rendered.getId() * 132897987541L
                ^ minecraft.player.getUUID().getLeastSignificantBits();
        Random random = new Random(seed);
        double distance = maximumDistance * (0.65D + random.nextDouble() * 0.35D);
        double angle = random.nextDouble() * Math.PI * 2.0D;
        return new Vec3(Math.cos(angle) * distance, 0.0D, Math.sin(angle) * distance);
    }
}
