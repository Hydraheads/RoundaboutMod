package net.hydra.jojomod.event;

import net.hydra.jojomod.access.IAreaOfEffectCloud;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class SavedSecondLiving extends SavedSecond {

    public Collection<MobEffectInstance> activeEffects;
    public float health;

    public SavedSecondLiving(float headYRotation, Vec2 rotationVec, Vec3 position,
                             Map<MobEffect, MobEffectInstance> activeEffects, float health){
        super(headYRotation,rotationVec,position);
        Collection<MobEffectInstance> effects = activeEffects.values();

        if (!effects.isEmpty()) {
            for (MobEffectInstance value : effects) {
                if (!value.isInfiniteDuration()) {
                    this.activeEffects.add(new MobEffectInstance(value));
                }
            }
        }
        this.health = health;
    }
}
