package net.hydra.jojomod.event;

import net.hydra.jojomod.access.IFoodData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public class SavedSecondPlayer extends SavedSecondLiving {

    public int foodLevel;
    public float saturationLevel;
    public float exhaustionLevel;

    public SavedSecondPlayer(float headYRotation, Vec2 rotationVec, Vec3 position, Vec3 deltaMovement,
                             Collection<MobEffectInstance> activeEffects, float health,
                             int foodLevel, float saturationLevel, float exhaustionLevel) {
        super(headYRotation, rotationVec, position, deltaMovement, activeEffects, health);
        this.foodLevel = foodLevel;
        this.saturationLevel = saturationLevel;
        this.exhaustionLevel = exhaustionLevel;
    }

    @Override
    public void loadTime(Entity ent) {
        super.loadTime(ent);
        if (ent instanceof Player PE) {
            IFoodData ifp = ((IFoodData) PE.getFoodData());
            ifp.roundabout$setExhaustionLevel(this.exhaustionLevel);
            ifp.roundabout$setSaturationLevel(this.saturationLevel);
            ifp.roundabout$setFoodLevel(this.foodLevel);
        }
    }
}