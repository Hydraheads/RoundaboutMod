package net.hydra.jojomod.event;

import net.hydra.jojomod.access.ICreeper;
import net.hydra.jojomod.access.IFoodData;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public class SavedSecondCreeper extends SavedSecondLiving {

    public int swell;


    public SavedSecondCreeper(float headYRotation, Vec2 rotationVec, Vec3 position, Vec3 deltaMovement, float fallDistance,
                              ResourceKey<DimensionType> dimensionId, Direction gravityDirection,
                              Collection<MobEffectInstance> activeEffects, float health, int onFireTicks,
                              int onStandFireTicks, byte onStandFireType, int gasolineTicks, int airtime, byte locacaca,
                              int leapTicks, byte bubbleEncased,
                              int swell) {
        super(headYRotation, rotationVec, position, deltaMovement, fallDistance, dimensionId, gravityDirection, activeEffects, health, onFireTicks,
                onStandFireTicks, onStandFireType, gasolineTicks, airtime, locacaca, leapTicks, bubbleEncased);
        this.swell = swell;
    }

    @Override
    public void loadTime(Entity ent) {
        if (ent != null && dimensionTypeId != ent.level().dimensionTypeId())
            return;
        super.loadTime(ent);
        if (ent instanceof Creeper CE) {
            ((ICreeper)CE).roundabout$setSwell(swell);
        }
    }
}