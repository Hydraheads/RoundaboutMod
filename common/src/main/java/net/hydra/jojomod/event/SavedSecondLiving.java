package net.hydra.jojomod.event;

import com.google.common.collect.Maps;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SavedSecondLiving extends SavedSecond {

    public Collection<MobEffectInstance> activeEffects =new ArrayList<>();
    public float health;
    public int onFireTicks;
    public int onStandFireTicks;
    public int gasolineTicks;


    public SavedSecondLiving(float headYRotation, Vec2 rotationVec, Vec3 position, Vec3 deltaMovement, float fallDistance,
                             ResourceKey<DimensionType> dimensionId,
                             Collection<MobEffectInstance> activeEffects, float health, int onFireTicks, int onStandFireTicks,
                             int gasolineTicks){
        super(headYRotation,rotationVec,position,deltaMovement,fallDistance,dimensionId);

        List<MobEffectInstance> effects = new ArrayList<>(activeEffects.stream().toList());
        if (!effects.isEmpty()) {
            for (MobEffectInstance value : effects) {
                if (!value.isInfiniteDuration()) {
                    this.activeEffects.add(new MobEffectInstance(value));
                }
            }
        }
        this.health = health;
        this.onFireTicks = onFireTicks;
        this.onStandFireTicks = onStandFireTicks;
        this.gasolineTicks = gasolineTicks;
    }
    @Override
    public void loadTime(Entity ent){
        if (ent != null && dimensionTypeId != ent.level().dimensionTypeId())
            return;
        super.loadTime(ent);

        if (ent instanceof LivingEntity LE) {
            List<MobEffectInstance> effects = new ArrayList<>(LE.getActiveEffects().stream().toList());
            if (!effects.isEmpty()) {
                for (MobEffectInstance value : effects) {
                    if (!value.isInfiniteDuration()) {
                        LE.removeEffect(value.getEffect());
                    }
                }
            }

            List<MobEffectInstance> effects2 = new ArrayList<>(this.activeEffects.stream().toList());
            if (!effects2.isEmpty()) {
                for (MobEffectInstance value : effects2) {
                    if (!value.isInfiniteDuration()) {
                        LE.addEffect(value);
                    }
                }
            }

            LE.setHealth(this.health);

            LE.setRemainingFireTicks(onFireTicks);
            StandUser user = ((StandUser) LE);
            user.roundabout$setRemainingStandFireTicks(onStandFireTicks);
            user.roundabout$setGasolineTime(gasolineTicks);

        }
    }
}
