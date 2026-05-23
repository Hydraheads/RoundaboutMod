package net.hydra.jojomod.entity.projectile;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class UltravioletProjectile extends RoundaboutGeneralProjectile{
    public UltravioletProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }
    public final AnimationState ripperEyes = new AnimationState();
    @Override

    public boolean needsStandUser(){
        return false;
    }
    @Override
    public boolean killAtZero(){
        return false;
    }
}
