package com.example.jjf.zero_gravity;

import com.example.jjf.registry.ModItems;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public final class AirPressureSystem {

    public static final int GRACE_TICKS = 20 * 15;
    public static final int FULL_PRESSURE_TICKS = 20 * 60;

    private static final int DAMAGE_INTERVAL_TICKS = 20 * 5;
    private static final float DAMAGE_PER_PULSE_FRACTION = 0.10F;

    private static final ResourceKey<DamageType> AIR_PRESSURE_DAMAGE =
            ResourceKey.create(
                    Registries.DAMAGE_TYPE,
                    new ResourceLocation("jjf", "air_pressure")
            );

    private AirPressureSystem() {
    }

    public static void tick(LivingEntity entity, ZeroGravityState state) {
        if (entity.level().isClientSide()) {
            return;
        }

        if (!isAirPressureActive(entity, state)) {
            state.resetAirPressureTicks();
            return;
        }

        state.incrementAirPressureTicks();

        if (entity instanceof Player player && player.isCreative()) {
            return;
        }

        int ticks = state.getAirPressureTicks();
        DamageSource damageSource = getAirPressureDamage(entity);

        if (ticks >= FULL_PRESSURE_TICKS) {
            finishAirPressure(entity, damageSource);
            return;
        }

        if (ticks < GRACE_TICKS || (ticks - GRACE_TICKS) % DAMAGE_INTERVAL_TICKS != 0) {
            return;
        }

        float damage = entity.getMaxHealth() * DAMAGE_PER_PULSE_FRACTION;

        if (damage > 0.0F) {
            entity.hurt(damageSource, damage);
        }
    }

    private static DamageSource getAirPressureDamage(LivingEntity entity) {
        return new DamageSource(
                entity.level().registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(AIR_PRESSURE_DAMAGE)
        );
    }

    private static void finishAirPressure(LivingEntity entity, DamageSource damageSource) {
        if (entity.isInvulnerableTo(damageSource)) {
            return;
        }

        entity.hurt(damageSource,
                Math.max(100.0F, entity.getHealth() + entity.getAbsorptionAmount() + 100.0F));

        if (entity.isAlive()) {
            entity.setHealth(0.0F);
            entity.die(damageSource);
        }
    }

    public static float getDamageStartFraction() {
        return GRACE_TICKS / (float) FULL_PRESSURE_TICKS;
    }

    public static float getDamageProgress(int ticks) {
        int damageDuration = FULL_PRESSURE_TICKS - GRACE_TICKS;
        if (damageDuration <= 0) {
            return 1.0F;
        }

        return Math.max(0.0F, Math.min(1.0F,
                (ticks - GRACE_TICKS) / (float) damageDuration));
    }

    public static boolean shouldShowMeter(LivingEntity entity, ZeroGravityState state) {
        return isAirPressureActive(entity, state);
    }

    public static boolean isAirPressureActive(LivingEntity entity, ZeroGravityState state) {
        return state.isSpread();
    }

    public static float getPressure(ZeroGravityState state) {
        return Math.min(1.0F, Math.max(0.0F,
                state.getAirPressureTicks() / (float) FULL_PRESSURE_TICKS));
    }

    public static int getPressurePercent(ZeroGravityState state) {
        return Math.round(getPressure(state) * 100.0F);
    }

    public static boolean isJJFUser(LivingEntity entity) {
        if (!(entity instanceof StandUser standUser)) {
            return false;
        }

        ItemStack disc = standUser.roundabout$getStandDisc();
        if (!disc.isEmpty()) {
            if (ModItems.JJF_STAND_DISC != null && disc.is(ModItems.JJF_STAND_DISC)) {
                return true;
            }
            if (ModItems.JJF_STAND_DISC_MAX != null && disc.is(ModItems.JJF_STAND_DISC_MAX)) {
                return true;
            }
        }

        return standUser.roundabout$getStandPowers() != null
                && standUser.roundabout$getStandPowers().getClass().getName().equals(
                        "com.example.jjf.stand.powers.PowersJumpinJackFlash");
    }
}
