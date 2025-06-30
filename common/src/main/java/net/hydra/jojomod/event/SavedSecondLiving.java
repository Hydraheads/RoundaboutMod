package net.hydra.jojomod.event;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SavedSecondLiving extends SavedSecond {

    public Collection<MobEffectInstance> activeEffects;
    public float health;

    public SavedSecondLiving(float headYRotation, Vec2 rotationVec, Vec3 position,
                             Collection<MobEffectInstance> activeEffects, float health){
        super(headYRotation,rotationVec,position);

        List<MobEffectInstance> effects = new ArrayList<>(activeEffects.stream().toList());
        if (!effects.isEmpty()) {
            for (MobEffectInstance value : effects) {
                if (!value.isInfiniteDuration()) {
                    this.activeEffects.add(new MobEffectInstance(value));
                }
            }
        }
        this.health = health;
    }
    @Override
    public void loadTime(Entity ent){
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


        }
    }
}
