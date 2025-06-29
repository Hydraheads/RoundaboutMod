package net.hydra.jojomod.event;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Map;

public class SavedSecondPlayer extends SavedSecondLiving{

    public int foodLevel;
    public float saturationLevel;
    public float exhaustionLevel;
    public SavedSecondPlayer(float headYRotation, Vec2 rotationVec, Vec3 position,
                             Collection<MobEffectInstance> activeEffects, float health,
                             int foodLevel, float saturationLevel, float exhaustionLevel){
        super(headYRotation,rotationVec,position,activeEffects,health);
        this.foodLevel = foodLevel;
        this.saturationLevel = saturationLevel;
        this.exhaustionLevel = exhaustionLevel;
    }
}
